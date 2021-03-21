package com.github.mnesikos.simplycats.block;

import net.minecraft.block.Block;
import net.minecraft.block.BlockHorizontal;
import net.minecraft.block.material.MapColor;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.PropertyDirection;
import net.minecraft.block.state.BlockFaceShape;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.EnumDyeColor;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.Rotation;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

import javax.annotation.Nullable;
import java.util.List;

public class BlockCatTree extends Block {
    public static final PropertyDirection FACING = BlockHorizontal.FACING;
    protected final AxisAlignedBB axisAlignedBB;

    public BlockCatTree(EnumDyeColor color, AxisAlignedBB axisAlignedBB) {
        super(Material.CLOTH, MapColor.getBlockColor(color));
        this.axisAlignedBB = axisAlignedBB;
    }

    @Override
    public boolean isOpaqueCube(IBlockState state) {
        return false;
    }

    @Override
    public boolean isFullCube(IBlockState state) {
        return false;
    }

    @Override
    public AxisAlignedBB getBoundingBox(IBlockState state, IBlockAccess source, BlockPos pos) {
        return axisAlignedBB;
    }

    @Override
    public boolean isPassable(IBlockAccess worldIn, BlockPos pos) {
        return true;
    }

    @Override
    public BlockFaceShape getBlockFaceShape(IBlockAccess worldIn, IBlockState state, BlockPos pos, EnumFacing face) {
        return BlockFaceShape.UNDEFINED;
    }

    public static class Facing extends BlockCatTree {
        public Facing(EnumDyeColor color, AxisAlignedBB axisAlignedBB) {
            super(color, axisAlignedBB);
            this.setDefaultState(this.blockState.getBaseState().withProperty(FACING, EnumFacing.NORTH));
        }

        @Override
        public IBlockState getStateForPlacement(World world, BlockPos pos, EnumFacing facing, float hitX, float hitY, float hitZ, int meta, EntityLivingBase placer, EnumHand hand) {
            EnumFacing enumFacing = placer.getHorizontalFacing().getOpposite();
            return super.getStateForPlacement(world, pos, facing, hitX, hitY, hitZ, meta, placer, hand).withProperty(FACING, enumFacing);
        }

        @Override
        public IBlockState getStateFromMeta(int meta) {
            return this.getDefaultState().withProperty(FACING, EnumFacing.getHorizontal(meta & 3));
        }

        @Override
        public int getMetaFromState(IBlockState state) {
            int i = 0;
            i = i | (state.getValue(FACING)).getHorizontalIndex();
            return i;
        }

        @Override
        public IBlockState withRotation(IBlockState state, Rotation rot) {
            return state.withProperty(FACING, rot.rotate(state.getValue(FACING)));
        }

        @Override
        protected BlockStateContainer createBlockState() {
            return new BlockStateContainer(this, FACING);
        }
    }

    public static class Box extends Facing {
        protected static final AxisAlignedBB AABB_BOTTOM = new AxisAlignedBB(0.0D, 0.0D, 0.0D, 1.0D, 0.0625D, 1.0D);
        protected static final AxisAlignedBB AABB_TOP = new AxisAlignedBB(0.0D, 0.875D, 0.0D, 1.0D, 1.0D, 1.0D);
        protected static final AxisAlignedBB AABB_WALL_NORTH = new AxisAlignedBB(0.0D, 0.0D, 0.0D, 1.0D, 1.0D, 0.125D);
        protected static final AxisAlignedBB AABB_WALL_SOUTH = new AxisAlignedBB(0.0D, 0.0D, 0.875D, 1.0D, 1.0D, 1.0D);
        protected static final AxisAlignedBB AABB_WALL_EAST = new AxisAlignedBB(0.875D, 0.0D, 0.0D, 1.0D, 1.0D, 1.0D);
        protected static final AxisAlignedBB AABB_WALL_WEST = new AxisAlignedBB(0.0D, 0.0D, 0.0D, 0.125D, 1.0D, 1.0D);

        public Box(EnumDyeColor color) {
            super(color, FULL_BLOCK_AABB);
        }

        @Override
        public void addCollisionBoxToList(IBlockState state, World worldIn, BlockPos pos, AxisAlignedBB entityBox, List<AxisAlignedBB> collidingBoxes, @Nullable Entity entityIn, boolean isActualState) {
            EnumFacing enumFacing = state.getValue(FACING);
            addCollisionBoxToList(pos, entityBox, collidingBoxes, AABB_BOTTOM);
            addCollisionBoxToList(pos, entityBox, collidingBoxes, AABB_TOP);
            if (enumFacing != EnumFacing.WEST) addCollisionBoxToList(pos, entityBox, collidingBoxes, AABB_WALL_WEST);
            if (enumFacing != EnumFacing.EAST) addCollisionBoxToList(pos, entityBox, collidingBoxes, AABB_WALL_EAST);
            if (enumFacing != EnumFacing.SOUTH) addCollisionBoxToList(pos, entityBox, collidingBoxes, AABB_WALL_SOUTH);
            if (enumFacing != EnumFacing.NORTH) addCollisionBoxToList(pos, entityBox, collidingBoxes, AABB_WALL_NORTH);
        }

        @Override
        public boolean isTopSolid(IBlockState state) {
            return true;
        }
    }

    public static class Bed extends Facing {
        protected static final AxisAlignedBB AABB_BOTTOM = new AxisAlignedBB(0.0D, 0.0D, 0.0D, 1.0D, 0.0625D, 1.0D);
        protected static final AxisAlignedBB AABB_WALL_NORTH = new AxisAlignedBB(0.0D, 0.0D, 0.0D, 1.0D, 0.375F, 0.125D);
        protected static final AxisAlignedBB AABB_WALL_SOUTH = new AxisAlignedBB(0.0D, 0.0D, 0.875D, 1.0D, 0.375F, 1.0D);
        protected static final AxisAlignedBB AABB_WALL_EAST = new AxisAlignedBB(0.875D, 0.0D, 0.0D, 1.0D, 0.375F, 1.0D);
        protected static final AxisAlignedBB AABB_WALL_WEST = new AxisAlignedBB(0.0D, 0.0D, 0.0D, 0.125D, 0.375F, 1.0D);

        public Bed(EnumDyeColor color, AxisAlignedBB axisAlignedBB) {
            super(color, axisAlignedBB);
        }

        @Override
        public void addCollisionBoxToList(IBlockState state, World worldIn, BlockPos pos, AxisAlignedBB entityBox, List<AxisAlignedBB> collidingBoxes, @Nullable Entity entityIn, boolean isActualState) {
            EnumFacing enumFacing = state.getValue(FACING);
            addCollisionBoxToList(pos, entityBox, collidingBoxes, AABB_BOTTOM);
            if (enumFacing != EnumFacing.WEST) addCollisionBoxToList(pos, entityBox, collidingBoxes, AABB_WALL_WEST);
            if (enumFacing != EnumFacing.EAST) addCollisionBoxToList(pos, entityBox, collidingBoxes, AABB_WALL_EAST);
            if (enumFacing != EnumFacing.SOUTH) addCollisionBoxToList(pos, entityBox, collidingBoxes, AABB_WALL_SOUTH);
            if (enumFacing != EnumFacing.NORTH) addCollisionBoxToList(pos, entityBox, collidingBoxes, AABB_WALL_NORTH);
        }
    }
}
