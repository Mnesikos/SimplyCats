package com.github.mnesikos.simplycats.entity.ai;

import com.github.mnesikos.simplycats.configuration.SCConfig;
import com.github.mnesikos.simplycats.entity.EntityCat;
import com.github.mnesikos.simplycats.entity.core.Genetics;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.ai.EntityAIBase;
import net.minecraft.world.World;

import java.util.List;

public class CatAIMate extends EntityAIBase {

    private static final double NEARBY_RADIUS_CHECK = 16.0D;
    private final double moveSpeed;
    private final EntityCat cat;
    private EntityCat target;
    private int mateDelay;
    private List<EntityCat> nearbyCats;

    public CatAIMate(EntityCat entityCat, double speed) {
        this.cat = entityCat;
        this.moveSpeed = speed;
        this.setMutexBits(3);
    }

    @Override
    public boolean shouldExecute() {
        World world = this.cat.world;
        if (this.cat.getSex() == Genetics.Sex.FEMALE)
            return false;

        this.nearbyCats = world.getEntitiesWithinAABB(this.cat.getClass(), this.cat.getEntityBoundingBox().grow(NEARBY_RADIUS_CHECK));
        if (this.nearbyCats.size() >= SCConfig.BREEDING_LIMIT)
            return false;

        EntityLivingBase catOwner = this.cat.getOwner();
        if (ownerExceedsLimit(this.cat, catOwner) || ownerIsOffline(this.cat, catOwner) || this.cat.getMateTimer() > 0)
            return false;

        this.target = this.getNearbyMate();
        EntityLivingBase targetOwner = this.target.getOwner();
        return this.target != null && this.cat.getEntitySenses().canSee(this.target) && this.target.getBreedingStatus("inheat")
                && !ownerExceedsLimit(this.target, targetOwner) && !ownerIsOffline(this.target, targetOwner);
    }

    @Override
    public boolean shouldContinueExecuting() {
        World world = this.cat.world;
        boolean maleCooldownCheck = this.cat.getSex() == Genetics.Sex.MALE && this.cat.getMateTimer() == 0;
        boolean femaleHeatCheck = this.target.getSex() == Genetics.Sex.FEMALE && this.target.getBreedingStatus("inheat");

        this.nearbyCats = world.getEntitiesWithinAABB(this.cat.getClass(), this.cat.getEntityBoundingBox().grow(NEARBY_RADIUS_CHECK));

        return maleCooldownCheck && this.target.isEntityAlive() && femaleHeatCheck && this.mateDelay < 60
                && this.nearbyCats.size() < SCConfig.BREEDING_LIMIT && this.cat.getEntitySenses().canSee(this.target);
    }

    private boolean ownerExceedsLimit(EntityCat tamedCat, EntityLivingBase owner) {
        return tamedCat != null && tamedCat.isTamed() && SCConfig.TAMED_LIMIT != 0 && owner != null && owner.getEntityData().getInteger("CatCount") >= SCConfig.TAMED_LIMIT;
    }

    private boolean ownerIsOffline(EntityCat tamedCat, EntityLivingBase owner) {
        return tamedCat != null && tamedCat.isTamed() && owner == null;
    }

    @Override
    public void resetTask() {
        this.target = null;
        this.mateDelay = 0;
        this.nearbyCats.clear();
    }

    @Override
    public void updateTask() {
        World world = this.cat.world;
        this.cat.getLookHelper().setLookPositionWithEntity(this.target, 10.0F, (float) this.cat.getVerticalFaceSpeed());
        this.target.getLookHelper().setLookPositionWithEntity(this.cat, 10.0F, (float) this.target.getVerticalFaceSpeed());
        this.cat.getNavigator().tryMoveToEntityLiving(this.target, this.moveSpeed);
        this.target.getNavigator().tryMoveToEntityLiving(this.cat, this.moveSpeed);
        ++this.mateDelay;

        if (this.mateDelay >= 60 && this.cat.getDistanceSq(this.target) < 4.0D) {
            if (world.rand.nextInt(4) <= 2) // 75% chance of success
                this.startPregnancy();
            this.cat.setMateTimer(SCConfig.MALE_COOLDOWN); // starts male cooldown
        }
    }

    private EntityCat getNearbyMate() {
        World world = this.cat.world;
        List<EntityCat> list = world.getEntitiesWithinAABB(this.cat.getClass(), this.cat.getEntityBoundingBox().grow(NEARBY_RADIUS_CHECK));
        double d0 = Double.MAX_VALUE;
        EntityCat entityCat = null;

        if (this.cat.getSex() == Genetics.Sex.MALE) {
            for (EntityCat cat1 : list) {
                if (this.cat.canMateWith(cat1) && this.cat.getDistanceSq(cat1) < d0) {
                    entityCat = cat1;
                    d0 = this.cat.getDistanceSq(cat1);
                }
            }
        }

        return entityCat;
    }

    private void startPregnancy() {
        World world = this.cat.world;
        int litterSize;
        if (this.target.getKittens() <= 0) {
            litterSize = world.rand.nextInt(6) + 1; // at least 1 kitten, max of 6
        } else {
            litterSize = world.rand.nextInt(6 - this.target.getKittens()) + 1; // max of 6, minus already accrued kittens
        }
        this.target.setBreedingStatus("ispregnant", true);
        this.target.setKittens(litterSize);
        this.target.addFather(this.cat, this.target.getKittens()); // save father nbt data to mother cat for each kitten added to litterSize

        if (litterSize == 6 || this.target.getKittens() == 6 || world.rand.nextInt(4) == 0) { // full litter OR 25% chance ends heat
            this.target.setBreedingStatus("inheat", false);
            this.target.setTimeCycle("pregnancy", SCConfig.PREGNANCY_TIMER); // starts pregnancy timer
        }
    }
}
