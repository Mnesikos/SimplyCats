package com.github.mnesikos.simplycats.worldgen.villages;

import net.minecraft.util.EnumFacing;
import net.minecraft.world.gen.structure.StructureComponent;
import net.minecraft.world.gen.structure.StructureVillagePieces;
import net.minecraftforge.fml.common.registry.VillagerRegistry;

import java.util.List;
import java.util.Random;

public class VillagePetShelterHandler implements VillagerRegistry.IVillageCreationHandler {
    @Override
    public StructureVillagePieces.PieceWeight getVillagePieceWeight (Random random, int i) {
        return new StructureVillagePieces.PieceWeight(ComponentPetShelter.class, 15, 1 + (i > 2 ? random.nextInt(2) : 0));
    }

    @Override
    public Class<?> getComponentClass() {
        return ComponentPetShelter.class;
    }

    @Override
    public StructureVillagePieces.Village buildComponent(StructureVillagePieces.PieceWeight villagePiece, StructureVillagePieces.Start startPiece, List<StructureComponent> pieces, Random random, int p1, int p2, int p3, EnumFacing facing, int p5) {
        return ComponentPetShelter.buildComponent(startPiece, pieces, random, p1, p2, p3, facing, p5);
    }
}