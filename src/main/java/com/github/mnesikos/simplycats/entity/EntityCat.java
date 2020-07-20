package com.github.mnesikos.simplycats.entity;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import com.github.mnesikos.simplycats.configuration.SimplyCatsConfig;
import com.github.mnesikos.simplycats.entity.ai.CatAIBirth;
import com.github.mnesikos.simplycats.entity.ai.CatAIWander;
import com.github.mnesikos.simplycats.entity.ai.EntityCatAIMate;

import com.github.mnesikos.simplycats.entity.core.Genetics;
import com.github.mnesikos.simplycats.init.ModItems;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityAgeable;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.*;
import net.minecraft.entity.passive.EntityAnimal;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemFood;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.ChatComponentTranslation;
import net.minecraft.util.DamageSource;
import net.minecraft.world.World;
import net.minecraftforge.oredict.OreDictionary;

public class EntityCat extends AbstractCat {
	private static final int FIXED = 5;
	private static final int IN_HEAT = 13;
	private static final int IS_PREGNANT = 14;
	private static final int MATE_TIMER = 15;
	private static final int KITTENS = 2;
	private static final int MOTHER = 3;
	private static final int FATHER = 4;

	private EntityAITempt aiTempt;

	public EntityCat(World world) {
		super(world);
		this.setSize(0.6F, 0.8F);
		this.setParent("mother", "Unknown");
		this.setParent("father", "Unknown");
		this.initEntityAI();
		this.onInitialSpawn();
	}

	protected void initEntityAI() {
		this.getNavigator().setAvoidsWater(true);
		this.aiSit = new EntityAISit(this);
		this.aiTempt = new EntityAITempt(this, 1.2D, ModItems.TREAT_BAG, false);
		this.tasks.addTask(1, new EntityAISwimming(this));
		this.tasks.addTask(2, this.aiSit);
		this.tasks.addTask(3, this.aiTempt);
		if (!this.isSitting())
			this.tasks.addTask(3, new EntityAIFollowParent(this, 1.0D));
		if (!this.isFixed())
			this.tasks.addTask(3, new EntityCatAIMate(this, 1.2D));
		this.tasks.addTask(4, new CatAIBirth(this));
		this.tasks.addTask(5, new EntityAILeapAtTarget(this, 0.4F));
		this.tasks.addTask(6, new EntityAIOcelotAttack(this));
		this.tasks.addTask(7, new CatAIWander(this, 1.0D));
		this.tasks.addTask(9, new EntityAIWatchClosest(this, EntityLiving.class, 7.0F));
		this.tasks.addTask(10, new EntityAILookIdle(this));
	}

	@Override
	protected void applyEntityAttributes() {
		super.applyEntityAttributes();
		getEntityAttribute(SharedMonsterAttributes.maxHealth).setBaseValue(10.0D);
		getEntityAttribute(SharedMonsterAttributes.movementSpeed).setBaseValue(0.3D);
		getEntityAttribute(SharedMonsterAttributes.knockbackResistance).setBaseValue(0.7D);
		getAttributeMap().registerAttribute(SharedMonsterAttributes.attackDamage);
		getEntityAttribute(SharedMonsterAttributes.attackDamage).setBaseValue(0.5D);
	}

	@Override
	protected void entityInit() { // VANILLA: 6-12, 16-17 && SUPER: 18-30 && THIS: 2-5, 13-15
		super.entityInit();

		this.dataWatcher.addObject(FIXED, (byte)0);
		this.dataWatcher.addObject(IN_HEAT, (byte)0);
		this.dataWatcher.addObject(IS_PREGNANT, (byte)0);
		this.dataWatcher.addObject(MATE_TIMER, 0);
		this.dataWatcher.addObject(KITTENS, 0);
		this.dataWatcher.addObject(MOTHER, "Unknown");
		this.dataWatcher.addObject(FATHER, "Unknown");
	}

	public void onInitialSpawn() {
		if (!this.worldObj.isRemote)
			if (this.isTamed())
				this.aiSit.setSitting(!this.isSitting());
		if (this.getSex().equals(Genetics.Sex.FEMALE.getName()) && !this.isFixed())
			this.setTimeCycle("end", this.worldObj.rand.nextInt(SimplyCatsConfig.HEAT_COOLDOWN));
	}

