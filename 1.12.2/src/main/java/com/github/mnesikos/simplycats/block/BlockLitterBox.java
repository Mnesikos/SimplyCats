package com.github.mnesikos.simplycats.block;

import com.github.mnesikos.simplycats.init.ModItems;
import com.github.mnesikos.simplycats.item.ItemLitterBox;
import com.github.mnesikos.simplycats.tileentity.TileEntityLitterBox;
import net.minecraft.block.BlockHorizontal;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyDirection;
import net.minecraft.block.properties.PropertyEnum;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.EnumDyeColor;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.*;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.world.ChunkCache;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraft.world.chunk.Chunk;

import javax.annotation.Nullable;
import java.util.Random;

public class BlockLitterBox extends BlockTileEntity<TileEntityLitterBox> {
    protected String name;

    public static final PropertyDirection FACING = BlockHorizontal.FACING;
    public static final PropertyEnum<EnumLevel> LEVEL = PropertyEnum.create("level", EnumLevel.class);
    public static final PropertyEnum<EnumDyeColor> COLOR = PropertyEnum.create("color", EnumDyeColor.class);
    protected static final AxisAlignedBB X_AXIS_AABB = new AxisAlignedBB(0.0F, 0.0F, 0.125F, 1.0F, 0.25F, 0.875F);
    protected static final AxisAlignedBB Z_AXIS_AABB = new AxisAlignedBB(0.125F, 0.0F, 0.0F, 0.875F, 0.25F, 1.0F);

    public BlockLitterBox(String name) {
        super(Material.GROUND, name);
        this.name = name;
        this.setSoundType(SoundType.STONE);
        this.setHardness(0.2F);
        this.setDefaultState(this.blockState.getBaseState()
                .withProperty(FACING, EnumFacing.NORTH)
                .withProperty(LEVEL, EnumLevel.EMPTY)
                .withProperty(COLOR, EnumDyeColor.BLACK));
    }

    public void setLevel(World world, BlockPos pos, IBlockState state, EnumLevel level, EnumDyeColor color) {
        world.setBlockState(pos, state.withProperty(LEVEL, level), 2);
        ((TileEntityLitterBox)world.getTileEntity(pos)).setColor(color);
    }

    @Override
    public boolean onBlockActivated(World world, BlockPos pos, IBlockState state, EntityPlayer playerIn, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) {
        ItemStack itemstack = playerIn.getHeldItem(hand);
        EnumLevel level = world.getBlockState(pos).getValue(LEVEL);
        TileEntityLitterBox tileEntityLitterBox = this.getTileEntity(world, pos);
        EnumDyeColor color = tileEntityLitterBox.getColor();

        if (tileEntityLitterBox == null)
            return false;
        else {
            if (level.equals(EnumLevel.EMPTY)) {
                if (itemstack.getItem() != Item.getItemFromBlock(Blocks.SAND))
                    return false;

                setLevel(world, pos, state, EnumLevel.CLEAN, color);
                if (!playerIn.capabilities.isCreativeMode)
                    itemstack.shrink(1);

            } else if (level.equals(EnumLevel.CLEAN)) {
                if (itemstack.getItem() == Items.BONE) // todo remove testing feature
                    setLevel(world, pos, state, EnumLevel.DIRTY, color);
                else {
                    ItemStack returnSand = new ItemStack(Blocks.SAND);
                    if (!playerIn.capabilities.isCreativeMode) {
                        if (itemstack.isEmpty())
                            playerIn.setHeldItem(hand, returnSand);
                        else if (!playerIn.addItemStackToInventory(returnSand))
                            playerIn.dropItem(returnSand, false);
                    }

                    setLevel(world, pos, state, EnumLevel.EMPTY, color);
                }

            } else {
                if (itemstack.getItem() != Item.getItemFromBlock(Blocks.SAND))
                    return false;

                setLevel(world, pos, state, EnumLevel.CLEAN, color);
                if (!playerIn.capabilities.isCreativeMode)
                    itemstack.shrink(1);
            }
        }

        world.notifyBlockUpdate(pos, state, state, 3);
        return true;
    }

    @Override
    public void getDrops(NonNullList<ItemStack> drops, IBlockAccess world, BlockPos pos, IBlockState state, int fortune) {
        super.getDrops(drops, world, pos, state, fortune);
        if (state.getValue(LEVEL).equals(EnumLevel.CLEAN))
            drops.add(new ItemStack(Blocks.SAND));
    }

