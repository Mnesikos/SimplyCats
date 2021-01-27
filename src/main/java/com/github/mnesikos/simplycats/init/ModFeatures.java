/*
package com.github.mnesikos.simplycats.init;

import com.github.mnesikos.simplycats.SimplyCats;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.gen.feature.Feature;
import net.minecraft.world.gen.feature.NoFeatureConfig;
import net.minecraft.world.gen.feature.structure.IStructurePieceType;
import net.minecraft.world.gen.feature.structure.Structure;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.registries.IForgeRegistry;

import java.util.Locale;

public class ModFeatures {
    public static Structure<NoFeatureConfig> PET_SHELTER = new PetShelterStructure(NoFeatureConfig::deserialize);
    public static IStructurePieceType PET_SHELTER_PIECE;

    @SuppressWarnings("unchecked")
    public static void registerFeatures(RegistryEvent.Register<Feature<?>> event)
    {
        IForgeRegistry<Feature<?>> registry = event.getRegistry();

        PET_SHELTER = (Structure<NoFeatureConfig>) SimplyCats.register(registry, PET_SHELTER, "pet_shelter");
        PET_SHELTER_PIECE = register(PetShelterPieces.Piece::new, "pet_shelter_piece");
    }

    static IStructurePieceType register(IStructurePieceType p_214750_0_, String key) {
        return Registry.register(Registry.STRUCTURE_PIECE, key.toLowerCase(Locale.ROOT), p_214750_0_);
    }
}
*/
