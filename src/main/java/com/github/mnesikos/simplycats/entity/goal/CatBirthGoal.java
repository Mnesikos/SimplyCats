package com.github.mnesikos.simplycats.entity.goal;

import com.github.mnesikos.simplycats.SimplyCats;
import com.github.mnesikos.simplycats.configuration.SCConfig;
import com.github.mnesikos.simplycats.entity.SimplyCatEntity;
import com.github.mnesikos.simplycats.entity.core.Genetics;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.ExperienceOrb;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.world.level.GameRules;
import net.minecraft.world.level.Level;
import net.minecraft.server.level.ServerLevel;
import net.minecraftforge.common.MinecraftForge;

import java.util.Random;

public class CatBirthGoal extends Goal {
    private final SimplyCatEntity mother;
    private SimplyCatEntity father;
    Level level;

    public CatBirthGoal(SimplyCatEntity catEntity) {
        this.mother = catEntity;
        this.level = catEntity.level();
    }

    @Override
    public boolean canUse() {
        if (this.mother.getSex() != Genetics.Sex.FEMALE || !this.mother.getBreedingStatus("ispregnant") || this.mother.getBreedingStatus("inheat"))
            return false;

        else if (this.mother.getMateTimer() >= 0)
            return false;

        else return !this.mother.isTame() || this.mother.getOwner() != null;
    }

    @Override
    public boolean canContinueToUse() {
        return this.mother.getBreedingStatus("ispregnant");
    }

    @Override
    public void stop() {
        this.father = null;
    }

    @Override
    public void tick() {
        for (int i = 0; i < this.mother.getKittens(); i++) {
            this.father = new SimplyCatEntity(SimplyCats.CAT.get(), this.level); // create the father cat for kitten referencing
            father.load(this.mother.getFather(i)); // set the saved father nbt data to new FATHER cat

            this.spawnBaby(this.father);
            this.mother.getPersistentData().remove("Father" + i); // deletes just used father data
        }

        this.mother.setKittens(0); // resets kitten counter
        this.mother.setBreedingStatus("ispregnant", false); // ends pregnancy
        this.mother.setTimeCycle("end", SCConfig.heat_cooldown.get()); // sets out of heat timer
    }

    private void spawnBaby(SimplyCatEntity father) {
        ServerLevel serverWorld = (ServerLevel) level;
        SimplyCatEntity child = (SimplyCatEntity) this.mother.getBreedOffspring(serverWorld, father);

        final net.minecraftforge.event.entity.living.BabyEntitySpawnEvent event = new net.minecraftforge.event.entity.living.BabyEntitySpawnEvent(mother, father, child);
        final boolean cancelled = MinecraftForge.EVENT_BUS.post(event);
        child = (SimplyCatEntity) event.getChild();

        if (cancelled) {
            return;
        }
        if (child != null) {
            child.setAge(-SCConfig.kitten_mature_timer.get());
            child.setMatureTimer((float) SCConfig.kitten_mature_timer.get());
            child.moveTo(this.mother.getX(), this.mother.getY(), this.mother.getZ(), 0.0F, 0.0F);
            child.setFather(this.father.getUUID());
            child.setMother(this.mother.getUUID());
            serverWorld.addFreshEntityWithPassengers(child);
            serverWorld.broadcastEntityEvent(this.mother, (byte) 18);

            RandomSource random = this.mother.getRandom();
            for (int i = 0; i < 7; ++i) {
                double d0 = random.nextGaussian() * 0.02D;
                double d1 = random.nextGaussian() * 0.02D;
                double d2 = random.nextGaussian() * 0.02D;
                this.level.addParticle(ParticleTypes.HEART, this.mother.getRandomX(1.0D), this.mother.getRandomY() + 0.5D, this.mother.getRandomZ(1.0D), d0, d1, d2);
            }

            if (serverWorld.getGameRules().getBoolean(GameRules.RULE_DOMOBLOOT))
                serverWorld.addFreshEntity(new ExperienceOrb(serverWorld, this.mother.getX(), this.mother.getY(), this.mother.getZ(), this.mother.getRandom().nextInt(2) + 1));
        }
    }
}
