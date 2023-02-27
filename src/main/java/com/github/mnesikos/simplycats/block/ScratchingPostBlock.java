package com.github.mnesikos.simplycats.block;

import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.Material;
import net.minecraft.core.BlockPos;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.minecraft.world.level.BlockGetter;

import net.minecraft.world.level.block.state.BlockBehaviour.Properties;

public class ScratchingPostBlock extends Block {
    protected static final VoxelShape AXIS_ALIGNED = box(2.5F, 0.0F, 2.5F, 13.5F, 16.0F, 13.5F);

    public ScratchingPostBlock() {
        super(Properties.of(Material.WOOD));
    }

    @Override
    public VoxelShape getShape(BlockState p_220053_1_, BlockGetter p_220053_2_, BlockPos p_220053_3_, CollisionContext p_220053_4_) {
        return AXIS_ALIGNED;
    }
}
