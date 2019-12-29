package com.github.mnesikos.simplycats.proxy;

import com.github.mnesikos.simplycats.Ref;
import com.github.mnesikos.simplycats.client.gui.GuiBowl;
import com.github.mnesikos.simplycats.client.model.entity.ModelCat;
import com.github.mnesikos.simplycats.client.renderer.block.RenderBowl;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;

import com.github.mnesikos.simplycats.block.BlockBowl;
import com.github.mnesikos.simplycats.client.renderer.entity.RenderCat;
import com.github.mnesikos.simplycats.entity.EntityCat;
import com.github.mnesikos.simplycats.tileentity.TileEntityBowl;

import cpw.mods.fml.client.registry.RenderingRegistry;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.registry.VillagerRegistry;

public class ClientProxy extends CommonProxy {
	private static final ResourceLocation NPC_PET_SHOP = new ResourceLocation(Ref.MODID +":textures/entity/npcs/pet_shop.png");
	
	public static int RENDER_BOWL_ID;
	
	@Override
	public void preInit(FMLPreInitializationEvent e) {
		super.preInit(e);
		
		RenderingRegistry.registerEntityRenderingHandler(EntityCat.class, new RenderCat(new ModelCat(), 0.5F));

        VillagerRegistry.instance().registerVillagerSkin(28643, NPC_PET_SHOP);
        
		RenderBowl renderBowl = new RenderBowl();
		RENDER_BOWL_ID = RenderingRegistry.getNextAvailableRenderId();
		RenderingRegistry.registerBlockHandler(RENDER_BOWL_ID, renderBowl);
	}

	@Override
	public void init(FMLInitializationEvent e) {
		super.init(e);
	}

	@Override
	public void postInit(FMLPostInitializationEvent e) {
		super.postInit(e);
	}
	
	@Override
	public Object getClientGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z) {
		TileEntity te = world.getTileEntity(x, y, z);
		if (te != null) {
			if (ID == BlockBowl.GUI_ID) {
				if (!(te instanceof TileEntityBowl))
					return null;
				else
					return new GuiBowl((TileEntityBowl)te, player);
			}
		}
		return null;
	}
}
