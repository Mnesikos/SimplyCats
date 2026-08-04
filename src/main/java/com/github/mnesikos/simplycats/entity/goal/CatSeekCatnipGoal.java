package com.github.mnesikos.simplycats.entity.goal;

import com.github.mnesikos.simplycats.block.CatnipBlock;
import com.github.mnesikos.simplycats.block.SCBlocks;
import com.github.mnesikos.simplycats.entity.SimplyCatEntity;
import com.github.mnesikos.simplycats.item.SCItems;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.ai.goal.MoveToBlockGoal;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.event.EventHooks;

public class CatSeekCatnipGoal extends MoveToBlockGoal {
    private final SimplyCatEntity cat;
    protected int ticksWaited;
    private boolean gotCatnip;

    public CatSeekCatnipGoal(SimplyCatEntity cat, double speed, int searchDistance) {
        super(cat, speed, searchDistance, 2);
        this.cat = cat;
    }

    @Override
    public boolean canUse() {
        gotCatnip = false;
        return super.canUse() && cat.getRandom().nextInt(3) == 0;
    }

    @Override
    public boolean canContinueToUse() {
        return gotCatnip && super.canContinueToUse();
    }

    @Override
    public boolean shouldRecalculatePath() {
        return tryTicks % 100 == 0;
    }

    @Override
    public double acceptedDistance() {
        return 2.0D;
    }

    @Override
    protected boolean isValidTarget(LevelReader levelReader, BlockPos blockPos) {
        if (!gotCatnip) {
            BlockState blockState = levelReader.getBlockState(blockPos);
            gotCatnip = blockState.is(SCBlocks.CATNIP_CROP.get()) && blockState.getValue(CatnipBlock.AGE) >= 1 || blockState.is(SCBlocks.POTTED_CATNIP.get());
        }
        return gotCatnip;
    }

    @Override
    public void tick() {
        super.tick();
        if (isReachedTarget()) {
            if (ticksWaited >= 40) onReachedTarget();
            else ++ticksWaited;
        }
    }

    private void onReachedTarget() {
        Level level = cat.level();
        if (cat.getRandom().nextFloat() <= 0.2F && gotCatnip && EventHooks.canEntityGrief(level, cat)) {
            BlockState blockState = level.getBlockState(blockPos);
            if (blockState.getBlock() instanceof CatnipBlock) {
                int age = blockState.getValue(CatnipBlock.AGE);
                if (age > 0) {
                    level.setBlock(blockPos, blockState.setValue(CatnipBlock.AGE, age - 1), 2);
                }

            } else if (blockState.is(SCBlocks.POTTED_CATNIP.get())) {
                CatnipBlock.popResource(level, blockPos, new ItemStack(SCItems.CATNIP_SEEDS.get()));
                level.setBlock(blockPos, Blocks.FLOWER_POT.defaultBlockState(), 3);
            }

            level.levelEvent(2001, blockPos, CatnipBlock.getId(blockState));
        }

        gotCatnip = false;
        nextStartTick = 10;
    }

    @Override
    public void start() {
        ticksWaited = 0;
        super.start();
    }
}
