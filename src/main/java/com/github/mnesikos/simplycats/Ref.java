package com.github.mnesikos.simplycats;

import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.tags.ItemTags;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.Util;

import java.util.*;

public class Ref {
    public static final String MOD_ID = "simplycats";
    public static final String VERSION = "@VERSION@";

    public static final List<? extends String> PREY_LIST = Util.make(new ArrayList<>(), (prey) -> {
        prey.addAll(Arrays.asList("minecraft:bat", "minecraft:parrot", "minecraft:chicken",
                "minecraft:rabbit", "minecraft:silverfish", "rats:rat", "zawa:brownrat", "zawa:cockatoo", "zawa:frigate",
                "zawa:macaw", "zawa:rattlesnake", "zawa:toucan", "zawa:treefrog", "exoticbirds:woodpecker", "birdwmod:brown_booby",
                "birdwmod:eastern_bluebird", "birdwmod:eurasian_bullfinch", "birdwmod:great_grey_owl", "birdwmod:green_heron",
                "birdwmod:hoatzin", "birdwmod:killdeer", "birdwmod:kingofsaxony_bird_of_paradise", "birdwmod:northern_mockingbird",
                "birdwmod:redflanked_bluetail", "birdwmod:rednecked_nightjar", "birdwmod:stellers_eider", "birdwmod:turquoisebrowed_motmot",
                "exoticbirds:bluejay", "exoticbirds:booby", "exoticbirds:budgerigar", "exoticbirds:cardinal", "exoticbirds:duck",
                "exoticbirds:gouldianfinch", "exoticbirds:hummingbird", "exoticbirds:kingfisher", "exoticbirds:kiwi",
                "exoticbirds:kookaburra", "exoticbirds:lyrebird", "exoticbirds:magpie", "exoticbirds:parrot", "exoticbirds:pigeon",
                "exoticbirds:roadrunner", "exoticbirds:robin", "exoticbirds:toucan", "animania:hamster", "animania:frog",
                "animania:toad", "animania:buck_cottontail", "animania:doe_cottontail", "animania:kit_cottontail", "animania:buck_chinchilla",
                "animania:doe_chinchilla", "animania:kit_chinchilla", "animania:buck_dutch", "animania:doe_dutch", "animania:kit_dutch",
                "animania:buck_havana", "animania:doe_havana", "animania:kit_havana", "animania:buck_jack", "animania:doe_jack", "animania:kit_jack",
                "animania:buck_new_zealand", "animania:doe_new_zealand", "animania:kit_new_zealand", "animania:buck_rex", "animania:doe_rex",
                "animania:kit_rex", "animania:buck_lop", "animania:doe_lop", "animania:kit_lop", "animania:rooster_leghorn", "animania:rooster_orpington",
                "animania:rooster_plymouth_rock", "animania:rooster_rhode_island_red", "animania:rooster_wyandotte", "animania:hen_leghorn",
                "animania:hen_orpington", "animania:hen_plymouth_rock", "animania:hen_rhode_island_red", "animania:hen_wyandotte",
                "animania:chick_leghorn", "animania:chick_orpington", "animania:chick_plymouth_rock", "animania:chick_rhode_island_red",
                "animania:chick_wyandotte"));
    });
    private static final List<Item> EDIBLE = new ArrayList<>();

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
        EDIBLE.removeAll(tofuDictionary);
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
