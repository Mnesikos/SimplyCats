package com.github.mnesikos.simplycats.entity.goal;

import com.github.mnesikos.simplycats.configuration.SCConfig;
import com.github.mnesikos.simplycats.entity.SimplyCatEntity;
import net.minecraft.block.material.Material;
import net.minecraft.entity.CreatureEntity;
import net.minecraft.entity.ai.goal.Goal;
import net.minecraft.pathfinding.PathNavigator;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.vector.Vector3d;

import javax.annotation.Nullable;
import java.util.EnumSet;
import java.util.Random;

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
        PathNavigator pathnavigate = cat.getNavigation();
        Random random = cat.getRandom();
        boolean outsideBounds;

        int xzRange = 10;
        int yRange = 3;

        if (cat.hasHomePos()) {
            double d0 = cat.getHomePos().distanceSq(MathHelper.floor(cat.getX()), MathHelper.floor(cat.getY()), MathHelper.floor(cat.getZ())) + 4.0D;
            double d1 = (SCConfig.WANDER_AREA_LIMIT + (float) xzRange);
            outsideBounds = d0 < d1 * d1;
        } else
            outsideBounds = false;

        boolean flag1 = false;
        float f = -99999.0F;
        int k1 = 0;
        int i = 0;
        int j = 0;

        for (int k = 0; k < 10; ++k) {
            int l = random.nextInt(2 * xzRange + 1) - xzRange;
            int i1 = random.nextInt(2 * yRange + 1) - yRange;
            int j1 = random.nextInt(2 * xzRange + 1) - xzRange;

            if (cat.hasHomePos()) {
                BlockPos blockpos = cat.getHomePos();

                if (cat.getX() > (double) blockpos.getX())
                    l -= random.nextInt(xzRange / 2);
                else
                    l += random.nextInt(xzRange / 2);

                if (cat.getZ() > (double) blockpos.getZ())
                    j1 -= random.nextInt(xzRange / 2);
                else
                    j1 += random.nextInt(xzRange / 2);
            }

            BlockPos blockpos1 = new BlockPos((double) l + cat.getX(), (double) i1 + cat.getY(), (double) j1 + cat.getZ());

            if ((!outsideBounds || (cat.getHomePos().distanceSq(blockpos1) < (SCConfig.WANDER_AREA_LIMIT * SCConfig.WANDER_AREA_LIMIT))) && pathnavigate.isStableDestination(blockpos1)) {
                blockpos1 = moveAboveSolid(blockpos1, cat);

                if (isWaterDestination(blockpos1, cat))
                    continue;

                float f1 = cat.getBlockPathWeight(blockpos1);

                if (f1 > f) {
                    f = f1;
                    k1 = l;
                    i = i1;
                    j = j1;
                    flag1 = true;
                }
            }
        }

        if (flag1)
            return new Vector3d((double) k1 + cat.getX(), (double) i + cat.getY(), (double) j + cat.getZ());
        else
            return null;
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

    private static BlockPos moveAboveSolid(BlockPos blockPos, CreatureEntity creatureEntity) {
        if (!creatureEntity.level.getBlockState(blockPos).getMaterial().isSolid())
            return blockPos;
        else {
            BlockPos blockpos;

            for (blockpos = blockPos.above(); blockpos.getY() < creatureEntity.level.getHeight() && creatureEntity.level.getBlockState(blockpos).getMaterial().isSolid(); blockpos = blockpos.above())
                ;

            return blockpos;
        }
    }

    private static boolean isWaterDestination(BlockPos blockPos, CreatureEntity creatureEntity) {
        return creatureEntity.level.getBlockState(blockPos).getMaterial() == Material.WATER;
    }
}
