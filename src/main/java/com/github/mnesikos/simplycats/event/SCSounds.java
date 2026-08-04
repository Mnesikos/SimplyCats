package com.github.mnesikos.simplycats.event;

import com.github.mnesikos.simplycats.SimplyCats;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.minecraft.core.registries.Registries;
import net.neoforged.neoforge.registries.DeferredHolder;

public class SCSounds {
    public static final DeferredRegister<SoundEvent> REGISTRAR = DeferredRegister.create(Registries.SOUND_EVENT, SimplyCats.MOD_ID);

    public static DeferredHolder<SoundEvent, SoundEvent> SHAKE_TREATS = registerSound("shake_treats");

    private static DeferredHolder<SoundEvent, SoundEvent> registerSound(String name) {
        return REGISTRAR.register(name, () -> SoundEvent.createVariableRangeEvent(ResourceLocation.fromNamespaceAndPath(SimplyCats.MOD_ID, name)));
    }
}
