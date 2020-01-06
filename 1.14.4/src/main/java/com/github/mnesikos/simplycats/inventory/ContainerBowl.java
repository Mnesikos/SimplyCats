package com.github.mnesikos.simplycats.inventory;

import com.github.mnesikos.simplycats.tileentity.TileEntityBowl;
import net.minecraft.inventory.*;
import net.minecraft.item.ItemStack;

/*public class ContainerBowl extends Container {
    private TileEntityBowl tileEntityBowl;

    public ContainerBowl(InventoryPlayer player, final TileEntityBowl bowl) {
        this.tileEntityBowl = bowl;
        //IItemHandler inventory = bowl.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, EnumFacing.NORTH);

        //Bowl Storage
        for (int i = 0; i < 2; i++) {
            for (int j = 0; j < 5; j++) {
                 addSlotToContainer(new SlotCatFood(tileEntityBowl, j + i * 5, 44 + j * 18, 21 + i * 18) {
                    @Override
                    public void onSlotChanged() {
                        bowl.markDirty();
                    }
                });
            }
        }

        //Player Inventory
        for (int i = 0; i < 3; i++)
            for (int j = 0; j < 9; j++)
                addSlotToContainer(new Slot(player, j + i * 9 + 9, 8 + j * 18, 74 + i * 18));

        //Hotbar
        for (int i = 0; i < 9; i++)
            addSlotToContainer(new Slot(player, i, 8 + i * 18, 132));
    }

    @Override
    public ItemStack transferStackInSlot(EntityPlayer player, int index) {
        ItemStack itemstack = ItemStack.EMPTY;
        Slot slot = inventorySlots.get(index);

        if (slot != null && slot.getHasStack()) {
            ItemStack itemstack1 = slot.getStack();
            itemstack = itemstack1.copy();

            int containerSlots = inventorySlots.size() - player.inventory.mainInventory.size();

            if (index < containerSlots) {
                if (!this.mergeItemStack(itemstack1, containerSlots, inventorySlots.size(), true)) {
                    return ItemStack.EMPTY;
                }
            } else if (!this.mergeItemStack(itemstack1, 0, containerSlots, false)) {
                return ItemStack.EMPTY;
            }

            if (itemstack1.getCount() == 0) {
                slot.putStack(ItemStack.EMPTY);
            } else {
                slot.onSlotChanged();
            }

            if (itemstack1.getCount() == itemstack.getCount()) {
                return ItemStack.EMPTY;
            }

            slot.onTake(player, itemstack1);
        }

        return itemstack;
    }

    @Override
    public boolean canInteractWith(EntityPlayer player) {
        return tileEntityBowl.isUsableByPlayer(player);
    }
}*/