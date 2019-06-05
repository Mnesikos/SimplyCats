package com.github.mnesikos.simplycats.entity.ai;

import com.github.mnesikos.simplycats.entity.EntityCat;
import net.minecraft.entity.ai.EntityAIBase;
import net.minecraft.entity.item.EntityXPOrb;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.world.World;

import java.util.Iterator;
import java.util.List;
import java.util.Random;

public class EntityCatAIMate extends EntityAIBase {

    private final EntityCat CAT;
    private EntityCat TARGET;
    World WORLD;
    double MOVE_SPEED;
    int MATE_DELAY;

    public EntityCatAIMate(EntityCat entityCat, double d) {
        this.CAT = entityCat;
        this.WORLD = entityCat.world;
        this.MOVE_SPEED = d;
        this.setMutexBits(3);
    }

    @Override
    public boolean shouldExecute() {
        if (this.CAT.getSex() == 0)
            return false;

        else if ((this.TARGET != null && !this.TARGET.getBreedingStatus("inheat")) || (this.CAT.getMateTimer() > 0))
            return false;

        else {
            this.TARGET = this.getNearbyMate();
            return this.TARGET != null;
        }
    }

    @Override
    public boolean shouldContinueExecuting() {
        boolean maleCooldownCheck = this.CAT.getSex() == 1 && this.CAT.getMateTimer() == 0;
        boolean femaleHeatCheck = this.TARGET.getSex() == 0 && this.TARGET.getBreedingStatus("inheat");
        return maleCooldownCheck && this.TARGET.isEntityAlive() && femaleHeatCheck && this.MATE_DELAY < 60;
    }

    @Override
    public void resetTask() {
        this.TARGET = null;
        this.MATE_DELAY = 0;
    }

    @Override
    public void updateTask() {
        this.CAT.getLookHelper().setLookPositionWithEntity(this.TARGET, 10.0F, (float) this.CAT.getVerticalFaceSpeed());
        this.TARGET.getLookHelper().setLookPositionWithEntity(this.CAT, 10.0F, (float) this.TARGET.getVerticalFaceSpeed());
        this.CAT.getNavigator().tryMoveToEntityLiving(this.TARGET, this.MOVE_SPEED);
        this.TARGET.getNavigator().tryMoveToEntityLiving(this.CAT, this.MOVE_SPEED);
        ++this.MATE_DELAY;

        if (this.MATE_DELAY >= 60 && this.CAT.getDistanceSq(this.TARGET) < 9.0D) {
            if (this.WORLD.rand.nextInt(4) <= 2) //75% chance of success
                this.startPregnancy();
            this.CAT.setMateTimer(24000); // starts male cooldown
        }
    }

    private EntityCat getNearbyMate() {
        float f = 8.0F;
        List<EntityCat> list = this.WORLD.getEntitiesWithinAABB(this.CAT.getClass(), this.CAT.getEntityBoundingBox().grow(8.0D));
        double d0 = Double.MAX_VALUE;
        EntityCat entityCat = null;
        Iterator<?> iterator = list.iterator();

        if (this.CAT.getSex() == 1)
            while (iterator.hasNext()) {
                EntityCat cat1 = (EntityCat) iterator.next();

                if (this.CAT.canMateWith(cat1) && this.CAT.getDistanceSq(cat1) < d0) {
                    entityCat = cat1;
                    d0 = this.CAT.getDistanceSq(cat1);
                }
            }

        return entityCat;
    }

    private void startPregnancy() {
        int litterSize;
        if (this.TARGET.getKittens("total") <= 0) {
            litterSize = this.WORLD.rand.nextInt(6) + 1; // at least 1 kitten, max of 6
        } else {
            litterSize = this.WORLD.rand.nextInt(6 - this.TARGET.getKittens("total")) + 1; // max of 6, minus already accrued kittens
        }
        this.TARGET.setBreedingStatus("ispregnant", true);
        this.TARGET.setKittens(litterSize);
        this.TARGET.addFather(this.CAT, this.CAT.getCustomNameTag()); // save father nbt data to mother cat

        if (litterSize == 6 || this.TARGET.getKittens("total") == 6 || this.WORLD.rand.nextInt(4) == 0) { // full litter OR 25% chance ends heat
            this.TARGET.setBreedingStatus("inheat", false);
            this.TARGET.setTimeCycle("pregnancy", 100); // starts pregnancy timer, 4 minecraft days
        }
        /*for (int i = 0; i < litterSize; i++)
            this.spawnBaby();*/
    }

    /*private void spawnBaby() {
        EntityCat child = this.CAT.createChild(this.TARGET);

        final net.minecraftforge.event.entity.living.BabyEntitySpawnEvent event = new net.minecraftforge.event.entity.living.BabyEntitySpawnEvent(CAT, TARGET, child);
        child = (EntityCat) event.getChild();

        if (child != null) {
            child.setGrowingAge(-24000);
            child.setLocationAndAngles(this.CAT.posX, this.CAT.posY, this.CAT.posZ, 0.0F, 0.0F);
            child.setParent("mother", this.TARGET.getCustomNameTag());
            child.setParent("father", this.CAT.getCustomNameTag());
            this.WORLD.spawnEntity(child);

            Random random = this.CAT.getRNG();
            for (int i = 0; i < 7; ++i) {
                double d0 = random.nextGaussian() * 0.02D;
                double d1 = random.nextGaussian() * 0.02D;
                double d2 = random.nextGaussian() * 0.02D;
                this.WORLD.spawnParticle(EnumParticleTypes.HEART, this.CAT.posX + (double) (random.nextFloat() * this.CAT.width * 2.0F) - (double) this.CAT.width, this.CAT.posY + 0.5D + (double) (random.nextFloat() * this.CAT.height), this.CAT.posZ + (double) (random.nextFloat() * this.CAT.width * 2.0F) - (double) this.CAT.width, d0, d1, d2);
            }

            if (this.WORLD.getGameRules().getBoolean("doMobLoot")) {
                this.WORLD.spawnEntity(new EntityXPOrb(this.WORLD, this.CAT.posX, this.CAT.posY, this.CAT.posZ, random.nextInt(7) + 1));
            }
        }
    }*/

}
