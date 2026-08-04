package com.github.mnesikos.simplycats.data;

import com.github.mnesikos.simplycats.SimplyCats;
import com.github.mnesikos.simplycats.block.CatnipBlock;
import com.github.mnesikos.simplycats.block.SCBlocks;
import com.github.mnesikos.simplycats.item.SCItems;
import net.minecraft.advancements.critereon.StatePropertiesPredicate;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.loot.packs.VanillaBlockLoot;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.properties.WoodType;
import net.minecraft.world.level.storage.loot.predicates.LootItemBlockStatePropertyCondition;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;
import net.minecraft.core.registries.BuiltInRegistries;

import java.util.Map;
import java.util.stream.Collectors;

public class SCBlockLoot extends VanillaBlockLoot {
    public SCBlockLoot(HolderLookup.Provider registries) {
        super(registries);
    }

    @Override
    protected void generate() {
        LootItemCondition.Builder catnipBuilder = LootItemBlockStatePropertyCondition.hasBlockStateProperties(SCBlocks.CATNIP_CROP.get()).setProperties(StatePropertiesPredicate.Builder.properties().hasProperty(CatnipBlock.AGE, 3));
        add(SCBlocks.CATNIP_CROP.get(), createCropDrops(SCBlocks.CATNIP_CROP.get(), SCItems.CATNIP.get(), SCItems.CATNIP_SEEDS.get(), catnipBuilder));
        dropPottedContents(SCBlocks.POTTED_CATNIP.get());
        dropSelf(SCBlocks.SHELTER_BOOK.get());
        dropSelf(SCBlocks.SCRATCHING_POSTS.get(WoodType.OAK.name()).get());
        dropSelf(SCBlocks.SCRATCHING_POSTS.get(WoodType.SPRUCE.name()).get());
        dropSelf(SCBlocks.SCRATCHING_POSTS.get(WoodType.BIRCH.name()).get());
        dropSelf(SCBlocks.SCRATCHING_POSTS.get(WoodType.ACACIA.name()).get());
//        dropSelf(SCBlocks.SCRATCHING_POSTS.get(WoodType.CHERRY.name()).get());
        dropSelf(SCBlocks.SCRATCHING_POSTS.get(WoodType.JUNGLE.name()).get());
        dropSelf(SCBlocks.SCRATCHING_POSTS.get(WoodType.DARK_OAK.name()).get());
        dropSelf(SCBlocks.SCRATCHING_POSTS.get(WoodType.CRIMSON.name()).get());
        dropSelf(SCBlocks.SCRATCHING_POSTS.get(WoodType.WARPED.name()).get());
//        dropSelf(SCBlocks.SCRATCHING_POSTS.get(WoodType.MANGROVE.name()).get());
//        dropSelf(SCBlocks.SCRATCHING_POSTS.get(WoodType.BAMBOO.name()).get());
        dropSelf(SCBlocks.WINDOW_PERCHES.get(WoodType.OAK.name()).get());
        dropSelf(SCBlocks.WINDOW_PERCHES.get(WoodType.SPRUCE.name()).get());
        dropSelf(SCBlocks.WINDOW_PERCHES.get(WoodType.BIRCH.name()).get());
        dropSelf(SCBlocks.WINDOW_PERCHES.get(WoodType.ACACIA.name()).get());
//        dropSelf(SCBlocks.WINDOW_PERCHES.get(WoodType.CHERRY.name()).get());
        dropSelf(SCBlocks.WINDOW_PERCHES.get(WoodType.JUNGLE.name()).get());
        dropSelf(SCBlocks.WINDOW_PERCHES.get(WoodType.DARK_OAK.name()).get());
        dropSelf(SCBlocks.WINDOW_PERCHES.get(WoodType.CRIMSON.name()).get());
        dropSelf(SCBlocks.WINDOW_PERCHES.get(WoodType.WARPED.name()).get());
//        dropSelf(SCBlocks.WINDOW_PERCHES.get(WoodType.MANGROVE.name()).get());
//        dropSelf(SCBlocks.WINDOW_PERCHES.get(WoodType.BAMBOO.name()).get());
        dropSelf(SCBlocks.CAT_BOWLS.get(DyeColor.WHITE).get());
        dropSelf(SCBlocks.CAT_BOWLS.get(DyeColor.ORANGE).get());
        dropSelf(SCBlocks.CAT_BOWLS.get(DyeColor.MAGENTA).get());
        dropSelf(SCBlocks.CAT_BOWLS.get(DyeColor.LIGHT_BLUE).get());
        dropSelf(SCBlocks.CAT_BOWLS.get(DyeColor.YELLOW).get());
        dropSelf(SCBlocks.CAT_BOWLS.get(DyeColor.LIME).get());
        dropSelf(SCBlocks.CAT_BOWLS.get(DyeColor.PINK).get());
        dropSelf(SCBlocks.CAT_BOWLS.get(DyeColor.GRAY).get());
        dropSelf(SCBlocks.CAT_BOWLS.get(DyeColor.LIGHT_GRAY).get());
        dropSelf(SCBlocks.CAT_BOWLS.get(DyeColor.CYAN).get());
        dropSelf(SCBlocks.CAT_BOWLS.get(DyeColor.PURPLE).get());
        dropSelf(SCBlocks.CAT_BOWLS.get(DyeColor.BLUE).get());
        dropSelf(SCBlocks.CAT_BOWLS.get(DyeColor.BROWN).get());
        dropSelf(SCBlocks.CAT_BOWLS.get(DyeColor.GREEN).get());
        dropSelf(SCBlocks.CAT_BOWLS.get(DyeColor.RED).get());
        dropSelf(SCBlocks.CAT_BOWLS.get(DyeColor.BLACK).get());
        dropSelf(SCBlocks.CAT_TREE_BEDS.get(DyeColor.WHITE).get());
        dropSelf(SCBlocks.CAT_TREE_BEDS.get(DyeColor.ORANGE).get());
        dropSelf(SCBlocks.CAT_TREE_BEDS.get(DyeColor.MAGENTA).get());
        dropSelf(SCBlocks.CAT_TREE_BEDS.get(DyeColor.LIGHT_BLUE).get());
        dropSelf(SCBlocks.CAT_TREE_BEDS.get(DyeColor.YELLOW).get());
        dropSelf(SCBlocks.CAT_TREE_BEDS.get(DyeColor.LIME).get());
        dropSelf(SCBlocks.CAT_TREE_BEDS.get(DyeColor.PINK).get());
        dropSelf(SCBlocks.CAT_TREE_BEDS.get(DyeColor.GRAY).get());
        dropSelf(SCBlocks.CAT_TREE_BEDS.get(DyeColor.LIGHT_GRAY).get());
        dropSelf(SCBlocks.CAT_TREE_BEDS.get(DyeColor.CYAN).get());
        dropSelf(SCBlocks.CAT_TREE_BEDS.get(DyeColor.PURPLE).get());
        dropSelf(SCBlocks.CAT_TREE_BEDS.get(DyeColor.BLUE).get());
        dropSelf(SCBlocks.CAT_TREE_BEDS.get(DyeColor.BROWN).get());
        dropSelf(SCBlocks.CAT_TREE_BEDS.get(DyeColor.GREEN).get());
        dropSelf(SCBlocks.CAT_TREE_BEDS.get(DyeColor.RED).get());
        dropSelf(SCBlocks.CAT_TREE_BEDS.get(DyeColor.BLACK).get());

        dropSelf(SCBlocks.CAT_TREE_BOXES.get(DyeColor.WHITE).get());
        dropSelf(SCBlocks.CAT_TREE_BOXES.get(DyeColor.ORANGE).get());
        dropSelf(SCBlocks.CAT_TREE_BOXES.get(DyeColor.MAGENTA).get());
        dropSelf(SCBlocks.CAT_TREE_BOXES.get(DyeColor.LIGHT_BLUE).get());
        dropSelf(SCBlocks.CAT_TREE_BOXES.get(DyeColor.YELLOW).get());
        dropSelf(SCBlocks.CAT_TREE_BOXES.get(DyeColor.LIME).get());
        dropSelf(SCBlocks.CAT_TREE_BOXES.get(DyeColor.PINK).get());
        dropSelf(SCBlocks.CAT_TREE_BOXES.get(DyeColor.GRAY).get());
        dropSelf(SCBlocks.CAT_TREE_BOXES.get(DyeColor.LIGHT_GRAY).get());
        dropSelf(SCBlocks.CAT_TREE_BOXES.get(DyeColor.CYAN).get());
        dropSelf(SCBlocks.CAT_TREE_BOXES.get(DyeColor.PURPLE).get());
        dropSelf(SCBlocks.CAT_TREE_BOXES.get(DyeColor.BLUE).get());
        dropSelf(SCBlocks.CAT_TREE_BOXES.get(DyeColor.BROWN).get());
        dropSelf(SCBlocks.CAT_TREE_BOXES.get(DyeColor.GREEN).get());
        dropSelf(SCBlocks.CAT_TREE_BOXES.get(DyeColor.RED).get());
        dropSelf(SCBlocks.CAT_TREE_BOXES.get(DyeColor.BLACK).get());

        dropSelf(SCBlocks.CAT_TREE_POSTS.get(DyeColor.WHITE).get());
        dropSelf(SCBlocks.CAT_TREE_POSTS.get(DyeColor.ORANGE).get());
        dropSelf(SCBlocks.CAT_TREE_POSTS.get(DyeColor.MAGENTA).get());
        dropSelf(SCBlocks.CAT_TREE_POSTS.get(DyeColor.LIGHT_BLUE).get());
        dropSelf(SCBlocks.CAT_TREE_POSTS.get(DyeColor.YELLOW).get());
        dropSelf(SCBlocks.CAT_TREE_POSTS.get(DyeColor.LIME).get());
        dropSelf(SCBlocks.CAT_TREE_POSTS.get(DyeColor.PINK).get());
        dropSelf(SCBlocks.CAT_TREE_POSTS.get(DyeColor.GRAY).get());
        dropSelf(SCBlocks.CAT_TREE_POSTS.get(DyeColor.LIGHT_GRAY).get());
        dropSelf(SCBlocks.CAT_TREE_POSTS.get(DyeColor.CYAN).get());
        dropSelf(SCBlocks.CAT_TREE_POSTS.get(DyeColor.PURPLE).get());
        dropSelf(SCBlocks.CAT_TREE_POSTS.get(DyeColor.BLUE).get());
        dropSelf(SCBlocks.CAT_TREE_POSTS.get(DyeColor.BROWN).get());
        dropSelf(SCBlocks.CAT_TREE_POSTS.get(DyeColor.GREEN).get());
        dropSelf(SCBlocks.CAT_TREE_POSTS.get(DyeColor.RED).get());
        dropSelf(SCBlocks.CAT_TREE_POSTS.get(DyeColor.BLACK).get());

        dropSelf(SCBlocks.LITTER_BOXES.get(DyeColor.WHITE).get());
        dropSelf(SCBlocks.LITTER_BOXES.get(DyeColor.ORANGE).get());
        dropSelf(SCBlocks.LITTER_BOXES.get(DyeColor.MAGENTA).get());
        dropSelf(SCBlocks.LITTER_BOXES.get(DyeColor.LIGHT_BLUE).get());
        dropSelf(SCBlocks.LITTER_BOXES.get(DyeColor.YELLOW).get());
        dropSelf(SCBlocks.LITTER_BOXES.get(DyeColor.LIME).get());
        dropSelf(SCBlocks.LITTER_BOXES.get(DyeColor.PINK).get());
        dropSelf(SCBlocks.LITTER_BOXES.get(DyeColor.GRAY).get());
        dropSelf(SCBlocks.LITTER_BOXES.get(DyeColor.LIGHT_GRAY).get());
        dropSelf(SCBlocks.LITTER_BOXES.get(DyeColor.CYAN).get());
        dropSelf(SCBlocks.LITTER_BOXES.get(DyeColor.PURPLE).get());
        dropSelf(SCBlocks.LITTER_BOXES.get(DyeColor.BLUE).get());
        dropSelf(SCBlocks.LITTER_BOXES.get(DyeColor.BROWN).get());
        dropSelf(SCBlocks.LITTER_BOXES.get(DyeColor.GREEN).get());
        dropSelf(SCBlocks.LITTER_BOXES.get(DyeColor.RED).get());
        dropSelf(SCBlocks.LITTER_BOXES.get(DyeColor.BLACK).get());
    }

    @Override
    protected Iterable<Block> getKnownBlocks() {
        return BuiltInRegistries.BLOCK.entrySet().stream()
                .filter(e -> e.getKey().location().getNamespace().equals(SimplyCats.MOD_ID))
                .map(Map.Entry::getValue)
                .collect(Collectors.toList());
    }
}
