package com.github.mnesikos.simplycats.block;

import com.github.mnesikos.simplycats.entity.SimplyCatEntity;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.HorizontalBlock;
import net.minecraft.block.material.Material;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.BlockItemUseContext;
import net.minecraft.state.DirectionProperty;
import net.minecraft.state.StateContainer;
import net.minecraft.util.Direction;
import net.minecraft.util.Rotation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.shapes.IBooleanFunction;
import net.minecraft.util.math.shapes.ISelectionContext;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.util.math.shapes.VoxelShapes;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.IWorldReader;

import javax.annotation.Nullable;

public class CatTreeBlock extends Block {
    public static final DirectionProperty FACING = HorizontalBlock.FACING;
    protected final VoxelShape voxelShape;

    public CatTreeBlock(VoxelShape axisAlignedBB) {
        super(Properties.of(Material.WOOL));
        this.voxelShape = axisAlignedBB;
    }

    @Override
    public boolean isLadder(BlockState state, IWorldReader world, BlockPos pos, LivingEntity entity) {
        return entity instanceof SimplyCatEntity;
    }

    @Override
    public VoxelShape getShape(BlockState p_220053_1_, IBlockReader p_220053_2_, BlockPos p_220053_3_, ISelectionContext p_220053_4_) {
        return voxelShape;
    }

    public static class Facing extends CatTreeBlock {
        public Facing(VoxelShape axisAlignedBB) {
            super(axisAlignedBB);
            this.registerDefaultState(this.getStateDefinition().any().setValue(FACING, Direction.NORTH));
        }

        @Nullable
        @Override
        public BlockState getStateForPlacement(BlockItemUseContext context) {
            Direction direction = context.getHorizontalDirection().getOpposite();
            return super.getStateForPlacement(context).setValue(FACING, direction);
        }

        @Override
        public BlockState rotate(BlockState state, Rotation rotation) {
            return state.setValue(FACING, rotation.rotate(state.getValue(FACING)));
        }

        @Override
        protected void createBlockStateDefinition(StateContainer.Builder<Block, BlockState> blockStateBuilder) {
            blockStateBuilder.add(FACING);
        }
    }

    public static class Box extends Facing {
        private static final VoxelShape INSIDE_NS = box(2.0D, 1.0D, 1.0D, 14.0D, 14.0D, 15.0D);
        public static final VoxelShape INSIDE_EW = box(1.0D, 1.0D, 2.0D, 15.0D, 14.0D, 14.0D);
        protected static final VoxelShape NORTH_SHAPE = VoxelShapes.join(VoxelShapes.block(), VoxelShapes.or(box(0.0D, 1.0D, 0.0D, 16.0D, 16.0D, 1.0D), INSIDE_NS), IBooleanFunction.ONLY_FIRST);
        protected static final VoxelShape EAST_SHAPE = VoxelShapes.join(VoxelShapes.block(), VoxelShapes.or(box(15.0D, 1.0D, 0.0D, 16.0D, 16.0D, 16.0D), INSIDE_EW), IBooleanFunction.ONLY_FIRST);
        protected static final VoxelShape SOUTH_SHAPE = VoxelShapes.join(VoxelShapes.block(), VoxelShapes.or(box(0.0D, 1.0D, 15.0D, 16.0D, 16.0D, 16.0D), INSIDE_NS), IBooleanFunction.ONLY_FIRST);
        protected static final VoxelShape WEST_SHAPE = VoxelShapes.join(VoxelShapes.block(), VoxelShapes.or(box(0.0D, 1.0D, 0.0D, 1.0D, 16.0D, 16.0D), INSIDE_EW), IBooleanFunction.ONLY_FIRST);

        public Box() {
            super(VoxelShapes.block());
        }

        @Override
        public VoxelShape getShape(BlockState state, IBlockReader blockReader, BlockPos pos, ISelectionContext context) {
            Direction direction = state.getValue(FACING);
            switch (direction) {
                default:
                    return NORTH_SHAPE;
                case EAST:
                    return EAST_SHAPE;
                case SOUTH:
                    return SOUTH_SHAPE;
                case WEST:
                    return WEST_SHAPE;
            }
        }

        @Override
        public VoxelShape getInteractionShape(BlockState state, IBlockReader blockReader, BlockPos pos) {
            Direction direction = state.getValue(FACING);
            return direction.getAxis() == Direction.Axis.X ? INSIDE_NS : INSIDE_EW;
        }
    }

    public static class Bed extends Facing {
        protected static final VoxelShape AABB_BOTTOM = box(0.0D, 0.0D, 0.0D, 16.0D, 2.0D, 16.0D);

        public Bed(VoxelShape voxelShape) {
            super(voxelShape);
        }

        @Override
        public VoxelShape getShape(BlockState p_220053_1_, IBlockReader p_220053_2_, BlockPos p_220053_3_, ISelectionContext p_220053_4_) {
            return AABB_BOTTOM;
        }
    }
}
