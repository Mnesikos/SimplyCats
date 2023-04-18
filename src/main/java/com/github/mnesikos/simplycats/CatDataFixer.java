package com.github.mnesikos.simplycats;

import com.github.mnesikos.simplycats.configuration.SCConfig;
import com.github.mnesikos.simplycats.entity.SimplyCatEntity;
import net.minecraft.entity.ai.attributes.Attributes;
import net.minecraft.entity.passive.CatEntity;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

public class CatDataFixer {
    @SubscribeEvent
    public static void joinWorldEvent(EntityJoinWorldEvent event) {
        if (event.getEntity().getClass() == CatEntity.class && !event.getWorld().isClientSide) {
            CatEntity vanillaCat = (CatEntity) event.getEntity();
            if (!vanillaCat.getPersistentData().contains("SimplyCatsSpawn")) {
                if (vanillaCat.isTame() && SCConfig.replace_tamed_vanilla.get()) {
                    World world = event.getWorld();
                    SimplyCatEntity simplyCatEntity = SimplyCats.CAT.get().create(world);
                    simplyCatEntity.load(vanillaCat.saveWithoutId(new CompoundNBT()));
                    simplyCatEntity.getAttribute(Attributes.MAX_HEALTH).setBaseValue(14.0D);
                    simplyCatEntity.getAttribute(Attributes.MOVEMENT_SPEED).setBaseValue(0.3D);
                    simplyCatEntity.getAttribute(Attributes.KNOCKBACK_RESISTANCE).setBaseValue(0.7D);
                    simplyCatEntity.getAttribute(Attributes.ATTACK_DAMAGE).setBaseValue(2.0D);
                    simplyCatEntity.heal(4.0F);
                    simplyCatEntity.setHomePos(simplyCatEntity.blockPosition());
                    simplyCatEntity.getGenome().randomize();
//                    if (!vanillaCat.isTame() && !SCConfig.intact_stray_spawns.get()) simplyCatEntity.setFixed((byte) 1);
                    if (simplyCatEntity.getSex() == SimplyCatEntity.Sex.FEMALE && !simplyCatEntity.isFixed())
                        simplyCatEntity.setTimeCycle("end", simplyCatEntity.getRandom().nextInt(SCConfig.heat_cooldown.get()));

                    if (world instanceof ServerWorld) ((ServerWorld) world).loadFromChunk(simplyCatEntity);
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
