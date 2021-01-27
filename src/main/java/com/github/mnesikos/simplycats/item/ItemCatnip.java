package com.github.mnesikos.simplycats.item;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Hand;

public class ItemCatnip extends ItemBase {
    public ItemCatnip() {
        super();
    }

    @Override
    public boolean itemInteractionForEntity(ItemStack stack, PlayerEntity playerIn, LivingEntity target, Hand hand) {
        return super.itemInteractionForEntity(stack, playerIn, target, hand);
    }
}
