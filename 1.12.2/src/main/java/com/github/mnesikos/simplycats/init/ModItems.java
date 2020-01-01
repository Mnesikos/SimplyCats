package com.github.mnesikos.simplycats.init;

import com.github.mnesikos.simplycats.item.*;
import net.minecraft.item.Item;
import net.minecraftforge.oredict.OreDictionary;
import net.minecraftforge.registries.IForgeRegistry;

public class ModItems {
    public static ItemBase PET_CARRIER = new ItemPetCarrier();
    public static ItemBase CERTIFICATE = new ItemCertificate("certificate");
    public static ItemBase TREAT_BAG = new ItemTreatBag("treat_bag");
    public static ItemBase CATNIP = new ItemCatnip("catnip");
    public static ItemCatnipSeeds CATNIP_SEEDS = new ItemCatnipSeeds("catnip_seeds");

    public static void registerOres() {
        OreDictionary.registerOre("cropCatnip", CATNIP);
        OreDictionary.registerOre("cropCatmint", CATNIP);
    }

    public static void register(IForgeRegistry<Item> registry) {
        registry.registerAll(
                PET_CARRIER,
                CERTIFICATE,
                TREAT_BAG,
                CATNIP, CATNIP_SEEDS
        );
    }

    public static void registerModels() {
        PET_CARRIER.registerItemModel();
        CERTIFICATE.registerItemModel();
        TREAT_BAG.registerItemModel();
        CATNIP.registerItemModel();
        CATNIP_SEEDS.registerItemModel();
    }
}
