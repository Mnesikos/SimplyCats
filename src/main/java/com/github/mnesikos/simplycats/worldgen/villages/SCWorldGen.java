package com.github.mnesikos.simplycats.worldgen.villages;

import com.github.mnesikos.simplycats.SimplyCats;
import com.mojang.datafixers.util.Pair;
import net.minecraft.core.Holder;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.level.levelgen.structure.pools.SinglePoolElement;
import net.minecraft.world.level.levelgen.structure.pools.StructurePoolElement;
import net.minecraft.world.level.levelgen.structure.pools.StructureTemplatePool;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureProcessorList;
import net.neoforged.neoforge.event.server.ServerAboutToStartEvent;

import java.util.ArrayList;
import java.util.List;

public class SCWorldGen {
    private static final ResourceLocation desertShelterStructure = ResourceLocation.fromNamespaceAndPath(SimplyCats.MOD_ID, "village/desert_shelter_1");
    private static final ResourceLocation plainsShelterStructure = ResourceLocation.fromNamespaceAndPath(SimplyCats.MOD_ID, "village/plains_shelter_1");
    private static final ResourceLocation savannaShelterStructure = ResourceLocation.fromNamespaceAndPath(SimplyCats.MOD_ID, "village/savanna_shelter_1");
    private static final ResourceLocation snowyShelterStructure = ResourceLocation.fromNamespaceAndPath(SimplyCats.MOD_ID, "village/snowy_shelter_1");
    private static final ResourceLocation taigaShelterStructure = ResourceLocation.fromNamespaceAndPath(SimplyCats.MOD_ID, "village/taiga_shelter_1");

    public static void setupVillageWorldGen(final ServerAboutToStartEvent event) {
        MinecraftServer server = event.getServer();

        Registry<StructureTemplatePool> templatePoolRegistry = server.registryAccess().registryOrThrow(Registries.TEMPLATE_POOL);

        addStructureToVillage(templatePoolRegistry.get(ResourceLocation.parse("village/desert/houses")), SinglePoolElement.single(desertShelterStructure.toString()).apply(StructureTemplatePool.Projection.RIGID), 6);
        addStructureToVillage(templatePoolRegistry.get(ResourceLocation.parse("village/plains/houses")), SinglePoolElement.single(plainsShelterStructure.toString()).apply(StructureTemplatePool.Projection.RIGID), 6);
        addStructureToVillage(templatePoolRegistry.get(ResourceLocation.parse("village/savanna/houses")), SinglePoolElement.single(savannaShelterStructure.toString()).apply(StructureTemplatePool.Projection.RIGID), 6);
        addStructureToVillage(templatePoolRegistry.get(ResourceLocation.parse("village/snowy/houses")), SinglePoolElement.single(snowyShelterStructure.toString()).apply(StructureTemplatePool.Projection.RIGID), 6);
        addStructureToVillage(templatePoolRegistry.get(ResourceLocation.parse("village/taiga/houses")), SinglePoolElement.single(taigaShelterStructure.toString()).apply(StructureTemplatePool.Projection.RIGID), 6);
    }

    private static <T extends StructurePoolElement> void addStructureToVillage(StructureTemplatePool pool, T piece, int weight) {
        if (pool == null) return;

        for (int i = 0; i < weight; i++) {
            pool.templates.add(piece);
        }

        List<Pair<StructurePoolElement, Integer>> listOfPieceEntries = new ArrayList<>(pool.rawTemplates);
        listOfPieceEntries.add(Pair.of(piece, weight));
        pool.rawTemplates = listOfPieceEntries;
    }
}