	@Override
	public void onUpdate() {
		super.onUpdate();

		if (!this.worldObj.isRemote && !this.isChild() && !this.isFixed() && this.getSex().equals(Genetics.Sex.FEMALE.getName())) { //if female & adult & not fixed
			if (this.getBreedingStatus("inheat")) //if in heat
				if (this.getMateTimer() <= 0) { //and timer is finished (reaching 0 after being in positives)
					if (!this.getBreedingStatus("ispregnant")) //and not pregnant
						setTimeCycle("end", SimplyCatsConfig.HEAT_COOLDOWN); //sets out of heat for 16 (default) minecraft days
					else { //or if IS pregnant
						setTimeCycle("pregnant", SimplyCatsConfig.PREGNANCY_TIMER); //and heat time runs out, starts pregnancy timer for birth
						this.setBreedingStatus("inheat", false); //sets out of heat
					}
				}
			if (!this.getBreedingStatus("inheat")) { //if not in heat
				if (this.getMateTimer() >= 0) { //and timer is finished (reaching 0 after being in negatives)
					if (!this.getBreedingStatus("ispregnant")) //and not pregnant
						setTimeCycle("start", SimplyCatsConfig.HEAT_TIMER); //sets in heat for 2 minecraft days
				}
			}
		}
	}

	@Override
	public void onLivingUpdate() {
		super.onLivingUpdate();

		if (!this.isChild() && !this.isFixed()) { //if not a child & not fixed
			int mateTimer = this.getMateTimer();
			if (this.getSex().equals(Genetics.Sex.FEMALE.getName())) {
				if (this.getBreedingStatus("inheat") || this.getBreedingStatus("ispregnant")) {
					--mateTimer;
					if (this.getBreedingStatus("inheat")) {
						if (mateTimer % 10 == 0) {

							double d0 = this.rand.nextGaussian() * 0.02D;
							double d1 = this.rand.nextGaussian() * 0.02D;
							double d2 = this.rand.nextGaussian() * 0.02D;
							this.worldObj.spawnParticle("heart", this.posX + (double)(this.rand.nextFloat() * this.width * 2.0F) - (double)this.width, this.posY + 0.5D + (double)(this.rand.nextFloat() * this.height), this.posZ + (double)(this.rand.nextFloat() * this.width * 2.0F) - (double)this.width, d0, d1, d2);
						}
					}
				}
				else if (!this.getBreedingStatus("inheat") && !this.getBreedingStatus("ispregnant"))
					++mateTimer;
			} else if (this.getSex().equals(Genetics.Sex.MALE.getName())) {
				if (mateTimer > 0)
					--mateTimer;
				else if (mateTimer <= 0)
					mateTimer = 0;
			}
			this.setMateTimer(mateTimer);
		}
	}

	@Override
	public boolean isAIEnabled() {
		return true;
	}

	@Override
	public boolean attackEntityFrom(DamageSource par1DamageSource, float par2) {
		if (this.isEntityInvulnerable()) {
			return false;
		} else {
			if (this.aiSit != null) {
				this.aiSit.setSitting(false);
			}

			return super.attackEntityFrom(par1DamageSource, par2);
		}
	}

	@Override
	public boolean attackEntityAsMob(Entity target) {
		return target.attackEntityFrom(DamageSource.causeMobDamage(this), 3.0F);
	}

	public void setParent (String parent, String name) {
		if (parent.equals("mother"))
			this.dataWatcher.updateObject(MOTHER, name);
		else if (parent.equals("father"))
			this.dataWatcher.updateObject(FATHER, name);
	}

	public String getParent(String parent) {
		if (parent.equals("mother"))
			return this.dataWatcher.getWatchableObjectString(MOTHER);
		else if (parent.equals("father"))
			return this.dataWatcher.getWatchableObjectString(FATHER);
		else
			return "Error";
	}

	public void setFixed(byte fixed) { // 1 = fixed, 0 = intact
		this.dataWatcher.updateObject(FIXED, fixed);
	}

	public boolean isFixed() {
		return this.dataWatcher.getWatchableObjectByte(FIXED) == 1;
	}

	public byte getIsFixed() {
		return this.dataWatcher.getWatchableObjectByte(FIXED);
	}

