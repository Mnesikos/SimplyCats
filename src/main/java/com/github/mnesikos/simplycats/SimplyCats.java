package com.github.mnesikos.simplycats;

import com.github.mnesikos.simplycats.block.SCBlocks;
import com.github.mnesikos.simplycats.client.color.ColorEvents;
import com.github.mnesikos.simplycats.configuration.SCConfig;
import com.github.mnesikos.simplycats.entity.SimplyCatEntity;
import com.github.mnesikos.simplycats.event.SCSounds;
import com.github.mnesikos.simplycats.item.SCItems;
import com.github.mnesikos.simplycats.worldgen.villages.SCVillagers;
import com.github.mnesikos.simplycats.worldgen.villages.SCWorldGen;
import net.minecraft.core.registries.Registries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.CreativeModeTabs;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.fml.loading.FMLEnvironment;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

@Mod(SimplyCats.MOD_ID)
public class SimplyCats {
    public static final String MOD_ID = "simplycats";
    public static final DeferredRegister<CreativeModeTab> CREATIVE_MODE_TABS = DeferredRegister.create(Registries.CREATIVE_MODE_TAB, SimplyCats.MOD_ID);
    public static final RegistryObject<CreativeModeTab> ITEM_GROUP = CREATIVE_MODE_TABS.register(MOD_ID + ".tab", () -> CreativeModeTab.builder()
            .withTabsBefore(CreativeModeTabs.SPAWN_EGGS)
            .title(Component.translatable("itemGroup." + MOD_ID + ".tab"))
            .icon(() -> SCItems.PET_CARRIER.get().getDefaultInstance())
            .displayItems((parameters, output) -> SCItems.REGISTRAR.getEntries().forEach(item -> {
                ItemStack catCarrier = new ItemStack(SCItems.PET_CARRIER.get(), 1, new CompoundTag());
                catCarrier.setDamageValue(3);
                output.accept(catCarrier);
                output.accept(item.get());
            })).build());

    public static final DeferredRegister<EntityType<?>> ENTITIES = DeferredRegister.create(ForgeRegistries.ENTITY_TYPES, SimplyCats.MOD_ID);
    public static final RegistryObject<EntityType<SimplyCatEntity>> CAT = ENTITIES.register("cat", () -> EntityType.Builder.of(SimplyCatEntity::new, MobCategory.CREATURE)
            .sized(0.6f, 0.8f)
            .setShouldReceiveVelocityUpdates(true).setTrackingRange(80).setUpdateInterval(1)
            .build("cat"));

    public SimplyCats() {
        IEventBus modBus = FMLJavaModLoadingContext.get().getModEventBus();
        IEventBus forgeBus = MinecraftForge.EVENT_BUS;

        ModLoadingContext.get().registerConfig(ModConfig.Type.COMMON, SCConfig.SPEC);

        ENTITIES.register(modBus);
        SCItems.REGISTRAR.register(modBus);
        SCBlocks.REGISTRAR.register(modBus);
        CREATIVE_MODE_TABS.register(modBus);
        SCSounds.REGISTRAR.register(modBus);
        SCVillagers.PROFESSIONS.register(modBus);
        SCVillagers.POI_TYPES.register(modBus);

        forgeBus.addListener(SCWorldGen::setupVillageWorldGen);

        if (FMLEnvironment.dist == Dist.CLIENT) {
            modBus.addListener(ColorEvents::registerColorHandlerBlocks);
        }

        forgeBus.register(CatDataFixer.class);
    }
}