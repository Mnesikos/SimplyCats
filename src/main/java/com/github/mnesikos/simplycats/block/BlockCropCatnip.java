package com.github.mnesikos.simplycats.block;

import com.github.mnesikos.simplycats.Ref;
import com.github.mnesikos.simplycats.init.ModItems;
import net.minecraft.block.Block;
import net.minecraft.block.CropsBlock;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.item.Item;

public class BlockCropCatnip extends CropsBlock {
    public BlockCropCatnip(String name) {
        super(Block.Properties.create(Material.PLANTS).doesNotBlockMovement().tickRandomly().hardnessAndResistance(0.0F).sound(SoundType.CROP));
        this.setRegistryName(Ref.MODID + ":" + name);
    }

    @Override
    protected Item getSeedsItem() {
        return ModItems.CATNIP_SEEDS;
    }
}
