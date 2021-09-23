package com.github.mnesikos.simplycats.block;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.HorizontalBlock;
import net.minecraft.block.material.Material;
import net.minecraft.item.BlockItemUseContext;
import net.minecraft.state.DirectionProperty;
import net.minecraft.state.StateContainer;
import net.minecraft.util.Direction;
import net.minecraft.util.Rotation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.shapes.ISelectionContext;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.IWorld;

import javax.annotation.Nullable;

public class WindowPerchBlock extends Block {
    public static final DirectionProperty FACING = HorizontalBlock.FACING;
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
    public BlockState getStateForPlacement(BlockItemUseContext context) {
        Direction enumFacing = context.getHorizontalDirection().getOpposite();
        return super.getStateForPlacement(context).setValue(FACING, enumFacing);
    }

    @Override
    public BlockState rotate(BlockState state, IWorld world, BlockPos pos, Rotation direction) {
        return state.setValue(FACING, direction.rotate(state.getValue(FACING)));
    }

    @Override
    protected void createBlockStateDefinition(StateContainer.Builder<Block, BlockState> blockStateBuilder) {
        blockStateBuilder.add(FACING);
    }

    @Override
    public VoxelShape getShape(BlockState state, IBlockReader reader, BlockPos pos, ISelectionContext context) {
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
