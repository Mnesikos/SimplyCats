package com.github.mnesikos.simplycats.entity.goal;

import com.github.mnesikos.simplycats.block.CatTreeBlock;
import com.github.mnesikos.simplycats.block.WindowPerchBlock;
import com.github.mnesikos.simplycats.entity.SimplyCatEntity;
import com.github.mnesikos.simplycats.entity.core.Genetics;
import net.minecraft.block.*;
import net.minecraft.entity.ai.goal.MoveToBlockGoal;
import net.minecraft.state.properties.BedPart;
import net.minecraft.tags.BlockTags;
import net.minecraft.tileentity.ChestTileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IWorldReader;

public class CatSitOnBlockGoal extends MoveToBlockGoal {
    private final SimplyCatEntity cat;

    public CatSitOnBlockGoal(SimplyCatEntity cat, double speed, int length) {
        super(cat, speed, length);
        this.cat = cat;
    }

    @Override
    public boolean canUse() {
        if (this.cat.getSex() == Genetics.Sex.FEMALE && this.cat.getBreedingStatus("inheat"))
            return false;

        return this.cat.isTame() && !this.cat.isOrderedToSit() && super.canUse();
    }

    @Override
    public void start() {
        super.start();
        this.cat.setInSittingPose(false);
    }

    @Override
    public void stop() {
        super.stop();
        this.cat.setInSittingPose(false);
    }

    @Override
    public void tick() {
        super.tick();
        this.cat.setInSittingPose(this.isReachedTarget());
    }

    @Override
    protected boolean isValidTarget(IWorldReader world, BlockPos pos) {
        BlockState blockState = world.getBlockState(pos);
        Block block = blockState.getBlock();
        if (!world.isEmptyBlock(pos.above())) {
            return block instanceof CatTreeBlock.Box;
        } else {
            if (block instanceof WindowPerchBlock) return true;
            if (block instanceof CatTreeBlock.Bed) return true;
            if (blockState.is(Blocks.CHEST)) {
                return ChestTileEntity.getOpenCount(world, pos) < 1;
            } else {
                return blockState.is(Blocks.FURNACE) && blockState.getValue(FurnaceBlock.LIT) || blockState.is(BlockTags.BEDS, (state) -> state.getOptionalValue(BedBlock.PART).map((bedPart) -> bedPart != BedPart.HEAD).orElse(true));
            }
        }
    }
}
