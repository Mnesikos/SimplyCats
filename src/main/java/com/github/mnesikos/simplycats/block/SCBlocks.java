package com.github.mnesikos.simplycats.block;

import com.github.mnesikos.simplycats.SimplyCats;
import com.github.mnesikos.simplycats.item.SCItems;
import net.minecraft.client.renderer.ItemBlockRenderTypes;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.FlowerPotBlock;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.minecraft.core.registries.Registries;
import net.neoforged.neoforge.registries.DeferredHolder;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Supplier;

public class SCBlocks {
    public static final DeferredRegister<Block> REGISTRAR = DeferredRegister.create(Registries.BLOCK, SimplyCats.MOD_ID);

    public static final DeferredHolder<Block, Block> CATNIP_CROP = REGISTRAR.register("catnip", () -> new CatnipBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.WHEAT)));
    public static final DeferredHolder<Block, Block> POTTED_CATNIP = REGISTRAR.register("potted_catnip", () -> new FlowerPotBlock(CATNIP_CROP.get(), BlockBehaviour.Properties.ofFullCopy(Blocks.POTTED_POPPY)));
    public static final DeferredHolder<Block, Block> SHELTER_BOOK = register("shelter_book", ShelterBookBlock::new);

    public static final Map<DyeColor, DeferredHolder<Block, Block>> CAT_BOWLS = new HashMap<>();
    public static final Map<DyeColor, DeferredHolder<Block, Block>> LITTER_BOXES = new HashMap<>();
    public static final Map<DyeColor, DeferredHolder<Block, Block>> CAT_TREE_BEDS = new HashMap<>();
    public static final Map<DyeColor, DeferredHolder<Block, Block>> CAT_TREE_POSTS = new HashMap<>();
    public static final Map<DyeColor, DeferredHolder<Block, Block>> CAT_TREE_BOXES = new HashMap<>();

    public static final Map<String, DeferredHolder<Block, Block>> SCRATCHING_POSTS = new HashMap<>();
    public static final Map<String, DeferredHolder<Block, Block>> WINDOW_PERCHES = new HashMap<>();

    static {
        for (DyeColor color : DyeColor.values()) {
            CAT_BOWLS.put(color, register(color.getName() + "_cat_bowl", CatBowlBlock::new));
            LITTER_BOXES.put(color, register(color.getName() + "_litter_box", LitterBoxBlock::new));
            CAT_TREE_BEDS.put(color, register(color.getName() + "_cat_tree_bed", () -> new CatTreeBlock.Bed(Block.box(0.0F, 0.0F, 0.0F, 16.0F, 6.0F, 16.0F))));
            CAT_TREE_POSTS.put(color, register(color.getName() + "_cat_tree_post", () -> new CatTreeBlock(Block.box(5.0F, 0.0F, 5.0F, 11.0F, 16.0F, 11.0F))));
            CAT_TREE_BOXES.put(color, register(color.getName() + "_cat_tree_box", CatTreeBlock.Box::new));
        }

        String[] woodTypes = new String[]{"oak", "spruce", "birch", "acacia", "jungle", "dark_oak", "crimson", "warped"};
        for (String woodType : woodTypes) {
            SCRATCHING_POSTS.put(woodType, register(woodType + "_scratching_post", ScratchingPostBlock::new));
            WINDOW_PERCHES.put(woodType, register(woodType + "_window_perch", WindowPerchBlock::new));
        }
    }

    private static <T extends Block> DeferredHolder<Block, T> register(String name, Supplier<T> block) {
        DeferredHolder<Block, T> registryObject = REGISTRAR.register(name, block);
        SCItems.REGISTRAR.register(name, () -> new BlockItem(registryObject.get(), new Item.Properties()));
        return registryObject;
    }

    @OnlyIn(Dist.CLIENT)
    public static void setRenderLayers() {
        RenderType cutout = RenderType.cutout();
        ItemBlockRenderTypes.setRenderLayer(POTTED_CATNIP.get(), cutout);
    }
}
