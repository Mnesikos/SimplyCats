package com.github.mnesikos.simplycats.block;

import com.github.mnesikos.simplycats.Ref;
import com.github.mnesikos.simplycats.init.ModItems;
import net.minecraft.block.BlockCrops;
import net.minecraft.item.Item;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraftforge.common.EnumPlantType;

public class BlockCropCatnip extends BlockCrops {
    public BlockCropCatnip(String name) {
        this.setUnlocalizedName(name);
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

    @Override
    public EnumPlantType getPlantType(IBlockAccess world, BlockPos pos) {
        return EnumPlantType.Crop;
    }
}
