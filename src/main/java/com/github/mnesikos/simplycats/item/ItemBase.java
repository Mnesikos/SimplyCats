package com.github.mnesikos.simplycats.item;

import com.github.mnesikos.simplycats.SimplyCats;
import net.minecraft.item.Item;

public class ItemBase extends Item {
    public ItemBase() {
        this(new Item.Properties().group(SimplyCats.GROUP));
    }

    public ItemBase(Properties prop) {
        super(prop);
    }
}
