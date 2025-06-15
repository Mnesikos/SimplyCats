package com.github.mnesikos.simplycats.data;

import com.github.mnesikos.simplycats.SimplyCats;
import com.github.mnesikos.simplycats.block.SCBlocks;
import com.github.mnesikos.simplycats.item.SCItems;
import com.github.mnesikos.simplycats.worldgen.villages.SCVillagers;
import net.minecraft.advancements.Advancement;
import net.minecraft.advancements.FrameType;
import net.minecraft.advancements.RequirementsStrategy;
import net.minecraft.advancements.critereon.*;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.minecraftforge.common.data.ForgeAdvancementProvider;

import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.function.Consumer;

public class SCAdvancementProvider extends ForgeAdvancementProvider {
    public SCAdvancementProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> registries, ExistingFileHelper existingFileHelper) {
        super(output, registries, existingFileHelper, List.of(new SCAdvancementGenerator()));
    }

    public static class SCAdvancementGenerator implements ForgeAdvancementProvider.AdvancementGenerator {
        @Override
        public void generate(HolderLookup.Provider registries, Consumer<Advancement> saver, ExistingFileHelper existingFileHelper) {
            Advancement root = Advancement.Builder.advancement()
                    .display(SCItems.PET_CARRIER.get(), Component.translatable("advancements.simplycats.root"), Component.translatable("advancements.simplycats.root.desc"), new ResourceLocation("textures/gui/advancements/backgrounds/husbandry.png"), FrameType.TASK, true, false, false)
                    .addCriterion("catnip", InventoryChangeTrigger.TriggerInstance.hasItems(SCItems.CATNIP_SEEDS.get()))
                    .save(saver, SimplyCats.MOD_ID + ":root");

            Advancement checklist = addChecklistCriterion(Advancement.Builder.advancement()).parent(root)
                    .display(SCItems.TREAT_BAG.get(), Component.translatable("advancements.simplycats.checklist"), Component.translatable("advancements.simplycats.checklist.desc"), null, FrameType.GOAL, true, true, false)
                    .save(saver, SimplyCats.MOD_ID + ":checklist");
            Advancement adoption = Advancement.Builder.advancement().parent(checklist)
                    .display(SCItems.ADOPT_CERTIFICATE.get(), Component.translatable("advancements.simplycats.adoption"), Component.translatable("advancements.simplycats.adoption.desc"), null, FrameType.TASK, true, true, false)
                    .addCriterion("adoption", TameAnimalTrigger.TriggerInstance.tamedAnimal(EntityPredicate.Builder.entity().of(SimplyCats.CAT.get()).build()))
                    .save(saver, SimplyCats.MOD_ID + ":adoption");

            Advancement catnip = Advancement.Builder.advancement().parent(checklist)
                    .display(SCItems.CATNIP.get(), Component.translatable("advancements.simplycats.catnip"), Component.translatable("advancements.simplycats.catnip.desc"), null, FrameType.TASK, true, true, false)
                    .addCriterion("catnip", InventoryChangeTrigger.TriggerInstance.hasItems(SCItems.CATNIP.get()))
                    .save(saver, SimplyCats.MOD_ID + ":catnip");
            Advancement catalogue = Advancement.Builder.advancement().parent(catnip)
                    .display(SCItems.CAT_BOOK.get(), Component.translatable("advancements.simplycats.catalogue"), Component.translatable("advancements.simplycats.catalogue.desc"), null, FrameType.TASK, true, true, false)
                    .addCriterion("catalogue", InventoryChangeTrigger.TriggerInstance.hasItems(SCItems.CAT_BOOK.get()))
                    .save(saver, SimplyCats.MOD_ID + ":catalogue");
            // todo tea

            Advancement speuter = Advancement.Builder.advancement().parent(root)
                    .display(SCItems.STERILIZE_POTION.get(), Component.translatable("advancements.simplycats.speuter"), Component.translatable("advancements.simplycats.speuter.desc"), null, FrameType.TASK, true, true, false)
                    .addCriterion("sterilize_potion", InventoryChangeTrigger.TriggerInstance.hasItems(SCItems.STERILIZE_POTION.get()))
                    .save(saver, SimplyCats.MOD_ID + ":speuter");

            String phaeomelanin = "Phaeomelanin";
            Advancement spay = addCatTagPhaeomelaninVariants(Advancement.Builder.advancement(),
                    List.of(
                            createTagWithGenes(Map.of(phaeomelanin, "Xo-Xo")),
                            createTagWithGenes(Map.of(phaeomelanin, "Xo-XO")),
                            createTagWithGenes(Map.of(phaeomelanin, "XO-Xo")),
                            createTagWithGenes(Map.of(phaeomelanin, "XO-XO"))
                    )).parent(speuter)
                    .display(SCItems.STERILIZE_POTION.get(), Component.translatable("advancements.simplycats.spay"), Component.translatable("advancements.simplycats.spay.desc"), null, FrameType.GOAL, true, true, false)
                    .requirements(RequirementsStrategy.OR)
                    .save(saver, SimplyCats.MOD_ID + ":spay");

            Advancement neuter = addCatTagPhaeomelaninVariants(Advancement.Builder.advancement(),
                    List.of(
                            createTagWithGenes(Map.of(phaeomelanin, "Xo-Y")),
                            createTagWithGenes(Map.of(phaeomelanin, "XO-Y"))
                    )).parent(speuter)
                    .display(SCItems.STERILIZE_POTION.get(), Component.translatable("advancements.simplycats.neuter"), Component.translatable("advancements.simplycats.neuter.desc"), null, FrameType.GOAL, true, true, false)
                    .requirements(RequirementsStrategy.OR)
                    .save(saver, SimplyCats.MOD_ID + ":neuter");

            Advancement tnr = addManyCatTagVariants(Advancement.Builder.advancement(),
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
                    .display(SCItems.STERILIZE_POTION.get(), Component.translatable("advancements.simplycats.tnr"), Component.translatable("advancements.simplycats.tnr.desc"), null, FrameType.CHALLENGE, true, true, true)
                    .requirements(RequirementsStrategy.AND)
                    .save(saver, SimplyCats.MOD_ID + ":tnr");
        }

        private static Advancement.Builder addChecklistCriterion(Advancement.Builder builder) {
            Advancement.Builder bowlsBuilder = Advancement.Builder.advancement().requirements(RequirementsStrategy.OR);
            SCBlocks.CAT_BOWLS.forEach(((dyeColor, blockRegistryObject) -> bowlsBuilder.addCriterion(dyeColor.getName() + "_bowl", InventoryChangeTrigger.TriggerInstance.hasItems(blockRegistryObject.get()))));
            String[] bowls = bowlsBuilder.getCriteria().keySet().toArray(String[]::new);

            Advancement.Builder litterBoxesBuilder = Advancement.Builder.advancement().requirements(RequirementsStrategy.OR);
            SCBlocks.LITTER_BOXES.forEach(((dyeColor, blockRegistryObject) -> litterBoxesBuilder.addCriterion(dyeColor.getName() + "_litter_box", InventoryChangeTrigger.TriggerInstance.hasItems(blockRegistryObject.get()))));
            String[] litterBoxes = litterBoxesBuilder.getCriteria().keySet().toArray(String[]::new);

            Advancement.Builder treeBedsBuilder = Advancement.Builder.advancement().requirements(RequirementsStrategy.OR);
            SCBlocks.CAT_TREE_BEDS.forEach(((dyeColor, blockRegistryObject) -> treeBedsBuilder.addCriterion(dyeColor.getName() + "_tree_bed", InventoryChangeTrigger.TriggerInstance.hasItems(blockRegistryObject.get()))));
            String[] treeBeds = treeBedsBuilder.getCriteria().keySet().toArray(String[]::new);

            Advancement.Builder treePostsBuilder = Advancement.Builder.advancement().requirements(RequirementsStrategy.OR);
            SCBlocks.CAT_TREE_POSTS.forEach(((dyeColor, blockRegistryObject) -> treePostsBuilder.addCriterion(dyeColor.getName() + "_tree_post", InventoryChangeTrigger.TriggerInstance.hasItems(blockRegistryObject.get()))));
            String[] treePosts = treePostsBuilder.getCriteria().keySet().toArray(String[]::new);

            Advancement.Builder treeBoxesBuilder = Advancement.Builder.advancement().requirements(RequirementsStrategy.OR);
            SCBlocks.CAT_TREE_BOXES.forEach(((dyeColor, blockRegistryObject) -> treeBoxesBuilder.addCriterion(dyeColor.getName() + "_tree_box", InventoryChangeTrigger.TriggerInstance.hasItems(blockRegistryObject.get()))));
            String[] treeBoxes = treeBoxesBuilder.getCriteria().keySet().toArray(String[]::new);

            Advancement.Builder postsBuilder = Advancement.Builder.advancement().requirements(RequirementsStrategy.OR);
            SCBlocks.SCRATCHING_POSTS.forEach((woodType, blockRegistryObject) -> postsBuilder.addCriterion(woodType + "_post", InventoryChangeTrigger.TriggerInstance.hasItems(blockRegistryObject.get())));
            String[] posts = postsBuilder.getCriteria().keySet().toArray(String[]::new);

            bowlsBuilder.getCriteria().forEach((builder::addCriterion));
            litterBoxesBuilder.getCriteria().forEach((builder::addCriterion));
            treeBedsBuilder.getCriteria().forEach((builder::addCriterion));
            treePostsBuilder.getCriteria().forEach((builder::addCriterion));
            treeBoxesBuilder.getCriteria().forEach((builder::addCriterion));
            postsBuilder.getCriteria().forEach((builder::addCriterion));
            builder.addCriterion("treat_bag", InventoryChangeTrigger.TriggerInstance.hasItems(SCItems.TREAT_BAG.get()))
                    .addCriterion("laser_pointer", InventoryChangeTrigger.TriggerInstance.hasItems(SCItems.LASER_POINTER.get()));
            builder.requirements(new String[][]{bowls, litterBoxes, treeBeds, treePosts, treeBoxes, posts, {"treat_bag"}, {"laser_pointer"}});
            return builder;
        }

        private static Advancement.Builder addCatTagPhaeomelaninVariants(Advancement.Builder builder, List<CompoundTag> tags) {
            tags.forEach(tag ->
                    builder.addCriterion(tag.getString("Phaeomelanin"), PlayerInteractTrigger.TriggerInstance.itemUsedOnEntity(ItemPredicate.Builder.item().of(SCItems.STERILIZE_POTION.get()),
                            EntityPredicate.wrap(EntityPredicate.Builder.entity().of(SimplyCats.CAT.get()).nbt(new NbtPredicate(tag)).build()))));
            return builder;
        }

        private static Advancement.Builder addManyCatTagVariants(Advancement.Builder builder, List<CompoundTag> tags) {
            tags.forEach(tag ->
                    builder.addCriterion(tag.getString("Eumelanin") + tag.getString("Phaeomelanin") + tag.getString("Dilution") + tag.getString("Agouti") + tag.getString("White"), PlayerInteractTrigger.TriggerInstance.itemUsedOnEntity(ItemPredicate.Builder.item().of(SCItems.STERILIZE_POTION.get()),
                            EntityPredicate.wrap(EntityPredicate.Builder.entity().of(SimplyCats.CAT.get()).nbt(new NbtPredicate(tag)).build()))));
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
