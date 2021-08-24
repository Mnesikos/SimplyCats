package com.github.mnesikos.simplycats.worldgen.villages;

import com.github.mnesikos.simplycats.Ref;
import com.github.mnesikos.simplycats.block.CatBlocks;
import com.github.mnesikos.simplycats.configuration.SCConfig;
import com.github.mnesikos.simplycats.item.CatItems;
import net.minecraft.entity.IMerchant;
import net.minecraft.entity.passive.EntityVillager;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.village.MerchantRecipe;
import net.minecraft.village.MerchantRecipeList;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.fml.common.registry.VillagerRegistry.VillagerCareer;
import net.minecraftforge.fml.common.registry.VillagerRegistry.VillagerProfession;
import net.minecraftforge.registries.IForgeRegistry;

import java.util.Random;

@GameRegistry.ObjectHolder(Ref.MODID)
public class CatProfessions {
    public static final VillagerProfession SHELTER_STAFF = null;
    public static VillagerCareer kennelWorker;

    @Mod.EventBusSubscriber(modid = Ref.MODID)
    public static class RegistrationHandler {
        @SubscribeEvent
        public static void onEvent(final RegistryEvent.Register<VillagerProfession> event) {
            final IForgeRegistry<VillagerProfession> registry = event.getRegistry();

            registry.register(new VillagerProfession(Ref.MODID + ":shelter_staff",
                    Ref.MODID + ":textures/entity/npcs/shelter_worker.png",
                    Ref.MODID + ":textures/entity/npcs/shelter_worker_zombie.png")
            );
        }
    }

    public static void associateCareersAndTrades() {
        kennelWorker = new VillagerCareer(SHELTER_STAFF, "kennel_worker");
        if (SCConfig.ADOPT_A_DOG)
            kennelWorker.addTrade(1, new AdoptADog());
        kennelWorker.addTrade(1, new AdoptACat());
        kennelWorker.addTrade(1, new EntityVillager.ListItemForEmeralds(CatBlocks.CROP_CATNIP.getSeed(), new EntityVillager.PriceInfo(-8, -4)));
        kennelWorker.addTrade(1, new EntityVillager.ListItemForEmeralds(CatItems.STERILIZE_POTION, new EntityVillager.PriceInfo(1, 1)));
    }

    public static class AdoptACat implements EntityVillager.ITradeList {
        private final ItemStack itemPurchase;
        private final EntityVillager.PriceInfo priceInfo;

        private AdoptACat() {
            itemPurchase = new ItemStack(CatItems.PET_CARRIER, 1);
            itemPurchase.setTagCompound(new NBTTagCompound());
            itemPurchase.setItemDamage(3);
            // PriceInfo(min, max) is a price range!
            priceInfo = new EntityVillager.PriceInfo(8, 16);
        }

        @Override
        public void addMerchantRecipe(IMerchant merchant, MerchantRecipeList recipeList, Random random) {
            int catPrice = 1;

            if (priceInfo != null) {
                catPrice = priceInfo.getPrice(random);
            }

            ItemStack stackToPay = new ItemStack(Items.FISH, catPrice, 0);
            recipeList.add(new MerchantRecipe(stackToPay, itemPurchase));
        }
    }

    public static class AdoptADog implements EntityVillager.ITradeList {
        private final ItemStack itemPurchase;
        private final EntityVillager.PriceInfo priceInfo;

        private AdoptADog() {
            itemPurchase = new ItemStack(CatItems.PET_CARRIER, 1);
            itemPurchase.setTagCompound(new NBTTagCompound());
            itemPurchase.setItemDamage(4);
            // PriceInfo(min, max) is a price range!
            priceInfo = new EntityVillager.PriceInfo(8, 16);
        }

        @Override
        public void addMerchantRecipe(IMerchant merchant, MerchantRecipeList recipeList, Random random) {
            int dogPrice = 1;

            if (priceInfo != null) {
                dogPrice = priceInfo.getPrice(random);
            }

            ItemStack stackToPay = new ItemStack(Items.BONE, dogPrice, 0);
            recipeList.add(new MerchantRecipe(stackToPay, itemPurchase));
        }
    }
}
