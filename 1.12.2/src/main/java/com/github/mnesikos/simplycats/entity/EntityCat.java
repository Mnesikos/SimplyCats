package com.github.mnesikos.simplycats.entity;

import com.google.common.base.Predicate;
import net.minecraft.entity.*;
import net.minecraft.entity.ai.*;
import net.minecraft.entity.passive.EntityAnimal;
import net.minecraft.entity.passive.EntityChicken;
import net.minecraft.entity.passive.EntityTameable;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.ItemFood;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EnumHand;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.translation.I18n;
import net.minecraft.world.World;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class EntityCat extends EntityTameable {
    private static final DataParameter<Integer> TYPE;
    private static final DataParameter<Integer> BASE;
    private static final DataParameter<Integer> TABBY;
    private static final DataParameter<Integer> WHITE;
    private static final DataParameter<Integer> EYES;
    private static final DataParameter<Byte> SEX;
    private static final DataParameter<Boolean> IS_ANGRY;
    private static final DataParameter<Byte> IN_HEAT;
    private static final DataParameter<Byte> IS_PREGNANT;
    private static final DataParameter<Integer> HEAT_TIMER;
    private static final List<String> PHENO_LIST = new ArrayList<String>(3);

    public EntityCat(World world) {
        super(world);
        this.setSize(0.6F, 0.8F);
        this.setTamed(true);
        this.setCatPheno();
    }

    @Override
    protected void initEntityAI() {
        this.aiSit = new EntityAISit(this);
        this.tasks.addTask(1, new EntityAISwimming(this));
        this.tasks.addTask(2, this.aiSit);
        this.tasks.addTask(3, new EntityAIFollowParent(this, 1.0D));
        //this.tasks.addTask(3, new EntityCatAIMate(this, 0.8D));
        this.tasks.addTask(4, new EntityAIMoveIndoors(this));
        this.tasks.addTask(5, new EntityAILeapAtTarget(this, 0.4F));
        this.tasks.addTask(6, new EntityAIOcelotAttack(this));
        this.tasks.addTask(7, new EntityAIWanderAvoidWater(this, 1.0D));
        this.tasks.addTask(8, new EntityAIWatchClosest(this, EntityPlayer.class, 10.0F));
        this.tasks.addTask(8, new EntityAIWatchClosest(this, EntityCat.class, 5.0F));
        this.tasks.addTask(8, new EntityAILookIdle(this));
        this.tasks.addTask(9, new EntityAIWatchClosest(this, EntityLiving.class, 7.0F));
        this.targetTasks.addTask(1, new EntityAITargetNonTamed(this, EntityChicken.class, false, (Predicate)null));
    }

    @Override
    protected void applyEntityAttributes() {
        super.applyEntityAttributes();
        getEntityAttribute(SharedMonsterAttributes.MAX_HEALTH).setBaseValue(10.0D);
        getEntityAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).setBaseValue(0.3D);
        getEntityAttribute(SharedMonsterAttributes.KNOCKBACK_RESISTANCE).setBaseValue(0.7D);
        getAttributeMap().registerAttribute(SharedMonsterAttributes.ATTACK_DAMAGE);
        getEntityAttribute(SharedMonsterAttributes.ATTACK_DAMAGE).setBaseValue(1.0D);
    }

    @Override
    protected void entityInit() {
        super.entityInit();
        this.dataManager.register(TYPE, 0);
        this.dataManager.register(BASE, 0);
        this.dataManager.register(TABBY, 0);
        this.dataManager.register(WHITE, 0);
        this.dataManager.register(EYES, 0);
        this.dataManager.register(SEX, (byte)0);
        this.dataManager.register(IS_ANGRY, false);
        this.dataManager.register(IN_HEAT, (byte)0);
        this.dataManager.register(IS_PREGNANT, (byte)0);
        this.dataManager.register(HEAT_TIMER, 0);
    }

    @Override
    public void onUpdate() {
        super.onUpdate();

        if (!this.world.isRemote && !this.isChild() && this.getSex() == 0) {
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

        if (!this.world.isRemote && !this.isChild()) {
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
    public void setAttackTarget(EntityLivingBase target) {
        super.setAttackTarget(target);

        if (target == null) {
            this.setIsAngry(false);
        } else {
            this.setIsAngry(true);
        }
    }

    @Override
    public boolean attackEntityFrom(DamageSource source, float amount) {
        if (this.isEntityInvulnerable(source)) {
            return false;
        } else {
            if (this.aiSit != null) {
                this.aiSit.setSitting(false);
            }

            return super.attackEntityFrom(source, amount);
        }
    }

    @Override
    public boolean attackEntityAsMob(Entity entity) {
        return entity.attackEntityFrom(DamageSource.causeMobDamage(this), 3.0F);
    }

    private void setCatPheno() {
        selectSex();
        selectType();
        setBase(this.world.rand.nextInt(4));
        PHENO_LIST.add("tabby");
        PHENO_LIST.add("white");
        PHENO_LIST.add("eyes");
        for (int i = 0; i < PHENO_LIST.size(); i++)
            setMarkings(PHENO_LIST.get(i), selectMarkings(PHENO_LIST.get(i)));
    }

    public void setType(int type) {
        this.dataManager.set(TYPE, type);
    }

    public int getType() {
        return this.dataManager.get(TYPE);
    }

    private void selectType() {
        if (this.hasCustomName() && this.getName() == "Maverick" && this.getSex() == 1)
            setType(3);
        else
            setType(0);
    }

    public void setBase(int base) {
        this.dataManager.set(BASE, base);
    }

    public int getBase() {
        return this.dataManager.get(BASE);
    }

    public void setMarkings(String type, int n) {
        switch (type) {
            case "tabby":
                this.dataManager.set(TABBY, n);
            case "white":
                this.dataManager.set(WHITE, n);
            case "eyes":
                this.dataManager.set(EYES, n);
        }
    }

    public int getMarkingNum(String type) {
        switch (type) {
            case "tabby":
                return this.dataManager.get(TABBY);
            case "white":
                return this.dataManager.get(WHITE);
            case "eyes":
                return this.dataManager.get(EYES);
            default:
                return 0;
        }
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
                int check = this.world.rand.nextInt(3);
                if (check > 1 && this.getBase() == 0)
                    num = 1;
                else if (check > 1 && this.getBase() == 1)
                    num = 2;
                else
                    num = 0;
            }
        } else if (type.equals("white")) {
            num = this.world.rand.nextInt(7);
        } else if (type.equals("eyes")) {
            num = this.getMarkingNum("white") >= 5 ? this.world.rand.nextInt(5) : this.world.rand.nextInt(4);
        }
        setMarkings(type, num);
        return num;
    }

    public void setSex(byte sex) {
        this.dataManager.set(SEX, sex);
    }

    public byte getSex() {
        return this.dataManager.get(SEX);
    }

    private void selectSex() {
        if (this.world.rand.nextInt(2) == 0) {
            setSex((byte) 0); // Female
        } else {
            setSex((byte) 1); // Male
        }
    }

    protected void setIsAngry(boolean isAngry) {
        if (isAngry)
            this.dataManager.set(IS_ANGRY, true);
        else
            this.dataManager.set(IS_ANGRY, false);
    }

    public boolean getIsAngry() {
        return this.dataManager.get(IS_ANGRY);
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
                this.dataManager.set(IN_HEAT, (byte)1);
            else
                this.dataManager.set(IN_HEAT, (byte) 0);
        }
        else if (string.equals("ispregnant")) {
            if (parTrue)
                this.dataManager.set(IS_PREGNANT, (byte) 1);
            else
                this.dataManager.set(IS_PREGNANT, (byte) 0);
        }
    }

    public boolean getBreedingStatus(String string) {
        if (string.equals("inheat"))
            return this.dataManager.get(IN_HEAT) == 1;
        else if (string.equals("ispregnant"))
            return this.dataManager.get(IS_PREGNANT) == 1;
        return false;
    }

    public void setHeatTimer(int time) {
        this.dataManager.set(HEAT_TIMER, time);
    }

    public int getHeatTimer() {
        return this.dataManager.get(HEAT_TIMER);
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
        EntityCat child = new EntityCat(this.world);

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
        child.setOwnerId(this.getOwnerId());
        child.setTamed(true);
        return child;
    }

    @Override
    public boolean processInteract(EntityPlayer player, EnumHand hand) {
        ItemStack stack = player.getHeldItem(hand);

        if (this.isTamed()) {
            if (stack != null) {
                if (stack.getItem() instanceof ItemFood) {
                    ItemFood food = (ItemFood) stack.getItem();
                    if (stack.getItem() == Items.FISH && this.getHealth() < this.getMaxHealth()) {
                        if (!player.capabilities.isCreativeMode)
                            stack.shrink(1);
                        this.heal((float) food.getHealAmount(stack));
                        if (stack.getCount() <= 0)
                            player.inventory.setInventorySlotContents(player.inventory.currentItem, null);
                        return true;
                    } else if (stack.getItem() == Items.FISH && this.getHealth() == this.getMaxHealth() && this.getSex() == 0 && !this.getBreedingStatus("inheat") && !this.getBreedingStatus("ispregnant")) {
                        if (!player.capabilities.isCreativeMode)
                            stack.shrink(1);
                        this.setHeatTimer(-60);
                        if (stack.getCount() <= 0)
                            player.inventory.setInventorySlotContents(player.inventory.currentItem, null);
                        return true;
                    }
                } else if (stack.getItem() == Items.STICK /*&& !this.isChild()*/) {
                    if (this.world.isRemote && this.getSex() == 0 && this.getBreedingStatus("ispregnant"))
                        player.sendMessage(new TextComponentString("This female is pregnant. Ticks until birth: " + this.getHeatTimer()));
                    else if (this.world.isRemote && this.getSex() == 0 && this.getBreedingStatus("inheat"))
                        player.sendMessage(new TextComponentString("This female is in heat. Ticks left: " + this.getHeatTimer()));
                    else if (this.world.isRemote && this.getSex() == 0 && !this.getBreedingStatus("inheat"))
                        player.sendMessage(new TextComponentString("This female is not in heat. Ticks until next heat: " + this.getHeatTimer()));
                    else if (this.world.isRemote && this.getSex() == 1)
                        player.sendMessage(new TextComponentString("This is a male. Cooldown ticks left: " + this.getHeatTimer()));
                    return true;
                }
            }
            if (!this.world.isRemote && !this.isBreedingItem(stack)) {
                this.aiSit.setSitting(!this.isSitting());
                this.setAttackTarget(null);
            }

        }

        return super.processInteract(player, hand);
    }

    @Override
    public void fall(float distance, float damageMultiplier) {
    }

    @Override
    protected boolean canTriggerWalking() {
        return false;
    }

    @Override
    protected SoundEvent getAmbientSound() {
        if (this.getIsAngry()) {
            if (this.rand.nextInt(10) == 0)
                return SoundEvents.ENTITY_CAT_HISS;
            else
                return null;
        } else if (this.isInLove()) {
            if (this.rand.nextInt(10) == 0)
                return SoundEvents.ENTITY_CAT_PURR;
            else
                return null;
        } else {
            if (this.rand.nextInt(10) == 0) {
                if (this.rand.nextInt(10) == 0)
                    return SoundEvents.ENTITY_CAT_PURREOW;
                else
                    return SoundEvents.ENTITY_CAT_AMBIENT;
            }
        }
        return null;
    }

    @Override
    protected SoundEvent getHurtSound(DamageSource damageSourceIn) {
        return SoundEvents.ENTITY_CAT_HURT;
    }

    @Override
    protected SoundEvent getDeathSound() {
        return SoundEvents.ENTITY_CAT_DEATH;
    }

    @Override
    protected float getSoundVolume() {
        return 0.4F;
    }

    @Override
    public String getName() {
        if (this.hasCustomName())
            return this.getCustomNameTag();
        else
            return this.isTamed() ? I18n.translateToLocal("entity.Cat.name") : super.getName();
    }

    static {
        TYPE = EntityDataManager.createKey(EntityCat.class, DataSerializers.VARINT);
        BASE = EntityDataManager.createKey(EntityCat.class, DataSerializers.VARINT);
        TABBY = EntityDataManager.createKey(EntityCat.class, DataSerializers.VARINT);
        WHITE = EntityDataManager.createKey(EntityCat.class, DataSerializers.VARINT);
        EYES = EntityDataManager.createKey(EntityCat.class, DataSerializers.VARINT);
        SEX = EntityDataManager.createKey(EntityCat.class, DataSerializers.BYTE);
        IS_ANGRY = EntityDataManager.createKey(EntityCat.class, DataSerializers.BOOLEAN);
        IN_HEAT = EntityDataManager.createKey(EntityCat.class, DataSerializers.BYTE);
        IS_PREGNANT = EntityDataManager.createKey(EntityCat.class, DataSerializers.BYTE);
        HEAT_TIMER = EntityDataManager.createKey(EntityCat.class, DataSerializers.VARINT);
    }
}
