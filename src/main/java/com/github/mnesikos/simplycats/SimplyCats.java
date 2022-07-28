package com.github.mnesikos.simplycats;

import com.github.mnesikos.simplycats.block.CatBlocks;
import com.github.mnesikos.simplycats.client.render.entity.RenderCat;
import com.github.mnesikos.simplycats.commands.CommandCatCount;
import com.github.mnesikos.simplycats.entity.EntityCat;
import com.github.mnesikos.simplycats.event.SCEvents;
import com.github.mnesikos.simplycats.init.CatSounds;
import com.github.mnesikos.simplycats.item.CatItems;
import com.github.mnesikos.simplycats.tileentity.TileEntityCatBowl;
import com.github.mnesikos.simplycats.worldgen.villages.CatProfessions;
import com.github.mnesikos.simplycats.worldgen.villages.ComponentPetShelter;
import com.github.mnesikos.simplycats.worldgen.villages.VillagePetShelterHandler;
import net.minecraft.block.Block;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.item.Item;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.datafix.FixTypes;
import net.minecraft.world.gen.structure.MapGenStructureIO;
import net.minecraftforge.client.event.ModelRegistryEvent;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.util.ModFixs;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.client.registry.RenderingRegistry;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.event.FMLServerStartingEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.network.NetworkRegistry;
import net.minecraftforge.fml.common.registry.EntityRegistry;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.fml.common.registry.VillagerRegistry;
import net.minecraftforge.fml.relauncher.Side;

@Mod(modid = Ref.MODID, name = Ref.MODNAME, version = Ref.VERSION,
        acceptedMinecraftVersions = Ref.ACCEPTED_VERSIONS,
        updateJSON = "https://raw.githubusercontent.com/Mnesikos/SimplyCats/forge/1.16.5/versions.json")
public class SimplyCats {
    @Mod.Instance
    public static SimplyCats instance;

    public static final int FIXER_VERSION = 6;

    @Mod.EventHandler
    public void preInit(FMLPreInitializationEvent e) {
        MinecraftForge.EVENT_BUS.register(new SCEvents());
        SCNetworking.CHANNEL.registerMessage(SCNetworking::onMessage, SCNetworking.class, 0, Side.CLIENT);

        int ENTITY_ID = 0;
        EntityRegistry.registerModEntity(new ResourceLocation(Ref.MODID + ":cat"), EntityCat.class, "Cat", ENTITY_ID++, SimplyCats.instance, 80, 1, true);

        // Client-side setup
        if (FMLCommonHandler.instance().getSide() == Side.CLIENT) {
            RenderingRegistry.registerEntityRenderingHandler(EntityCat.class, RenderCat::new);
        }
    }

    @Mod.EventHandler
    public void init(FMLInitializationEvent e) {
        CatItems.registerOres();

        ModFixs fixer = FMLCommonHandler.instance().getDataFixer().init(Ref.MODID, FIXER_VERSION);
        fixer.registerFix(FixTypes.ENTITY, new CatDataFixer());

        NetworkRegistry.INSTANCE.registerGuiHandler(SimplyCats.instance, NetworkGuiHandler.INSTANCE);

        CatProfessions.associateCareersAndTrades();
        VillagerRegistry.instance().registerVillageCreationHandler(new VillagePetShelterHandler());
        MapGenStructureIO.registerStructureComponent(ComponentPetShelter.class, Ref.MODID + ":PetShelterStructure");
    }

    @Mod.EventHandler
    public void postInit(FMLPostInitializationEvent e) {
        Ref.registerCatFoods();
    }

    @Mod.EventHandler
    public void serverLoad(FMLServerStartingEvent event) {
        event.registerServerCommand(new CommandCatCount());
    }

    @Mod.EventBusSubscriber
    public static class RegistrationHandler {

        @SubscribeEvent
        public static void registerBlocks(RegistryEvent.Register<Block> event) {
            CatBlocks.BLOCKS.forEach(event.getRegistry()::register);
            GameRegistry.registerTileEntity(TileEntityCatBowl.class, new ResourceLocation(Ref.MODID, "cat_bowl"));
        }

        @SubscribeEvent
        public static void registerItems(RegistryEvent.Register<Item> event) {
            CatItems.ITEMS.forEach(event.getRegistry()::register);
            if (FMLCommonHandler.instance().getSide() == Side.CLIENT) {
                CatItems.collectModelVariants();
            }
        }

        @SubscribeEvent
        public static void registerModels(ModelRegistryEvent event) {
            for (CatItems.ItemVariant variant : CatItems.VARIANTS) {
                ModelLoader.setCustomModelResourceLocation(variant.item, variant.meta, new ModelResourceLocation(Ref.MODID + ":" + variant.name, "inventory"));
            }
        }

        @SubscribeEvent
        public static void registerSoundEvents(final RegistryEvent.Register<SoundEvent> event) {
            event.getRegistry().register(CatSounds.SHAKE_TREATS);
        }
    }
}
