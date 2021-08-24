package com.github.mnesikos.simplycats.entity.ai;

import com.github.mnesikos.simplycats.block.BlockCatTree;
import com.github.mnesikos.simplycats.block.BlockWindowPerch;
import com.github.mnesikos.simplycats.entity.EntityCat;
import net.minecraft.block.Block;
import net.minecraft.block.BlockBed;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.ai.EntityAIMoveToBlock;
import net.minecraft.init.Blocks;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityChest;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class CatAIOcelotSit extends EntityAIMoveToBlock {
    private final EntityCat cat;
    private final double movementSpeed;
    private int timeoutCounter;
    private int maxStayTicks;
    private boolean isAboveDestination;
    private final int searchLength;

    public CatAIOcelotSit(EntityCat cat, double speed, int length) {
        super(cat, speed, length);
        this.cat = cat;
        this.movementSpeed = speed;
        this.searchLength = length;
    }

    @Override
    public boolean shouldExecute() {
        if (!this.cat.isTamed())
            return false;

        if (this.cat.isTamed() && this.cat.isSitting())
            return false;

        if (this.runDelay > 0) {
            --this.runDelay;
            return false;

        } else {
            this.runDelay = 200 + this.cat.getRNG().nextInt(200);
            return this.searchForDestination();
        }
    }

    @Override
    public boolean shouldContinueExecuting() {
        return this.timeoutCounter >= -this.maxStayTicks && this.timeoutCounter <= 1200 && this.shouldMoveTo(this.cat.world, this.destinationBlock);
    }

    @Override
    public void startExecuting() {
        this.cat.getAISit().setSitting(false);
        this.cat.getNavigator().tryMoveToXYZ((double) ((float) this.destinationBlock.getX()) + 0.5D, (this.destinationBlock.getY() + 1), (double) ((float) this.destinationBlock.getZ()) + 0.5D, this.movementSpeed);
        this.timeoutCounter = 0;
        this.maxStayTicks = this.cat.getRNG().nextInt(this.cat.getRNG().nextInt(1200) + 1200) + 1200;
    }

    @Override
    public void resetTask() {
        super.resetTask();
        this.cat.setSitting(false);
    }

    @Override
    public void updateTask() {
        this.cat.getAISit().setSitting(false);

        if (!this.getIsAboveDestination())
            this.cat.setSitting(false);
        else if (!this.cat.isSitting())
            this.cat.setSitting(true);

        if (this.cat.getDistanceSqToCenter(this.destinationBlock.up()) > 1.0D && this.cat.getDistanceSqToCenter(this.destinationBlock) > 1.0D) {
            this.isAboveDestination = false;
            ++this.timeoutCounter;

            if (this.timeoutCounter % 40 == 0)
                this.cat.getNavigator().tryMoveToXYZ((double) ((float) this.destinationBlock.getX()) + 0.5D, (this.destinationBlock.getY() + 1), (double) ((float) this.destinationBlock.getZ()) + 0.5D, this.movementSpeed);

        } else if (this.cat.getDistanceSqToCenter(this.destinationBlock.up()) <= 1.0D || this.cat.getDistanceSqToCenter(this.destinationBlock) <= 1.0D) {
            this.isAboveDestination = true;
            --this.timeoutCounter;
        }
    }

    @Override
    protected boolean getIsAboveDestination() {
        return this.isAboveDestination;
    }

    private boolean searchForDestination() {
        BlockPos blockpos = new BlockPos(this.cat);

//        for (int k = 0; k <= 3; k = k > 0 ? -k : 1 - k) {
            for (int l = 0; l < this.searchLength; ++l) {
                int k = this.cat.getRNG().nextInt(5) - 1;
                for (int i1 = 0; i1 <= l; i1 = i1 > 0 ? -i1 : 1 - i1) {
                    for (int j1 = i1 < l && i1 > -l ? l : 0; j1 <= l; j1 = j1 > 0 ? -j1 : 1 - j1) {
                        BlockPos blockpos1 = blockpos.add(i1, k - 1, j1);

                        if (this.cat.isWithinHomeDistanceFromPosition(blockpos1) && this.shouldMoveTo(this.cat.world, blockpos1)) {
                            this.destinationBlock = blockpos1;
                            return true;
                        }
                    }
                }
            }
//        }

        return false;
    }

    @Override
    protected boolean shouldMoveTo(World worldIn, BlockPos pos) {
        IBlockState iblockstate = worldIn.getBlockState(pos);
        Block block = iblockstate.getBlock();
        if (!worldIn.isAirBlock(pos.up())) {
            return block instanceof BlockCatTree.Box;
        } else {
            if (block instanceof BlockWindowPerch) return true;
            if (block instanceof BlockCatTree.Bed) return true;
            if (block == Blocks.CHEST) {
                TileEntity tileentity = worldIn.getTileEntity(pos);

                return tileentity instanceof TileEntityChest && ((TileEntityChest) tileentity).numPlayersUsing < 1;
            } else {
                if (block == Blocks.LIT_FURNACE)
                    return true;

                return block == Blocks.BED && iblockstate.getValue(BlockBed.PART) != BlockBed.EnumPartType.HEAD;
            }
        }
    }
}