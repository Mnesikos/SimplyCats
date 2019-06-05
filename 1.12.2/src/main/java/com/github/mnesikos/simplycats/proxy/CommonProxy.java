package com.github.mnesikos.simplycats.proxy;

import com.github.mnesikos.simplycats.util.Ref;
import com.github.mnesikos.simplycats.SimplyCats;
import com.github.mnesikos.simplycats.entity.EntityCat;
import com.github.mnesikos.simplycats.init.ModItems;
import com.github.mnesikos.simplycats.init.ModProfessions;
import com.github.mnesikos.simplycats.init.ModRecipes;
import com.github.mnesikos.simplycats.worldgen.villages.ComponentPetShelter;
import com.github.mnesikos.simplycats.worldgen.villages.VillagePetShelterHandler;
import net.minecraft.block.Block;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import net.minecraft.world.gen.structure.MapGenStructureIO;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.network.IGuiHandler;
import net.minecraftforge.fml.common.network.NetworkRegistry;
import net.minecraftforge.fml.common.registry.EntityRegistry;
import net.minecraftforge.fml.common.registry.VillagerRegistry;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nullable;

@Mod.EventBusSubscriber
public class CommonProxy implements IGuiHandler {
    public static CreativeTabs SIMPLYCATS;

    @Mod.EventHandler
    public void preInit(FMLPreInitializationEvent e) {
        SIMPLYCATS = new CreativeTabs(Ref.MODID + ".tab") {
            @Override @SideOnly(Side.CLIENT)
            public ItemStack getTabIconItem() {
                return new ItemStack(ModItems.PET_CARRIER);
            }
        };

        //GameRegistry.registerTileEntity(TileEntityBowl.class, "tebowl");

        int entityID = 0;
        EntityRegistry.registerModEntity(new ResourceLocation(Ref.MODID + ":cat"), EntityCat.class, "Cat", entityID++, SimplyCats.instance, 80, 1, true);
    }

    @Mod.EventHandler
    public void init(FMLInitializationEvent e) {
        ModItems.registerOres();
        ModRecipes.init();

        NetworkRegistry.INSTANCE.registerGuiHandler(SimplyCats.instance, SimplyCats.PROXY);

        ModProfessions.associateCareersAndTrades();
        VillagerRegistry.instance().registerVillageCreationHandler(new VillagePetShelterHandler());
        MapGenStructureIO.registerStructureComponent(ComponentPetShelter.class, Ref.MODID + ":PetShelterStructure");
    }

    @Mod.EventHandler
    public void postInit(FMLPostInitializationEvent e) {

    }

    @SubscribeEvent
    public static void registerItems(RegistryEvent.Register<Item> event) {
        event.getRegistry().registerAll(ModItems.ITEMS.toArray(new Item[0]));
    }

    @SubscribeEvent
    public static void registerBlocks(RegistryEvent.Register<Block> event) {
        //event.getRegistry().registerAll(ModBlocks.BLOCKS.toArray(new Block[0]));
    }

    @Nullable
    @Override
    public Object getServerGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z) {
        // TODO
        /*TileEntity te = world.getTileEntity(x, y, z);
        if (te != null) {
            if (ID == BlockBowl.guiID) {
                if (!(te instanceof TileEntityBowl))
                    return null;
                else
                    return new ContainerBowl((TileEntityBowl)te, player);
            }
        }*/
        return null;
    }

    @Nullable
    @Override
    public Object getClientGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z) {
        return null;
    }
}
