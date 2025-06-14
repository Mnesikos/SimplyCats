package com.github.mnesikos.simplycats.event;

import com.github.mnesikos.simplycats.SimplyCats;
import com.github.mnesikos.simplycats.configuration.SCConfig;
import com.github.mnesikos.simplycats.entity.SimplyCatEntity;
import com.github.mnesikos.simplycats.entity.core.Genetics;
import com.github.mnesikos.simplycats.entity.npc.SimplyCatSpawner;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.AvoidEntityGoal;
import net.minecraft.world.entity.animal.Cat;
import net.minecraft.world.entity.monster.Creeper;
import net.minecraft.world.entity.monster.Witch;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.entity.EntityJoinLevelEvent;
import net.minecraftforge.event.entity.living.LivingChangeTargetEvent;
import net.minecraftforge.event.server.ServerStartingEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.server.ServerLifecycleHooks;

import java.util.stream.Stream;

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

    @SubscribeEvent
    public static void joinWorldEvent(EntityJoinLevelEvent event) {
        if (event.getEntity().getClass() == Cat.class && !event.getLevel().isClientSide) {
            Cat vanillaCat = (Cat) event.getEntity();
            if (!vanillaCat.getPersistentData().contains("SimplyCatsSpawn")) {
                if (vanillaCat.isTame() && SCConfig.replace_tamed_vanilla.get()) {
                    Level world = event.getLevel();
                    SimplyCatEntity simplyCatEntity = SimplyCats.CAT.get().create(world);
                    simplyCatEntity.load(vanillaCat.saveWithoutId(new CompoundTag()));
                    simplyCatEntity.getAttribute(Attributes.MAX_HEALTH).setBaseValue(14.0D);
                    simplyCatEntity.getAttribute(Attributes.MOVEMENT_SPEED).setBaseValue(0.3D);
                    simplyCatEntity.getAttribute(Attributes.KNOCKBACK_RESISTANCE).setBaseValue(0.7D);
                    simplyCatEntity.getAttribute(Attributes.ATTACK_DAMAGE).setBaseValue(2.0D);
                    simplyCatEntity.heal(4.0F);
                    simplyCatEntity.setHomePos(simplyCatEntity.blockPosition());
                    simplyCatEntity.setPhenotype();
                    if (!vanillaCat.isTame() && !SCConfig.intact_stray_spawns.get()) simplyCatEntity.setFixed((byte) 1);
                    if (simplyCatEntity.getSex() == Genetics.Sex.FEMALE && !simplyCatEntity.isFixed())
                        simplyCatEntity.setTimeCycle("end", simplyCatEntity.getRandom().nextInt(SCConfig.heat_cooldown.get()));

                    if (world instanceof ServerLevel)
                        ((ServerLevel) world).addWorldGenChunkEntities(Stream.of(simplyCatEntity));
                    vanillaCat.getPersistentData().putBoolean("SimplyCatsSpawn", true);
                    event.setCanceled(true);

                } else if (!vanillaCat.isTame() && SCConfig.stop_vanilla_spawns.get()) {
                    vanillaCat.getPersistentData().putBoolean("SimplyCatsSpawn", true);
                    event.setCanceled(true);
                }
            }

        } else if (event.getEntity() instanceof Creeper) {
            Creeper creeper = (Creeper) event.getEntity();
            creeper.goalSelector.addGoal(3, new AvoidEntityGoal<>(creeper, SimplyCatEntity.class, 6.0F, 1.0D, 1.2D));
        }
    }

    /*@SubscribeEvent
    public void onPlayerLogsIn(PlayerEvent.PlayerLoggedInEvent event) { // todo
        if (SCConfig.join_message.get()) {
            PlayerEntity player = event.getPlayer();
            player.sendMessage(new TranslationTextComponent("chat.join.cat_count", player.getPersistentData().getInt("CatCount")), Util.NIL_UUID);
        }
    }*/

    public static void onLivingChangeTargetEvent(LivingChangeTargetEvent event) {
        if (event.getEntity() instanceof Witch witch && event.getNewTarget() instanceof Player) {
            if (!witch.level().getEntitiesOfClass(SimplyCatEntity.class, witch.getBoundingBox().inflate(16.0F)).isEmpty()) {
                event.setCanceled(true);
                witch.setTarget(null);
            }
        }
    }
}
