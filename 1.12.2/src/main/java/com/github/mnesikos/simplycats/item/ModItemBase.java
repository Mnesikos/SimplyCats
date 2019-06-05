package com.github.mnesikos.simplycats.item;

import com.github.mnesikos.simplycats.Ref;
import com.github.mnesikos.simplycats.SimplyCats;
import com.github.mnesikos.simplycats.init.ModItems;
import net.minecraft.item.Item;

public class ModItemBase extends Item {

    public ModItemBase(String name) {
        setUnlocalizedName(name);
        setRegistryName(Ref.MODID + ":" + name);
        setCreativeTab(SimplyCats.PROXY.SIMPLYCATS);
        ModItems.ITEMS.add(this);
    }
}
