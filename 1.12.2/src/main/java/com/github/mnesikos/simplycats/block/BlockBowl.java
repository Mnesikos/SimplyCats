package com.github.mnesikos.simplycats.block;

import com.github.mnesikos.simplycats.SimplyCats;
import com.github.mnesikos.simplycats.tileentity.TileEntityBowl;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
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
import java.util.ArrayList;

public class BlockBowl extends BlockTileEntity<TileEntityBowl> {
    protected String name;
    public static final int GUI_ID = 0;
    float min = 0.34375F; float max = 0.65625F;

    public BlockBowl(String name) {
        super(Material.ROCK, name);
        this.name = name;
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
        ArrayList<ItemStack> drops = new ArrayList<>();
        TileEntityBowl te = getTileEntity(world, pos);
        if (te != null) {
            for (int i = 0; i < te.getSizeInventory(); i++) {
                ItemStack stack = te.getStackInSlot(i);
                if (stack != null) drops.add(stack.copy());
            }
        }

        for (int i = 0; i < drops.size(); i++) {
            EntityItem item = new EntityItem(world, pos.getX(), pos.getY(), pos.getZ(), drops.get(i));
            world.spawnEntity(item);
        }

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

    /*@SideOnly(Side.CLIENT)
    public BlockRenderLayer getBlockLayer() {
        return BlockRenderLayer.SOLID;
    }*/

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

    /*@Override
    public EnumBlockRenderType getRenderType(IBlockState state) {
        return EnumBlockRenderType.MODEL;
    }*/

    @Override
    public AxisAlignedBB getBoundingBox(IBlockState state, IBlockAccess source, BlockPos pos) {
        return new AxisAlignedBB(min, 0.0F, min, max, 0.125F, max);
    }
}
