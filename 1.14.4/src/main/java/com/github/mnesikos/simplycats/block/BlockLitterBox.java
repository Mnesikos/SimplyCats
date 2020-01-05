package com.github.mnesikos.simplycats.block;

import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyBool;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.NonNullList;
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

    @Override
    public boolean onBlockActivated(World worldIn, BlockPos pos, IBlockState state, EntityPlayer playerIn, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) {
        ItemStack itemstack = playerIn.getHeldItem(hand);
        boolean isThisFull = worldIn.getBlockState(pos).getValue(FULL);
        boolean isThisDirty = worldIn.getBlockState(pos).getValue(DIRTY);

        if (!isThisFull) {
            if (itemstack.getItem() != Item.getItemFromBlock(Blocks.SAND))
                return false;

            setFull(worldIn, pos, state, true);
            if (!playerIn.capabilities.isCreativeMode)
                itemstack.shrink(1);

        } else if (!isThisDirty) {
            if (itemstack.getItem() == Items.BONE) // todo remove testing feature
                setDirty(worldIn, pos, state, true);
            else {
                ItemStack returnSand = new ItemStack(Blocks.SAND);
                if (!playerIn.capabilities.isCreativeMode) {
                    if (itemstack.isEmpty())
                        playerIn.setHeldItem(hand, returnSand);
                    else if (!playerIn.addItemStackToInventory(returnSand))
                        playerIn.dropItem(returnSand, false);
                }

                setFull(worldIn, pos, state, false);
            }

        } else {
            if (itemstack.getItem() != Item.getItemFromBlock(Blocks.SAND))
                return false;

            setDirty(worldIn, pos, state, false);
            if (!playerIn.capabilities.isCreativeMode)
                itemstack.shrink(1);
        }

        worldIn.notifyBlockUpdate(pos, state, state, 3);
        return true;
    }

    @Override
    public void getDrops(NonNullList<ItemStack> drops, IBlockAccess world, BlockPos pos, IBlockState state, int fortune) {
        super.getDrops(drops, world, pos, state, fortune);
        if (state.getValue(FULL) && !state.getValue(DIRTY))
            drops.add(new ItemStack(Blocks.SAND));
    }

    @Override
    public IBlockState getActualState(IBlockState state, IBlockAccess worldIn, BlockPos pos) {
        return getDefaultState().withProperty(FULL, state.getValue(FULL))
                .withProperty(DIRTY, state.getValue(DIRTY));
        //return state;
    }

    @Override
    public IBlockState getStateFromMeta(int meta) {
        switch (meta) {
            case 0:
            default:
                return this.getDefaultState();
            case 1:
                return this.getDefaultState().withProperty(FULL, true).withProperty(DIRTY, false);
            case 2:
                return this.getDefaultState().withProperty(FULL, true).withProperty(DIRTY, true);
        }
        //return this.getDefaultState();
    }

    @Override
    public int getMetaFromState(IBlockState state) {
        if (state.getValue(FULL) && !state.getValue(DIRTY))
            return 1;
        if (state.getValue(FULL) && state.getValue(DIRTY))
            return 2;
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
