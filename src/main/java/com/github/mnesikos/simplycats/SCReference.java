package com.github.mnesikos.simplycats;

import com.google.common.collect.Maps;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.ItemTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.*;

public class SCReference {

    private static final Map<UUID, String> CUSTOM_CATS = Maps.newHashMap();

    private static final List<Item> EDIBLE = new ArrayList<>();

    public static void registerCatFoods() { //todo
        // add vanilla raw meats
        EDIBLE.add(Items.RABBIT);
        EDIBLE.add(Items.MUTTON);
        EDIBLE.add(Items.CHICKEN);
        EDIBLE.add(Items.PORKCHOP);
        EDIBLE.add(Items.BEEF);
        // add vanilla cooked meats
        EDIBLE.add(Items.COOKED_RABBIT);
        EDIBLE.add(Items.COOKED_MUTTON);
        EDIBLE.add(Items.COOKED_CHICKEN);
        EDIBLE.add(Items.COOKED_PORKCHOP);
        EDIBLE.add(Items.COOKED_BEEF);

        EDIBLE.addAll(ForgeRegistries.ITEMS.tags().getTag(ItemTags.FISHES).stream().toList());

        // todo double check tag naming && fix this
        TagKey<Item> meatsRawTag = ForgeRegistries.ITEMS.tags().createTagKey(new ResourceLocation("forge", "meats/raw"));
        EDIBLE.addAll(ForgeRegistries.ITEMS.tags().getTag(meatsRawTag).stream().toList());
        TagKey<Item> meatsCookedTag = ForgeRegistries.ITEMS.tags().createTagKey(new ResourceLocation("forge", "meats/cooked"));
        EDIBLE.addAll(ForgeRegistries.ITEMS.tags().getTag(meatsCookedTag).stream().toList());
        TagKey<Item> meatsTag = ForgeRegistries.ITEMS.tags().createTagKey(new ResourceLocation("forge", "meats"));
        EDIBLE.addAll(ForgeRegistries.ITEMS.tags().getTag(meatsTag).stream().toList());

        TagKey<Item> tofuTag = ForgeRegistries.ITEMS.tags().createTagKey(new ResourceLocation("forge", "tofus"));
        EDIBLE.removeAll(ForgeRegistries.ITEMS.tags().getTag(tofuTag).stream().toList());
    }

    public static boolean catFoodItems(ItemStack stack) {
        Iterator foods = EDIBLE.iterator();
        Item i;
        do {
            if (!foods.hasNext()) {
                return false;
            }

            i = (Item) foods.next();
        } while (stack.getItem() != i);

        return true;
    }

    public static Map<UUID, String> getCustomCats() {
        return CUSTOM_CATS;
    }

    static {
        CUSTOM_CATS.put(UUID.fromString("9b1ef261-ebc0-42ad-aacb-621b50fb8269"), "penny");
        CUSTOM_CATS.put(UUID.fromString("966ebb69-a63d-4bb2-ac90-ed39d8c64b80"), "spinny");
    }
}
