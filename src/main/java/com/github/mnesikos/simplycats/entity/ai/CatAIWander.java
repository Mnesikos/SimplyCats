package com.github.mnesikos.simplycats.entity.ai;

import com.github.mnesikos.simplycats.configuration.SCConfig;
import com.github.mnesikos.simplycats.entity.EntityCat;
import net.minecraft.block.material.Material;
import net.minecraft.entity.EntityCreature;
import net.minecraft.entity.ai.EntityAIBase;
import net.minecraft.pathfinding.PathNavigate;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;

import javax.annotation.Nullable;
import java.util.Random;

public class CatAIWander extends EntityAIBase {
    protected final EntityCat cat;
    protected double x;
    protected double y;
    protected double z;
    protected final double speed;
    protected int executionChance;

    public CatAIWander(EntityCat cat, double speed) {
        this.cat = cat;
        this.speed = speed;
        this.executionChance = 45; // todo activeness level should effect this
        this.setMutexBits(1);
    }

    @Override
    public boolean shouldExecute() {
        if (this.cat.getIdleTime() >= 100)
            return false;
        else if (this.cat.getRNG().nextInt(this.executionChance) != 0)
            return false;

        Vec3d vec3d = this.getPosition();

        if (vec3d == null)
            return false;
        else {
            this.x = vec3d.x;
            this.y = vec3d.y;
            this.z = vec3d.z;
            return true;
        }
    }

    @Nullable
    private Vec3d getPosition() {
        PathNavigate pathnavigate = cat.getNavigator();
        Random random = cat.getRNG();
        boolean outsideBounds;

        int xzRange = 10;
        int yRange = 3;

        if (cat.hasHomePos()) {
            double d0 = cat.getHomePos().distanceSq((double) MathHelper.floor(cat.posX), (double)MathHelper.floor(cat.posY), (double)MathHelper.floor(cat.posZ)) + 4.0D;
            double d1 = (SCConfig.WANDER_AREA_LIMIT + (float)xzRange);
            outsideBounds = d0 < d1 * d1;
        }
        else
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

                if (cat.posX > (double)blockpos.getX())
                    l -= random.nextInt(xzRange / 2);
                else
                    l += random.nextInt(xzRange / 2);

                if (cat.posZ > (double)blockpos.getZ())
                    j1 -= random.nextInt(xzRange / 2);
                else
                    j1 += random.nextInt(xzRange / 2);
            }

            BlockPos blockpos1 = new BlockPos((double)l + cat.posX, (double)i1 + cat.posY, (double)j1 + cat.posZ);

            if ((!outsideBounds || (cat.getHomePos().distanceSq(blockpos1) < (SCConfig.WANDER_AREA_LIMIT * SCConfig.WANDER_AREA_LIMIT))) && pathnavigate.canEntityStandOnPos(blockpos1)) {
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
            return new Vec3d((double)k1 + cat.posX, (double)i + cat.posY, (double)j + cat.posZ);
        else
            return null;
    }

    /**
     * Returns whether an in-progress EntityAIBase should continue executing
     */
    @Override
    public boolean shouldContinueExecuting() {
        return !this.cat.getNavigator().noPath();
    }

    /**
     * Execute a one shot task or start executing a continuous task
     */
    @Override
    public void startExecuting() {
        this.cat.getNavigator().tryMoveToXYZ(this.x, this.y, this.z, this.speed);
    }


    private static BlockPos moveAboveSolid(BlockPos blockPos, EntityCreature entityCreature) {
        if (!entityCreature.world.getBlockState(blockPos).getMaterial().isSolid())
            return blockPos;
        else {
            BlockPos blockpos;

            for (blockpos = blockPos.up(); blockpos.getY() < entityCreature.world.getHeight() && entityCreature.world.getBlockState(blockpos).getMaterial().isSolid(); blockpos = blockpos.up())
                ;

            return blockpos;
        }
    }

    private static boolean isWaterDestination(BlockPos blockPos, EntityCreature entityCreature) {
        return entityCreature.world.getBlockState(blockPos).getMaterial() == Material.WATER;
    }
}
