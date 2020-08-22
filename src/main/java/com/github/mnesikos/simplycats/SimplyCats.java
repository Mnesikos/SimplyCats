package com.github.mnesikos.simplycats;

import com.github.mnesikos.simplycats.client.render.entity.RenderCat;
import com.github.mnesikos.simplycats.configuration.SimplyCatsConfig;
import com.github.mnesikos.simplycats.entity.EntityCat;
import com.github.mnesikos.simplycats.init.ModFeatures;
import com.github.mnesikos.simplycats.init.ModItems;
import com.google.common.collect.ImmutableList;
import com.mojang.datafixers.util.Pair;
import net.minecraft.block.Block;
import net.minecraft.entity.EntityClassification;
import net.minecraft.entity.EntityType;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.gen.GenerationStage;
import net.minecraft.world.gen.feature.Feature;
import net.minecraft.world.gen.feature.IFeatureConfig;
import net.minecraft.world.gen.feature.jigsaw.EmptyJigsawPiece;
import net.minecraft.world.gen.feature.jigsaw.JigsawManager;
import net.minecraft.world.gen.feature.jigsaw.JigsawPattern;
import net.minecraft.world.gen.feature.jigsaw.SingleJigsawPiece;
import net.minecraft.world.gen.placement.Placement;
import net.minecraftforge.client.event.ModelRegistryEvent;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.client.registry.RenderingRegistry;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.fml.loading.FMLPaths;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.IForgeRegistry;
import net.minecraftforge.registries.IForgeRegistryEntry;
import net.minecraftforge.registries.ObjectHolder;

@Mod(value = Ref.MODID)
public class SimplyCats {
    public SimplyCats() {
        ModLoadingContext.get().registerConfig(ModConfig.Type.COMMON, SimplyCatsConfig.COMMON_CONFIG);
        SimplyCatsConfig.loadConfig(SimplyCatsConfig.COMMON_CONFIG, FMLPaths.CONFIGDIR.get().resolve(Ref.MODID + "-" + Ref.VERSION + ".toml"));

        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::setup);
    }

    public static ItemGroup GROUP = new ItemGroup(Ref.MODID + ".tab") {
        @Override
        public ItemStack createIcon() {
            return new ItemStack(ModItems.CATNIP);
        }
    };

    @ObjectHolder(Ref.MODID + ":cat")
    public static EntityType<EntityCat> CAT;

    public void setup(final FMLCommonSetupEvent event) {
        /*for (Biome biome : ForgeRegistries.BIOMES) {
            biome.addStructure(ModFeatures.PET_SHELTER.func_225566_b_(IFeatureConfig.NO_FEATURE_CONFIG));
            biome.addFeature(GenerationStage.Decoration.SURFACE_STRUCTURES, ModFeatures.PET_SHELTER.func_225566_b_(IFeatureConfig.NO_FEATURE_CONFIG)
                    .func_227228_a_(Placement.NOPE.func_227446_a_(IPlacementConfig.NO_PLACEMENT_CONFIG)));
        }*/
        JigsawManager.REGISTRY.register(new JigsawPattern(new ResourceLocation(Ref.MODID + ":village/plains"), new ResourceLocation("village/plains/terminators"), ImmutableList.of(new Pair(new SingleJigsawPiece(Ref.MODID + ":structures/petshelter"), 2), com.mojang.datafixers.util.Pair.of(EmptyJigsawPiece.INSTANCE, 10)), JigsawPattern.PlacementBehaviour.RIGID));
    }

    /*@Mod.Instance
    public static SimplyCats instance;

    @SidedProxy(clientSide=Ref.CLIENT_PROXY, serverSide=Ref.SERVER_PROXY)
    public static CommonProxy PROXY;*/

    /*@Mod.EventHandler
    public void preInit(FMLPreInitializationEvent e) {
        PROXY.preInit(e);
    }

    @Mod.EventHandler
    public void init(FMLInitializationEvent e) {
        PROXY.init(e);
    }

    @Mod.EventHandler
    public void postInit(FMLPostInitializationEvent e) {
        PROXY.postInit(e);
    }

    @Mod.EventHandler
    public void serverLoad(FMLServerStartingEvent event) {
        if (SimplyCatsConfig.COMMAND_BECKON)
            event.registerServerCommand(new CommandBeckon());
    }*/

    @Mod.EventBusSubscriber(bus=Mod.EventBusSubscriber.Bus.MOD)
    public static class RegistrationHandler {
        @SubscribeEvent
        public static void registerEntities(final RegistryEvent.Register<EntityType<?>> event) {
            event.getRegistry().register(EntityType.Builder.create(EntityCat::new, EntityClassification.CREATURE)
                    .size(0.6F, 0.8F)
                    .setShouldReceiveVelocityUpdates(true).setTrackingRange(80).setUpdateInterval(1)
                    .build("cat").setRegistryName(Ref.MODID, "cat")
            );
        }

        @SubscribeEvent
        public static void registerRenders(final ModelRegistryEvent event) {
            RenderingRegistry.registerEntityRenderingHandler(EntityCat.class, RenderCat::new);
        }

        @SubscribeEvent
        public static void registerBlocks(final RegistryEvent.Register<Block> event) {
            //ModBlocks.register(event.getRegistry());
        }

        @SubscribeEvent
        public static void registerItems(final RegistryEvent.Register<Item> event) {
            ModItems.register(event.getRegistry());
            //ModBlocks.registerItemBlocks(event.getRegistry());
        }

        @SubscribeEvent
        public static void registerTileEntities(final RegistryEvent.Register<TileEntityType<?>> event) {
            //ModBlocks.registerTE(event.getRegistry());
        }

        @SubscribeEvent
        public static void onRegisterFeatures(final RegistryEvent.Register<Feature<?>> event) {
            ModFeatures.registerFeatures(event);
        }
    }

    public static <T extends IForgeRegistryEntry<T>> T register(IForgeRegistry<T> registry, T entry, String registryKey) {
        entry.setRegistryName(new ResourceLocation(Ref.MODID, registryKey));
        registry.register(entry);
        return entry;
    }
}
