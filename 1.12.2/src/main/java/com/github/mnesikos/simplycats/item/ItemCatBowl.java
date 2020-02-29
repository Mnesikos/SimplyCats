package com.github.mnesikos.simplycats.item;

import com.github.mnesikos.simplycats.Ref;
import com.github.mnesikos.simplycats.SimplyCats;
import com.github.mnesikos.simplycats.init.ModBlocks;
import net.minecraft.item.EnumDyeColor;
import net.minecraft.item.ItemBlock;

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

    public void registerItemModel() {
        SimplyCats.PROXY.registerItemRenderer(this, 0, name);
    }
}
