package com.github.mnesikos.simplycats.item;

import com.github.mnesikos.simplycats.Ref;
import com.github.mnesikos.simplycats.SimplyCats;
import com.github.mnesikos.simplycats.init.ModItems;
import net.minecraft.item.Item;

public class ItemBase extends Item {

    public ItemBase(String name) {
        this.setUnlocalizedName(name);
        this.setTextureName(Ref.MODID + ":" + name);
        setCreativeTab(ModItems.CREATIVE_TAB);
    }
}
