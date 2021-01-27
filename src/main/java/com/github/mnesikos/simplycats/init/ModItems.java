package com.github.mnesikos.simplycats.init;

import com.github.mnesikos.simplycats.item.*;
import net.minecraft.item.Item;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

import static com.github.mnesikos.simplycats.Ref.MOD_ID;

public class ModItems {
    public static final DeferredRegister<Item> REGISTER = new DeferredRegister<>(ForgeRegistries.ITEMS, MOD_ID);
    public static final RegistryObject<Item> CAT_BOOK = REGISTER.register("cat_book", ItemCatBook::new);
    public static final RegistryObject<Item> PET_CARRIER = REGISTER.register("pet_carrier", ItemPetCarrier::new);
    public static final RegistryObject<Item> ADOPT_CERTIFICATE = REGISTER.register("certificate_adopt", ItemCertificate::new);
    public static final RegistryObject<Item> RELEASE_CERTIFICATE = REGISTER.register("certificate_release", ItemCertificate::new);
    public static final RegistryObject<Item> TREAT_BAG = REGISTER.register("treat_bag", ItemTreatBag::new);
    public static final RegistryObject<Item> CATNIP = REGISTER.register("catnip", ItemCatnip::new);
    public static final RegistryObject<Item> CATNIP_SEEDS = REGISTER.register("catnip_seeds", ItemCatnipSeeds::new);
    public static final RegistryObject<Item> LASER_POINTER = REGISTER.register("laser_pointer", ItemLaserPointer::new);
    public static final RegistryObject<Item> STERILIZE_POTION = REGISTER.register("sterilization_potion", ItemSterilizationPotion::new);

