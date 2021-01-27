package com.github.mnesikos.simplycats.init;

import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundEvent;

import static com.github.mnesikos.simplycats.Ref.MOD_ID;

public class ModSounds {
    public static SoundEvent SHAKE_TREATS = registerSound("shake_treats");

    private static SoundEvent registerSound(final String name) {
        final ResourceLocation resourceLocation = new ResourceLocation(MOD_ID, name);
        return new SoundEvent(resourceLocation).setRegistryName(resourceLocation);
    }
}
