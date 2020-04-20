package com.github.mnesikos.simplycats.entity.ai;

import com.github.mnesikos.simplycats.configuration.SCConfig;
import com.github.mnesikos.simplycats.entity.EntityCat;
import com.google.common.base.Predicate;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.ai.EntityAINearestAttackableTarget;
import net.minecraft.world.EnumDifficulty;

public class CatAITargetNearest<T extends EntityLivingBase> extends EntityAINearestAttackableTarget<T> {
    private EntityCat cat;

    public CatAITargetNearest(EntityCat entityIn, Class<T> classTarget, boolean checkSight, Predicate<? super T> targetSelector) {
        super(entityIn, classTarget, 10, checkSight, false, targetSelector);
        this.cat = entityIn;
    }

    @Override
    public boolean shouldExecute() {
        if (this.cat.world.getDifficulty() == EnumDifficulty.PEACEFUL || !SCConfig.ATTACK_AI)
            return false;

        return super.shouldExecute() && this.targetEntity != null && !this.targetEntity.getClass().equals(this.cat.getClass());
    }
}
