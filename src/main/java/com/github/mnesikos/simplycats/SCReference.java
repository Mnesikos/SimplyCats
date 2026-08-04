package com.github.mnesikos.simplycats;

import com.google.common.collect.Maps;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.ItemTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;

import java.util.*;

public class SCReference {
    private static final Map<UUID, String> CUSTOM_CATS = Maps.newHashMap();
    private static final List<Item> EDIBLE = new ArrayList<>();

    public static boolean isRatEntity(Entity entity) {
        String entityType = EntityType.getKey(entity.getType()).toString();
        return entityType.equals("rats:rat")/* || entityType.equals("zawa:brownrat")*/;
    }

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

        addTagItems(ItemTags.FISHES);

        // todo double check tag naming && fix this
        addTagItems(itemTag("forge", "meats/raw"));
        addTagItems(itemTag("forge", "meats/cooked"));
        addTagItems(itemTag("forge", "meats"));

        removeTagItems(itemTag("forge", "tofus"));
    }

    private static TagKey<Item> itemTag(String namespace, String path) {
        return TagKey.create(Registries.ITEM, ResourceLocation.fromNamespaceAndPath(namespace, path));
    }

    private static void addTagItems(TagKey<Item> tag) {
        BuiltInRegistries.ITEM.getTag(tag).ifPresent(holders -> holders.forEach(holder -> EDIBLE.add(holder.value())));
    }

    private static void removeTagItems(TagKey<Item> tag) {
        BuiltInRegistries.ITEM.getTag(tag).ifPresent(holders -> holders.forEach(holder -> EDIBLE.remove(holder.value())));
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
