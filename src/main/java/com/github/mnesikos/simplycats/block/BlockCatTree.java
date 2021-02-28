package com.github.mnesikos.simplycats.block;

import net.minecraft.block.Block;
import net.minecraft.block.material.MapColor;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.item.EnumDyeColor;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;

public class BlockCatTree extends Block {
    protected static final AxisAlignedBB AXIS_ALIGNED = new AxisAlignedBB(0.15625F, 0.0F, 0.15625F, 0.84375F, 1.0F, 0.84375F);

    public BlockCatTree(EnumDyeColor color) {
        super(Material.CLOTH, MapColor.getBlockColor(color));
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
    public AxisAlignedBB getBoundingBox(IBlockState state, IBlockAccess source, BlockPos pos) {
        return AXIS_ALIGNED;
    }
}
