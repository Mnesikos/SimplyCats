package com.github.mnesikos.simplycats.block;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.material.Material;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.shapes.ISelectionContext;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.world.IBlockReader;

public class ScratchingPostBlock extends Block {
    protected static final VoxelShape AXIS_ALIGNED = box(2.5F, 0.0F, 2.5F, 13.5F, 16.0F, 13.5F);

    public ScratchingPostBlock() {
        super(Properties.of(Material.WOOD));
    }

    @Override
    public VoxelShape getShape(BlockState p_220053_1_, IBlockReader p_220053_2_, BlockPos p_220053_3_, ISelectionContext p_220053_4_) {
        return AXIS_ALIGNED;
    }
}
