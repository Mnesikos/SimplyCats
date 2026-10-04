package com.github.mnesikos.simplycats.data;

import com.github.mnesikos.simplycats.SimplyCats;
import com.github.mnesikos.simplycats.block.SCBlocks;
import com.github.mnesikos.simplycats.item.SCItems;
import net.minecraft.advancements.Advancement;
import net.minecraft.advancements.AdvancementHolder;
import net.minecraft.advancements.AdvancementRequirements;
import net.minecraft.advancements.AdvancementType;
import net.minecraft.advancements.critereon.*;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.neoforge.common.data.AdvancementProvider;
import net.neoforged.neoforge.common.data.ExistingFileHelper;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.function.Consumer;

public class SCAdvancementProvider extends AdvancementProvider {
    public SCAdvancementProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> registries, ExistingFileHelper existingFileHelper) {
        super(output, registries, existingFileHelper, List.of(new SCAdvancementGenerator()));
    }

    public static class SCAdvancementGenerator implements AdvancementGenerator {
        @Override
        public void generate(HolderLookup.Provider registries, Consumer<AdvancementHolder> saver, ExistingFileHelper existingFileHelper) {
            AdvancementHolder root = Advancement.Builder.advancement()
                    .display(SCItems.PET_CARRIER.get(), Component.translatable("advancements.simplycats.root"), Component.translatable("advancements.simplycats.root.desc"), ResourceLocation.fromNamespaceAndPath(SimplyCats.MOD_ID, "textures/gui/advancements/backgrounds/simply_cats.png"), AdvancementType.TASK, true, false, false)
                    .addCriterion("catnip", InventoryChangeTrigger.TriggerInstance.hasItems(SCItems.CATNIP_SEEDS.get()))
                    .save(saver, SimplyCats.MOD_ID + ":root");

            AdvancementHolder checklist = addChecklistCriterion(Advancement.Builder.advancement()).parent(root)
                    .display(SCItems.TREAT_BAG.get(), Component.translatable("advancements.simplycats.checklist"), Component.translatable("advancements.simplycats.checklist.desc"), null, AdvancementType.GOAL, true, true, false)
                    .save(saver, SimplyCats.MOD_ID + ":checklist");
            AdvancementHolder adoption = Advancement.Builder.advancement().parent(checklist)
                    .display(SCItems.ADOPT_CERTIFICATE.get(), Component.translatable("advancements.simplycats.adoption"), Component.translatable("advancements.simplycats.adoption.desc"), null, AdvancementType.TASK, true, true, false)
                    .addCriterion("adoption", TameAnimalTrigger.TriggerInstance.tamedAnimal(EntityPredicate.Builder.entity().of(SimplyCats.CAT.get())))
                    .save(saver, SimplyCats.MOD_ID + ":adoption");

            AdvancementHolder catnip = Advancement.Builder.advancement().parent(checklist)
                    .display(SCItems.CATNIP.get(), Component.translatable("advancements.simplycats.catnip"), Component.translatable("advancements.simplycats.catnip.desc"), null, AdvancementType.TASK, true, true, false)
                    .addCriterion("catnip", InventoryChangeTrigger.TriggerInstance.hasItems(SCItems.CATNIP.get()))
                    .save(saver, SimplyCats.MOD_ID + ":catnip");
            AdvancementHolder catalogue = Advancement.Builder.advancement().parent(catnip)
                    .display(SCItems.CAT_BOOK.get(), Component.translatable("advancements.simplycats.catalogue"), Component.translatable("advancements.simplycats.catalogue.desc"), null, AdvancementType.TASK, true, true, false)
                    .addCriterion("catalogue", InventoryChangeTrigger.TriggerInstance.hasItems(SCItems.CAT_BOOK.get()))
                    .save(saver, SimplyCats.MOD_ID + ":catalogue");
            // todo tea

            AdvancementHolder speuter = Advancement.Builder.advancement().parent(root)
                    .display(SCItems.STERILIZE_POTION.get(), Component.translatable("advancements.simplycats.speuter"), Component.translatable("advancements.simplycats.speuter.desc"), null, AdvancementType.TASK, true, true, false)
                    .addCriterion("sterilize_potion", InventoryChangeTrigger.TriggerInstance.hasItems(SCItems.STERILIZE_POTION.get()))
                    .save(saver, SimplyCats.MOD_ID + ":speuter");

            String phaeomelanin = "Phaeomelanin";
            AdvancementHolder spay = addCatTagPhaeomelaninVariants(Advancement.Builder.advancement(),
                    List.of(
                            createTagWithGenes(Map.of(phaeomelanin, "Xo-Xo")),
                            createTagWithGenes(Map.of(phaeomelanin, "Xo-XO")),
                            createTagWithGenes(Map.of(phaeomelanin, "XO-Xo")),
                            createTagWithGenes(Map.of(phaeomelanin, "XO-XO"))
                    )).parent(speuter)
                    .display(SCItems.STERILIZE_POTION.get(), Component.translatable("advancements.simplycats.spay"), Component.translatable("advancements.simplycats.spay.desc"), null, AdvancementType.GOAL, true, true, false)
                    .requirements(AdvancementRequirements.Strategy.OR)
                    .save(saver, SimplyCats.MOD_ID + ":spay");

            AdvancementHolder neuter = addCatTagPhaeomelaninVariants(Advancement.Builder.advancement(),
                    List.of(
                            createTagWithGenes(Map.of(phaeomelanin, "Xo-Y")),
                            createTagWithGenes(Map.of(phaeomelanin, "XO-Y"))
                    )).parent(speuter)
                    .display(SCItems.STERILIZE_POTION.get(), Component.translatable("advancements.simplycats.neuter"), Component.translatable("advancements.simplycats.neuter.desc"), null, AdvancementType.GOAL, true, true, false)
                    .requirements(AdvancementRequirements.Strategy.OR)
                    .save(saver, SimplyCats.MOD_ID + ":neuter");

            AdvancementHolder tnr = addManyCatTagVariants(Advancement.Builder.advancement(),
                    List.of(
                            createTagWithGenes(Map.of("Eumelanin", "B-B", phaeomelanin, "Xo-Xo", "Dilution", "D-D", "Agouti", "a-a")),
                            createTagWithGenes(Map.of("Eumelanin", "B-B", phaeomelanin, "Xo-Y", "Dilution", "D-D", "Agouti", "a-a")),
                            createTagWithGenes(Map.of("Eumelanin", "B-B", phaeomelanin, "Xo-Xo", "Dilution", "D-D", "Agouti", "A-A")),
                            createTagWithGenes(Map.of("Eumelanin", "B-B", phaeomelanin, "Xo-Y", "Dilution", "D-D", "Agouti", "A-A")),
                            createTagWithGenes(Map.of("Eumelanin", "b-b", phaeomelanin, "Xo-Xo", "Dilution", "D-D")),
                            createTagWithGenes(Map.of("Eumelanin", "b-b", phaeomelanin, "Xo-Y", "Dilution", "D-D")),
                            createTagWithGenes(Map.of("Eumelanin", "b1-b1", phaeomelanin, "Xo-Xo", "Dilution", "D-D")),
                            createTagWithGenes(Map.of("Eumelanin", "b1-b1", phaeomelanin, "Xo-Y", "Dilution", "D-D")),
                            createTagWithGenes(Map.of(phaeomelanin, "XO-Y", "Dilution", "D-D")),
                            createTagWithGenes(Map.of(phaeomelanin, "XO-XO", "Dilution", "D-D")),
                            createTagWithGenes(Map.of(phaeomelanin, "XO-Xo")),
                            createTagWithGenes(Map.of("Eumelanin", "B-B", phaeomelanin, "Xo-Xo", "Dilution", "d-d", "Agouti", "a-a")),
                            createTagWithGenes(Map.of("Eumelanin", "B-B", phaeomelanin, "Xo-Y", "Dilution", "d-d", "Agouti", "a-a")),
                            createTagWithGenes(Map.of("Eumelanin", "B-B", phaeomelanin, "Xo-Xo", "Dilution", "d-d", "Agouti", "A-A")),
                            createTagWithGenes(Map.of("Eumelanin", "B-B", phaeomelanin, "Xo-Y", "Dilution", "d-d", "Agouti", "A-A")),
                            createTagWithGenes(Map.of("Eumelanin", "b-b", phaeomelanin, "Xo-Xo", "Dilution", "d-d")),
                            createTagWithGenes(Map.of("Eumelanin", "b-b", phaeomelanin, "Xo-Y", "Dilution", "d-d")),
                            createTagWithGenes(Map.of("Eumelanin", "b1-b1", phaeomelanin, "Xo-Xo", "Dilution", "d-d")),
                            createTagWithGenes(Map.of("Eumelanin", "b1-b1", phaeomelanin, "Xo-Y", "Dilution", "d-d")),
                            createTagWithGenes(Map.of(phaeomelanin, "XO-Y", "Dilution", "d-d")),
                            createTagWithGenes(Map.of(phaeomelanin, "XO-XO", "Dilution", "d-d")),
                            createTagWithGenes(Map.of("White", "Wd-w"))
                    )).parent(speuter)
                    .display(SCItems.STERILIZE_POTION.get(), Component.translatable("advancements.simplycats.tnr"), Component.translatable("advancements.simplycats.tnr.desc"), null, AdvancementType.CHALLENGE, true, true, true)
                    .requirements(AdvancementRequirements.Strategy.AND)
                    .save(saver, SimplyCats.MOD_ID + ":tnr");
        }

        private static Advancement.Builder addChecklistCriterion(Advancement.Builder builder) {
            Advancement.Builder bowlsBuilder = Advancement.Builder.advancement().requirements(AdvancementRequirements.Strategy.OR);
            ArrayList<String> bowls = new ArrayList<>();
            SCBlocks.CAT_BOWLS.forEach(((dyeColor, blockRegistryObject) -> {
                String criterionKey = dyeColor.getName() + "_bowl";
                bowlsBuilder.addCriterion(criterionKey, InventoryChangeTrigger.TriggerInstance.hasItems(blockRegistryObject.get()));
                bowls.add(criterionKey);
            }));

            Advancement.Builder litterBoxesBuilder = Advancement.Builder.advancement().requirements(AdvancementRequirements.Strategy.OR);
            ArrayList<String> litterBoxes = new ArrayList<>();
            SCBlocks.LITTER_BOXES.forEach(((dyeColor, blockRegistryObject) -> {
                String criterionKey = dyeColor.getName() + "_litter_box";
                litterBoxesBuilder.addCriterion(criterionKey, InventoryChangeTrigger.TriggerInstance.hasItems(blockRegistryObject.get()));
                litterBoxes.add(criterionKey);
            }));

            Advancement.Builder treeBedsBuilder = Advancement.Builder.advancement().requirements(AdvancementRequirements.Strategy.OR);
            ArrayList<String> treeBeds = new ArrayList<>();
            SCBlocks.CAT_TREE_BEDS.forEach(((dyeColor, blockRegistryObject) -> {
                String criterionKey = dyeColor.getName() + "_tree_bed";
                treeBedsBuilder.addCriterion(criterionKey, InventoryChangeTrigger.TriggerInstance.hasItems(blockRegistryObject.get()));
                treeBeds.add(criterionKey);
            }));

            Advancement.Builder treePostsBuilder = Advancement.Builder.advancement().requirements(AdvancementRequirements.Strategy.OR);
            ArrayList<String> treePosts = new ArrayList<>();
            SCBlocks.CAT_TREE_POSTS.forEach(((dyeColor, blockRegistryObject) -> {
                String criterionKey = dyeColor.getName() + "_tree_post";
                treePostsBuilder.addCriterion(criterionKey, InventoryChangeTrigger.TriggerInstance.hasItems(blockRegistryObject.get()));
                treePosts.add(criterionKey);
            }));

            Advancement.Builder treeBoxesBuilder = Advancement.Builder.advancement().requirements(AdvancementRequirements.Strategy.OR);
            ArrayList<String> treeBoxes = new ArrayList<>();
            SCBlocks.CAT_TREE_BOXES.forEach(((dyeColor, blockRegistryObject) -> {
                String criterionKey = dyeColor.getName() + "_tree_box";
                treeBoxesBuilder.addCriterion(criterionKey, InventoryChangeTrigger.TriggerInstance.hasItems(blockRegistryObject.get()));
                treeBoxes.add(criterionKey);
            }));

            Advancement.Builder postsBuilder = Advancement.Builder.advancement().requirements(AdvancementRequirements.Strategy.OR);
            ArrayList<String> posts = new ArrayList<>();
            SCBlocks.SCRATCHING_POSTS.forEach(((woodType, blockRegistryObject) -> {
                String criterionKey = woodType + "_post";
                postsBuilder.addCriterion(criterionKey, InventoryChangeTrigger.TriggerInstance.hasItems(blockRegistryObject.get()));
                posts.add(criterionKey);
            }));

//            bowlsBuilder.getCriteria().forEach((builder::addCriterion));
//            litterBoxesBuilder.getCriteria().forEach((builder::addCriterion));
//            treeBedsBuilder.getCriteria().forEach((builder::addCriterion));
//            treePostsBuilder.getCriteria().forEach((builder::addCriterion));
//            treeBoxesBuilder.getCriteria().forEach((builder::addCriterion));
//            postsBuilder.getCriteria().forEach((builder::addCriterion));
            builder.addCriterion("treat_bag", InventoryChangeTrigger.TriggerInstance.hasItems(SCItems.TREAT_BAG.get()))
                    .addCriterion("laser_pointer", InventoryChangeTrigger.TriggerInstance.hasItems(SCItems.LASER_POINTER.get()));
            builder.requirements(new AdvancementRequirements(List.of(bowls, litterBoxes, treeBeds, treePosts, treeBoxes, posts, List.of("treat_bag"), List.of("laser_pointer"))));
            return builder;
        }

        private static Advancement.Builder addCatTagPhaeomelaninVariants(Advancement.Builder builder, List<CompoundTag> tags) {
            tags.forEach(tag ->
                    builder.addCriterion(tag.getString("Phaeomelanin"), PlayerInteractTrigger.TriggerInstance.itemUsedOnEntity(ItemPredicate.Builder.item().of(SCItems.STERILIZE_POTION.get()),
                            EntityPredicate.wrap(Optional.of(EntityPredicate.Builder.entity().of(SimplyCats.CAT.get()).nbt(new NbtPredicate(tag)).build())))));
            return builder;
        }

        private static Advancement.Builder addManyCatTagVariants(Advancement.Builder builder, List<CompoundTag> tags) {
            tags.forEach(tag ->
                    builder.addCriterion(tag.getString("Eumelanin") + tag.getString("Phaeomelanin") + tag.getString("Dilution") + tag.getString("Agouti") + tag.getString("White"), PlayerInteractTrigger.TriggerInstance.itemUsedOnEntity(ItemPredicate.Builder.item().of(SCItems.STERILIZE_POTION.get()),
                            EntityPredicate.wrap(Optional.of(EntityPredicate.Builder.entity().of(SimplyCats.CAT.get()).nbt(new NbtPredicate(tag)).build())))));
            return builder;
        }

        private static CompoundTag createTagWithGenes(Map<String, String> genesMap) {
            CompoundTag tag = new CompoundTag();
            for (var entry : genesMap.entrySet()) {
                tag.putString(entry.getKey(), entry.getValue());
            }
            return tag;
        }
    }
}
