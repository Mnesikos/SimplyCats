package com.github.mnesikos.simplycats.client.color;

import com.github.mnesikos.simplycats.block.SCBlocks;
import net.minecraft.client.renderer.BiomeColors;
import net.minecraft.world.item.DyeColor;
import net.minecraftforge.client.event.RegisterColorHandlersEvent;

public class ColorEvents {
    public static void registerColorHandlerBlocks(final RegisterColorHandlersEvent.Block event) {
        event.getBlockColors().register((state, reader, pos, color) -> reader != null && pos != null ? BiomeColors.getAverageWaterColor(reader, pos) : -1,
                SCBlocks.CAT_BOWLS.get(DyeColor.WHITE).get(), SCBlocks.CAT_BOWLS.get(DyeColor.ORANGE).get(),
                SCBlocks.CAT_BOWLS.get(DyeColor.MAGENTA).get(), SCBlocks.CAT_BOWLS.get(DyeColor.LIGHT_BLUE).get(),
                SCBlocks.CAT_BOWLS.get(DyeColor.YELLOW).get(), SCBlocks.CAT_BOWLS.get(DyeColor.LIME).get(),
                SCBlocks.CAT_BOWLS.get(DyeColor.PINK).get(), SCBlocks.CAT_BOWLS.get(DyeColor.GRAY).get(),
                SCBlocks.CAT_BOWLS.get(DyeColor.LIGHT_GRAY).get(), SCBlocks.CAT_BOWLS.get(DyeColor.CYAN).get(),
                SCBlocks.CAT_BOWLS.get(DyeColor.PURPLE).get(), SCBlocks.CAT_BOWLS.get(DyeColor.BLUE).get(),
                SCBlocks.CAT_BOWLS.get(DyeColor.BROWN).get(), SCBlocks.CAT_BOWLS.get(DyeColor.GREEN).get(),
                SCBlocks.CAT_BOWLS.get(DyeColor.RED).get(), SCBlocks.CAT_BOWLS.get(DyeColor.BLACK).get()
        );
    }
}
