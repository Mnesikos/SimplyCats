package com.github.mnesikos.simplycats.entity.goal;

import com.github.mnesikos.simplycats.configuration.SCConfig;
import com.github.mnesikos.simplycats.entity.SimplyCatEntity;
import com.github.mnesikos.simplycats.entity.core.Genetics;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.ai.targeting.TargetingConditions;
import net.minecraft.world.level.Level;

import java.util.EnumSet;
import java.util.List;

public class CatMateGoal extends Goal {
    private static final TargetingConditions PARTNER_TARGETING = TargetingConditions.forNonCombat().range(8.0D).ignoreLineOfSight();
    private static final double NEARBY_RADIUS_CHECK = 16.0D;
    private final double moveSpeed;
    private final SimplyCatEntity cat;
    protected final Level level;
    private SimplyCatEntity target;
    private int mateDelay;
    private List<? extends SimplyCatEntity> nearbyCats;

    public CatMateGoal(SimplyCatEntity entityCat, double speed) {
        this.cat = entityCat;
        this.level = entityCat.level();
        this.moveSpeed = speed;
        this.setFlags(EnumSet.of(Goal.Flag.MOVE, Goal.Flag.LOOK));
    }

    @Override
    public boolean canUse() {
        if (cat.getSex() == Genetics.Sex.FEMALE)
            return false;

        nearbyCats = level.getEntitiesOfClass(cat.getClass(), cat.getBoundingBox().inflate(NEARBY_RADIUS_CHECK));
        if (nearbyCats.size() >= SCConfig.breeding_limit.get())
            return false;

        LivingEntity catOwner = cat.getOwner();
        if (ownerExceedsLimit(cat, catOwner) || ownerIsOffline(cat, catOwner) || cat.getMateTimer() > 0)
            return false;

        target = getNearbyMate();
        if (target != null && cat.getSensing().hasLineOfSight(target) && target.getBreedingStatus("inheat")) {
            if (!target.isTame()) return true;
            LivingEntity targetOwner = target.getOwner();
            return !ownerExceedsLimit(target, targetOwner) && !ownerIsOffline(target, targetOwner);
        }

        return false;
    }

    @Override
    public boolean canContinueToUse() {
        if (cat.isOrderedToSit() || target.isOrderedToSit())
            return false;

        boolean maleCooldownCheck = cat.getSex() == Genetics.Sex.MALE && cat.getMateTimer() == 0;
        boolean femaleHeatCheck = target.getSex() == Genetics.Sex.FEMALE && target.getBreedingStatus("inheat");

        nearbyCats = level.getEntitiesOfClass(cat.getClass(), cat.getBoundingBox().inflate(NEARBY_RADIUS_CHECK));

        return maleCooldownCheck && target.isAlive() && femaleHeatCheck && mateDelay < 60
                && nearbyCats.size() < SCConfig.breeding_limit.get() && cat.getSensing().hasLineOfSight(target);
    }

    private boolean ownerExceedsLimit(SimplyCatEntity tamedCat, LivingEntity owner) {
        return tamedCat != null && tamedCat.isTame() && SCConfig.tamed_limit.get() != 0 && owner != null && owner.getPersistentData().getInt("CatCount") >= SCConfig.tamed_limit.get();
    }

    private boolean ownerIsOffline(SimplyCatEntity tamedCat, LivingEntity owner) {
        return tamedCat != null && tamedCat.isTame() && owner == null;
    }

    @Override
    public void stop() {
        target = null;
        mateDelay = 0;
        nearbyCats.clear();
    }

    @Override
    public void tick() {
        cat.getLookControl().setLookAt(target, 10.0F, (float) cat.getMaxHeadXRot());
        target.getLookControl().setLookAt(cat, 10.0F, (float) target.getMaxHeadXRot());
        cat.getNavigation().moveTo(target, moveSpeed);
        target.getNavigation().moveTo(cat, moveSpeed);
        ++mateDelay;

        if (mateDelay >= 60 && cat.distanceToSqr(target) < 4.0D) {
            if (level.random.nextInt(4) <= 2) // 75% chance of success
                startPregnancy();
            cat.setMateTimer(SCConfig.male_cooldown.get()); // starts male cooldown
        }
    }

    private SimplyCatEntity getNearbyMate() {
        List<? extends SimplyCatEntity> list = level.getNearbyEntities(cat.getClass(), PARTNER_TARGETING, cat, cat.getBoundingBox().inflate(NEARBY_RADIUS_CHECK));
        double d0 = Double.MAX_VALUE;
        SimplyCatEntity entityCat = null;

        if (cat.getSex() == Genetics.Sex.MALE) {
            for (SimplyCatEntity cat1 : list) {
                if (cat.canMate(cat1) && cat.distanceToSqr(cat1) < d0) {
                    entityCat = cat1;
                    d0 = cat.distanceToSqr(cat1);
                }
            }
        }

        return entityCat;
    }

    private void startPregnancy() {
        int litterSize;
        if (target.getKittens() <= 0) {
            litterSize = level.random.nextInt(6) + 1; // at least 1 kitten, max of 6
        } else {
            litterSize = level.random.nextInt(6 - target.getKittens()) + 1; // max of 6, minus already accrued kittens
        }
        target.setBreedingStatus("ispregnant", true);
        target.setKittens(litterSize);
        target.addFather(cat, target.getKittens()); // save father nbt data to mother cat for each kitten added to litterSize

        if (litterSize == 6 || target.getKittens() == 6 || level.random.nextInt(4) == 0) { // full litter OR 25% chance ends heat
            target.setBreedingStatus("inheat", false);
            target.setTimeCycle("pregnancy", SCConfig.pregnancy_timer.get()); // starts pregnancy timer
        }
    }
}
