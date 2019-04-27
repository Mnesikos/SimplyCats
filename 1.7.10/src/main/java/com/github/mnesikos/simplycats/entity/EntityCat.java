package com.github.mnesikos.simplycats.entity;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import com.github.mnesikos.simplycats.entity.ai.EntityCatAIMate;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityAgeable;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.EntityAIAttackOnCollide;
import net.minecraft.entity.ai.EntityAIFollowParent;
import net.minecraft.entity.ai.EntityAILeapAtTarget;
import net.minecraft.entity.ai.EntityAILookIdle;
import net.minecraft.entity.ai.EntityAIMoveIndoors;
import net.minecraft.entity.ai.EntityAISwimming;
import net.minecraft.entity.ai.EntityAITargetNonTamed;
import net.minecraft.entity.ai.EntityAIWander;
import net.minecraft.entity.ai.EntityAIWatchClosest;
import net.minecraft.entity.passive.EntityAnimal;
import net.minecraft.entity.passive.EntityChicken;
import net.minecraft.entity.passive.EntityTameable;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.ItemFood;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.DamageSource;
import net.minecraft.util.StatCollector;
import net.minecraft.world.World;

public class EntityCat extends EntityTameable {
	private static final List<String> PHENO_LIST = new ArrayList<String>(3);

	public EntityCat(World world) {
		super(world);
		this.setSize(0.6F, 0.8F);
		this.setCatPheno();
		this.setTamed(true);
		this.getNavigator().setAvoidsWater(true);
		this.tasks.addTask(1, new EntityAISwimming(this));
		this.tasks.addTask(2, this.aiSit);
		this.tasks.addTask(3, new EntityAIFollowParent(this, 1.0D));
		this.tasks.addTask(3, new EntityCatAIMate(this, 0.8D));
		this.tasks.addTask(4, new EntityAIMoveIndoors(this));
		this.tasks.addTask(5, new EntityAILeapAtTarget(this, 0.4F));
		this.tasks.addTask(6, new EntityAIAttackOnCollide(this, 1.0D, true));
		this.tasks.addTask(7, new EntityAIWander(this, 1.0D));
		this.tasks.addTask(8, new EntityAIWatchClosest(this, EntityPlayer.class, 10.0F));
		this.tasks.addTask(8, new EntityAIWatchClosest(this, EntityCat.class, 5.0F));
		this.tasks.addTask(8, new EntityAILookIdle(this));
		this.tasks.addTask(9, new EntityAIWatchClosest(this, EntityLiving.class, 7.0F));
		this.targetTasks.addTask(1, new EntityAITargetNonTamed(this, EntityChicken.class, 750, false));
	}

	@Override
	protected void applyEntityAttributes() {
		super.applyEntityAttributes();
		getEntityAttribute(SharedMonsterAttributes.maxHealth).setBaseValue(10.0D);
		getEntityAttribute(SharedMonsterAttributes.movementSpeed).setBaseValue(0.3D);
		getEntityAttribute(SharedMonsterAttributes.knockbackResistance).setBaseValue(0.7D);
		getAttributeMap().registerAttribute(SharedMonsterAttributes.attackDamage);
		getEntityAttribute(SharedMonsterAttributes.attackDamage).setBaseValue(1.0D);
	}

	@Override
	protected void entityInit() { // avail: 3-5, 13-15, 18
		super.entityInit();
		this.dataWatcher.addObject(19, Integer.valueOf(0));
		this.dataWatcher.addObject(20, Byte.valueOf((byte) 0));
		this.dataWatcher.addObject(23, Byte.valueOf((byte) 0));
		this.dataWatcher.addObject(24, Integer.valueOf(0));
		this.dataWatcher.addObject(25, Integer.valueOf(0));
		this.dataWatcher.addObject(26, Integer.valueOf(0));
		if (this.getSex() == 0) {
			this.dataWatcher.addObject(30, Byte.valueOf((byte) 0));
			this.dataWatcher.addObject(2, Byte.valueOf((byte) 0));
			if (!this.isChild())
				this.dataWatcher.addObject(31, Integer.valueOf(-24000 * 8));
		}
		else
			this.dataWatcher.addObject(31, Integer.valueOf(0));
	}

	@Override
	public void onUpdate() {
		super.onUpdate();

		if (!this.worldObj.isRemote && !this.isChild() && this.getSex() == 0) {
			if (this.getBreedingStatus("inheat") && !this.getBreedingStatus("ispregnant"))
				if (this.getHeatTimer() <= 0)
					setHeatCycle("end", -24000 * 16);
			if (!this.getBreedingStatus("inheat") && !this.getBreedingStatus("ispregnant"))
				if (this.getHeatTimer() >= 0)
					setHeatCycle("start", 24000 * 2);
		}
	}

