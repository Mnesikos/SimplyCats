package com.github.mnesikos.simplycats.init;

import com.github.mnesikos.simplycats.Ref;
import com.github.mnesikos.simplycats.item.ItemCatFood;
import com.github.mnesikos.simplycats.item.ItemPetCarrier;
import com.github.mnesikos.simplycats.item.ModItemBase;
import net.minecraft.item.Item;
import net.minecraftforge.fml.common.registry.GameRegistry;

import java.util.ArrayList;
import java.util.List;

public class ModItems {
    public static final List<Item> ITEMS = new ArrayList<>();

    @GameRegistry.ObjectHolder(Ref.MODID + ":pet_carrier")
    public static final Item PET_CARRIER = new ItemPetCarrier();

    @GameRegistry.ObjectHolder(Ref.MODID + ":cat_food")
    public static final Item CAT_FOOD = new ItemCatFood();

    @GameRegistry.ObjectHolder(Ref.MODID + ":cat_litter")
    public static final Item CAT_LITTER = new ModItemBase("cat_litter");

    @GameRegistry.ObjectHolder(Ref.MODID + ":cat_mint")
    public static final Item CAT_MINT = new ModItemBase("cat_mint");


    public static void registerOres() {
        // ore dictionary registries here
    }

}
