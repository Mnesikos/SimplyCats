package com.github.mnesikos.simplycats.compat;

import com.github.mnesikos.simplycats.SimplyCats;
import com.github.mnesikos.simplycats.item.SCItems;
import com.google.common.collect.Lists;
import com.simibubi.create.content.fluids.transfer.EmptyingRecipe;
import com.simibubi.create.content.fluids.transfer.FillingRecipe;
import com.simibubi.create.content.processing.recipe.ProcessingRecipeBuilder;
import net.minecraft.data.recipes.FinishedRecipe;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.ItemLike;
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

import java.util.List;
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
        SCKettleRecipeBuilder.kettleRecipe(new FluidStack(Fluids.WATER, 1000), new FluidStack(FarmersRespiteCompat.CATNIP_TEA_SOURCE.get(), 1000), 2400, 0.35F).addIngredient(SCItems.CATNIP.get()).addIngredient(FRTags.TEA_LEAVES).build(consumer);
        SCKettleRecipeBuilder.kettleRecipe(new FluidStack(FarmersRespiteCompat.CATNIP_TEA_SOURCE.get(), 1000), new FluidStack(FarmersRespiteCompat.LONG_CATNIP_TEA_SOURCE.get(), 1000), 2400, 0.35F).addIngredient(ForgeTags.MILK).build(consumer);
        SCKettlePouringRecipeBuilder.kettlePouringRecipe(Items.GLASS_BOTTLE, FarmersRespiteCompat.CATNIP_TEA_SOURCE.get(), 250, FarmersRespiteCompat.CATNIP_TEA.get(), consumer);
        SCKettlePouringRecipeBuilder.kettlePouringRecipe(Items.GLASS_BOTTLE, FarmersRespiteCompat.LONG_CATNIP_TEA_SOURCE.get(), 250, FarmersRespiteCompat.LONG_CATNIP_TEA.get(), consumer);
    }

    public static class SCKettleRecipeBuilder {
        private final List<Ingredient> ingredients = Lists.newArrayList();
        private final int brewingTime;
        private final float experience;
        private final FluidStack fluidIn;
        private final FluidStack fluidOut;

        public SCKettleRecipeBuilder(FluidStack fluidIn, FluidStack fluidOut, int brewingTime, float experience) {
            this.fluidIn = fluidIn;
            this.fluidOut = fluidOut;
            this.brewingTime = brewingTime;
            this.experience = experience;
        }

        public static SCKettleRecipeBuilder kettleRecipe(FluidStack fluidIn, FluidStack fluidOut, int cookingTime, float experience) {
            return new SCKettleRecipeBuilder(fluidIn, fluidOut, cookingTime, experience);
        }

        public SCKettleRecipeBuilder addIngredient(TagKey<Item> itemTagKey) {
            ingredients.add(Ingredient.of(itemTagKey));
            return this;
        }

        public SCKettleRecipeBuilder addIngredient(ItemLike itemLike) {
            ingredients.add(Ingredient.of(itemLike));
            return this;
        }

        public void build(Consumer<FinishedRecipe> consumer) {
            ResourceLocation baseFluidLocation = ForgeRegistries.FLUIDS.getKey(fluidIn.getFluid());
            ResourceLocation resultFluidLocation = ForgeRegistries.FLUIDS.getKey(fluidOut.getFluid());
            ResourceLocation id = new ResourceLocation(SimplyCats.MOD_ID + ":brewing/" + resultFluidLocation.getPath() + "_from_" + baseFluidLocation.getPath());
            consumer.accept(new KettleRecipeBuilder.Result(id, fluidIn, fluidOut, ingredients, brewingTime, experience));
        }
    }

    public static class SCKettlePouringRecipeBuilder {
        private final ItemStack container;
        private final Fluid fluid;
        private final int amount;
        private final ItemStack output;

        public SCKettlePouringRecipeBuilder(ItemStack container, Fluid fluid, int amount, ItemStack output) {
            this.container = container;
            this.fluid = fluid;
            this.amount = amount;
            this.output = output;
        }

        public static void kettlePouringRecipe(ItemLike container, Fluid fluid, int amount, ItemLike output, Consumer<FinishedRecipe> consumer) {
            new SCKettlePouringRecipeBuilder(container.asItem().getDefaultInstance(), fluid, amount, output.asItem().getDefaultInstance()).build(consumer);
        }

        public void build(Consumer<FinishedRecipe> consumer) {
            ResourceLocation id = new ResourceLocation(SimplyCats.MOD_ID + ":pouring/" + ForgeRegistries.ITEMS.getKey(output.getItem()).getPath());
            consumer.accept(new KettlePouringRecipeBuilder.Result(id, container, fluid, amount, output));
            if (ModList.get().isLoaded("create")) {
                new ProcessingRecipeBuilder<>(FillingRecipe::new, new ResourceLocation(SimplyCats.MOD_ID, id.getPath().replace("pouring/", ""))).require(fluid, amount).require(container.getItem()).output(output).build(consumer);
                new ProcessingRecipeBuilder<>(EmptyingRecipe::new, new ResourceLocation(SimplyCats.MOD_ID, id.getPath().replace("pouring/", ""))).require(output.getItem()).output(fluid, amount).output(container).build(consumer);
            }
        }
    }

}