	@Override
	public void onLivingUpdate() {
		super.onLivingUpdate();

		if (!this.worldObj.isRemote && !this.isChild()) {
			int heatTimer = this.getHeatTimer();
			if (this.getSex() == 0) {
				if (this.getBreedingStatus("inheat") || this.getBreedingStatus("ispregnant"))
					--heatTimer;
				else if (!this.getBreedingStatus("inheat") && !this.getBreedingStatus("ispregnant"))
					++heatTimer;
			} else if (this.getSex() == 1) {
				if (heatTimer > 0)
					--heatTimer;
				else if (heatTimer <= 0)
					heatTimer = 0;
			}
			this.setHeatTimer(heatTimer);
		}
	}

	@Override
	public boolean isAIEnabled() {
		return true;
	}

	@Override
	public void setAttackTarget(EntityLivingBase target) {
		super.setAttackTarget(target);

		if (target == null) {
			this.setIsAngry(false);
		} else {
			this.setIsAngry(true);
		}
	}

	@Override
	public boolean attackEntityFrom(DamageSource par1DamageSource, float par2) {
		if (isEntityInvulnerable()) {
			return false;
		} else {
			this.aiSit.setSitting(false);
			return super.attackEntityFrom(par1DamageSource, par2);
		}
	}

	@Override
	public boolean attackEntityAsMob(Entity target) {
		return target.attackEntityFrom(DamageSource.causeMobDamage(this), 3.0F);
	}

	private void setCatPheno() {
		selectSex();
		selectType();
		setBase(this.worldObj.rand.nextInt(4));
		PHENO_LIST.add("tabby");
		PHENO_LIST.add("white");
		PHENO_LIST.add("eyes");
		for (int i = 0; i < PHENO_LIST.size(); i++)
			setMarkings(PHENO_LIST.get(i), selectMarkings(PHENO_LIST.get(i)));
	}

	public void setType(int type) {
		this.dataWatcher.updateObject(19, Integer.valueOf(type));
	}

	public int getType() {
		return this.dataWatcher.getWatchableObjectInt(19);
	}

	private void selectType() {
		if (this.hasCustomNameTag() && this.getCustomNameTag() == "Maverick" && this.getSex() == 1)
			setType(3);
		else
			setType(0);
	}

	public void setBase(int base) {
		this.dataWatcher.updateObject(20, Byte.valueOf((byte) base));
	}

	public int getBase() {
		return this.dataWatcher.getWatchableObjectByte(20);
	}

	public void setMarkings(String type, int n) {
		if (type.equals("tabby"))
			this.dataWatcher.updateObject(24, Integer.valueOf(n));
		else if (type.equals("white"))
			this.dataWatcher.updateObject(25, Integer.valueOf(n));
		else if (type.equals("eyes"))
			this.dataWatcher.updateObject(26, Integer.valueOf(n));
	}

	public int getMarkingNum(String type) {
		for (int i = 0; i < PHENO_LIST.size(); i++)
			if (type.equals(PHENO_LIST.get(i))) {
				int nbt = (24 + i);
				return this.dataWatcher.getWatchableObjectInt(nbt);
			}
		return 0;
	}

	public int selectMarkings(String type) {
		int num = 0;
		if (type == null) {
			return 0;
		} else if (type.equals("tabby")) {
			if (this.getBase() == 2)
				num = 3;
			else if (this.getBase() == 3)
				num = 4;
			else if (this.getBase() == 0 || this.getBase() == 1) {
				int check = this.worldObj.rand.nextInt(3);
				if (check > 1 && this.getBase() == 0)
					num = 1;
				else if (check > 1 && this.getBase() == 1)
					num = 2;
				else
					num = 0;
			}
		} else if (type.equals("white")) {
			num = this.worldObj.rand.nextInt(7);
		} else if (type.equals("eyes")) {
			num = this.getMarkingNum("white") >= 5 ? this.worldObj.rand.nextInt(5) : this.worldObj.rand.nextInt(4);
		}
		setMarkings(type, num);
		return num;
	}

	public void setSex(byte sex) {
		this.dataWatcher.updateObject(23, Byte.valueOf(sex));
	}

	public byte getSex() {
		return this.dataWatcher.getWatchableObjectByte(23);
	}

