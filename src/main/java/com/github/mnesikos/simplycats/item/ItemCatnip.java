package com.github.mnesikos.simplycats.item;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumHand;

public class ItemCatnip extends ItemBase {
    public ItemCatnip(String name) {
        super(name);
    }

    @Override
    public boolean itemInteractionForEntity(ItemStack stack, EntityPlayer playerIn, EntityLivingBase target, EnumHand hand) {
        return super.itemInteractionForEntity(stack, playerIn, target, hand);
    }
}
