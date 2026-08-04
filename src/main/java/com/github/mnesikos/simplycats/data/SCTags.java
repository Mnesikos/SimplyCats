package com.github.mnesikos.simplycats.data;

import com.github.mnesikos.simplycats.SimplyCats;
import com.github.mnesikos.simplycats.block.SCBlocks;
import com.github.mnesikos.simplycats.item.SCItems;
import com.github.mnesikos.simplycats.worldgen.villages.SCVillagers;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.data.tags.ItemTagsProvider;
import net.minecraft.data.tags.PoiTypeTagsProvider;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.ItemTags;
import net.minecraft.tags.PoiTypeTags;
import net.neoforged.neoforge.common.data.BlockTagsProvider;
import net.neoforged.neoforge.common.data.ExistingFileHelper;
import org.jetbrains.annotations.Nullable;

import java.util.concurrent.CompletableFuture;

public class SCTags {
    public static class SCBlockTags extends BlockTagsProvider {
        public SCBlockTags(PackOutput output, CompletableFuture<HolderLookup.Provider> lookupProvider, @Nullable ExistingFileHelper existingFileHelper) {
            super(output, lookupProvider, SimplyCats.MOD_ID, existingFileHelper);
        }

        @Override
        protected void addTags(HolderLookup.Provider provider) {
            tag(BlockTags.FLOWER_POTS).add(SCBlocks.POTTED_CATNIP.get());
            tag(BlockTags.CROPS).add(SCBlocks.CATNIP_CROP.get());
            tag(BlockTags.MAINTAINS_FARMLAND).add(SCBlocks.CATNIP_CROP.get());
            tag(BlockTags.create(new ResourceLocation("sereneseasons", "spring_crops"))).add(SCBlocks.CATNIP_CROP.get());
            tag(BlockTags.create(new ResourceLocation("sereneseasons", "autumn_crops"))).add(SCBlocks.CATNIP_CROP.get());
        }
    }

    public static class SCItemTags extends ItemTagsProvider {
        public SCItemTags(PackOutput packOutput, CompletableFuture<HolderLookup.Provider> lookupProvider, BlockTagsProvider tagsProvider, @Nullable ExistingFileHelper existingFileHelper) {
            super(packOutput, lookupProvider, tagsProvider.contentsGetter(), SimplyCats.MOD_ID, existingFileHelper);
        }

        @Override
        protected void addTags(HolderLookup.Provider provider) {
            tag(ItemTags.create(new ResourceLocation("sereneseasons", "spring_crops"))).add(SCItems.CATNIP_SEEDS.get());
            tag(ItemTags.create(new ResourceLocation("sereneseasons", "autumn_crops"))).add(SCItems.CATNIP_SEEDS.get());
        }
    }

    public static class SCPoiTypeTags extends PoiTypeTagsProvider {
        public SCPoiTypeTags(PackOutput packOutput, CompletableFuture<HolderLookup.Provider> lookupProvider, @Nullable ExistingFileHelper existingFileHelper) {
            super(packOutput, lookupProvider, SimplyCats.MOD_ID, existingFileHelper);
        }

        @Override
        protected void addTags(HolderLookup.Provider provider) {
            tag(PoiTypeTags.ACQUIRABLE_JOB_SITE).add(SCVillagers.ADOPTION_BOOK.getKey());
        }
    }
}
