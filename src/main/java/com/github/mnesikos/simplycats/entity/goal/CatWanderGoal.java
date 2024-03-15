package com.github.mnesikos.simplycats.entity.goal;

import com.github.mnesikos.simplycats.entity.SimplyCatEntity;
import net.minecraft.world.entity.ai.goal.WaterAvoidingRandomStrollGoal;
import net.minecraft.world.phys.Vec3;

import javax.annotation.Nullable;

public class CatWanderGoal extends WaterAvoidingRandomStrollGoal {
    public CatWanderGoal(SimplyCatEntity cat, double speed, float probability) {
        super(cat, speed, probability);
    }

    @Override
    public boolean canUse() {
        return super.canUse();
    }

    @Nullable
    protected Vec3 getPosition() {
        return super.getPosition(); // todo home point check
    }
}
