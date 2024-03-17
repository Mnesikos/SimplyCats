package com.github.mnesikos.simplycats.block;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.HorizontalDirectionalBlock;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.DirectionProperty;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;

import javax.annotation.Nullable;

public class LitterBoxBlock extends Block {
    public static final DirectionProperty FACING = HorizontalDirectionalBlock.FACING;
    public static final IntegerProperty LEVEL = BlockStateProperties.LEVEL_CAULDRON;
    protected static final VoxelShape X_AXIS_AABB = box(0.0F, 0.0F, 2.0F, 16.0F, 4.0F, 14.0F);
    protected static final VoxelShape Z_AXIS_AABB = box(2.0F, 0.0F, 0.0F, 14.0F, 4.0F, 16.0F);

    public LitterBoxBlock() {
        super(Properties.of().noOcclusion().strength(0.2F));
        this.registerDefaultState(this.stateDefinition.any().setValue(FACING, Direction.NORTH).setValue(LEVEL, 1));
    }

    public void setLevel(Level world, BlockPos pos, BlockState state, int level) {
        world.setBlock(pos, state.setValue(LEVEL, level), 2);
    }

    @Override
    public InteractionResult use(BlockState state, Level world, BlockPos pos, Player player, InteractionHand hand, BlockHitResult rayTraceResult) {
        ItemStack itemStack = player.getItemInHand(hand);
        int level = state.getValue(LEVEL);

        if (level == 0) {
            if (itemStack.getItem() != Blocks.SAND.asItem())
                return InteractionResult.PASS;

            setLevel(world, pos, state, 1);
            if (!player.isCreative())
                itemStack.shrink(1);

        } else if (level == 1) {
            if (itemStack.getItem() == Items.BONE)
                setLevel(world, pos, state, 2);
            else {
                ItemStack returnSand = new ItemStack(Blocks.SAND);
                if (!player.isCreative()) {
                    if (itemStack.isEmpty())
                        player.setItemInHand(hand, returnSand);
                    else if (!player.addItem(returnSand))
                        player.drop(returnSand, false);
                }

                setLevel(world, pos, state, 0);
            }

        } else {
            if (itemStack.getItem() != Blocks.SAND.asItem())
                return InteractionResult.PASS;

            setLevel(world, pos, state, 1);
            if (!player.isCreative())
                itemStack.shrink(1);
        }

        return InteractionResult.SUCCESS;
    }

    @Nullable
    @Override
    public BlockState getStateForPlacement(BlockPlaceContext context) {
        Direction direction = context.getHorizontalDirection().getOpposite();
        return super.getStateForPlacement(context).setValue(FACING, direction);
    }

    @Override
    public BlockState rotate(BlockState state, Rotation rotation) {
        return state.getBlock() != this ? state : state.setValue(FACING, rotation.rotate(state.getValue(FACING)));
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> p_206840_1_) {
        p_206840_1_.add(FACING, LEVEL);
    }

    @Override
    public VoxelShape getShape(BlockState state, BlockGetter p_220053_2_, BlockPos p_220053_3_, CollisionContext p_220053_4_) {
        Direction direction = state.getValue(FACING);
        return direction.getAxis() == Direction.Axis.X ? X_AXIS_AABB : Z_AXIS_AABB;
    }
}