    /*public static final RegistryObject<Item> CAT_BOWL = REGISTER("cat_bowl", () -> new BlockItem(ModBlocks.CAT_BOWL.get(), new Item.Properties().group(SimplyCats.GROUP)));
    public static final RegistryObject<Item> WHITE_CAT_BOWL = REGISTER("white_cat_bowl", () -> new BlockItem(ModBlocks.CAT_BOWL.get(), new Item.Properties().group(SimplyCats.GROUP)));
    public static final RegistryObject<Item> ORANGE_CAT_BOWL = REGISTER("orange_cat_bowl", () -> new BlockItem(ModBlocks.CAT_BOWL.get(), new Item.Properties().group(SimplyCats.GROUP)));
    public static final RegistryObject<Item> MAGENTA_CAT_BOWL = REGISTER("magenta_cat_bowl", () -> new BlockItem(ModBlocks.CAT_BOWL.get(), new Item.Properties().group(SimplyCats.GROUP)));
    public static final RegistryObject<Item> LIGHT_BLUE_CAT_BOWL = REGISTER("light_blue_cat_bowl", () -> new BlockItem(ModBlocks.CAT_BOWL.get(), new Item.Properties().group(SimplyCats.GROUP)));
    public static final RegistryObject<Item> YELLOW_CAT_BOWL = REGISTER("yellow_cat_bowl", () -> new BlockItem(ModBlocks.CAT_BOWL.get(), new Item.Properties().group(SimplyCats.GROUP)));
    public static final RegistryObject<Item> LIME_CAT_BOWL = REGISTER("lime_cat_bowl", () -> new BlockItem(ModBlocks.CAT_BOWL.get(), new Item.Properties().group(SimplyCats.GROUP)));
    public static final RegistryObject<Item> PINK_CAT_BOWL = REGISTER("pink_cat_bowl", () -> new BlockItem(ModBlocks.CAT_BOWL.get(), new Item.Properties().group(SimplyCats.GROUP)));
    public static final RegistryObject<Item> GRAY_CAT_BOWL = REGISTER("gray_cat_bowl", () -> new BlockItem(ModBlocks.CAT_BOWL.get(), new Item.Properties().group(SimplyCats.GROUP)));
    public static final RegistryObject<Item> LIGHT_GRAY_CAT_BOWL = REGISTER("light_gray_cat_bowl", () -> new BlockItem(ModBlocks.CAT_BOWL.get(), new Item.Properties().group(SimplyCats.GROUP)));
    public static final RegistryObject<Item> CYAN_CAT_BOWL = REGISTER("cyan_cat_bowl", () -> new BlockItem(ModBlocks.CAT_BOWL.get(), new Item.Properties().group(SimplyCats.GROUP)));
    public static final RegistryObject<Item> PURPLE_CAT_BOWL = REGISTER("purple_cat_bowl", () -> new BlockItem(ModBlocks.CAT_BOWL.get(), new Item.Properties().group(SimplyCats.GROUP)));
    public static final RegistryObject<Item> BLUE_CAT_BOWL = REGISTER("blue_cat_bowl", () -> new BlockItem(ModBlocks.CAT_BOWL.get(), new Item.Properties().group(SimplyCats.GROUP)));
    public static final RegistryObject<Item> BROWN_CAT_BOWL = REGISTER("brown_cat_bowl", () -> new BlockItem(ModBlocks.CAT_BOWL.get(), new Item.Properties().group(SimplyCats.GROUP)));
    public static final RegistryObject<Item> GREEN_CAT_BOWL = REGISTER("green_cat_bowl", () -> new BlockItem(ModBlocks.CAT_BOWL.get(), new Item.Properties().group(SimplyCats.GROUP)));
    public static final RegistryObject<Item> RED_CAT_BOWL = REGISTER("red_cat_bowl", () -> new BlockItem(ModBlocks.CAT_BOWL.get(), new Item.Properties().group(SimplyCats.GROUP)));
    public static final RegistryObject<Item> BLACK_CAT_BOWL = REGISTER("black_cat_bowl", () -> new BlockItem(ModBlocks.CAT_BOWL.get(), new Item.Properties().group(SimplyCats.GROUP)));

    public static final RegistryObject<Item> LITTER_BOX = REGISTER("litter_box", () -> new BlockItem(ModBlocks.LITTER_BOX.get(), new Item.Properties().group(SimplyCats.GROUP)));
    public static final RegistryObject<Item> WHITE_LITTER_BOX = REGISTER("white_litter_box", () -> new BlockItem(ModBlocks.LITTER_BOX.get(), new Item.Properties().group(SimplyCats.GROUP)));
    public static final RegistryObject<Item> ORANGE_LITTER_BOX = REGISTER("orange_litter_box", () -> new BlockItem(ModBlocks.LITTER_BOX.get(), new Item.Properties().group(SimplyCats.GROUP)));
    public static final RegistryObject<Item> MAGENTA_LITTER_BOX = REGISTER("magenta_litter_box", () -> new BlockItem(ModBlocks.LITTER_BOX.get(), new Item.Properties().group(SimplyCats.GROUP)));
    public static final RegistryObject<Item> LIGHT_BLUE_LITTER_BOX = REGISTER("light_blue_litter_box", () -> new BlockItem(ModBlocks.LITTER_BOX.get(), new Item.Properties().group(SimplyCats.GROUP)));
    public static final RegistryObject<Item> YELLOW_LITTER_BOX = REGISTER("yellow_litter_box", () -> new BlockItem(ModBlocks.LITTER_BOX.get(), new Item.Properties().group(SimplyCats.GROUP)));
    public static final RegistryObject<Item> LIME_LITTER_BOX = REGISTER("lime_litter_box", () -> new BlockItem(ModBlocks.LITTER_BOX.get(), new Item.Properties().group(SimplyCats.GROUP)));
    public static final RegistryObject<Item> PINK_LITTER_BOX = REGISTER("pink_litter_box", () -> new BlockItem(ModBlocks.LITTER_BOX.get(), new Item.Properties().group(SimplyCats.GROUP)));
    public static final RegistryObject<Item> GRAY_LITTER_BOX = REGISTER("gray_litter_box", () -> new BlockItem(ModBlocks.LITTER_BOX.get(), new Item.Properties().group(SimplyCats.GROUP)));
    public static final RegistryObject<Item> LIGHT_GRAY_LITTER_BOX = REGISTER("light_gray_litter_box", () -> new BlockItem(ModBlocks.LITTER_BOX.get(), new Item.Properties().group(SimplyCats.GROUP)));
    public static final RegistryObject<Item> CYAN_LITTER_BOX = REGISTER("cyan_litter_box", () -> new BlockItem(ModBlocks.LITTER_BOX.get(), new Item.Properties().group(SimplyCats.GROUP)));
    public static final RegistryObject<Item> PURPLE_LITTER_BOX = REGISTER("purple_litter_box", () -> new BlockItem(ModBlocks.LITTER_BOX.get(), new Item.Properties().group(SimplyCats.GROUP)));
    public static final RegistryObject<Item> BLUE_LITTER_BOX = REGISTER("blue_litter_box", () -> new BlockItem(ModBlocks.LITTER_BOX.get(), new Item.Properties().group(SimplyCats.GROUP)));
    public static final RegistryObject<Item> BROWN_LITTER_BOX = REGISTER("brown_litter_box", () -> new BlockItem(ModBlocks.LITTER_BOX.get(), new Item.Properties().group(SimplyCats.GROUP)));
    public static final RegistryObject<Item> GREEN_LITTER_BOX = REGISTER("green_litter_box", () -> new BlockItem(ModBlocks.LITTER_BOX.get(), new Item.Properties().group(SimplyCats.GROUP)));
    public static final RegistryObject<Item> RED_LITTER_BOX = REGISTER("red_litter_box", () -> new BlockItem(ModBlocks.LITTER_BOX.get(), new Item.Properties().group(SimplyCats.GROUP)));
    public static final RegistryObject<Item> BLACK_LITTER_BOX = REGISTER("black_litter_box", () -> new BlockItem(ModBlocks.LITTER_BOX.get(), new Item.Properties().group(SimplyCats.GROUP)));

    public static final RegistryObject<Item> OAK_SCRATCHING_POST = REGISTER("oak_scratching_post", () -> new BlockItem(ModBlocks.OAK_SCRATCHING_POST.get(), new Item.Properties().group(SimplyCats.GROUP)));
    public static final RegistryObject<Item> SPRUCE_SCRATCHING_POST = REGISTER("spruce_scratching_post", () -> new BlockItem(ModBlocks.SPRUCE_SCRATCHING_POST.get(), new Item.Properties().group(SimplyCats.GROUP)));
    public static final RegistryObject<Item> BIRCH_SCRATCHING_POST = REGISTER("birch_scratching_post", () -> new BlockItem(ModBlocks.BIRCH_SCRATCHING_POST.get(), new Item.Properties().group(SimplyCats.GROUP)));
    public static final RegistryObject<Item> JUNGLE_SCRATCHING_POST = REGISTER("jungle_scratching_post", () -> new BlockItem(ModBlocks.JUNGLE_SCRATCHING_POST.get(), new Item.Properties().group(SimplyCats.GROUP)));
    public static final RegistryObject<Item> ACACIA_SCRATCHING_POST = REGISTER("acacia_scratching_post", () -> new BlockItem(ModBlocks.ACACIA_SCRATCHING_POST.get(), new Item.Properties().group(SimplyCats.GROUP)));
    public static final RegistryObject<Item> DARK_OAK_SCRATCHING_POST = REGISTER("dark_oak_scratching_post", () -> new BlockItem(ModBlocks.DARK_OAK_SCRATCHING_POST.get(), new Item.Properties().group(SimplyCats.GROUP)));*/

    public static void registerOres() { // TODO Tags https://mcforge.readthedocs.io/en/1.13.x/utilities/tags/#migration-from-oredictionary
        /*OreDictionary.registerOre("cropCatnip", CATNIP);
        OreDictionary.registerOre("cropCatmint", CATNIP);*/
    }
}
