package com.github.mnesikos.simplycats.event;

import com.github.mnesikos.simplycats.SimplyCats;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class SCSounds {
    public static final DeferredRegister<SoundEvent> REGISTRAR = DeferredRegister.create(ForgeRegistries.SOUND_EVENTS, SimplyCats.MOD_ID);

    public static RegistryObject<SoundEvent> SHAKE_TREATS = registerSound("shake_treats");

    private static RegistryObject<SoundEvent> registerSound(String name) {
        return REGISTRAR.register(name, () -> SoundEvent.createVariableRangeEvent(new ResourceLocation(SimplyCats.MOD_ID, name)));
    }
}
