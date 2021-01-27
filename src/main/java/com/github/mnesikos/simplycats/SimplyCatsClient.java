package com.github.mnesikos.simplycats;

import com.github.mnesikos.simplycats.client.render.entity.RenderCat;
import net.minecraft.client.Minecraft;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.fml.client.registry.RenderingRegistry;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;

@Mod.EventBusSubscriber(modid = Ref.MOD_ID, value = Dist.CLIENT, bus = Mod.EventBusSubscriber.Bus.MOD)
public class SimplyCatsClient {
    public static void setup(final FMLClientSetupEvent event) {
        RenderingRegistry.registerEntityRenderingHandler(SimplyCats.CAT, RenderCat::new);
    }
}
