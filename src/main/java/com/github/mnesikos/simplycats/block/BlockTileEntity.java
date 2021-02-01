package com.github.mnesikos.simplycats.block;

import net.minecraft.block.BlockState;
import net.minecraft.block.material.Material;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockReader;

import javax.annotation.Nullable;

//This is unused and should probably be removed, especially since there is already a vanilla type that does the same thing, but i've gone and cleaned it up regardless
public abstract class BlockTileEntity<TE extends TileEntity> extends BlockBase {
    private final TileEntityType<TE> type;

    public BlockTileEntity(Material material, TileEntityType<TE> type, String name) {
        super(material, name);
        this.type = type;
    }

    public TE getTileEntity(IBlockReader world, BlockPos pos) {
        return type.func_226986_a_(world, pos);
    }

    @Override
    public boolean hasTileEntity(BlockState state) {
        return true;
    }

    @Nullable
    @Override
    public TE createTileEntity(BlockState state, IBlockReader world) {
        return type.create();
    }
}
