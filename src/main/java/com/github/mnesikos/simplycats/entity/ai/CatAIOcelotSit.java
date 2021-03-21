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

    public CatAIOcelotSit(EntityCat cat, double speed) {
        super(cat, speed, 8);
        this.cat = cat;
    }

    @Override
    public boolean shouldExecute() {
        return this.cat.isTamed() && super.shouldExecute();
    }

    @Override
    public void startExecuting() {
        this.cat.getAISit().setSitting(false);
        super.startExecuting();
    }

    @Override
    public void resetTask() {
        super.resetTask();
        this.cat.setSitting(false);
    }

    @Override
    public void updateTask() {
        super.updateTask();
        this.cat.getAISit().setSitting(false);

        if (!this.getIsAboveDestination()) {
            this.cat.setSitting(false);
        } else if (!this.cat.isSitting()) {
            this.cat.setSitting(true);
        }
    }

    @Override
    protected boolean shouldMoveTo(World worldIn, BlockPos pos) {
        if (!worldIn.isAirBlock(pos.up())) {
            return false;
        } else {
            IBlockState iblockstate = worldIn.getBlockState(pos);
            Block block = iblockstate.getBlock();

            if (block instanceof BlockWindowPerch) return true;
            if (block instanceof BlockCatTree.Bed) return true;
            if (block instanceof BlockCatTree.Box) return true;
            if (block == Blocks.CHEST) {
                TileEntity tileentity = worldIn.getTileEntity(pos);

                return tileentity instanceof TileEntityChest && ((TileEntityChest) tileentity).numPlayersUsing < 1;
            } else {
                if (block == Blocks.LIT_FURNACE) {
                    return true;
                }

                return block == Blocks.BED && iblockstate.getValue(BlockBed.PART) != BlockBed.EnumPartType.HEAD;
            }
        }
    }
}