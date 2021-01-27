package com.github.mnesikos.simplycats.item;

import com.github.mnesikos.simplycats.SimplyCats;
import com.github.mnesikos.simplycats.init.ModBlocks;
import net.minecraft.item.BlockNamedItem;
import net.minecraft.item.Item;

public class ItemCatnipSeeds extends BlockNamedItem {
    public ItemCatnipSeeds() {
        super(ModBlocks.CROP_CATNIP.get(), new Item.Properties().group(SimplyCats.GROUP));
    }
}
