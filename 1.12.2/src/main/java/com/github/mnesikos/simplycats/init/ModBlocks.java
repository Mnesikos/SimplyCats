package com.github.mnesikos.simplycats.init;

import com.github.mnesikos.simplycats.block.BlockBowl;
import com.github.mnesikos.simplycats.block.BlockCropCatnip;
import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.registries.IForgeRegistry;

public class ModBlocks {
    public static BlockCropCatnip CROP_CATNIP = new BlockCropCatnip("crop_catnip");
    public static BlockBowl BOWL = new BlockBowl("cat_bowl");

    public static void register(IForgeRegistry<Block> registry) {
        registry.registerAll(
                CROP_CATNIP,
                BOWL
        );
        GameRegistry.registerTileEntity(BOWL.getTileEntityClass(), new ResourceLocation(BOWL.getRegistryName().toString()));
    }

    public static void registerItemBlocks(IForgeRegistry<Item> registry) {
        registry.registerAll(
                BOWL.createItemBlock()
        );
    }

    public static void registerModels() {
        BOWL.registerItemModel(Item.getItemFromBlock(BOWL));
    }
}
