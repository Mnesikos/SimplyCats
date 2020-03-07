package com.github.mnesikos.simplycats.item;

import com.github.mnesikos.simplycats.Ref;
import com.github.mnesikos.simplycats.SimplyCats;
import com.github.mnesikos.simplycats.init.ModBlocks;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.EnumDyeColor;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;

public class ItemLitterBox extends ItemBlock {
    protected String name;
    public final EnumDyeColor color;

    public ItemLitterBox(String name, EnumDyeColor color) {
        super(ModBlocks.LITTER_BOX);
        this.color = color;
        this.name = name + "_" + color.getName();
        this.setTranslationKey(this.name);
        this.setRegistryName(Ref.MODID + ":" + this.name);
        setCreativeTab(SimplyCats.PROXY.SIMPLYCATS);
    }

    @Override
    public String getTranslationKey() {
        return "block." + this.name;
    }

    @Override
    public String getTranslationKey(ItemStack stack) {
        return "block." + this.name;
    }

    @Override
    public void getSubItems(CreativeTabs tab, NonNullList<ItemStack> items) {
        if(isInCreativeTab(tab)) items.add(new ItemStack(this));
    }

    public void registerItemModel() {
        SimplyCats.PROXY.registerItemRenderer(this, 0, name);
    }
}
