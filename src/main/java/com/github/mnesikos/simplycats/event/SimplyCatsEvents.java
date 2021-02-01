package com.github.mnesikos.simplycats.event;

import com.github.mnesikos.simplycats.configuration.SimplyCatsConfig;
import com.github.mnesikos.simplycats.entity.AbstractCat;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.goal.AvoidEntityGoal;
import net.minecraft.entity.monster.CreeperEntity;
import net.minecraft.entity.monster.WitchEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.ResourceLocation;
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

public class SimplyCatsEvents {
    public static boolean isRatEntity(Entity entity) {
        String entityClass = EntityType.getKey(entity.getType()).toString();
        return entityClass.equals("rats:rat");
    }

    @SubscribeEvent
    public void onPlayerLogsIn(PlayerEvent.PlayerLoggedInEvent event) {
        if (SimplyCatsConfig.JOIN_MESSAGE.get()) {
            PlayerEntity player = event.getPlayer();
            player.sendMessage(new TranslationTextComponent("chat.join.cat_count", player.getPersistentData().getInt("CatCount")));
        }
    }

    @SubscribeEvent
    public void onEntityJoinWorldEvent(EntityJoinWorldEvent event) {
        if (event.getEntity() instanceof CreeperEntity) {
            CreeperEntity creeper = (CreeperEntity) event.getEntity();
            creeper.goalSelector.addGoal(3, new AvoidEntityGoal<>(creeper, AbstractCat.class, 6.0F, 1.0D, 1.2D));
        }
    }

    @SubscribeEvent
    public void onLivingSetAttackTargetEvent(LivingSetAttackTargetEvent event) {
        if (event.getTarget() != null && event.getEntityLiving() != null) {
            LivingEntity attackingEntity = event.getEntityLiving();
            if (attackingEntity instanceof WitchEntity) {
                if (!attackingEntity.world.getEntitiesWithinAABB(AbstractCat.class, attackingEntity.getBoundingBox().grow(16.0F)).isEmpty()) {
                    ((WitchEntity) attackingEntity).setAttackTarget(null);
                }
            }
        }
    }
}
