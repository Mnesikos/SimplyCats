package com.github.mnesikos.simplycats.data;

import com.github.mnesikos.simplycats.SimplyCats;
import com.github.mnesikos.simplycats.block.SCBlocks;
import com.github.mnesikos.simplycats.item.SCItems;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.data.recipes.*;

import java.util.concurrent.CompletableFuture;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.properties.WoodType;

import java.util.function.Consumer;

public class SCRecipeProvider extends RecipeProvider {
    public SCRecipeProvider(PackOutput packOutput, CompletableFuture<HolderLookup.Provider> registries) {
        super(packOutput, registries);
    }

    @Override
    protected void buildRecipes(RecipeOutput consumer) {
        ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, SCItems.CAT_BOOK.get())
                .requires(Items.BOOK)
                .requires(SCItems.CATNIP.get())
                .group("cat_book")
                .unlockedBy("has_catnip", has(SCItems.CATNIP.get())).unlockedBy("has_book", has(Items.BOOK)).save(consumer);
        ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, SCItems.CAT_BOOK.get())
                .requires(SCItems.CAT_BOOK.get())
                .group("cat_book")
                .unlockedBy("has_cat_book", has(SCItems.CAT_BOOK.get())).save(consumer, modSaveLoc("cat_book_reset"));

        ShapelessRecipeBuilder.shapeless(RecipeCategory.DECORATIONS, SCBlocks.SHELTER_BOOK.get())
                .requires(SCItems.CAT_BOOK.get())
                .requires(SCItems.CATNIP.get())
                .unlockedBy("has_cat_book", has(SCItems.CAT_BOOK.get())).save(consumer);
        ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, SCItems.STERILIZE_POTION.get())
                .requires(Items.GLASS_BOTTLE)
                .requires(SCItems.CATNIP.get())
                .requires(Items.SPIDER_EYE)
                .unlockedBy("has_catnip", has(SCItems.CATNIP.get())).save(consumer);
        ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, SCItems.TREAT_BAG.get())
                .requires(Items.LEATHER)
                .requires(Items.IRON_INGOT)
                .unlockedBy("has_leather", has(Items.LEATHER)).save(consumer);

        ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, SCItems.ADOPT_CERTIFICATE.get())
                .requires(Items.PAPER)
                .requires(Items.BLACK_DYE)
                .group("adopt_certificate")
                .unlockedBy("has_black_dye", has(Items.BLACK_DYE)).unlockedBy("has_paper", has(Items.PAPER)).save(consumer);
        ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, SCItems.RELEASE_CERTIFICATE.get())
                .requires(SCItems.ADOPT_CERTIFICATE.get())
                .unlockedBy("has_adopt_certificate", has(SCItems.ADOPT_CERTIFICATE.get())).save(consumer);
        ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, SCItems.ADOPT_CERTIFICATE.get())
                .requires(SCItems.RELEASE_CERTIFICATE.get())
                .group("adopt_certificate")
                .unlockedBy("has_release_certificate", has(SCItems.RELEASE_CERTIFICATE.get())).save(consumer, modSaveLoc("adopt_certificate_from_release_certificate"));

        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, SCItems.LASER_POINTER.get())
                .pattern(" B")
                .pattern("RI")
                .define('B', Items.STONE_BUTTON)
                .define('R', Items.REDSTONE_TORCH)
                .define('I', Items.IRON_INGOT)
                .unlockedBy("has_redstone", has(Items.REDSTONE)).save(consumer);

        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, Items.NAME_TAG)
                .pattern("S ")
                .pattern(" I")
                .define('S', Items.STRING)
                .define('I', Ingredient.of(Items.IRON_INGOT, Items.GOLD_INGOT))
                .unlockedBy("has_string", has(Items.STRING)).save(consumer, modSaveLoc("name_tag"));

        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, SCItems.PET_CARRIER.get())
                .pattern("ISI")
                .pattern("SBS")
                .pattern("ISI")
                .define('I', Items.IRON_INGOT)
                .define('B', Items.IRON_BARS)
                .define('S', Items.TERRACOTTA)
                .unlockedBy("has_iron", has(Items.IRON_INGOT)).save(consumer);

        catTreePieces(consumer, SCBlocks.CAT_TREE_BEDS.get(DyeColor.WHITE).get(), SCBlocks.CAT_TREE_BOXES.get(DyeColor.WHITE).get(), SCBlocks.CAT_TREE_POSTS.get(DyeColor.WHITE).get(), Blocks.WHITE_CARPET);
        catTreePieces(consumer, SCBlocks.CAT_TREE_BEDS.get(DyeColor.ORANGE).get(), SCBlocks.CAT_TREE_BOXES.get(DyeColor.ORANGE).get(), SCBlocks.CAT_TREE_POSTS.get(DyeColor.ORANGE).get(), Blocks.ORANGE_CARPET);
        catTreePieces(consumer, SCBlocks.CAT_TREE_BEDS.get(DyeColor.MAGENTA).get(), SCBlocks.CAT_TREE_BOXES.get(DyeColor.MAGENTA).get(), SCBlocks.CAT_TREE_POSTS.get(DyeColor.MAGENTA).get(), Blocks.MAGENTA_CARPET);
        catTreePieces(consumer, SCBlocks.CAT_TREE_BEDS.get(DyeColor.LIGHT_BLUE).get(), SCBlocks.CAT_TREE_BOXES.get(DyeColor.LIGHT_BLUE).get(), SCBlocks.CAT_TREE_POSTS.get(DyeColor.LIGHT_BLUE).get(), Blocks.LIGHT_BLUE_CARPET);
        catTreePieces(consumer, SCBlocks.CAT_TREE_BEDS.get(DyeColor.YELLOW).get(), SCBlocks.CAT_TREE_BOXES.get(DyeColor.YELLOW).get(), SCBlocks.CAT_TREE_POSTS.get(DyeColor.YELLOW).get(), Blocks.YELLOW_CARPET);
        catTreePieces(consumer, SCBlocks.CAT_TREE_BEDS.get(DyeColor.LIME).get(), SCBlocks.CAT_TREE_BOXES.get(DyeColor.LIME).get(), SCBlocks.CAT_TREE_POSTS.get(DyeColor.LIME).get(), Blocks.LIME_CARPET);
        catTreePieces(consumer, SCBlocks.CAT_TREE_BEDS.get(DyeColor.PINK).get(), SCBlocks.CAT_TREE_BOXES.get(DyeColor.PINK).get(), SCBlocks.CAT_TREE_POSTS.get(DyeColor.PINK).get(), Blocks.PINK_CARPET);
        catTreePieces(consumer, SCBlocks.CAT_TREE_BEDS.get(DyeColor.GRAY).get(), SCBlocks.CAT_TREE_BOXES.get(DyeColor.GRAY).get(), SCBlocks.CAT_TREE_POSTS.get(DyeColor.GRAY).get(), Blocks.GRAY_CARPET);
        catTreePieces(consumer, SCBlocks.CAT_TREE_BEDS.get(DyeColor.LIGHT_GRAY).get(), SCBlocks.CAT_TREE_BOXES.get(DyeColor.LIGHT_GRAY).get(), SCBlocks.CAT_TREE_POSTS.get(DyeColor.LIGHT_GRAY).get(), Blocks.LIGHT_GRAY_CARPET);
        catTreePieces(consumer, SCBlocks.CAT_TREE_BEDS.get(DyeColor.CYAN).get(), SCBlocks.CAT_TREE_BOXES.get(DyeColor.CYAN).get(), SCBlocks.CAT_TREE_POSTS.get(DyeColor.CYAN).get(), Blocks.CYAN_CARPET);
        catTreePieces(consumer, SCBlocks.CAT_TREE_BEDS.get(DyeColor.PURPLE).get(), SCBlocks.CAT_TREE_BOXES.get(DyeColor.PURPLE).get(), SCBlocks.CAT_TREE_POSTS.get(DyeColor.PURPLE).get(), Blocks.PURPLE_CARPET);
        catTreePieces(consumer, SCBlocks.CAT_TREE_BEDS.get(DyeColor.BLUE).get(), SCBlocks.CAT_TREE_BOXES.get(DyeColor.BLUE).get(), SCBlocks.CAT_TREE_POSTS.get(DyeColor.BLUE).get(), Blocks.BLUE_CARPET);
        catTreePieces(consumer, SCBlocks.CAT_TREE_BEDS.get(DyeColor.BROWN).get(), SCBlocks.CAT_TREE_BOXES.get(DyeColor.BROWN).get(), SCBlocks.CAT_TREE_POSTS.get(DyeColor.BROWN).get(), Blocks.BROWN_CARPET);
        catTreePieces(consumer, SCBlocks.CAT_TREE_BEDS.get(DyeColor.GREEN).get(), SCBlocks.CAT_TREE_BOXES.get(DyeColor.GREEN).get(), SCBlocks.CAT_TREE_POSTS.get(DyeColor.GREEN).get(), Blocks.GREEN_CARPET);
        catTreePieces(consumer, SCBlocks.CAT_TREE_BEDS.get(DyeColor.RED).get(), SCBlocks.CAT_TREE_BOXES.get(DyeColor.RED).get(), SCBlocks.CAT_TREE_POSTS.get(DyeColor.RED).get(), Blocks.RED_CARPET);
        catTreePieces(consumer, SCBlocks.CAT_TREE_BEDS.get(DyeColor.BLACK).get(), SCBlocks.CAT_TREE_BOXES.get(DyeColor.BLACK).get(), SCBlocks.CAT_TREE_POSTS.get(DyeColor.BLACK).get(), Blocks.BLACK_CARPET);

        catBowl(consumer, SCBlocks.CAT_BOWLS.get(DyeColor.WHITE).get(), Blocks.WHITE_TERRACOTTA);
        catBowl(consumer, SCBlocks.CAT_BOWLS.get(DyeColor.ORANGE).get(), Blocks.ORANGE_TERRACOTTA);
        catBowl(consumer, SCBlocks.CAT_BOWLS.get(DyeColor.MAGENTA).get(), Blocks.MAGENTA_TERRACOTTA);
        catBowl(consumer, SCBlocks.CAT_BOWLS.get(DyeColor.LIGHT_BLUE).get(), Blocks.LIGHT_BLUE_TERRACOTTA);
        catBowl(consumer, SCBlocks.CAT_BOWLS.get(DyeColor.YELLOW).get(), Blocks.YELLOW_TERRACOTTA);
        catBowl(consumer, SCBlocks.CAT_BOWLS.get(DyeColor.LIME).get(), Blocks.LIME_TERRACOTTA);
        catBowl(consumer, SCBlocks.CAT_BOWLS.get(DyeColor.PINK).get(), Blocks.PINK_TERRACOTTA);
        catBowl(consumer, SCBlocks.CAT_BOWLS.get(DyeColor.GRAY).get(), Blocks.GRAY_TERRACOTTA);
        catBowl(consumer, SCBlocks.CAT_BOWLS.get(DyeColor.LIGHT_GRAY).get(), Blocks.LIGHT_GRAY_TERRACOTTA);
        catBowl(consumer, SCBlocks.CAT_BOWLS.get(DyeColor.CYAN).get(), Blocks.CYAN_TERRACOTTA);
        catBowl(consumer, SCBlocks.CAT_BOWLS.get(DyeColor.PURPLE).get(), Blocks.PURPLE_TERRACOTTA);
        catBowl(consumer, SCBlocks.CAT_BOWLS.get(DyeColor.BLUE).get(), Blocks.BLUE_TERRACOTTA);
        catBowl(consumer, SCBlocks.CAT_BOWLS.get(DyeColor.BROWN).get(), Blocks.BROWN_TERRACOTTA);
        catBowl(consumer, SCBlocks.CAT_BOWLS.get(DyeColor.GREEN).get(), Blocks.GREEN_TERRACOTTA);
        catBowl(consumer, SCBlocks.CAT_BOWLS.get(DyeColor.RED).get(), Blocks.RED_TERRACOTTA);
        catBowl(consumer, SCBlocks.CAT_BOWLS.get(DyeColor.BLACK).get(), Blocks.BLACK_TERRACOTTA);

        litterBox(consumer, SCBlocks.LITTER_BOXES.get(DyeColor.WHITE).get(), Blocks.WHITE_TERRACOTTA);
        litterBox(consumer, SCBlocks.LITTER_BOXES.get(DyeColor.ORANGE).get(), Blocks.ORANGE_TERRACOTTA);
        litterBox(consumer, SCBlocks.LITTER_BOXES.get(DyeColor.MAGENTA).get(), Blocks.MAGENTA_TERRACOTTA);
        litterBox(consumer, SCBlocks.LITTER_BOXES.get(DyeColor.LIGHT_BLUE).get(), Blocks.LIGHT_BLUE_TERRACOTTA);
        litterBox(consumer, SCBlocks.LITTER_BOXES.get(DyeColor.YELLOW).get(), Blocks.YELLOW_TERRACOTTA);
        litterBox(consumer, SCBlocks.LITTER_BOXES.get(DyeColor.LIME).get(), Blocks.LIME_TERRACOTTA);
        litterBox(consumer, SCBlocks.LITTER_BOXES.get(DyeColor.PINK).get(), Blocks.PINK_TERRACOTTA);
        litterBox(consumer, SCBlocks.LITTER_BOXES.get(DyeColor.GRAY).get(), Blocks.GRAY_TERRACOTTA);
        litterBox(consumer, SCBlocks.LITTER_BOXES.get(DyeColor.LIGHT_GRAY).get(), Blocks.LIGHT_GRAY_TERRACOTTA);
        litterBox(consumer, SCBlocks.LITTER_BOXES.get(DyeColor.CYAN).get(), Blocks.CYAN_TERRACOTTA);
        litterBox(consumer, SCBlocks.LITTER_BOXES.get(DyeColor.PURPLE).get(), Blocks.PURPLE_TERRACOTTA);
        litterBox(consumer, SCBlocks.LITTER_BOXES.get(DyeColor.BLUE).get(), Blocks.BLUE_TERRACOTTA);
        litterBox(consumer, SCBlocks.LITTER_BOXES.get(DyeColor.BROWN).get(), Blocks.BROWN_TERRACOTTA);
        litterBox(consumer, SCBlocks.LITTER_BOXES.get(DyeColor.GREEN).get(), Blocks.GREEN_TERRACOTTA);
        litterBox(consumer, SCBlocks.LITTER_BOXES.get(DyeColor.RED).get(), Blocks.RED_TERRACOTTA);
        litterBox(consumer, SCBlocks.LITTER_BOXES.get(DyeColor.BLACK).get(), Blocks.BLACK_TERRACOTTA);

        scratchingPost(consumer, SCBlocks.SCRATCHING_POSTS.get(WoodType.OAK.name()).get(), Blocks.OAK_PLANKS);
        scratchingPost(consumer, SCBlocks.SCRATCHING_POSTS.get(WoodType.SPRUCE.name()).get(), Blocks.SPRUCE_PLANKS);
        scratchingPost(consumer, SCBlocks.SCRATCHING_POSTS.get(WoodType.BIRCH.name()).get(), Blocks.BIRCH_PLANKS);
        scratchingPost(consumer, SCBlocks.SCRATCHING_POSTS.get(WoodType.ACACIA.name()).get(), Blocks.ACACIA_PLANKS);
