package com.github.mnesikos.simplycats.init;

import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;

import cpw.mods.fml.common.registry.GameRegistry;
import net.minecraftforge.oredict.OreDictionary;

public final class ModRecipes {
	public static final void init() {
        GameRegistry.addShapelessRecipe(new ItemStack(ModItems.CERTIFICATE, 1, 0), new ItemStack(Items.dye, 1, 0), Items.paper);
        GameRegistry.addShapelessRecipe(new ItemStack(ModItems.CERTIFICATE, 1, 1), new ItemStack(ModItems.CERTIFICATE, 1, 0));
        GameRegistry.addShapelessRecipe(new ItemStack(ModItems.CERTIFICATE, 1, 0), new ItemStack(ModItems.CERTIFICATE, 1, 1));

        GameRegistry.addShapelessRecipe(new ItemStack(ModItems.TREAT_BAG, 1), Items.leather, Items.iron_ingot);

        GameRegistry.addRecipe(new ItemStack(Items.name_tag, 1),
				"S  ",
						 " I ",
						 "  I",
				'S', Items.string, 'I', Items.iron_ingot, 'I', Items.gold_ingot);
        GameRegistry.addRecipe(new ItemStack(ModItems.PET_CARRIER, 1),
				"ISI",
						 "SBS",
						 "ISI",
				'I', Items.iron_ingot, 'B', Blocks.iron_bars, 'S', Blocks.hardened_clay);

        /*GameRegistry.addRecipe(new ItemStack(ModBlocks.BOWL, 1), new Object[]{
        	"I I",
        	"III",
        	'I', Items.iron_ingot});*/
	}
}
