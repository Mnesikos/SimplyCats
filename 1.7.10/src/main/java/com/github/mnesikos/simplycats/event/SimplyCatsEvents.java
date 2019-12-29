package com.github.mnesikos.simplycats.event;

import com.github.mnesikos.simplycats.entity.AbstractCat;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.ai.EntityAIAvoidEntity;
import net.minecraft.entity.monster.EntityCreeper;
import net.minecraft.entity.monster.EntityWitch;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;
import net.minecraftforge.event.entity.living.LivingSetAttackTargetEvent;

public class SimplyCatsEvents {

    @SubscribeEvent
    public void onEntityJoinWorldEvent(EntityJoinWorldEvent event) {
        if (event.entity instanceof EntityCreeper) {
            EntityCreeper creeper = (EntityCreeper) event.entity;
            creeper.tasks.addTask(3, new EntityAIAvoidEntity(creeper, AbstractCat.class, 6.0F, 1.0D, 1.2D));
        }
    }

    @SubscribeEvent
    public void onLivingSetAttackTargetEvent(LivingSetAttackTargetEvent event) {
        if (event.target != null && event.entityLiving instanceof EntityLiving) {
            EntityLiving attackingEntity = (EntityLiving) event.entityLiving;
            if (attackingEntity instanceof EntityWitch) {
                if (!attackingEntity.worldObj.getEntitiesWithinAABB(AbstractCat.class, attackingEntity.getBoundingBox().expand(16.0D, 16.0D, 16.0D)).isEmpty()) {
                    attackingEntity.setAttackTarget(null);
                }
            }
        }
    }
}
