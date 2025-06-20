package com.github.mnesikos.simplycats.compat.farmersrespite;

import com.github.mnesikos.simplycats.SimplyCats;
import com.github.mnesikos.simplycats.item.SCItems;
import net.minecraft.data.recipes.FinishedRecipe;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.material.FlowingFluid;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.level.material.Fluids;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidType;
import net.minecraftforge.fluids.ForgeFlowingFluid;
import net.minecraftforge.fml.ModList;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import umpaz.farmersrespite.common.fluid.TeaFluidType;
import umpaz.farmersrespite.common.tag.FRTags;
import umpaz.farmersrespite.data.builder.KettlePouringRecipeBuilder;
import umpaz.farmersrespite.data.builder.KettleRecipeBuilder;
import vectorwing.farmersdelight.common.item.DrinkableItem;
import vectorwing.farmersdelight.common.registry.ModEffects;
import vectorwing.farmersdelight.common.tag.ForgeTags;

import java.util.function.Consumer;

public class FarmersRespiteCompat {
    public static void register(IEventBus bus) {
        ITEMS.register(bus);
        FLUID_TYPES.register(bus);
        FLUIDS.register(bus);
    }

    public static final FoodProperties CATNIP_TEA_FOOD = new FoodProperties.Builder().alwaysEat().effect(() -> new MobEffectInstance(ModEffects.COMFORT.get(), 3600, 0), 1.0F).build();
    public static final FoodProperties LONG_CATNIP_TEA_FOOD = new FoodProperties.Builder().alwaysEat().effect(() -> new MobEffectInstance(ModEffects.COMFORT.get(), 5400, 0), 1.0F).build();

    public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, SimplyCats.MOD_ID);
    public static final RegistryObject<Item> CATNIP_TEA = ITEMS.register("catnip_tea", () -> new DrinkableItem(new Item.Properties().craftRemainder(Items.GLASS_BOTTLE).stacksTo(16).food(CATNIP_TEA_FOOD), true, false));
    public static final RegistryObject<Item> LONG_CATNIP_TEA = ITEMS.register("long_catnip_tea", () -> new DrinkableItem(new Item.Properties().craftRemainder(Items.GLASS_BOTTLE).stacksTo(16).food(LONG_CATNIP_TEA_FOOD), true, false));

    public static final DeferredRegister<FluidType> FLUID_TYPES = DeferredRegister.create(ForgeRegistries.Keys.FLUID_TYPES, SimplyCats.MOD_ID);
    public static final DeferredRegister<Fluid> FLUIDS = DeferredRegister.create(ForgeRegistries.FLUIDS, SimplyCats.MOD_ID);

    public static final RegistryObject<FluidType> CATNIP_TEA_FLUID = FLUID_TYPES.register("catnip_tea_type", () -> new TeaFluidType(-12493357));
    public static final RegistryObject<FlowingFluid> CATNIP_TEA_SOURCE = FLUIDS.register("catnip_tea", () -> new ForgeFlowingFluid.Source(FarmersRespiteCompat.CATNIP_TEA_FLUID_PROPERTIES));
    public static final RegistryObject<FlowingFluid> CATNIP_TEA_FLOWING = FLUIDS.register("flowing_catnip_tea", () -> new ForgeFlowingFluid.Flowing(FarmersRespiteCompat.CATNIP_TEA_FLUID_PROPERTIES));
    public static final ForgeFlowingFluid.Properties CATNIP_TEA_FLUID_PROPERTIES = new ForgeFlowingFluid.Properties(FarmersRespiteCompat.CATNIP_TEA_FLUID, FarmersRespiteCompat.CATNIP_TEA_SOURCE, FarmersRespiteCompat.CATNIP_TEA_FLOWING);
    public static final RegistryObject<FluidType> LONG_CATNIP_TEA_FLUID = FLUID_TYPES.register("long_catnip_tea_type", () -> new TeaFluidType(-12493357));
    public static final RegistryObject<FlowingFluid> LONG_CATNIP_TEA_SOURCE = FLUIDS.register("long_catnip_tea", () -> new ForgeFlowingFluid.Source(FarmersRespiteCompat.LONG_CATNIP_TEA_FLUID_PROPERTIES));
    public static final RegistryObject<FlowingFluid> LONG_CATNIP_TEA_FLOWING = FLUIDS.register("flowing_long_catnip_tea", () -> new ForgeFlowingFluid.Flowing(FarmersRespiteCompat.LONG_CATNIP_TEA_FLUID_PROPERTIES));
    public static final ForgeFlowingFluid.Properties LONG_CATNIP_TEA_FLUID_PROPERTIES = new ForgeFlowingFluid.Properties(FarmersRespiteCompat.LONG_CATNIP_TEA_FLUID, FarmersRespiteCompat.LONG_CATNIP_TEA_SOURCE, FarmersRespiteCompat.LONG_CATNIP_TEA_FLOWING);

    public static void buildRecipes(Consumer<FinishedRecipe> consumer) {
        KettleRecipeBuilder.kettleRecipe(new FluidStack(Fluids.WATER, 1000), new FluidStack(FarmersRespiteCompat.CATNIP_TEA_SOURCE.get(), 1000), 2400, 0.35F).addIngredient(SCItems.CATNIP.get()).addIngredient(FRTags.TEA_LEAVES).build(consumer);
        KettleRecipeBuilder.kettleRecipe(new FluidStack(FarmersRespiteCompat.CATNIP_TEA_SOURCE.get(), 1000), new FluidStack(FarmersRespiteCompat.LONG_CATNIP_TEA_SOURCE.get(), 1000), 2400, 0.35F).addIngredient(ForgeTags.MILK).build(consumer);
//        if (ModList.get().isLoaded("create")) {
            KettlePouringRecipeBuilder.kettlePouringRecipe(Items.GLASS_BOTTLE, FarmersRespiteCompat.CATNIP_TEA_SOURCE.get(), 250, FarmersRespiteCompat.CATNIP_TEA.get(), consumer);
            KettlePouringRecipeBuilder.kettlePouringRecipe(Items.GLASS_BOTTLE, FarmersRespiteCompat.LONG_CATNIP_TEA_SOURCE.get(), 250, FarmersRespiteCompat.LONG_CATNIP_TEA.get(), consumer);
//        }
    }
}
