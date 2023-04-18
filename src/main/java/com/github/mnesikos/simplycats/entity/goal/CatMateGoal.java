package com.github.mnesikos.simplycats.entity.goal;

import com.github.mnesikos.simplycats.configuration.SCConfig;
import com.github.mnesikos.simplycats.entity.SimplyCatEntity;
import com.github.mnesikos.simplycats.entity.genetics.Genetics;
import net.minecraft.entity.EntityPredicate;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.goal.Goal;
import net.minecraft.world.World;

import java.util.EnumSet;
import java.util.List;

public class CatMateGoal extends Goal {
    private static final EntityPredicate PARTNER_TARGETING = (new EntityPredicate()).range(8.0D).allowInvulnerable().allowSameTeam().allowUnseeable();
    private static final double NEARBY_RADIUS_CHECK = 16.0D;
    private final double moveSpeed;
    private final SimplyCatEntity cat;
    protected final World level;
    private SimplyCatEntity target;
    private int mateDelay;
    private List<SimplyCatEntity> nearbyCats;

    public CatMateGoal(SimplyCatEntity entityCat, double speed) {
        this.cat = entityCat;
        this.level = entityCat.level;
        this.moveSpeed = speed;
        this.setFlags(EnumSet.of(Goal.Flag.MOVE, Goal.Flag.LOOK));
    }

    @Override
    public boolean canUse() {
        if (this.cat.getSex() == Genetics.Sex.FEMALE)
            return false;

        this.nearbyCats = level.getEntitiesOfClass(this.cat.getClass(), this.cat.getBoundingBox().inflate(NEARBY_RADIUS_CHECK));
        if (this.nearbyCats.size() >= SCConfig.breeding_limit.get())
            return false;

        LivingEntity catOwner = this.cat.getOwner();
        if (ownerExceedsLimit(this.cat, catOwner) || ownerIsOffline(this.cat, catOwner) || this.cat.getMateTimer() > 0)
            return false;

        this.target = this.getNearbyMate();
        if (this.target != null && this.cat.getSensing().canSee(this.target) && this.target.getBreedingStatus("inheat")) {
            if (!this.target.isTame()) return true;
            LivingEntity targetOwner = this.target.getOwner();
            return !ownerExceedsLimit(this.target, targetOwner) && !ownerIsOffline(this.target, targetOwner);
        }

        return false;
    }

    @Override
    public boolean canContinueToUse() {
        if (this.cat.isOrderedToSit() || this.target.isOrderedToSit())
            return false;

        boolean maleCooldownCheck = this.cat.getSex() == Genetics.Sex.MALE && this.cat.getMateTimer() == 0;
        boolean femaleHeatCheck = this.target.getSex() == Genetics.Sex.FEMALE && this.target.getBreedingStatus("inheat");

        this.nearbyCats = level.getEntitiesOfClass(this.cat.getClass(), this.cat.getBoundingBox().inflate(NEARBY_RADIUS_CHECK));

        return maleCooldownCheck && this.target.isAlive() && femaleHeatCheck && this.mateDelay < 60
                && this.nearbyCats.size() < SCConfig.breeding_limit.get() && this.cat.getSensing().canSee(this.target);
    }

    private boolean ownerExceedsLimit(SimplyCatEntity tamedCat, LivingEntity owner) {
        return tamedCat != null && tamedCat.isTame() && SCConfig.tamed_limit.get() != 0 && owner != null && owner.getPersistentData().getInt("CatCount") >= SCConfig.tamed_limit.get();
    }

    private boolean ownerIsOffline(SimplyCatEntity tamedCat, LivingEntity owner) {
        return tamedCat != null && tamedCat.isTame() && owner == null;
    }

    @Override
    public void stop() {
        this.target = null;
        this.mateDelay = 0;
        this.nearbyCats.clear();
    }

    @Override
    public void tick() {
        this.cat.getLookControl().setLookAt(this.target, 10.0F, (float) this.cat.getMaxHeadXRot());
        this.target.getLookControl().setLookAt(this.cat, 10.0F, (float) this.target.getMaxHeadXRot());
        this.cat.getNavigation().moveTo(this.target, this.moveSpeed);
        this.target.getNavigation().moveTo(this.cat, this.moveSpeed);
        ++this.mateDelay;

        if (this.mateDelay >= 60 && this.cat.distanceToSqr(this.target) < 4.0D) {
            if (level.random.nextInt(4) <= 2) // 75% chance of success
                this.startPregnancy();
            this.cat.setMateTimer(SCConfig.male_cooldown.get()); // starts male cooldown
        }
    }

    private SimplyCatEntity getNearbyMate() {
        List<SimplyCatEntity> list = level.getNearbyEntities(this.cat.getClass(), PARTNER_TARGETING, this.cat, this.cat.getBoundingBox().inflate(NEARBY_RADIUS_CHECK));
        double d0 = Double.MAX_VALUE;
        SimplyCatEntity entityCat = null;

        if (this.cat.getSex() == Genetics.Sex.MALE) {
            for (SimplyCatEntity cat1 : list) {
                if (this.cat.canMate(cat1) && this.cat.distanceToSqr(cat1) < d0) {
                    entityCat = cat1;
                    d0 = this.cat.distanceToSqr(cat1);
                }
            }
        }

        return entityCat;
    }

    private void startPregnancy() {
        int litterSize;
        if (this.target.getKittens() <= 0) {
            litterSize = level.random.nextInt(6) + 1; // at least 1 kitten, max of 6
        } else {
            litterSize = level.random.nextInt(6 - this.target.getKittens()) + 1; // max of 6, minus already accrued kittens
        }
        this.target.setBreedingStatus("ispregnant", true);
        this.target.setKittens(litterSize);
        this.target.addFather(this.cat, this.target.getKittens()); // save father nbt data to mother cat for each kitten added to litterSize

        if (litterSize == 6 || this.target.getKittens() == 6 || level.random.nextInt(4) == 0) { // full litter OR 25% chance ends heat
            this.target.setBreedingStatus("inheat", false);
            this.target.setTimeCycle("pregnancy", SCConfig.pregnancy_timer.get()); // starts pregnancy timer
        }
    }
}
