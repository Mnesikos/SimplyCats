package com.github.mnesikos.simplycats.proxy;

import com.github.mnesikos.simplycats.Ref;
import com.github.mnesikos.simplycats.SimplyCats;
import com.github.mnesikos.simplycats.worldgen.villages.ComponentPetShelter;
import com.github.mnesikos.simplycats.worldgen.villages.VillagePetShelterHandler;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import net.minecraft.world.gen.structure.MapGenStructureIO;

import com.github.mnesikos.simplycats.init.ModBlocks;
import com.github.mnesikos.simplycats.init.ModItems;
import com.github.mnesikos.simplycats.init.ModRecipes;
import com.github.mnesikos.simplycats.block.BlockBowl;
import com.github.mnesikos.simplycats.entity.EntityCat;
import com.github.mnesikos.simplycats.inventory.ContainerBowl;
import com.github.mnesikos.simplycats.tileentity.TileEntityBowl;

import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.network.IGuiHandler;
import cpw.mods.fml.common.network.NetworkRegistry;
import cpw.mods.fml.common.registry.EntityRegistry;
import cpw.mods.fml.common.registry.GameRegistry;
import cpw.mods.fml.common.registry.VillagerRegistry;

public class CommonProxy implements IGuiHandler {
	
    public void preInit(FMLPreInitializationEvent e) {
        ModItems.init();
        ModBlocks.init();
        ModRecipes.init();
        
        GameRegistry.registerTileEntity(TileEntityBowl.class, "tebowl");
		
        int modEntityID = 0;    
        EntityRegistry.registerModEntity(EntityCat.class, "Cat", ++modEntityID, SimplyCats.instance, 80, 3, false);
        
        VillagerRegistry.instance().registerVillagerId(28643);
        VillagerRegistry.instance().registerVillageTradeHandler(28643, new VillagePetShelterHandler());
        VillagerRegistry.instance().registerVillageCreationHandler(new VillagePetShelterHandler());
        MapGenStructureIO.func_143031_a(ComponentPetShelter.class, Ref.MODID+":PetShopStructure");
    }

    public void init(FMLInitializationEvent e) {
        NetworkRegistry.INSTANCE.registerGuiHandler(SimplyCats.instance, SimplyCats.proxy);
    }

    public void postInit(FMLPostInitializationEvent e) {

    }
    

	@Override
	public Object getServerGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z) {
		TileEntity te = world.getTileEntity(x, y, z);
		if (te != null) {
			if (ID == BlockBowl.GUI_ID) {
				if (!(te instanceof TileEntityBowl))
					return null;
				else
					return new ContainerBowl((TileEntityBowl)te, player);
			}
		}
		return null;
	}
	

	@Override
	public Object getClientGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z) {
		return null;
	}
}