package com.github.mnesikos.simplycats.entity;

import com.github.mnesikos.simplycats.configuration.SimplyCatsConfig;
import com.github.mnesikos.simplycats.entity.ai.EntityCatAIBirth;
import com.github.mnesikos.simplycats.entity.ai.EntityCatAIMate;
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
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EnumHand;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.world.World;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class EntityCat extends EntityTameable {
    private static final DataParameter<Integer> TYPE;
    private static final DataParameter<Integer> BASE;
    private static final DataParameter<Integer> TABBY;
    private static final DataParameter<Integer> TORTIE;
    private static final DataParameter<Integer> WHITE;
    private static final DataParameter<Integer> EYES;
    private static final DataParameter<Byte> SEX;
    private static final List<String> PHENO_LIST = new ArrayList<>(4);

    private static final DataParameter<Byte> IN_HEAT;
    private static final DataParameter<Byte> IS_PREGNANT;
    private static final DataParameter<Integer> MATE_TIMER;
    private static final DataParameter<Integer> KITTENS;
    private static final DataParameter<String> MOTHER;
    private static final DataParameter<String> FATHER;

    private static final DataParameter<Boolean> IS_ANGRY;

    public EntityCat(World world) {
        super(world);
        this.setSize(0.6F, 0.8F);
        this.setTamed(true);
        this.setCatPheno();
        this.setParent("mother", "Unknown");
        this.setParent("father", "Unknown");
    }

    @Override
    protected void initEntityAI() {
        this.aiSit = new EntityAISit(this);
        this.tasks.addTask(1, new EntityAISwimming(this));
        this.tasks.addTask(2, this.aiSit);
        this.tasks.addTask(3, new EntityAIFollowParent(this, 1.0D));
        this.tasks.addTask(3, new EntityCatAIMate(this, 0.8D));
        this.tasks.addTask(4, new EntityAIMoveIndoors(this));
        this.tasks.addTask(4, new EntityCatAIBirth(this));
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
        this.dataManager.register(TORTIE, 0);
        this.dataManager.register(WHITE, 0);
        this.dataManager.register(EYES, 0);
        this.dataManager.register(SEX, (byte)0);

        this.dataManager.register(IN_HEAT, (byte)0);
        this.dataManager.register(IS_PREGNANT, (byte)0);
        this.dataManager.register(MATE_TIMER, 0);
        this.dataManager.register(KITTENS, 0);
        this.dataManager.register(MOTHER, "Unknown");
        this.dataManager.register(FATHER, "Unknown");

        this.dataManager.register(IS_ANGRY, false);
    }

    @Override
    public void onUpdate() {
        super.onUpdate();

        if (!this.world.isRemote && !this.isChild() && this.getSex() == 0) { //if female & adult
            if (this.getBreedingStatus("inheat")) //if in heat
                if (this.getMateTimer() <= 0) { //and timer is finished (reaching 0 after being in positives)
                    if (!this.getBreedingStatus("ispregnant")) //and not pregnant
                        setTimeCycle("end", SimplyCatsConfig.heatCooldown); //sets out of heat for 16 minecraft days
                    else //or if IS pregnant
                        setTimeCycle("pregnant", SimplyCatsConfig.prengancyTimer); //and heat time runs out, starts pregnancy timer for birth
                }
            if (!this.getBreedingStatus("inheat")) { //if not in heat
                if (this.getMateTimer() >= 0) { //and timer is finished (reaching 0 after being in negatives)
                    if (!this.getBreedingStatus("ispregnant")) //and not pregnant
                        setTimeCycle("start", SimplyCatsConfig.heatTimer); //sets in heat for 2 minecraft days
                }
            }
        }
    }

    @Override
    public void onLivingUpdate() {
        super.onLivingUpdate();

        if (!this.world.isRemote && !this.isChild()) {
            int mateTimer = this.getMateTimer();
            if (this.getSex() == 0) {
                if (this.getBreedingStatus("inheat") || this.getBreedingStatus("ispregnant"))
                    --mateTimer;
                else if (!this.getBreedingStatus("inheat") && !this.getBreedingStatus("ispregnant"))
                    ++mateTimer;
            } else if (this.getSex() == 1) {
                if (mateTimer > 0)
                    --mateTimer;
                else if (mateTimer <= 0)
                    mateTimer = 0;
            }
            this.setMateTimer(mateTimer);
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

    public void setParent (String parent, String name) {
        if (parent.equals("mother"))
            this.dataManager.set(MOTHER, name);
        else if (parent.equals("father"))
            this.dataManager.set(FATHER, name);
    }

    public String getParent(String parent) {
        if (parent.equals("mother"))
            return this.dataManager.get(MOTHER);
        else if (parent.equals("father"))
            return this.dataManager.get(FATHER);
        else
            return "Error";
    }

    private void setCatPheno() {
        selectSex();
        selectType();
        setBase(this.world.rand.nextInt(4));
        PHENO_LIST.add("tabby");
        PHENO_LIST.add("tortie");
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
            case "tortie":
                this.dataManager.set(TORTIE, n);
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
            case "tortie":
                return this.dataManager.get(TORTIE);
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
            if (this.getBase() == 2 || this.getBase() == 3) // red tabby || cream tabby
                num = this.getBase() == 2 ? 3 : 4; // if red ? then red : else cream
            else if (this.getBase() == 0 || this.getBase() == 1) { // black || grey
                int check = this.world.rand.nextInt(4);
                if (check > 1 && this.getBase() == 0)
                    num = 1; // black tabby
                else if (check > 1 && this.getBase() == 1)
                    num = 2; // grey tabby
                else
                    num = 0; // no tabby
            }
        } else if (type.equals("tortie")) {
            // TODO
            if (this.getSex() == 1 || this.getBase() == 2 || this.getBase() == 3) // males != tortie || full reds || full creams
                num = 0;
            else {
                int check = this.world.rand.nextInt(4);
                if (check == 0 && this.getBase() == 0)
                    num = 1; // black tortie
                else if (check == 0 && this.getBase() == 1)
                    num = 2; // grey tortie
                else
                    num = 0; // no tortie
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

    public void setMateTimer(int time) {
        this.dataManager.set(MATE_TIMER, time);
    }

    public int getMateTimer() {
        return this.dataManager.get(MATE_TIMER);
    }

    public void setKittens(int kittens) {
        if (getKittens("total") <= 0 || kittens == 0)
            this.dataManager.set(KITTENS, kittens);
        else if (getKittens("total") > 0)
            this.dataManager.set(KITTENS, this.getKittens("total") + kittens);
    }

    public int getKittens(String s) {
        return this.dataManager.get(KITTENS);
    }

    public void addFather(EntityCat father) {
        for (int i = 0; i < 5; i++) {
            if(!this.getEntityData().hasKey("Father" + i) || (this.getEntityData().hasKey("Father" + i) && this.getEntityData().getCompoundTag("Father" + i) == null)) {
                this.getEntityData().setTag("Father" + i, father.writeToNBT(new NBTTagCompound()));
                break;
            }
        }
    }

    public void setFather(int i, NBTBase father) {
        if (this.getEntityData().getCompoundTag("Father" + i) != null)
            this.getEntityData().setTag("Father" + i, father);
    }

    public NBTTagCompound getFather(int i) {
        return this.getEntityData().getCompoundTag("Father" + i);
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
            nbt.setInteger("Kittens", this.getKittens("total"));
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
        this.setIsAngry(nbt.getBoolean("Angry"));
        this.setType(nbt.getInteger("Type"));
        this.setBase(nbt.getInteger("Base"));
        for (int i = 0; i < PHENO_LIST.size(); i++)
            this.setMarkings(PHENO_LIST.get(i), nbt.getInteger(PHENO_LIST.get(i)));
        this.setSex(nbt.getByte("Sex"));
        if (this.getSex() == 0) {
            this.setBreedingStatus("inheat", nbt.getBoolean("InHeat"));
            this.setBreedingStatus("ispregnant", nbt.getBoolean("IsPregnant"));
            this.setKittens(nbt.getInteger("Kittens"));
            for (int i = 0; i < 5; i++) {
                this.setFather(i, nbt.getTag("Father" + i));
            }
        }
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
        if (mate.getType() == 3 || this.getType() == 3)
            return false;

        if ((this.getSex() == 1 && this.getMateTimer() == 0)) // if (this) is male & not on a cooldown
            return (mate.getSex() == 0 && mate.getBreedingStatus("inheat")); // returns true if (mate) is female & in heat
        else
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
        boolean parent1Tortie = this.getMarkingNum("tortie") != 0;
        boolean parent2Tortie = parent2.getMarkingNum("tortie") != 0;

        int chance = this.rand.nextInt(4);
        final boolean quarterChance = chance == 0;
        final boolean halfChance = chance <= 1;
        final int black = 0; final int grey = 1; final int red = 2; final int cream = 3;

        int base = 0;
        int tortie = 0;
        if (this.getBase() == parent2.getBase()) {
            if (this.getBase() == black) {
                base = quarterChance ? grey : black; // black x black = 25% grey, 75% black
                if (parent1Tortie || parent2Tortie) { // black male x tortie mother adds half chance red boys, half chance tortie girls
                    if (child.getSex() == 1 && halfChance)
                        base = quarterChance ? cream : red;
                    if (child.getSex() == 0 && halfChance)
                        tortie = base == grey ? 2 : 1;
                }
            }
            else if (this.getBase() == red) // red x red = 25% cream, 75% red
                base = quarterChance ? cream : red;
            else {
                base = this.getBase(); // grey x grey = grey, cream x cream = cream
                if (parent1Tortie || parent2Tortie) { // grey male x tortie mother adds half chance red boys, half chance tortie girls
                    if (child.getSex() == 1 && halfChance)
                        base = cream;
                    if (child.getSex() == 0 && halfChance)
                        tortie = 2;
                }
            }
        }
        else if (this.getBase() == black || parent2.getBase() == black) {
            if (this.getBase() == grey || parent2.getBase() == grey) // black x grey = 50% black, 50% grey
                base = halfChance ? black : grey;
            else if (this.getBase() == red || parent2.getBase() == red) {
                if (this.getSex() == 0 || parent2.getSex() == 0) {
                    // black x female red = 25% cream boys, 75% red boys, 25% cream tortie girls, 75% red tortie girls
                    if (child.getSex() == 1)
                        base = quarterChance ? cream : red;
                    else {
                        base = quarterChance ? grey : black;
                        tortie = base == grey ? 2 : 1;
                    }
                } else if (this.getSex() == 1 || parent2.getSex() == 1) {
                    // black x male red = 25% grey boys, 75% black boys, 25% cream tortie girls, 75% red tortie girls
                    base = quarterChance ? grey : black;
                    if (child.getSex() == 0)
                        tortie = base == grey ? 2 : 1;
                }
            }
            else if (this.getBase() == cream|| parent2.getBase() == cream) {
                if (this.getSex() == 0 || parent2.getSex() == 0) {
                    // black x female cream = 50/50 cream/red boys, 50/50 cream/red tortie girls
                    if (child.getSex() == 1)
                        base = halfChance ? cream : red;
                    else {
                        base = halfChance ? grey : black;
                        tortie = base == grey ? 2 : 1;
                    }
                } else if (this.getSex() == 1 || parent2.getSex() == 1) {
                    // black x male cream = 50/50 grey/black boys, 50/50 cream/red tortie girls
                    base = halfChance ? grey : black;
                    if (child.getSex() == 0)
                        tortie = base == grey ? 2 : 1;
                }
            }
        }
        else if (this.getBase() == grey || parent2.getBase() == grey) {
            if (this.getBase() == red || parent2.getBase() == red) {
                if (this.getSex() == 0 || parent2.getSex() == 0) {
                    // grey x female red = 50/50 red/cream boys, 50/50 red/cream tortie girls
                    if (child.getSex() == 1)
                        base = halfChance ? red : cream;
                    else {
                        base = halfChance ? black : grey;
                        tortie = base == grey ? 2 : 1;
                    }
                } else if (this.getSex() == 1 || parent2.getSex() == 1) {
                    // grey x male red = 50/50 black/grey boys, 50/50 red/cream tortie girls
                    base = halfChance ? black : grey;
                    if (child.getSex() == 0)
                        tortie = base == grey ? 2 : 1;
                }
            } else if (this.getBase() == cream || parent2.getBase() == cream) {
                if (this.getSex() == 0 || parent2.getSex() == 0) {
                    // grey x female cream = 100% cream boys, 100% cream tortie girls
                    if (child.getSex() == 1)
                        base = cream;
                    else {
                        base = grey;
                        tortie = 2;
                    }
                } else if (this.getSex() == 1 || parent2.getSex() == 1) {
                    // grey x male cream = 100% grey boys, 100% cream tortie girls
                    base = grey;
                    if (child.getSex() == 0)
                        tortie = 2;
                }
            }
        }
        else if (this.getBase() == red || parent2.getBase() == red) {
            if (this.getBase() == cream || parent2.getBase() == cream) // red x cream = 50/50 red/cream
                base = halfChance ? red : cream;
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
        eyesMax = eyesMax == 4 ? (child.getMarkingNum("white") < 5 ? (eyesMin < 3 ? eyesMin+1 : 3) : eyesMax) : eyesMax; // removes blue eyed child possibility based on white
        int eyes = rand.nextInt((eyesMax - eyesMin) + 1) + eyesMin;
        eyes = this.getMarkingNum("eyes") == 4 && parent2.getMarkingNum("eyes") == 4 ? (child.getMarkingNum("white") >= 5 ? 4 : this.world.rand.nextInt(4)) : eyes;

        child.setBase(base);
        child.setMarkings("tabby", tabby);
        child.setMarkings("tortie", tortie);
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
                    if (stack.getItem() == Items.FISH) {
                        if (this.getHealth() < this.getMaxHealth()) {
                            this.heal((float) food.getHealAmount(stack));
                        } else if (this.getMateTimer() > 0)
                            this.setMateTimer(600);
                        else if (this.getMateTimer() < 0)
                            this.setMateTimer(-600);
                        if (!player.capabilities.isCreativeMode)
                            stack.shrink(1);
                        if (stack.getCount() <= 0)
                            player.inventory.setInventorySlotContents(player.inventory.currentItem, null);
                        return true;
                    }
                } else if (stack.getItem() == Items.STICK) {
                    if (this.world.isRemote) {
                        String pregnant = new TextComponentTranslation("chat.info.pregnant").getFormattedText();
                        String inheat = new TextComponentTranslation("chat.info.in_heat").getFormattedText();
                        String noheat = new TextComponentTranslation("chat.info.not_in_heat").getFormattedText();
                        String male = new TextComponentTranslation("chat.info.male").getFormattedText();
                        String parents = new TextComponentTranslation("chat.info.parents").getFormattedText();
                        if (this.getSex() == 0 && this.getBreedingStatus("ispregnant")) {
                            if (!this.getBreedingStatus("inheat"))
                                player.sendMessage(new TextComponentString(pregnant + this.getMateTimer() + parents + this.getParent("mother") + "/" + this.getParent("father")));
                            else
                                player.sendMessage(new TextComponentString("This cat is pregnant, but still in heat for: " + this.getMateTimer()));
                        }
                        else if (this.getSex() == 0 && this.getBreedingStatus("inheat"))
                            player.sendMessage(new TextComponentTranslation(inheat + this.getMateTimer() + parents + this.getParent("mother") + "/" + this.getParent("father")));
                        else if (this.getSex() == 0 && !this.getBreedingStatus("inheat"))
                            player.sendMessage(new TextComponentTranslation(noheat + this.getMateTimer() + parents + this.getParent("mother") + "/" + this.getParent("father")));
                        else if (this.getSex() == 1)
                            player.sendMessage(new TextComponentTranslation(male + this.getMateTimer() + parents + this.getParent("mother") + "/" + this.getParent("father")));
                    }
                    return true;
                } else if (stack.getItem() == Items.BONE) {
                    if (this.world.isRemote) {
                        if (this.getSex() == 0 && this.getBreedingStatus("ispregnant"))
                            player.sendMessage(new TextComponentString("Kitten count: " + this.getKittens("total")));
                    }
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
            return this.isTamed() ? new TextComponentTranslation("entity.Cat.name").toString() : super.getName();
    }

    static {
        TYPE = EntityDataManager.createKey(EntityCat.class, DataSerializers.VARINT);
        BASE = EntityDataManager.createKey(EntityCat.class, DataSerializers.VARINT);
        TABBY = EntityDataManager.createKey(EntityCat.class, DataSerializers.VARINT);
        TORTIE = EntityDataManager.createKey(EntityCat.class, DataSerializers.VARINT);
        WHITE = EntityDataManager.createKey(EntityCat.class, DataSerializers.VARINT);
        EYES = EntityDataManager.createKey(EntityCat.class, DataSerializers.VARINT);
        SEX = EntityDataManager.createKey(EntityCat.class, DataSerializers.BYTE);

        IN_HEAT = EntityDataManager.createKey(EntityCat.class, DataSerializers.BYTE);
        IS_PREGNANT = EntityDataManager.createKey(EntityCat.class, DataSerializers.BYTE);
        MATE_TIMER = EntityDataManager.createKey(EntityCat.class, DataSerializers.VARINT);
        KITTENS = EntityDataManager.createKey(EntityCat.class, DataSerializers.VARINT);
        MOTHER = EntityDataManager.createKey(EntityCat.class, DataSerializers.STRING);
        FATHER = EntityDataManager.createKey(EntityCat.class, DataSerializers.STRING);

        IS_ANGRY = EntityDataManager.createKey(EntityCat.class, DataSerializers.BOOLEAN);
    }
}
