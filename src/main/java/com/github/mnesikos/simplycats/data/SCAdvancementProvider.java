package com.github.mnesikos.simplycats.data;

import com.github.mnesikos.simplycats.SimplyCats;
import com.github.mnesikos.simplycats.item.SCItems;
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
            Advancement adoption = Advancement.Builder.advancement()
                    .display(SCItems.PET_CARRIER.get(), Component.translatable("advancements.simplycats.adoption"), Component.translatable("advancements.simplycats.adoption.desc"), new ResourceLocation("textures/gui/advancements/backgrounds/husbandry.png"), FrameType.TASK, true, true, false)
                    .addCriterion("adoption", TameAnimalTrigger.TriggerInstance.tamedAnimal(EntityPredicate.Builder.entity().of(SimplyCats.CAT.get()).build()))
                    .save(saver, SimplyCats.MOD_ID + ":adoption");

            Advancement catnip = Advancement.Builder.advancement().parent(adoption)
                    .display(SCItems.CATNIP.get(), Component.translatable("advancements.simplycats.catnip"), Component.translatable("advancements.simplycats.catnip.desc"), null, FrameType.TASK, true, true, false)
                    .addCriterion("catnip", InventoryChangeTrigger.TriggerInstance.hasItems(SCItems.CATNIP_SEEDS.get()))
                    .save(saver, SimplyCats.MOD_ID + ":catnip");
            Advancement catalogue = Advancement.Builder.advancement().parent(catnip)
                    .display(SCItems.CAT_BOOK.get(), Component.translatable("advancements.simplycats.catalogue"), Component.translatable("advancements.simplycats.catalogue.desc"), null, FrameType.TASK, true, true, false)
                    .addCriterion("catalogue", InventoryChangeTrigger.TriggerInstance.hasItems(SCItems.CAT_BOOK.get()))
                    .save(saver, SimplyCats.MOD_ID + ":catalogue");
            // todo tea

            Advancement speuter = Advancement.Builder.advancement().parent(adoption)
                    .display(SCItems.STERILIZE_POTION.get(), Component.translatable("advancements.simplycats.speuter"), Component.translatable("advancements.simplycats.speuter.desc"), null, FrameType.TASK, true, true, false)
                    .addCriterion("sterilize_potion", InventoryChangeTrigger.TriggerInstance.hasItems(SCItems.STERILIZE_POTION.get()))
                    .save(saver, SimplyCats.MOD_ID + ":speuter");

            String phaeomelanin = "Phaeomelanin";
            Advancement spay = addCatTagVariants(Advancement.Builder.advancement(), phaeomelanin,
                    List.of(
                            createTagWithGenes(Map.of(phaeomelanin, "Xo-Xo")),
                            createTagWithGenes(Map.of(phaeomelanin, "Xo-XO")),
                            createTagWithGenes(Map.of(phaeomelanin, "XO-Xo")),
                            createTagWithGenes(Map.of(phaeomelanin, "XO-XO"))
                    )).parent(speuter)
                    .display(SCItems.STERILIZE_POTION.get(), Component.translatable("advancements.simplycats.spay"), Component.translatable("advancements.simplycats.spay.desc"), null, FrameType.GOAL, true, true, false)
                    .requirements(RequirementsStrategy.OR)
                    .save(saver, SimplyCats.MOD_ID + ":spay");

            Advancement neuter = addCatTagVariants(Advancement.Builder.advancement(), phaeomelanin,
                    List.of(
                            createTagWithGenes(Map.of(phaeomelanin, "Xo-Y")),
                            createTagWithGenes(Map.of(phaeomelanin, "XO-Y"))
                    )).parent(speuter)
                    .display(SCItems.STERILIZE_POTION.get(), Component.translatable("advancements.simplycats.neuter"), Component.translatable("advancements.simplycats.neuter.desc"), null, FrameType.GOAL, true, true, false)
                    .requirements(RequirementsStrategy.OR)
                    .save(saver, SimplyCats.MOD_ID + ":neuter");

            Advancement tnr = Advancement.Builder.advancement().parent(speuter)
                    .display(SCItems.STERILIZE_POTION.get(), Component.translatable("advancements.simplycats.tnr"), Component.translatable("advancements.simplycats.tnr.desc"), null, FrameType.CHALLENGE, true, true, true)
                    .addCriterion("impossible", new ImpossibleTrigger.TriggerInstance()) //todo
                    .save(saver, SimplyCats.MOD_ID + ":tnr");
        }

        private static Advancement.Builder addCatTagVariants(Advancement.Builder builder, String stringTagKey, List<CompoundTag> tags) {
            tags.forEach(tag ->
                    builder.addCriterion(tag.getString(stringTagKey), PlayerInteractTrigger.TriggerInstance.itemUsedOnEntity(ItemPredicate.Builder.item().of(SCItems.STERILIZE_POTION.get()),
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
