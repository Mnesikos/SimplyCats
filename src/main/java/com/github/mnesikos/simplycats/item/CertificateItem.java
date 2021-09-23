package com.github.mnesikos.simplycats.item;

import com.github.mnesikos.simplycats.SimplyCats;
import net.minecraft.item.Item;

public class CertificateItem extends Item {
    private final boolean adoption;

    public CertificateItem(boolean adoption) {
        super(new Item.Properties().tab(SimplyCats.ITEM_GROUP));
        this.adoption = adoption;
    }
}
