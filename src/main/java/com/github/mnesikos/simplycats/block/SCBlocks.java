package com.github.mnesikos.simplycats.block;

import com.github.mnesikos.simplycats.SimplyCats;
import com.github.mnesikos.simplycats.item.SCItems;
import net.minecraft.block.AbstractBlock;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.block.WoodType;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.RenderTypeLookup;
import net.minecraft.item.BlockItem;
import net.minecraft.item.DyeColor;
import net.minecraft.item.Item;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.loading.FMLEnvironment;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Supplier;

@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD)
public class SCBlocks {
    public static final DeferredRegister<Block> REGISTRAR = DeferredRegister.create(ForgeRegistries.BLOCKS, SimplyCats.MOD_ID);

    public static final RegistryObject<Block> CATNIP_CROP = REGISTRAR.register("catnip", () -> new CatnipBlock(AbstractBlock.Properties.copy(Blocks.WHEAT)));
    public static final RegistryObject<Block> SHELTER_BOOK = register("shelter_book", ShelterBookBlock::new);

//    public static final Map<DyeColor, RegistryObject<Block>> CAT_BOWLS = new HashMap<>();
    public static final Map<DyeColor, RegistryObject<Block>> LITTER_BOXES = new HashMap<>();
    public static final Map<DyeColor, RegistryObject<Block>> CAT_TREE_BEDS = new HashMap<>();
    public static final Map<DyeColor, RegistryObject<Block>> CAT_TREE_POSTS = new HashMap<>();
    public static final Map<DyeColor, RegistryObject<Block>> CAT_TREE_BOXES = new HashMap<>();

    public static final Map<WoodType, RegistryObject<Block>> SCRATCHING_POSTS = new HashMap<>();
    public static final Map<WoodType, RegistryObject<Block>> WINDOW_PERCHES = new HashMap<>();

    static {
        for (DyeColor color : DyeColor.values()) {
//            CAT_BOWLS.put(color, register(color.getName() + "_cat_bowl", () -> new CatBowlBlock(color)));
            LITTER_BOXES.put(color, register(color.getName() + "_litter_box", LitterBoxBlock::new));
            CAT_TREE_BEDS.put(color, register(color.getName() + "_cat_tree_bed", () -> new CatTreeBlock.Bed(Block.box(0.0F, 0.0F, 0.0F, 16.0F, 6.0F, 16.0F))));
            CAT_TREE_POSTS.put(color, register(color.getName() + "_cat_tree_post", () -> new CatTreeBlock(Block.box(5.0F, 0.0F, 5.0F, 11.0F, 16.0F, 11.0F))));
            CAT_TREE_BOXES.put(color, register(color.getName() + "_cat_tree_box", CatTreeBlock.Box::new));
        }

        WoodType.values().forEach((woodType) -> {
            SCRATCHING_POSTS.put(woodType, register(woodType.name() + "_scratching_post", ScratchingPostBlock::new));
            WINDOW_PERCHES.put(woodType, register(woodType.name() + "_window_perch", WindowPerchBlock::new));
        });
    }

    private static <T extends Block> RegistryObject<T> register(String name, Supplier<T> block) {
        RegistryObject<T> registryObject = REGISTRAR.register(name, block);
        SCItems.REGISTRAR.register(name, () -> new BlockItem(registryObject.get(), new Item.Properties().tab(SimplyCats.ITEM_GROUP)));
        return registryObject;
    }

    @SubscribeEvent
    public static void onBlocksRegistry(final RegistryEvent.Register<Block> event) {
        if (FMLEnvironment.dist == Dist.CLIENT) {
            RenderTypeLookup.setRenderLayer(CATNIP_CROP.get(), RenderType.cutout());
        }
    }
}
