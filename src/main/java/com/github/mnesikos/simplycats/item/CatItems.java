package com.github.mnesikos.simplycats.item;

import com.github.mnesikos.simplycats.Ref;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.oredict.OreDictionary;

import java.util.ArrayList;
import java.util.List;

public class CatItems {
    public static final CreativeTabs CREATIVE_TAB = new CreativeTabs(Ref.MODID + ".tab") {
        @Override
        public ItemStack getTabIconItem() {
            return new ItemStack(PET_CARRIER);
        }
    };

    // A list of metadata variants that the client will register models for
    public static final List<ItemVariant> VARIANTS = new ArrayList<>();

    public static final List<Item> ITEMS = new ArrayList<>();
    public static final ItemCatBook CAT_BOOK = registerItem("cat_book", new ItemCatBook());
    public static final ItemPetCarrier PET_CARRIER = registerItem("pet_carrier", new ItemPetCarrier());
    public static final ItemCertificate CERTIFICATE_ADOPT = registerItem("certificate_adopt", new ItemCertificate(true));
    public static final ItemCertificate CERTIFICATE_RELEASE = registerItem("certificate_release", new ItemCertificate(false));
    public static final ItemTreatBag TREAT_BAG = registerItem("treat_bag", new ItemTreatBag());
    public static final Item CATNIP = registerItem("catnip", new Item());
    public static final ItemLaserPointer LASER_POINTER = registerItem("laser_pointer", new ItemLaserPointer());
    public static final ItemSterilizationPotion STERILIZE_POTION = registerItem("sterilization_potion", new ItemSterilizationPotion());

    public static void registerOres() {
        OreDictionary.registerOre("cropCatnip", CATNIP);
        OreDictionary.registerOre("cropCatmint", CATNIP);
    }

    public static <T extends Item> T registerItem(String name, T item) {
        item.setRegistryName(name);
        item.setUnlocalizedName(Ref.MODID + "." + name);
        item.setCreativeTab(CREATIVE_TAB);
        ITEMS.add(item);
        return item;
    }

    // Adds the variants for each item to the list of variants
    public static void collectModelVariants() {
        for (Item item : CatItems.ITEMS) {
            VARIANTS.add(new ItemVariant(item, 0, item.getRegistryName().getResourcePath()));
        }
        // Skip 0 because that's already added
        for (int i = 1; i < PET_CARRIER.getNumModels(); i++) {
            VARIANTS.add(new ItemVariant(PET_CARRIER, i, "pet_carrier"));
        }
    }

    // Used by the client for registering item models
    // If Java had structs, this would be a struct
    public static class ItemVariant {
        public final int meta;
        public final String name;
        public final Item item;

        public ItemVariant(Item item, int meta, String name) {
            this.meta = meta;
            this.name = name;
            this.item = item;
        }
    }
}
