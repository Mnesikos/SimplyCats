package com.github.mnesikos.simplycats.event;

import com.github.mnesikos.simplycats.entity.AbstractCat;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.ai.EntityAIAvoidEntity;
import net.minecraft.entity.monster.EntityCreeper;
import net.minecraft.entity.monster.EntityWitch;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;
import net.minecraftforge.event.entity.living.LivingSetAttackTargetEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class SimplyCatsEvents {

    @SubscribeEvent
    public void onEntityJoinWorldEvent(EntityJoinWorldEvent event) {
        if (event.getEntity() instanceof EntityCreeper) {
            EntityCreeper creeper = (EntityCreeper) event.getEntity();
            creeper.tasks.addTask(3, new EntityAIAvoidEntity<>(creeper, AbstractCat.class, 6.0F, 1.0D, 1.2D));
        }
    }

    @SubscribeEvent
    public void onLivingSetAttackTargetEvent(LivingSetAttackTargetEvent event) {
        if (event.getTarget() != null && event.getEntityLiving() instanceof EntityLiving) {
            EntityLiving attackingEntity = (EntityLiving) event.getEntityLiving();
            if (attackingEntity instanceof EntityWitch) {
                if (!attackingEntity.world.getEntitiesWithinAABB(AbstractCat.class, attackingEntity.getEntityBoundingBox().grow(16.0F)).isEmpty()) {
                    attackingEntity.setAttackTarget(null);
                }
            }
        }
    }
}
