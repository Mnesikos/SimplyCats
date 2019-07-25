package com.github.mnesikos.simplycats.worldgen.villages;

import com.github.mnesikos.simplycats.entity.EntityCat;
import com.github.mnesikos.simplycats.init.ModProfessions;
import net.minecraft.block.BlockCarpet;
import net.minecraft.block.BlockStairs;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.IEntityLivingData;
import net.minecraft.entity.passive.EntityWolf;
import net.minecraft.init.Blocks;
import net.minecraft.item.EnumDyeColor;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.gen.structure.StructureBoundingBox;
import net.minecraft.world.gen.structure.StructureComponent;
import net.minecraft.world.gen.structure.StructureVillagePieces;
import net.minecraft.world.gen.structure.template.TemplateManager;
import net.minecraftforge.fml.common.registry.VillagerRegistry.VillagerProfession;

import java.util.List;
import java.util.Random;

public class ComponentPetShelter extends StructureVillagePieces.Village {
    private int AVG_GROUND_LEVEL = -1;
    private int CATS_SPAWNED;
    private int DOGS_SPAWNED;

    public ComponentPetShelter() {

    }

    public ComponentPetShelter(StructureVillagePieces.Start parStart, int parType, Random parRand, StructureBoundingBox parStructBB, EnumFacing facing) {
        super(parStart, parType);
        this.setCoordBaseMode(facing);
        this.boundingBox = parStructBB;
    }

    @Override
    protected void writeStructureToNBT(NBTTagCompound tagCompound) {
        super.writeStructureToNBT(tagCompound);
        tagCompound.setInteger("CatsSpawned", this.CATS_SPAWNED);
        tagCompound.setInteger("DogsSpawned", this.DOGS_SPAWNED);
    }

    @Override
    protected void readStructureFromNBT(NBTTagCompound tagCompound, TemplateManager p_143011_2_) {
        super.readStructureFromNBT(tagCompound, p_143011_2_);
        this.CATS_SPAWNED = tagCompound.getInteger("CatsSpawned");
        this.DOGS_SPAWNED = tagCompound.getInteger("DogsSpawned");
    }

    public static ComponentPetShelter buildComponent(StructureVillagePieces.Start villagePiece, List pieces, Random random, int x, int y, int z, EnumFacing facing, int p5) {
        StructureBoundingBox box = StructureBoundingBox.getComponentToAddBoundingBox(x, y, z, 0, 0, -1, 17, 6, 14, facing);
        return canVillageGoDeeper(box) && StructureComponent.findIntersecting(pieces, box) == null ? new ComponentPetShelter(villagePiece, p5, random, box, facing) : null;
    }

