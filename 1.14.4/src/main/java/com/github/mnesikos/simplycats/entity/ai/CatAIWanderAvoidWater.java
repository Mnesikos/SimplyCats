package com.github.mnesikos.simplycats.entity.ai;

import com.github.mnesikos.simplycats.entity.EntityCat;
import net.minecraft.entity.ai.RandomPositionGenerator;
import net.minecraft.util.math.Vec3d;

import javax.annotation.Nullable;

public class CatAIWanderAvoidWater extends CatAIWander {
    protected final float probability;

    public CatAIWanderAvoidWater(EntityCat entity, double speed) {
        this(entity, speed, 0.001F);
    }

    public CatAIWanderAvoidWater(EntityCat entity, double speed, float probability) {
        super(entity, speed);
        this.probability = probability;
    }

    @Nullable @Override
    protected Vec3d getPosition() {
        if (this.cat.isInWater()) {
            Vec3d vec3d = RandomPositionGenerator.getLandPos(this.cat, 15, 7);
            return vec3d == null ? super.getPosition() : vec3d;
        } else
            return this.cat.getRNG().nextFloat() >= this.probability ? RandomPositionGenerator.getLandPos(this.cat, 10, 7) : super.getPosition();
    }
}