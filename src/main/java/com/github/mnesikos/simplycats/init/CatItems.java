package com.github.mnesikos.simplycats.init;

import com.github.mnesikos.simplycats.Ref;
import com.github.mnesikos.simplycats.SimplyCats;
import com.github.mnesikos.simplycats.item.*;
import net.minecraft.item.Item;
import net.minecraftforge.oredict.OreDictionary;

import java.util.ArrayList;
import java.util.List;

public class CatItems {
    public static final List<Item> ITEMS = new ArrayList<>();
    public static final ItemCatBook CAT_BOOK = registerItem("cat_book", new ItemCatBook());
    public static final ItemPetCarrier PET_CARRIER = registerItem("pet_carrier", new ItemPetCarrier());
    public static final ItemCertificate CERTIFICATE = registerItem("certificate", new ItemCertificate());
    public static final ItemTreatBag TREAT_BAG = registerItem("treat_bag", new ItemTreatBag());
    public static final Item CATNIP = registerItem("catnip", new Item());
    public static final ItemLaserPointer LASER_POINTER = registerItem("laser_pointer", new ItemLaserPointer());
    public static final ItemSterilizationPotion STERILIZE_POTION = registerItem("sterilization_potion", new ItemSterilizationPotion());

    public static void registerOres() {
        OreDictionary.registerOre("cropCatnip", CATNIP);
        OreDictionary.registerOre("cropCatmint", CATNIP);
    }

    public static <T extends Item> T registerItem(String name, T item) {
        item.setRegistryName(name);
        item.setUnlocalizedName(Ref.MODID + "." + name);
        item.setCreativeTab(SimplyCats.PROXY.SIMPLYCATS);
        ITEMS.add(item);
        return item;
    }
}
