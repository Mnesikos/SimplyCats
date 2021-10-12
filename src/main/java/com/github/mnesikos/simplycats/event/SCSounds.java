package com.github.mnesikos.simplycats.event;

import com.github.mnesikos.simplycats.SimplyCats;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundEvent;

public class SCSounds {
    public static SoundEvent SHAKE_TREATS = registerSound("shake_treats");

    private static SoundEvent registerSound(final String name) {
        final ResourceLocation resourceLocation = new ResourceLocation(SimplyCats.MOD_ID, name);
        return new SoundEvent(resourceLocation).setRegistryName(resourceLocation);
    }
}
