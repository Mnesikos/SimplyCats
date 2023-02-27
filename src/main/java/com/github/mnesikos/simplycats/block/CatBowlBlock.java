package com.github.mnesikos.simplycats.block;

import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.Material;
import net.minecraft.world.item.DyeColor;
import net.minecraft.core.BlockPos;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.minecraft.world.level.BlockGetter;

import net.minecraft.world.level.block.state.BlockBehaviour.Properties;

public class CatBowlBlock extends Block {
    protected static final VoxelShape BOWL_AABB = box(5.5F, 0.0F, 5.5F, 10.5F, 2.0F, 10.5F);

    public CatBowlBlock(DyeColor color) {
        super(Properties.of(Material.STONE));
    }

    @Override
    public VoxelShape getShape(BlockState p_220053_1_, BlockGetter p_220053_2_, BlockPos p_220053_3_, CollisionContext p_220053_4_) {
        return BOWL_AABB;
    }
}
