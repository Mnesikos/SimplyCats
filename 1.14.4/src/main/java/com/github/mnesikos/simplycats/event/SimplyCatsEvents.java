package com.github.mnesikos.simplycats.event;

import com.github.mnesikos.simplycats.entity.AbstractCat;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.goal.AvoidEntityGoal;
import net.minecraft.entity.monster.CreeperEntity;
import net.minecraft.entity.monster.WitchEntity;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;
import net.minecraftforge.event.entity.living.LivingSetAttackTargetEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

public class SimplyCatsEvents {

    @SubscribeEvent
    public void onEntityJoinWorldEvent(EntityJoinWorldEvent event) {
        if (event.getEntity() instanceof CreeperEntity) {
            CreeperEntity creeper = (CreeperEntity) event.getEntity();
            creeper.goalSelector.addGoal(3, new AvoidEntityGoal<AbstractCat>(creeper, AbstractCat.class, 6.0F, 1.0D, 1.2D));
        }
    }

    @SubscribeEvent
    public void onLivingSetAttackTargetEvent(LivingSetAttackTargetEvent event) {
        if (event.getTarget() != null && event.getEntityLiving() instanceof LivingEntity) {
            LivingEntity attackingEntity = (LivingEntity) event.getEntityLiving();
            if (attackingEntity instanceof WitchEntity) {
                if (!attackingEntity.world.getEntitiesWithinAABB(AbstractCat.class, attackingEntity.getBoundingBox().grow(16.0F)).isEmpty()) {
                    ((WitchEntity) attackingEntity).setAttackTarget(null);
                }
            }
        }
    }
}
