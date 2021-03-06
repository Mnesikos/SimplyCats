package com.github.mnesikos.simplycats.init;

import com.github.mnesikos.simplycats.Ref;
import com.github.mnesikos.simplycats.SimplyCats;
import com.github.mnesikos.simplycats.block.*;
import net.minecraft.block.Block;
import net.minecraft.block.BlockPlanks;
import net.minecraft.init.Blocks;
import net.minecraft.item.EnumDyeColor;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemSeeds;
import net.minecraft.util.math.AxisAlignedBB;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

public class CatBlocks {
    public static final List<Block> BLOCKS = new ArrayList<>();
    public static final BlockCropCatnip CROP_CATNIP = registerBlock("crop_catnip", new BlockCropCatnip(), block -> new ItemSeeds(block, Blocks.FARMLAND));
    public static final Map<EnumDyeColor, BlockCatBowl> CAT_BOWLS = new HashMap<>();
    public static final Map<EnumDyeColor, BlockLitterBox> LITTER_BOXES = new HashMap<>();
    public static final Map<BlockPlanks.EnumType, BlockScratchingPost> SCRATCHING_POSTS = new HashMap<>();
    public static final Map<EnumDyeColor, BlockCatTree.Bed> CAT_TREE_BEDS = new HashMap<>();
    public static final Map<EnumDyeColor, BlockCatTree> CAT_TREE_POSTS = new HashMap<>();
    public static final Map<EnumDyeColor, BlockCatTree.Box> CAT_TREE_BOXES = new HashMap<>();

    public static <T extends Block> T registerBlock(String name, T block) {
        return registerBlock(name, block, ItemBlock::new);
    }

    public static <T extends Block> T registerBlock(String name, T block, Function<T, Item> itemFunction) {
        block.setRegistryName(name);
        block.setUnlocalizedName(Ref.MODID + "." + name);
        block.setCreativeTab(SimplyCats.PROXY.SIMPLYCATS);
        Item item = itemFunction.apply(block);
        CatItems.registerItem(name, item);
        BLOCKS.add(block);
        return block;
    }

    static {
        for (EnumDyeColor color : EnumDyeColor.values()) {
            CAT_BOWLS.put(color, registerBlock("cat_bowl_" + color.getName(), new BlockCatBowl(color)));
            LITTER_BOXES.put(color, registerBlock("litter_box_" + color.getName(), new BlockLitterBox()));
            CAT_TREE_BEDS.put(color, registerBlock("cat_tree_bed_" + color.getName(), new BlockCatTree.Bed(color, new AxisAlignedBB(0.0F, 0.0F, 0.0F, 1.0F, 0.375F, 1.0F))));
            CAT_TREE_POSTS.put(color, registerBlock("cat_tree_post_" + color.getName(), new BlockCatTree(color, new AxisAlignedBB(0.3125F, 0.0F, 0.3125F, 0.6875F, 1.0F, 0.6875F))));
            CAT_TREE_BOXES.put(color, registerBlock("cat_tree_box_" + color.getName(), new BlockCatTree.Box(color)));
        }
        for (BlockPlanks.EnumType type : BlockPlanks.EnumType.values()) {
            SCRATCHING_POSTS.put(type, registerBlock("scratching_post_" + type.getName(), new BlockScratchingPost()));
        }
    }
}
