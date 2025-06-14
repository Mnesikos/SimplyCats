package com.github.mnesikos.simplycats.entity.goal;

import com.github.mnesikos.simplycats.configuration.SCConfig;
import com.github.mnesikos.simplycats.entity.SimplyCatEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.ai.goal.WaterAvoidingRandomStrollGoal;
import net.minecraft.world.entity.ai.util.DefaultRandomPos;
import net.minecraft.world.entity.ai.util.LandRandomPos;
import net.minecraft.world.entity.ai.util.RandomPos;
import net.minecraft.world.entity.npc.Villager;
import net.minecraft.world.phys.Vec3;

import javax.annotation.Nullable;
import java.util.List;

public class CatWanderGoal extends WaterAvoidingRandomStrollGoal {
    protected final SimplyCatEntity cat;

    public CatWanderGoal(SimplyCatEntity cat, double speed, float probability) {
        super(cat, speed, probability);
        this.cat = cat;
    }

    @Override
    public boolean canUse() {
        return super.canUse();
    }

    @Nullable
    protected Vec3 getPosition() {
        boolean outsideBounds = cat.getHomePos() != null && !cat.getHomePos().closerToCenterThan(cat.position(), SCConfig.wander_area_limit.get());
        Vec3 defaultPos = DefaultRandomPos.getPos(cat, 10, 7);
        Vec3 towardsHomePos = LandRandomPos.getPosTowards(cat, 10, 7, cat.getHomePos().getCenter());
        if (cat.isInWaterOrBubble()) {
            Vec3 landPos = LandRandomPos.getPos(cat, 15, 7);
            return landPos == null ? defaultPos : landPos;
        }

        Vec3 pos = null;
        if (outsideBounds)
            return towardsHomePos == null ? defaultPos : towardsHomePos;
        else if (cat.getRandom().nextFloat() >= probability) {
            pos = LandRandomPos.getPos(cat, 10, 7);

            if (pos != null && cat.getHomePos() != null && !cat.getHomePos().closerToCenterThan(pos, SCConfig.wander_area_limit.get()))
                pos = LandRandomPos.getPosTowards(cat, 10, 7, cat.getHomePos().getCenter());
        }

        return pos == null ? defaultPos : pos;
    }
}
