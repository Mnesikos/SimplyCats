package com.github.mnesikos.simplycats;

import com.github.mnesikos.simplycats.configuration.SCConfig;
import com.github.mnesikos.simplycats.entity.SimplyCatEntity;
import com.github.mnesikos.simplycats.entity.core.Genetics;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.animal.Cat;
import net.minecraft.world.level.Level;
import net.minecraftforge.event.entity.EntityJoinLevelEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

import java.util.stream.Stream;

public class CatDataFixer {
    @SubscribeEvent
    public static void joinWorldEvent(EntityJoinLevelEvent event) {
        if (event.getEntity().getClass() == Cat.class && !event.getLevel().isClientSide) {
            Cat vanillaCat = (Cat) event.getEntity();
            if (!vanillaCat.getPersistentData().contains("SimplyCatsSpawn")) {
                if (vanillaCat.isTame() && SCConfig.replace_tamed_vanilla.get()) {
                    Level world = event.getLevel();
                    SimplyCatEntity simplyCatEntity = SimplyCats.CAT.get().create(world);
                    simplyCatEntity.load(vanillaCat.saveWithoutId(new CompoundTag()));
                    simplyCatEntity.getAttribute(Attributes.MAX_HEALTH).setBaseValue(14.0D);
                    simplyCatEntity.getAttribute(Attributes.MOVEMENT_SPEED).setBaseValue(0.3D);
                    simplyCatEntity.getAttribute(Attributes.KNOCKBACK_RESISTANCE).setBaseValue(0.7D);
                    simplyCatEntity.getAttribute(Attributes.ATTACK_DAMAGE).setBaseValue(2.0D);
                    simplyCatEntity.heal(4.0F);
                    simplyCatEntity.setHomePos(simplyCatEntity.blockPosition());
                    simplyCatEntity.setPhenotype();
//                    if (!vanillaCat.isTame() && !SCConfig.intact_stray_spawns.get()) simplyCatEntity.setFixed((byte) 1);
                    if (simplyCatEntity.getSex() == Genetics.Sex.FEMALE && !simplyCatEntity.isFixed())
                        simplyCatEntity.setTimeCycle("end", simplyCatEntity.getRandom().nextInt(SCConfig.heat_cooldown.get()));

                    if (world instanceof ServerLevel)
                        ((ServerLevel) world).addWorldGenChunkEntities(Stream.of(simplyCatEntity));
                    vanillaCat.getPersistentData().putBoolean("SimplyCatsSpawn", true);
                    event.setCanceled(true);

                } else if (!vanillaCat.isTame() && SCConfig.stop_vanilla_spawns.get()) {
                    vanillaCat.getPersistentData().putBoolean("SimplyCatsSpawn", true);
                    event.setCanceled(true);
                }
            }
        }
    }
}
