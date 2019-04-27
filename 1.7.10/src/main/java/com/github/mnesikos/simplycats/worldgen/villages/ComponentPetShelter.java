package com.github.mnesikos.simplycats.worldgen.villages;

import java.util.List;
import java.util.Random;

import net.minecraft.init.Blocks;
import net.minecraft.world.World;
import net.minecraft.world.gen.structure.StructureBoundingBox;
import net.minecraft.world.gen.structure.StructureComponent;
import net.minecraft.world.gen.structure.StructureVillagePieces;
import net.minecraft.world.gen.structure.StructureVillagePieces.Start;

public class ComponentPetShelter extends StructureVillagePieces.Village {
		private int averageGroundLevel = -1;
	
	public ComponentPetShelter(StructureVillagePieces.Start parStart, int parType, Random parRand, StructureBoundingBox parStructBB, int parFacing) {
		super();
		this.coordBaseMode = parFacing;
		this.boundingBox = parStructBB;
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
		if (this.averageGroundLevel < 0) 
        {
			this.averageGroundLevel = this.getAverageGroundLevel(world, structureBoundingBox);

            if (this.averageGroundLevel < 0)
                return true;

            this.boundingBox.offset(0, this.averageGroundLevel - this.boundingBox.maxY + 4, 0);
         }
		
        /**
         * arguments: (World worldObj, StructureBoundingBox structBB,
         * int minX, int minY, int minZ,
         * int maxX, int maxY, int maxZ,
         * int placeBlockId, int replaceBlockId, boolean alwaysreplace)
         * 
         * this.fillWithBlocks(world, structureBoundingBox, 0, 5, 0, 6, 5, 6, Blocks.log, Blocks.log, false);
         * placeBlockAtCurrentPosition(world, blockID, metadata, x, y, z, structureBoundingBox);
         * this.placeDoorAtCurrentPosition(world, structureBoundingBox, rand, x, y, z, this.getMetadataWithOffset(Blocks.wooden_door, 1));
         */
		
		// Foundation & Pillars
        this.fillWithBlocks(world, structureBoundingBox, 2, 1, 1, 8, 1, 13, Blocks.cobblestone, Blocks.cobblestone, false);
        this.fillWithBlocks(world, structureBoundingBox, 9, 1, 5, 16, 1, 13, Blocks.cobblestone, Blocks.cobblestone, false);
        
        this.fillWithBlocks(world, structureBoundingBox, 2, 2, 1, 2, 4, 1, Blocks.cobblestone, Blocks.cobblestone, false);
        this.fillWithBlocks(world, structureBoundingBox, 2, 2, 13, 2, 4, 13, Blocks.cobblestone, Blocks.cobblestone, false);
        this.fillWithBlocks(world, structureBoundingBox, 8, 2, 5, 8, 4, 5, Blocks.cobblestone, Blocks.cobblestone, false);
        this.fillWithBlocks(world, structureBoundingBox, 8, 2, 1, 8, 4, 1, Blocks.cobblestone, Blocks.cobblestone, false);
        this.fillWithBlocks(world, structureBoundingBox, 8, 2, 13, 8, 4, 13, Blocks.cobblestone, Blocks.cobblestone, false);
        this.fillWithBlocks(world, structureBoundingBox, 16, 2, 5, 16, 3, 5, Blocks.cobblestone, Blocks.cobblestone, false);
        this.fillWithBlocks(world, structureBoundingBox, 16, 2, 13, 16, 3, 13, Blocks.cobblestone, Blocks.cobblestone, false);
        
        // Floor & Pen
        this.fillWithBlocks(world, structureBoundingBox, 3, 1, 2, 7, 1, 12, Blocks.planks, Blocks.planks, false);
        this.fillWithBlocks(world, structureBoundingBox, 8, 1, 6, 15, 1, 12, Blocks.planks, Blocks.planks, false);
        this.fillWithBlocks(world, structureBoundingBox, 9, 1, 1, 16, 1, 4, Blocks.dirt, Blocks.dirt, false);
        this.fillWithBlocks(world, structureBoundingBox, 9, 2, 1, 16, 2, 4, Blocks.fence, Blocks.fence, false);
        this.fillWithBlocks(world, structureBoundingBox, 9, 2, 2, 15, 2, 4, Blocks.air, Blocks.air, false);
        
        
        // Front Walls
        this.placeDoorAtCurrentPosition(world, structureBoundingBox, rand, 5, 2, 1, this.getMetadataWithOffset(Blocks.wooden_door, 1));
        this.placeBlockAtCurrentPosition(world, Blocks.stone_stairs, getMetadataWithOffset(Blocks.stone_stairs, 3), 5, 1, 0, structureBoundingBox);
        this.fillWithBlocks(world, structureBoundingBox, 3, 2, 1, 4, 2, 1, Blocks.cobblestone, Blocks.cobblestone, false);
        this.fillWithBlocks(world, structureBoundingBox, 6, 2, 1, 7, 2, 1, Blocks.cobblestone, Blocks.cobblestone, false);
        this.fillWithBlocks(world, structureBoundingBox, 3, 3, 1, 4, 4, 1, Blocks.planks, Blocks.planks, false);
        this.fillWithBlocks(world, structureBoundingBox, 6, 3, 1, 7, 4, 1, Blocks.planks, Blocks.planks, false);
        this.fillWithBlocks(world, structureBoundingBox, 4, 6, 1, 6, 6, 1, Blocks.planks, Blocks.planks, false);
        this.placeBlockAtCurrentPosition(world, Blocks.planks, 0, 5, 4, 1, structureBoundingBox);
        this.placeBlockAtCurrentPosition(world, Blocks.planks, 0, 3, 5, 1, structureBoundingBox);
        this.placeBlockAtCurrentPosition(world, Blocks.planks, 0, 7, 5, 1, structureBoundingBox);
        this.placeBlockAtCurrentPosition(world, Blocks.log, 0, 4, 5, 1, structureBoundingBox);
        this.placeBlockAtCurrentPosition(world, Blocks.log, 0, 6, 5, 1, structureBoundingBox);
        this.placeBlockAtCurrentPosition(world, Blocks.glass_pane, 0, 5, 5, 1, structureBoundingBox);
        
        this.placeBlockAtCurrentPosition(world, Blocks.fence_gate, getMetadataWithOffset(Blocks.fence_gate, 2), 12, 2, 5, structureBoundingBox);
        this.fillWithBlocks(world, structureBoundingBox, 9, 2, 5, 11, 2, 5, Blocks.cobblestone, Blocks.cobblestone, false);
        this.fillWithBlocks(world, structureBoundingBox, 13, 2, 5, 15, 2, 5, Blocks.cobblestone, Blocks.cobblestone, false);
        this.fillWithBlocks(world, structureBoundingBox, 9, 3, 5, 10, 3, 5, Blocks.planks, Blocks.planks, false);
        this.fillWithBlocks(world, structureBoundingBox, 14, 3, 5, 15, 3, 5, Blocks.planks, Blocks.planks, false);
        this.placeBlockAtCurrentPosition(world, Blocks.log, 0, 11, 3, 5, structureBoundingBox);
        this.placeBlockAtCurrentPosition(world, Blocks.log, 0, 13, 3, 5, structureBoundingBox);
        this.placeBlockAtCurrentPosition(world, Blocks.glass_pane, 0, 12, 3, 5, structureBoundingBox);
        
        // Long Wall
        this.fillWithBlocks(world, structureBoundingBox, 2, 2, 2, 2, 2, 12, Blocks.cobblestone, Blocks.cobblestone, false);
        this.fillWithBlocks(world, structureBoundingBox, 2, 3, 2, 2, 4, 2, Blocks.planks, Blocks.planks, false);
        this.fillWithBlocks(world, structureBoundingBox, 2, 3, 12, 2, 4, 12, Blocks.planks, Blocks.planks, false);
        this.fillWithBlocks(world, structureBoundingBox, 2, 4, 3, 2, 4, 5, Blocks.planks, Blocks.planks, false);
        this.fillWithBlocks(world, structureBoundingBox, 2, 4, 9, 2, 4, 11, Blocks.planks, Blocks.planks, false);
        this.fillWithBlocks(world, structureBoundingBox, 2, 3, 3, 2, 3, 5, Blocks.log, Blocks.log, false);
        this.fillWithBlocks(world, structureBoundingBox, 2, 3, 9, 2, 3, 11, Blocks.log, Blocks.log, false);
        this.placeBlockAtCurrentPosition(world, Blocks.glass_pane, 0, 2, 3, 4, structureBoundingBox);
        this.placeBlockAtCurrentPosition(world, Blocks.glass_pane, 0, 2, 3, 10, structureBoundingBox);
        for(int z = 6; z <= 8; z++)
        {
        	this.placeBlockAtCurrentPosition(world, Blocks.oak_stairs, getMetadataWithOffset(Blocks.oak_stairs, 1), 2, 3, z, structureBoundingBox);
        	this.placeBlockAtCurrentPosition(world, Blocks.oak_stairs, getMetadataWithOffset(Blocks.oak_stairs, 1), 2, 4, z, structureBoundingBox);
        }
        
        // Short Walls
        this.fillWithBlocks(world, structureBoundingBox, 8, 2, 2, 8, 2, 4, Blocks.cobblestone, Blocks.cobblestone, false);
        this.fillWithBlocks(world, structureBoundingBox, 16, 2, 6, 16, 2, 12, Blocks.cobblestone, Blocks.cobblestone, false);
        this.fillWithBlocks(world, structureBoundingBox, 8, 4, 2, 8, 4, 4, Blocks.planks, Blocks.planks, false);
        this.fillWithBlocks(world, structureBoundingBox, 16, 4, 6, 16, 4, 12, Blocks.planks, Blocks.planks, false);
        this.fillWithBlocks(world, structureBoundingBox, 16, 5, 7, 16, 5, 11, Blocks.planks, Blocks.planks, false);
        this.fillWithBlocks(world, structureBoundingBox, 16, 6, 8, 16, 6, 10, Blocks.planks, Blocks.planks, false);
        this.placeBlockAtCurrentPosition(world, Blocks.planks, 0, 16, 3, 9, structureBoundingBox);
        this.fillWithBlocks(world, structureBoundingBox, 16, 3, 6, 16, 3, 8, Blocks.log, Blocks.log, false);
        this.fillWithBlocks(world, structureBoundingBox, 16, 3, 10, 16, 3, 12, Blocks.log, Blocks.log, false);
        this.fillWithBlocks(world, structureBoundingBox, 8, 3, 2, 8, 3, 4, Blocks.glass, Blocks.log, false);
        this.placeBlockAtCurrentPosition(world, Blocks.glass_pane, 0, 16, 3, 7, structureBoundingBox);
        this.placeBlockAtCurrentPosition(world, Blocks.glass_pane, 0, 16, 3, 11, structureBoundingBox);
        
        // Back Wall
        this.fillWithBlocks(world, structureBoundingBox, 3, 2, 13, 7, 2, 13, Blocks.cobblestone, Blocks.cobblestone, false);
        this.fillWithBlocks(world, structureBoundingBox, 9, 2, 13, 15, 2, 13, Blocks.cobblestone, Blocks.cobblestone, false);
        this.fillWithBlocks(world, structureBoundingBox, 3, 3, 13, 3, 5, 13, Blocks.planks, Blocks.planks, false);
        this.fillWithBlocks(world, structureBoundingBox, 7, 3, 13, 7, 5, 13, Blocks.planks, Blocks.planks, false);
        this.fillWithBlocks(world, structureBoundingBox, 4, 6, 13, 6, 6, 13, Blocks.planks, Blocks.planks, false);
        this.fillWithBlocks(world, structureBoundingBox, 9, 3, 13, 10, 3, 13, Blocks.planks, Blocks.planks, false);
        this.fillWithBlocks(world, structureBoundingBox, 14, 3, 13, 15, 3, 13, Blocks.planks, Blocks.planks, false);
        for(int x = 4; x <= 6; x++)
        {
        	this.placeBlockAtCurrentPosition(world, Blocks.oak_stairs, getMetadataWithOffset(Blocks.oak_stairs, 3), x, 3, 13, structureBoundingBox);
        	this.placeBlockAtCurrentPosition(world, Blocks.oak_stairs, getMetadataWithOffset(Blocks.oak_stairs, 3), x, 4, 13, structureBoundingBox);
        }
        	this.placeBlockAtCurrentPosition(world, Blocks.planks, 0, 5, 4, 13, structureBoundingBox);
        this.placeBlockAtCurrentPosition(world, Blocks.log, 0, 4, 5, 13, structureBoundingBox);
        this.placeBlockAtCurrentPosition(world, Blocks.log, 0, 6, 5, 13, structureBoundingBox);
        this.placeBlockAtCurrentPosition(world, Blocks.log, 0, 11, 3, 13, structureBoundingBox);
        this.placeBlockAtCurrentPosition(world, Blocks.log, 0, 13, 3, 13, structureBoundingBox);
        this.placeBlockAtCurrentPosition(world, Blocks.glass_pane, 0, 5, 5, 13, structureBoundingBox);
        this.placeBlockAtCurrentPosition(world, Blocks.glass_pane, 0, 12, 3, 13, structureBoundingBox);

    	// Main Room
        this.fillWithBlocks(world, structureBoundingBox, 4, 2, 11, 6, 2, 11, Blocks.double_stone_slab, Blocks.double_stone_slab, false);
        this.placeBlockAtCurrentPosition(world, Blocks.torch, getMetadataWithOffset(Blocks.torch, 1), 5, 4, 12, structureBoundingBox);
        this.placeBlockAtCurrentPosition(world, Blocks.torch, getMetadataWithOffset(Blocks.torch, 2), 5, 4, 2, structureBoundingBox);
        this.fillWithBlocks(world, structureBoundingBox, 8, 2, 10, 8, 2, 12, Blocks.cobblestone, Blocks.cobblestone, false);
        this.fillWithBlocks(world, structureBoundingBox, 8, 2, 6, 8, 2, 8, Blocks.cobblestone, Blocks.cobblestone, false);
        this.fillWithBlocks(world, structureBoundingBox, 8, 3, 11, 8, 4, 12, Blocks.planks, Blocks.planks, false);
        this.fillWithBlocks(world, structureBoundingBox, 8, 3, 6, 8, 4, 7, Blocks.planks, Blocks.planks, false);
        this.fillWithBlocks(world, structureBoundingBox, 8, 3, 8, 8, 4, 10, Blocks.glass, Blocks.glass, false);
        this.placeDoorAtCurrentPosition(world, structureBoundingBox, rand, 8, 2, 9, this.getMetadataWithOffset(Blocks.wooden_door, 0));
        
        // Pet Room
        this.fillWithBlocks(world, structureBoundingBox, 11, 2, 9, 15, 2, 9, Blocks.fence, Blocks.fence, false);
        this.fillWithBlocks(world, structureBoundingBox, 11, 2, 6, 11, 2, 12, Blocks.fence, Blocks.fence, false);
        this.placeBlockAtCurrentPosition(world, Blocks.fence_gate, getMetadataWithOffset(Blocks.fence_gate, 3), 11, 2, 7, structureBoundingBox);
        this.placeBlockAtCurrentPosition(world, Blocks.fence_gate, getMetadataWithOffset(Blocks.fence_gate, 3), 11, 2, 11, structureBoundingBox);
        	for (int x = 14; x <= 15; x++)
        	{
        		for (int z = 6; z <= 7; z++)
        		{
                	this.placeBlockAtCurrentPosition(world, Blocks.carpet, getMetadataWithOffset(Blocks.carpet, 3), x, 2, z, structureBoundingBox);
        		}
        		for (int z = 11; z <= 12; z++)
        		{
                	this.placeBlockAtCurrentPosition(world, Blocks.carpet, getMetadataWithOffset(Blocks.carpet, 3), x, 2, z, structureBoundingBox);
        		}
        	}
        
        
        // Roof mayhem
        this.fillWithBlocks(world, structureBoundingBox, 7, 6, 8, 8, 6, 10, Blocks.planks, Blocks.planks, false);
        this.fillWithBlocks(world, structureBoundingBox, 8, 5, 7, 8, 5, 11, Blocks.planks, Blocks.planks, false);
        this.fillWithBlocks(world, structureBoundingBox, 5, 7, 1, 5, 7, 13, Blocks.planks, Blocks.planks, false);
	        for (int z = 1; z <= 13; z++)
	        {
	        	this.placeBlockAtCurrentPosition(world, Blocks.oak_stairs, getMetadataWithOffset(Blocks.oak_stairs, 0), 1, 4, z, structureBoundingBox);
	        	this.placeBlockAtCurrentPosition(world, Blocks.oak_stairs, getMetadataWithOffset(Blocks.oak_stairs, 0), 2, 5, z, structureBoundingBox);
	        	this.placeBlockAtCurrentPosition(world, Blocks.oak_stairs, getMetadataWithOffset(Blocks.oak_stairs, 0), 3, 6, z, structureBoundingBox);
	        	this.placeBlockAtCurrentPosition(world, Blocks.oak_stairs, getMetadataWithOffset(Blocks.oak_stairs, 0), 4, 7, z, structureBoundingBox);
	        }
        this.fillWithBlocks(world, structureBoundingBox, 3, 5, 2, 3, 5, 12, Blocks.planks, Blocks.planks, false);
        this.fillWithBlocks(world, structureBoundingBox, 4, 6, 2, 4, 6, 12, Blocks.planks, Blocks.planks, false);
        this.fillWithBlocks(world, structureBoundingBox, 6, 6, 2, 6, 6, 12, Blocks.planks, Blocks.planks, false);
        this.fillWithBlocks(world, structureBoundingBox, 7, 5, 2, 7, 5, 12, Blocks.planks, Blocks.planks, false);
	        for (int z = 1; z <= 5; z++)
	        {
	        	this.placeBlockAtCurrentPosition(world, Blocks.oak_stairs, getMetadataWithOffset(Blocks.oak_stairs, 1), 9, 4, z, structureBoundingBox);
	        }
	        for (int z = 1; z <= 6; z++)
	        {
	        	this.placeBlockAtCurrentPosition(world, Blocks.oak_stairs, getMetadataWithOffset(Blocks.oak_stairs, 1), 8, 5, z, structureBoundingBox);
	        }
	        for (int z = 1; z <= 7; z++)
	        {
	        	this.placeBlockAtCurrentPosition(world, Blocks.oak_stairs, getMetadataWithOffset(Blocks.oak_stairs, 1), 7, 6, z, structureBoundingBox);
	        }
	        for (int z = 1; z <= 8; z++)
	        {
	        	this.placeBlockAtCurrentPosition(world, Blocks.oak_stairs, getMetadataWithOffset(Blocks.oak_stairs, 1), 6, 7, z, structureBoundingBox);
	        }
		        for (int z = 10; z <= 13; z++)
		        {
		        	this.placeBlockAtCurrentPosition(world, Blocks.oak_stairs, getMetadataWithOffset(Blocks.oak_stairs, 1), 6, 7, z, structureBoundingBox);
		        }
		        for (int z = 11; z <= 13; z++)
		        {
		        	this.placeBlockAtCurrentPosition(world, Blocks.oak_stairs, getMetadataWithOffset(Blocks.oak_stairs, 1), 7, 6, z, structureBoundingBox);
		        }
		        for (int z = 12; z <= 13; z++)
		        {
		        	this.placeBlockAtCurrentPosition(world, Blocks.oak_stairs, getMetadataWithOffset(Blocks.oak_stairs, 1), 8, 5, z, structureBoundingBox);
		        }

        this.fillWithBlocks(world, structureBoundingBox, 6, 7, 9, 16, 7, 9, Blocks.planks, Blocks.planks, false);
	        for(int x = 7; x <= 16; x++)
	    	{
	        	this.placeBlockAtCurrentPosition(world, Blocks.oak_stairs, getMetadataWithOffset(Blocks.oak_stairs, 3), x, 7, 8, structureBoundingBox);
	    	}
		        for(int x = 8; x <= 16; x++)
		    	{
		        	this.placeBlockAtCurrentPosition(world, Blocks.oak_stairs, getMetadataWithOffset(Blocks.oak_stairs, 3), x, 6, 7, structureBoundingBox);
		    	}
	        	for(int x = 9; x <= 16; x++)
	        	{
	        		this.placeBlockAtCurrentPosition(world, Blocks.oak_stairs, getMetadataWithOffset(Blocks.oak_stairs, 3), x, 5, 6, structureBoundingBox);
	        	}
	        	for (int x = 10; x <= 16; x++)
	        	{
	        		this.placeBlockAtCurrentPosition(world, Blocks.oak_stairs, getMetadataWithOffset(Blocks.oak_stairs, 3), x, 4, 5, structureBoundingBox);
	        	}
        	for (int x = 9; x <= 16; x++)
        	{
        		this.placeBlockAtCurrentPosition(world, Blocks.oak_stairs, getMetadataWithOffset(Blocks.oak_stairs, 2), x, 4, 13, structureBoundingBox);
        	}
	        	for (int x = 9; x <= 16; x++)
	        	{
	        		this.placeBlockAtCurrentPosition(world, Blocks.oak_stairs, getMetadataWithOffset(Blocks.oak_stairs, 2), x, 5, 12, structureBoundingBox);
	        	}
	        	for (int x = 8; x <= 16; x++)
	        	{
		        	this.placeBlockAtCurrentPosition(world, Blocks.oak_stairs, getMetadataWithOffset(Blocks.oak_stairs, 2), x, 6, 11, structureBoundingBox);
	        	}
	        	for (int x = 7; x <= 16; x++)
	        	{
		        	this.placeBlockAtCurrentPosition(world, Blocks.oak_stairs, getMetadataWithOffset(Blocks.oak_stairs, 2), x, 7, 10, structureBoundingBox);
	        	}
    	
        
        
        for (int l = 0; l < 7; ++l)
        {
            for (int i1 = 0; i1 < 7; ++i1)
            {
                this.clearCurrentPositionBlocksUpwards(world, i1, 9, l, structureBoundingBox);
                this.func_151554_b(world, Blocks.cobblestone, 0, i1, -1, l, structureBoundingBox);
            }
        }
        this.spawnVillagers(world, structureBoundingBox, 2, 1, 2, 1);
 //       this.spawnCats();
 //       this.spawnDogs();
        
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

}
