package com.github.mnesikos.simplycats.entity;

import com.github.mnesikos.simplycats.Ref;
import com.github.mnesikos.simplycats.SimplyCats;
import com.github.mnesikos.simplycats.configuration.SimplyCatsConfig;
import com.github.mnesikos.simplycats.entity.ai.*;
import com.github.mnesikos.simplycats.entity.core.Genetics;
import com.github.mnesikos.simplycats.event.SimplyCatsEvents;
import com.github.mnesikos.simplycats.init.ModItems;
import net.minecraft.entity.*;
import net.minecraft.entity.ai.goal.*;
import net.minecraft.entity.monster.IMob;
import net.minecraft.entity.passive.AnimalEntity;
import net.minecraft.entity.passive.TameableEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.particles.ParticleTypes;
import net.minecraft.util.DamageSource;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.Difficulty;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.IWorld;
import net.minecraft.world.World;

import javax.annotation.Nullable;
import java.util.Objects;
import java.util.function.Predicate;

public class EntityCat extends AbstractCat {
    private static final DataParameter<Byte> FIXED = EntityDataManager.createKey(EntityCat.class, DataSerializers.BYTE);
    private static final DataParameter<Byte> IN_HEAT = EntityDataManager.createKey(EntityCat.class, DataSerializers.BYTE);
    private static final DataParameter<Byte> IS_PREGNANT = EntityDataManager.createKey(EntityCat.class, DataSerializers.BYTE);
    private static final DataParameter<Integer> MATE_TIMER = EntityDataManager.createKey(EntityCat.class, DataSerializers.VARINT);
    private static final DataParameter<Integer> KITTENS = EntityDataManager.createKey(EntityCat.class, DataSerializers.VARINT);
    private static final DataParameter<String> MOTHER = EntityDataManager.createKey(EntityCat.class, DataSerializers.STRING);
    private static final DataParameter<String> FATHER = EntityDataManager.createKey(EntityCat.class, DataSerializers.STRING);

    public Predicate<LivingEntity> targetEntities = entity -> {
        EntityType<?> entityType = entity.getType();
        if (entity instanceof TameableEntity && ((TameableEntity) entity).isTamed())
            return false;

        return entityType != SimplyCats.CAT.get() && entityType != EntityType.PLAYER && !(entity instanceof IMob) && !entity.isOnSameTeam(EntityCat.this) && SimplyCatsConfig.getPreys().contains(entityType);
    };
    private CatTemptGoal aiTempt;
    private CatAITargetNearest<LivingEntity> aiTargetNearest;
    private Vec3d nearestLaser;

    public EntityCat(EntityType<? extends EntityCat> entityType, World world) {
        super(entityType, world);
        this.setParent("mother", "Unknown");
        this.setParent("father", "Unknown");
    }

    @Override
    protected void registerGoals() {
        this.sitGoal = new SitGoal(this);
        this.aiTempt = new CatTemptGoal(this, 1.2D, ModItems.TREAT_BAG.get(), false);
        this.goalSelector.addGoal(1, new SwimGoal(this));
        this.goalSelector.addGoal(2, this.sitGoal);
        this.goalSelector.addGoal(3, this.aiTempt);
        this.goalSelector.addGoal(3, new FollowParentGoal(this, 1.0D));
        if (!this.isFixed())
            this.goalSelector.addGoal(3, new CatAIMate(this, 1.2D));
        this.goalSelector.addGoal(4, new CatAIBirth(this));
        this.goalSelector.addGoal(5, new LeapAtTargetGoal(this, 0.4F));
        this.goalSelector.addGoal(6, new CatAIAttack(this));
        this.goalSelector.addGoal(7, new CatAIWander(this, 1.0D));
        this.goalSelector.addGoal(9, new LookAtGoal(this, LivingEntity.class, 7.0F));
        this.goalSelector.addGoal(10, new LookRandomlyGoal(this));
        if (SimplyCatsConfig.ATTACK_AI.get()) {
            this.aiTargetNearest = new CatAITargetNearest<>(this, LivingEntity.class, true, targetEntities);
            this.targetSelector.addGoal(1, this.aiTargetNearest);
        }
    }

