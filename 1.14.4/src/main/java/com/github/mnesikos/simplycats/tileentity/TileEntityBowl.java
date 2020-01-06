package com.github.mnesikos.simplycats.tileentity;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SUpdateTileEntityPacket;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.Direction;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.items.ItemStackHandler;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class TileEntityBowl extends TileEntity implements IInventory {
    private ItemStackHandler inventory = new ItemStackHandler(10);

    public TileEntityBowl(TileEntityType<?> tileEntityTypeIn) {
        super(tileEntityTypeIn);
    }

    @Override
    public CompoundNBT write(CompoundNBT nbt) {
        super.write(nbt);
        nbt.put("inventory", inventory.serializeNBT());
        return nbt;
    }

    @Override
    public void read(CompoundNBT nbt) {
        super.read(nbt);
        inventory.deserializeNBT(nbt.getCompound("inventory"));
    }

    /*@Override
    public boolean hasCapability(Capability<?> capability, @Nullable EnumFacing facing) {
        return capability == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY || super.hasCapability(capability, facing);
    }*/

    @Nonnull
    @Override
    public <T> LazyOptional<T> getCapability(@Nonnull Capability<T> capability, @Nullable Direction facing) {
        return /*capability == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY ? (T)inventory :*/ super.getCapability(capability, facing);
    }

    @Override
    public int getSizeInventory() {
        return inventory.getSlots();
    }

    @Override
    public ItemStack getStackInSlot(int index) {
        return inventory.getStackInSlot(index);
    }

    @Override
    public ItemStack decrStackSize(int index, int count) {
        if (inventory != null) {
            ItemStack itemstack;

            if (inventory.getStackInSlot(index).getCount() == count) {
                itemstack = inventory.getStackInSlot(index);
                inventory.setStackInSlot(index, ItemStack.EMPTY);
                markDirty();
                return itemstack;
            } else {
                itemstack = inventory.getStackInSlot(index).split(count);
                if (inventory.getStackInSlot(index).getCount() == 0)
                    inventory.setStackInSlot(index, ItemStack.EMPTY);
                markDirty();
                return itemstack;
            }
        } else
            return null;
    }

    @Override
    public void setInventorySlotContents(int index, ItemStack stack) {
        inventory.setStackInSlot(index, stack);
        if (stack != null && stack.getCount() > getInventoryStackLimit())
            stack.setCount(getInventoryStackLimit());
        markDirty();
        this.world.notifyBlockUpdate(this.pos, this.getBlockState().getBlock().getDefaultState(), this.getBlockState().getBlock().getDefaultState(), 3);
    }

    /*@Override
    public String getName() {
        return "container.cat_bowl.name";
    }

    @Override
    public boolean hasCustomName() {
        return false;
    }

    @Nullable
    @Override
    public ITextComponent getDisplayName() {
        return this.hasCustomName() ? new StringTextComponent(this.getName()) : new TranslationTextComponent(this.getName());
    }*/

    @Override
    public int getInventoryStackLimit() {
        return 16;
    }

    @Override
    public boolean isUsableByPlayer(PlayerEntity player) {
        return world.getTileEntity(pos) == this && player.getDistanceSq((double)pos.getX() + 0.5D, (double)pos.getY() + 0.5D, (double)pos.getZ() + 0.5D) <= 64.0D;
    }

    @Override
    @Nullable
    public SUpdateTileEntityPacket getUpdatePacket() {
        CompoundNBT updateTagDescribingTileEntityState = getUpdateTag();
        final int METADATA = 0;
        return new SUpdateTileEntityPacket(this.pos, METADATA, updateTagDescribingTileEntityState);
    }

    @Override
    public void onDataPacket(NetworkManager net, SUpdateTileEntityPacket pkt) {
        CompoundNBT updateTagDescribingTileEntityState = pkt.getNbtCompound();
        handleUpdateTag(updateTagDescribingTileEntityState);
    }

    @Override
    public CompoundNBT getUpdateTag() {
        CompoundNBT nbtTagCompound = new CompoundNBT();
        write(nbtTagCompound);
        return nbtTagCompound;
    }

    @Override
    public void handleUpdateTag(CompoundNBT tag) {
        this.read(tag);
    }

    @Override
    public void openInventory(PlayerEntity player) {}

    @Override
    public void closeInventory(PlayerEntity player) {}

    @Override
    public boolean isItemValidForSlot(int index, ItemStack stack) {
        return true;
    }

    /*@Override
    public int getField(int id) {
        return 0;
    }

    @Override
    public void setField(int id, int value) {}

    @Override
    public int getFieldCount() {
        return 0;
    }*/

    @Override
    public void clear() {}

    @Override
    public boolean isEmpty() {
        for (int i = 0; i < inventory.getSlots(); i++) {
            if (!inventory.getStackInSlot(i).isEmpty())
                return false;
        }

        return true;
    }

    @Override
    public ItemStack removeStackFromSlot(int index) {
        return null;
    }
}
