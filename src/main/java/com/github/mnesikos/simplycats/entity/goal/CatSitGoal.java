package com.github.mnesikos.simplycats.entity.goal;

import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.goal.SitWhenOrderedToGoal;
import net.minecraft.world.entity.TamableAnimal;

public class CatSitGoal extends SitWhenOrderedToGoal {
    private final TamableAnimal tameable;
    private boolean isSitting;

    public CatSitGoal(TamableAnimal entity) {
        super(entity);
        this.tameable = entity;
    }

    @Override
    public boolean canUse() {
        if (!this.tameable.isTame())
            return false;
        else if (this.tameable.isInWater())
            return false;
        else if (!this.tameable.onGround())
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
