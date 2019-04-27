package com.github.mnesikos.simplycats.worldgen.villages;

import java.util.*;

import com.github.mnesikos.simplycats.init.ModItems;

import net.minecraft.entity.passive.EntityVillager;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.village.MerchantRecipe;
import net.minecraft.village.MerchantRecipeList;
import net.minecraft.world.gen.structure.StructureVillagePieces.PieceWeight;
import net.minecraft.world.gen.structure.StructureVillagePieces.Start;
import cpw.mods.fml.common.registry.VillagerRegistry.IVillageCreationHandler;
import cpw.mods.fml.common.registry.VillagerRegistry.IVillageTradeHandler;


public class VillagePetShelterHandler implements IVillageCreationHandler, IVillageTradeHandler
{
    @Override
    public PieceWeight getVillagePieceWeight (Random random, int i)
    {
        return new PieceWeight(ComponentPetShelter.class, 15, 1 + (i > 2 ? random.nextInt(2) : 0));
    }
    
    @Override
	public Class<?> getComponentClass()
	{
		return ComponentPetShelter.class;
	}
	
    @Override
	public Object buildComponent(PieceWeight villagePiece, Start startPiece, @SuppressWarnings("rawtypes") List pieces, Random random, int p1, int p2, int p3, int p4, int p5)
	{
		return ComponentPetShelter.buildComponent(startPiece, pieces, random, p1, p2, p3, p4, p5);
	}
    
	public void manipulateTradesForVillager(EntityVillager villager, MerchantRecipeList recipeList, Random random)
	{
		int dogPrice = 8;
		int catPrice = 8;
		ItemStack cat = new ItemStack(ModItems.petCarrier, 1); cat.setTagCompound(new NBTTagCompound()); cat.setItemDamage(3);
		ItemStack dog = new ItemStack(ModItems.petCarrier, 1); dog.setTagCompound(new NBTTagCompound()); dog.setItemDamage(4);
		
		if (villager.getProfession() == 28643)
		{
		    recipeList.addToListWithCheck(new MerchantRecipe(new ItemStack(Items.bone, dogPrice), dog));
		    recipeList.addToListWithCheck(new MerchantRecipe(new ItemStack(Items.fish, catPrice), cat));
		}
	}
}