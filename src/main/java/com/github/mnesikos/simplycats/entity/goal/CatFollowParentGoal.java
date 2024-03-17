package com.github.mnesikos.simplycats.entity.goal;

import com.github.mnesikos.simplycats.entity.SimplyCatEntity;
import net.minecraft.world.entity.ai.goal.Goal;

import java.util.List;

public class CatFollowParentGoal extends Goal {
    private final SimplyCatEntity childAnimal;
    private SimplyCatEntity parentAnimal;
    private final double moveSpeed;
    private final float min = 12.0F;
    private int delayCounter;

    public CatFollowParentGoal(SimplyCatEntity cat, double speed) {
        this.childAnimal = cat;
        this.moveSpeed = speed;
    }

    @Override
    public boolean canUse() {
        if (this.childAnimal.getAge() >= 0)
            return false;
        else {
            SimplyCatEntity parent = this.childAnimal.getFollowParent();

            if (parent == null) {
                List<SimplyCatEntity> list = this.childAnimal.level().getEntitiesOfClass(SimplyCatEntity.class, this.childAnimal.getBoundingBox().inflate(8.0D, 4.0D, 8.0D));

                for (SimplyCatEntity catEntity : list) {
                    if (catEntity.getAge() >= 0) {
                        if (this.childAnimal.distanceToSqr(catEntity) <= Double.MAX_VALUE) {
                            parent = catEntity;
                            this.childAnimal.setFollowParent(catEntity);
                        }
                    }
                }
            }

            if (parent == null)
                return false;
            else if (this.childAnimal.isOrderedToSit())
                return false;
            else if (this.childAnimal.distanceTo(parent) < (this.childAnimal.getAgeScale()) * this.min + 2 + 2.0D)
                return false;
            else {
                this.parentAnimal = parent;
                return true;
            }
        }
    }

    @Override
    public boolean canContinueToUse() {
        if (this.childAnimal.getAge() >= 0)
            return false;
        else if (!this.parentAnimal.isAlive())
            return false;
        else {
            double d0 = (this.childAnimal.getAgeScale()) * this.min + 2;
            return this.childAnimal.distanceToSqr(this.parentAnimal) >= d0 && this.childAnimal.distanceToSqr(this.parentAnimal) <= 256.0D && !this.childAnimal.isOrderedToSit();
        }
    }

    @Override
    public void start() {
        this.delayCounter = 0;
    }

    @Override
    public void stop() {
        this.parentAnimal = null;
    }

    @Override
    public void tick() {
        this.childAnimal.getLookControl().setLookAt(this.parentAnimal, 10.0F, (float) this.childAnimal.getHeadRotSpeed());
        if (!this.childAnimal.isOrderedToSit()) {
            if (--this.delayCounter <= 0) {
                this.delayCounter = 10;
                this.childAnimal.getNavigation().moveTo(this.parentAnimal, this.moveSpeed);
            }
        }
    }
}
