package com.github.mnesikos.simplycats.init;

import com.github.mnesikos.simplycats.block.BlockCatBowl;
import com.github.mnesikos.simplycats.block.BlockCropCatnip;
import com.github.mnesikos.simplycats.block.BlockLitterBox;
import com.github.mnesikos.simplycats.block.BlockScratchingPost;
import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.registries.IForgeRegistry;

import java.util.Objects;

public class ModBlocks {
    public static BlockCropCatnip CROP_CATNIP = new BlockCropCatnip("crop_catnip");
    public static BlockCatBowl CAT_BOWL = new BlockCatBowl("cat_bowl");
    public static BlockLitterBox LITTER_BOX = new BlockLitterBox("litter_box");
    public static BlockScratchingPost SCRATCHING_POST = new BlockScratchingPost("scratching_post");

    public static void register(IForgeRegistry<Block> registry) {
        registry.registerAll(
                CROP_CATNIP,
                CAT_BOWL,
                LITTER_BOX,
                SCRATCHING_POST
        );

        GameRegistry.registerTileEntity(CAT_BOWL.getTileEntityClass(), Objects.requireNonNull(CAT_BOWL.getRegistryName()));
    }

    public static void registerItemBlocks(IForgeRegistry<Item> registry) {
        registry.registerAll(
                LITTER_BOX.createItemBlock()
        );
    }

    public static void registerModels() {
        LITTER_BOX.registerItemModel(Item.getItemFromBlock(LITTER_BOX));
    }
}
