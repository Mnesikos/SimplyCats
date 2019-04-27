package com.github.mnesikos.simplycats.init;

import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;

import cpw.mods.fml.common.registry.GameRegistry;

public final class ModRecipes {
	public static final void init() {
		GameRegistry.addShapelessRecipe(new ItemStack(ModItems.CAT_FOOD, 4, 0), new Object[]
	            {new ItemStack(Items.fish, 1, 0), Items.wheat});
        GameRegistry.addShapelessRecipe(new ItemStack(ModItems.CAT_FOOD, 4, 1), new Object[]
        		{new ItemStack(Items.fish, 1, 1), Items.wheat});
        GameRegistry.addShapelessRecipe(new ItemStack(ModItems.CAT_FOOD, 4, 2), new Object[]
        		{new ItemStack(Items.chicken, 1, 0), Items.wheat});
        GameRegistry.addShapelessRecipe(new ItemStack(ModItems.CAT_FOOD, 4, 3), new Object[]
        		{new ItemStack(Items.beef, 1, 0), Items.wheat});
        
        GameRegistry.addRecipe(new ItemStack(ModItems.PET_CARRIER, 1), new Object[]{
    		"ISI",
    		"SBS",
    		"ISI",
    		'I', Items.iron_ingot, 'B', Blocks.iron_bars, 'S', Blocks.stained_hardened_clay});
        GameRegistry.addRecipe(new ItemStack(ModBlocks.BOWL, 1), new Object[]{
        	"I I",
        	"III",
        	'I', Items.iron_ingot});
	}
}
