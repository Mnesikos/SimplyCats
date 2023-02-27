package com.github.mnesikos.simplycats.entity.goal;

import com.github.mnesikos.simplycats.configuration.SCConfig;
import com.github.mnesikos.simplycats.entity.SimplyCatEntity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
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
        if (cat.level.getDifficulty() == Difficulty.PEACEFUL || !SCConfig.attack_ai.get())
            return false;

        return super.canUse() && target != null && !target.getClass().equals(cat.getClass());
    }
}
