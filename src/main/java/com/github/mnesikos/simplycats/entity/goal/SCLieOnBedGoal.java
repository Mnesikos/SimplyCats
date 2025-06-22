package com.github.mnesikos.simplycats.entity.goal;

import com.github.mnesikos.simplycats.entity.SimplyCatEntity;
import com.github.mnesikos.simplycats.entity.core.Genetics;
import net.minecraft.core.BlockPos;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.ai.goal.MoveToBlockGoal;
import net.minecraft.world.level.LevelReader;

import java.util.EnumSet;

public class SCLieOnBedGoal extends MoveToBlockGoal {
    private final SimplyCatEntity cat;

    public SCLieOnBedGoal(SimplyCatEntity cat, double speed, int searchRange) {
        super(cat, speed, searchRange, 6);
        this.cat = cat;
//        this.verticalSearchStart = -2;
        this.setFlags(EnumSet.of(Goal.Flag.JUMP, Goal.Flag.MOVE));
    }

    @Override
    public boolean canUse() {
        if (this.cat.getSex() == Genetics.Sex.FEMALE && this.cat.getBreedingStatus(SimplyCatEntity.BreedingStatus.HEAT))
            return false;

        return cat.isTame() && !cat.isOrderedToSit() && !cat.isResting() && super.canUse();
    }

    @Override
    public void start() {
        super.start();
        cat.setInSittingPose(false);
    }

    @Override
    protected int nextStartTick(PathfinderMob mob) {
        return 40;
    }

    @Override
    public void stop() {
        super.stop();
        this.cat.setRestingState(SimplyCatEntity.RestingState.AWAKE.ordinal());
    }

    @Override
    public void tick() {
        super.tick();
        cat.setInSittingPose(false);
        if (!isReachedTarget()) cat.setRestingState(SimplyCatEntity.RestingState.AWAKE.ordinal());
        else if (!cat.isResting()) {
            int restingState = cat.getRandom().nextInt(4) + 1;
            cat.setRestingState(restingState);
            System.out.println("Cat set to resting position: " + SimplyCatEntity.RestingState.fromOrdinal(restingState) + "(" + restingState + ")");
        }
    }

    @Override
    protected boolean isValidTarget(LevelReader levelReader, BlockPos blockPos) {
        return levelReader.isEmptyBlock(blockPos.above()) && levelReader.getBlockState(blockPos).is(BlockTags.BEDS);
    }
}
