package com.github.mnesikos.simplycats.entity.ai;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.ai.EntityAIBase;
import net.minecraft.entity.ai.EntityAISit;
import net.minecraft.entity.passive.EntityTameable;

public class CatAISit extends EntityAISit {
    private final EntityTameable tameable;
    private boolean isSitting;

    public CatAISit(EntityTameable entity) {
        super(entity);
        this.tameable = entity;
    }

    @Override
    public boolean shouldExecute() {
        if (!this.tameable.isTamed())
            return false;
        else if (this.tameable.isInWater())
            return false;
        else if (!this.tameable.onGround)
            return false;
        else {
            EntityLivingBase owner = this.tameable.getOwner();
            if (owner == null)
                return true;
            else
                return this.isSitting;
        }
    }

    @Override
    public void setSitting(boolean sitting) {
        this.isSitting = sitting;
    }
}
