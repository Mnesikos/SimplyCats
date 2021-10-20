package com.github.mnesikos.simplycats.event;

import com.github.mnesikos.simplycats.SimplyCats;
import com.github.mnesikos.simplycats.entity.SimplyCatEntity;
import com.github.mnesikos.simplycats.worldgen.villages.SCVillagers;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.ai.attributes.GlobalEntityTypeAttributes;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;

@Mod.EventBusSubscriber(modid = SimplyCats.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class SCEvents {
    @SubscribeEvent
    public static void setupCommon(FMLCommonSetupEvent event) {
        event.enqueueWork(() -> {
            GlobalEntityTypeAttributes.put(SimplyCats.CAT.get(), SimplyCatEntity.createAttributes().build());
            SCVillagers.registerTrades();
            SCVillagers.registerPointOfInterests();
        });
    }

    public static boolean isRatEntity(Entity entity) {
        String entityType = EntityType.getKey(entity.getType()).toString();
        return entityType.equals("rats:rat")/* || entityType.equals("zawa:brownrat")*/;
    }

    /*@SubscribeEvent
    public void onPlayerLogsIn(PlayerEvent.PlayerLoggedInEvent event) { // todo
        if (SCConfig.Common.join_message.get()) {
            PlayerEntity player = event.getPlayer();
            player.sendMessage(new TranslationTextComponent("chat.join.cat_count", player.getPersistentData().getInt("CatCount")), Util.NIL_UUID);
        }
    }*/

    /*@SubscribeEvent
    public void onEntityJoinWorldEvent(EntityJoinWorldEvent event) {
        if (event.getEntity() instanceof CreeperEntity) {
            CreeperEntity creeper = (CreeperEntity) event.getEntity();
            creeper.goalSelector.addGoal(3, new AvoidEntityGoal<>(creeper, SimplyCatEntity.class, 6.0F, 1.0D, 1.2D));
        }
    }

    @SubscribeEvent
    public void onLivingSetAttackTargetEvent(LivingSetAttackTargetEvent event) {
        if (event.getTarget() != null && event.getEntityLiving() != null) {
            LivingEntity attackingEntity = event.getEntityLiving();
            if (attackingEntity instanceof WitchEntity)
                if (!attackingEntity.level.getEntitiesOfClass(SimplyCatEntity.class, attackingEntity.getBoundingBox().inflate(16.0F)).isEmpty())
                    ((WitchEntity) attackingEntity).setTarget(null);
        }
    }*/
}