	public void setTimeCycle(String s, int time) {
		if (s.equals("start")) {
			this.setBreedingStatus("inheat", true);
			this.setMateTimer(time);
		}
		if (s.equals("end")) {
			this.setBreedingStatus("inheat", false);
			this.setMateTimer(-time);
		}
		if (s.equals("pregnancy")) {
			this.setMateTimer(time);
		}
	}

	public void setBreedingStatus(String string, boolean parTrue) {
		if (string.equals("inheat")) {
			if (parTrue)
				this.dataWatcher.updateObject(IN_HEAT, (byte)1);
			else
				this.dataWatcher.updateObject(IN_HEAT, (byte) 0);
		}
		else if (string.equals("ispregnant")) {
			if (parTrue)
				this.dataWatcher.updateObject(IS_PREGNANT, (byte) 1);
			else
				this.dataWatcher.updateObject(IS_PREGNANT, (byte) 0);
		}
	}

	public boolean getBreedingStatus(String string) {
		if (string.equals("inheat"))
			return this.dataWatcher.getWatchableObjectByte(IN_HEAT) == 1;
		else if (string.equals("ispregnant"))
			return this.dataWatcher.getWatchableObjectByte(IS_PREGNANT) == 1;
		return false;
	}

	public void setMateTimer(int time) {
		this.dataWatcher.updateObject(MATE_TIMER, time);
	}

	public int getMateTimer() {
		return this.dataWatcher.getWatchableObjectInt(MATE_TIMER);
	}

	public void setKittens(int kittens) {
		if (getKittens() <= 0 || kittens == 0)
			this.dataWatcher.updateObject(KITTENS, kittens);
		else if (getKittens() > 0)
			this.dataWatcher.updateObject(KITTENS, this.getKittens() + kittens);
	}

	public int getKittens() {
		return this.dataWatcher.getWatchableObjectInt(KITTENS);
	}

	public void addFather(EntityCat father, int size) {
		for (int i = 0; i < size; i++) {
			if(!this.getEntityData().hasKey("Father" + i) || (this.getEntityData().hasKey("Father" + i) && this.getEntityData().getCompoundTag("Father" + i) == null)) {
				NBTTagCompound fatherData = new NBTTagCompound();
				father.writeToNBT(fatherData);
				this.getEntityData().setTag("Father" + i, fatherData);
				//break;
			}
		}
	}

	private void setFather(int i, NBTBase father) {
		if (this.getEntityData().hasKey("Father" + i))
			this.getEntityData().setTag("Father" + i, father);
	}

	public NBTTagCompound getFather(int i) {
		return this.getEntityData().getCompoundTag("Father" + i);
	}

	/*public void setHeatTimer(int time) {
		this.dataWatcher.updateObject(31, Integer.valueOf(time));
	}

	public int getHeatTimer() {
		return this.dataWatcher.getWatchableObjectInt(31);
	}*/

	@Override
	public void writeEntityToNBT(NBTTagCompound nbt) {
		super.writeEntityToNBT(nbt);
		nbt.setByte("Fixed", this.getIsFixed());
		if (this.getSex().equals(Genetics.Sex.FEMALE.getName())) {
			nbt.setBoolean("InHeat", this.getBreedingStatus("inheat"));
			nbt.setBoolean("IsPregnant", this.getBreedingStatus("ispregnant"));
			nbt.setInteger("Kittens", this.getKittens());
			for (int i = 0; i < 5; i++)
				nbt.setTag("Father" + i, this.getFather(i));
		}
		nbt.setInteger("Timer", this.getMateTimer());
		nbt.setString("Mother", this.getParent("mother"));
		nbt.setString("Father", this.getParent("father"));
	}

	@Override
	public void readEntityFromNBT(NBTTagCompound nbt) {
		super.readEntityFromNBT(nbt);
		this.setFixed(nbt.getByte("Fixed"));
		if (this.getSex().equals(Genetics.Sex.FEMALE.getName()) && !this.isFixed()) {
			this.setBreedingStatus("inheat", nbt.getBoolean("InHeat"));
			this.setBreedingStatus("ispregnant", nbt.getBoolean("IsPregnant"));
			this.setKittens(nbt.getInteger("Kittens"));
			for (int i = 0; i < 5; i++) {
				this.setFather(i, nbt.getTag("Father" + i));
			}
		}
		if (!this.isFixed())
			this.setMateTimer(nbt.getInteger("Timer"));
		this.setParent("mother", nbt.getString("Mother"));
		this.setParent("father", nbt.getString("Father"));
	}

