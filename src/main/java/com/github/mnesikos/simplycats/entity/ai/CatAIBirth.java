package com.github.mnesikos.simplycats.entity.ai;

import com.github.mnesikos.simplycats.SimplyCats;
import com.github.mnesikos.simplycats.configuration.SimplyCatsConfig;
import com.github.mnesikos.simplycats.entity.EntityCat;
import com.github.mnesikos.simplycats.entity.core.Genetics;
import net.minecraft.entity.ai.goal.Goal;
import net.minecraft.entity.item.ExperienceOrbEntity;
import net.minecraft.particles.ParticleTypes;
import net.minecraft.world.GameRules;
import net.minecraft.world.World;

import java.util.EnumSet;
import java.util.Random;

public class CatAIBirth extends Goal {
    private final EntityCat mother;
    private EntityCat father;
    World world;
    //int KITTEN_DELAY; // might include later, thinking one kitten spawns at a time

    public CatAIBirth(EntityCat entityCat) {
        this.mother = entityCat;
        this.world = entityCat.world;
        this.setMutexFlags(EnumSet.of(Goal.Flag.MOVE, Goal.Flag.LOOK));
    }

    @Override
    public boolean shouldExecute() {
        if (this.mother.getSex() != Genetics.Sex.FEMALE || !this.mother.getBreedingStatus("ispregnant") || this.mother.getBreedingStatus("inheat"))
            return false;

        else if (this.mother.getMateTimer() >= 0)
            return false;

        else return !this.mother.isTamed() || this.mother.getOwner() != null;
    }

    @Override
    public boolean shouldContinueExecuting() {
        return this.mother.getBreedingStatus("ispregnant");
    }

    @Override
    public void resetTask() {
        this.father = null;
    }

    @Override
    public void tick() {
        for (int i = 0; i < this.mother.getKittens(); i++) {
            this.father = SimplyCats.CAT.get().create(this.world); // create the father cat for kitten referencing
            father.read(this.mother.getFather(i)); // set the saved father nbt data to new FATHER cat

            this.spawnBaby(this.father);
            this.mother.getPersistentData().remove("Father" + i); // deletes just used father data
        }

        this.mother.setKittens(0); // resets kitten counter
        this.mother.setBreedingStatus("ispregnant", false); // ends pregnancy
        this.mother.setTimeCycle("end", SimplyCatsConfig.HEAT_COOLDOWN.get()); // sets out of heat timer
    }

    private void spawnBaby(EntityCat father) {
        EntityCat child = this.mother.createChild(father);

        final net.minecraftforge.event.entity.living.BabyEntitySpawnEvent event = new net.minecraftforge.event.entity.living.BabyEntitySpawnEvent(mother, father, child);
        child = (EntityCat) event.getChild();

        if (child != null) {
            child.setGrowingAge(-SimplyCatsConfig.KITTEN_MATURE_TIMER.get());
            child.setLocationAndAngles(this.mother.getPosX(), this.mother.getPosY(), this.mother.getPosZ(), 0.0F, 0.0F);
            child.setParent("father", this.father.getName().getFormattedText());
            child.setParent("mother", this.mother.getName().getFormattedText());
            this.world.addEntity(child);

            Random random = this.mother.getRNG();
            for (int i = 0; i < 7; ++i) {
                double d0 = random.nextGaussian() * 0.02D;
                double d1 = random.nextGaussian() * 0.02D;
                double d2 = random.nextGaussian() * 0.02D;
                this.world.addParticle(ParticleTypes.HEART, this.mother.getPosX() + (double) (random.nextFloat() * this.mother.getWidth() * 2.0F) - (double) this.mother.getWidth(), this.mother.getPosY() + 0.5D + (double) (random.nextFloat() * this.mother.getHeight()), this.mother.getPosZ() + (double) (random.nextFloat() * this.mother.getWidth() * 2.0F) - (double) this.mother.getWidth(), d0, d1, d2);
            }

            if (this.world.getGameRules().getBoolean(GameRules.DO_MOB_LOOT)) {
                this.world.addEntity(new ExperienceOrbEntity(this.world, this.mother.getPosX(), this.mother.getPosY(), this.mother.getPosZ(), random.nextInt(2) + 1));
            }
        }
    }
}
