package com.github.mnesikos.simplycats.event;

import com.github.mnesikos.simplycats.configuration.SCConfig;
import com.github.mnesikos.simplycats.entity.AbstractCat;
import com.github.mnesikos.simplycats.entity.EntityCat;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityList;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.EntityAIAvoidEntity;
import net.minecraft.entity.ai.attributes.IAttributeInstance;
import net.minecraft.entity.monster.EntityCreeper;
import net.minecraft.entity.monster.EntityWitch;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;
import net.minecraftforge.event.entity.living.LivingSetAttackTargetEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.PlayerEvent.PlayerLoggedInEvent;
import net.minecraftforge.fml.common.registry.ForgeRegistries;

import javax.annotation.Nullable;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

public class SCEvents {
    private static Set<Class<? extends Entity>> entities;

    public static boolean isEntityPrey(@Nullable Entity entity) {
        if (entities == null) {
            entities = new HashSet<>();
            for (String s : SCConfig.PREY_LIST) {
                ResourceLocation location = new ResourceLocation(s);
                if (ForgeRegistries.ENTITIES.containsKey(location))
                    entities.add(Objects.requireNonNull(ForgeRegistries.ENTITIES.getValue(location)).getEntityClass());
            }
        }
        return entities.contains(entity.getClass());
    }

    public static boolean isRatEntity(Entity entity) {
        String entityClass = EntityList.getKey(entity).toString();
        return entityClass.equals("rats:rat")/* || entityClass.equals("zawa:brownrat")*/;
    }

    @SubscribeEvent
    public void onPlayerLogsIn(PlayerLoggedInEvent event) {
        if (SCConfig.JOIN_MESSAGE) {
            EntityPlayer player = event.player;
            player.sendMessage(new TextComponentTranslation("chat.join.cat_count", player.getEntityData().getInteger("CatCount")));
        }
    }

    @SubscribeEvent
    public void onEntityJoinWorldEvent(EntityJoinWorldEvent event) {
        if (event.getEntity() instanceof EntityCreeper) {
            EntityCreeper creeper = (EntityCreeper) event.getEntity();
            creeper.tasks.addTask(3, new EntityAIAvoidEntity<>(creeper, AbstractCat.class, 6.0F, 1.0D, 1.2D));
        }

        if (event.getEntity() instanceof EntityCat) {
            EntityCat cat = (EntityCat) event.getEntity();
            IAttributeInstance maxHealth = cat.getEntityAttribute(SharedMonsterAttributes.MAX_HEALTH);
            if (maxHealth.getBaseValue() < 14.0D) {
                maxHealth.setBaseValue(14.0D);
                cat.setHealth(14.0F);
            }
        }
    }

    @SubscribeEvent
    public void onLivingSetAttackTargetEvent(LivingSetAttackTargetEvent event) {
        if (event.getTarget() != null && event.getEntityLiving() instanceof EntityLiving) {
            EntityLiving attackingEntity = (EntityLiving) event.getEntityLiving();
            if (attackingEntity instanceof EntityWitch) {
                if (!attackingEntity.world.getEntitiesWithinAABB(AbstractCat.class, attackingEntity.getEntityBoundingBox().grow(16.0F)).isEmpty()) {
                    attackingEntity.setAttackTarget(null);
                }
            }
        }
    }
}
