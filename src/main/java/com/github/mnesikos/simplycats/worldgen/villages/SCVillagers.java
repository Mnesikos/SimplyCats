package com.github.mnesikos.simplycats.worldgen.villages;

import com.github.mnesikos.simplycats.SimplyCats;
import com.github.mnesikos.simplycats.block.SCBlocks;
import com.github.mnesikos.simplycats.item.SCItems;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableSet;
import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
import it.unimi.dsi.fastutil.ints.Int2ObjectOpenHashMap;
import net.minecraft.block.Blocks;
import net.minecraft.entity.Entity;
import net.minecraft.entity.merchant.villager.VillagerProfession;
import net.minecraft.entity.merchant.villager.VillagerTrades;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.item.MerchantOffer;
import net.minecraft.util.IItemProvider;
import net.minecraft.util.SoundEvents;
import net.minecraft.village.PointOfInterestType;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.fml.common.ObfuscationReflectionHelper;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

import javax.annotation.Nullable;
import java.lang.reflect.InvocationTargetException;
import java.util.Random;

public class SCVillagers {
    public static final DeferredRegister<VillagerProfession> PROFESSIONS = DeferredRegister.create(ForgeRegistries.PROFESSIONS, SimplyCats.MOD_ID);
    public static final DeferredRegister<PointOfInterestType> POI_TYPES = DeferredRegister.create(ForgeRegistries.POI_TYPES, SimplyCats.MOD_ID);

    // todo change interest block (bookshelf) to custom block
    public static final RegistryObject<PointOfInterestType> ADOPTION_BOOK = POI_TYPES.register("adoption_book", () -> new PointOfInterestType("shelter_worker", PointOfInterestType.getBlockStates(SCBlocks.SHELTER_BOOK.get()), 1, 1));
    public static final RegistryObject<VillagerProfession> SHELTER_WORKER = PROFESSIONS.register("shelter_worker", () -> new VillagerProfession("shelter_worker", ADOPTION_BOOK.get(), ImmutableSet.of(), ImmutableSet.of(), SoundEvents.VILLAGER_WORK_LIBRARIAN));

    public static void registerPointOfInterests() {
        try {
            ObfuscationReflectionHelper.findMethod(PointOfInterestType.class, "registerBlockStates", PointOfInterestType.class).invoke(null, ADOPTION_BOOK.get());
        } catch (IllegalAccessException | InvocationTargetException e) {
            e.printStackTrace();
        }
    }

    public static void registerTrades() {
        VillagerTrades.ITrade[] level1 = new VillagerTrades.ITrade[]{new VillagerTrades.ItemsForEmeraldsTrade(SCItems.CATNIP_SEEDS.get(), 1, 12, 4, 8), new VillagerTrades.EmeraldForItemsTrade(SCItems.CATNIP.get(), 12, 4, 8)};
        VillagerTrades.ITrade[] level2 = new VillagerTrades.ITrade[]{new ItemsForPetTrade(Items.BONE), new ItemsForPetTrade(Items.COD), new VillagerTrades.ItemsForEmeraldsTrade(Items.COBWEB, 2, 1, 4, 16)}; //todo cobweb -> empty pet carrier
        VillagerTrades.ITrade[] level3 = new VillagerTrades.ITrade[]{new VillagerTrades.ItemsForEmeraldsTrade(SCItems.STERILIZE_POTION.get(), 1, 4, 4, 16)};
        VillagerTrades.ITrade[] level4 = new VillagerTrades.ITrade[]{new ItemsForPetTrade(Items.WHEAT_SEEDS), new ItemsForPetTrade(Items.CARROT), new VillagerTrades.ItemsForEmeraldsTrade(Items.SADDLE, 2, 1, 2, 16)};
        VillagerTrades.ITrade[] level5 = new VillagerTrades.ITrade[]{new VillagerTrades.ItemsForEmeraldsTrade(SCItems.LASER_POINTER.get(), 1, 1, 1, 32)};
        VillagerTrades.TRADES.put(SHELTER_WORKER.get(), toIntMap(ImmutableMap.of(1, level1, 2, level2, 3, level3, 4, level4, 5, level5)));
    }

    private static Int2ObjectMap<VillagerTrades.ITrade[]> toIntMap(ImmutableMap<Integer, VillagerTrades.ITrade[]> map) {
        return new Int2ObjectOpenHashMap<>(map);
    }

    public static class ItemsForPetTrade implements VillagerTrades.ITrade {
        private final ItemStack itemPurchase;
        private final Item itemCost;

        private ItemsForPetTrade(IItemProvider itemCost/*, IItemProvider itemPurchase --OR-- int carrierItemDamage*/) { //todo full carrier variants
            this.itemPurchase = new ItemStack(Items.EMERALD, 1); //itemPurchase???
            /*itemPurchase = new ItemStack(CatItems.PET_CARRIER, 1);
            itemPurchase.setTagCompound(new NBTTagCompound());
            itemPurchase.setItemDamage(carrierItemDamage); // 3 = cat, 4 = dog, ...*/
            this.itemCost = itemCost.asItem();
        }

        @Nullable
        @Override
        public MerchantOffer getOffer(Entity entity, Random random) {
            int j = Math.min(8 + random.nextInt(9), 16);
            ItemStack price = new ItemStack(this.itemCost, j);
            return new MerchantOffer(price, this.itemPurchase, 2, 16, 0.2f); //price, purchase, maxUses, xp, priceMultiplier
        }
    }
}
