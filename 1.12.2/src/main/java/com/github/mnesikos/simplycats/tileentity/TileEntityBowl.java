package com.github.mnesikos.simplycats.tileentity;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.common.util.Constants;

public class TileEntityBowl extends TileEntity implements IInventory {
    private ItemStack[] items = new ItemStack[10];

    @Override
    public int getSizeInventory() {
        return items.length;
    }

    @Override
    public ItemStack getStackInSlot(int index) {
        return items[index];
    }

    @Override
    public ItemStack decrStackSize(int index, int count) {
        if (items[index] != null) {
            ItemStack itemstack;

            if (items[index].getCount() == count) {
                itemstack = items[index];
                items[index] = null;
                markDirty();
                return itemstack;
            } else {
                itemstack = items[index].splitStack(count);
                if (items[index].getCount() == 0)
                    items[index] = null;
                markDirty();
                return itemstack;
            }
        } else
            return null;
    }

    // getStackInSlotOnClosing(int index) ???

    @Override
    public void setInventorySlotContents(int index, ItemStack stack) {
        items[index] = stack;
        if (stack != null && stack.getCount() > getInventoryStackLimit())
            stack.setCount(getInventoryStackLimit());
        markDirty();
    }

    @Override
    public String getName() {
        return "container.bowl";
    }

    @Override
    public boolean hasCustomName() {
        return false;
    }

    @Override
    public void readFromNBT(NBTTagCompound nbt) {
        super.readFromNBT(nbt);
        NBTTagList list = nbt.getTagList("Items", Constants.NBT.TAG_COMPOUND);
        items = new ItemStack[getSizeInventory()];

        for (int i = 0; i < list.tagCount(); i++) {
            NBTTagCompound comp = list.getCompoundTagAt(i);
            int j = comp.getByte("Slot") & 255;
            if (j >= 0 && j < items.length)
                items[j] = new ItemStack(comp);
        }
    }

    @Override
    public NBTTagCompound writeToNBT(NBTTagCompound nbt) {
        super.writeToNBT(nbt);
        NBTTagList list = new NBTTagList();

        for (int i = 0; i < items.length; i++) {
            if (items[i] != null) {
                NBTTagCompound comp = new NBTTagCompound();
                comp.setByte("Slot", (byte)i);
                items[i].writeToNBT(comp);
                list.appendTag(comp);
            }
        }

        nbt.setTag("Items", list);
        return nbt;
    }

    @Override
    public int getInventoryStackLimit() {
        return 16;
    }

    @Override
    public boolean isUsableByPlayer(EntityPlayer player) {
        return world.getTileEntity(pos) != this ? false : player.getDistanceSq((double)pos.getX() + 0.5D, (double)pos.getY() + 0.5D, (double)pos.getZ() + 0.5D) <= 64.0D;
    }

    @Override
    public void openInventory(EntityPlayer player) {

    }

    @Override
    public void closeInventory(EntityPlayer player) {

    }

    @Override
    public boolean isItemValidForSlot(int index, ItemStack stack) {
        return true;
    }

    @Override
    public int getField(int id) {
        return 0;
    }

    @Override
    public void setField(int id, int value) {

    }

    @Override
    public int getFieldCount() {
        return 0;
    }

    @Override
    public void clear() {

    }

    @Override
    public boolean isEmpty() {
        return false;
    }

    @Override
    public ItemStack removeStackFromSlot(int index) {
        return null;
    }
}
