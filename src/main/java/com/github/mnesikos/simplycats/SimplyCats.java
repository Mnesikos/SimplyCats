package com.github.mnesikos.simplycats;

import com.github.mnesikos.simplycats.worldgen.SCWorldGen;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.server.FMLServerAboutToStartEvent;

@Mod(SimplyCats.MOD_ID)
public class SimplyCats {
    public static final String MOD_ID = "simplycats";

    public SimplyCats() {
        IEventBus bus = MinecraftForge.EVENT_BUS;
        bus.addListener(SimplyCats::setupVillages);
    }

    public static void setupVillages(FMLServerAboutToStartEvent event) {
        SCWorldGen.setupVillageWorldGen(event.getServer().registryAccess());
    }
}