package com.github.mnesikos.simplycats.init;

import com.github.mnesikos.simplycats.Ref;
import com.github.mnesikos.simplycats.configuration.SCConfig;
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
import net.minecraftforge.fml.common.registry.VillagerRegistry.*;
import net.minecraftforge.registries.IForgeRegistry;

import java.util.Random;

@GameRegistry.ObjectHolder(Ref.MODID)
public class ModProfessions {
    public final static VillagerProfession SHELTER_STAFF = null;

    public static VillagerCareer KENNEL_WORKER;

    @Mod.EventBusSubscriber(modid = Ref.MODID)
    public static class RegistrationHandler {
        @SubscribeEvent
        public static void onEvent(final RegistryEvent.Register<VillagerProfession> event) {
            final IForgeRegistry<VillagerProfession> registry = event.getRegistry();

            registry.register(new VillagerProfession(Ref.MODID+":shelter_staff",
                    Ref.MODID+":textures/entity/npcs/shelter_worker.png",
                    Ref.MODID+":textures/entity/npcs/shelter_worker_zombie.png")
            );
        }
    }

    public static void associateCareersAndTrades() {
        if (SCConfig.ADOPT_A_DOG)
            KENNEL_WORKER = (new VillagerCareer(SHELTER_STAFF, "kennel_worker")).addTrade(1, new AdoptACat()).addTrade(1, new AdoptADog());
        else
            KENNEL_WORKER = (new VillagerCareer(SHELTER_STAFF, "kennel_worker")).addTrade(1, new AdoptACat());
    }

    public static class AdoptACat implements EntityVillager.ITradeList {
        private ItemStack PURCHASE;
        private EntityVillager.PriceInfo PRICE;

        private AdoptACat() {
            PURCHASE = new ItemStack(ModItems.PET_CARRIER, 1);
            PURCHASE.setTagCompound(new NBTTagCompound());
            PURCHASE.setItemDamage(3);
            // PriceInfo(min, max) is a price range!
            PRICE = new EntityVillager.PriceInfo(8, 16);
        }

        @Override
        public void addMerchantRecipe(IMerchant merchant, MerchantRecipeList recipeList, Random random) {
            int catPrice = 1;

            if (PRICE != null) {
                catPrice = PRICE.getPrice(random);
            }

            ItemStack stackToPay = new ItemStack(Items.FISH, catPrice, 0);
            recipeList.add(new MerchantRecipe(stackToPay, PURCHASE));
        }
    }

    public static class AdoptADog implements EntityVillager.ITradeList {
        private ItemStack PURCHASE;
        private EntityVillager.PriceInfo PRICE;

        private AdoptADog() {
            PURCHASE = new ItemStack(ModItems.PET_CARRIER, 1);
            PURCHASE.setTagCompound(new NBTTagCompound());
            PURCHASE.setItemDamage(4);
            // PriceInfo(min, max) is a price range!
            PRICE = new EntityVillager.PriceInfo(8, 16);
        }

        @Override
        public void addMerchantRecipe(IMerchant merchant, MerchantRecipeList recipeList, Random random) {
            int dogPrice = 1;

            if (PRICE != null) {
                dogPrice = PRICE.getPrice(random);
            }

            ItemStack stackToPay = new ItemStack(Items.BONE, dogPrice, 0);
            recipeList.add(new MerchantRecipe(stackToPay, PURCHASE));
        }
    }
}
