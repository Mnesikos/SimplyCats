package com.github.mnesikos.simplycats.entity.goal;

import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.MobEntity;
import net.minecraft.entity.ai.goal.Goal;
import net.minecraft.world.IBlockReader;

import java.util.EnumSet;

public class CatAttackGoal extends Goal {
    private final IBlockReader level;
    private final MobEntity cat;
    private LivingEntity target;
    private int attackCountdown;

    public CatAttackGoal(MobEntity cat) {
        this.cat = cat;
        this.level = cat.level;
        this.setFlags(EnumSet.of(Goal.Flag.MOVE, Flag.LOOK));
    }

    @Override
    public boolean canUse() {
        LivingEntity livingEntity = this.cat.getTarget();

        if (livingEntity == null)
            return false;
        else
            this.target = livingEntity;
        return true;
    }

    @Override
    public boolean canContinueToUse() {
        if (!this.target.isAlive())
            return false;
        else if (this.cat.distanceToSqr(this.target) > 225.0D)
            return false;
        else
            return !this.cat.getNavigation().isDone() || this.canUse();
    }

    @Override
    public void stop() {
        this.target = null;
        this.cat.getNavigation().stop();
    }

    @Override
    public void tick() {
        this.cat.getLookControl().setLookAt(this.target, 30.0F, 30.0F);
        double d0 = (double) (this.cat.getBbWidth() * 2.0F * this.cat.getBbWidth() * 2.0F);
        double distanceSq = this.cat.distanceToSqr(this.target.getX(), this.target.getY(), this.target.getZ());
        double speed = 0.8D;

        if ((distanceSq > d0 && distanceSq < 16.0D) || isEntityMoving(target))
            speed = 1.33D;
        else if (distanceSq < 255.0D || !isEntityMoving(target))
            speed = 0.6D;

        this.cat.getNavigation().moveTo(this.target, speed);
        this.attackCountdown = Math.max(this.attackCountdown - 1, 0);
        if (!(distanceSq > d0)) {
            if (this.attackCountdown <= 0) {
                this.attackCountdown = 20;
                this.cat.doHurtTarget(this.target);
            }
        }
    }

    public static boolean isEntityMoving(Entity target) {
        return target.getX() != target.xOld && target.getZ() != target.zOld;
    }
}
