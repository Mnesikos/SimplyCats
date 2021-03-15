package com.github.mnesikos.simplycats.entity.ai;

import com.github.mnesikos.simplycats.configuration.SCConfig;
import com.github.mnesikos.simplycats.entity.EntityCat;
import net.minecraft.entity.ai.EntityAIBase;
import net.minecraft.pathfinding.PathNavigate;
import net.minecraft.pathfinding.PathNodeType;

import java.util.List;

public class CatAIFollowParent extends EntityAIBase {
    EntityCat childAnimal;
    EntityCat parentAnimal;
    double moveSpeed;
    private final PathNavigate petPathfinder;
    private final float min = 22.0F;
    private int delayCounter;
    private float oldWaterCost;

    public CatAIFollowParent(EntityCat cat, double speed) {
        this.childAnimal = cat;
        this.moveSpeed = speed;
        this.petPathfinder = cat.getNavigator();
        this.setMutexBits(3);
    }

    @Override
    public boolean shouldExecute() {
        if (this.childAnimal.getGrowingAge() >= 0) {
            return false;
        } else {
            EntityCat parent = this.childAnimal.getFollowParent();

            if (parent == null) {
                List<EntityCat> list = this.childAnimal.world.getEntitiesWithinAABB(EntityCat.class, this.childAnimal.getEntityBoundingBox().grow(8.0D, 4.0D, 8.0D));

                for (EntityCat entityCat : list) {
                    if (entityCat.getGrowingAge() >= 0) {
                        if (this.childAnimal.getDistanceSq(entityCat) <= Double.MAX_VALUE) {
                            parent = entityCat;
                            this.childAnimal.setFollowParent(entityCat);
                        }
                    }
                }
            }

            if (parent == null) {
                return false;
            } else if (this.childAnimal.isSitting()) {
                return false;
            } else if (this.childAnimal.getDistance(parent) < (this.childAnimal.getAge() / (float) SCConfig.KITTEN_MATURE_TIMER + 1) * this.min + 2 + 2.0D) {
                return false;
            } else {
                this.parentAnimal = parent;
                return true;
            }
        }
    }

    @Override
    public boolean shouldContinueExecuting() {
        if (this.childAnimal.getGrowingAge() >= 0) {
            return false;
        } else if (!this.parentAnimal.isEntityAlive()) {
            return false;
        } else {
            double d0 = (this.childAnimal.getAge() / (float) SCConfig.KITTEN_MATURE_TIMER + 1) * this.min + 2;
            return !this.petPathfinder.noPath() && this.childAnimal.getDistanceSq(this.parentAnimal) >= d0 && this.childAnimal.getDistanceSq(this.parentAnimal) <= 256.0D && !this.childAnimal.isSitting();
        }
    }

    @Override
    public void startExecuting() {
        this.delayCounter = 0;
        this.oldWaterCost = this.childAnimal.getPathPriority(PathNodeType.WATER);
        this.childAnimal.setPathPriority(PathNodeType.WATER, 0.0F);
    }

    @Override
    public void resetTask() {
        this.parentAnimal = null;
        this.petPathfinder.clearPath();
        this.childAnimal.setPathPriority(PathNodeType.WATER, this.oldWaterCost);
    }

    @Override
    public void updateTask() {
        this.childAnimal.getLookHelper().setLookPositionWithEntity(this.parentAnimal, 10.0F, (float) this.childAnimal.getVerticalFaceSpeed());
        if (!this.childAnimal.isSitting()) {
            if (--this.delayCounter <= 0) {
                this.delayCounter = 10;
                this.petPathfinder.tryMoveToEntityLiving(this.parentAnimal, this.moveSpeed);
            }
        }
    }
}
