package com.github.mnesikos.simplycats.block;

import com.github.mnesikos.simplycats.init.ModItems;
import com.github.mnesikos.simplycats.item.ItemScratchingPost;
import net.minecraft.block.BlockPlanks;
import net.minecraft.block.material.MapColor;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyEnum;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

import java.util.Random;

public class BlockScratchingPost extends BlockBase {
    protected String name;

    public static final PropertyEnum<BlockPlanks.EnumType> VARIANT = PropertyEnum.<BlockPlanks.EnumType>create("variant", BlockPlanks.EnumType.class);
    protected static final AxisAlignedBB POST_AABB = new AxisAlignedBB(0.15625F, 0.0F, 0.15625F, 0.84375F, 1.0F, 0.84375F);

    public BlockScratchingPost(String name) {
        super(Material.WOOD, name);
        this.name = name;
        this.setDefaultState(this.blockState.getBaseState()
                .withProperty(VARIANT, BlockPlanks.EnumType.OAK));
    }

    @Override
    public IBlockState getStateFromMeta(int meta) {
        return this.getDefaultState().withProperty(VARIANT, BlockPlanks.EnumType.byMetadata(meta));
    }

    @Override
    public MapColor getMapColor(IBlockState state, IBlockAccess worldIn, BlockPos pos)
    {
        return ((BlockPlanks.EnumType)state.getValue(VARIANT)).getMapColor();
    }

    @Override
    public int getMetaFromState(IBlockState state)
    {
        return ((BlockPlanks.EnumType)state.getValue(VARIANT)).getMetadata();
    }

    @Override
    protected BlockStateContainer createBlockState()
    {
        return new BlockStateContainer(this, new IProperty[] {VARIANT});
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
        return POST_AABB;
    }

    @Override
    public Item createItemBlock() {
        return null;
    }

    @Override
    public void onBlockPlacedBy(World worldIn, BlockPos pos, IBlockState state, EntityLivingBase placer, ItemStack stack) {
        if (stack.getItem() instanceof ItemScratchingPost) {
            BlockPlanks.EnumType type = ((ItemScratchingPost) stack.getItem()).type;
            worldIn.setBlockState(pos, state.withProperty(VARIANT, type));
        } else
            worldIn.setBlockState(pos, state.withProperty(VARIANT, BlockPlanks.EnumType.OAK));
    }

    @Override
    public ItemStack getPickBlock(IBlockState state, RayTraceResult target, World world, BlockPos pos, EntityPlayer player) {
        return new ItemStack(ModItems.SCRATCHING_POSTS.get(state.getValue(VARIANT)));
    }

    @Override
    public Item getItemDropped(IBlockState state, Random rand, int fortune) {
        return ModItems.SCRATCHING_POSTS.get(state.getValue(VARIANT));
    }
}
