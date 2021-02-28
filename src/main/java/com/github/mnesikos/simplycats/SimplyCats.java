package com.github.mnesikos.simplycats;

import com.github.mnesikos.simplycats.commands.CommandBeckon;
import com.github.mnesikos.simplycats.commands.CommandCatCount;
import com.github.mnesikos.simplycats.configuration.SCConfig;
import com.github.mnesikos.simplycats.init.CatBlocks;
import com.github.mnesikos.simplycats.init.CatItems;
import com.github.mnesikos.simplycats.item.ItemPetCarrier;
import com.github.mnesikos.simplycats.proxy.CommonProxy;
import com.github.mnesikos.simplycats.tileentity.TileEntityCatBowl;
import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.event.ModelRegistryEvent;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.event.FMLServerStartingEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.registry.GameRegistry;

@Mod(modid = Ref.MODID, name = Ref.MODNAME, version = Ref.VERSION,
        acceptedMinecraftVersions = Ref.ACCEPTED_VERSIONS)
public class SimplyCats {
    @Mod.Instance
    public static SimplyCats instance;

    @SidedProxy(clientSide=Ref.CLIENT_PROXY, serverSide=Ref.SERVER_PROXY)
    public static CommonProxy PROXY;

    @Mod.EventHandler
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
        if (SCConfig.COMMAND_BECKON)
            event.registerServerCommand(new CommandBeckon());
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
            for (Item item : CatItems.ITEMS) {
                PROXY.registerItemRenderer(item, 0, item.getRegistryName().getResourcePath());
            }
            CatItems.PET_CARRIER.registerItemModel();
            CatItems.CERTIFICATE.registerItemModel();
        }

        @SubscribeEvent
        public static void registerModels(ModelRegistryEvent event) {
            PROXY.registerVariants();
        }
    }
}
