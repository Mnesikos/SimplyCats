package com.github.mnesikos.simplycats.network;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.network.PacketBuffer;

public class SetCatCountPacket implements INetworkPacket {
    int count;

    public SetCatCountPacket() {
    }

    public SetCatCountPacket(int count) {
        this.count = count;
    }

    @Override
    public void read(PacketBuffer buf) {
        count = buf.readVarInt();
    }

    @Override
    public void write(PacketBuffer buf) {
        buf.writeVarInt(count);
    }

    @Override
    public void handle(PlayerEntity player) {
        player.getPersistentData().putInt("CatCount", count);
    }
}
