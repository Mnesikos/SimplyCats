package com.github.mnesikos.simplycats.worldgen.villages;

import com.github.mnesikos.simplycats.SimplyCats;
import com.github.mnesikos.simplycats.block.SCBlocks;
import com.github.mnesikos.simplycats.item.SCItems;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableSet;
import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
import it.unimi.dsi.fastutil.ints.Int2ObjectOpenHashMap;
import net.minecraft.entity.Entity;
import net.minecraft.entity.merchant.villager.VillagerProfession;
import net.minecraft.entity.merchant.villager.VillagerTrades;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.item.MerchantOffer;
import net.minecraft.nbt.CompoundNBT;
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

    public static final RegistryObject<PointOfInterestType> ADOPTION_BOOK = POI_TYPES.register("adoption_book", () -> new PointOfInterestType("shelter_worker", PointOfInterestType.getBlockStates(SCBlocks.SHELTER_BOOK.get()), 2, 1));
    public static final RegistryObject<VillagerProfession> SHELTER_WORKER = PROFESSIONS.register("shelter_worker", () -> new VillagerProfession("shelter_worker", ADOPTION_BOOK.get(), ImmutableSet.of(), ImmutableSet.of(), SoundEvents.VILLAGER_WORK_LIBRARIAN));

    public static void registerPointOfInterests() {
        try {
            ObfuscationReflectionHelper.findMethod(PointOfInterestType.class, "registerBlockStates", PointOfInterestType.class).invoke(null, ADOPTION_BOOK.get());
        } catch (IllegalAccessException | InvocationTargetException e) {
            e.printStackTrace();
        }
    }

    public static void registerTrades() {
        VillagerTrades.ITrade[] level1 = new VillagerTrades.ITrade[]{new VillagerTrades.ItemsForEmeraldsTrade(SCItems.CATNIP_SEEDS.get(), 1, 8, 4, 8), new VillagerTrades.EmeraldForItemsTrade(SCItems.CATNIP.get(), 20, 16, 2), new VillagerTrades.ItemsForEmeraldsTrade(SCItems.STERILIZE_POTION.get(), 1, 8, 4, 16)};
        VillagerTrades.ITrade[] level2 = new VillagerTrades.ITrade[]{new ItemsForPetTrade(Items.COD, 3), new ItemsForPetTrade(Items.BONE, 4)};
        VillagerTrades.ITrade[] level3 = new VillagerTrades.ITrade[]{new VillagerTrades.ItemsForEmeraldsTrade(SCItems.PET_CARRIER.get(), 2, 1, 4, 16), new VillagerTrades.EmeraldForItemsTrade(SCItems.CATNIP_SEEDS.get(), 20, 16, 2)};
        VillagerTrades.ITrade[] level4 = new VillagerTrades.ITrade[]{new ItemsForPetTrade(Items.WHEAT_SEEDS, 5), new ItemsForPetTrade(Items.CARROT, 6)};
        VillagerTrades.ITrade[] level5 = new VillagerTrades.ITrade[]{new VillagerTrades.ItemsForEmeraldsTrade(SCItems.LASER_POINTER.get(), 1, 1, 1, 32), new VillagerTrades.ItemsForEmeraldsTrade(SCItems.TREAT_BAG.get(), 1, 1, 1, 32)};
        VillagerTrades.TRADES.put(SHELTER_WORKER.get(), toIntMap(ImmutableMap.of(1, level1, 2, level2, 3, level3, 4, level4, 5, level5)));
    }

    private static Int2ObjectMap<VillagerTrades.ITrade[]> toIntMap(ImmutableMap<Integer, VillagerTrades.ITrade[]> map) {
        return new Int2ObjectOpenHashMap<>(map);
    }

    public static class ItemsForPetTrade implements VillagerTrades.ITrade {
        private final ItemStack petCarrierItem;
        private final Item itemCost;

        private ItemsForPetTrade(IItemProvider itemCost, int setDamageValue) {
            ItemStack carrierItem = new ItemStack(SCItems.PET_CARRIER.get(), 1, new CompoundNBT());
            carrierItem.setDamageValue(setDamageValue);
            this.petCarrierItem = carrierItem;
            this.itemCost = itemCost.asItem();
        }

        @Nullable
        @Override
        public MerchantOffer getOffer(Entity entity, Random random) {
            int j = Math.min(8 + random.nextInt(9), 16);
            ItemStack price = new ItemStack(this.itemCost, j);
            return new MerchantOffer(price, this.petCarrierItem, 2, 16, 0.2f);
        }
    }
}
