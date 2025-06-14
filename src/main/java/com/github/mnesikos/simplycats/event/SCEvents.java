package com.github.mnesikos.simplycats.event;

import com.github.mnesikos.simplycats.entity.npc.SimplyCatSpawner;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.server.ServerStartingEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.server.ServerLifecycleHooks;

@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.FORGE)
public class SCEvents {
    private static SimplyCatSpawner simplyCatSpawner;

    @SubscribeEvent
    public static void onServerStart(ServerStartingEvent event) {
        simplyCatSpawner = new SimplyCatSpawner();
    }

    @SubscribeEvent
    public static void onServerTick(TickEvent.ServerTickEvent event) {
        if (simplyCatSpawner != null)
            simplyCatSpawner.tick(ServerLifecycleHooks.getCurrentServer().overworld(), true, true);
    }

    /*@SubscribeEvent
    public void onPlayerLogsIn(PlayerEvent.PlayerLoggedInEvent event) { // todo
        if (SCConfig.join_message.get()) {
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
                if (!attackingEntity.level().getEntitiesOfClass(SimplyCatEntity.class, attackingEntity.getBoundingBox().inflate(16.0F)).isEmpty())
                    ((WitchEntity) attackingEntity).setTarget(null);
        }
    }*/
}