    @Override
    public boolean addComponentParts(World world, Random rand, StructureBoundingBox structureBoundingBox) {
        if (this.AVG_GROUND_LEVEL < 0) {
            this.AVG_GROUND_LEVEL = this.getAverageGroundLevel(world, structureBoundingBox);

            if (this.AVG_GROUND_LEVEL < 0)
                return true;

            this.boundingBox.offset(0, this.AVG_GROUND_LEVEL - this.boundingBox.maxY + 4, 0);
        }

        /**
         * ARGUMENTS: (World worldObj, StructureBoundingBox structBB,
         *              int minX, int minY, int minZ,
         *              int maxX, int maxY, int maxZ,
         *              int placeBlockId, int replaceBlockId, boolean alwaysreplace)
         * EXAMPLE: this.fillWithBlocks(world, structureBoundingBox, 0, 5, 0, 6, 5, 6, Blocks.log, Blocks.log, false);
         *
         * OLD placeBlockAtCurrentPosition(world, blockID, metadata, x, y, z, structureBoundingBox);
         * NEW this.setBlockState(world, iblockstate, x, y, z, structureBoundingBox);
         *
         * STAIRS:
         * 0 = East
         * 1 = West
         * 2 = South
         * 3 = North
         * 4 = East Upside down
         * 5 = West Upside down
         * 6 = South Upside down
         * 7 = North Upside down
         *
         * NEW this.placeTorch(world, enumfacing, x, y, z, structureBoundingBox);
         *
         * OLD this.placeDoorAtCurrentPosition(world, structureBoundingBox, rand, x, y, z, this.getMetadataWithOffset(Blocks.wooden_door, 1));
         * NEW this.createVillageDoor(world, structureBoundingBox, rand, x, y, z, facing);
         */

        IBlockState cobble = this.getBiomeSpecificBlockState(Blocks.COBBLESTONE.getDefaultState());
        IBlockState log = this.getBiomeSpecificBlockState(Blocks.LOG.getDefaultState());
        IBlockState planks = this.getBiomeSpecificBlockState(Blocks.PLANKS.getDefaultState());

        IBlockState stoneStairs = this.getBiomeSpecificBlockState(Blocks.STONE_STAIRS.getDefaultState().withProperty(BlockStairs.FACING, EnumFacing.NORTH));
        IBlockState stairs1 = this.getBiomeSpecificBlockState(Blocks.OAK_STAIRS.getDefaultState().withProperty(BlockStairs.FACING, EnumFacing.WEST));
        IBlockState stairs2 = this.getBiomeSpecificBlockState(Blocks.OAK_STAIRS.getDefaultState().withProperty(BlockStairs.FACING, EnumFacing.SOUTH));
        IBlockState stairs3 = this.getBiomeSpecificBlockState(Blocks.OAK_STAIRS.getDefaultState().withProperty(BlockStairs.FACING, EnumFacing.NORTH));
        IBlockState stairs0 = this.getBiomeSpecificBlockState(Blocks.OAK_STAIRS.getDefaultState().withProperty(BlockStairs.FACING, EnumFacing.EAST));

        IBlockState fence = this.getBiomeSpecificBlockState(Blocks.OAK_FENCE.getDefaultState());
        IBlockState dirt = this.getBiomeSpecificBlockState(Blocks.DIRT.getDefaultState());

        // Foundation & Pillars
        this.fillWithBlocks(world, structureBoundingBox, 2, 1, 1, 8, 1, 13, cobble, cobble, false);
        this.fillWithBlocks(world, structureBoundingBox, 9, 1, 5, 16, 1, 13, cobble, cobble, false);

        this.fillWithBlocks(world, structureBoundingBox, 2, 2, 1, 2, 4, 1, cobble, cobble, false);
        this.fillWithBlocks(world, structureBoundingBox, 2, 2, 13, 2, 4, 13, cobble, cobble, false);
        this.fillWithBlocks(world, structureBoundingBox, 8, 2, 5, 8, 4, 5, cobble, cobble, false);
        this.fillWithBlocks(world, structureBoundingBox, 8, 2, 1, 8, 4, 1, cobble, cobble, false);
        this.fillWithBlocks(world, structureBoundingBox, 8, 2, 13, 8, 4, 13, cobble, cobble, false);
        this.fillWithBlocks(world, structureBoundingBox, 16, 2, 5, 16, 3, 5, cobble, cobble, false);
        this.fillWithBlocks(world, structureBoundingBox, 16, 2, 13, 16, 3, 13, cobble, cobble, false);

        // Floor & Pen
        this.fillWithBlocks(world, structureBoundingBox, 3, 1, 2, 7, 1, 12, planks, planks, false);
        this.fillWithBlocks(world, structureBoundingBox, 8, 1, 6, 15, 1, 12, planks, planks, false);
        this.fillWithBlocks(world, structureBoundingBox, 9, 1, 1, 16, 1, 4, dirt, dirt, false);
        this.fillWithBlocks(world, structureBoundingBox, 9, 2, 1, 16, 2, 4, fence, fence, false);
        this.fillWithBlocks(world, structureBoundingBox, 9, 2, 2, 15, 2, 4, Blocks.AIR.getDefaultState(), Blocks.AIR.getDefaultState(), false);


        // Front Walls
        //this.placeDoorAtCurrentPosition(world, structureBoundingBox, rand, 5, 2, 1, this.getMetadataWithOffset(Blocks.wooden_door, 1));
        this.createVillageDoor(world, structureBoundingBox, rand, 5, 2, 1, EnumFacing.SOUTH);
        this.setBlockState(world, stoneStairs, 5, 1, 0, structureBoundingBox);
        this.fillWithBlocks(world, structureBoundingBox, 3, 2, 1, 4, 2, 1, cobble, cobble, false);
        this.fillWithBlocks(world, structureBoundingBox, 6, 2, 1, 7, 2, 1, cobble, cobble, false);
        this.fillWithBlocks(world, structureBoundingBox, 3, 3, 1, 4, 4, 1, planks, planks, false);
        this.fillWithBlocks(world, structureBoundingBox, 6, 3, 1, 7, 4, 1, planks, planks, false);
        this.fillWithBlocks(world, structureBoundingBox, 4, 6, 1, 6, 6, 1, planks, planks, false);
        this.setBlockState(world, planks, 5, 4, 1, structureBoundingBox);
        this.setBlockState(world, planks, 3, 5, 1, structureBoundingBox);
        this.setBlockState(world, planks, 7, 5, 1, structureBoundingBox);
        this.setBlockState(world, log, 4, 5, 1, structureBoundingBox);
        this.setBlockState(world, log, 6, 5, 1, structureBoundingBox);
        this.setBlockState(world, Blocks.GLASS_PANE.getDefaultState(), 5, 5, 1, structureBoundingBox);

        //this.setBlockState(world, Blocks.fence_gate, getMetadataWithOffset(Blocks.fence_gate, 2), 12, 2, 5, structureBoundingBox);
        this.setBlockState(world, getBiomeSpecificBlockState(Blocks.OAK_FENCE_GATE.getDefaultState().withProperty(BlockStairs.FACING, EnumFacing.SOUTH)), 12, 2, 5, structureBoundingBox);
        this.fillWithBlocks(world, structureBoundingBox, 9, 2, 5, 11, 2, 5, cobble, cobble, false);
        this.fillWithBlocks(world, structureBoundingBox, 13, 2, 5, 15, 2, 5, cobble, cobble, false);
        this.fillWithBlocks(world, structureBoundingBox, 9, 3, 5, 10, 3, 5, planks, planks, false);
        this.fillWithBlocks(world, structureBoundingBox, 14, 3, 5, 15, 3, 5, planks, planks, false);
        this.setBlockState(world, log, 11, 3, 5, structureBoundingBox);
        this.setBlockState(world, log, 13, 3, 5, structureBoundingBox);
        this.setBlockState(world, Blocks.GLASS_PANE.getDefaultState(), 12, 3, 5, structureBoundingBox);

        // Long Wall
        this.fillWithBlocks(world, structureBoundingBox, 2, 2, 2, 2, 2, 12, cobble, cobble, false);
        this.fillWithBlocks(world, structureBoundingBox, 2, 3, 2, 2, 4, 2, planks, planks, false);
        this.fillWithBlocks(world, structureBoundingBox, 2, 3, 12, 2, 4, 12, planks, planks, false);
        this.fillWithBlocks(world, structureBoundingBox, 2, 4, 3, 2, 4, 5, planks, planks, false);
        this.fillWithBlocks(world, structureBoundingBox, 2, 4, 9, 2, 4, 11, planks, planks, false);
        this.fillWithBlocks(world, structureBoundingBox, 2, 3, 3, 2, 3, 5, log, log, false);
        this.fillWithBlocks(world, structureBoundingBox, 2, 3, 9, 2, 3, 11, log, log, false);
        this.setBlockState(world, Blocks.GLASS_PANE.getDefaultState(), 2, 3, 4, structureBoundingBox);
        this.setBlockState(world, Blocks.GLASS_PANE.getDefaultState(), 2, 3, 10, structureBoundingBox);
        for(int z = 6; z <= 8; z++) {
            this.setBlockState(world, stairs1, 2, 3, z, structureBoundingBox);
            this.setBlockState(world, stairs1, 2, 4, z, structureBoundingBox);
        }

        // Short Walls
        this.fillWithBlocks(world, structureBoundingBox, 8, 2, 2, 8, 2, 4, cobble, cobble, false);
        this.fillWithBlocks(world, structureBoundingBox, 16, 2, 6, 16, 2, 12, cobble, cobble, false);
        this.fillWithBlocks(world, structureBoundingBox, 8, 4, 2, 8, 4, 4, planks, planks, false);
        this.fillWithBlocks(world, structureBoundingBox, 16, 4, 6, 16, 4, 12, planks, planks, false);
        this.fillWithBlocks(world, structureBoundingBox, 16, 5, 7, 16, 5, 11, planks, planks, false);
        this.fillWithBlocks(world, structureBoundingBox, 16, 6, 8, 16, 6, 10, planks, planks, false);
        this.setBlockState(world, planks, 16, 3, 9, structureBoundingBox);
        this.fillWithBlocks(world, structureBoundingBox, 16, 3, 6, 16, 3, 8, log, log, false);
        this.fillWithBlocks(world, structureBoundingBox, 16, 3, 10, 16, 3, 12, log, log, false);
        this.fillWithBlocks(world, structureBoundingBox, 8, 3, 2, 8, 3, 4, Blocks.GLASS.getDefaultState(), log, false);
        this.setBlockState(world, Blocks.GLASS_PANE.getDefaultState(), 16, 3, 7, structureBoundingBox);
        this.setBlockState(world, Blocks.GLASS_PANE.getDefaultState(), 16, 3, 11, structureBoundingBox);

        // Back Wall
        this.fillWithBlocks(world, structureBoundingBox, 3, 2, 13, 7, 2, 13, cobble, cobble, false);
        this.fillWithBlocks(world, structureBoundingBox, 9, 2, 13, 15, 2, 13, cobble, cobble, false);
        this.fillWithBlocks(world, structureBoundingBox, 3, 3, 13, 3, 5, 13, planks, planks, false);
        this.fillWithBlocks(world, structureBoundingBox, 7, 3, 13, 7, 5, 13, planks, planks, false);
        this.fillWithBlocks(world, structureBoundingBox, 4, 6, 13, 6, 6, 13, planks, planks, false);
        this.fillWithBlocks(world, structureBoundingBox, 9, 3, 13, 10, 3, 13, planks, planks, false);
        this.fillWithBlocks(world, structureBoundingBox, 14, 3, 13, 15, 3, 13, planks, planks, false);
        for(int x = 4; x <= 6; x++) {
            this.setBlockState(world, stairs3, x, 3, 13, structureBoundingBox);
            this.setBlockState(world, stairs3, x, 4, 13, structureBoundingBox);
        }
        this.setBlockState(world, planks, 5, 4, 13, structureBoundingBox);
        this.setBlockState(world, log, 4, 5, 13, structureBoundingBox);
        this.setBlockState(world, log, 6, 5, 13, structureBoundingBox);
        this.setBlockState(world, log, 11, 3, 13, structureBoundingBox);
        this.setBlockState(world, log, 13, 3, 13, structureBoundingBox);
        this.setBlockState(world, Blocks.GLASS_PANE.getDefaultState(), 5, 5, 13, structureBoundingBox);
        this.setBlockState(world, Blocks.GLASS_PANE.getDefaultState(), 12, 3, 13, structureBoundingBox);

        // Main Room
        this.fillWithBlocks(world, structureBoundingBox, 4, 2, 11, 6, 2, 11, Blocks.DOUBLE_STONE_SLAB.getDefaultState(), Blocks.DOUBLE_STONE_SLAB.getDefaultState(), false);
        //this.placeBlockAtCurrentPosition(world, Blocks.torch, getMetadataWithOffset(Blocks.torch, 1), 5, 4, 12, structureBoundingBox);
        this.placeTorch(world, EnumFacing.SOUTH, 5, 4, 12, structureBoundingBox);
        //this.placeBlockAtCurrentPosition(world, Blocks.torch, getMetadataWithOffset(Blocks.torch, 2), 5, 4, 2, structureBoundingBox);
        this.placeTorch(world, EnumFacing.NORTH, 5, 4, 2, structureBoundingBox);
        this.placeTorch(world, EnumFacing.EAST, 3, 3, 5, structureBoundingBox);
        this.placeTorch(world, EnumFacing.EAST, 3, 3, 9, structureBoundingBox);
        this.fillWithBlocks(world, structureBoundingBox, 8, 2, 10, 8, 2, 12, cobble, cobble, false);
        this.fillWithBlocks(world, structureBoundingBox, 8, 2, 6, 8, 2, 8, cobble, cobble, false);
        this.fillWithBlocks(world, structureBoundingBox, 8, 3, 11, 8, 4, 12, planks, planks, false);
        this.fillWithBlocks(world, structureBoundingBox, 8, 3, 6, 8, 4, 7, planks, planks, false);
        this.fillWithBlocks(world, structureBoundingBox, 8, 3, 8, 8, 4, 10, Blocks.GLASS.getDefaultState(), Blocks.GLASS.getDefaultState(), false);
        //this.placeDoorAtCurrentPosition(world, structureBoundingBox, rand, 8, 2, 9, this.getMetadataWithOffset(Blocks.wooden_door, 0));
        this.generateDoor(world, structureBoundingBox, rand, 8, 2, 9, EnumFacing.EAST, this.biomeDoor());

        // Pet Room
        this.fillWithBlocks(world, structureBoundingBox, 11, 2, 9, 15, 2, 9, fence, fence, false);
        this.fillWithBlocks(world, structureBoundingBox, 11, 2, 6, 11, 2, 12, fence, fence, false);
        this.placeTorch(world, EnumFacing.EAST, 9, 6, 9, structureBoundingBox);
        this.placeTorch(world, EnumFacing.WEST, 15, 5, 9, structureBoundingBox);
        //this.setBlockState(world, Blocks.fence_gate, getMetadataWithOffset(Blocks.fence_gate, 3), 11, 2, 7, structureBoundingBox);
        this.setBlockState(world, getBiomeSpecificBlockState(Blocks.OAK_FENCE_GATE.getDefaultState().withProperty(BlockStairs.FACING, EnumFacing.WEST)), 11, 2, 7, structureBoundingBox);
        //this.setBlockState(world, Blocks.fence_gate, getMetadataWithOffset(Blocks.fence_gate, 3), 11, 2, 11, structureBoundingBox);
        this.setBlockState(world, getBiomeSpecificBlockState(Blocks.OAK_FENCE_GATE.getDefaultState().withProperty(BlockStairs.FACING, EnumFacing.WEST)), 11, 2, 11, structureBoundingBox);
        for (int x = 14; x <= 15; x++) {
            for (int z = 6; z <= 7; z++) {
                this.setBlockState(world, Blocks.CARPET.getDefaultState().withProperty(BlockCarpet.COLOR, EnumDyeColor.LIGHT_BLUE), x, 2, z, structureBoundingBox);
            }
            for (int z = 11; z <= 12; z++) {
                this.setBlockState(world,Blocks.CARPET.getDefaultState().withProperty(BlockCarpet.COLOR, EnumDyeColor.LIGHT_BLUE), x, 2, z, structureBoundingBox);
            }
        }


        // Roof mayhem
        this.fillWithBlocks(world, structureBoundingBox, 7, 6, 8, 8, 6, 10, planks, planks, false);
        this.fillWithBlocks(world, structureBoundingBox, 8, 5, 7, 8, 5, 11, planks, planks, false);
        this.fillWithBlocks(world, structureBoundingBox, 5, 7, 1, 5, 7, 13, planks, planks, false);
        for (int z = 1; z <= 13; z++) {
            this.setBlockState(world, stairs0, 1, 4, z, structureBoundingBox);
            this.setBlockState(world, stairs0, 2, 5, z, structureBoundingBox);
            this.setBlockState(world, stairs0, 3, 6, z, structureBoundingBox);
            this.setBlockState(world, stairs0, 4, 7, z, structureBoundingBox);
        }
        this.fillWithBlocks(world, structureBoundingBox, 3, 5, 2, 3, 5, 12, planks, planks, false);
        this.fillWithBlocks(world, structureBoundingBox, 4, 6, 2, 4, 6, 12, planks, planks, false);
        this.fillWithBlocks(world, structureBoundingBox, 6, 6, 2, 6, 6, 12, planks, planks, false);
        this.fillWithBlocks(world, structureBoundingBox, 7, 5, 2, 7, 5, 12, planks, planks, false);
        for (int z = 1; z <= 5; z++) {
            this.setBlockState(world, stairs1, 9, 4, z, structureBoundingBox);
        }
        for (int z = 1; z <= 6; z++) {
            this.setBlockState(world, stairs1, 8, 5, z, structureBoundingBox);
        }
        for (int z = 1; z <= 7; z++) {
            this.setBlockState(world, stairs1, 7, 6, z, structureBoundingBox);
        }
        for (int z = 1; z <= 8; z++) {
            this.setBlockState(world, stairs1, 6, 7, z, structureBoundingBox);
        }
        for (int z = 10; z <= 13; z++) {
            this.setBlockState(world, stairs1, 6, 7, z, structureBoundingBox);
        }
        for (int z = 11; z <= 13; z++) {
            this.setBlockState(world, stairs1, 7, 6, z, structureBoundingBox);
        }
        for (int z = 12; z <= 13; z++) {
            this.setBlockState(world, stairs1, 8, 5, z, structureBoundingBox);
        }

        this.fillWithBlocks(world, structureBoundingBox, 6, 7, 9, 16, 7, 9, planks, planks, false);
        for(int x = 7; x <= 16; x++) {
            this.setBlockState(world, stairs3, x, 7, 8, structureBoundingBox);
        }
        for(int x = 8; x <= 16; x++) {
            this.setBlockState(world, stairs3, x, 6, 7, structureBoundingBox);
        }
        for(int x = 9; x <= 16; x++) {
            this.setBlockState(world, stairs3, x, 5, 6, structureBoundingBox);
        }
        for (int x = 10; x <= 16; x++) {
            this.setBlockState(world, stairs3, x, 4, 5, structureBoundingBox);
        }
        for (int x = 9; x <= 16; x++) {
            this.setBlockState(world, stairs2, x, 4, 13, structureBoundingBox);
        }
        for (int x = 9; x <= 16; x++) {
            this.setBlockState(world, stairs2, x, 5, 12, structureBoundingBox);
        }
        for (int x = 8; x <= 16; x++) {
            this.setBlockState(world, stairs2, x, 6, 11, structureBoundingBox);
        }
        for (int x = 7; x <= 16; x++) {
            this.setBlockState(world, stairs2, x, 7, 10, structureBoundingBox);
        }



        for (int l = 0; l < 7; ++l) {
            for (int i1 = 0; i1 < 7; ++i1) {
                this.clearCurrentPositionBlocksUpwards(world, i1, 9, l, structureBoundingBox);
                this.replaceAirAndLiquidDownwards(world, cobble, i1, -1, l, structureBoundingBox);
            }
        }
        this.spawnVillagers(world, structureBoundingBox, 2, 2, 2, 1);
        this.spawnCats(world, structureBoundingBox, 14, 2, 11);
        this.spawnDogs(world, structureBoundingBox, 14, 2, 7);

        return true;
    }
    
