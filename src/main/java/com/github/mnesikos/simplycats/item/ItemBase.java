package com.github.mnesikos.simplycats.item;

import com.github.mnesikos.simplycats.Ref;
import com.github.mnesikos.simplycats.SimplyCats;
import net.minecraft.item.Item;

public class ItemBase extends Item {
    public ItemBase(String name) {
        this(name, new Item.Properties().group(SimplyCats.GROUP));
    }

    public ItemBase(String name, Properties prop) {
        super(prop);
        this.setRegistryName(Ref.MODID + ":" + name);
    }
}
