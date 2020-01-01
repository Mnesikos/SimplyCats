package com.github.mnesikos.simplycats.block;

import com.github.mnesikos.simplycats.Ref;
import com.github.mnesikos.simplycats.init.ModItems;
import net.minecraft.block.BlockCrops;
import net.minecraft.item.Item;

public class BlockCropCatnip extends BlockCrops {
    public BlockCropCatnip(String name) {
        this.setTranslationKey(name);
        this.setRegistryName(Ref.MODID + ":" + name);
    }

    @Override
    protected Item getSeed() {
        return ModItems.CATNIP_SEEDS;
    }

    @Override
    protected Item getCrop() {
        return ModItems.CATNIP;
    }
}
