package com.github.mnesikos.simplycats.item;

import com.github.mnesikos.simplycats.SimplyCats;
import com.github.mnesikos.simplycats.block.SCBlocks;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemNameBlockItem;
import net.neoforged.neoforge.registries.DeferredItem;
import net.neoforged.neoforge.registries.DeferredRegister;

public class SCItems {
    public static final DeferredRegister.Items REGISTRAR = DeferredRegister.createItems(SimplyCats.MOD_ID);

    public static final DeferredItem<Item> PET_CARRIER = REGISTRAR.register("pet_carrier", PetCarrierItem::new);
    public static final DeferredItem<Item> CAT_BOOK = REGISTRAR.register("cat_book", CatBookItem::new);
    public static final DeferredItem<Item> ADOPT_CERTIFICATE = REGISTRAR.register("adopt_certificate", () -> new CertificateItem(true));
    public static final DeferredItem<Item> RELEASE_CERTIFICATE = REGISTRAR.register("release_certificate", () -> new CertificateItem(false));
    public static final DeferredItem<Item> TREAT_BAG = REGISTRAR.register("treat_bag", TreatBagItem::new);
    public static final DeferredItem<Item> CATNIP = REGISTRAR.register("catnip", () -> new Item(new Item.Properties()));
    public static final DeferredItem<Item> CATNIP_SEEDS = REGISTRAR.register("catnip_seeds", () -> new ItemNameBlockItem(SCBlocks.CATNIP_CROP.get(), new Item.Properties()));
    public static final DeferredItem<Item> LASER_POINTER = REGISTRAR.register("laser_pointer", LaserPointerItem::new);
    public static final DeferredItem<Item> STERILIZE_POTION = REGISTRAR.register("sterilization_potion", SterilizationPotionItem::new);
}
