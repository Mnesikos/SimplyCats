package com.github.mnesikos.simplycats.entity.npc;

import com.github.mnesikos.simplycats.SimplyCats;
import com.github.mnesikos.simplycats.block.CatnipBlock;
import com.github.mnesikos.simplycats.block.SCBlocks;
import com.github.mnesikos.simplycats.configuration.SCConfig;
import com.github.mnesikos.simplycats.entity.SimplyCatEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.tags.StructureTags;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.SpawnPlacements;
import net.minecraft.world.entity.ai.village.poi.PoiManager;
import net.minecraft.world.entity.ai.village.poi.PoiTypes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.CustomSpawner;
import net.minecraft.world.level.GameRules;
import net.minecraft.world.level.NaturalSpawner;
import net.minecraft.world.phys.AABB;

import java.util.List;

public class SimplyCatSpawner implements CustomSpawner {
    private static final int TICK_DELAY = 1200;
    private int nextTick;

    @Override
    public int tick(ServerLevel level, boolean spawnHostiles, boolean spawnPassives) {
        if (spawnPassives && level.getGameRules().getBoolean(GameRules.RULE_DOMOBSPAWNING)) {
            --nextTick;
            if (nextTick <= 0) {
                nextTick = TICK_DELAY;
                Player player = level.getRandomPlayer();
                if (player != null) {
                    RandomSource random = level.random;
                    int xOffset = (8 + random.nextInt(24)) * (random.nextBoolean() ? -1 : 1);
                    int zOffset = (8 + random.nextInt(24)) * (random.nextBoolean() ? -1 : 1);
                    BlockPos blockPos = player.blockPosition().offset(xOffset, 0, zOffset);
                    int chunkRadius = 10;
                    if (level.hasChunksAt(blockPos.getX() - chunkRadius, blockPos.getZ() - chunkRadius, blockPos.getX() + chunkRadius, blockPos.getZ() + chunkRadius)) {
                        if (NaturalSpawner.isSpawnPositionOk(SpawnPlacements.Type.ON_GROUND, level, blockPos, SimplyCats.CAT.get())) {
                            if (level.isCloseToVillage(blockPos, 2))
                                return spawnInVillage(level, blockPos);

                            if (level.structureManager().getStructureWithPieceAt(blockPos, StructureTags.CATS_SPAWN_IN).isValid())
                                return spawnOneCatWithinRadius(level, blockPos, 16);
                        }

                        if (level.getBlockState(blockPos).is(SCBlocks.CATNIP_CROP.get()) && level.getBlockState(blockPos).getValue(CatnipBlock.AGE) == 3) {
                            int horizontalRange = 4;
                            for (int h = 0; h < horizontalRange; ++h) {
                                for (int x = 0; x <= h; x = x > 0 ? -x : 1 - x) {
                                    for (int z = x < h && x > -h ? h : 0; z <= h; z = z > 0 ? -z : 1 - z) {
                                        BlockPos blockPos2 = blockPos.offset(x, 0, z);
                                        if (NaturalSpawner.isSpawnPositionOk(SpawnPlacements.Type.ON_GROUND, level, blockPos2, SimplyCats.CAT.get()))
                                            return spawnOneCatWithinRadius(level, blockPos2, 48);
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
        return 0;
    }

    private int spawnInVillage(ServerLevel level, BlockPos blockPos) {
        int radius = 48;
        if (level.getPoiManager().getCountInRange((poiTypeHolder) ->
                poiTypeHolder.is(PoiTypes.HOME), blockPos, radius, PoiManager.Occupancy.IS_OCCUPIED) > 4L) {
            List<SimplyCatEntity> list = level.getEntitiesOfClass(SimplyCatEntity.class, new AABB(blockPos).inflate(radius, 8.0D, radius));

            if (list.size() < 3) return spawnCat(blockPos, level);
        }

        return 0;
    }

    private int spawnOneCatWithinRadius(ServerLevel level, BlockPos blockPos, int radius) {
        List<SimplyCatEntity> list = level.getEntitiesOfClass(SimplyCatEntity.class, new AABB(blockPos).inflate(radius, 8.0D, radius));
        return list.isEmpty() ? spawnCat(blockPos, level) : 0;
    }

    private int spawnCat(BlockPos blockPos, ServerLevel level) {
        SimplyCatEntity cat = SimplyCats.CAT.get().create(level);
        if (cat == null) return 0;
        else {
            cat.moveTo(blockPos, 0.0F, 0.0F);
            cat.finalizeSpawn(level, level.getCurrentDifficultyAt(blockPos), MobSpawnType.NATURAL, null, null);
            level.addFreshEntityWithPassengers(cat);
            if (cat.getRandom().nextFloat() < 0.9F || !SCConfig.intact_stray_spawns.get()) cat.setFixed(true);
            if (cat.getRandom().nextFloat() < 0.1F) {
                int age = cat.getRandom().nextInt(SCConfig.kitten_mature_timer.get());
                cat.setAge(-age);
                cat.setMatureTimer((float) age);
            }
            cat.setHomePos(blockPos);
            return 1;
        }
    }
}
