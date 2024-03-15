package com.github.mnesikos.simplycats.worldgen.villages;

import com.github.mnesikos.simplycats.SimplyCats;
import com.mojang.datafixers.util.Pair;
import net.minecraft.core.Holder;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.levelgen.structure.pools.SinglePoolElement;
import net.minecraft.world.level.levelgen.structure.pools.StructurePoolElement;
import net.minecraft.world.level.levelgen.structure.pools.StructureTemplatePool;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureProcessorList;
import net.minecraftforge.event.server.ServerAboutToStartEvent;

import java.util.ArrayList;
import java.util.List;

public class SCWorldGen {
    private static final ResourceKey<StructureProcessorList> EMPTY_PROCESSOR_LIST_KEY = ResourceKey.create(Registry.PROCESSOR_LIST_REGISTRY, new ResourceLocation("minecraft", "empty"));

    public static void setupVillageWorldGen(final ServerAboutToStartEvent event) {
        Registry<StructureTemplatePool> templatePoolRegistry = event.getServer().registryAccess().registry(Registry.TEMPLATE_POOL_REGISTRY).orElseThrow();
        Registry<StructureProcessorList> processorListRegistry = event.getServer().registryAccess().registry(Registry.PROCESSOR_LIST_REGISTRY).orElseThrow();

        addStructureToVillage(templatePoolRegistry, processorListRegistry, new ResourceLocation("minecraft:village/desert/houses"), SimplyCats.MOD_ID + ":village/desert_shelter_1", 6);
        addStructureToVillage(templatePoolRegistry, processorListRegistry, new ResourceLocation("minecraft:village/plains/houses"), SimplyCats.MOD_ID + ":village/plains_shelter_1", 6);
        addStructureToVillage(templatePoolRegistry, processorListRegistry, new ResourceLocation("minecraft:village/savanna/houses"), SimplyCats.MOD_ID + ":village/savanna_shelter_1", 6);
        addStructureToVillage(templatePoolRegistry, processorListRegistry, new ResourceLocation("minecraft:village/snowy/houses"), SimplyCats.MOD_ID + ":village/snowy_shelter_1", 6);
        addStructureToVillage(templatePoolRegistry, processorListRegistry, new ResourceLocation("minecraft:village/taiga/houses"), SimplyCats.MOD_ID + ":village/taiga_shelter_1", 6);
    }

    private static void addStructureToVillage(Registry<StructureTemplatePool> templatePoolRegistry, Registry<StructureProcessorList> processorListRegistry, ResourceLocation poolRL, String nbtPieceRL, int weight) {
        Holder<StructureProcessorList> emptyProcessorList = processorListRegistry.getHolderOrThrow(EMPTY_PROCESSOR_LIST_KEY);

        StructureTemplatePool pool = templatePoolRegistry.get(poolRL);
        if (pool == null) return;

        SinglePoolElement piece = SinglePoolElement.legacy(nbtPieceRL, emptyProcessorList).apply(StructureTemplatePool.Projection.RIGID);

        for (int i = 0; i < weight; i++) {
            pool.templates.add(piece);
        }

        List<Pair<StructurePoolElement, Integer>> listOfPieceEntries = new ArrayList<>(pool.rawTemplates);
        listOfPieceEntries.add(new Pair<>(piece, weight));
        pool.rawTemplates = listOfPieceEntries;
    }
}
