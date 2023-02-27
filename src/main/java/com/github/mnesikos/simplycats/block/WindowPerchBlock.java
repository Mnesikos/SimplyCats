package com.github.mnesikos.simplycats.block;

import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.HorizontalDirectionalBlock;
import net.minecraft.world.level.material.Material;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.block.state.properties.DirectionProperty;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.core.Direction;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.core.BlockPos;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.LevelAccessor;

import javax.annotation.Nullable;

import net.minecraft.world.level.block.state.BlockBehaviour.Properties;

public class WindowPerchBlock extends Block {
    public static final DirectionProperty FACING = HorizontalDirectionalBlock.FACING;
    protected static final VoxelShape NORTH_AABB = box(0.0F, 9.0F, 4.0F, 16.0F, 16.0F, 16.0F);
    protected static final VoxelShape SOUTH_AABB = box(0.0F, 9.0F, 0.0F, 16.0F, 16.0F, 12.0F);
    protected static final VoxelShape EAST_AABB = box(0.0F, 9.0F, 0.0F, 12.0F, 16.0F, 16.0F);
    protected static final VoxelShape WEST_AABB = box(4.0F, 9.0F, 0.0F, 16.0F, 16.0F, 16.0F);

    public WindowPerchBlock() {
        super(Properties.of(Material.WOOD));
        this.registerDefaultState(this.getStateDefinition().any().setValue(FACING, Direction.NORTH));
    }

    @Nullable
    @Override
    public BlockState getStateForPlacement(BlockPlaceContext context) {
        Direction enumFacing = context.getHorizontalDirection().getOpposite();
        return super.getStateForPlacement(context).setValue(FACING, enumFacing);
    }

    @Override
    public BlockState rotate(BlockState state, LevelAccessor world, BlockPos pos, Rotation direction) {
        return state.setValue(FACING, direction.rotate(state.getValue(FACING)));
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> blockStateBuilder) {
        blockStateBuilder.add(FACING);
    }

    @Override
    public VoxelShape getShape(BlockState state, BlockGetter reader, BlockPos pos, CollisionContext context) {
        switch (state.getValue(FACING)) {
            case EAST:
                return EAST_AABB;
            case WEST:
                return WEST_AABB;
            case SOUTH:
                return SOUTH_AABB;
            default:
                return NORTH_AABB;
        }
    }
}
