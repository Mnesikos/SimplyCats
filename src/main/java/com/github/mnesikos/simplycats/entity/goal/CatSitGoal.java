package com.github.mnesikos.simplycats.entity.goal;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.goal.SitGoal;
import net.minecraft.entity.passive.TameableEntity;

public class CatSitGoal extends SitGoal {
    private final TameableEntity tameable;
    private boolean isSitting;

    public CatSitGoal(TameableEntity entity) {
        super(entity);
        this.tameable = entity;
    }

    @Override
    public boolean canUse() {
        if (!this.tameable.isTame())
            return false;
        else if (this.tameable.isInWater())
            return false;
        else if (!this.tameable.isOnGround())
            return false;
        else {
            LivingEntity owner = this.tameable.getOwner();
            if (owner == null)
                return true;
            else
                return this.tameable.isOrderedToSit();
        }
    }
}