    @Override
    protected void registerAttributes() {
        super.registerAttributes();
        getAttribute(SharedMonsterAttributes.MAX_HEALTH).setBaseValue(14.0D);
        getAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).setBaseValue(0.3D);
        getAttribute(SharedMonsterAttributes.KNOCKBACK_RESISTANCE).setBaseValue(0.7D);
        getAttributes().registerAttribute(SharedMonsterAttributes.ATTACK_DAMAGE);
        getAttribute(SharedMonsterAttributes.ATTACK_DAMAGE).setBaseValue(2.0D);
    }

    @Override
    protected void registerData() {
        super.registerData();

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
        if (this.world.getDifficulty() == Difficulty.PEACEFUL || !SimplyCatsConfig.ATTACK_AI.get())
            this.targetSelector.removeGoal(aiTargetNearest);

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
    public ILivingEntityData onInitialSpawn(IWorld worldIn, DifficultyInstance difficultyIn, SpawnReason reason, @Nullable ILivingEntityData spawnDataIn, @Nullable CompoundNBT dataTag) {
        if (!this.world.isRemote)
            if (this.isTamed())
                this.sitGoal.setSitting(!this.isSitting());
        if (this.getSex() == Genetics.Sex.FEMALE && !this.isFixed())
            this.setTimeCycle("end", this.world.rand.nextInt(SimplyCatsConfig.HEAT_COOLDOWN.get()));
        return super.onInitialSpawn(worldIn, difficultyIn, reason, spawnDataIn, dataTag);
    }

    @Override
    public void tick() {
        super.tick();
        if (this.getNearestLaser() != null) {
            if (this.sitGoal != null && this.isSitting())
                this.sitGoal.setSitting(false);
            this.getNavigator().tryMoveToXYZ(this.getNearestLaser().x, this.getNearestLaser().y, this.getNearestLaser().z, 1.2D);
            this.getLookController().setLookPosition(this.getNearestLaser().x, this.getNearestLaser().y, this.getNearestLaser().z, 10.0F, (float) this.getVerticalFaceSpeed());
        }

        if (!this.world.isRemote && !this.isChild() && !this.isFixed() && this.getSex() == Genetics.Sex.FEMALE) { //if female & adult & not fixed
            if (this.getBreedingStatus("inheat")) //if in heat
                if (this.getMateTimer() <= 0) { //and timer is finished (reaching 0 after being in positives)
                    if (!this.getBreedingStatus("ispregnant")) //and not pregnant
                        setTimeCycle("end", SimplyCatsConfig.HEAT_COOLDOWN.get()); //sets out of heat for 16 (default) minecraft days
                    else { //or if IS pregnant
                        setTimeCycle("pregnant", SimplyCatsConfig.PREGNANCY_TIMER.get()); //and heat time runs out, starts pregnancy timer for birth
                        this.setBreedingStatus("inheat", false); //sets out of heat
                    }
                }
            if (!this.getBreedingStatus("inheat")) { //if not in heat
                if (this.getMateTimer() >= 0) { //and timer is finished (reaching 0 after being in negatives)
                    if (!this.getBreedingStatus("ispregnant")) //and not pregnant
                        setTimeCycle("start", SimplyCatsConfig.HEAT_TIMER.get()); //sets in heat for 2 minecraft days
                }
            }
        }
    }

    @Override
    public void livingTick() {
        super.livingTick();

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
                            this.world.addParticle(ParticleTypes.HEART, this.getPosX() + (double) (this.rand.nextFloat() * this.getWidth() * 2.0F) - (double) this.getWidth(), this.getPosY() + 0.5D + (double) (this.rand.nextFloat() * this.getHeight()), this.getPosZ() + (double) (this.rand.nextFloat() * this.getWidth() * 2.0F) - (double) this.getWidth(), d0, d1, d2);
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
        if (this.isInvulnerableTo(source) || (this.isTamed() && this.getOwner() == null)) {
            return false;

        } else {
            if (this.sitGoal != null) {
                this.sitGoal.setSitting(false);
            }

            return super.attackEntityFrom(source, amount);
        }
    }

    @Override
    public boolean attackEntityAsMob(Entity entity) {
        float damage = (int) this.getAttribute(SharedMonsterAttributes.ATTACK_DAMAGE).getValue();
        if (SimplyCatsEvents.isRatEntity(entity))
            damage *= 3.0F;
        if (this.isSneaking() || this.isSprinting())
            damage *= 2.0F;
        return entity.attackEntityFrom(DamageSource.causeMobDamage(this), damage);
    }

    @Override
    public void setAttackTarget(@Nullable LivingEntity entitylivingbaseIn) {
        super.setAttackTarget(entitylivingbaseIn);

        if (entitylivingbaseIn == null)
            this.setAngry(false);
        else if (!this.isTamed())
            this.setAngry(true);
    }

    @Override
    public boolean isOnSameTeam(Entity entity) {
        if (entity instanceof TameableEntity) {
            TameableEntity tameable = (TameableEntity) entity;
            if (tameable.isTamed() && tameable.getOwnerId() != null && this.isTamed() && this.getOwnerId() != null && this.getOwnerId().equals(tameable.getOwnerId()))
                return true;
        }

        return super.isOnSameTeam(entity);
    }

    public void onBagShake(PlayerEntity player) {
        if (!this.isTamed() || (this.isOwner(player) && !this.isSitting())) {
            this.getLookController().setLookPositionWithEntity(player, 10, (float) this.getVerticalFaceSpeed());
            this.getNavigator().tryMoveToEntityLiving(player, 1.8);
        }
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
            if (!this.getPersistentData().contains("Father" + i) || (this.getPersistentData().contains("Father" + i) && this.getPersistentData().getCompound("Father" + i) == null)) {
                CompoundNBT tags = new CompoundNBT();
                father.writeWithoutTypeId(tags);
                this.getPersistentData().put("Father" + i, tags);
            }
        }
    }

    private void setFather(int i, CompoundNBT father) {
        if (this.getPersistentData().contains("Father" + i))
            this.getPersistentData().put("Father" + i, father);
    }

    public CompoundNBT getFather(int i) {
        return this.getPersistentData().getCompound("Father" + i);
    }

    @Override
    public void writeAdditional(CompoundNBT nbt) {
        super.writeAdditional(nbt);
        nbt.putByte("Fixed", this.getIsFixed());
        if (this.getSex() == Genetics.Sex.FEMALE) {
            nbt.putBoolean("InHeat", this.getBreedingStatus("inheat"));
            nbt.putBoolean("IsPregnant", this.getBreedingStatus("ispregnant"));
            nbt.putInt("Kittens", this.getKittens());
            for (int i = 0; i < 5; i++)
                nbt.put("Father" + i, this.getFather(i));
        }
        nbt.putInt("Timer", this.getMateTimer());
        nbt.putString("Mother", this.getParent("mother"));
        nbt.putString("Father", this.getParent("father"));
    }

    @Override
    public void readAdditional(CompoundNBT nbt) {
        super.readAdditional(nbt);
        this.setFixed(nbt.getByte("Fixed"));
        if (this.getSex() == Genetics.Sex.FEMALE && !this.isFixed()) {
            this.setBreedingStatus("inheat", nbt.getBoolean("InHeat"));
            this.setBreedingStatus("ispregnant", nbt.getBoolean("IsPregnant"));
            this.setKittens(nbt.getInt("Kittens"));
            for (int i = 0; i < 5; i++) {
                this.setFather(i, nbt.getCompound("Father" + i));
            }
        }
        if (!this.isFixed())
            this.setMateTimer(nbt.getInt("Timer"));
        this.setParent("mother", nbt.getString("Mother"));
        this.setParent("father", nbt.getString("Father"));
    }

    @Override
    public boolean canDespawn(double distanceToClosestPlayer) {
        return false;
    }

    @Override
    public boolean canMateWith(AnimalEntity target) {
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
    public EntityCat createChild(AgeableEntity parFather) {
        return (EntityCat) super.createChild(parFather);
    }

    private boolean isFoodItem(ItemStack item) {
        return Ref.isCatFood(item);
    }

    @Override
    public boolean processInteract(PlayerEntity player, Hand hand) {
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

            /*if (!this.isTamed() || (this.isTamed() && this.isOwner(player))) {
                if (stack.getItem() == Items.SHEARS && player.isSneaking()) {
                    if (!this.isFixed()) {
                        this.setFixed((byte) 1);
                        if (this.world.isRemote) {
                            String FIXED_FEMALE = new TranslationTextComponent("chat.info.success_fixed_female").getFormattedText();
                            String FIXED_MALE = new TranslationTextComponent("chat.info.success_fixed_male").getFormattedText();
                            player.sendMessage(new StringTextComponent(this.getName().getFormattedText() + " " + (this.getSex() == Genetics.Sex.FEMALE ? FIXED_FEMALE : FIXED_MALE)));
                        }
                    }
                    return true;
                }
            }*/

            if (isFoodItem(stack) && this.getHealth() < this.getMaxHealth()) {
                Item food = stack.getItem();
                this.consumeItemFromStack(player, stack);
                this.heal((float) Objects.requireNonNull(food.getFood()).getHealing());
                return true;
            }

            /*if (stack.getItem() == Items.STICK && player.isSneaking()) {
                if (this.world.isRemote) {
                    if (this.isFixed()) {
                        if (this.getSex() == Genetics.Sex.FEMALE)
                            player.sendMessage(new TranslationTextComponent("chat.info.fixed_female"));
                        else
                            player.sendMessage(new TranslationTextComponent("chat.info.fixed_male"));
                    } else if (this.getSex() == Genetics.Sex.FEMALE && this.getBreedingStatus("ispregnant")) {
                        if (!this.getBreedingStatus("inheat"))
                            player.sendMessage(new StringTextComponent(new TranslationTextComponent("chat.info.pregnant").getFormattedText() + " " + this.getMateTimer()*//* + parents + this.getParent("mother") + "/" + this.getParent("father")*//*));
                        else
                            player.sendMessage(new StringTextComponent(new TranslationTextComponent("chat.info.pregnant_heat").getFormattedText() + " " + this.getMateTimer()));
                    } else if (this.getSex() == Genetics.Sex.FEMALE && this.getBreedingStatus("inheat"))
                        player.sendMessage(new StringTextComponent(new TranslationTextComponent("chat.info.in_heat").getFormattedText() + " " + this.getMateTimer()*//* + parents + this.getParent("mother") + "/" + this.getParent("father")*//*));
                    else if (this.getSex() == Genetics.Sex.FEMALE && !this.getBreedingStatus("inheat"))
                        player.sendMessage(new StringTextComponent(new TranslationTextComponent("chat.info.not_in_heat").getFormattedText() + " " + this.getMateTimer()*//* + parents + this.getParent("mother") + "/" + this.getParent("father")*//*));
                    else if (this.getSex() == Genetics.Sex.MALE)
                        player.sendMessage(new StringTextComponent(new TranslationTextComponent("chat.info.male").getFormattedText() + " " + this.getMateTimer()*//* + parents + this.getParent("mother") + "/" + this.getParent("father")*//*));
                }
                return true;

            } else */
            if (stack.getItem() == Items.BONE && player.isSneaking()) {
                if (this.world.isRemote)
                    if (this.getSex() == Genetics.Sex.FEMALE && this.getBreedingStatus("ispregnant"))
                        player.sendMessage(new TranslationTextComponent("chat.info.kitten_count", this.getKittens()));
                return true;
            }

            if ((this.aiTempt == null || this.aiTempt.isRunning()) && stack.getItem() == ModItems.TREAT_BAG.get() && player.getDistanceSq(this) < 9.0D) {
                if (player.isSneaking()) {
                    if (this.hasHomePos()) {
                        this.resetHomePos();
                        if (this.world.isRemote)
                            player.sendMessage(new TranslationTextComponent("chat.info.remove_home", this.getName()));
                    } else {
                        this.setHomePos(new BlockPos(this.getPosition()));
                        if (this.world.isRemote)
                            player.sendMessage(new TranslationTextComponent("chat.info.set_home", this.getName(), getHomePos().getX(), getHomePos().getY(), getHomePos().getZ()));
                    }
                    return true;
                } else {
                    if (this.hasHomePos())
                        if (this.world.isRemote)
                            player.sendMessage(new StringTextComponent(getHomePos().getX() + ", " + getHomePos().getY() + ", " + getHomePos().getZ()));
                }

            }
        }

        if (this.isOwner(player) && !this.world.isRemote && !player.isSneaking()) {
            this.sitGoal.setSitting(!this.isSitting());
            this.navigator.clearPath();
            this.setAttackTarget(null);
        }

        return super.processInteract(player, hand);
    }
}
