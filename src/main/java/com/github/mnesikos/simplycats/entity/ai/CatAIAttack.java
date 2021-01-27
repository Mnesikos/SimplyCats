package com.github.mnesikos.simplycats.entity.ai;

import net.minecraft.entity.CreatureEntity;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.goal.Goal;
import net.minecraft.world.World;

import java.util.EnumSet;

public class CatAIAttack extends Goal {
    World world;
    CreatureEntity cat;
    LivingEntity target;
    int attackCountdown;

    public CatAIAttack(CreatureEntity cat) {
        this.cat = cat;
        this.world = cat.world;
        this.setMutexFlags(EnumSet.of(Goal.Flag.MOVE, Goal.Flag.LOOK));
    }

    @Override
    public boolean shouldExecute() {
        LivingEntity attackTarget = this.cat.getAttackTarget();

        if (attackTarget == null)
            return false;
        else
            this.target = attackTarget;
        return true;
    }

    @Override
    public boolean shouldContinueExecuting() {
        if (!this.target.isAlive())
            return false;
        else if (this.cat.getDistanceSq(this.target) > 225.0D)
            return false;
        else
            return !this.cat.getNavigator().noPath() || this.shouldExecute();
    }

    @Override
    public void resetTask() {
        this.target = null;
        this.cat.getNavigator().clearPath();
    }

    @Override
    public void tick() {
        this.cat.getLookController().setLookPositionWithEntity(this.target, 30.0F, 30.0F);
        double d0 = this.cat.getWidth() * 2.0F * this.cat.getWidth() * 2.0F;
        double distanceSq = this.cat.getDistanceSq(this.target.getPosX(), this.target.getBoundingBox().minY, this.target.getPosZ());
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
        return target.getPosX() != target.prevPosX && target.getPosZ() != target.prevPosZ;
    }
}
