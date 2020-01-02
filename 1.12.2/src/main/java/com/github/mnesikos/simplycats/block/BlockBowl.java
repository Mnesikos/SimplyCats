package com.github.mnesikos.simplycats.block;

import com.github.mnesikos.simplycats.SimplyCats;
import com.github.mnesikos.simplycats.tileentity.TileEntityBowl;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.InventoryHelper;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;

import javax.annotation.Nullable;

public class BlockBowl extends BlockTileEntity<TileEntityBowl> {
    protected String name;
    public static final int GUI_ID = 0;

    public BlockBowl(String name) {
        super(Material.GROUND, name);
        this.name = name;
        this.setSoundType(SoundType.STONE);
        this.setHardness(0.2F);
    }

    @Override
    public boolean onBlockActivated(World world, BlockPos pos, IBlockState state, EntityPlayer player, EnumHand hand, EnumFacing side, float hitX, float hitY, float hitZ) {
        if (!world.isRemote) {
            TileEntity tile = getTileEntity(world, pos);
            IItemHandler itemHandler = tile.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, side);
            if (player.isSneaking() && !player.getHeldItemMainhand().isEmpty()) {
                player.setHeldItem(hand, itemHandler.insertItem(0, player.getHeldItemMainhand(), false));
                tile.markDirty();
            } else
                player.openGui(SimplyCats.instance, GUI_ID, world, pos.getX(), pos.getY(), pos.getZ());
        }
        return true;
    }

    @Override
    public void breakBlock(World world, BlockPos pos, IBlockState state) {
        TileEntityBowl te = getTileEntity(world, pos);
        if (te instanceof IInventory)
            InventoryHelper.dropInventoryItems(world, pos, (IInventory)te);

        super.breakBlock(world, pos, state);
    }

    @Override
    public Class<TileEntityBowl> getTileEntityClass() {
        return TileEntityBowl.class;
    }

    @Nullable
    @Override
    public TileEntityBowl createTileEntity(World world, IBlockState state) {
        return new TileEntityBowl();
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
        float min = 0.34375F, max = 0.65625F;
        return new AxisAlignedBB(min, 0.0F, min, max, 0.125F, max);
    }
}
