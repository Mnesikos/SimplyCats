package com.github.mnesikos.simplycats;

import com.github.mnesikos.simplycats.block.SCBlocks;
import com.github.mnesikos.simplycats.client.color.ColorEvents;
import com.github.mnesikos.simplycats.client.model.entity.SimplyCatModel;
import com.github.mnesikos.simplycats.client.render.entity.SimplyCatRenderer;
import com.github.mnesikos.simplycats.configuration.SCConfig;
import com.github.mnesikos.simplycats.data.*;
import com.github.mnesikos.simplycats.entity.SimplyCatEntity;
import com.github.mnesikos.simplycats.event.SCEvents;
import com.github.mnesikos.simplycats.event.SCSounds;
import com.github.mnesikos.simplycats.item.PetCarrierItem;
import com.github.mnesikos.simplycats.item.SCComposting;
import com.github.mnesikos.simplycats.item.SCItems;
import com.github.mnesikos.simplycats.worldgen.villages.SCVillagers;
import com.github.mnesikos.simplycats.worldgen.villages.SCWorldGen;
import net.minecraft.client.renderer.entity.EntityRenderers;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.PackOutput;
import net.minecraft.data.loot.LootTableProvider;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.minecraft.world.entity.SpawnPlacementTypes;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.CreativeModeTabs;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSets;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import net.neoforged.neoforge.client.event.EntityRenderersEvent;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.data.event.GatherDataEvent;
import net.neoforged.neoforge.event.entity.EntityAttributeCreationEvent;
import net.neoforged.neoforge.event.entity.RegisterSpawnPlacementsEvent;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.config.ModConfig;
import net.neoforged.fml.event.lifecycle.FMLClientSetupEvent;
import net.neoforged.fml.event.lifecycle.FMLCommonSetupEvent;
import net.neoforged.fml.loading.FMLEnvironment;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.neoforged.neoforge.registries.DeferredHolder;

import java.util.Collections;
import java.util.List;

@Mod(SimplyCats.MOD_ID)
public class SimplyCats {
    public static final String MOD_ID = "simplycats";
    public static final DeferredRegister<CreativeModeTab> CREATIVE_MODE_TABS = DeferredRegister.create(Registries.CREATIVE_MODE_TAB, SimplyCats.MOD_ID);
    public static final DeferredHolder<CreativeModeTab, CreativeModeTab> ITEM_GROUP = CREATIVE_MODE_TABS.register(MOD_ID + ".tab", () -> CreativeModeTab.builder()
            .withTabsBefore(CreativeModeTabs.SPAWN_EGGS)
            .title(Component.translatable("itemGroup." + MOD_ID + ".tab"))
            .icon(() -> SCItems.PET_CARRIER.get().getDefaultInstance())
            .displayItems((parameters, output) -> {
                ItemStack catCarrier = new ItemStack(SCItems.PET_CARRIER.get());
                PetCarrierItem.setCarrierType(catCarrier, 3);
                output.accept(catCarrier);
                SCItems.REGISTRAR.getEntries().forEach(item -> output.accept(item.get()));
            }).build());

    public static final DeferredRegister<EntityType<?>> ENTITIES = DeferredRegister.create(Registries.ENTITY_TYPE, SimplyCats.MOD_ID);
    public static final DeferredHolder<EntityType<?>, EntityType<SimplyCatEntity>> CAT = ENTITIES.register("cat", () -> EntityType.Builder.of(SimplyCatEntity::new, MobCategory.CREATURE)
            .sized(0.6f, 0.8f)
            .setShouldReceiveVelocityUpdates(true).setTrackingRange(80).setUpdateInterval(1)
            .build("cat"));

    public SimplyCats(IEventBus bus, ModContainer modContainer) {
        modContainer.registerConfig(ModConfig.Type.COMMON, SCConfig.SPEC);

        ENTITIES.register(bus);
        SCBlocks.REGISTRAR.register(bus);
        SCItems.REGISTRAR.register(bus);
        CREATIVE_MODE_TABS.register(bus);
        SCSounds.REGISTRAR.register(bus);
        SCVillagers.POI_TYPES.register(bus);
        SCVillagers.PROFESSIONS.register(bus);

        bus.addListener(this::setup);
        bus.addListener(this::registerSpawnPlacements);
        bus.addListener(this::registerAttributes);
        bus.addListener(this::gatherData);

        NeoForge.EVENT_BUS.addListener(SCWorldGen::setupVillageWorldGen);
        NeoForge.EVENT_BUS.addListener(SCEvents::onLivingChangeTargetEvent);

        bus.addListener(this::setupClient);

        if (FMLEnvironment.dist == Dist.CLIENT) {
            bus.addListener(this::registerLayerDefinitions);
            bus.addListener(ColorEvents::registerColorHandlerBlocks);
        }
    }

    public void setup(final FMLCommonSetupEvent event) {
        event.enqueueWork(() -> {
            SCVillagers.registerTrades();
            SCComposting.registerCompostables();
        });
    }

    private void setupClient(final FMLClientSetupEvent event) {
        EntityRenderers.register(SimplyCats.CAT.get(), SimplyCatRenderer::new);
        SCBlocks.setRenderLayers();
    }

    @OnlyIn(Dist.CLIENT)
    public void registerLayerDefinitions(final EntityRenderersEvent.RegisterLayerDefinitions event) {
        event.registerLayerDefinition(SimplyCatModel.LAYER_LOCATION, SimplyCatModel::createBodyLayer);
    }

    public void registerSpawnPlacements(final RegisterSpawnPlacementsEvent event) {
        event.register(CAT.get(), SpawnPlacementTypes.ON_GROUND, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, SimplyCatEntity::checkAnimalSpawnRules, RegisterSpawnPlacementsEvent.Operation.REPLACE);
    }

    public void registerAttributes(EntityAttributeCreationEvent event) {
        event.put(SimplyCats.CAT.get(), SimplyCatEntity.createAttributes().build());
    }

    private void gatherData(final GatherDataEvent event) {
        DataGenerator dataGenerator = event.getGenerator();
        PackOutput packOutput = dataGenerator.getPackOutput();
        dataGenerator.addProvider(event.includeClient(), new SCBlockModels(packOutput, event.getExistingFileHelper()));
        dataGenerator.addProvider(event.includeClient(), new SCBlockStates(packOutput, event.getExistingFileHelper()));
//        dataGenerator.addProvider(event.includeClient(), new SCItemModels(packOutput, event.getExistingFileHelper()));

        SCTags.SCBlockTags blockTagsProvider = new SCTags.SCBlockTags(packOutput, event.getLookupProvider(), event.getExistingFileHelper());
        dataGenerator.addProvider(event.includeServer(), blockTagsProvider);
        dataGenerator.addProvider(event.includeServer(), new SCTags.SCItemTags(packOutput, event.getLookupProvider(), blockTagsProvider, event.getExistingFileHelper()));
        dataGenerator.addProvider(event.includeServer(), new SCTags.SCPoiTypeTags(packOutput, event.getLookupProvider(), event.getExistingFileHelper()));
        dataGenerator.addProvider(event.includeServer(), new LootTableProvider(packOutput, Collections.emptySet(),
                List.of(new LootTableProvider.SubProviderEntry(SCBlockLoot::new, LootContextParamSets.BLOCK)), event.getLookupProvider()));
        dataGenerator.addProvider(event.includeServer(), new SCRecipeProvider(packOutput, event.getLookupProvider()));
        dataGenerator.addProvider(event.includeServer(), new SCAdvancementProvider(packOutput, event.getLookupProvider(), event.getExistingFileHelper()));
    }
}