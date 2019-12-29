package com.github.mnesikos.simplycats.entity.ai;

import java.util.Iterator;
import java.util.List;

import com.github.mnesikos.simplycats.configuration.SimplyCatsConfig;
import com.github.mnesikos.simplycats.entity.core.Genetics;
import net.minecraft.entity.ai.EntityAIBase;
import net.minecraft.world.World;

import com.github.mnesikos.simplycats.entity.EntityCat;

public class EntityCatAIMate extends EntityAIBase {

	private EntityCat CAT;
	private EntityCat TARGET;
	private World WORLD;
	private double MOVE_SPEED;
	private int MATE_DELAY;

	private double NEARBY_SIZE_CHECK = 16.0D;

	public EntityCatAIMate(EntityCat entityCat, double speed) {
		this.CAT = entityCat;
		this.WORLD = entityCat.worldObj;
		this.MOVE_SPEED = speed;
		this.setMutexBits(3);
	}

	@Override
	public boolean shouldExecute() {
		if (this.CAT.getSex().equals(Genetics.Sex.FEMALE.getName()))
			return false;

		List LIST = this.WORLD.getEntitiesWithinAABB(this.CAT.getClass(), this.CAT.getBoundingBox().expand(NEARBY_SIZE_CHECK, NEARBY_SIZE_CHECK, NEARBY_SIZE_CHECK));
		if (LIST.size() >= SimplyCatsConfig.BREEDING_LIMIT)
			return false;

		else if ((this.TARGET != null && !this.TARGET.getBreedingStatus("inheat")) || (this.CAT.getMateTimer() > 0))
			return false;

		else {
			this.TARGET = this.getNearbyMate();
			return this.TARGET != null && this.CAT.getEntitySenses().canSee(this.TARGET);
		}
	}

	@Override
	public boolean continueExecuting() {
		boolean maleCooldownCheck = this.CAT.getSex().equals(Genetics.Sex.MALE.getName()) && this.CAT.getMateTimer() == 0;
		boolean femaleHeatCheck = this.TARGET.getSex().equals(Genetics.Sex.FEMALE.getName()) && this.TARGET.getBreedingStatus("inheat");
		List LIST = this.WORLD.getEntitiesWithinAABB(this.CAT.getClass(), this.CAT.getBoundingBox().expand(NEARBY_SIZE_CHECK, NEARBY_SIZE_CHECK, NEARBY_SIZE_CHECK));
		return maleCooldownCheck && this.TARGET.isEntityAlive() && femaleHeatCheck && this.MATE_DELAY < 60 && LIST.size() < SimplyCatsConfig.BREEDING_LIMIT && this.CAT.getEntitySenses().canSee(this.TARGET);
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

		if (this.MATE_DELAY >= 60 && this.CAT.getDistanceSqToEntity(this.TARGET) < 4.0D) {
			if (this.WORLD.rand.nextInt(4) <= 2) // 75% chance of success
				this.startPregnancy();
			this.CAT.setMateTimer(SimplyCatsConfig.MALE_COOLDOWN); // starts male cooldown
		}
	}

	private EntityCat getNearbyMate() {
		List<?> list = this.WORLD.getEntitiesWithinAABB(this.CAT.getClass(), this.CAT.boundingBox.expand(NEARBY_SIZE_CHECK, NEARBY_SIZE_CHECK, NEARBY_SIZE_CHECK));
		double d0 = Double.MAX_VALUE;
		EntityCat entityCat = null;
		Iterator<?> iterator = list.iterator();

		if (this.CAT.getSex().equals(Genetics.Sex.MALE.getName()))
			while (iterator.hasNext()) {
				EntityCat cat1 = (EntityCat) iterator.next();
	
				if (this.CAT.canMateWith(cat1) && this.CAT.getDistanceSqToEntity(cat1) < d0) {
					entityCat = cat1;
					d0 = this.CAT.getDistanceSqToEntity(cat1);
				}
			}

		return entityCat;
	}

	private void startPregnancy() {
		int litterSize;
		if (this.TARGET.getKittens() <= 0) {
			litterSize = this.WORLD.rand.nextInt(6) + 1; // at least 1 kitten, max of 6
		} else {
			litterSize = this.WORLD.rand.nextInt(6 - this.TARGET.getKittens()) + 1; // max of 6, minus already accrued kittens
		}
		this.TARGET.setBreedingStatus("ispregnant", true);
		this.TARGET.setKittens(litterSize);
		this.TARGET.addFather(this.CAT, this.TARGET.getKittens()); // save father nbt data to mother cat for each kitten added to litterSize

		if (litterSize == 6 || this.TARGET.getKittens() == 6 || this.WORLD.rand.nextInt(4) == 0) { // full litter OR 25% chance ends heat
			this.TARGET.setBreedingStatus("inheat", false);
			this.TARGET.setTimeCycle("pregnancy", SimplyCatsConfig.PREGNANCY_TIMER); // starts pregnancy timer
		}
	}

}
