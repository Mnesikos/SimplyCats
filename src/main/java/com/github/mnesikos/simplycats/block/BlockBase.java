package com.github.mnesikos.simplycats.block;

import com.github.mnesikos.simplycats.Ref;
import com.github.mnesikos.simplycats.SimplyCats;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;

public class BlockBase extends Block {
    public BlockBase(Material material, String name) {
        this(name, Block.Properties.create(material));
    }

    public BlockBase(String name, Properties properties) {
        super(properties);
        this.setRegistryName(Ref.MOD_ID + ":" + name);
    }

    public Item createItemBlock() {
        return new BlockItem(this, new Item.Properties().group(SimplyCats.GROUP)).setRegistryName(getRegistryName());
    }
}
