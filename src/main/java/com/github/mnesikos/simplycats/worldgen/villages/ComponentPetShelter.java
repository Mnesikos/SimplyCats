package com.github.mnesikos.simplycats.worldgen.villages;

import java.util.List;
import java.util.Random;

import com.github.mnesikos.simplycats.configuration.SimplyCatsConfig;
import com.github.mnesikos.simplycats.entity.EntityCat;
import net.minecraft.block.Block;
import net.minecraft.entity.passive.EntityWolf;
import net.minecraft.init.Blocks;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;
import net.minecraft.world.gen.structure.StructureBoundingBox;
import net.minecraft.world.gen.structure.StructureComponent;
import net.minecraft.world.gen.structure.StructureVillagePieces;
import net.minecraft.world.gen.structure.StructureVillagePieces.Start;

public class ComponentPetShelter extends StructureVillagePieces.Village {
    private int averageGroundLevel = -1;
    private int CATS_SPAWNED;
    private int DOGS_SPAWNED;
	
	public ComponentPetShelter(StructureVillagePieces.Start parStart, int parType, Random parRand, StructureBoundingBox parStructBB, int parFacing) {
		super();
		this.coordBaseMode = parFacing;
		this.boundingBox = parStructBB;
	}

    /**
     * writeStructureToNBT
     */
	@Override
    protected void func_143012_a(NBTTagCompound tagCompound) {
        super.func_143012_a(tagCompound);
        tagCompound.setInteger("CatsSpawned", this.CATS_SPAWNED);
        tagCompound.setInteger("DogsSpawned", this.DOGS_SPAWNED);
    }

    /**
     * readStructureFromNBT
     */
    @Override
    protected void func_143011_b(NBTTagCompound tagCompound) {
        super.func_143011_b(tagCompound);
        this.CATS_SPAWNED = tagCompound.getInteger("CatsSpawned");
        this.DOGS_SPAWNED = tagCompound.getInteger("DogsSpawned");
    }

	public static ComponentPetShelter buildComponent(Start villagePiece, @SuppressWarnings("rawtypes") List pieces, Random random, int x, int y, int z, int coordBaseMode, int p5) {
		StructureBoundingBox box = StructureBoundingBox.getComponentToAddBoundingBox(x, y, z, 0, 0, -1, 17, 6, 14, coordBaseMode);
		return canVillageGoDeeper(box) && StructureComponent.findIntersecting(pieces, box) == null ? new ComponentPetShelter(villagePiece, p5, random, box, coordBaseMode) : null;
    }
	
