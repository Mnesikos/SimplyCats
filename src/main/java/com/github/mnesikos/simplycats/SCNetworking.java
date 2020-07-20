package com.github.mnesikos.simplycats;

import io.netty.buffer.ByteBuf;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.fml.common.network.NetworkRegistry;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import net.minecraftforge.fml.common.network.simpleimpl.SimpleNetworkWrapper;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import static com.github.mnesikos.simplycats.Ref.MODID;

public class SCNetworking implements IMessage {
    public static final SimpleNetworkWrapper CHANNEL = NetworkRegistry.INSTANCE.newSimpleChannel(MODID + ":network");
    int count;

    public SCNetworking() {
    }

    public SCNetworking(int i) {
        this.count = i;
    }

    @Override
    public void fromBytes(ByteBuf buf) {
        this.count = buf.readInt();
    }

    @Override
    public void toBytes(ByteBuf buf) {
        buf.writeInt(count);
    }

    public IMessage someMethodIDontUnderstandYet(MessageContext msg) {
        getPlayer().getEntityData().setInteger("CatCount", count);
        return null;
    }

    @SideOnly(Side.CLIENT)
    public static EntityPlayer getPlayer() {
        return Minecraft.getMinecraft().player;
    }
}
