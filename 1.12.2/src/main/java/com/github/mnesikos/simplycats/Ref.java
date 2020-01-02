package com.github.mnesikos.simplycats;

import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;
import net.minecraftforge.oredict.OreDictionary;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class Ref {
    public static final String MODID = "simplycats";
    public static final String MODNAME = "Simply Cats";
    public static final String VERSION = "@VERSION@";
    public static final String ACCEPTED_VERSIONS = "[1.12.2]";

    public static final String CLIENT_PROXY = "com.github.mnesikos.simplycats.proxy.ClientProxy";
    public static final String SERVER_PROXY = "com.github.mnesikos.simplycats.proxy.CommonProxy";

    private static List<Item> EDIBLE = new ArrayList<>();

    public static void registerCatFoods() {
        // add vanilla raw meats
        EDIBLE.add(Items.FISH);
        EDIBLE.add(Items.RABBIT);
        EDIBLE.add(Items.MUTTON);
        EDIBLE.add(Items.CHICKEN);
        EDIBLE.add(Items.PORKCHOP);
        EDIBLE.add(Items.BEEF);
        // add vanilla cooked meats
        EDIBLE.add(Items.COOKED_FISH);
        EDIBLE.add(Items.COOKED_RABBIT);
        EDIBLE.add(Items.COOKED_MUTTON);
        EDIBLE.add(Items.COOKED_CHICKEN);
        EDIBLE.add(Items.COOKED_PORKCHOP);
        EDIBLE.add(Items.COOKED_BEEF);

        NonNullList<ItemStack> meatrawDictionary = OreDictionary.getOres("listAllmeatraw");
        for (ItemStack stack : meatrawDictionary)
            EDIBLE.add(stack.getItem());
        NonNullList<ItemStack> meatcookedDictionary = OreDictionary.getOres("listAllmeatcooked");
        for (ItemStack stack : meatcookedDictionary)
            EDIBLE.add(stack.getItem());
        NonNullList<ItemStack> fishrawDictionary = OreDictionary.getOres("listAllfishraw");
        for (ItemStack stack : fishrawDictionary)
            EDIBLE.add(stack.getItem());
        NonNullList<ItemStack> fishcookedDictionary = OreDictionary.getOres("listAllfishcooked");
        for (ItemStack stack : fishcookedDictionary)
            EDIBLE.add(stack.getItem());

        NonNullList<ItemStack> tofuDictionary = OreDictionary.getOres("listAlltofu");
        for (ItemStack stack : tofuDictionary)
            EDIBLE.remove(stack.getItem());
    }

    public static boolean catFoodItems(ItemStack stack) {
        Iterator foods = EDIBLE.iterator();
        Item i;
        do {
            if (!foods.hasNext()) {
                return false;
            }

            i = (Item)foods.next();
        } while (stack.getItem() != i);

        return true;
    }
}
