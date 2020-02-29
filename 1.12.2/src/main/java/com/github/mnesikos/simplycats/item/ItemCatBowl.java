package com.github.mnesikos.simplycats.item;

import com.github.mnesikos.simplycats.Ref;
import com.github.mnesikos.simplycats.SimplyCats;
import com.github.mnesikos.simplycats.init.ModBlocks;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.EnumDyeColor;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;

public class ItemCatBowl extends ItemBlock {
    protected String name;
    public final EnumDyeColor color;

    public ItemCatBowl(String name, EnumDyeColor color) {
        super(ModBlocks.BOWL);
        this.name = name;
        this.color = color;
        String s = name + "_" + color.getName();
        this.setTranslationKey(s);
        this.setRegistryName(Ref.MODID + ":" + s);
        setCreativeTab(SimplyCats.PROXY.SIMPLYCATS);
    }

    @Override
    public void getSubItems(CreativeTabs tab, NonNullList<ItemStack> items) {
        if(isInCreativeTab(tab)) items.add(new ItemStack(this));
    }

    public void registerItemModel() {
        SimplyCats.PROXY.registerItemRenderer(this, 0, name);
    }
}
