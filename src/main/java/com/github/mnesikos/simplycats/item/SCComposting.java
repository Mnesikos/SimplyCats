package com.github.mnesikos.simplycats.item;

import net.minecraft.world.level.block.ComposterBlock;

public class SCComposting {
    public static void registerCompostables() {
        ComposterBlock.COMPOSTABLES.put(SCItems.CATNIP_SEEDS.get(), 0.3F);
        ComposterBlock.COMPOSTABLES.put(SCItems.CATNIP.get(), 0.65F);
    }
}
