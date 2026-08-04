package com.github.mnesikos.simplycats.compat;

import com.github.mnesikos.simplycats.SimplyCats;
import com.github.mnesikos.simplycats.entity.SimplyCatEntity;
import com.github.mnesikos.simplycats.entity.core.Genetics;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import snownee.jade.api.*;
import snownee.jade.api.config.IPluginConfig;
import snownee.jade.api.theme.IThemeHelper;

@WailaPlugin
public class JadeCompat implements IWailaPlugin {
    public static final ResourceLocation CAT_TIMERS = ResourceLocation.fromNamespaceAndPath(SimplyCats.MOD_ID, "cat_timers");

    @Override
    public void register(IWailaCommonRegistration registration) {
        registration.registerEntityDataProvider(CatTimersProvider.INSTANCE, SimplyCatEntity.class);
    }

    @Override
    public void registerClient(IWailaClientRegistration registration) {
        registration.registerEntityComponent(CatTimersProvider.INSTANCE, SimplyCatEntity.class);
    }

    public enum CatTimersProvider implements IEntityComponentProvider, IServerDataProvider<EntityAccessor> {
        INSTANCE;

        @Override
        public void appendTooltip(ITooltip iTooltip, EntityAccessor entityAccessor, IPluginConfig iPluginConfig) {
            if (!entityAccessor.getServerData().contains("Timer", CompoundTag.TAG_INT)) return;

            SimplyCatEntity cat = (SimplyCatEntity) entityAccessor.getEntity();
            boolean catIsFemale = cat.getSex() == Genetics.Sex.FEMALE;
            int timer = entityAccessor.getServerData().getInt("Timer");

            String timerInfo = catIsFemale ? cat.getBreedingStatus(SimplyCatEntity.BreedingStatus.HEAT) ? "chat.info.in_heat" : "chat.info.not_in_heat" : "chat.info.male";
            if (cat.getBreedingStatus(SimplyCatEntity.BreedingStatus.PREGNANT))
                timerInfo = cat.getBreedingStatus(SimplyCatEntity.BreedingStatus.HEAT) ? "chat.info.pregnant_heat" : "chat.info.pregnant";

            iTooltip.add(Component.translatable(catIsFemale ? "cat.sex.female.name" : "cat.sex.male.name")
                    .append(cat.isFixed() && entityAccessor.showDetails() ? Component.literal(", ").append(Component.translatable("cat.fixed.name")) : Component.empty())
                    .append(!cat.isFixed() && !cat.isBaby() && entityAccessor.showDetails() ? Component.literal(", ").append(Component.translatable(timerInfo, IThemeHelper.get().seconds(timer))) : Component.empty()));
        }

        @Override
        public void appendServerData(CompoundTag compoundTag, EntityAccessor entityAccessor) {
            if (entityAccessor.getEntity() instanceof SimplyCatEntity cat)
                compoundTag.putInt("Timer", cat.getMateTimer());
        }

        @Override
        public ResourceLocation getUid() {
            return JadeCompat.CAT_TIMERS;
        }
    }
}
