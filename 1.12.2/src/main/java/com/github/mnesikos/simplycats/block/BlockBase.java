package com.github.mnesikos.simplycats.block;

import com.github.mnesikos.simplycats.Ref;
import com.github.mnesikos.simplycats.SimplyCats;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;

public class BlockBase extends Block {
    protected String name;

    public BlockBase(Material material, String name) {
        super(material);
        this.name = name;
        this.setUnlocalizedName(name);
        this.setRegistryName(Ref.MODID + ":" + name);
        setCreativeTab(SimplyCats.PROXY.SIMPLYCATS);
    }

    public void registerItemModel(Item itemBlock) {
        SimplyCats.PROXY.registerItemRenderer(itemBlock, 0, name);
    }

    public Item createItemBlock() {
        return new ItemBlock(this).setRegistryName(getRegistryName());
    }
}