    /**
     * second Part of Structure generating, this for example places Spiderwebs,
     * Mob Spawners, it closes Mineshafts at the end, it adds Fences...
     */
	@Override
	public boolean addComponentParts(World world, Random rand, StructureBoundingBox structureBoundingBox) {
		if (this.averageGroundLevel < 0) {
			this.averageGroundLevel = this.getAverageGroundLevel(world, structureBoundingBox);

            if (this.averageGroundLevel < 0)
                return true;

            this.boundingBox.offset(0, this.averageGroundLevel - this.boundingBox.maxY + 4, 0);
         }

        /* arguments: (World worldObj, StructureBoundingBox structBB,
         *              int minX, int minY, int minZ,
         *              int maxX, int maxY, int maxZ,
         *              int placeBlockId, int replaceBlockId, boolean alwaysreplace)
         * EXAMPLE: this.fillWithBlocks(world, structureBoundingBox, 0, 5, 0, 6, 5, 6, Blocks.log, Blocks.log, false);
         *
         * placeBlockAtCurrentPosition(world, blockID, metadata, x, y, z, structureBoundingBox);
         * this.placeDoorAtCurrentPosition(world, structureBoundingBox, rand, x, y, z, this.getMetadataWithOffset(Blocks.wooden_door, 1));
         */

		// Foundation & Pillars
        this.fillWithBlocks(world, structureBoundingBox, 2, 1, -1, 8, 1, 11, Blocks.cobblestone, Blocks.cobblestone, false);
        this.fillWithBlocks(world, structureBoundingBox, 9, 1, 3, 16, 1, 11, Blocks.cobblestone, Blocks.cobblestone, false);
        this.fillWithBlocks(world, structureBoundingBox, 2, 0, -1, 8, 0, 2, Blocks.cobblestone, Blocks.cobblestone, false);
        this.fillWithBlocks(world, structureBoundingBox, 2, 0, 3, 16, 0, 11, Blocks.cobblestone, Blocks.cobblestone, false);
        this.fillWithBlocks(world, structureBoundingBox, 2, -1, -1, 16, -1, 11, Blocks.cobblestone, Blocks.cobblestone, false);

        this.fillWithBlocks(world, structureBoundingBox, 2, 2, -1, 2, 4, -1, Blocks.cobblestone, Blocks.cobblestone, false);
        this.fillWithBlocks(world, structureBoundingBox, 2, 2, 11, 2, 4, 11, Blocks.cobblestone, Blocks.cobblestone, false);
        this.fillWithBlocks(world, structureBoundingBox, 8, 2, 3, 8, 4, 3, Blocks.cobblestone, Blocks.cobblestone, false);
        this.fillWithBlocks(world, structureBoundingBox, 8, 2, -1, 8, 4, -1, Blocks.cobblestone, Blocks.cobblestone, false);
        this.fillWithBlocks(world, structureBoundingBox, 8, 2, 11, 8, 4, 11, Blocks.cobblestone, Blocks.cobblestone, false);
        this.fillWithBlocks(world, structureBoundingBox, 16, 2, 3, 16, 3, 3, Blocks.cobblestone, Blocks.cobblestone, false);
        this.fillWithBlocks(world, structureBoundingBox, 16, 2, 11, 16, 4, 11, Blocks.cobblestone, Blocks.cobblestone, false);

        // Floor & Pen
        this.fillWithBlocks(world, structureBoundingBox, 3, 1, 0, 7, 1, 10, Blocks.planks, Blocks.planks, false);
        this.fillWithBlocks(world, structureBoundingBox, 8, 1, 4, 15, 1, 10, Blocks.planks, Blocks.planks, false);
        this.fillWithBlocks(world, structureBoundingBox, 9, 1, -1, 16, 1, 2, Blocks.grass, Blocks.grass, false);
        this.fillWithBlocks(world, structureBoundingBox, 9, 0, -1, 16, 0, 2, Blocks.dirt, Blocks.dirt, false);
        this.fillWithBlocks(world, structureBoundingBox, 9, 2, -1, 16, 2, 2, Blocks.fence, Blocks.air, false);
        this.fillWithAir(world, structureBoundingBox, 9, 2, 0, 15, 2, 2);
        this.fillWithAir(world, structureBoundingBox, 9, 3, -1, 16, 7, 2);

        // Front Walls
        this.placeDoorAtCurrentPosition(world, structureBoundingBox, rand, 5, 2, -1, this.getMetadataWithOffset(Blocks.wooden_door, 1));
        this.placeBlockAtCurrentPosition(world, Blocks.stone_stairs, getMetadataWithOffset(Blocks.stone_stairs, 3), 5, 1, -2, structureBoundingBox);
        this.fillWithBlocks(world, structureBoundingBox, 3, 2, -1, 4, 2, -1, Blocks.cobblestone, Blocks.cobblestone, false);
        this.fillWithBlocks(world, structureBoundingBox, 6, 2, -1, 7, 2, -1, Blocks.cobblestone, Blocks.cobblestone, false);
        this.fillWithBlocks(world, structureBoundingBox, 3, 3, -1, 4, 4, -1, Blocks.planks, Blocks.planks, false);
        this.fillWithBlocks(world, structureBoundingBox, 6, 3, -1, 7, 4, -1, Blocks.planks, Blocks.planks, false);
        this.fillWithBlocks(world, structureBoundingBox, 4, 6, -1, 6, 6, -1, Blocks.planks, Blocks.planks, false);
        this.placeBlockAtCurrentPosition(world, Blocks.planks, 0, 5, 4, -1, structureBoundingBox);
        this.placeBlockAtCurrentPosition(world, Blocks.planks, 0, 3, 5, -1, structureBoundingBox);
        this.placeBlockAtCurrentPosition(world, Blocks.planks, 0, 7, 5, -1, structureBoundingBox);
        this.placeBlockAtCurrentPosition(world, Blocks.log, 0, 4, 5, -1, structureBoundingBox);
        this.placeBlockAtCurrentPosition(world, Blocks.log, 0, 6, 5, -1, structureBoundingBox);
        this.placeBlockAtCurrentPosition(world, Blocks.glass_pane, 0, 5, 5, -1, structureBoundingBox);

        this.placeBlockAtCurrentPosition(world, Blocks.fence_gate, getMetadataWithOffset(Blocks.fence_gate, 2), 12, 2, 3, structureBoundingBox);
        this.fillWithBlocks(world, structureBoundingBox, 9, 2, 3, 11, 2, 3, Blocks.cobblestone, Blocks.cobblestone, false);
        this.fillWithBlocks(world, structureBoundingBox, 13, 2, 3, 15, 2, 3, Blocks.cobblestone, Blocks.cobblestone, false);
        this.fillWithBlocks(world, structureBoundingBox, 9, 3, 3, 10, 3, 3, Blocks.planks, Blocks.planks, false);
        this.fillWithBlocks(world, structureBoundingBox, 14, 3, 3, 15, 3, 3, Blocks.planks, Blocks.planks, false);
        this.placeBlockAtCurrentPosition(world, Blocks.log, 0, 11, 3, 3, structureBoundingBox);
        this.placeBlockAtCurrentPosition(world, Blocks.log, 0, 13, 3, 3, structureBoundingBox);
        this.placeBlockAtCurrentPosition(world, Blocks.glass_pane, 0, 12, 3, 3, structureBoundingBox);

        // Long Wall
        this.fillWithBlocks(world, structureBoundingBox, 2, 2, 0, 2, 2, 10, Blocks.cobblestone, Blocks.cobblestone, false);
        this.fillWithBlocks(world, structureBoundingBox, 2, 3, 0, 2, 4, 0, Blocks.planks, Blocks.planks, false);
        this.fillWithBlocks(world, structureBoundingBox, 2, 3, 10, 2, 4, 10, Blocks.planks, Blocks.planks, false);
        this.fillWithBlocks(world, structureBoundingBox, 2, 4, 1, 2, 4, 3, Blocks.planks, Blocks.planks, false);
        this.fillWithBlocks(world, structureBoundingBox, 2, 4, 7, 2, 4, 9, Blocks.planks, Blocks.planks, false);
        this.fillWithBlocks(world, structureBoundingBox, 2, 3, 1, 2, 3, 3, Blocks.log, Blocks.log, false);
        this.fillWithBlocks(world, structureBoundingBox, 2, 3, 7, 2, 3, 9, Blocks.log, Blocks.log, false);
        this.placeBlockAtCurrentPosition(world, Blocks.glass_pane, 0, 2, 3, 2, structureBoundingBox);
        this.placeBlockAtCurrentPosition(world, Blocks.glass_pane, 0, 2, 3, 8, structureBoundingBox);
        for(int z = 4; z <= 6; z++) {
            this.placeBlockAtCurrentPosition(world, Blocks.oak_stairs, getMetadataWithOffset(Blocks.oak_stairs, 1), 2, 3, z, structureBoundingBox);
            this.placeBlockAtCurrentPosition(world, Blocks.oak_stairs, getMetadataWithOffset(Blocks.oak_stairs, 1), 2, 4, z, structureBoundingBox);
        }

        // Short Walls
        this.fillWithBlocks(world, structureBoundingBox, 8, 2, 0, 8, 2, 2, Blocks.cobblestone, Blocks.cobblestone, false);
        this.fillWithBlocks(world, structureBoundingBox, 16, 2, 4, 16, 2, 10, Blocks.cobblestone, Blocks.cobblestone, false);
        this.fillWithBlocks(world, structureBoundingBox, 8, 4, 0, 8, 4, 2, Blocks.planks, Blocks.planks, false);
        this.fillWithBlocks(world, structureBoundingBox, 16, 4, 4, 16, 4, 10, Blocks.planks, Blocks.planks, false);
        this.fillWithBlocks(world, structureBoundingBox, 16, 5, 5, 16, 5, 9, Blocks.planks, Blocks.planks, false);
        this.fillWithBlocks(world, structureBoundingBox, 16, 6, 6, 16, 6, 8, Blocks.planks, Blocks.planks, false);
        this.placeBlockAtCurrentPosition(world, Blocks.planks, 0, 16, 3, 7, structureBoundingBox);
        this.fillWithBlocks(world, structureBoundingBox, 16, 3, 4, 16, 3, 6, Blocks.log, Blocks.log, false);
        this.fillWithBlocks(world, structureBoundingBox, 16, 3, 8, 16, 3, 10, Blocks.log, Blocks.log, false);
        this.fillWithBlocks(world, structureBoundingBox, 8, 3, 0, 8, 3, 2, Blocks.glass_pane, Blocks.log, false);
        this.placeBlockAtCurrentPosition(world, Blocks.glass_pane, 0, 16, 3, 5, structureBoundingBox);
        this.placeBlockAtCurrentPosition(world, Blocks.glass_pane, 0, 16, 3, 9, structureBoundingBox);

        // Back Wall
        this.fillWithBlocks(world, structureBoundingBox, 3, 2, 11, 7, 2, 11, Blocks.cobblestone, Blocks.cobblestone, false);
        this.fillWithBlocks(world, structureBoundingBox, 9, 2, 11, 15, 2, 11, Blocks.cobblestone, Blocks.cobblestone, false);
        this.fillWithBlocks(world, structureBoundingBox, 3, 3, 11, 3, 5, 11, Blocks.planks, Blocks.planks, false);
        this.fillWithBlocks(world, structureBoundingBox, 7, 3, 11, 7, 5, 11, Blocks.planks, Blocks.planks, false);
        this.fillWithBlocks(world, structureBoundingBox, 4, 6, 11, 6, 6, 11, Blocks.planks, Blocks.planks, false);
        this.fillWithBlocks(world, structureBoundingBox, 9, 3, 11, 10, 3, 11, Blocks.planks, Blocks.planks, false);
        this.fillWithBlocks(world, structureBoundingBox, 14, 3, 11, 15, 3, 11, Blocks.planks, Blocks.planks, false);
        for(int x = 4; x <= 6; x++) {
            this.placeBlockAtCurrentPosition(world, Blocks.oak_stairs, getMetadataWithOffset(Blocks.oak_stairs, 3), x, 3, 11, structureBoundingBox);
            this.placeBlockAtCurrentPosition(world, Blocks.oak_stairs, getMetadataWithOffset(Blocks.oak_stairs, 3), x, 4, 11, structureBoundingBox);
        }
        this.placeBlockAtCurrentPosition(world, Blocks.planks, 0, 5, 4, 11, structureBoundingBox);
        this.placeBlockAtCurrentPosition(world, Blocks.log, 0, 4, 5, 11, structureBoundingBox);
        this.placeBlockAtCurrentPosition(world, Blocks.log, 0, 6, 5, 11, structureBoundingBox);
        this.placeBlockAtCurrentPosition(world, Blocks.log, 0, 11, 3, 11, structureBoundingBox);
        this.placeBlockAtCurrentPosition(world, Blocks.log, 0, 13, 3, 11, structureBoundingBox);
        this.placeBlockAtCurrentPosition(world, Blocks.glass_pane, 0, 5, 5, 11, structureBoundingBox);
        this.placeBlockAtCurrentPosition(world, Blocks.glass_pane, 0, 12, 3, 11, structureBoundingBox);

    	// Main Room
        this.fillWithAir(world, structureBoundingBox, 3, 2, 0, 7, 7, 10);
        this.fillWithBlocks(world, structureBoundingBox, 4, 2, 9, 6, 2, 9, Blocks.double_stone_slab, Blocks.double_stone_slab, false);
        this.placeBlockAtCurrentPosition(world, Blocks.torch, 0, 5, 4, 10, structureBoundingBox);
        this.placeBlockAtCurrentPosition(world, Blocks.torch, 0, 5, 4, 0, structureBoundingBox);
        this.placeBlockAtCurrentPosition(world, Blocks.torch, 0, 3, 3, 3, structureBoundingBox);
        this.placeBlockAtCurrentPosition(world, Blocks.torch, 0, 3, 3, 7, structureBoundingBox);
        this.fillWithBlocks(world, structureBoundingBox, 8, 2, 8, 8, 2, 10, Blocks.cobblestone, Blocks.cobblestone, false);
        this.fillWithBlocks(world, structureBoundingBox, 8, 2, 4, 8, 2, 6, Blocks.cobblestone, Blocks.cobblestone, false);
        this.fillWithBlocks(world, structureBoundingBox, 8, 3, 9, 8, 4, 10, Blocks.planks, Blocks.planks, false);
        this.fillWithBlocks(world, structureBoundingBox, 8, 3, 4, 8, 4, 5, Blocks.planks, Blocks.planks, false);
        this.fillWithBlocks(world, structureBoundingBox, 8, 3, 6, 8, 4, 8, Blocks.glass, Blocks.glass, false);
        this.placeDoorAtCurrentPosition(world, structureBoundingBox, rand, 8, 2, 7, this.getMetadataWithOffset(Blocks.wooden_door, 0));

        // Pet Room
        this.fillWithAir(world, structureBoundingBox, 9, 2, 4, 15, 7, 10);
        this.fillWithBlocks(world, structureBoundingBox, 11, 2, 7, 15, 2, 7, Blocks.fence, Blocks.fence, false);
        this.fillWithBlocks(world, structureBoundingBox, 11, 2, 4, 11, 2, 10, Blocks.fence, Blocks.fence, false);
        this.fillWithBlocks(world, structureBoundingBox, 7, 6, 6, 8, 6, 8, Blocks.planks, Blocks.planks, false);
        this.fillWithBlocks(world, structureBoundingBox, 8, 5, 5, 8, 5, 9, Blocks.planks, Blocks.planks, false);
        this.placeBlockAtCurrentPosition(world, Blocks.torch, 0, 9, 5, 7, structureBoundingBox);
        this.placeBlockAtCurrentPosition(world, Blocks.torch, 0, 15, 5, 7, structureBoundingBox);
        this.placeBlockAtCurrentPosition(world, Blocks.fence_gate, getMetadataWithOffset(Blocks.fence_gate, 3), 11, 2, 5, structureBoundingBox);
        this.placeBlockAtCurrentPosition(world, Blocks.fence_gate, getMetadataWithOffset(Blocks.fence_gate, 3), 11, 2, 9, structureBoundingBox);
        for (int x = 14; x <= 15; x++) {
            for (int z = 4; z <= 5; z++) {
                this.placeBlockAtCurrentPosition(world, Blocks.carpet, getMetadataWithOffset(Blocks.carpet, 3), x, 2, z, structureBoundingBox);
            }
            for (int z = 9; z <= 10; z++) {
                this.placeBlockAtCurrentPosition(world,Blocks.carpet, getMetadataWithOffset(Blocks.carpet, 3), x, 2, z, structureBoundingBox);
            }
        }

        // Roof mayhem
        this.fillWithBlocks(world, structureBoundingBox, 5, 7, -1, 5, 7, 11, Blocks.planks, Blocks.planks, false);
        for (int z = -1; z <= 11; z++) {
            this.placeBlockAtCurrentPosition(world, Blocks.oak_stairs, getMetadataWithOffset(Blocks.oak_stairs, 0), 1, 4, z, structureBoundingBox);
            this.placeBlockAtCurrentPosition(world, Blocks.oak_stairs, getMetadataWithOffset(Blocks.oak_stairs, 0), 2, 5, z, structureBoundingBox);
            this.placeBlockAtCurrentPosition(world, Blocks.oak_stairs, getMetadataWithOffset(Blocks.oak_stairs, 0), 3, 6, z, structureBoundingBox);
            this.placeBlockAtCurrentPosition(world, Blocks.oak_stairs, getMetadataWithOffset(Blocks.oak_stairs, 0), 4, 7, z, structureBoundingBox);
        }
        this.fillWithBlocks(world, structureBoundingBox, 3, 5, 0, 3, 5, 10, Blocks.planks, Blocks.planks, false);
        this.fillWithBlocks(world, structureBoundingBox, 4, 6, 0, 4, 6, 10, Blocks.planks, Blocks.planks, false);
        this.fillWithBlocks(world, structureBoundingBox, 6, 6, 0, 6, 6, 10, Blocks.planks, Blocks.planks, false);
        this.fillWithBlocks(world, structureBoundingBox, 7, 5, 0, 7, 5, 10, Blocks.planks, Blocks.planks, false);
        for (int z = -1; z <= 3; z++) {
            this.placeBlockAtCurrentPosition(world, Blocks.oak_stairs, getMetadataWithOffset(Blocks.oak_stairs, 1), 9, 4, z, structureBoundingBox);
        }
        for (int z = -1; z <= 4; z++) {
            this.placeBlockAtCurrentPosition(world, Blocks.oak_stairs, getMetadataWithOffset(Blocks.oak_stairs, 1), 8, 5, z, structureBoundingBox);
        }
        for (int z = -1; z <= 5; z++) {
            this.placeBlockAtCurrentPosition(world, Blocks.oak_stairs, getMetadataWithOffset(Blocks.oak_stairs, 1), 7, 6, z, structureBoundingBox);
        }
        for (int z = -1; z <= 6; z++) {
            this.placeBlockAtCurrentPosition(world, Blocks.oak_stairs, getMetadataWithOffset(Blocks.oak_stairs, 1), 6, 7, z, structureBoundingBox);
        }
        for (int z = 8; z <= 11; z++) {
            this.placeBlockAtCurrentPosition(world, Blocks.oak_stairs, getMetadataWithOffset(Blocks.oak_stairs, 1), 6, 7, z, structureBoundingBox);
        }
        for (int z = 9; z <= 11; z++) {
            this.placeBlockAtCurrentPosition(world, Blocks.oak_stairs, getMetadataWithOffset(Blocks.oak_stairs, 1), 7, 6, z, structureBoundingBox);
        }
        for (int z = 10; z <= 11; z++) {
            this.placeBlockAtCurrentPosition(world, Blocks.oak_stairs, getMetadataWithOffset(Blocks.oak_stairs, 1), 8, 5, z, structureBoundingBox);
        }

        this.fillWithBlocks(world, structureBoundingBox, 6, 7, 7, 16, 7, 7, Blocks.planks, Blocks.planks, false);
        for(int x = 7; x <= 16; x++) {
            this.placeBlockAtCurrentPosition(world, Blocks.oak_stairs, getMetadataWithOffset(Blocks.oak_stairs, 3), x, 7, 6, structureBoundingBox);
        }
        for(int x = 8; x <= 16; x++) {
            this.placeBlockAtCurrentPosition(world, Blocks.oak_stairs, getMetadataWithOffset(Blocks.oak_stairs, 3), x, 6, 5, structureBoundingBox);
        }
        for(int x = 9; x <= 16; x++) {
            this.placeBlockAtCurrentPosition(world, Blocks.oak_stairs, getMetadataWithOffset(Blocks.oak_stairs, 3), x, 5, 4, structureBoundingBox);
        }
        for (int x = 10; x <= 16; x++) {
            this.placeBlockAtCurrentPosition(world, Blocks.oak_stairs, getMetadataWithOffset(Blocks.oak_stairs, 3), x, 4, 3, structureBoundingBox);
        }
        for (int x = 9; x <= 16; x++) {
            this.placeBlockAtCurrentPosition(world, Blocks.oak_stairs, getMetadataWithOffset(Blocks.oak_stairs, 2), x, 4, 11, structureBoundingBox);
        }
        for (int x = 9; x <= 16; x++) {
            this.placeBlockAtCurrentPosition(world, Blocks.oak_stairs, getMetadataWithOffset(Blocks.oak_stairs, 2), x, 5, 10, structureBoundingBox);
        }
        for (int x = 8; x <= 16; x++) {
            this.placeBlockAtCurrentPosition(world, Blocks.oak_stairs, getMetadataWithOffset(Blocks.oak_stairs, 2), x, 6, 9, structureBoundingBox);
        }
        for (int x = 7; x <= 16; x++) {
            this.placeBlockAtCurrentPosition(world, Blocks.oak_stairs, getMetadataWithOffset(Blocks.oak_stairs, 2), x, 7, 8, structureBoundingBox);
        }

        this.spawnVillagers(world, structureBoundingBox, 5, 2, 10, 1);
        this.spawnCats(world, structureBoundingBox, 14, 2, 9);
        this.spawnDogs(world, structureBoundingBox, 14, 2, 5);
        
		return true;
	}
	