//        scratchingPost(consumer, SCBlocks.SCRATCHING_POSTS.get(WoodType.CHERRY.name()).get(), Blocks.CHERRY_PLANKS);
        scratchingPost(consumer, SCBlocks.SCRATCHING_POSTS.get(WoodType.JUNGLE.name()).get(), Blocks.JUNGLE_PLANKS);
        scratchingPost(consumer, SCBlocks.SCRATCHING_POSTS.get(WoodType.DARK_OAK.name()).get(), Blocks.DARK_OAK_PLANKS);
        scratchingPost(consumer, SCBlocks.SCRATCHING_POSTS.get(WoodType.CRIMSON.name()).get(), Blocks.CRIMSON_PLANKS);
        scratchingPost(consumer, SCBlocks.SCRATCHING_POSTS.get(WoodType.WARPED.name()).get(), Blocks.WARPED_PLANKS);
//        scratchingPost(consumer, SCBlocks.SCRATCHING_POSTS.get(WoodType.MANGROVE.name()).get(), Blocks.MANGROVE_PLANKS);
//        scratchingPost(consumer, SCBlocks.SCRATCHING_POSTS.get(WoodType.BAMBOO.name()).get(), Blocks.BAMBOO_PLANKS);

        windowPerch(consumer, SCBlocks.WINDOW_PERCHES.get(WoodType.OAK.name()).get(), Blocks.OAK_PLANKS);
        windowPerch(consumer, SCBlocks.WINDOW_PERCHES.get(WoodType.SPRUCE.name()).get(), Blocks.SPRUCE_PLANKS);
        windowPerch(consumer, SCBlocks.WINDOW_PERCHES.get(WoodType.BIRCH.name()).get(), Blocks.BIRCH_PLANKS);
        windowPerch(consumer, SCBlocks.WINDOW_PERCHES.get(WoodType.ACACIA.name()).get(), Blocks.ACACIA_PLANKS);
