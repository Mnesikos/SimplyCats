package com.github.mnesikos.simplycats.entity;

import com.github.mnesikos.simplycats.Ref;
import com.github.mnesikos.simplycats.configuration.SCConfig;
import com.github.mnesikos.simplycats.entity.ai.*;
import com.github.mnesikos.simplycats.entity.core.Genetics;
import com.github.mnesikos.simplycats.event.SCEvents;
import com.github.mnesikos.simplycats.item.CatItems;
import net.minecraft.entity.*;
import net.minecraft.entity.ai.*;
import net.minecraft.entity.monster.IMob;
import net.minecraft.entity.passive.EntityAnimal;
import net.minecraft.entity.passive.EntityTameable;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.ItemFood;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.pathfinding.PathNavigate;
import net.minecraft.pathfinding.PathNavigateClimber;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EnumHand;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.EnumDifficulty;
import net.minecraft.world.World;

import javax.annotation.Nullable;

public class EntityCat extends AbstractCat {
    private static final DataParameter<Byte> FIXED = EntityDataManager.createKey(EntityCat.class, DataSerializers.BYTE);
    private static final DataParameter<Byte> IN_HEAT = EntityDataManager.createKey(EntityCat.class, DataSerializers.BYTE);
    private static final DataParameter<Byte> IS_PREGNANT = EntityDataManager.createKey(EntityCat.class, DataSerializers.BYTE);
    private static final DataParameter<Integer> MATE_TIMER = EntityDataManager.createKey(EntityCat.class, DataSerializers.VARINT);
    private static final DataParameter<Integer> KITTENS = EntityDataManager.createKey(EntityCat.class, DataSerializers.VARINT);
    private static final DataParameter<String> MOTHER = EntityDataManager.createKey(EntityCat.class, DataSerializers.STRING);
    private static final DataParameter<String> FATHER = EntityDataManager.createKey(EntityCat.class, DataSerializers.STRING);

    private EntityAITempt aiTempt;
    private CatAITargetNearest aiTargetNearest;
    private Vec3d nearestLaser;

    public EntityCat(World world) {
        super(world);
        this.setSize(0.6F, 0.8F);
        this.setParent("mother", "Unknown");
        this.setParent("father", "Unknown");
    }

    @Override
    protected PathNavigate createNavigator(World worldIn) {
        return new PathNavigateClimber(this, worldIn);
    }

    public Vec3d getNearestLaser() {
        return nearestLaser;
    } //todo

    public void setNearestLaser(Vec3d vec) {
        this.nearestLaser = vec;
        if (vec == null) {
            this.getNavigator().clearPath();
        }
    }

    @Override
    protected void initEntityAI() {
        this.aiSit = new EntityAISit(this);
        this.aiTempt = new EntityAITempt(this, 1.2D, CatItems.TREAT_BAG, false);
        this.tasks.addTask(1, new EntityAISwimming(this));
        this.tasks.addTask(2, this.aiSit);
        this.tasks.addTask(3, this.aiTempt);
        this.tasks.addTask(3, new EntityAIFollowParent(this, 1.0D));
        if (!this.isFixed())
            this.tasks.addTask(3, new CatAIMate(this, 1.2D));
        this.tasks.addTask(4, new CatAIBirth(this));
        this.tasks.addTask(5, new EntityAILeapAtTarget(this, 0.4F));
        this.tasks.addTask(6, new CatAIAttack(this));
        this.tasks.addTask(7, new CatAIWander(this, 1.0D));
        this.tasks.addTask(9, new EntityAIWatchClosest2(this, EntityPlayer.class, 4.0F, 1.0F));
        this.tasks.addTask(10, new EntityAIWatchClosest(this, EntityLiving.class, 7.0F));
        this.tasks.addTask(11, new EntityAILookIdle(this));
        if (SCConfig.ATTACK_AI) {
            this.aiTargetNearest = new CatAITargetNearest<>(this, EntityLivingBase.class, true, entity -> {
                if (entity instanceof EntityTameable && ((EntityTameable) entity).isTamed())
                    return false;

                return entity != null && !(entity instanceof EntityCat) && !(entity instanceof EntityPlayer) && !(entity instanceof IMob) && !entity.isOnSameTeam(EntityCat.this) && SCEvents.isEntityPrey(entity);
            });
            this.targetTasks.addTask(1, this.aiTargetNearest);
        }
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

        this.dataManager.register(FIXED, (byte) 0);
        this.dataManager.register(IN_HEAT, (byte) 0);
        this.dataManager.register(IS_PREGNANT, (byte) 0);
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
            } else if (d0 == 1.33D) {
                this.setSneaking(false);
                this.setSprinting(true);
            } else {
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
        if (this.getSex() == Genetics.Sex.FEMALE && !this.isFixed())
            this.setTimeCycle("end", this.world.rand.nextInt(SCConfig.HEAT_COOLDOWN));
        return super.onInitialSpawn(difficulty, livingdata);
    }

