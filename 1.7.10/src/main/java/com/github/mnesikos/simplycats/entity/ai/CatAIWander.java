package com.github.mnesikos.simplycats.entity.ai;

import com.github.mnesikos.simplycats.configuration.SimplyCatsConfig;
import com.github.mnesikos.simplycats.entity.EntityCat;
import net.minecraft.entity.ai.EntityAIBase;
import net.minecraft.entity.ai.RandomPositionGenerator;
import net.minecraft.pathfinding.PathNavigate;
import net.minecraft.util.Vec3;

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
        if (this.cat.getAge() >= 100)
            return false;
        else if (this.cat.getRNG().nextInt(this.executionChance) != 0)
            return false;
        else if (!this.cat.canWander())
            return false;

        Vec3 vec3d = this.getPosition();

        if (vec3d == null)
            return false;
        else {
            this.x = vec3d.xCoord;
            this.y = vec3d.yCoord;
            this.z = vec3d.zCoord;
            return true;
        }
    }

    @Nullable
    protected Vec3 getPosition() {
        if (cat.hasHomePos()) {
            if (cat.getDistanceSq(cat.getHomePosX(), cat.getHomePosY(), cat.getHomePosZ())
                    > SimplyCatsConfig.WANDER_AREA_LIMIT) {
                return Vec3.createVectorHelper(cat.getHomePosX(), cat.getHomePosY(), cat.getHomePosZ());

            } else {
                PathNavigate pathNavigate = cat.getNavigator();
                Random rand = cat.getRNG();

                int xzRange = 5;
                int yRange = 3;

                float bestWeight = -99999.0F;
                int bestPosX = cat.getHomePosX(), bestPosY = cat.getHomePosY(), bestPosZ = cat.getHomePosZ();

                for (int attempt = 0; attempt < 10; attempt++) {
                    int l = rand.nextInt(2 * xzRange + 1) - xzRange;
                    int i1 = rand.nextInt(2 * yRange + 1) - yRange;
                    int j1 = rand.nextInt(2 * xzRange + 1) - xzRange;

                    float weight = cat.getBlockPathWeight(l + cat.getHomePosX(), i1 + cat.getHomePosY(), j1 + cat.getHomePosZ());

                    if (weight > bestWeight) {
                        bestWeight = weight;
                        bestPosX = l;
                        bestPosY = i1;
                        bestPosZ = j1;
                    }
                }
                return Vec3.createVectorHelper(cat.getHomePosX() + bestPosX, cat.getHomePosY() + bestPosY, cat.getHomePosZ() + bestPosZ);
            }

        } else
            return RandomPositionGenerator.findRandomTarget(cat, 10, 7);
    }

    /**
     * Returns whether an in-progress EntityAIBase should continue executing
     */
    @Override
    public boolean continueExecuting() {
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
