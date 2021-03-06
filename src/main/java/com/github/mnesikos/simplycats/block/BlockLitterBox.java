package com.github.mnesikos.simplycats.block;

import net.minecraft.block.Block;
import net.minecraft.block.BlockHorizontal;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.PropertyDirection;
import net.minecraft.block.properties.PropertyEnum;
import net.minecraft.block.state.BlockFaceShape;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.*;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

public class BlockLitterBox extends Block {
    public static final PropertyDirection FACING = BlockHorizontal.FACING;
    public static final PropertyEnum<EnumLevel> LEVEL = PropertyEnum.create("level", EnumLevel.class);
    protected static final AxisAlignedBB X_AXIS_AABB = new AxisAlignedBB(0.0F, 0.0F, 0.125F, 1.0F, 0.25F, 0.875F);
    protected static final AxisAlignedBB Z_AXIS_AABB = new AxisAlignedBB(0.125F, 0.0F, 0.0F, 0.875F, 0.25F, 1.0F);

    public BlockLitterBox() {
        super(Material.GROUND);
        this.setSoundType(SoundType.STONE);
        this.setHardness(0.2F);
        this.setDefaultState(this.blockState.getBaseState()
                .withProperty(FACING, EnumFacing.NORTH)
                .withProperty(LEVEL, EnumLevel.EMPTY));
    }

    public void setLevel(World world, BlockPos pos, IBlockState state, EnumLevel level) {
        world.setBlockState(pos, state.withProperty(LEVEL, level), 2);
    }

    @Override
    public boolean onBlockActivated(World world, BlockPos pos, IBlockState state, EntityPlayer playerIn, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) {
        ItemStack itemstack = playerIn.getHeldItem(hand);
        EnumLevel level = state.getValue(LEVEL);

        if (level.equals(EnumLevel.EMPTY)) {
            if (itemstack.getItem() != Item.getItemFromBlock(Blocks.SAND))
                return false;

            setLevel(world, pos, state, EnumLevel.CLEAN);
            if (!playerIn.capabilities.isCreativeMode)
                itemstack.shrink(1);

        } else if (level.equals(EnumLevel.CLEAN)) {
            if (itemstack.getItem() == Items.BONE) // todo remove testing feature
                setLevel(world, pos, state, EnumLevel.DIRTY);
            else {
                ItemStack returnSand = new ItemStack(Blocks.SAND);
                if (!playerIn.capabilities.isCreativeMode) {
                    if (itemstack.isEmpty())
                        playerIn.setHeldItem(hand, returnSand);
                    else if (!playerIn.addItemStackToInventory(returnSand))
                        playerIn.dropItem(returnSand, false);
                }

                setLevel(world, pos, state, EnumLevel.EMPTY);
            }

        } else {
            if (itemstack.getItem() != Item.getItemFromBlock(Blocks.SAND))
                return false;

            setLevel(world, pos, state, EnumLevel.CLEAN);
            if (!playerIn.capabilities.isCreativeMode)
                itemstack.shrink(1);
        }

        return true;
    }

    @Override
    public void getDrops(NonNullList<ItemStack> drops, IBlockAccess world, BlockPos pos, IBlockState state, int fortune) {
        super.getDrops(drops, world, pos, state, fortune);
        if (state.getValue(LEVEL).equals(EnumLevel.CLEAN))
            drops.add(new ItemStack(Blocks.SAND));
    }

    @Override
    public IBlockState getStateForPlacement(World worldIn, BlockPos pos, EnumFacing facing, float hitX, float hitY, float hitZ, int meta, EntityLivingBase placer, EnumHand hand) {
        EnumFacing enumfacing = placer.getHorizontalFacing().rotateY();
        return super.getStateForPlacement(worldIn, pos, facing, hitX, hitY, hitZ, meta, placer).withProperty(FACING, enumfacing).withProperty(LEVEL, EnumLevel.byMetadata(MathHelper.clamp(meta >> 2, 0, 2)));
    }

    @Override
    public IBlockState getStateFromMeta(int meta) {
        return this.getDefaultState()
                .withProperty(FACING, EnumFacing.getHorizontal(meta & 3))
                .withProperty(LEVEL, EnumLevel.byMetadata((meta & 15) >> 2));
    }

    @Override
    public int getMetaFromState(IBlockState state) {
        int i = 0;
        i = i | (state.getValue(FACING)).getHorizontalIndex();
        i = i | (state.getValue(LEVEL)).getMetadata() << 2;
        return i;
    }

    @Override
    public IBlockState withRotation(IBlockState state, Rotation rot) {
        return state.getBlock() != this ? state : state
                .withProperty(FACING, rot.rotate(state.getValue(FACING)));
    }

    @Override
    protected BlockStateContainer createBlockState() {
        return new BlockStateContainer(this, FACING, LEVEL);
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
    public boolean isPassable(IBlockAccess world, BlockPos pos) {
        return false;
    }

    @Override
    public BlockFaceShape getBlockFaceShape(IBlockAccess worldIn, IBlockState state, BlockPos pos, EnumFacing face) {
        return BlockFaceShape.UNDEFINED;
    }

    @Override
    public AxisAlignedBB getBoundingBox(IBlockState state, IBlockAccess source, BlockPos pos) {
        EnumFacing enumfacing = state.getValue(FACING);
        return enumfacing.getAxis() == EnumFacing.Axis.X ? X_AXIS_AABB : Z_AXIS_AABB;
    }

    public enum EnumLevel implements IStringSerializable {
        EMPTY(0, "empty"),
        CLEAN(1, "clean"),
        DIRTY(2, "dirty");

        private static final EnumLevel[] META_LOOKUP = new EnumLevel[values().length];
        private final int meta;
        private final String name;
        private final String translationKey;

        EnumLevel(int metaIn, String nameIn) {
            this(metaIn, nameIn, nameIn);
        }

        EnumLevel(int metaIn, String nameIn, String unlocalizedNameIn) {
            this.meta = metaIn;
            this.name = nameIn;
            this.translationKey = unlocalizedNameIn;
        }

        public int getMetadata() {
            return this.meta;
        }

        public String toString() {
            return this.name;
        }

        public static EnumLevel byMetadata(int meta) {
            if (meta < 0 || meta >= META_LOOKUP.length)
                meta = 0;

            return META_LOOKUP[meta];
        }

        public String getName() {
            return this.name;
        }

        public String getTranslationKey() {
            return this.translationKey;
        }

        static {
            for (EnumLevel state : values())
                META_LOOKUP[state.getMetadata()] = state;
        }
    }
}
