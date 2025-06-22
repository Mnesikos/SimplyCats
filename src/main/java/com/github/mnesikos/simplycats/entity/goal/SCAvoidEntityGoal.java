package com.github.mnesikos.simplycats.entity.goal;

import com.github.mnesikos.simplycats.entity.SimplyCatEntity;
import net.minecraft.world.entity.EntitySelector;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.goal.AvoidEntityGoal;

public class SCAvoidEntityGoal<T extends LivingEntity> extends AvoidEntityGoal<T> {
    private final SimplyCatEntity cat;

    public SCAvoidEntityGoal(SimplyCatEntity cat, Class<T> p_25028_, float p_25029_, double p_25030_, double p_25031_) {
        super(cat, p_25028_, p_25029_, p_25030_, p_25031_, EntitySelector.NO_CREATIVE_OR_SPECTATOR::test);
        this.cat = cat;
    }

    @Override
    public boolean canUse() {
        return !cat.isTame() && super.canUse();
    }

    @Override
    public boolean canContinueToUse() {
        return !cat.isTame() && super.canContinueToUse();
    }
}
