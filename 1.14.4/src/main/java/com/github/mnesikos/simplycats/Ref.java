package com.github.mnesikos.simplycats;

import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.tags.ItemTags;
import net.minecraft.util.NonNullList;
import net.minecraft.util.ResourceLocation;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

public class Ref {
    public static final String MODID = "simplycats";
    public static final String VERSION = "@VERSION@";
	
    private static List<Item> EDIBLE = new ArrayList<>();

    public static void registerCatFoods() {
        // add vanilla raw meats
        EDIBLE.add(Items.COD);
        EDIBLE.add(Items.SALMON);
        EDIBLE.add(Items.RABBIT);
        EDIBLE.add(Items.MUTTON);
        EDIBLE.add(Items.CHICKEN);
        EDIBLE.add(Items.PORKCHOP);
        EDIBLE.add(Items.BEEF);
        // add vanilla cooked meats
        EDIBLE.add(Items.COOKED_COD);
        EDIBLE.add(Items.COOKED_SALMON);
        EDIBLE.add(Items.COOKED_RABBIT);
        EDIBLE.add(Items.COOKED_MUTTON);
        EDIBLE.add(Items.COOKED_CHICKEN);
        EDIBLE.add(Items.COOKED_PORKCHOP);
        EDIBLE.add(Items.COOKED_BEEF);

        /*NonNullList<ItemStack> meatrawDictionary = OreDictionary.getOres("listAllmeatraw");
        for (Item stack : meatrawDictionary)
            EDIBLE.add(stack.getItem());*/
        Collection<Item> meatrawDictionary = ItemTags.getCollection().get(new ResourceLocation("forge", "listAllmeatraw")).getAllElements();
        EDIBLE.addAll(meatrawDictionary);
        Collection<Item> meatcookedDictionary = ItemTags.getCollection().get(new ResourceLocation("forge","listAllmeatcooked")).getAllElements();
        EDIBLE.addAll(meatcookedDictionary);
        Collection<Item> fishrawDictionary = ItemTags.getCollection().get(new ResourceLocation("forge","listAllfishraw")).getAllElements();
        EDIBLE.addAll(fishrawDictionary);
        Collection<Item> fishcookedDictionary = ItemTags.getCollection().get(new ResourceLocation("forge","listAllfishcooked")).getAllElements();
        EDIBLE.addAll(fishcookedDictionary);
        Collection<Item> tofuDictionary = ItemTags.getCollection().get(new ResourceLocation("forge","listAlltofu")).getAllElements();
        EDIBLE.addAll(tofuDictionary);
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
