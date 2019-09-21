package com.github.mnesikos.simplycats.entity.ai;

import com.github.mnesikos.simplycats.configuration.SimplyCatsConfig;
import com.github.mnesikos.simplycats.entity.EntityCat;
import net.minecraft.entity.ai.EntityAIBase;
import net.minecraft.entity.ai.RandomPositionGenerator;
import net.minecraft.pathfinding.PathNavigate;
import net.minecraft.util.math.BlockPos;
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
        this.executionChance = 60;
        this.setMutexBits(1);
    }

    @Override
    public boolean shouldExecute() {
        if (this.cat.getIdleTime() >= 100)
            return false;
        else if (this.cat.getRNG().nextInt(this.executionChance) != 0)
            return false;
        else if (!this.cat.canWander())
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
    protected Vec3d getPosition() {
        if (cat.hasHomePos()) {
            if (cat.getPosition().distanceSq(cat.getHomePos().getX(), cat.getHomePos().getY(), cat.getHomePos().getZ())
                    > SimplyCatsConfig.WANDER_AREA_LIMIT) {
                return new Vec3d(cat.getHomePos().getX(), cat.getHomePos().getY(), cat.getHomePos().getZ());

            } else {
                PathNavigate pathNavigate = cat.getNavigator();
                Random rand = cat.getRNG();

                int xzRange = 5;
                int yRange = 3;

                float bestWeight = -99999.0F;
                BlockPos bestPos = cat.getHomePos();

                for (int attempt = 0; attempt < 10; attempt++) {
                    int l = rand.nextInt(2 * xzRange + 1) - xzRange;
                    int i1 = rand.nextInt(2 * yRange + 1) - yRange;
                    int j1 = rand.nextInt(2 * xzRange + 1) - xzRange;

                    BlockPos testPos = cat.getHomePos().add(l, i1, j1);

                    if (pathNavigate.canEntityStandOnPos(testPos)) {
                        float weight = cat.getBlockPathWeight(testPos);
                        if (weight > bestWeight) {
                            bestWeight = weight;
                            bestPos = testPos;
                        }
                    }
                }
                return new Vec3d(bestPos.getX(), bestPos.getY(), bestPos.getZ());
            }

        } else
            return RandomPositionGenerator.findRandomTarget(cat, 10, 7);
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
}
