package com.github.mnesikos.simplycats.block;

import com.github.mnesikos.simplycats.SimplyCats;
import com.github.mnesikos.simplycats.tileentity.TileEntityCatBowl;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.PropertyBool;
import net.minecraft.block.properties.PropertyInteger;
import net.minecraft.block.state.BlockFaceShape;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.Items;
import net.minecraft.init.PotionTypes;
import net.minecraft.init.SoundEvents;
import net.minecraft.inventory.InventoryHelper;
import net.minecraft.item.EnumDyeColor;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.PotionUtils;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.ChunkCache;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraft.world.chunk.Chunk;

import javax.annotation.Nullable;

public class BlockCatBowl extends BlockTileEntity<TileEntityCatBowl> {
    public static final int GUI_ID = 0;

    public static final PropertyBool FULL_FOOD = PropertyBool.create("full_food");
    public static final PropertyInteger WATER_LEVEL = PropertyInteger.create("water_level", 0, 3);
    protected static final AxisAlignedBB BOWL_AABB = new AxisAlignedBB(0.34375F, 0.0F, 0.34375F, 0.65625F, 0.125F, 0.65625F);
    private final EnumDyeColor color;

    public BlockCatBowl(EnumDyeColor color) {
        super(Material.GROUND);
        this.color = color;
        this.setSoundType(SoundType.STONE);
        this.setHardness(0.2F);
        this.setDefaultState(this.blockState.getBaseState()
                .withProperty(FULL_FOOD, Boolean.FALSE)
                .withProperty(WATER_LEVEL, 0));
    }

    public void setWaterLevel(World world, BlockPos pos, IBlockState state, int level) {
        world.setBlockState(pos, state.withProperty(WATER_LEVEL, MathHelper.clamp(level, 0, 3)), 2);
        world.updateComparatorOutputLevel(pos, this);
    }

    @Override
    public boolean onBlockActivated(World world, BlockPos pos, IBlockState state, EntityPlayer player, EnumHand hand, EnumFacing side, float hitX, float hitY, float hitZ) {
        if (this.getTileEntity(world, pos) == null)
            return false;

        TileEntityCatBowl tileEntityCatBowl = this.getTileEntity(world, pos);

        if (tileEntityCatBowl == null)
            return false;
        else {
            int waterLevel = (state.getValue(WATER_LEVEL));
            ItemStack itemstack = player.getHeldItem(hand);

            if (tileEntityCatBowl.isEmpty() && itemstack.getItem() == Items.WATER_BUCKET && waterLevel < 3 && !world.isRemote) {
                if (!player.capabilities.isCreativeMode)
                    player.setHeldItem(hand, new ItemStack(Items.BUCKET));

                this.setWaterLevel(world, pos, state, 3);
                world.playSound(null, pos, SoundEvents.ITEM_BUCKET_EMPTY, SoundCategory.BLOCKS, 1.0F, 1.0F);
                return true;

            } else if (tileEntityCatBowl.isEmpty() && itemstack.getItem() == Items.BUCKET && waterLevel == 3 && !world.isRemote) {
                if (!player.capabilities.isCreativeMode) {
                    itemstack.shrink(1);

                    if (itemstack.isEmpty())
                        player.setHeldItem(hand, new ItemStack(Items.WATER_BUCKET));
                    else if (!player.inventory.addItemStackToInventory(new ItemStack(Items.WATER_BUCKET)))
                        player.dropItem(new ItemStack(Items.WATER_BUCKET), false);
                }

                this.setWaterLevel(world, pos, state, 0);
                world.playSound(null, pos, SoundEvents.ITEM_BUCKET_FILL, SoundCategory.BLOCKS, 1.0F, 1.0F);
                return true;

            } else if (tileEntityCatBowl.isEmpty() && itemstack.getItem() == Items.POTIONITEM && PotionUtils.getPotionFromItem(itemstack) == PotionTypes.WATER && waterLevel < 3 && !world.isRemote) {
                if (!player.capabilities.isCreativeMode) {
                    player.setHeldItem(hand, new ItemStack(Items.GLASS_BOTTLE));
                    if (player instanceof EntityPlayerMP)
                        ((EntityPlayerMP) player).sendContainerToPlayer(player.inventoryContainer);
                }

                this.setWaterLevel(world, pos, state, waterLevel + 1);
                world.playSound(null, pos, SoundEvents.ITEM_BOTTLE_EMPTY, SoundCategory.BLOCKS, 1.0F, 1.0F);
                return true;

            } else if (waterLevel == 0 && !world.isRemote)
                player.openGui(SimplyCats.instance, GUI_ID, world, pos.getX(), pos.getY(), pos.getZ());

            return true;
        }
    }

    @Override
    public void breakBlock(World world, BlockPos pos, IBlockState state) {
        TileEntityCatBowl te = getTileEntity(world, pos);
        if (te != null)
            InventoryHelper.dropInventoryItems(world, pos, te);

        super.breakBlock(world, pos, state);
    }

    @Override
    public void harvestBlock(World worldIn, EntityPlayer player, BlockPos pos, IBlockState state, @Nullable TileEntity te, ItemStack stack) {
        if (te instanceof TileEntityCatBowl) {
            ItemStack itemstack = new ItemStack(this);
            spawnAsEntity(worldIn, pos, itemstack);
        } else
            super.harvestBlock(worldIn, player, pos, state, null, stack);
    }

    public boolean hasComparatorInputOverride(IBlockState state) {
        return true;
    }

    public int getComparatorInputOverride(IBlockState blockState, World worldIn, BlockPos pos) {
        return blockState.getValue(WATER_LEVEL);
    }

    @Override
    public Class<TileEntityCatBowl> getTileEntityClass() {
        return TileEntityCatBowl.class;
    }

    @Nullable
    @Override
    public TileEntityCatBowl createTileEntity(World world, IBlockState state) {
        return new TileEntityCatBowl();
    }

    @Override
    public IBlockState getActualState(IBlockState state, IBlockAccess worldIn, BlockPos pos) {
        TileEntity tileEntity = worldIn instanceof ChunkCache ? ((ChunkCache) worldIn).getTileEntity(pos, Chunk.EnumCreateEntityType.CHECK) : worldIn.getTileEntity(pos);
        if (tileEntity instanceof TileEntityCatBowl) {
            TileEntityCatBowl tileEntityCatBowl = (TileEntityCatBowl) tileEntity;
            return state.withProperty(FULL_FOOD, !tileEntityCatBowl.isEmpty());
        }
        return state;
    }

    @Override
    public IBlockState getStateFromMeta(int meta) {
        return this.getDefaultState().withProperty(WATER_LEVEL, Integer.valueOf(meta));
    }

    @Override
    public int getMetaFromState(IBlockState state) {
        return state.getValue(WATER_LEVEL);
    }

    @Override
    protected BlockStateContainer createBlockState() {
        return new BlockStateContainer(this, FULL_FOOD, WATER_LEVEL);
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
    public BlockFaceShape getBlockFaceShape(IBlockAccess worldIn, IBlockState state, BlockPos pos, EnumFacing face) {
        return BlockFaceShape.UNDEFINED;
    }

    @Override
    public AxisAlignedBB getBoundingBox(IBlockState state, IBlockAccess source, BlockPos pos) {
        return BOWL_AABB;
    }
}