	private void selectSex() {
		if (this.worldObj.rand.nextInt(9) + 1 <= 5) {
			setSex((byte) 0); // Female
		} else {
			setSex((byte) 1); // Male
		}
	}

	protected void setIsAngry(boolean isAngry) {
		byte var2 = this.dataWatcher.getWatchableObjectByte(16);
		if (isAngry)
			this.dataWatcher.updateObject(16, Byte.valueOf((byte) (var2 | 2)));
		else
			this.dataWatcher.updateObject(16, Byte.valueOf((byte) (var2 & -3)));
	}

	public boolean getIsAngry() {
		return (this.dataWatcher.getWatchableObjectByte(16) & 2) != 0;
	}

	protected void setHeatCycle(String s, int time) {
        if (s.equals("start")) {
			this.setBreedingStatus("inheat", true);
			this.setHeatTimer(time);
		}
        if (s.equals("end")) {
			this.setBreedingStatus("inheat", false);
			this.setHeatTimer(-time);
		}
	}

	public void setBreedingStatus(String string, boolean parTrue) {
        if (string.equals("inheat")) {
			if (parTrue)
				this.dataWatcher.updateObject(30, Byte.valueOf((byte) 1));
			else
				this.dataWatcher.updateObject(30, Byte.valueOf((byte) 0));
		}
        else if (string.equals("ispregnant")) {
			if (parTrue)
				this.dataWatcher.updateObject(2, Byte.valueOf((byte) 1));
			else
				this.dataWatcher.updateObject(2, Byte.valueOf((byte) 0));
		}
	}

	public boolean getBreedingStatus(String string) {
        if (string.equals("inheat"))
			return this.dataWatcher.getWatchableObjectByte(30) == 1;
        else if (string.equals("ispregnant"))
			return this.dataWatcher.getWatchableObjectByte(2) == 1;
		return false;
	}

	public void setHeatTimer(int time) {
		this.dataWatcher.updateObject(31, Integer.valueOf(time));
	}

	public int getHeatTimer() {
		return this.dataWatcher.getWatchableObjectInt(31);
	}

	@Override
	public void writeEntityToNBT(NBTTagCompound nbt) {
		super.writeEntityToNBT(nbt);
		nbt.setBoolean("Angry", this.getIsAngry());
		nbt.setInteger("Type", this.getType());
		nbt.setInteger("Base", this.getBase());
		for (int i = 0; i < PHENO_LIST.size(); i++)
			nbt.setInteger(PHENO_LIST.get(i), this.getMarkingNum(PHENO_LIST.get(i)));
		nbt.setByte("Sex", this.getSex());
		if (this.getSex() == 0) {
			nbt.setBoolean("InHeat", this.getBreedingStatus("inheat"));
			nbt.setBoolean("IsPregnant", this.getBreedingStatus("ispregnant"));
		}
		nbt.setInteger("Timer", this.getHeatTimer());
	}

	@Override
	public void readEntityFromNBT(NBTTagCompound nbt) {
		super.readEntityFromNBT(nbt);
		this.setIsAngry(nbt.getBoolean("Angry"));
		this.setType(nbt.getInteger("Type"));
		this.setBase(nbt.getInteger("Base"));
		for (int i = 0; i < PHENO_LIST.size(); i++)
			this.setMarkings(PHENO_LIST.get(i), nbt.getInteger(PHENO_LIST.get(i)));
		this.setSex(nbt.getByte("Sex"));
		if (this.getSex() == 0) {
			this.setBreedingStatus("inheat", nbt.getBoolean("InHeat"));
			this.setBreedingStatus("ispregnant", nbt.getBoolean("IsPregnant"));
		}
		this.setHeatTimer(nbt.getInteger("Timer"));
	}

	@Override
	protected boolean canDespawn() {
		return false;
	}

	@Override
	public boolean canMateWith(EntityAnimal entity) {
		if (entity == this)
			return false;
		if (!(entity instanceof EntityCat))
			return false;
		if (entity.isChild())
			return false;
		if (this.isSitting() || ((EntityCat) entity).isSitting())
			return false;

		EntityCat mate = (EntityCat) entity;
		if (mate.getType() == 3 || this.getType() == 3)
			return false;

		if ((this.getSex() == 1 && this.getHeatTimer() == 0) || (mate.getSex() == 1 && mate.getHeatTimer() == 0))
			return ((mate.getSex() == 0 && mate.getBreedingStatus("inheat") && this.getSex() == 1) || (mate.getSex() == 1 && this.getSex() == 0 && this.getBreedingStatus("inheat")));

		return false;
	}