	@Override
	protected boolean canDespawn() {
		return false;
	}

	@Override
	public boolean canMateWith(EntityAnimal target) {
		if (target == this)
			return false;
		if (!(target instanceof EntityCat))
			return false;
		if (target.isChild() || this.isChild())
			return false;
		if (this.isSitting() || ((EntityCat) target).isSitting())
			return false;

		EntityCat mate = (EntityCat) target;
		if (mate.isFixed() || this.isFixed())
			return false;

		if ((this.getSex().equals(Genetics.Sex.MALE.getName()) && this.getMateTimer() == 0)) // if (this) is male & not on a cooldown
			return (mate.getSex().equals(Genetics.Sex.FEMALE.getName()) && mate.getBreedingStatus("inheat")); // returns true if (mate) is female & in heat
		else
			return false;
	}

	@Override
	public boolean isBreedingItem(ItemStack item) {
		return false;
	}

	@Override
	public EntityCat createChild(EntityAgeable parFather) {
		return (EntityCat) super.createChild(parFather);
	}

	private boolean isFoodItem(ItemStack item) {
		return FoodItems(item);
	}

	private static boolean FoodItems(ItemStack stack) {
		List<Item> foods = new ArrayList<Item>();
		List<ItemStack> meatrawDictionary = OreDictionary.getOres("listAllmeatraw");
		for (ItemStack stk : meatrawDictionary) {
			foods.add(stk.getItem());
		}
		List<ItemStack> meatcookedDictionary = OreDictionary.getOres("listAllmeatcooked");
		for (ItemStack stk : meatcookedDictionary) {
			foods.add(stk.getItem());
		}
		List<ItemStack> fishfreshDictionary = OreDictionary.getOres("listAllfishfresh");
		for (ItemStack stk : fishfreshDictionary) {
			foods.add(stk.getItem());
		}
		List<ItemStack> fishcookedDictionary = OreDictionary.getOres("listAllfishcooked");
		for (ItemStack stk : fishcookedDictionary) {
			foods.add(stk.getItem());
		}
		Iterator foodsItr = foods.iterator();
		Item i;
		do {
			if (!foodsItr.hasNext()) {
				return false;
			}

			i = (Item)foodsItr.next();
		} while(stack.getItem() != i);

		return true;
	}

