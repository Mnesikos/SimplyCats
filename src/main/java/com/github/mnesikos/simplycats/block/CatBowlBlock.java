package com.github.mnesikos.simplycats.block;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.material.Material;
import net.minecraft.item.DyeColor;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.shapes.ISelectionContext;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.world.IBlockReader;

public class CatBowlBlock extends Block {
    protected static final VoxelShape BOWL_AABB = box(5.5F, 0.0F, 5.5F, 10.5F, 2.0F, 10.5F);

    public CatBowlBlock(DyeColor color) {
        super(Properties.of(Material.STONE));
    }

    @Override
    public VoxelShape getShape(BlockState p_220053_1_, IBlockReader p_220053_2_, BlockPos p_220053_3_, ISelectionContext p_220053_4_) {
        return BOWL_AABB;
    }
}
