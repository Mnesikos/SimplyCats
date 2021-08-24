package com.github.mnesikos.simplycats.entity.ai;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.ai.EntityAIBase;
import net.minecraft.world.World;

public class CatAIAttack extends EntityAIBase {
    World world;
    EntityLiving cat;
    EntityLivingBase target;
    int attackCountdown;

    public CatAIAttack(EntityLiving cat) {
        this.cat = cat;
        this.world = cat.world;
        this.setMutexBits(3);
    }

    public boolean shouldExecute() {
        EntityLivingBase entitylivingbase = this.cat.getAttackTarget();

        if (entitylivingbase == null)
            return false;
        else
            this.target = entitylivingbase;
        return true;
    }

    public boolean shouldContinueExecuting() {
        if (!this.target.isEntityAlive())
            return false;
        else if (this.cat.getDistanceSq(this.target) > 225.0D)
            return false;
        else
            return !this.cat.getNavigator().noPath() || this.shouldExecute();
    }

    public void resetTask() {
        this.target = null;
        this.cat.getNavigator().clearPath();
    }

    public void updateTask() {
        this.cat.getLookHelper().setLookPositionWithEntity(this.target, 30.0F, 30.0F);
        double d0 = (double) (this.cat.width * 2.0F * this.cat.width * 2.0F);
        double distanceSq = this.cat.getDistanceSq(this.target.posX, this.target.getEntityBoundingBox().minY, this.target.posZ);
        double speed = 0.8D;

        if ((distanceSq > d0 && distanceSq < 16.0D) || isEntityMoving(target))
            speed = 1.33D;
        else if (distanceSq < 225.0D || !isEntityMoving(target))
            speed = 0.6D;

        this.cat.getNavigator().tryMoveToEntityLiving(this.target, speed);
        this.attackCountdown = Math.max(this.attackCountdown - 1, 0);

        if (distanceSq <= d0) {
            if (this.attackCountdown <= 0) {
                this.attackCountdown = 20;
                this.cat.attackEntityAsMob(this.target);
            }
        }
    }

    public static boolean isEntityMoving(Entity target) {
        return target.posX != target.prevPosX && target.posZ != target.prevPosZ;
    }
}
