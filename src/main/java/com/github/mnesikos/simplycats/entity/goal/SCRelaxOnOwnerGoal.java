package com.github.mnesikos.simplycats.entity.goal;

import com.github.mnesikos.simplycats.entity.SimplyCatEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.tags.BlockTags;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.BedBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.storage.loot.BuiltInLootTables;
import net.minecraft.world.level.storage.loot.LootParams;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSets;
import net.minecraft.world.level.storage.loot.parameters.LootContextParams;
import net.minecraft.world.phys.AABB;

import javax.annotation.Nullable;

public class SCRelaxOnOwnerGoal extends Goal {
    private final SimplyCatEntity cat;
    @Nullable
    private Player owner;
    @Nullable
    private BlockPos goalPos;
    private int onBedTicks;

    public SCRelaxOnOwnerGoal(SimplyCatEntity cat) {
        this.cat = cat;
    }

    @Override
    public boolean canUse() {
        if (!cat.isTame()) return false;
        else if (cat.isOrderedToSit()) return false;
        else {
            LivingEntity living = cat.getOwner();
            if (living instanceof Player) {
                owner = (Player) living;
                if (!living.isSleeping()) return false;

                if (cat.distanceToSqr(owner) > 12.0D) return false;

                BlockPos blockpos = owner.blockPosition();
                BlockState blockstate = cat.level().getBlockState(blockpos);
                if (blockstate.is(BlockTags.BEDS)) {
                    goalPos = blockstate.getOptionalValue(BedBlock.FACING).map((direction) -> blockpos.relative(direction.getOpposite())).orElseGet(() -> new BlockPos(blockpos));
                    return !spaceIsOccupied();
                }
            }

            return false;
        }
    }

    private boolean spaceIsOccupied() {
        for (SimplyCatEntity catCheck : cat.level().getEntitiesOfClass(SimplyCatEntity.class, new AABB(goalPos).inflate(2.0D))) {
            if (catCheck != cat && catCheck.isResting()) return true;
        }

        return false;
    }

    @Override
    public boolean canContinueToUse() {
        return cat.isTame() && !cat.isOrderedToSit() && owner != null && owner.isSleeping() && goalPos != null && !spaceIsOccupied();
    }

    @Override
    public void start() {
        if (goalPos != null) {
            cat.setInSittingPose(false);
            cat.getNavigation().moveTo(goalPos.getX(), goalPos.getY(), goalPos.getZ(), 1.1F);
        }
    }

    @Override
    public void stop() {
        cat.setRestingState(SimplyCatEntity.RestingState.AWAKE.ordinal());
        float timeOfDay = cat.level().getTimeOfDay(1.0F);
        if (owner.getSleepTimer() >= 100 && (double) timeOfDay > 0.77D && (double) timeOfDay < 0.8D && (double) cat.level().getRandom().nextFloat() < 0.7D)
            giveMorningGift();

        onBedTicks = 0;
        cat.getNavigation().stop();
    }

    private void giveMorningGift() {
        RandomSource random = cat.getRandom();
        BlockPos.MutableBlockPos mutableBlockPos = new BlockPos.MutableBlockPos();
        mutableBlockPos.set(cat.isLeashed() ? cat.getLeashHolder().blockPosition() : cat.blockPosition());
        cat.randomTeleport((mutableBlockPos.getX() + random.nextInt(11) - 5), (mutableBlockPos.getY() + random.nextInt(5) - 2), (mutableBlockPos.getZ() + random.nextInt(11) - 5), false);
        mutableBlockPos.set(cat.blockPosition());
        LootTable lootTable = cat.level().getServer().reloadableRegistries().getLootTable(BuiltInLootTables.CAT_MORNING_GIFT);
        LootParams lootParams = new LootParams.Builder((ServerLevel) cat.level()).withParameter(LootContextParams.ORIGIN, cat.position()).withParameter(LootContextParams.THIS_ENTITY, cat).create(LootContextParamSets.GIFT);

        for (ItemStack itemStack : lootTable.getRandomItems(lootParams)) {
            cat.level().addFreshEntity(new ItemEntity(cat.level(), (double) mutableBlockPos.getX() - (double) Mth.sin(cat.yBodyRot * ((float) Math.PI / 180F)), mutableBlockPos.getY(), (double) mutableBlockPos.getZ() + (double) Mth.cos(cat.yBodyRot * ((float) Math.PI / 180F)), itemStack));
        }

    }

    @Override
    public void tick() {
        if (owner != null && goalPos != null) {
            cat.setInSittingPose(false);
            cat.getNavigation().moveTo(goalPos.getX(), goalPos.getY(), goalPos.getZ(), 1.1F);
            if (cat.distanceToSqr(owner) < 2.5D) {
                ++onBedTicks;
                if (onBedTicks > adjustedTickDelay(16) && !cat.isResting())
                    cat.setRestingState(cat.getRandom().nextInt(3) + 1);
                else cat.lookAt(owner, 45.0F, 45.0F);

            } else cat.setRestingState(SimplyCatEntity.RestingState.AWAKE.ordinal());
        }
    }
}
