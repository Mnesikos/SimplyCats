package com.github.mnesikos.simplycats.entity.goal;

import com.github.mnesikos.simplycats.configuration.SCConfig;
import com.github.mnesikos.simplycats.entity.SimplyCatEntity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.goal.NearestAttackableTargetGoal;
import net.minecraft.world.Difficulty;

import java.util.function.Predicate;

public class CatTargetNearestGoal<T extends LivingEntity> extends NearestAttackableTargetGoal<T> {
    private SimplyCatEntity cat;

    public CatTargetNearestGoal(SimplyCatEntity entityIn, Class<T> classTarget, boolean checkSight, Predicate<LivingEntity> targetSelector) {
        super(entityIn, classTarget, 10, checkSight, false, targetSelector);
        this.cat = entityIn;
    }

    @Override
    public boolean canUse() {
        if (this.cat.level.getDifficulty() == Difficulty.PEACEFUL || !SCConfig.ATTACK_AI)
            return false;

        return super.canUse() && this.target != null && !this.target.getClass().equals(this.cat.getClass());
    }
}