	@Override
	public boolean interact(EntityPlayer player) {
		ItemStack stack = player.inventory.getCurrentItem();

		if (stack != null) {
			if (this.isTamed() && this.getOwner() == player) {
				if (stack.getItem() == Items.blaze_powder && player.isSneaking()) {
					if (!this.isFixed() && this.getMateTimer() != 0) {
						this.setMateTimer(this.getMateTimer() / 2); // heat inducer, used on females not in  heat to quicken the process
						if (!player.capabilities.isCreativeMode)
							--stack.stackSize;
						if (stack.stackSize <= 0)
							player.inventory.setInventorySlotContents(player.inventory.currentItem, (ItemStack) null);
					}
					return true;

				}
			}

			if (!this.isTamed() || (this.isTamed() && this.getOwner() == player)) {
				if (stack.getItem() == Items.shears && player.isSneaking()) {
					if (!this.isFixed()) {
						this.setFixed((byte) 1);
						if (this.worldObj.isRemote) {
							String FIXED_FEMALE = new ChatComponentTranslation("chat.info.success_fixed_female").getFormattedText();
							String FIXED_MALE = new ChatComponentTranslation("chat.info.success_fixed_male").getFormattedText();
							player.addChatComponentMessage(new ChatComponentText(this.getName() + " " + (this.getSex().equals(Genetics.Sex.FEMALE.getName()) ? FIXED_FEMALE : FIXED_MALE)));
						}
					}
					return true;

				}
			}

			if (isFoodItem(stack)) {
				ItemFood food = (ItemFood) stack.getItem();
				if (this.getHealth() < this.getMaxHealth())
					this.heal((float) food.func_150905_g(stack));
				if (!player.capabilities.isCreativeMode)
					--stack.stackSize;
				if (stack.stackSize <= 0)
					player.inventory.setInventorySlotContents(player.inventory.currentItem, (ItemStack) null);
				return true;
			}

			if (stack.getItem() == Items.stick && player.isSneaking()) {
				if (this.worldObj.isRemote) {
					if (this.isFixed()) {
						if (this.getSex().equals(Genetics.Sex.FEMALE.getName()))
							player.addChatComponentMessage(new ChatComponentTranslation("chat.info.fixed_female"));
						else
							player.addChatComponentMessage(new ChatComponentTranslation("chat.info.fixed_male"));
					} else if (this.getSex().equals(Genetics.Sex.FEMALE.getName()) && this.getBreedingStatus("ispregnant")) {
						if (!this.getBreedingStatus("inheat"))
							player.addChatComponentMessage(new ChatComponentText(new ChatComponentTranslation("chat.info.pregnant").getFormattedText() + " " + this.getMateTimer()/* + parents + this.getParent("mother") + "/" + this.getParent("father")*/));
						else
							player.addChatComponentMessage(new ChatComponentText(new ChatComponentTranslation("chat.info.pregnant_heat").getFormattedText() + " " + this.getMateTimer()));
					} else if (this.getSex().equals(Genetics.Sex.FEMALE.getName()) && this.getBreedingStatus("inheat"))
						player.addChatComponentMessage(new ChatComponentText(new ChatComponentTranslation("chat.info.in_heat").getFormattedText() + " " + this.getMateTimer()/* + parents + this.getParent("mother") + "/" + this.getParent("father")*/));
					else if (this.getSex().equals(Genetics.Sex.FEMALE.getName()) && !this.getBreedingStatus("inheat"))
						player.addChatComponentMessage(new ChatComponentText(new ChatComponentTranslation("chat.info.not_in_heat").getFormattedText() + " " + this.getMateTimer()/* + parents + this.getParent("mother") + "/" + this.getParent("father")*/));
					else if (this.getSex().equals(Genetics.Sex.MALE.getName()))
						player.addChatComponentMessage(new ChatComponentText(new ChatComponentTranslation("chat.info.male").getFormattedText() + " " + this.getMateTimer()/* + parents + this.getParent("mother") + "/" + this.getParent("father")*/));
				}
				return true;

			} else if (stack.getItem() == Items.bone && player.isSneaking()) {
				if (this.worldObj.isRemote) {
					if (this.getSex().equals(Genetics.Sex.FEMALE.getName()) && this.getBreedingStatus("ispregnant"))
						player.addChatComponentMessage(new ChatComponentText(new ChatComponentTranslation("chat.info.kitten_count").getFormattedText() + " " + this.getKittens()));
				}
				return true;

			} else if ((this.aiTempt == null || this.aiTempt.isRunning()) && stack.getItem() == ModItems.TREAT_BAG && player.getDistanceSq(this.posX, this.posY, this.posZ) < 9.0D) {
				if (player.isSneaking()) {
					if (this.hasHomePos()) {
						this.resetHomePos();
						if (this.worldObj.isRemote)
							player.addChatComponentMessage(new ChatComponentText(new ChatComponentTranslation("chat.info.remove_home").getFormattedText() + " " + this.getName()));
						return true;
					} else {
						this.setHomePos((int)this.posX, (int)this.posY, (int)this.posZ);
						if (this.worldObj.isRemote)
							player.addChatComponentMessage(new ChatComponentText(this.getName() +
									new ChatComponentTranslation("chat.info.set_home").getFormattedText() +
									" " + getHomePosX() + ", " + getHomePosY() + ", " + getHomePosZ()));
						return true;
					}
				} else {
					if (this.hasHomePos())
						if (this.worldObj.isRemote)
							player.addChatComponentMessage(new ChatComponentText(getHomePosX() + ", " + getHomePosY() + ", " + getHomePosZ()));
				}

			}
		}

		if (!this.worldObj.isRemote && this.getOwner() == player && !player.isSneaking()) {
			if (stack == null || (!this.isBreedingItem(stack) && !this.isFoodItem(stack))) {
				this.aiSit.setSitting(!this.isSitting());
				this.getNavigator().clearPathEntity();
				this.setAttackTarget(null);
			}
		}

		return super.interact(player);
	}

}