    @Override
    public void onUpdate() {
        super.onUpdate();
        if (this.getNearestLaser() != null) {
            if (this.aiSit != null && this.isSitting())
                this.aiSit.setSitting(false);
            this.getNavigator().tryMoveToXYZ(this.getNearestLaser().x, this.getNearestLaser().y, this.getNearestLaser().z, 1.2D);
            this.getLookHelper().setLookPosition(this.getNearestLaser().x, this.getNearestLaser().y, this.getNearestLaser().z, 10.0F, (float) this.getVerticalFaceSpeed());
        }

        if (!this.world.isRemote && !this.isChild() && !this.isFixed() && this.getSex() == Genetics.Sex.FEMALE) { //if female & adult & not fixed
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
            if (this.getSex() == Genetics.Sex.FEMALE) {
                if (this.getBreedingStatus("inheat") || this.getBreedingStatus("ispregnant")) {
                    --mateTimer;
                    if (this.getBreedingStatus("inheat")) {
                        if (mateTimer % 10 == 0) {

                            double d0 = this.rand.nextGaussian() * 0.02D;
                            double d1 = this.rand.nextGaussian() * 0.02D;
                            double d2 = this.rand.nextGaussian() * 0.02D;
                            this.world.spawnParticle(EnumParticleTypes.HEART, this.posX + (double) (this.rand.nextFloat() * this.width * 2.0F) - (double) this.width, this.posY + 0.5D + (double) (this.rand.nextFloat() * this.height), this.posZ + (double) (this.rand.nextFloat() * this.width * 2.0F) - (double) this.width, d0, d1, d2);
                        }
                    }
                } else if (!this.getBreedingStatus("inheat") && !this.getBreedingStatus("ispregnant"))
                    ++mateTimer;
            } else if (this.getSex() == Genetics.Sex.MALE) {
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
        if (this.isEntityInvulnerable(source) || (this.isTamed() && this.getOwner() == null)) {
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

    @Override
    public boolean isOnSameTeam(Entity entity) {
        if (entity instanceof EntityTameable) {
            EntityTameable tameable = (EntityTameable) entity;
            if (tameable.isTamed() && tameable.getOwnerId() != null && this.isTamed() && this.getOwnerId() != null && this.getOwnerId().equals(tameable.getOwnerId()))
                return true;
        }

        return super.isOnSameTeam(entity);
    }

    public void onBagShake(EntityPlayer player) {
        if (!this.isTamed() || (this.isOwner(player) && !this.isSitting())) {
            this.getLookHelper().setLookPositionWithEntity(player, 10, (float) this.getVerticalFaceSpeed());
            this.getNavigator().tryMoveToEntityLiving(player, 1.8);
        }
    }

    public void setParent(String parent, String name) {
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
                this.dataManager.set(IN_HEAT, (byte) 1);
            else
                this.dataManager.set(IN_HEAT, (byte) 0);
        } else if (string.equals("ispregnant")) {
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
            if (!this.getEntityData().hasKey("Father" + i) || (this.getEntityData().hasKey("Father" + i) && this.getEntityData().getCompoundTag("Father" + i) == null)) {
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
        if (this.getSex() == Genetics.Sex.FEMALE) {
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
        if (this.getSex() == Genetics.Sex.FEMALE && !this.isFixed()) {
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

        if ((this.getSex() == Genetics.Sex.MALE && this.getMateTimer() == 0)) // if (this) is male & not on a cooldown
            return (mate.getSex() == Genetics.Sex.FEMALE && mate.getBreedingStatus("inheat")); // returns true if (mate) is female & in heat
        else
            return false;
    }

    @Override
    public boolean isBreedingItem(ItemStack item) {
        return item.getItem() == Items.BLAZE_POWDER;
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
            if (isBreedingItem(stack) && player.isSneaking() && this.isTamed() && this.isOwner(player)) {
                if (this.isChild()) {
                    this.consumeItemFromStack(player, stack);
                    this.ageUp((int) ((float) (-this.getGrowingAge() / 20) * 0.8F), true);

                } else if (!this.isFixed() && this.getMateTimer() != 0) {
                    this.consumeItemFromStack(player, stack);
                    this.setMateTimer(this.getMateTimer() / 2);
                }
                return true;
            }

            if (isFoodItem(stack) && this.getHealth() < this.getMaxHealth()) {
                ItemFood food = (ItemFood) stack.getItem();
                this.consumeItemFromStack(player, stack);
                this.heal((float) food.getHealAmount(stack));
                return true;
            }

            if (stack.getItem() == Items.BONE && player.isSneaking()) {
                if (this.getSex() == Genetics.Sex.FEMALE && this.getBreedingStatus("ispregnant"))
                    player.sendStatusMessage(new TextComponentTranslation("chat.info.kitten_count", this.getKittens()), true);
                return true;
            }

            if ((this.aiTempt == null || this.aiTempt.isRunning()) && stack.getItem() == CatItems.TREAT_BAG && player.getDistanceSq(this) < 9.0D) {
                if (player.isSneaking()) {
                    if (this.hasHomePos()) {
                        this.resetHomePos();
                        player.sendStatusMessage(new TextComponentTranslation("chat.info.remove_home", this.getName()), true);
                    } else {
                        this.setHomePos(new BlockPos(this));
                        player.sendStatusMessage(new TextComponentTranslation("chat.info.set_home", this.getName(), getHomePos().getX(), getHomePos().getY(), getHomePos().getZ()), true);
                    }
                    return true;
                } else if (this.hasHomePos())
                    player.sendStatusMessage(new TextComponentString(getHomePos().getX() + ", " + getHomePos().getY() + ", " + getHomePos().getZ()), true);
            }
        }

        if (this.isOwner(player) && !this.world.isRemote && !player.isSneaking()) {
            this.aiSit.setSitting(!this.isSitting());
            this.navigator.clearPath();
            this.setAttackTarget(null);
        }

        return super.processInteract(player, hand);
    }
}