    @Override
    protected VillagerProfession chooseForgeProfession(int count, VillagerProfession prof) {
        return super.chooseForgeProfession(1, ModProfessions.SHELTER_STAFF);
    }

    private void spawnCats(World world, StructureBoundingBox structureBoundingBoxIn, int x, int y, int z) {
        int count = world.rand.nextInt(3) + 1;
        if (this.CATS_SPAWNED < count) {
            for (int i = this.CATS_SPAWNED; i < count; ++i) {
                int offX = this.getXWithOffset(x - i, z);
                int offY = this.getYWithOffset(y);
                int offZ = this.getZWithOffset(x - i, z);

                if (!structureBoundingBoxIn.isVecInside(new BlockPos(offX, offY, offZ))) {
                    break;
                }

                ++this.CATS_SPAWNED;

                if (world.rand.nextInt(4) == 0) {
                    EntityCat kitten = new EntityCat(world);
                    kitten.setGrowingAge(-24000);
                    kitten.setLocationAndAngles((double) offX + 0.5D, (double) offY, (double) offZ + 0.5D, 0.0F, 0.0F);
                    kitten.onInitialSpawn(world.getDifficultyForLocation(new BlockPos(kitten)), null);
                    kitten.enablePersistence();
                    world.spawnEntity(kitten);
                } else {
                    EntityCat cat = new EntityCat(world);
                    cat.setLocationAndAngles((double) offX + 0.5D, (double) offY, (double) offZ + 0.5D, 0.0F, 0.0F);
                    cat.onInitialSpawn(world.getDifficultyForLocation(new BlockPos(cat)), null);
                    cat.enablePersistence();
                    world.spawnEntity(cat);
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

                if (!structureBoundingBoxIn.isVecInside(new BlockPos(offX, offY, offZ))) {
                    break;
                }

                ++this.DOGS_SPAWNED;

                if (world.rand.nextInt(4) == 0) {
                    EntityWolf puppy = new EntityWolf(world);
                    puppy.setGrowingAge(-24000);
                    puppy.setLocationAndAngles((double) offX + 0.5D, (double) offY, (double) offZ + 0.5D, 0.0F, 0.0F);
                    puppy.onInitialSpawn(world.getDifficultyForLocation(new BlockPos(puppy)), null);
                    puppy.enablePersistence();
                    world.spawnEntity(puppy);
                } else {
                    EntityWolf dog = new EntityWolf(world);
                    dog.setLocationAndAngles((double) offX + 0.5D, (double) offY, (double) offZ + 0.5D, 0.0F, 0.0F);
                    dog.onInitialSpawn(world.getDifficultyForLocation(new BlockPos(dog)), null);
                    dog.enablePersistence();
                    world.spawnEntity(dog);
                }
            }
        }
    }
}