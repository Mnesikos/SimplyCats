package com.github.mnesikos.simplycats;

import com.google.common.collect.Maps;
import com.mojang.blaze3d.platform.GlStateManager;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.tags.Tag;
import net.minecraft.tags.ItemTags;
import net.minecraft.util.NonNullList;
import net.minecraft.resources.ResourceLocation;

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

        EDIBLE.addAll(ItemTags.FISHES.getValues());

        // todo double check tag naming
        Tag<Item> meatsRawTag = ItemTags.getAllTags().getTagOrEmpty(new ResourceLocation("forge", "meats/raw"));
        EDIBLE.addAll(meatsRawTag.getValues());
        Tag<Item> meatsCookedTag = ItemTags.getAllTags().getTagOrEmpty(new ResourceLocation("forge", "meats/cooked"));
        EDIBLE.addAll(meatsCookedTag.getValues());
        Tag<Item> meatsTag = ItemTags.getAllTags().getTagOrEmpty(new ResourceLocation("forge", "meats"));
        EDIBLE.addAll(meatsTag.getValues());

        Tag<Item> tofuTag = ItemTags.getAllTags().getTagOrEmpty(new ResourceLocation("forge", "tofus"));
        EDIBLE.removeAll(tofuTag.getValues());
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
