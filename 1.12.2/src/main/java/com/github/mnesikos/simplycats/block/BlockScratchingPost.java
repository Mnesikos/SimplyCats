package com.github.mnesikos.simplycats.block;

import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;

public class BlockScratchingPost extends BlockBase {
    protected String name;

    public BlockScratchingPost(String name) {
        super(Material.WOOD, name);
        this.name = name;
    }

    @Override
    public boolean isOpaqueCube(IBlockState state) {
        return false;
    }

    @Override
    public boolean isFullCube(IBlockState state) {
        return false;
    }

    @Override
    public boolean isPassable(IBlockAccess worldIn, BlockPos pos) {
        return false;
    }

    @Override
    public AxisAlignedBB getBoundingBox(IBlockState state, IBlockAccess source, BlockPos pos) {
        float min = 0.15625F, max = 0.84375F;
        return new AxisAlignedBB(min, 0.0F, min, max, 1.0F, max);
    }
}
