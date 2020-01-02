package com.github.mnesikos.simplycats.block;

import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyBool;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

public class BlockLitterBox extends BlockBase {
    protected String name;

    public static final PropertyBool FULL = PropertyBool.create("full");
    public static final PropertyBool DIRTY = PropertyBool.create("dirty");

    public BlockLitterBox(String name) {
        super(Material.GROUND, name);
        this.name = name;
        this.setSoundType(SoundType.STONE);
        this.setHardness(0.2F);
        this.setDefaultState(this.blockState.getBaseState()
                .withProperty(FULL, Boolean.FALSE)
                .withProperty(DIRTY, Boolean.FALSE));
    }

    public void setFull(World world, BlockPos pos, IBlockState state, boolean isFull) {
        world.setBlockState(pos, state.withProperty(FULL, isFull), 2);
    }

    public void setDirty(World world, BlockPos pos, IBlockState state, boolean isDirty) {
        world.setBlockState(pos, state.withProperty(DIRTY, isDirty), 2);
    }

    /*@Override
    public IBlockState getActualState(IBlockState state, IBlockAccess worldIn, BlockPos pos) {
        return getDefaultState().withProperty(FULL, )
                .withProperty(DIRTY, );
    }*/

    @Override
    public IBlockState getStateFromMeta(int meta) {
        return this.getDefaultState();
    }

    @Override
    public int getMetaFromState(IBlockState state) {
        return 0;
    }

    @Override
    protected BlockStateContainer createBlockState() {
        return new BlockStateContainer(this, new IProperty[] {
                FULL, DIRTY
        });
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
    public boolean isPassable(IBlockAccess worldIn, BlockPos pos) {
        return false;
    }

    @Override
    public AxisAlignedBB getBoundingBox(IBlockState state, IBlockAccess source, BlockPos pos) {
        return new AxisAlignedBB(0.125F, 0.0F, 0.0F, 0.875F, 0.25F, 1.0F);
    }
}
