package com.github.mnesikos.simplycats.entity;

import com.github.mnesikos.simplycats.entity.core.Genetics;
import net.minecraft.entity.*;
import net.minecraft.entity.ai.attributes.AttributeModifierMap;
import net.minecraft.entity.ai.attributes.Attributes;
import net.minecraft.entity.passive.CatEntity;
import net.minecraft.entity.passive.TameableEntity;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.util.DamageSource;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.SoundEvents;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.IServerWorld;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;

import javax.annotation.Nullable;

public class SimplyCatEntity extends TameableEntity {
    private static final DataParameter<String> GENES = EntityDataManager.defineId(CatEntity.class, DataSerializers.STRING);

    public SimplyCatEntity(EntityType<? extends TameableEntity> type, World world) {
        super(type, world);
    }

    @Override
    protected void registerGoals() {
//        super.registerGoals();
    }

    @Override
    public ILivingEntityData finalizeSpawn(IServerWorld world, DifficultyInstance difficulty, SpawnReason spawnReason, @Nullable ILivingEntityData entityData, @Nullable CompoundNBT compound) {
        entityData = super.finalizeSpawn(world, difficulty, spawnReason, entityData, compound);
        this.setPhenotype();
        return entityData;
    }

    @Override
    protected void defineSynchedData() {
        super.defineSynchedData();
        this.entityData.define(GENES, "");
    }

    public void setPhenotype() {
        String genes = "";
        genes += Genetics.FurLength.init(random) + Genetics.FurLength.init(random);
        genes += Genetics.Eumelanin.init(random) + Genetics.Eumelanin.init(random);
        this.setGenes(genes);
    }

    // selectEyeColor
    // selectWhiteMarkings
    // selectWhitePaws
    // getWhiteTextures
    // setWhiteTextures
    // getWhitePawTextures
    // setWhitePawTextures

    public String getGenes() {
        return this.entityData.get(GENES);
    }

    public void setGenes(String genes) {
        this.entityData.set(GENES, genes);
    }

    // getSex
    // isBobtail
    // isLongFur
    // hasHomePos
    // getHomePos
    // resetHomePos
    // getOwnerName
    // setOwnerName

    @Override
    public void addAdditionalSaveData(CompoundNBT compound) {
        super.addAdditionalSaveData(compound);
        compound.putString("Genes", this.getGenes());
    }

    @Override
    public void readAdditionalSaveData(CompoundNBT compound) {
        super.readAdditionalSaveData(compound);
        this.setGenes(compound.getString("Genes"));
    }

    // getPhenotype
    // resetTexturePrefix
    // setCatTexturePaths
    // getCatTexture
    // getTexturePaths
    // onUpdate
    // onLivingUpdate
    // canBeTamed
    // processInteract
    // setTamed
    // onDeath
    // inheritGene

    @Nullable
    @Override
    public AgeableEntity getBreedOffspring(ServerWorld world, AgeableEntity entity) {
        return null;
    }

    @Override
    public boolean causeFallDamage(float distance, float damageMultiplier) {
        return false;
    }

    // canTriggerWalking
    // isAngry
    // setAngry

    @Nullable
    @Override
    protected SoundEvent getAmbientSound() {
        /*if (this.isAngry()) {
            return this.random.nextInt(10) == 0 ? SoundEvents.CAT_HISS : null;
        } else*/
        if (this.isInLove() /*|| this.PURR*/) {
            return SoundEvents.CAT_PURR;
        } else {
            if (this.isTame())
                return this.random.nextInt(10) == 0 ? SoundEvents.CAT_PURREOW : SoundEvents.CAT_AMBIENT;
            return SoundEvents.CAT_STRAY_AMBIENT;
        }
    }

    @Override
    public int getAmbientSoundInterval() {
        return 240;
    }

    @Nullable
    @Override
    protected SoundEvent getHurtSound(DamageSource damageSource) {
        return SoundEvents.CAT_HURT;
    }

    @Nullable
    @Override
    protected SoundEvent getDeathSound() {
        return SoundEvents.CAT_DEATH;
    }

    // getSoundVolume
    // setCustomNameTag
    // getName
    // getLootTable

    public static AttributeModifierMap.MutableAttribute createAttributes() {
        return MobEntity.createMobAttributes().add(Attributes.MAX_HEALTH, 14.0D).add(Attributes.MOVEMENT_SPEED, 0.3F).add(Attributes.KNOCKBACK_RESISTANCE, 0.7D).add(Attributes.ATTACK_DAMAGE, 2.0D);
    }
}
