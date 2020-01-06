package com.github.mnesikos.simplycats;

import com.github.mnesikos.simplycats.client.render.entity.RenderCat;
import com.github.mnesikos.simplycats.entity.EntityCat;
//import com.github.mnesikos.simplycats.init.ModBlocks;
import com.github.mnesikos.simplycats.init.ModItems;
import net.minecraft.block.Block;
import net.minecraft.entity.EntityClassification;
import net.minecraft.entity.EntityType;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntityType;
import net.minecraftforge.client.event.ModelRegistryEvent;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.client.registry.RenderingRegistry;
import net.minecraftforge.fml.common.Mod;

@Mod(value = Ref.MODID)
public class SimplyCats {
    public SimplyCats() {

    }

    public static ItemGroup GROUP = new ItemGroup(Ref.MODID + ".tab") {
        @Override
        public ItemStack createIcon() {
            return new ItemStack(ModItems.CATNIP);
        }
    };

    public static final EntityType<EntityCat> CAT = null;

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
        public static void registerEntities(final RegistryEvent.Register<EntityType<?>> event) {event.getRegistry().register(
                    EntityType.Builder.create(EntityCat::new, EntityClassification.CREATURE).size(0.6F, 0.8F)
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
    }
}
