package com.github.mnesikos.simplycats;

import com.github.mnesikos.simplycats.configuration.SimplyCatsConfig;
import com.github.mnesikos.simplycats.entity.EntityCat;
import com.github.mnesikos.simplycats.init.ModBlocks;
import com.github.mnesikos.simplycats.init.ModItems;
import com.github.mnesikos.simplycats.network.INetworkPacket;
import com.github.mnesikos.simplycats.network.SetCatCountPacket;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.EntityClassification;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.LogicalSide;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.fml.network.NetworkDirection;
import net.minecraftforge.fml.network.NetworkEvent;
import net.minecraftforge.fml.network.simple.SimpleChannel;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.Optional;
import java.util.function.Supplier;

import static com.github.mnesikos.simplycats.Ref.MOD_ID;

@Mod(MOD_ID)
public class SimplyCats {
    public static final DeferredRegister<EntityType<?>> ENTITIES = new DeferredRegister<>(ForgeRegistries.ENTITIES, MOD_ID);
    public static final RegistryObject<EntityType<EntityCat>> CAT = createCatEntity("cat");

    public static final ItemGroup GROUP = new ItemGroup(MOD_ID + ".tab") {
        @Override
        public ItemStack createIcon() {
            return new ItemStack(ModItems.PET_CARRIER.get());
        }
    };

    public static final SimpleChannel CHANNEL = INetworkPacket.makeChannel("network", "1");
    private static int currentNetworkId;

    public SimplyCats() {
        IEventBus bus = FMLJavaModLoadingContext.get().getModEventBus();
        ModLoadingContext.get().registerConfig(ModConfig.Type.COMMON, SimplyCatsConfig.COMMON_CONFIG);
        ModBlocks.REGISTER.register(bus);
        ModItems.REGISTER.register(bus);
        ModBlocks.TILE_ENTITY.register(bus);
        ENTITIES.register(bus);
        registerMessage(SetCatCountPacket.class, SetCatCountPacket::new, LogicalSide.CLIENT);
        bus.addListener(SimplyCatsClient::setup);
    }

    private static RegistryObject<EntityType<EntityCat>> createCatEntity(String name) {
        return ENTITIES.register(name, () -> EntityType.Builder.create(EntityCat::new, EntityClassification.CREATURE)
                .size(0.6f, 0.8f)
                .setShouldReceiveVelocityUpdates(true).setTrackingRange(80).setUpdateInterval(1)
                .build(MOD_ID + "." + name));
    }

    private <T extends INetworkPacket> void registerMessage(Class<T> message, Supplier<T> supplier, LogicalSide side) {
        CHANNEL.registerMessage(currentNetworkId++, message, INetworkPacket::write, packetBuffer -> {
            T msg = supplier.get();
            msg.read(packetBuffer);
            return msg;
        }, (msg, contextSupplier) -> {
            NetworkEvent.Context context = contextSupplier.get();
            context.enqueueWork(() -> msg.handle(context.getDirection().getOriginationSide().isServer() ? getClientPlayer() : context.getSender()));
            context.setPacketHandled(true);
        }, Optional.of(side.isClient() ? NetworkDirection.PLAY_TO_CLIENT : NetworkDirection.PLAY_TO_SERVER));
    }

    @OnlyIn(Dist.CLIENT)
    public static PlayerEntity getClientPlayer() {
        return Minecraft.getInstance().player;
    }
}
