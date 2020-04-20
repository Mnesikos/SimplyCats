package com.github.mnesikos.simplycats.entity;

import com.github.mnesikos.simplycats.Ref;
import com.github.mnesikos.simplycats.configuration.SCConfig;
import com.github.mnesikos.simplycats.entity.ai.*;
import com.github.mnesikos.simplycats.entity.core.Genetics;
import com.github.mnesikos.simplycats.event.SCEvents;
import com.github.mnesikos.simplycats.init.ModItems;
import com.google.common.base.Predicate;
import net.minecraft.entity.*;
import net.minecraft.entity.ai.*;
import net.minecraft.entity.passive.*;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.ItemFood;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.util.*;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.EnumDifficulty;
import net.minecraft.world.World;

import javax.annotation.Nullable;

public class EntityCat extends AbstractCat {
    private static final DataParameter<Byte> FIXED;
    private static final DataParameter<Byte> IN_HEAT;
    private static final DataParameter<Byte> IS_PREGNANT;
    private static final DataParameter<Integer> MATE_TIMER;
    private static final DataParameter<Integer> KITTENS;
    private static final DataParameter<String> MOTHER;
    private static final DataParameter<String> FATHER;

    private EntityAITempt aiTempt;
    private CatAITargetNearest aiTargetNearest;
    private Vec3d nearestLaser;

    public EntityCat(World world) {
        super(world);
        this.setSize(0.6F, 0.8F);
        this.setParent("mother", "Unknown");
        this.setParent("father", "Unknown");
    }

    public Vec3d getNearestLaser(){
        return nearestLaser;
    }

    public void setNearestLaser(Vec3d vec){
        this.nearestLaser = vec;
    }

    @Override
    protected void initEntityAI() {
        this.aiSit = new EntityAISit(this);
        this.aiTempt = new EntityAITempt(this, 1.2D, ModItems.TREAT_BAG, false);
        this.tasks.addTask(1, new EntityAISwimming(this));
        this.tasks.addTask(2, this.aiSit);
        this.tasks.addTask(3, this.aiTempt);
        if (!this.isSitting())
            this.tasks.addTask(3, new EntityAIFollowParent(this, 1.0D));
        if (!this.isFixed())
            this.tasks.addTask(3, new CatAIMate(this, 1.2D));
        this.tasks.addTask(4, new CatAIBirth(this));
        this.tasks.addTask(5, new EntityAILeapAtTarget(this, 0.4F));
        this.tasks.addTask(6, new CatAIAttack(this));
        this.tasks.addTask(7, new CatAIWander(this, 1.0D));
        //this.tasks.addTask(8, new CatAIWanderAvoidWater(this, 1.0D));
        this.tasks.addTask(9, new EntityAIWatchClosest(this, EntityLiving.class, 7.0F));
        this.tasks.addTask(10, new EntityAILookIdle(this));
        this.aiTargetNearest = new CatAITargetNearest<>(this, EntityLivingBase.class, true, new Predicate<EntityLivingBase>() {
            @Override
            public boolean apply(@Nullable EntityLivingBase entity) {
                //return SCEvents.isRatEntity(entity) && SCEvents.isBirdEntity(entity);
                return entity instanceof EntityChicken || (!(entity instanceof EntityCat) && !(entity instanceof EntityPlayer) && SCEvents.isEntityPrey(entity));
            }
        });
        this.targetTasks.addTask(1, this.aiTargetNearest);
    }

