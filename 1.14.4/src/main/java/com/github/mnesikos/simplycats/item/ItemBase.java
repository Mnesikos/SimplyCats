package com.github.mnesikos.simplycats.item;

import com.github.mnesikos.simplycats.Ref;
import com.github.mnesikos.simplycats.SimplyCats;
import net.minecraft.item.Item;

public class ItemBase extends Item {
    protected String name;

    public ItemBase(String name) {
        super(new Item.Properties().group(SimplyCats.GROUP));
        this.name = name;
        this.setRegistryName(Ref.MODID + ":" + name);
    }

    public ItemBase(String name, Properties prop) {
        super(prop);
        this.name = name;
        this.setRegistryName(Ref.MODID + ":" + name);
    }
}
