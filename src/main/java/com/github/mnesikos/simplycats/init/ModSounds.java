package com.github.mnesikos.simplycats.init;

import com.github.mnesikos.simplycats.Ref;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundEvent;

public final class ModSounds {
    public static SoundEvent SHAKE_TREATS = registerSound("shake_treats");

    private static SoundEvent registerSound(final String name) {
        final ResourceLocation resourceLocation = new ResourceLocation(Ref.MODID, name);
        return new SoundEvent(resourceLocation).setRegistryName(resourceLocation);
    }
}
