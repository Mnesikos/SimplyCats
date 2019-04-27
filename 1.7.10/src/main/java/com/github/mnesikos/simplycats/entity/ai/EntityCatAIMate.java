package com.github.mnesikos.simplycats.entity.ai;

import java.util.Iterator;
import java.util.List;
import java.util.Random;

import net.minecraft.entity.ai.EntityAIBase;
import net.minecraft.entity.item.EntityXPOrb;
import net.minecraft.world.World;

import com.github.mnesikos.simplycats.entity.EntityCat;

public class EntityCatAIMate extends EntityAIBase {

	private EntityCat CAT;
	private EntityCat TARGET;
	private World WORLD;
	private double MOVEMENT_SPEED;
	private int BABY_DELAY;

	public EntityCatAIMate(EntityCat entityCat, double d) {
		this.CAT = entityCat;
		this.WORLD = entityCat.worldObj;
		this.MOVEMENT_SPEED = d;
		this.setMutexBits(3);
	}

	@Override
	public boolean shouldExecute() {
		if ((this.CAT.getSex() == 0) || (this.CAT.getSex() == 1 && this.TARGET != null && !this.TARGET.getBreedingStatus("inheat")) || (this.CAT.getSex() == 1 && this.CAT.getHeatTimer() > 0)) {
			return false;
		} else
			this.TARGET = this.getNearbyMate();
			return this.TARGET != null;
	}

	private EntityCat getNearbyMate() {
		float f = 8.0F;
		List<?> list = this.WORLD.getEntitiesWithinAABB(this.CAT.getClass(), this.CAT.boundingBox.expand((double) f, (double) f, (double) f));
		double d0 = Double.MAX_VALUE;
		EntityCat cat = null;
		Iterator<?> iterator = list.iterator();

		if (this.CAT.getSex() == 1)
			while (iterator.hasNext()) {
				EntityCat cat1 = (EntityCat) iterator.next();
	
				if (this.CAT.canMateWith(cat1) && this.CAT.getDistanceSqToEntity(cat1) < d0) {
					cat = cat1;
					d0 = this.CAT.getDistanceSqToEntity(cat1);
				}
			}

		return cat;
	}

	@Override
	public boolean continueExecuting() {
		boolean maleCooldownCheck = this.CAT.getSex() == 1 && this.CAT.getHeatTimer() == 0;
		boolean femaleHeatCheck = this.TARGET.getSex() == 0 && this.TARGET.getBreedingStatus("inheat");
		return maleCooldownCheck && this.TARGET.isEntityAlive() && femaleHeatCheck && this.BABY_DELAY < 60;
	}

	@Override
	public void resetTask() {
		this.TARGET = null;
		this.BABY_DELAY = 0;
	}

	@Override
	public void updateTask() {
		this.CAT.getLookHelper().setLookPositionWithEntity(this.TARGET, 10.0F, (float) this.CAT.getVerticalFaceSpeed());
		this.TARGET.getLookHelper().setLookPositionWithEntity(this.CAT, 10.0F, (float) this.TARGET.getVerticalFaceSpeed());
		this.CAT.getNavigator().tryMoveToEntityLiving(this.TARGET, this.MOVEMENT_SPEED);
		this.TARGET.getNavigator().tryMoveToEntityLiving(this.CAT, this.MOVEMENT_SPEED);
		++this.BABY_DELAY;

		if (this.BABY_DELAY >= 60 && this.CAT.getDistanceSqToEntity(this.TARGET) < 9.0D) {
			int litterSize = this.WORLD.rand.nextInt(6) + 1; //at least 1 kitten, max of 6
			for (int i = 0; i < litterSize; i++)
				this.spawnBaby();
			this.CAT.setHeatTimer(24000);
		}
	}

	private void spawnBaby() {
		EntityCat child = this.CAT.createChild(this.TARGET);

		if (child != null) {
			child.setGrowingAge(-24000);
			child.setLocationAndAngles(this.CAT.posX, this.CAT.posY, this.CAT.posZ, 0.0F, 0.0F);
			this.WORLD.spawnEntityInWorld(child);

			Random random = this.CAT.getRNG();
			for (int i = 0; i < 7; ++i) {
				double d0 = random.nextGaussian() * 0.02D;
				double d1 = random.nextGaussian() * 0.02D;
				double d2 = random.nextGaussian() * 0.02D;
				this.WORLD.spawnParticle("heart", this.CAT.posX + (double) (random.nextFloat() * this.CAT.width * 2.0F) - (double) this.CAT.width, this.CAT.posY + 0.5D + (double) (random.nextFloat() * this.CAT.height), this.CAT.posZ + (double) (random.nextFloat() * this.CAT.width * 2.0F) - (double) this.CAT.width, d0, d1, d2);
			}

			if (this.WORLD.getGameRules().getGameRuleBooleanValue("doMobLoot")) {
				this.WORLD.spawnEntityInWorld(new EntityXPOrb(this.WORLD, this.CAT.posX, this.CAT.posY, this.CAT.posZ, random.nextInt(7) + 1));
			}
		}
	}

}
