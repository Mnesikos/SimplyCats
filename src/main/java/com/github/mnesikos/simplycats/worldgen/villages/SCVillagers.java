package com.github.mnesikos.simplycats.worldgen.villages;

import com.github.mnesikos.simplycats.SimplyCats;
import com.github.mnesikos.simplycats.block.SCBlocks;
import com.github.mnesikos.simplycats.item.SCItems;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableSet;
import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
import it.unimi.dsi.fastutil.ints.Int2ObjectOpenHashMap;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.npc.VillagerProfession;
import net.minecraft.world.entity.npc.VillagerTrades;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.trading.MerchantOffer;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.level.ItemLike;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.entity.ai.village.poi.PoiType;
import net.minecraftforge.fml.util.ObfuscationReflectionHelper;
import net.minecraftforge.registries.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

import javax.annotation.Nullable;
import java.lang.reflect.InvocationTargetException;
import java.util.Random;

public class SCVillagers {
    public static final DeferredRegister<VillagerProfession> PROFESSIONS = DeferredRegister.create(ForgeRegistries.PROFESSIONS, SimplyCats.MOD_ID);
    public static final DeferredRegister<PoiType> POI_TYPES = DeferredRegister.create(ForgeRegistries.POI_TYPES, SimplyCats.MOD_ID);

    public static final RegistryObject<PoiType> ADOPTION_BOOK = POI_TYPES.register("adoption_book", () -> new PoiType("shelter_worker", PoiType.getBlockStates(SCBlocks.SHELTER_BOOK.get()), 2, 1));
    public static final RegistryObject<VillagerProfession> SHELTER_WORKER = PROFESSIONS.register("shelter_worker", () -> new VillagerProfession("shelter_worker", ADOPTION_BOOK.get(), ImmutableSet.of(), ImmutableSet.of(), SoundEvents.VILLAGER_WORK_LIBRARIAN));

    public static void registerPointOfInterests() {
        try {
            ObfuscationReflectionHelper.findMethod(PoiType.class, "registerBlockStates", PoiType.class).invoke(null, ADOPTION_BOOK.get());
        } catch (IllegalAccessException | InvocationTargetException e) {
            e.printStackTrace();
        }
    }

    public static void registerTrades() {
        VillagerTrades.ItemListing[] level1 = new VillagerTrades.ItemListing[]{new VillagerTrades.ItemsForEmeralds(SCItems.CATNIP_SEEDS.get(), 1, 8, 4, 8), new VillagerTrades.EmeraldForItems(SCItems.CATNIP.get(), 20, 16, 2), new VillagerTrades.ItemsForEmeralds(SCItems.STERILIZE_POTION.get(), 1, 8, 4, 16)};
        VillagerTrades.ItemListing[] level2 = new VillagerTrades.ItemListing[]{new ItemsForPetTrade(Items.COD, 3), new ItemsForPetTrade(Items.BONE, 4)};
        VillagerTrades.ItemListing[] level3 = new VillagerTrades.ItemListing[]{new VillagerTrades.ItemsForEmeralds(SCItems.PET_CARRIER.get(), 2, 1, 4, 16), new VillagerTrades.EmeraldForItems(SCItems.CATNIP_SEEDS.get(), 20, 16, 2)};
        VillagerTrades.ItemListing[] level4 = new VillagerTrades.ItemListing[]{new ItemsForPetTrade(Items.WHEAT_SEEDS, 5), new ItemsForPetTrade(Items.CARROT, 6)};
        VillagerTrades.ItemListing[] level5 = new VillagerTrades.ItemListing[]{new VillagerTrades.ItemsForEmeralds(SCItems.LASER_POINTER.get(), 1, 1, 1, 32), new VillagerTrades.ItemsForEmeralds(SCItems.TREAT_BAG.get(), 1, 1, 1, 32)};
        VillagerTrades.TRADES.put(SHELTER_WORKER.get(), toIntMap(ImmutableMap.of(1, level1, 2, level2, 3, level3, 4, level4, 5, level5)));
    }

    private static Int2ObjectMap<VillagerTrades.ItemListing[]> toIntMap(ImmutableMap<Integer, VillagerTrades.ItemListing[]> map) {
        return new Int2ObjectOpenHashMap<>(map);
    }

    public static class ItemsForPetTrade implements VillagerTrades.ItemListing {
        private final ItemStack petCarrierItem;
        private final Item itemCost;

        private ItemsForPetTrade(ItemLike itemCost, int setDamageValue) {
            ItemStack carrierItem = new ItemStack(SCItems.PET_CARRIER.get(), 1, new CompoundTag());
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
