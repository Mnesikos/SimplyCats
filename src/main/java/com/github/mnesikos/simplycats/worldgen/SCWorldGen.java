package com.github.mnesikos.simplycats.worldgen;

import com.github.mnesikos.simplycats.SimplyCats;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.registry.DynamicRegistries;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.gen.feature.jigsaw.JigsawPattern;
import net.minecraft.world.gen.feature.jigsaw.JigsawPiece;
import net.minecraft.world.gen.feature.jigsaw.LegacySingleJigsawPiece;
import net.minecraftforge.fml.common.Mod;

import java.util.ArrayList;
import java.util.List;

@Mod.EventBusSubscriber(modid = SimplyCats.MOD_ID)
public class SCWorldGen {
    private static final ResourceLocation desertShelterStructure = new ResourceLocation(SimplyCats.MOD_ID, "village/desert_shelter_1");
    private static final ResourceLocation plainsShelterStructure = new ResourceLocation(SimplyCats.MOD_ID, "village/plains_shelter_1");
    private static final ResourceLocation savannaShelterStructure = new ResourceLocation(SimplyCats.MOD_ID, "village/savanna_shelter_1");
    private static final ResourceLocation snowyShelterStructure = new ResourceLocation(SimplyCats.MOD_ID, "village/snowy_shelter_1");
    private static final ResourceLocation taigaShelterStructure = new ResourceLocation(SimplyCats.MOD_ID, "village/taiga_shelter_1");

    public static void setupVillageWorldGen(DynamicRegistries dynamicRegistries) {
        addStructureToVillage(dynamicRegistries, "village/desert/houses", desertShelterStructure, 6);
        addStructureToVillage(dynamicRegistries, "village/plains/houses", plainsShelterStructure, 6);
        addStructureToVillage(dynamicRegistries, "village/savanna/houses", savannaShelterStructure, 6);
        addStructureToVillage(dynamicRegistries, "village/snowy/houses", snowyShelterStructure, 6);
        addStructureToVillage(dynamicRegistries, "village/taiga/houses", taigaShelterStructure, 6);
    }

    private static void addStructureToVillage(DynamicRegistries dynamicRegistries, String villagePool, ResourceLocation structureLocation, int weight) {
        LegacySingleJigsawPiece piece = JigsawPiece.legacy(structureLocation.toString()).apply(JigsawPattern.PlacementBehaviour.RIGID);
        JigsawPattern pool = dynamicRegistries.registryOrThrow(Registry.TEMPLATE_POOL_REGISTRY).getOptional(new ResourceLocation(villagePool)).orElse(null);

        if (pool != null) {
            List<JigsawPiece> piecesList = new ArrayList<>(pool.templates);
            for (int i = 0; i < weight; i++) {
                piecesList.add(piece);
            }
            pool.templates = piecesList;
        }
    }
}
