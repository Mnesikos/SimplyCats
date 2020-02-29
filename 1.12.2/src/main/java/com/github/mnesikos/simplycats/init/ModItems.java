package com.github.mnesikos.simplycats.init;

import com.github.mnesikos.simplycats.item.*;
import net.minecraft.init.Items;
import net.minecraft.item.EnumDyeColor;
import net.minecraft.item.Item;
import net.minecraftforge.oredict.OreDictionary;
import net.minecraftforge.registries.IForgeRegistry;

import java.util.HashMap;
import java.util.Map;

public class ModItems {
    public static ItemBase PET_CARRIER = new ItemPetCarrier();
    public static ItemBase CERTIFICATE = new ItemCertificate("certificate");
    public static ItemBase TREAT_BAG = new ItemTreatBag("treat_bag");
    public static ItemBase CATNIP = new ItemCatnip("catnip");
    public static ItemCatnipSeeds CATNIP_SEEDS = new ItemCatnipSeeds("catnip_seeds");

    public static final Map<EnumDyeColor, ItemCatBowl> BOWLS = new HashMap<>();

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

        for (EnumDyeColor color : EnumDyeColor.values()) {
            BOWLS.put(color, new ItemCatBowl("cat_bowl", color));
            registry.register(BOWLS.get(color));
        }
    }

    public static void registerModels() {
        PET_CARRIER.registerItemModel();
        CERTIFICATE.registerItemModel();
        TREAT_BAG.registerItemModel();
        CATNIP.registerItemModel();
        CATNIP_SEEDS.registerItemModel();
        for (EnumDyeColor color : EnumDyeColor.values())
            BOWLS.get(color).registerItemModel();
    }
}
