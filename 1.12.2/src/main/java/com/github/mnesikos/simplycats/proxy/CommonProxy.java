package com.github.mnesikos.simplycats.proxy;

import com.github.mnesikos.simplycats.CatDataFixer;
import com.github.mnesikos.simplycats.Ref;
import com.github.mnesikos.simplycats.SimplyCats;
import com.github.mnesikos.simplycats.block.BlockCatBowl;
import com.github.mnesikos.simplycats.entity.EntityCat;
import com.github.mnesikos.simplycats.event.SimplyCatsEvents;
import com.github.mnesikos.simplycats.init.ModItems;
import com.github.mnesikos.simplycats.init.ModProfessions;
import com.github.mnesikos.simplycats.init.ModRecipes;
import com.github.mnesikos.simplycats.inventory.ContainerBowl;
import com.github.mnesikos.simplycats.tileentity.TileEntityBowl;
import com.github.mnesikos.simplycats.worldgen.villages.ComponentPetShelter;
import com.github.mnesikos.simplycats.worldgen.villages.VillagePetShelterHandler;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.datafix.FixTypes;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.gen.structure.MapGenStructureIO;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.util.ModFixs;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.network.IGuiHandler;
import net.minecraftforge.fml.common.network.NetworkRegistry;
import net.minecraftforge.fml.common.registry.EntityRegistry;
import net.minecraftforge.fml.common.registry.VillagerRegistry;

import javax.annotation.Nullable;

@Mod.EventBusSubscriber
public class CommonProxy implements IGuiHandler {
    public static final int FIXER_VERSION = 2;

    public final CreativeTabs SIMPLYCATS = new CreativeTabs(Ref.MODID + ".tab") {
        @Override
        public ItemStack createIcon() {
            return new ItemStack(ModItems.PET_CARRIER);
        }
    };

    public void preInit(FMLPreInitializationEvent e) {
        MinecraftForge.EVENT_BUS.register(new SimplyCatsEvents());

        int ENTITY_ID = 0;
        EntityRegistry.registerModEntity(new ResourceLocation(Ref.MODID + ":cat"), EntityCat.class, "Cat", ENTITY_ID++, SimplyCats.instance, 80, 1, true);
    }

    public void init(FMLInitializationEvent e) {
        ModItems.registerOres();
        ModRecipes.init();

        ModFixs fixer = FMLCommonHandler.instance().getDataFixer().init(Ref.MODID, FIXER_VERSION);
        fixer.registerFix(FixTypes.ENTITY, new CatDataFixer());

        NetworkRegistry.INSTANCE.registerGuiHandler(SimplyCats.instance, SimplyCats.PROXY);

        ModProfessions.associateCareersAndTrades();
        VillagerRegistry.instance().registerVillageCreationHandler(new VillagePetShelterHandler());
        MapGenStructureIO.registerStructureComponent(ComponentPetShelter.class, Ref.MODID + ":PetShelterStructure");
    }

    public void postInit(FMLPostInitializationEvent e) {
        Ref.registerCatFoods();
    }

    public void registerItemRenderer(Item item, int meta, String id) {
    }

    @Nullable
    @Override
    public Container getServerGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z) {
        switch (ID) {
            case BlockCatBowl.GUI_ID:
                return new ContainerBowl(player.inventory, (TileEntityBowl)world.getTileEntity(new BlockPos(x, y, z)));
            default:
                return null;
        }
    }

    @Nullable
    @Override
    public Object getClientGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z) {
        return null;
    }
}
