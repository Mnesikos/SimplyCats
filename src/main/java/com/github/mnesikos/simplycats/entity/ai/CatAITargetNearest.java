package com.github.mnesikos.simplycats.entity.ai;

import com.github.mnesikos.simplycats.configuration.SimplyCatsConfig;
import com.github.mnesikos.simplycats.entity.EntityCat;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.goal.NearestAttackableTargetGoal;
import net.minecraft.world.Difficulty;

import javax.annotation.Nullable;
import java.util.function.Predicate;

public class CatAITargetNearest<T extends LivingEntity> extends NearestAttackableTargetGoal<T> {
    private final EntityCat cat;

    public CatAITargetNearest(EntityCat entityIn, Class<T> targetClassIn, boolean checkSight, @Nullable Predicate<LivingEntity> targetPredicate) {
        super(entityIn, targetClassIn, 10, checkSight, false, targetPredicate);
        this.cat = entityIn;
    }

    @Override
    public boolean shouldExecute() {
        if (this.cat.world.getDifficulty() == Difficulty.PEACEFUL || !SimplyCatsConfig.ATTACK_AI.get())
            return false;

        return super.shouldExecute() && this.nearestTarget != null && !this.nearestTarget.getClass().equals(this.cat.getClass());
    }
}
