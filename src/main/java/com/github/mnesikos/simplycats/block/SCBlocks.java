package com.github.mnesikos.simplycats.block;

import com.github.mnesikos.simplycats.SimplyCats;
import com.github.mnesikos.simplycats.item.SCItems;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Supplier;

public class SCBlocks {
    public static final DeferredRegister<Block> REGISTRAR = DeferredRegister.create(ForgeRegistries.BLOCKS, SimplyCats.MOD_ID);

    public static final RegistryObject<Block> CATNIP_CROP = REGISTRAR.register("catnip", () -> new CatnipBlock(BlockBehaviour.Properties.copy(Blocks.WHEAT)));
    public static final RegistryObject<Block> SHELTER_BOOK = register("shelter_book", ShelterBookBlock::new);

    public static final Map<DyeColor, RegistryObject<Block>> CAT_BOWLS = new HashMap<>();
    public static final Map<DyeColor, RegistryObject<Block>> LITTER_BOXES = new HashMap<>();
    public static final Map<DyeColor, RegistryObject<Block>> CAT_TREE_BEDS = new HashMap<>();
    public static final Map<DyeColor, RegistryObject<Block>> CAT_TREE_POSTS = new HashMap<>();
    public static final Map<DyeColor, RegistryObject<Block>> CAT_TREE_BOXES = new HashMap<>();

    public static final Map<String, RegistryObject<Block>> SCRATCHING_POSTS = new HashMap<>();
    public static final Map<String, RegistryObject<Block>> WINDOW_PERCHES = new HashMap<>();

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

    private static <T extends Block> RegistryObject<T> register(String name, Supplier<T> block) {
        RegistryObject<T> registryObject = REGISTRAR.register(name, block);
        SCItems.REGISTRAR.register(name, () -> new BlockItem(registryObject.get(), new Item.Properties()));
        return registryObject;
    }
}
