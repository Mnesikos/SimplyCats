package com.github.mnesikos.simplycats.init;

import com.github.mnesikos.simplycats.Ref;
import com.github.mnesikos.simplycats.item.ItemBase;
import com.github.mnesikos.simplycats.item.ItemCertificate;
import com.github.mnesikos.simplycats.item.ItemPetCarrier;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;

import cpw.mods.fml.common.registry.GameRegistry;

public final class ModItems {
    public static Item PET_CARRIER;
    public static Item CERTIFICATE;
    public static Item TREAT_BAG;
    public static Item CAT_MINT;
    
    public static final void init() {
        PET_CARRIER = new ItemPetCarrier();
        GameRegistry.registerItem(PET_CARRIER, "pet_carrier");

        CERTIFICATE = new ItemCertificate();
        GameRegistry.registerItem(CERTIFICATE, "certificate");

        TREAT_BAG = new ItemBase("treat_bag");
        GameRegistry.registerItem(TREAT_BAG, "treat_bag");

    	CAT_MINT = new ItemBase("catnip");
        GameRegistry.registerItem(CAT_MINT, "catnip");
        
    }
    
	public static final CreativeTabs CREATIVE_TAB = new CreativeTabs(Ref.MODID + ".tab") {
	    @Override
	    public Item getTabIconItem() {
	        return ModItems.PET_CARRIER;
	    }
	};

}