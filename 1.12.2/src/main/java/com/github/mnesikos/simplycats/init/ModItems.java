package com.github.mnesikos.simplycats.init;

import com.github.mnesikos.simplycats.Ref;
import com.github.mnesikos.simplycats.item.ItemBase;
import com.github.mnesikos.simplycats.item.ItemCertificate;
import com.github.mnesikos.simplycats.item.ItemPetCarrier;
import net.minecraft.item.Item;
import net.minecraftforge.fml.common.registry.GameRegistry;

import java.util.ArrayList;
import java.util.List;

public class ModItems {
    public static final List<Item> ITEMS = new ArrayList<>();

    @GameRegistry.ObjectHolder(Ref.MODID + ":pet_carrier")
    public static Item PET_CARRIER = new ItemPetCarrier();

    @GameRegistry.ObjectHolder(Ref.MODID + ":certificate")
    public static Item CERTIFICATE = new ItemCertificate();

    @GameRegistry.ObjectHolder(Ref.MODID + ":treat_bag")
    public static Item TREAT_BAG = new ItemBase("treat_bag");

    @GameRegistry.ObjectHolder(Ref.MODID + ":catnip")
    public static Item CAT_MINT = new ItemBase("catnip");


    public static void registerOres() {
        // ore dictionary registries here
    }

}
