package com.github.mnesikos.simplycats.init;

import com.github.mnesikos.simplycats.Ref;
import com.github.mnesikos.simplycats.item.*;
import net.minecraft.item.Item;
import net.minecraftforge.registries.IForgeRegistry;
import net.minecraftforge.registries.ObjectHolder;

@ObjectHolder(value = Ref.MODID)
public class ModItems {
    //public static final ItemBase PET_CARRIER = new ItemPetCarrier();
    public static final ItemBase CERTIFICATE = new ItemCertificate("certificate");
    public static final ItemBase TREAT_BAG = new ItemTreatBag("treat_bag");
    public static final ItemBase CATNIP = new ItemCatnip("catnip");
    public static final ItemCatnipSeeds CATNIP_SEEDS = new ItemCatnipSeeds("catnip_seeds");

    public static void registerOres() { // TODO Tags https://mcforge.readthedocs.io/en/1.13.x/utilities/tags/#migration-from-oredictionary
        /*OreDictionary.registerOre("cropCatnip", CATNIP);
        OreDictionary.registerOre("cropCatmint", CATNIP);*/
    }

    public static void register(IForgeRegistry<Item> registry) {
        registry.registerAll(
                //PET_CARRIER,
                CERTIFICATE,
                TREAT_BAG,
                CATNIP,
                CATNIP_SEEDS
        );
    }
}