    @Override
    public void harvestBlock(World worldIn, EntityPlayer player, BlockPos pos, IBlockState state, @Nullable TileEntity te, ItemStack stack) {
        if (te instanceof TileEntityLitterBox) {
            TileEntityLitterBox TileEntityLitterBox = (TileEntityLitterBox)te;
            ItemStack itemstack = TileEntityLitterBox.getItemStack();
            spawnAsEntity(worldIn, pos, itemstack);
        }
        else
            super.harvestBlock(worldIn, player, pos, state, null, stack);
    }

    @Override
    public Class<TileEntityLitterBox> getTileEntityClass() {
        return TileEntityLitterBox.class;
    }

    @Nullable
    @Override
    public TileEntityLitterBox createTileEntity(World world, IBlockState state) {
        return new TileEntityLitterBox();
    }

    @Override
    public IBlockState getStateForPlacement(World worldIn, BlockPos pos, EnumFacing facing, float hitX, float hitY, float hitZ, int meta, EntityLivingBase placer, EnumHand hand) {
        EnumFacing enumfacing = placer.getHorizontalFacing().rotateY();

        try {
            return super.getStateForPlacement(worldIn, pos, facing, hitX, hitY, hitZ, meta, placer).withProperty(FACING, enumfacing).withProperty(LEVEL, EnumLevel.byMetadata(meta >> 2));
        } catch (IllegalArgumentException var11) {
            if (!worldIn.isRemote) {
                System.out.println(String.format("Invalid level property for litter box at %s. Found %d, must be in [0, 1, 2]", pos, meta >> 2));

                if (placer instanceof EntityPlayer)
                    placer.sendMessage(new TextComponentTranslation("Invalid level property. Please pick in [0, 1, 2]", new Object[0]));
            }

            return super.getStateForPlacement(worldIn, pos, facing, hitX, hitY, hitZ, 0, placer).withProperty(FACING, enumfacing).withProperty(LEVEL, EnumLevel.byMetadata(0));
        }
    }

    @Override
    public IBlockState getActualState(IBlockState state, IBlockAccess world, BlockPos pos) {
        TileEntity tileEntity = world instanceof ChunkCache ? ((ChunkCache)world).getTileEntity(pos, Chunk.EnumCreateEntityType.CHECK) : world.getTileEntity(pos);
        if (tileEntity instanceof TileEntityLitterBox) {
            TileEntityLitterBox tileEntityLitterBox = (TileEntityLitterBox) tileEntity;
            return state.withProperty(LEVEL, state.getValue(LEVEL))
                    .withProperty(COLOR, tileEntityLitterBox.getColor())
                    .withProperty(FACING, state.getValue(FACING));
        }
        return state;
    }

    @Override
    public IBlockState getStateFromMeta(int meta) {
        return this.getDefaultState()
                .withProperty(FACING, EnumFacing.byHorizontalIndex(meta & 3))
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
        return new BlockStateContainer(this, new IProperty[] {
                FACING, LEVEL, COLOR
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
    public boolean isPassable(IBlockAccess world, BlockPos pos) {
        return false;
    }

    @Override
    public AxisAlignedBB getBoundingBox(IBlockState state, IBlockAccess source, BlockPos pos) {
        EnumFacing enumfacing = state.getValue(FACING);
        return enumfacing.getAxis() == EnumFacing.Axis.X ? X_AXIS_AABB : Z_AXIS_AABB;
    }

    @Override
    public Item createItemBlock() {
        return null;
    }

    @Override
    public void onBlockPlacedBy(World worldIn, BlockPos pos, IBlockState state, EntityLivingBase placer, ItemStack stack) {
        worldIn.setBlockState(pos, state, 2);

        TileEntity te = worldIn.getTileEntity(pos);
        if (te instanceof TileEntityLitterBox) {
            TileEntityLitterBox box = (TileEntityLitterBox) te;
            if (stack.getItem() instanceof ItemLitterBox) {
                EnumDyeColor color = ((ItemLitterBox) stack.getItem()).color;
                box.setColor(color);
            } else
                box.setColor(EnumDyeColor.BLACK);
        }
    }

    @Override
    public ItemStack getPickBlock(IBlockState state, RayTraceResult target, World world, BlockPos pos, EntityPlayer player) {
        TileEntity te = world.getTileEntity(target.getBlockPos());
        if (te instanceof TileEntityLitterBox) {
            TileEntityLitterBox box = (TileEntityLitterBox) te;
            return new ItemStack(ModItems.LITTER_BOXES.get(box.getColor()));
        } else
            return ItemStack.EMPTY;
    }

    @Override
    public Item getItemDropped(IBlockState state, Random rand, int fortune) {
        return ModItems.LITTER_BOXES.get(state.getValue(COLOR));
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