    @Override
    protected void applyEntityAttributes() {
        super.applyEntityAttributes();
        getEntityAttribute(SharedMonsterAttributes.MAX_HEALTH).setBaseValue(14.0D);
        getEntityAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).setBaseValue(0.3D);
        getEntityAttribute(SharedMonsterAttributes.KNOCKBACK_RESISTANCE).setBaseValue(0.7D);
        getAttributeMap().registerAttribute(SharedMonsterAttributes.ATTACK_DAMAGE);
        getEntityAttribute(SharedMonsterAttributes.ATTACK_DAMAGE).setBaseValue(2.0D);
    }

    @Override
    protected void entityInit() {
        super.entityInit();

        this.dataManager.register(FIXED, (byte)0);
        this.dataManager.register(IN_HEAT, (byte)0);
        this.dataManager.register(IS_PREGNANT, (byte)0);
        this.dataManager.register(MATE_TIMER, 0);
        this.dataManager.register(KITTENS, 0);
        this.dataManager.register(MOTHER, "Unknown");
        this.dataManager.register(FATHER, "Unknown");
    }

    @Override
    protected void updateAITasks() {
        if (this.world.getDifficulty() == EnumDifficulty.PEACEFUL || !SCConfig.ATTACK_AI)
            this.targetTasks.removeTask(aiTargetNearest);

        if (this.getMoveHelper().isUpdating()) {
            double d0 = this.getMoveHelper().getSpeed();

            if (d0 == 0.6D) {
                this.setSneaking(true);
                this.setSprinting(false);
            }
            else if (d0 == 1.33D) {
                this.setSneaking(false);
                this.setSprinting(true);
            }
            else {
                this.setSneaking(false);
                this.setSprinting(false);
            }
        } else {
            this.setSneaking(false);
            this.setSprinting(false);
        }
    }

    @Override
    public IEntityLivingData onInitialSpawn(DifficultyInstance difficulty, @Nullable IEntityLivingData livingdata) {
        if (!this.world.isRemote)
            if (this.isTamed())
                this.aiSit.setSitting(!this.isSitting());
        if (this.getSex().equals(Genetics.Sex.FEMALE.getName()) && !this.isFixed())
            this.setTimeCycle("end", this.world.rand.nextInt(SCConfig.HEAT_COOLDOWN));
        return super.onInitialSpawn(difficulty, livingdata);
    }

    @Override
    public void onUpdate() {
        super.onUpdate();
        if(this.getNearestLaser() != null) {
            if(this.isSitting())
                this.setSitting(false);
            this.getNavigator().tryMoveToXYZ(this.getNearestLaser().x, this.getNearestLaser().y, this.getNearestLaser().z, 1.2D);
        }

        if (!this.world.isRemote && !this.isChild() && !this.isFixed() && this.getSex().equals(Genetics.Sex.FEMALE.getName())) { //if female & adult & not fixed
            if (this.getBreedingStatus("inheat")) //if in heat
                if (this.getMateTimer() <= 0) { //and timer is finished (reaching 0 after being in positives)
                    if (!this.getBreedingStatus("ispregnant")) //and not pregnant
                        setTimeCycle("end", SCConfig.HEAT_COOLDOWN); //sets out of heat for 16 (default) minecraft days
                    else { //or if IS pregnant
                        setTimeCycle("pregnant", SCConfig.PREGNANCY_TIMER); //and heat time runs out, starts pregnancy timer for birth
                        this.setBreedingStatus("inheat", false); //sets out of heat
                    }
                }
            if (!this.getBreedingStatus("inheat")) { //if not in heat
                if (this.getMateTimer() >= 0) { //and timer is finished (reaching 0 after being in negatives)
                    if (!this.getBreedingStatus("ispregnant")) //and not pregnant
                        setTimeCycle("start", SCConfig.HEAT_TIMER); //sets in heat for 2 minecraft days
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
                            this.world.spawnParticle(EnumParticleTypes.HEART, this.posX + (double)(this.rand.nextFloat() * this.width * 2.0F) - (double)this.width, this.posY + 0.5D + (double)(this.rand.nextFloat() * this.height), this.posZ + (double)(this.rand.nextFloat() * this.width * 2.0F) - (double)this.width, d0, d1, d2);
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

        if (!this.world.isRemote && this.getAttackTarget() == null && this.isAngry())
            this.setAngry(false);
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
        float damage = (int) this.getEntityAttribute(SharedMonsterAttributes.ATTACK_DAMAGE).getAttributeValue();
        if (SCEvents.isRatEntity(entity))
            damage *= 3.0F;
        if (this.isSneaking() || this.isSprinting())
            damage *= 2.0F;
        return entity.attackEntityFrom(DamageSource.causeMobDamage(this), damage);
    }

    @Override
    public void setAttackTarget(@Nullable EntityLivingBase entitylivingbaseIn) {
        super.setAttackTarget(entitylivingbaseIn);

        if (entitylivingbaseIn == null)
            this.setAngry(false);
        else if (!this.isTamed())
            this.setAngry(true);
    }

    public void setParent (String parent, String name) {
        if (parent.equals("mother"))
            this.dataManager.set(MOTHER, name);
        else if (parent.equals("father"))
            this.dataManager.set(FATHER, name);
    }

    public String getParent(String parent) {
        switch (parent) {
            case "mother":
                return this.dataManager.get(MOTHER);
            case "father":
                return this.dataManager.get(FATHER);
            default:
                return "Error";
        }
    }

    public void setFixed(byte fixed) { // 1 = fixed, 0 = intact
        this.dataManager.set(FIXED, fixed);
    }

    public boolean isFixed() {
        return this.dataManager.get(FIXED) == 1;
    }

    public byte getIsFixed() {
        return this.dataManager.get(FIXED);
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
        if (getKittens() <= 0 || kittens == 0)
            this.dataManager.set(KITTENS, kittens);
        else if (getKittens() > 0)
            this.dataManager.set(KITTENS, this.getKittens() + kittens);
    }

    public int getKittens() {
        return this.dataManager.get(KITTENS);
    }

    public void addFather(EntityCat father, int size) {
        for (int i = 0; i < size; i++) {
            if(!this.getEntityData().hasKey("Father" + i) || (this.getEntityData().hasKey("Father" + i) && this.getEntityData().getCompoundTag("Father" + i) == null)) {
                this.getEntityData().setTag("Father" + i, father.writeToNBT(new NBTTagCompound()));
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
        return Ref.catFoodItems(item);
    }

    @Override
    public boolean processInteract(EntityPlayer player, EnumHand hand) {
        ItemStack stack = player.getHeldItem(hand);

        if (!stack.isEmpty()) {
            if (this.isTamed() && this.isOwner(player)) {
                if (stack.getItem() == Items.BLAZE_POWDER && player.isSneaking()) {
                    if (!this.isFixed() && this.getMateTimer() != 0) {
                        this.setMateTimer(this.getMateTimer() / 2); // heat inducer, used on females not in heat to quicken the process
                        if (!player.capabilities.isCreativeMode)
                            stack.shrink(1);
                        if (stack.getCount() <= 0)
                            player.inventory.setInventorySlotContents(player.inventory.currentItem, ItemStack.EMPTY);
                    }
                    return true;

                }
            }

            if (!this.isTamed() || (this.isTamed() && this.isOwner(player))) {
                if (stack.getItem() == Items.SHEARS && player.isSneaking()) {
                    if (!this.isFixed()) {
                        this.setFixed((byte) 1);
                        if (this.world.isRemote) {
                            String FIXED_FEMALE = new TextComponentTranslation("chat.info.success_fixed_female").getFormattedText();
                            String FIXED_MALE = new TextComponentTranslation("chat.info.success_fixed_male").getFormattedText();
                            player.sendMessage(new TextComponentString(this.getName() + " " + (this.getSex().equals(Genetics.Sex.FEMALE.getName()) ? FIXED_FEMALE : FIXED_MALE)));
                        }
                    }
                    return true;
                }
            }

            if (isFoodItem(stack)) {
                ItemFood food = (ItemFood) stack.getItem();
                if (this.getHealth() < this.getMaxHealth())
                    this.heal((float) food.getHealAmount(stack));
                if (!player.capabilities.isCreativeMode)
                    stack.shrink(1);
                if (stack.getCount() <= 0)
                    player.inventory.setInventorySlotContents(player.inventory.currentItem, ItemStack.EMPTY);
                return true;
            }

            if (stack.getItem() == Items.STICK && player.isSneaking()) {
                if (this.world.isRemote) {
                    if (this.isFixed()) {
                        if (this.getSex().equals(Genetics.Sex.FEMALE.getName()))
                            player.sendMessage(new TextComponentTranslation("chat.info.fixed_female"));
                        else
                            player.sendMessage(new TextComponentTranslation("chat.info.fixed_male"));
                    } else if (this.getSex().equals(Genetics.Sex.FEMALE.getName()) && this.getBreedingStatus("ispregnant")) {
                        if (!this.getBreedingStatus("inheat"))
                            player.sendMessage(new TextComponentString(new TextComponentTranslation("chat.info.pregnant").getFormattedText() + " " + this.getMateTimer()/* + parents + this.getParent("mother") + "/" + this.getParent("father")*/));
                        else
                            player.sendMessage(new TextComponentString(new TextComponentTranslation("chat.info.pregnant_heat").getFormattedText() + " " + this.getMateTimer()));
                    } else if (this.getSex().equals(Genetics.Sex.FEMALE.getName()) && this.getBreedingStatus("inheat"))
                        player.sendMessage(new TextComponentString(new TextComponentTranslation("chat.info.in_heat").getFormattedText() + " " + this.getMateTimer()/* + parents + this.getParent("mother") + "/" + this.getParent("father")*/));
                    else if (this.getSex().equals(Genetics.Sex.FEMALE.getName()) && !this.getBreedingStatus("inheat"))
                        player.sendMessage(new TextComponentString(new TextComponentTranslation("chat.info.not_in_heat").getFormattedText() + " " + this.getMateTimer()/* + parents + this.getParent("mother") + "/" + this.getParent("father")*/));
                    else if (this.getSex().equals(Genetics.Sex.MALE.getName()))
                        player.sendMessage(new TextComponentString(new TextComponentTranslation("chat.info.male").getFormattedText() + " " + this.getMateTimer()/* + parents + this.getParent("mother") + "/" + this.getParent("father")*/));
                }
                return true;

            } else if (stack.getItem() == Items.BONE && player.isSneaking()) {
                if (this.world.isRemote) {
                    if (this.getSex().equals(Genetics.Sex.FEMALE.getName()) && this.getBreedingStatus("ispregnant"))
                        player.sendMessage(new TextComponentString(new TextComponentTranslation("chat.info.kitten_count").getFormattedText() + " " + this.getKittens()));
                }
                return true;

            } else if ((this.aiTempt == null || this.aiTempt.isRunning()) && stack.getItem() == ModItems.TREAT_BAG && player.getDistanceSq(this) < 9.0D) {
                if (player.isSneaking()) {
                    if (this.hasHomePos()) {
                        this.resetHomePos();
                        if (this.world.isRemote)
                            player.sendMessage(new TextComponentString(new TextComponentTranslation("chat.info.remove_home").getFormattedText() + " " + this.getName()));
                        return true;
                    } else {
                        this.setHomePos(new BlockPos(this));
                        if (this.world.isRemote)
                            player.sendMessage(new TextComponentString(this.getName() +
                                    new TextComponentTranslation("chat.info.set_home").getFormattedText() +
                                    " " + getHomePos().getX() + ", " + getHomePos().getY() + ", " + getHomePos().getZ()));
                        return true;
                    }
                } else {
                    if (this.hasHomePos())
                        if (this.world.isRemote)
                            player.sendMessage(new TextComponentString(getHomePos().getX() + ", " + getHomePos().getY() + ", " + getHomePos().getZ()));
                }

            }
        }

        if (!this.world.isRemote && this.isOwner(player) && !player.isSneaking()) {
            if (stack.isEmpty() || (!this.isBreedingItem(stack) && !this.isFoodItem(stack))) {
                this.aiSit.setSitting(!this.isSitting());
                this.navigator.clearPath();
                this.setAttackTarget(null);
            }
        }

        return super.processInteract(player, hand);
    }

    static {
        FIXED = EntityDataManager.createKey(EntityCat.class, DataSerializers.BYTE);
        IN_HEAT = EntityDataManager.createKey(EntityCat.class, DataSerializers.BYTE);
        IS_PREGNANT = EntityDataManager.createKey(EntityCat.class, DataSerializers.BYTE);
        MATE_TIMER = EntityDataManager.createKey(EntityCat.class, DataSerializers.VARINT);
        KITTENS = EntityDataManager.createKey(EntityCat.class, DataSerializers.VARINT);
        MOTHER = EntityDataManager.createKey(EntityCat.class, DataSerializers.STRING);
        FATHER = EntityDataManager.createKey(EntityCat.class, DataSerializers.STRING);
    }
}
