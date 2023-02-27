package com.github.mnesikos.simplycats.item;

import com.github.mnesikos.simplycats.SimplyCats;
import com.github.mnesikos.simplycats.block.SCBlocks;
import net.minecraft.world.item.ItemNameBlockItem;
import net.minecraft.world.item.Item;
import net.minecraftforge.registries.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

public class SCItems {
    public static final DeferredRegister<Item> REGISTRAR = DeferredRegister.create(ForgeRegistries.ITEMS, SimplyCats.MOD_ID);

    public static final RegistryObject<Item> CAT_BOOK = REGISTRAR.register("cat_book", CatBookItem::new);
    public static final RegistryObject<Item> PET_CARRIER = REGISTRAR.register("pet_carrier", PetCarrierItem::new);
    public static final RegistryObject<Item> ADOPT_CERTIFICATE = REGISTRAR.register("adopt_certificate", () -> new CertificateItem(true));
    public static final RegistryObject<Item> RELEASE_CERTIFICATE = REGISTRAR.register("release_certificate", () -> new CertificateItem(false));
    public static final RegistryObject<Item> TREAT_BAG = REGISTRAR.register("treat_bag", TreatBagItem::new);
    public static final RegistryObject<Item> CATNIP = REGISTRAR.register("catnip", () -> new Item(new Item.Properties().tab(SimplyCats.ITEM_GROUP)));
    public static final RegistryObject<Item> CATNIP_SEEDS = REGISTRAR.register("catnip_seeds", () -> new ItemNameBlockItem(SCBlocks.CATNIP_CROP.get(), new Item.Properties().tab(SimplyCats.ITEM_GROUP)));
    public static final RegistryObject<Item> LASER_POINTER = REGISTRAR.register("laser_pointer", LaserPointerItem::new);
    public static final RegistryObject<Item> STERILIZE_POTION = REGISTRAR.register("sterilization_potion", SterilizationPotionItem::new);
}