	@Override
	public boolean isBreedingItem(ItemStack item) {
		return false;
	}

	@Override
	public EntityCat createChild(EntityAgeable parParent2) {
		EntityCat parent2 = (EntityCat) parParent2;
		EntityCat child = new EntityCat(this.worldObj);

		int chance = this.rand.nextInt(4);
		boolean quarterChance = chance == 0;
		boolean halfChance = chance <= 1;
		int black = 0; int grey = 1; int red = 2; int cream = 3;

		int base = 0;
		if (this.getBase() == parent2.getBase()) {
			if (this.getBase() == black)
				base = quarterChance ? grey : black;
			else if (this.getBase() == red)
				base = quarterChance ? cream : red;
			else
				base = this.getBase();
		}
		else if (this.getBase() == black || parent2.getBase() == black) {
			if (this.getBase() == grey || parent2.getBase() == grey)
				base = halfChance ? black : grey;
			else if ((this.getBase() == red && this.getSex() == 0) || (parent2.getBase() == red && parent2.getSex() == 0))
				base = quarterChance ? cream : red; // TODO: TORTIE
			else if ((this.getBase() == red && this.getSex() == 1) || (parent2.getBase() == red && parent2.getSex() == 1))
				base = quarterChance ? grey : black;
			else if ((this.getBase() == cream && this.getSex() == 0) || (parent2.getBase() == cream && parent2.getSex() == 0))
				base = halfChance ? cream : red;
			else if ((this.getBase() == cream && this.getSex() == 1) || (parent2.getBase() == cream && parent2.getSex() == 1))
				base = halfChance ? grey : black;
		}
		else if (this.getBase() == grey || parent2.getBase() == grey) {
			if ((this.getBase() == red && this.getSex() == 0) || (parent2.getBase() == red && parent2.getSex() == 0))
				base = halfChance ? red : cream;
			else if ((this.getBase() == red && this.getSex() == 1) || (parent2.getBase() == red && parent2.getSex() == 1))
				base = halfChance ? black : grey;
			else if ((this.getBase() == cream && this.getSex() == 0) || (parent2.getBase() == cream && parent2.getSex() == 0))
				base = cream;
			else if ((this.getBase() == cream && this.getSex() == 1) || (parent2.getBase() == cream && parent2.getSex() == 1))
				base = grey;
		}
		else if (this.getBase() == red || parent2.getBase() == red) {
			if (this.getBase() == cream || parent2.getBase() == cream)
				base = quarterChance ? red : cream;
		}

		int tabby = base + 1;
		if (this.getMarkingNum("tabby") != 0 || parent2.getMarkingNum("tabby") != 0) {
			if (this.getMarkingNum("tabby") != 0 && parent2.getMarkingNum("tabby") != 0)
				tabby = quarterChance ? 0 : tabby;
			else if (this.getMarkingNum("tabby") == 0 || parent2.getMarkingNum("tabby") == 0)
				tabby = halfChance ? 0 : tabby;
		} else if (this.getMarkingNum("tabby") == 0 && parent2.getMarkingNum("tabby") == 0)
			tabby = 0;
		tabby = base == 2 || base == 3 ? base + 1 : tabby;

		Random rand = new Random();
		int whiteMin = 0;
		int whiteMax = 6;
		if (this.getMarkingNum("white") > parent2.getMarkingNum("white")) {
			whiteMin = parent2.getMarkingNum("white") - 1;
			whiteMax = this.getMarkingNum("white");
		} else if (this.getMarkingNum("white") < parent2.getMarkingNum("white") || this.getMarkingNum("white") == parent2.getMarkingNum("white")) {
			whiteMin = this.getMarkingNum("white") - 1;
			whiteMax = parent2.getMarkingNum("white");
		}
		whiteMin = whiteMin < 0 ? 0 : whiteMin;
		whiteMax = whiteMax > 6 ? 6 : whiteMax;
		int white = rand.nextInt((whiteMax - whiteMin) + 1) + whiteMin;

		int eyesMin = 0;
		int eyesMax = 4;
		if (this.getMarkingNum("eyes") > parent2.getMarkingNum("eyes")) {
			eyesMin = parent2.getMarkingNum("eyes") - 1;
			eyesMax = this.getMarkingNum("eyes");
		} else if (this.getMarkingNum("eyes") < parent2.getMarkingNum("eyes") || this.getMarkingNum("eyes") == parent2.getMarkingNum("eyes")) {
			eyesMin = this.getMarkingNum("eyes") - 1;
			eyesMax = parent2.getMarkingNum("eyes");
		}
		eyesMin = eyesMin < 0 ? 0 : eyesMin;
		eyesMax = eyesMax > 4 ? 4 : eyesMax;
		eyesMax = eyesMax == 4 ? (child.getMarkingNum("white") < 5 ? (eyesMin < 3 ? eyesMin+1 : 3) : eyesMax) : eyesMax;
		int eyes = rand.nextInt((eyesMax - eyesMin) + 1) + eyesMin;
		eyes = this.getMarkingNum("eyes") == 4 ? (parent2.getMarkingNum("eyes") == 4 ? 4 : eyes) : eyes;

		child.setBase(base);
		child.setMarkings("tabby", tabby);
		child.setMarkings("white", white);
		child.setMarkings("eyes", eyes);
		child.func_152115_b(this.func_152113_b());
		child.setTamed(true);
		return child;
	}