	/**
     * Returns the villager type to spawn in this component, based on the number
     * of villagers already spawned.
     */
	@Override
    protected int getVillagerType (int par1) {
        return 28643;
    }

    private void spawnCats(World world, StructureBoundingBox structureBoundingBoxIn, int x, int y, int z) {
        int count = world.rand.nextInt(3) + 1;
        if (this.CATS_SPAWNED < count) {
            for (int i = this.CATS_SPAWNED; i < count; ++i) {
                int offX = this.getXWithOffset(x - i, z);
                int offY = this.getYWithOffset(y);
                int offZ = this.getZWithOffset(x - i, z);

                if (!structureBoundingBoxIn.isVecInside(offX, offY, offZ)) {
                    break;
                }

                ++this.CATS_SPAWNED;

                if (world.rand.nextInt(4) == 0) {
                    EntityCat kitten = new EntityCat(world);
                    kitten.setGrowingAge(-SimplyCatsConfig.KITTEN_MATURE_TIMER);
                    kitten.setLocationAndAngles((double) offX + 0.5D, (double) offY, (double) offZ + 0.5D, 0.0F, 0.0F);
                    kitten.setFixed((byte)1);
                    kitten.onInitialSpawn();
                    world.spawnEntityInWorld(kitten);
                } else {
                    EntityCat cat = new EntityCat(world);
                    cat.setLocationAndAngles((double) offX + 0.5D, (double) offY, (double) offZ + 0.5D, 0.0F, 0.0F);
                    cat.setFixed((byte)1);
                    cat.onInitialSpawn();
                    world.spawnEntityInWorld(cat);
                }
            }
        }
    }

    private void spawnDogs(World world, StructureBoundingBox structureBoundingBoxIn, int x, int y, int z) {
        int count = world.rand.nextInt(2) + 1;
        if (this.DOGS_SPAWNED < count) {
            for (int i = this.DOGS_SPAWNED; i < count; ++i) {
                int offX = this.getXWithOffset(x - i, z);
                int offY = this.getYWithOffset(y);
                int offZ = this.getZWithOffset(x - i, z);

                if (!structureBoundingBoxIn.isVecInside(offX, offY, offZ)) {
                    break;
                }

                ++this.DOGS_SPAWNED;

                if (world.rand.nextInt(4) == 0) {
                    EntityWolf puppy = new EntityWolf(world);
                    puppy.setGrowingAge(-24000);
                    puppy.setLocationAndAngles((double) offX + 0.5D, (double) offY, (double) offZ + 0.5D, 0.0F, 0.0F);
                    world.spawnEntityInWorld(puppy);
                } else {
                    EntityWolf dog = new EntityWolf(world);
                    dog.setLocationAndAngles((double) offX + 0.5D, (double) offY, (double) offZ + 0.5D, 0.0F, 0.0F);
                    world.spawnEntityInWorld(dog);
                }
            }
        }
    }

}
