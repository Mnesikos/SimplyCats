package com.github.mnesikos.simplycats.worldgen.villages;

/*import com.github.mnesikos.simplycats.configuration.SimplyCatsConfig;
import com.github.mnesikos.simplycats.entity.EntityCat;
import com.github.mnesikos.simplycats.init.ModProfessions;
import net.minecraft.block.BlockCarpet;
import net.minecraft.block.BlockStairs;
import net.minecraft.block.state.IBlockState;
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
         *

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
        this.fillWithBlocks(world, structureBoundingBox, 2, 1, -1, 8, 1, 11, cobble, cobble, false);
        this.fillWithBlocks(world, structureBoundingBox, 9, 1, 3, 16, 1, 11, cobble, cobble, false);
        this.fillWithBlocks(world, structureBoundingBox, 2, 0, -1, 8, 0, 2, cobble, cobble, false);
        this.fillWithBlocks(world, structureBoundingBox, 2, 0, 3, 16, 0, 11, cobble, cobble, false);
        this.fillWithBlocks(world, structureBoundingBox, 2, -1, -1, 16, -1, 11, cobble, cobble, false);

        this.replaceAirAndLiquidDownwards(world, cobble, 2, -2, -1, structureBoundingBox);
        this.replaceAirAndLiquidDownwards(world, cobble, 8, -2, -1, structureBoundingBox);
        this.replaceAirAndLiquidDownwards(world, cobble, 16, -2, -1, structureBoundingBox);
        this.replaceAirAndLiquidDownwards(world, cobble, 16, -2, 3, structureBoundingBox);
        this.replaceAirAndLiquidDownwards(world, cobble, 16, -2, 11, structureBoundingBox);
        this.replaceAirAndLiquidDownwards(world, cobble, 8, -2, 11, structureBoundingBox);
        this.replaceAirAndLiquidDownwards(world, cobble, 2, -2, 11, structureBoundingBox);

        this.fillWithBlocks(world, structureBoundingBox, 2, 2, -1, 2, 4, -1, cobble, cobble, false);
        this.fillWithBlocks(world, structureBoundingBox, 2, 2, 11, 2, 4, 11, cobble, cobble, false);
        this.fillWithBlocks(world, structureBoundingBox, 8, 2, 3, 8, 4, 3, cobble, cobble, false);
        this.fillWithBlocks(world, structureBoundingBox, 8, 2, -1, 8, 4, -1, cobble, cobble, false);
        this.fillWithBlocks(world, structureBoundingBox, 8, 2, 11, 8, 4, 11, cobble, cobble, false);
        this.fillWithBlocks(world, structureBoundingBox, 16, 2, 3, 16, 3, 3, cobble, cobble, false);
        this.fillWithBlocks(world, structureBoundingBox, 16, 2, 11, 16, 4, 11, cobble, cobble, false);

        // Floor & Pen
        this.fillWithBlocks(world, structureBoundingBox, 3, 1, 0, 7, 1, 10, planks, planks, false);
        this.fillWithBlocks(world, structureBoundingBox, 8, 1, 4, 15, 1, 10, planks, planks, false);
        this.fillWithBlocks(world, structureBoundingBox, 9, 1, -1, 16, 1, 2, dirt, dirt, false);
        this.fillWithBlocks(world, structureBoundingBox, 9, 0, -1, 16, 0, 2, dirt, dirt, false);
        this.fillWithBlocks(world, structureBoundingBox, 9, 2, -1, 16, 2, 2, fence, Blocks.AIR.getDefaultState(), false);
        this.fillWithAir(world, structureBoundingBox, 9, 2, 0, 15, 2, 2);
        this.fillWithAir(world, structureBoundingBox, 9, 3, -1, 16, 7, 2);

        // Front Walls
        this.createVillageDoor(world, structureBoundingBox, rand, 5, 2, -1, EnumFacing.SOUTH);
        this.setBlockState(world, stoneStairs, 5, 1, -2, structureBoundingBox);
        this.fillWithBlocks(world, structureBoundingBox, 3, 2, -1, 4, 2, -1, cobble, cobble, false);
        this.fillWithBlocks(world, structureBoundingBox, 6, 2, -1, 7, 2, -1, cobble, cobble, false);
        this.fillWithBlocks(world, structureBoundingBox, 3, 3, -1, 4, 4, -1, planks, planks, false);
        this.fillWithBlocks(world, structureBoundingBox, 6, 3, -1, 7, 4, -1, planks, planks, false);
        this.fillWithBlocks(world, structureBoundingBox, 4, 6, -1, 6, 6, -1, planks, planks, false);
        this.setBlockState(world, planks, 5, 4, -1, structureBoundingBox);
        this.setBlockState(world, planks, 3, 5, -1, structureBoundingBox);
        this.setBlockState(world, planks, 7, 5, -1, structureBoundingBox);
        this.setBlockState(world, log, 4, 5, -1, structureBoundingBox);
        this.setBlockState(world, log, 6, 5, -1, structureBoundingBox);
        this.setBlockState(world, Blocks.GLASS_PANE.getDefaultState(), 5, 5, -1, structureBoundingBox);

        //this.setBlockState(world, Blocks.fence_gate, getMetadataWithOffset(Blocks.fence_gate, 2), 12, 2, 5, structureBoundingBox);
        this.setBlockState(world, getBiomeSpecificBlockState(Blocks.OAK_FENCE_GATE.getDefaultState().withProperty(BlockStairs.FACING, EnumFacing.SOUTH)), 12, 2, 3, structureBoundingBox);
        this.fillWithBlocks(world, structureBoundingBox, 9, 2, 3, 11, 2, 3, cobble, cobble, false);
        this.fillWithBlocks(world, structureBoundingBox, 13, 2, 3, 15, 2, 3, cobble, cobble, false);
        this.fillWithBlocks(world, structureBoundingBox, 9, 3, 3, 10, 3, 3, planks, planks, false);
        this.fillWithBlocks(world, structureBoundingBox, 14, 3, 3, 15, 3, 3, planks, planks, false);
        this.setBlockState(world, log, 11, 3, 3, structureBoundingBox);
        this.setBlockState(world, log, 13, 3, 3, structureBoundingBox);
        this.setBlockState(world, Blocks.GLASS_PANE.getDefaultState(), 12, 3, 3, structureBoundingBox);

        // Long Wall
        this.fillWithBlocks(world, structureBoundingBox, 2, 2, 0, 2, 2, 10, cobble, cobble, false);
        this.fillWithBlocks(world, structureBoundingBox, 2, 3, 0, 2, 4, 0, planks, planks, false);
        this.fillWithBlocks(world, structureBoundingBox, 2, 3, 10, 2, 4, 10, planks, planks, false);
        this.fillWithBlocks(world, structureBoundingBox, 2, 4, 1, 2, 4, 3, planks, planks, false);
        this.fillWithBlocks(world, structureBoundingBox, 2, 4, 7, 2, 4, 9, planks, planks, false);
        this.fillWithBlocks(world, structureBoundingBox, 2, 3, 1, 2, 3, 3, log, log, false);
        this.fillWithBlocks(world, structureBoundingBox, 2, 3, 7, 2, 3, 9, log, log, false);
        this.setBlockState(world, Blocks.GLASS_PANE.getDefaultState(), 2, 3, 2, structureBoundingBox);
        this.setBlockState(world, Blocks.GLASS_PANE.getDefaultState(), 2, 3, 8, structureBoundingBox);
        for(int z = 4; z <= 6; z++) {
            this.setBlockState(world, stairs1, 2, 3, z, structureBoundingBox);
            this.setBlockState(world, stairs1, 2, 4, z, structureBoundingBox);
        }

        // Short Walls
        this.fillWithBlocks(world, structureBoundingBox, 8, 2, 0, 8, 2, 2, cobble, cobble, false);
        this.fillWithBlocks(world, structureBoundingBox, 16, 2, 4, 16, 2, 10, cobble, cobble, false);
        this.fillWithBlocks(world, structureBoundingBox, 8, 4, 0, 8, 4, 2, planks, planks, false);
        this.fillWithBlocks(world, structureBoundingBox, 16, 4, 4, 16, 4, 10, planks, planks, false);
        this.fillWithBlocks(world, structureBoundingBox, 16, 5, 5, 16, 5, 9, planks, planks, false);
        this.fillWithBlocks(world, structureBoundingBox, 16, 6, 6, 16, 6, 8, planks, planks, false);
        this.setBlockState(world, planks, 16, 3, 7, structureBoundingBox);
        this.fillWithBlocks(world, structureBoundingBox, 16, 3, 4, 16, 3, 6, log, log, false);
        this.fillWithBlocks(world, structureBoundingBox, 16, 3, 8, 16, 3, 10, log, log, false);
        this.fillWithBlocks(world, structureBoundingBox, 8, 3, 0, 8, 3, 2, Blocks.GLASS.getDefaultState(), log, false);
        this.setBlockState(world, Blocks.GLASS_PANE.getDefaultState(), 16, 3, 5, structureBoundingBox);
        this.setBlockState(world, Blocks.GLASS_PANE.getDefaultState(), 16, 3, 9, structureBoundingBox);

        // Back Wall
        this.fillWithBlocks(world, structureBoundingBox, 3, 2, 11, 7, 2, 11, cobble, cobble, false);
        this.fillWithBlocks(world, structureBoundingBox, 9, 2, 11, 15, 2, 11, cobble, cobble, false);
        this.fillWithBlocks(world, structureBoundingBox, 3, 3, 11, 3, 5, 11, planks, planks, false);
        this.fillWithBlocks(world, structureBoundingBox, 7, 3, 11, 7, 5, 11, planks, planks, false);
        this.fillWithBlocks(world, structureBoundingBox, 4, 6, 11, 6, 6, 11, planks, planks, false);
        this.fillWithBlocks(world, structureBoundingBox, 9, 3, 11, 10, 3, 11, planks, planks, false);
        this.fillWithBlocks(world, structureBoundingBox, 14, 3, 11, 15, 3, 11, planks, planks, false);
        for(int x = 4; x <= 6; x++) {
            this.setBlockState(world, stairs3, x, 3, 11, structureBoundingBox);
            this.setBlockState(world, stairs3, x, 4, 11, structureBoundingBox);
        }
        this.setBlockState(world, planks, 5, 4, 11, structureBoundingBox);
        this.setBlockState(world, log, 4, 5, 11, structureBoundingBox);
        this.setBlockState(world, log, 6, 5, 11, structureBoundingBox);
        this.setBlockState(world, log, 11, 3, 11, structureBoundingBox);
        this.setBlockState(world, log, 13, 3, 11, structureBoundingBox);
        this.setBlockState(world, Blocks.GLASS_PANE.getDefaultState(), 5, 5, 11, structureBoundingBox);
        this.setBlockState(world, Blocks.GLASS_PANE.getDefaultState(), 12, 3, 11, structureBoundingBox);

        // Main Room
        this.fillWithAir(world, structureBoundingBox, 3, 2, 0, 7, 7, 10);
        this.fillWithBlocks(world, structureBoundingBox, 4, 2, 9, 6, 2, 9, Blocks.DOUBLE_STONE_SLAB.getDefaultState(), Blocks.DOUBLE_STONE_SLAB.getDefaultState(), false);
        this.placeTorch(world, EnumFacing.SOUTH, 5, 4, 10, structureBoundingBox);
        this.placeTorch(world, EnumFacing.NORTH, 5, 4, 0, structureBoundingBox);
        this.placeTorch(world, EnumFacing.EAST, 3, 3, 3, structureBoundingBox);
        this.placeTorch(world, EnumFacing.EAST, 3, 3, 7, structureBoundingBox);
        this.fillWithBlocks(world, structureBoundingBox, 8, 2, 8, 8, 2, 10, cobble, cobble, false);
        this.fillWithBlocks(world, structureBoundingBox, 8, 2, 4, 8, 2, 6, cobble, cobble, false);
        this.fillWithBlocks(world, structureBoundingBox, 8, 3, 9, 8, 4, 10, planks, planks, false);
        this.fillWithBlocks(world, structureBoundingBox, 8, 3, 4, 8, 4, 5, planks, planks, false);
        this.fillWithBlocks(world, structureBoundingBox, 8, 3, 6, 8, 4, 8, Blocks.GLASS.getDefaultState(), Blocks.GLASS.getDefaultState(), false);
        this.generateDoor(world, structureBoundingBox, rand, 8, 2, 7, EnumFacing.EAST, this.biomeDoor());

        // Pet Room
        this.fillWithAir(world, structureBoundingBox, 9, 2, 4, 15, 7, 10);
        this.fillWithBlocks(world, structureBoundingBox, 11, 2, 7, 15, 2, 7, fence, fence, false);
        this.fillWithBlocks(world, structureBoundingBox, 11, 2, 4, 11, 2, 10, fence, fence, false);
        this.placeTorch(world, EnumFacing.EAST, 9, 5, 7, structureBoundingBox);
        this.placeTorch(world, EnumFacing.WEST, 15, 5, 7, structureBoundingBox);
        this.setBlockState(world, getBiomeSpecificBlockState(Blocks.OAK_FENCE_GATE.getDefaultState().withProperty(BlockStairs.FACING, EnumFacing.WEST)), 11, 2, 5, structureBoundingBox);
        this.setBlockState(world, getBiomeSpecificBlockState(Blocks.OAK_FENCE_GATE.getDefaultState().withProperty(BlockStairs.FACING, EnumFacing.WEST)), 11, 2, 9, structureBoundingBox);
        for (int x = 14; x <= 15; x++) {
            for (int z = 4; z <= 5; z++) {
                this.setBlockState(world, Blocks.CARPET.getDefaultState().withProperty(BlockCarpet.COLOR, EnumDyeColor.LIGHT_BLUE), x, 2, z, structureBoundingBox);
            }
            for (int z = 9; z <= 10; z++) {
                this.setBlockState(world,Blocks.CARPET.getDefaultState().withProperty(BlockCarpet.COLOR, EnumDyeColor.LIGHT_BLUE), x, 2, z, structureBoundingBox);
            }
        }

        // Roof mayhem
        this.fillWithBlocks(world, structureBoundingBox, 7, 6, 6, 8, 6, 8, planks, planks, false);
        this.fillWithBlocks(world, structureBoundingBox, 8, 5, 5, 8, 5, 9, planks, planks, false);
        this.fillWithBlocks(world, structureBoundingBox, 5, 7, -1, 5, 7, 11, planks, planks, false);
        for (int z = -1; z <= 11; z++) {
            this.setBlockState(world, stairs0, 1, 4, z, structureBoundingBox);
            this.setBlockState(world, stairs0, 2, 5, z, structureBoundingBox);
            this.setBlockState(world, stairs0, 3, 6, z, structureBoundingBox);
            this.setBlockState(world, stairs0, 4, 7, z, structureBoundingBox);
        }
        this.fillWithBlocks(world, structureBoundingBox, 3, 5, 0, 3, 5, 10, planks, planks, false);
        this.fillWithBlocks(world, structureBoundingBox, 4, 6, 0, 4, 6, 10, planks, planks, false);
        this.fillWithBlocks(world, structureBoundingBox, 6, 6, 0, 6, 6, 10, planks, planks, false);
        this.fillWithBlocks(world, structureBoundingBox, 7, 5, 0, 7, 5, 10, planks, planks, false);
        for (int z = -1; z <= 3; z++) {
            this.setBlockState(world, stairs1, 9, 4, z, structureBoundingBox);
        }
        for (int z = -1; z <= 4; z++) {
            this.setBlockState(world, stairs1, 8, 5, z, structureBoundingBox);
        }
        for (int z = -1; z <= 5; z++) {
            this.setBlockState(world, stairs1, 7, 6, z, structureBoundingBox);
        }
        for (int z = -1; z <= 6; z++) {
            this.setBlockState(world, stairs1, 6, 7, z, structureBoundingBox);
        }
        for (int z = 8; z <= 11; z++) {
            this.setBlockState(world, stairs1, 6, 7, z, structureBoundingBox);
        }
        for (int z = 9; z <= 11; z++) {
            this.setBlockState(world, stairs1, 7, 6, z, structureBoundingBox);
        }
        for (int z = 10; z <= 11; z++) {
            this.setBlockState(world, stairs1, 8, 5, z, structureBoundingBox);
        }

        this.fillWithBlocks(world, structureBoundingBox, 6, 7, 7, 16, 7, 7, planks, planks, false);
        for(int x = 7; x <= 16; x++) {
            this.setBlockState(world, stairs3, x, 7, 6, structureBoundingBox);
        }
        for(int x = 8; x <= 16; x++) {
            this.setBlockState(world, stairs3, x, 6, 5, structureBoundingBox);
        }
        for(int x = 9; x <= 16; x++) {
            this.setBlockState(world, stairs3, x, 5, 4, structureBoundingBox);
        }
        for (int x = 10; x <= 16; x++) {
            this.setBlockState(world, stairs3, x, 4, 3, structureBoundingBox);
        }
        for (int x = 9; x <= 16; x++) {
            this.setBlockState(world, stairs2, x, 4, 11, structureBoundingBox);
        }
        for (int x = 9; x <= 16; x++) {
            this.setBlockState(world, stairs2, x, 5, 10, structureBoundingBox);
        }
        for (int x = 8; x <= 16; x++) {
            this.setBlockState(world, stairs2, x, 6, 9, structureBoundingBox);
        }
        for (int x = 7; x <= 16; x++) {
            this.setBlockState(world, stairs2, x, 7, 8, structureBoundingBox);
        }


        this.spawnVillagers(world, structureBoundingBox, 5, 2, 10, 1);
        this.spawnCats(world, structureBoundingBox, 14, 2, 9);
        this.spawnDogs(world, structureBoundingBox, 14, 2, 5);

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
                    kitten.setGrowingAge(-SimplyCatsConfig.KITTEN_MATURE_TIMER);
                    kitten.setLocationAndAngles((double) offX + 0.5D, (double) offY, (double) offZ + 0.5D, 0.0F, 0.0F);
                    kitten.setFixed((byte)1);
                    kitten.onInitialSpawn(world.getDifficultyForLocation(new BlockPos(kitten)), null);
                    kitten.enablePersistence();
                    world.spawnEntity(kitten);
                } else {
                    EntityCat cat = new EntityCat(world);
                    cat.setLocationAndAngles((double) offX + 0.5D, (double) offY, (double) offZ + 0.5D, 0.0F, 0.0F);
                    cat.setFixed((byte)1);
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
}*/