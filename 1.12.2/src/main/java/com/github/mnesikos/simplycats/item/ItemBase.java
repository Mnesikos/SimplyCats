package com.github.mnesikos.simplycats.item;

import com.github.mnesikos.simplycats.Ref;
import com.github.mnesikos.simplycats.SimplyCats;
import net.minecraft.item.Item;
import net.minecraft.util.ResourceLocation;

public class ItemBase extends Item {
    protected String name;

    public ItemBase(String name) {
        this.name = name;
        this.setUnlocalizedName(name);
        this.setRegistryName(new ResourceLocation(Ref.MODID, name));
        setCreativeTab(SimplyCats.PROXY.SIMPLYCATS);
    }

    public void registerItemModel() {
        SimplyCats.PROXY.registerItemRenderer(this, 0, name);
    }
}
