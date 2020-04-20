package com.github.mnesikos.simplycats.item;

import com.github.mnesikos.simplycats.Ref;
import com.github.mnesikos.simplycats.SimplyCats;
import com.github.mnesikos.simplycats.init.ModBlocks;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemSeeds;

public class ItemCatnipSeeds extends ItemSeeds {
    protected String name;

    public ItemCatnipSeeds(String name) {
        super(ModBlocks.CROP_CATNIP, Blocks.FARMLAND);
        this.name = name;
        this.setUnlocalizedName(name);
        this.setRegistryName(Ref.MODID + ":" + name);
        setCreativeTab(SimplyCats.PROXY.SIMPLYCATS);
    }


    public void registerItemModel() {
        SimplyCats.PROXY.registerItemRenderer(this, 0, name);
    }
}
