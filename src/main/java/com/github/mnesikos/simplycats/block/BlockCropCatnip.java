package com.github.mnesikos.simplycats.block;

import com.github.mnesikos.simplycats.item.CatItems;
import net.minecraft.block.BlockCrops;
import net.minecraft.item.Item;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraftforge.common.EnumPlantType;
import net.minecraftforge.fml.common.registry.ForgeRegistries;

public class BlockCropCatnip extends BlockCrops {
    private Item seeds;

    @Override
    public Item getSeed() {
        if (seeds == null) {
            seeds = ForgeRegistries.ITEMS.getValue(getRegistryName());
        }
        return seeds;
    }

    @Override
    protected Item getCrop() {
        return CatItems.CATNIP;
    }

    @Override
    public EnumPlantType getPlantType(IBlockAccess world, BlockPos pos) {
        return EnumPlantType.Crop;
    }
}
