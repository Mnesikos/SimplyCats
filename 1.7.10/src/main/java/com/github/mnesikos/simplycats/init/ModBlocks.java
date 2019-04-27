package com.github.mnesikos.simplycats.init;

import com.github.mnesikos.simplycats.block.BlockBowl;
import net.minecraft.block.Block;

public final class ModBlocks {
	public static Block BOWL;
	
    public static final void init() {
    	BOWL = new BlockBowl();
    }
}