	@Override
	public boolean interact(EntityPlayer player) {
		ItemStack stack = player.inventory.getCurrentItem();

		if (this.isTamed()) {
			if (stack != null) {
				if (stack.getItem() instanceof ItemFood) {
					ItemFood food = (ItemFood) stack.getItem();
					if (stack.getItem() == Items.fish && this.getHealth() < this.getMaxHealth()) {
						if (!player.capabilities.isCreativeMode)
							--stack.stackSize;
						this.heal((float) food.func_150905_g(stack));
						if (stack.stackSize <= 0)
							player.inventory.setInventorySlotContents(player.inventory.currentItem, (ItemStack) null);
						return true;
					} else if (stack.getItem() == Items.fish && this.getHealth() == this.getMaxHealth() && this.getSex() == 0 && !this.getBreedingStatus("inheat") && !this.getBreedingStatus("ispregnant")) {
						if (!player.capabilities.isCreativeMode)
							--stack.stackSize;
						this.setHeatTimer(-60);
						if (stack.stackSize <= 0)
							player.inventory.setInventorySlotContents(player.inventory.currentItem, (ItemStack) null);
						return true;
					}
				} else if (stack.getItem() == Items.stick /*&& !this.isChild()*/) {
					if (this.worldObj.isRemote && this.getSex() == 0 && this.getBreedingStatus("ispregnant"))
						player.addChatComponentMessage(new ChatComponentText("This female is pregnant. Ticks until birth: " + this.getHeatTimer()));
					else if (this.worldObj.isRemote && this.getSex() == 0 && this.getBreedingStatus("inheat"))
						player.addChatComponentMessage(new ChatComponentText("This female is in heat. Ticks left: " + this.getHeatTimer()));
					else if (this.worldObj.isRemote && this.getSex() == 0 && !this.getBreedingStatus("inheat"))
						player.addChatComponentMessage(new ChatComponentText("This female is not in heat. Ticks until next heat: " + this.getHeatTimer()));
					else if (this.worldObj.isRemote && this.getSex() == 1)
						player.addChatComponentMessage(new ChatComponentText("This is a male. Cooldown ticks left: " + this.getHeatTimer()));
					return true;
				}
			}
			if (!this.worldObj.isRemote && !this.isBreedingItem(stack)) {
				this.aiSit.setSitting(!this.isSitting());
				this.setAttackTarget(null);
			}
		}

		return super.interact(player);
	}

	@Override
	protected void fall(float p_70069_1_) {
	}

	@Override
	protected boolean canTriggerWalking() {
		return false;
	}

	@Override
	protected String getLivingSound() {
		if (this.getIsAngry()) {
			if (this.rand.nextInt(10) == 0)
				return "mob.cat.hiss";
			else
				return null;
		} else if (this.isInLove()) {
			if (this.rand.nextInt(10) == 0)
				return "mob.cat.purr";
			else
				return null;
		} else {
			if (this.rand.nextInt(10) == 0) {
				if (this.rand.nextInt(10) == 0)
					return "mob.cat.purreow";
				else
					return "mob.cat.meow";
			}
		}
		return null;
	}

	@Override
	protected String getHurtSound() {
		return "mob.cat.hitt";
	}

	@Override
	protected String getDeathSound() {
		return "mob.cat.hitt";
	}

	@Override
	protected float getSoundVolume() {
		return 0.4F;
	}

	@Override
	public String getCommandSenderName() {
		return this.hasCustomNameTag() ? this.getCustomNameTag() : StatCollector.translateToLocal("entity.ProjectC.Cat.name");
	}

}
