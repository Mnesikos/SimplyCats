package com.github.mnesikos.simplycats.block;

import com.github.mnesikos.simplycats.entity.SimplyCatEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.HorizontalDirectionalBlock;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.DirectionProperty;
import net.minecraft.world.level.block.state.properties.NoteBlockInstrument;
import net.minecraft.world.level.material.MapColor;
import net.minecraft.world.phys.shapes.BooleanOp;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;

import javax.annotation.Nullable;

public class CatTreeBlock extends Block {
    public static final DirectionProperty FACING = HorizontalDirectionalBlock.FACING;
    protected final VoxelShape voxelShape;

    public CatTreeBlock(VoxelShape axisAlignedBB) {
        super(Properties.of().mapColor(MapColor.SNOW).instrument(NoteBlockInstrument.GUITAR).strength(0.8F).sound(SoundType.WOOL));
        this.voxelShape = axisAlignedBB;
    }

    @Override
    public boolean isLadder(BlockState state, LevelReader world, BlockPos pos, LivingEntity entity) {
        return entity instanceof SimplyCatEntity;
    }

    @Override
    public VoxelShape getShape(BlockState p_220053_1_, BlockGetter p_220053_2_, BlockPos p_220053_3_, CollisionContext p_220053_4_) {
        return voxelShape;
    }

    public static class Facing extends CatTreeBlock {
        public Facing(VoxelShape axisAlignedBB) {
            super(axisAlignedBB);
            this.registerDefaultState(this.getStateDefinition().any().setValue(FACING, Direction.NORTH));
        }

        @Nullable
        @Override
        public BlockState getStateForPlacement(BlockPlaceContext context) {
            Direction direction = context.getHorizontalDirection().getOpposite();
            return super.getStateForPlacement(context).setValue(FACING, direction);
        }

        @Override
        public BlockState rotate(BlockState state, Rotation rotation) {
            return state.setValue(FACING, rotation.rotate(state.getValue(FACING)));
        }

        @Override
        protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> blockStateBuilder) {
            blockStateBuilder.add(FACING);
        }
    }

    public static class Box extends Facing {
        private static final VoxelShape INSIDE_NS = box(2.0D, 1.0D, 1.0D, 14.0D, 14.0D, 15.0D);
        public static final VoxelShape INSIDE_EW = box(1.0D, 1.0D, 2.0D, 15.0D, 14.0D, 14.0D);
        protected static final VoxelShape NORTH_SHAPE = Shapes.join(Shapes.block(), Shapes.or(box(0.0D, 1.0D, 0.0D, 16.0D, 16.0D, 1.0D), INSIDE_NS), BooleanOp.ONLY_FIRST);
        protected static final VoxelShape EAST_SHAPE = Shapes.join(Shapes.block(), Shapes.or(box(15.0D, 1.0D, 0.0D, 16.0D, 16.0D, 16.0D), INSIDE_EW), BooleanOp.ONLY_FIRST);
        protected static final VoxelShape SOUTH_SHAPE = Shapes.join(Shapes.block(), Shapes.or(box(0.0D, 1.0D, 15.0D, 16.0D, 16.0D, 16.0D), INSIDE_NS), BooleanOp.ONLY_FIRST);
        protected static final VoxelShape WEST_SHAPE = Shapes.join(Shapes.block(), Shapes.or(box(0.0D, 1.0D, 0.0D, 1.0D, 16.0D, 16.0D), INSIDE_EW), BooleanOp.ONLY_FIRST);

        public Box() {
            super(Shapes.block());
        }

        @Override
        public VoxelShape getShape(BlockState state, BlockGetter blockReader, BlockPos pos, CollisionContext context) {
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
        public VoxelShape getInteractionShape(BlockState state, BlockGetter blockReader, BlockPos pos) {
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
        public VoxelShape getShape(BlockState p_220053_1_, BlockGetter p_220053_2_, BlockPos p_220053_3_, CollisionContext p_220053_4_) {
            return AABB_BOTTOM;
        }
    }
}
