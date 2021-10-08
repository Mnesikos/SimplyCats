package com.github.mnesikos.simplycats.event;

import com.github.mnesikos.simplycats.configuration.SCConfig;
import com.github.mnesikos.simplycats.entity.SimplyCatEntity;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.goal.AvoidEntityGoal;
import net.minecraft.entity.monster.CreeperEntity;
import net.minecraft.entity.monster.WitchEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.Util;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;
import net.minecraftforge.event.entity.living.LivingSetAttackTargetEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.registries.ForgeRegistries;

import javax.annotation.Nullable;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

public class SCEvents {
    private static Set<Class<? extends EntityType>> entities;

    public static boolean isEntityPrey(@Nullable Entity entity) {
        if (entities == null) {
            entities = new HashSet<>();
            for (String s : SCConfig.PREY_LIST) {
                ResourceLocation location = new ResourceLocation(s);
                if (ForgeRegistries.ENTITIES.containsKey(location))
                    entities.add(Objects.requireNonNull(ForgeRegistries.ENTITIES.getValue(location)).getClass());
            }
        }
        return entities.contains(entity.getType());
    }

    public static boolean isRatEntity(EntityType entity) {
        String entityClass = EntityType.getKey(entity).toString();
        return entityClass.equals("rats:rat")/* || entityClass.equals("zawa:brownrat")*/;
    }

    @SubscribeEvent
    public void onPlayerLogsIn(PlayerEvent.PlayerLoggedInEvent event) {
        if (SCConfig.JOIN_MESSAGE) {
            PlayerEntity player = event.getPlayer();
            player.sendMessage(new TranslationTextComponent("chat.join.cat_count", player.getPersistentData().getInt("CatCount")), Util.NIL_UUID);
        }
    }

    @SubscribeEvent
    public void onEntityJoinWorldEvent(EntityJoinWorldEvent event) {
        if (event.getEntity() instanceof CreeperEntity) {
            CreeperEntity creeper = (CreeperEntity) event.getEntity();
            creeper.goalSelector.addGoal(3, new AvoidEntityGoal<>(creeper, SimplyCatEntity.class, 6.0F, 1.0D, 1.2D));
        }
    }

    @SubscribeEvent
    public void onLivingSetAttackTargetEvent(LivingSetAttackTargetEvent event) {
        if (event.getTarget() != null && event.getEntityLiving() instanceof LivingEntity) {
            LivingEntity attackingEntity = (LivingEntity) event.getEntityLiving();
            if (attackingEntity instanceof WitchEntity) {
                if (!attackingEntity.level.getEntitiesOfClass(SimplyCatEntity.class, attackingEntity.getBoundingBox().inflate(16.0F)).isEmpty()) {
                    ((WitchEntity) attackingEntity).setTarget(null);
                }
            }
        }
    }
}
