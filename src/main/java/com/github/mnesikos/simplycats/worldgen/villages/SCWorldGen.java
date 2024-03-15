package com.github.mnesikos.simplycats.worldgen.villages;

import com.github.mnesikos.simplycats.SimplyCats;
import net.minecraft.core.Registry;
import net.minecraft.core.RegistryAccess;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.levelgen.structure.pools.LegacySinglePoolElement;
import net.minecraft.world.level.levelgen.structure.pools.StructurePoolElement;
import net.minecraft.world.level.levelgen.structure.pools.StructureTemplatePool;
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

    public static void setupVillageWorldGen(RegistryAccess dynamicRegistries) {
        addStructureToVillage(dynamicRegistries, "village/desert/houses", desertShelterStructure, 6);
        addStructureToVillage(dynamicRegistries, "village/plains/houses", plainsShelterStructure, 6);
        addStructureToVillage(dynamicRegistries, "village/savanna/houses", savannaShelterStructure, 6);
        addStructureToVillage(dynamicRegistries, "village/snowy/houses", snowyShelterStructure, 6);
        addStructureToVillage(dynamicRegistries, "village/taiga/houses", taigaShelterStructure, 6);
    }

    private static void addStructureToVillage(RegistryAccess dynamicRegistries, String villagePool, ResourceLocation structureLocation, int weight) {
        LegacySinglePoolElement piece = StructurePoolElement.legacy(structureLocation.toString()).apply(StructureTemplatePool.Projection.RIGID);
        StructureTemplatePool pool = dynamicRegistries.registryOrThrow(Registry.TEMPLATE_POOL_REGISTRY).getOptional(new ResourceLocation(villagePool)).orElse(null);

        if (pool != null) {
            List<StructurePoolElement> piecesList = new ArrayList<>(pool.templates);
            for (int i = 0; i < weight; i++) {
                piecesList.add(piece);
            }
            pool.templates = piecesList;
        }
    }
}
