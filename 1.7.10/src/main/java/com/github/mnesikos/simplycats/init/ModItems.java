package com.github.mnesikos.simplycats.init;

import com.github.mnesikos.simplycats.SimplyCats;
import com.github.mnesikos.simplycats.item.ItemCatFood;
import com.github.mnesikos.simplycats.item.ItemPetCarrier;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;

import cpw.mods.fml.common.registry.GameRegistry;

public final class ModItems {
	public static Item CAT_MINT;
    public static Item CAT_LITTER;
    public static Item CAT_FOOD;
	public static Item PET_CARRIER;
    
    public static final void init() {
        PET_CARRIER = new ItemPetCarrier().setUnlocalizedName("pet_carrier").setCreativeTab(ModItems.CREATIVE_TAB).setTextureName(SimplyCats.MODID + ":pet_carrier");
        GameRegistry.registerItem(PET_CARRIER, "pet_carrier");
        
        CAT_FOOD = new ItemCatFood().setUnlocalizedName("cat_food").setCreativeTab(ModItems.CREATIVE_TAB).setTextureName(SimplyCats.MODID + ":cat_food");
        GameRegistry.registerItem(CAT_FOOD, "cat_food");
        
        CAT_LITTER = new Item().setUnlocalizedName("cat_litter").setCreativeTab(ModItems.CREATIVE_TAB).setTextureName(SimplyCats.MODID + ":cat_litter");
        GameRegistry.registerItem(CAT_LITTER, "cat_litter");
        
    	CAT_MINT = new Item().setUnlocalizedName("cat_mint").setCreativeTab(ModItems.CREATIVE_TAB).setTextureName(SimplyCats.MODID + ":cat_mint");
        GameRegistry.registerItem(CAT_MINT, "cat_mint");
        
    }
    
	public static final CreativeTabs CREATIVE_TAB = new CreativeTabs(SimplyCats.MODID + ".tab") {
	    @Override
	    public Item getTabIconItem() {
	        return ModItems.PET_CARRIER;
	    }
	};

}