package com.github.mnesikos.simplycats.entity.goal;

import com.github.mnesikos.simplycats.configuration.SCConfig;
import com.github.mnesikos.simplycats.entity.SimplyCatEntity;
import net.minecraft.entity.ai.goal.Goal;
import net.minecraft.pathfinding.PathNavigator;
import net.minecraft.pathfinding.PathNodeType;
import net.minecraft.pathfinding.WalkNodeProcessor;
import net.minecraft.tags.FluidTags;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.vector.Vector3d;

import javax.annotation.Nullable;
import java.util.EnumSet;
import java.util.Random;
import java.util.function.Predicate;

public class CatWanderGoal extends Goal {
    protected final SimplyCatEntity cat;
    protected double x;
    protected double y;
    protected double z;
    protected final double speed;
    protected int interval;

    public CatWanderGoal(SimplyCatEntity cat, double speed) {
        this.cat = cat;
        this.speed = speed;
        this.interval = 45; // todo activeness level should effect this
        this.setFlags(EnumSet.of(Goal.Flag.MOVE));
    }

    @Override
    public boolean canUse() {
        if (this.cat.getNoActionTime() >= 100)
            return false;
        else if (this.cat.getRandom().nextInt(this.interval) != 0)
            return false;

        Vector3d vector3d = this.getPosition();

        if (vector3d == null)
            return false;
        else {
            this.x = vector3d.x;
            this.y = vector3d.y;
            this.z = vector3d.z;
            return true;
        }
    }

    @Nullable
    private Vector3d getPosition() {
        PathNavigator pathNavigator = cat.getNavigation();
        Random random = cat.getRandom();
        boolean outsideBounds;
        int xzRange = 10;
        int yRange = 3;

        if (cat.getHomePos().isPresent()) {
            double homePosDist = cat.getHomePos().get().distSqr(MathHelper.floor(cat.getX()), MathHelper.floor(cat.getY()), MathHelper.floor(cat.getZ()), true) + 4.0D;
            double wanderRange = (SCConfig.Common.wander_area_limit.get() + (float) xzRange);
            outsideBounds = homePosDist < wanderRange * wanderRange;
        } else
            outsideBounds = false;

        boolean flag1 = false;
        double d0 = Double.NEGATIVE_INFINITY;
        BlockPos blockPos = cat.blockPosition();

        for (int i = 0; i < 10; ++i) {
            int j = random.nextInt(2 * xzRange + 1) - xzRange;
            int k = random.nextInt(2 * yRange + 1) - yRange;
            int l = random.nextInt(2 * xzRange + 1) - xzRange;

            if (cat.getHomePos().isPresent()) {
                BlockPos blockPos2 = cat.getHomePos().get();

                if (cat.getX() > (double) blockPos2.getX())
                    j -= random.nextInt(xzRange / 2);
                else
                    j += random.nextInt(xzRange / 2);

                if (cat.getZ() > (double) blockPos2.getZ())
                    l -= random.nextInt(xzRange / 2);
                else
                    l += random.nextInt(xzRange / 2);
            }

            BlockPos blockPos3 = new BlockPos((double) j + cat.getX(), (double) k + cat.getY(), (double) l + cat.getZ());

            if (blockPos3.getY() >= 0 && blockPos3.getY() <= cat.level.getMaxBuildHeight() && (!outsideBounds || (cat.getHomePos().get().distSqr(blockPos3) < (SCConfig.Common.wander_area_limit.get() * SCConfig.Common.wander_area_limit.get()))) && pathNavigator.isStableDestination(blockPos3)) {
                blockPos3 = moveAboveSolid(blockPos3, cat.level.getMaxBuildHeight(), (block) -> cat.level.getBlockState(block).getMaterial().isSolid());

                if (!cat.level.getFluidState(blockPos3).is(FluidTags.WATER)) {
                    PathNodeType pathNodeType = WalkNodeProcessor.getBlockPathTypeStatic(cat.level, blockPos3.mutable());
                    if (cat.getPathfindingMalus(pathNodeType) == 0.0F) {
                        float d1 = cat.getWalkTargetValue(blockPos3);
                        if (d1 > d0) {
                            d0 = d1;
                            blockPos = blockPos3;
                            flag1 = true;
                        }
                    }
                }
            }
        }

        return flag1 ? Vector3d.atBottomCenterOf(blockPos) : null;
    }

    @Override
    public boolean canContinueToUse() {
        return !this.cat.getNavigation().isDone();
    }

    @Override
    public void start() {
        this.cat.getNavigation().moveTo(this.x, this.y, this.z, this.speed);
    }

    @Override
    public void stop() {
        this.cat.getNavigation().stop();
    }

    private static BlockPos moveAboveSolid(BlockPos pos, int maxBuildHeight, Predicate<BlockPos> isSolid) {
        if (!isSolid.test(pos))
            return pos;
        else {
            BlockPos blockPos;
            for (blockPos = pos.above(); blockPos.getY() < maxBuildHeight && isSolid.test(blockPos); blockPos = blockPos.above()) {
            }

            return blockPos;
        }
    }
}
