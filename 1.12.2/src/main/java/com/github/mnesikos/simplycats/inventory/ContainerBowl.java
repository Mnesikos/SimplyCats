package com.github.mnesikos.simplycats.inventory;

import com.github.mnesikos.simplycats.tileentity.TileEntityBowl;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;

import java.awt.*;
// TODO
public class ContainerBowl extends Container {/*
    private TileEntityBowl te;

    public ContainerBowl(TileEntityBowl te, EntityPlayer player) {
        this.te = te;

        //Bowl Storage
        for (int i = 0; i < 2; i++)
            for (int j = 0; j < 5; j++)
                addSlotToContainer(new Slot(te, j + i * 5, 44 + j * 18, 17 + i * 18));

        //Player Inventory
        for (int i = 0; i < 3; i++)
            for (int j = 0; j < 9; j++)
                addSlotToContainer(new Slot(player.inventory, j + i * 9 + 9, 8 + j * 18, 84 + i * 18));

        //Hotbar
        for (int i = 0; i < 9; i++)
            addSlotToContainer(new Slot(player.inventory, i, 8 + i * 18, 142));
    }

    @Override
    protected boolean mergeItemStack(ItemStack itemstack, int startIndex, int endIndex, boolean reverseDirection){
        boolean flag = false;
        int i = startIndex;
        if (reverseDirection) i = endIndex - 1;

        if (itemstack.isStackable()){
            while (itemstack.stackSize > 0 && (!reverseDirection && i < endIndex || reverseDirection && i >= startIndex)){
                Slot slot = (Slot)this.inventorySlots.get(i);
                ItemStack stackInSlot = slot.getStack();
                int maxLimit = Math.min(itemstack.getMaxStackSize(), slot.getSlotStackLimit());

                if (stackInSlot != null && ItemStack.areItemStacksEqual(itemstack, stackInSlot)){
                    int j = stackInSlot.stackSize + itemstack.stackSize;
                    if (j <= maxLimit){
                        itemstack.stackSize = 0;
                        stackInSlot.stackSize = j;
                        slot.onSlotChanged();
                        flag = true;

                    }else if (stackInSlot.stackSize < maxLimit){
                        itemstack.stackSize -= maxLimit - stackInSlot.stackSize;
                        stackInSlot.stackSize = maxLimit;
                        slot.onSlotChanged();
                        flag = true;
                    }
                }
                if (reverseDirection){
                    --i;
                }else ++i;
            }
        }
        if (itemstack.stackSize > 0){
            if (reverseDirection){
                i = endIndex - 1;
            }else i = startIndex;

            while (!reverseDirection && i < endIndex || reverseDirection && i >= startIndex){
                Slot slot1 = (Slot)this.inventorySlots.get(i);
                ItemStack itemstack1 = slot1.getStack();

                if (itemstack1 == null && slot1.isItemValid(itemstack)){ // Forge: Make sure to respect isItemValid in the slot.
                    if(itemstack.stackSize <= slot1.getSlotStackLimit()){
                        slot1.putStack(itemstack.copy());
                        slot1.onSlotChanged();
                        itemstack.stackSize = 0;
                        flag = true;
                        break;
                    }else{
                        itemstack1 = itemstack.copy();
                        itemstack.stackSize -= slot1.getSlotStackLimit();
                        itemstack1.stackSize = slot1.getSlotStackLimit();
                        slot1.putStack(itemstack1);
                        slot1.onSlotChanged();
                        flag = true;
                    }
                }
                if (reverseDirection){
                    --i;
                }else ++i;
            }
        }
        return flag;
    }

    @Override
    public ItemStack transferStackInSlot(EntityPlayer player, int parSlot) {
        ItemStack itemstack = null;
        Slot slot = (Slot)this.inventorySlots.get(parSlot);

        if (slot != null && slot.getHasStack()) {
            ItemStack itemstack1 = slot.getStack();
            itemstack = itemstack1.copy();

            // If itemstack is in bowl inventory
            if (parSlot < 10) {
                // try to place in player inventory / action bar;
                // mergeItemStack uses < index, so the last slot in the inventory won't get checked if you don't add 1
                if (!this.mergeItemStack(itemstack1, 10, 45+1, false))
                    return null;

                slot.onSlotChange(itemstack1, itemstack);
            }
            // itemstack is in player inventory, try to place in bowl inventory
            else if (parSlot > 9) {
                // try to place in bowl inventory; add 1 to final input slot because mergeItemStack uses < index
                if (!this.mergeItemStack(itemstack1, 0, 9+1, false))
                    return null;
            }

            if (itemstack1.stackSize == 0)
                slot.putStack((ItemStack)null);
            else
                slot.onSlotChanged();

            if (itemstack1.stackSize == itemstack.stackSize)
                return null;

            slot.onPickupFromSlot(player, itemstack1);
        }
        return itemstack;
    }

    @Override
    public boolean canInteractWith(EntityPlayer player) {
        return te.isUseableByPlayer(player);
    }
*/
}
