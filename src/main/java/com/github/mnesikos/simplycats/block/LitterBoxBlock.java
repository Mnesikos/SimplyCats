package com.github.mnesikos.simplycats.block;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.HorizontalBlock;
import net.minecraft.block.material.Material;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.BlockItemUseContext;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.state.DirectionProperty;
import net.minecraft.state.IntegerProperty;
import net.minecraft.state.StateContainer;
import net.minecraft.state.properties.BlockStateProperties;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Direction;
import net.minecraft.util.Hand;
import net.minecraft.util.Rotation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.math.shapes.ISelectionContext;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;

import javax.annotation.Nullable;

public class LitterBoxBlock extends Block {
    public static final DirectionProperty FACING = HorizontalBlock.FACING;
    public static final IntegerProperty LEVEL = BlockStateProperties.LEVEL_CAULDRON;
    protected static final VoxelShape X_AXIS_AABB = box(0.0F, 0.0F, 2.0F, 16.0F, 4.0F, 14.0F);
    protected static final VoxelShape Z_AXIS_AABB = box(2.0F, 0.0F, 0.0F, 14.0F, 4.0F, 16.0F);

    public LitterBoxBlock() {
        super(Properties.of(Material.STONE).strength(0.2F));
        this.registerDefaultState(this.stateDefinition.any().setValue(FACING, Direction.NORTH).setValue(LEVEL, 0));
    }

    public void setLevel(World world, BlockPos pos, BlockState state, int level) {
        world.setBlock(pos, state.setValue(LEVEL, level), 2);
    }

    @Override
    public ActionResultType use(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockRayTraceResult rayTraceResult) {
        ItemStack itemStack = player.getItemInHand(hand);
        int level = state.getValue(LEVEL);

        if (level == 0) {
            if (itemStack.getItem() != Blocks.SAND.asItem())
                return ActionResultType.PASS;

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
                return ActionResultType.PASS;

            setLevel(world, pos, state, 1);
            if (!player.isCreative())
                itemStack.shrink(1);
        }

        return ActionResultType.SUCCESS;
    }

    @Nullable
    @Override
    public BlockState getStateForPlacement(BlockItemUseContext context) {
        Direction direction = context.getHorizontalDirection().getOpposite();
        return super.getStateForPlacement(context).setValue(FACING, direction);
    }

    @Override
    public BlockState rotate(BlockState state, Rotation rotation) {
        return state.getBlock() != this ? state : state.setValue(FACING, rotation.rotate(state.getValue(FACING)));
    }

    @Override
    protected void createBlockStateDefinition(StateContainer.Builder<Block, BlockState> p_206840_1_) {
        p_206840_1_.add(FACING, LEVEL);
    }

    @Override
    public VoxelShape getShape(BlockState state, IBlockReader p_220053_2_, BlockPos p_220053_3_, ISelectionContext p_220053_4_) {
        Direction direction = state.getValue(FACING);
        return direction.getAxis() == Direction.Axis.X ? X_AXIS_AABB : Z_AXIS_AABB;
    }
}