//        windowPerch(consumer, SCBlocks.WINDOW_PERCHES.get(WoodType.CHERRY.name()).get(), Blocks.CHERRY_PLANKS);
        windowPerch(consumer, SCBlocks.WINDOW_PERCHES.get(WoodType.JUNGLE.name()).get(), Blocks.JUNGLE_PLANKS);
        windowPerch(consumer, SCBlocks.WINDOW_PERCHES.get(WoodType.DARK_OAK.name()).get(), Blocks.DARK_OAK_PLANKS);
        windowPerch(consumer, SCBlocks.WINDOW_PERCHES.get(WoodType.CRIMSON.name()).get(), Blocks.CRIMSON_PLANKS);
        windowPerch(consumer, SCBlocks.WINDOW_PERCHES.get(WoodType.WARPED.name()).get(), Blocks.WARPED_PLANKS);
//        windowPerch(consumer, SCBlocks.WINDOW_PERCHES.get(WoodType.MANGROVE.name()).get(), Blocks.MANGROVE_PLANKS);
//        windowPerch(consumer, SCBlocks.WINDOW_PERCHES.get(WoodType.BAMBOO.name()).get(), Blocks.BAMBOO_PLANKS);
    }

    protected static String modSaveLoc(String name) {
        return SimplyCats.MOD_ID + ":" + name;
    }

    protected static void catBowl(RecipeOutput consumer, ItemLike bowl, ItemLike terracotta) {
        ShapedRecipeBuilder.shaped(RecipeCategory.DECORATIONS, bowl)
                .pattern("C C")
                .pattern(" C ")
                .define('C', terracotta)
                .group("cat_bowl")
                .unlockedBy(getHasName(terracotta), has(terracotta)).save(consumer);
    }

    protected static void litterBox(RecipeOutput consumer, ItemLike litterBox, ItemLike terracotta) {
        ShapedRecipeBuilder.shaped(RecipeCategory.DECORATIONS, litterBox)
                .pattern("CCC")
                .pattern("C C")
                .pattern("CCC")
                .define('C', terracotta)
                .group("litter_box")
                .unlockedBy(getHasName(terracotta), has(terracotta)).save(consumer);
    }

    protected static void scratchingPost(RecipeOutput consumer, ItemLike scratchingPost, ItemLike planks) {
        ShapedRecipeBuilder.shaped(RecipeCategory.DECORATIONS, scratchingPost)
                .pattern(" P ")
                .pattern("STS")
                .pattern("PPP")
                .define('P', planks)
                .define('S', Items.STRING)
                .define('T', Items.STICK)
                .group("scratching_post")
                .unlockedBy(getHasName(planks), has(planks)).save(consumer);
    }

    protected static void windowPerch(RecipeOutput consumer, ItemLike windowPerch, ItemLike planks) {
        ShapedRecipeBuilder.shaped(RecipeCategory.DECORATIONS, windowPerch, 4)
                .pattern("PPP")
                .pattern("T T")
                .define('P', planks)
                .define('T', Items.STICK)
                .group("window_perch")
                .unlockedBy(getHasName(planks), has(planks)).save(consumer);
    }

    protected static void catTreePieces(RecipeOutput consumer, ItemLike bed, ItemLike box, ItemLike post, ItemLike carpet) {
        ShapedRecipeBuilder.shaped(RecipeCategory.DECORATIONS, bed, 4)
                .pattern("C C")
                .pattern("CCC")
                .define('C', carpet)
                .group("cat_tree_bed")
                .unlockedBy(getHasName(carpet), has(carpet)).save(consumer);
        ShapedRecipeBuilder.shaped(RecipeCategory.DECORATIONS, box, 4)
                .pattern("CCC")
                .pattern("C C")
                .pattern("CCC")
                .define('C', carpet)
                .group("cat_tree_box")
                .unlockedBy(getHasName(carpet), has(carpet)).save(consumer);
        ShapedRecipeBuilder.shaped(RecipeCategory.DECORATIONS, post, 4)
                .pattern(" C ")
                .pattern("SWS")
                .pattern(" C ")
                .define('C', carpet)
                .define('S', Items.STRING)
                .define('W', Items.STICK)
                .group("cat_tree_post")
                .unlockedBy(getHasName(carpet), has(carpet)).save(consumer);
    }
}
