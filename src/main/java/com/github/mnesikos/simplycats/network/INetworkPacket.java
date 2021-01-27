package com.github.mnesikos.simplycats.network;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.network.NetworkRegistry;
import net.minecraftforge.fml.network.simple.SimpleChannel;

import static com.github.mnesikos.simplycats.Ref.MOD_ID;

public interface INetworkPacket {
    static SimpleChannel makeChannel(String name, String version) {
        return NetworkRegistry.newSimpleChannel(new ResourceLocation(MOD_ID, name), () -> version, version::equals, version::equals);
    }

    default void read(PacketBuffer buffer) {}
    default void write(PacketBuffer buffer) {}
    void handle(PlayerEntity player);
}
