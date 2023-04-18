package com.github.mnesikos.simplycats.entity;

import com.github.mnesikos.simplycats.SCReference;
import com.github.mnesikos.simplycats.SimplyCats;
import com.github.mnesikos.simplycats.configuration.SCConfig;
import com.github.mnesikos.simplycats.entity.genetics.CatAlleles;
import com.github.mnesikos.simplycats.entity.genetics.FelineGenome;
import com.github.mnesikos.simplycats.entity.genetics.Genetics;
import com.github.mnesikos.simplycats.entity.genetics.Genetics.Sex;
import com.github.mnesikos.simplycats.event.SCEvents;
import com.github.mnesikos.simplycats.item.SCItems;
import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.entity.*;
import net.minecraft.entity.ai.attributes.AttributeModifierMap;
import net.minecraft.entity.ai.attributes.Attributes;
import net.minecraft.entity.passive.AnimalEntity;
import net.minecraft.entity.passive.TameableEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.INBT;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.particles.ParticleTypes;
import net.minecraft.scoreboard.Team;
import net.minecraft.util.*;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.IServerWorld;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;

import javax.annotation.Nullable;
import java.util.Optional;
import java.util.UUID;

public class SimplyCatEntity extends TameableEntity {
    protected FelineGenome genes = new FelineGenome(this);
    protected static final DataParameter<String> GENES = EntityDataManager.defineId(SimplyCatEntity.class, DataSerializers.STRING);
    protected static final DataParameter<String> MARKINGS = EntityDataManager.defineId(SimplyCatEntity.class, DataSerializers.STRING);
    protected static final DataParameter<Optional<BlockPos>> HOME_POSITION = EntityDataManager.defineId(SimplyCatEntity.class, DataSerializers.OPTIONAL_BLOCK_POS);
    protected static final DataParameter<String> OWNER_NAME = EntityDataManager.defineId(SimplyCatEntity.class, DataSerializers.STRING);
    protected static final DataParameter<Byte> FIXED = EntityDataManager.defineId(SimplyCatEntity.class, DataSerializers.BYTE);
    protected static final DataParameter<Byte> IN_HEAT = EntityDataManager.defineId(SimplyCatEntity.class, DataSerializers.BYTE);
    protected static final DataParameter<Byte> IS_PREGNANT = EntityDataManager.defineId(SimplyCatEntity.class, DataSerializers.BYTE);
    protected static final DataParameter<Integer> MATE_TIMER = EntityDataManager.defineId(SimplyCatEntity.class, DataSerializers.INT);
    protected static final DataParameter<Integer> KITTENS = EntityDataManager.defineId(SimplyCatEntity.class, DataSerializers.INT);
    protected static final DataParameter<Optional<UUID>> MOTHER = EntityDataManager.defineId(SimplyCatEntity.class, DataSerializers.OPTIONAL_UUID);
    protected static final DataParameter<Optional<UUID>> FATHER = EntityDataManager.defineId(SimplyCatEntity.class, DataSerializers.OPTIONAL_UUID);
    protected static final DataParameter<Integer> AGE_TRACKER = EntityDataManager.defineId(SimplyCatEntity.class, DataSerializers.INT);
    protected static final DataParameter<Float> MATURE_TIMER = EntityDataManager.defineId(SimplyCatEntity.class, DataSerializers.FLOAT);

    private SimplyCatEntity followParent;
    private Vector3d nearestLaser;

    public SimplyCatEntity(EntityType<? extends TameableEntity> type, World world) {
        super(type, world);
    }

    @Override
    protected void registerGoals() {
//        TemptGoal temptGoal = new TemptGoal(this, 1.2D, Ingredient.of(SCItems.CATNIP.get(), SCItems.TREAT_BAG.get()), false);
//        this.goalSelector.addGoal(1, new SwimGoal(this));
//        this.goalSelector.addGoal(1, new CatSitGoal(this));
//        this.goalSelector.addGoal(3, temptGoal);
//        this.goalSelector.addGoal(4, new CatFollowParentGoal(this, 1.0D));
//        this.goalSelector.addGoal(5, new CatSitOnBlockGoal(this, 1.0D, 8));
//        this.goalSelector.addGoal(6, new CatBirthGoal(this));
//        this.goalSelector.addGoal(7, new LeapAtTargetGoal(this, 0.4F));
//        this.goalSelector.addGoal(8, new CatAttackGoal(this));
//        if (!this.isFixed())
//            this.goalSelector.addGoal(9, new CatMateGoal(this, 1.2D));
//        this.goalSelector.addGoal(10, new CatWanderGoal(this, 1.0D));
//        this.goalSelector.addGoal(11, new LookAtGoal(this, LivingEntity.class, 7.0F));
//        this.goalSelector.addGoal(12, new LookRandomlyGoal(this));
//        this.targetSelector.addGoal(1, new CatTargetNearestGoal<>(this, LivingEntity.class, true, (entity) -> {
//            EntityType<?> entityType = entity.getType();
//            if (entity instanceof TameableEntity && ((TameableEntity) entity).isTame())
//                return false;
//            return !(entity instanceof SimplyCatEntity) && !(entity instanceof PlayerEntity) && !(entity instanceof IMob) && !entity.isAlliedTo(this) && SCConfig.prey_list.get().contains(entityType.getRegistryName().toString());
//        }));
    }

    public static AttributeModifierMap.MutableAttribute createAttributes() {
        return MobEntity.createMobAttributes().add(Attributes.MAX_HEALTH, 14.0D).add(Attributes.MOVEMENT_SPEED, 0.3F).add(Attributes.KNOCKBACK_RESISTANCE, 0.7D).add(Attributes.ATTACK_DAMAGE, 2.0D);
    }

    @Override
    protected void defineSynchedData() {
        super.defineSynchedData();
        this.entityData.define(GENES, "");
        this.entityData.define(MARKINGS, "");
        this.entityData.define(HOME_POSITION, Optional.empty());
        this.entityData.define(OWNER_NAME, "");
        this.entityData.define(FIXED, (byte) 0);
        this.entityData.define(IN_HEAT, (byte) 0);
        this.entityData.define(IS_PREGNANT, (byte) 0);
        this.entityData.define(MATE_TIMER, 0);
        this.entityData.define(KITTENS, 0);
        this.entityData.define(MOTHER, Optional.empty());
        this.entityData.define(FATHER, Optional.empty());
        this.entityData.define(AGE_TRACKER, 0);
        this.entityData.define(MATURE_TIMER, 168000f);
    }

    @Override
    protected void customServerAiStep() {
        if (this.getMoveControl().hasWanted()) {
            double d0 = this.getMoveControl().getSpeedModifier();

            if (d0 == 0.6D) {
                this.setPose(Pose.CROUCHING);
                this.setSprinting(false);
            } else if (d0 == 1.33D) {
                this.setPose(Pose.STANDING);
                this.setSprinting(true);
            } else {
                this.setPose(Pose.STANDING);
                this.setSprinting(false);
            }
        } else {
            this.setPose(Pose.STANDING);
            this.setSprinting(false);
        }
    }

    @Override
    public ILivingEntityData finalizeSpawn(IServerWorld world, DifficultyInstance difficulty, SpawnReason spawnReason, @Nullable ILivingEntityData entityData, @Nullable CompoundNBT compound) {
        entityData = super.finalizeSpawn(world, difficulty, spawnReason, entityData, compound);
        this.getGenome().randomize();

        if (!this.level.isClientSide)
            if (this.isTame())
                this.setOrderedToSit(!this.isOrderedToSit());
        if (this.getSex() == Genetics.Sex.FEMALE && !this.isFixed())
            this.setTimeCycle("end", random.nextInt(SCConfig.heat_cooldown.get()));

        return entityData;
    }

    @Override
    public float getScale() { //setScaleForAge ?
        return this.isBaby() ? 0.7F : 1.0F;
    }

    @Override
    public void tick() {
        super.tick();
        if (this.getNearestLaser() != null) {
            if (this.isOrderedToSit()) this.setOrderedToSit(false);
            this.getNavigation().moveTo(this.getNearestLaser().x, this.getNearestLaser().y, this.getNearestLaser().z, 1.2D);
            this.getLookControl().setLookAt(this.getNearestLaser().x, this.getNearestLaser().y, this.getNearestLaser().z, 10.0F, (float) this.getHeadRotSpeed());
        }

        if (!this.level.isClientSide && !this.isBaby() && !this.isFixed() && this.getSex() == Genetics.Sex.FEMALE) { //if female & adult & not fixed
            if (this.getBreedingStatus("inheat")) //if in heat
                if (this.getMateTimer() <= 0) { //and timer is finished (reaching 0 after being in positives)
                    if (!this.getBreedingStatus("ispregnant")) //and not pregnant
                        setTimeCycle("end", SCConfig.heat_cooldown.get()); //sets out of heat for 16 (default) minecraft days
                    else { //or if IS pregnant
                        setTimeCycle("pregnant", SCConfig.pregnancy_timer.get()); //and heat time runs out, starts pregnancy timer for birth
                        this.setBreedingStatus("inheat", false); //sets out of heat
                    }
                }
            if (!this.getBreedingStatus("inheat")) { //if not in heat
                if (this.getMateTimer() >= 0) { //and timer is finished (reaching 0 after being in negatives)
                    if (!this.getBreedingStatus("ispregnant")) //and not pregnant
                        setTimeCycle("start", SCConfig.heat_timer.get()); //sets in heat for 2 minecraft days
                }
            }
        }

        if (this.tickCount % 40 == 0) {
            if (!this.level.isClientSide && this.getOwner() != null)
                this.setOwnerName(this.getOwner().getDisplayName().getString());
        }

        if (this.level.isClientSide && this.entityData.isDirty()) {
            this.entityData.clearDirty();
            this.getGenome().resetTexturePrefix();
        }
    }

    @Override
    public void baseTick() {
        super.baseTick();

        if (!this.isBaby() && !this.isFixed()) { //if not a child & not fixed
            int mateTimer = this.getMateTimer();
            if (this.getSex() == Genetics.Sex.FEMALE) {
                if (this.getBreedingStatus("inheat") || this.getBreedingStatus("ispregnant")) {
                    --mateTimer;
                    if (this.getBreedingStatus("inheat")) {
                        if (mateTimer % 10 == 0) {

                            double d0 = this.random.nextGaussian() * 0.02D;
                            double d1 = this.random.nextGaussian() * 0.02D;
                            double d2 = this.random.nextGaussian() * 0.02D;
                            this.level.addParticle(ParticleTypes.HEART, this.getRandomX(1.0D), this.getRandomY() + 0.5D, this.getRandomZ(1.0D), d0, d1, d2);
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

        if (!this.level.isClientSide && this.getTarget() == null && this.isAngry())
            this.setAngry(false);

        if (this.getHealth() <= 0 && this.isTame() && this.getOwner() == null) {
            this.deathTime = 0;
            this.setHealth(1);
        }
    }

    @Override
    public void die(DamageSource cause) { // todo
//        if (this.isTame() && this.getOwner() != null) {
//            int count = this.getOwner().getPersistentData().getInt("CatCount");
//            this.getOwner().getPersistentData().putInt("CatCount", count - 1);
//            if (!level.isClientSide)
//                CHANNEL.sendTo(new SCNetworking(count - 1), (EntityPlayerMP) this.getOwner());
//        }
        super.die(cause);
    }

    @Override
    public boolean hurt(DamageSource source, float amount) {
        if (this.isInvulnerableTo(source) || (this.isTame() && this.getOwner() == null)) {
            return false;

        } else {
            this.setOrderedToSit(false);

            return super.hurt(source, amount);
        }
    }

    @Override
    public boolean doHurtTarget(Entity entity) {
        float damage = (int) this.getAttributeValue(Attributes.ATTACK_DAMAGE);
        if (SCEvents.isRatEntity(entity))
            damage *= 3.0F;
        if (this.isCrouching() || this.isSprinting())
            damage *= 2.0F;
        return entity.hurt(DamageSource.mobAttack(this), damage);
    }

    @Override
    public void setTarget(@Nullable LivingEntity entity) {
        super.setTarget(entity);

        if (entity == null)
            this.setAngry(false);
        else if (!this.isTame())
            this.setAngry(true);
    }

    @Override
    public Team getTeam() {
        return super.getTeam();
    }

    @Override
    public boolean isAlliedTo(Entity entity) {
        if (entity instanceof TameableEntity) {
            TameableEntity tameable = (TameableEntity) entity;
            if (tameable.isTame() && tameable.getOwnerUUID() != null && this.isTame() && this.getOwnerUUID() != null && this.getOwnerUUID().equals(tameable.getOwnerUUID()))
                return true;
        }

        return super.isAlliedTo(entity);
    }

    public FelineGenome getGenome() {
        return genes;
    }

    public void setGeneData(String genes) {
        this.entityData.set(GENES, genes);
    }

    public String getGeneData() {
        return (String) this.entityData.get(GENES);
    }

    public void setMarkingsData(String markings) {
        this.entityData.set(MARKINGS, markings);
    }

    public String getMarkingsData() {
        return (String) this.entityData.get(MARKINGS);
    }

    public Sex getSex() {
        return getGenome().isMale() ? Sex.MALE : Sex.FEMALE;
    }

    public Optional<BlockPos> getHomePos() {
        return this.entityData.get(HOME_POSITION);
    }

    public void setHomePos(BlockPos position) {
        this.entityData.set(HOME_POSITION, Optional.of(position));
    }

    public void resetHomePos() {
        this.entityData.set(HOME_POSITION, Optional.empty());
    }

    public ITextComponent getOwnerName() {
        if (this.getOwner() != null)
            return this.getOwner().getDisplayName();
        else if (!this.entityData.get(OWNER_NAME).isEmpty())
            return new StringTextComponent(this.entityData.get(OWNER_NAME));
        else if (this.getOwnerUUID() != null)
            return new TranslationTextComponent("entity.simplycats.cat.unknown_owner");
        else
            return new TranslationTextComponent("entity.simplycats.cat.untamed");
    }

    public void setOwnerName(String name) {
        this.entityData.set(OWNER_NAME, name);
    }

    public void onBagShake(PlayerEntity player) {
        if (!this.isTame() || (this.getOwner() == player && !this.isOrderedToSit())) {
            this.getLookControl().setLookAt(player, 10, (float) this.getHeadRotSpeed());
            this.getNavigation().moveTo(player, 1.8);
        }
    }

    public Vector3d getNearestLaser() {
        return nearestLaser;
    }

    public void setNearestLaser(Vector3d vec) {
        this.nearestLaser = vec;
        if (vec == null) {
            this.getNavigation().stop();
        }
    }

    public final float getAgeScale() {
        return this.getAgeTracker() / this.getMatureTimer() + 1;
    }

    public float getMatureTimer() {
        return this.entityData.get(MATURE_TIMER);
    }

    public void setMatureTimer(float maxAge) {
        this.entityData.set(MATURE_TIMER, maxAge);
    }

    public int getAgeTracker() {
        return this.entityData.get(AGE_TRACKER);
    }

    @Override
    public void setAge(int age) {
        this.entityData.set(AGE_TRACKER, age);
        super.setAge(age);
    }

    public SimplyCatEntity getFollowParent() {
        return followParent;
    }

    public void setFollowParent(SimplyCatEntity followParent) {
        this.followParent = followParent;
    }

    public void setMother(@Nullable UUID uuid) {
        this.entityData.set(MOTHER, Optional.ofNullable(uuid));
    }

    public UUID getMother() {
        return (UUID) ((Optional) this.entityData.get(MOTHER)).orElse(null);
    }

    public void setFather(@Nullable UUID uuid) {
        this.entityData.set(FATHER, Optional.ofNullable(uuid));
    }

    public UUID getFather() {
        return (UUID) ((Optional) this.entityData.get(FATHER)).orElse(null);
    }

    public void setFixed(byte fixed) { // 1 = fixed, 0 = intact
        this.entityData.set(FIXED, fixed);
    }

    public boolean isFixed() {
        return this.entityData.get(FIXED) == 1;
    }

    public byte getIsFixed() {
        return this.entityData.get(FIXED);
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
                this.entityData.set(IN_HEAT, (byte) 1);
            else
                this.entityData.set(IN_HEAT, (byte) 0);
        } else if (string.equals("ispregnant")) {
            if (parTrue)
                this.entityData.set(IS_PREGNANT, (byte) 1);
            else
                this.entityData.set(IS_PREGNANT, (byte) 0);
        }
    }

    public boolean getBreedingStatus(String string) {
        if (string.equals("inheat"))
            return this.entityData.get(IN_HEAT) == 1;
        else if (string.equals("ispregnant"))
            return this.entityData.get(IS_PREGNANT) == 1;
        return false;
    }

    public void setMateTimer(int time) {
        this.entityData.set(MATE_TIMER, time);
    }

    public int getMateTimer() {
        return this.entityData.get(MATE_TIMER);
    }

    public void setKittens(int kittens) {
        if (getKittens() <= 0 || kittens == 0)
            this.entityData.set(KITTENS, kittens);
        else if (getKittens() > 0)
            this.entityData.set(KITTENS, this.getKittens() + kittens);
    }

    public int getKittens() {
        return this.entityData.get(KITTENS);
    }

    public void addFather(SimplyCatEntity father, int size) {
        for (int i = 0; i < size; i++) {
            if (!this.getPersistentData().contains("Father" + i) || (this.getPersistentData().contains("Father" + i) && this.getPersistentData().getCompound("Father" + i).isEmpty())) {
                this.getPersistentData().put("Father" + i, father.saveWithoutId(new CompoundNBT()));
            }
        }
    }

    private void setFather(int i, INBT father) {
        if (this.getPersistentData().contains("Father" + i))
            this.getPersistentData().put("Father" + i, father);
    }

    public CompoundNBT getFather(int i) {
        return this.getPersistentData().getCompound("Father" + i);
    }

    @Override
    public void addAdditionalSaveData(CompoundNBT compound) {
        super.addAdditionalSaveData(compound);
        compound.putString("Genes", this.getGeneData());
        compound.putString("Markings", this.getMarkingsData());
        compound.putString("OwnerName", this.entityData.get(OWNER_NAME));
        compound.putByte("Fixed", this.getIsFixed());
        if (this.getSex() == Genetics.Sex.FEMALE) {
            compound.putBoolean("InHeat", this.getBreedingStatus("inheat"));
            compound.putBoolean("IsPregnant", this.getBreedingStatus("ispregnant"));
            compound.putInt("Kittens", this.getKittens());
            for (int i = 0; i < 5; i++)
                compound.put("Father" + i, this.getFather(i));
        }
        compound.putInt("Timer", this.getMateTimer());
        if (this.getMother() == null) compound.putString("Mother", "");
        else compound.putString("Mother", this.getMother().toString());
        if (this.getFather() == null) compound.putString("Father", "");
        else compound.putString("Father", this.getFather().toString());
        if (this.isBaby()) {
            compound.putInt("AgeTracker", this.getAgeTracker());
            compound.putFloat("MatureTimer", this.getMatureTimer());
        }
    }

    @Override
    public void readAdditionalSaveData(CompoundNBT compound) {
        super.readAdditionalSaveData(compound);
        if (compound.contains("Genes")) this.setGeneData(compound.getString("Genes"));
        else this.getGenome().randomize();
        this.setMarkingsData(compound.getString("Markings"));
        this.setOwnerName(compound.getString("OwnerName"));
        this.setFixed(compound.getByte("Fixed"));
        if (this.getSex() == Genetics.Sex.FEMALE && !this.isFixed()) {
            this.setBreedingStatus("inheat", compound.getBoolean("InHeat"));
            this.setBreedingStatus("ispregnant", compound.getBoolean("IsPregnant"));
            this.setKittens(compound.getInt("Kittens"));
            for (int i = 0; i < 5; i++) {
                this.setFather(i, compound.get("Father" + i));
            }
        }
        if (!this.isFixed())
            this.setMateTimer(compound.getInt("Timer"));
        if (compound.contains("Mother", 8)) {
            String s;
            s = compound.getString("Mother");
            if (!s.isEmpty()) this.setMother(UUID.fromString(s));
        }
        if (compound.contains("Father", 8)) {
            String s;
            s = compound.getString("Father");
            if (!s.isEmpty()) this.setFather(UUID.fromString(s));
        }
        if (this.isBaby()) {
            this.setAge(compound.getInt("AgeTracker"));
            this.setMatureTimer(compound.getFloat("MatureTimer"));
        }
    }

//    private void resetTexturePrefix() {
//        this.texturePrefix = null;
//    }
//
//    @OnlyIn(Dist.CLIENT)
//    private void setCatTexturePaths() {
//    }
//
//    @OnlyIn(Dist.CLIENT)
//    public String getCatTexture() {
//        if (this.texturePrefix == null)
//            this.setCatTexturePaths();
//
//        return this.texturePrefix;
//    }
//
//    @OnlyIn(Dist.CLIENT)
//    public String[] getTexturePaths() {
//        if (this.texturePrefix == null)
//            this.setCatTexturePaths();
//
//        return this.catTexturesArray;
//    }

    public boolean canBeTamed(PlayerEntity player) {
        return (SCConfig.tamed_limit.get() == 0 || player.getPersistentData().getInt("CatCount") < SCConfig.tamed_limit.get()) && !this.isTame();
    }

    /**
     * A custom setTamed method to set the owner's data along with taming or untaming a cat.
     *
     * @param tamed - true is tamed, false is untamed, used for this.setTamed(tamed) call at the end.
     * @param owner - the EntityPlayer who is taming the cat.
     */
    public void setTamed(boolean tamed, PlayerEntity owner) {
        this.setTame(tamed);
        int catCount = owner.getPersistentData().getInt("CatCount");
        if (tamed) {
//            owner.getPersistentData().putInt("CatCount", catCount + 1);
//            if (!level.isClientSide())
//                CHANNEL.sendTo(new SCNetworking(catCount + 1), (EntityPlayerMP) owner);
            this.setOwnerName(owner.getDisplayName().getString());
            this.setOwnerUUID(owner.getUUID());
            if (owner instanceof ServerPlayerEntity)
                CriteriaTriggers.TAME_ANIMAL.trigger((ServerPlayerEntity) owner, this);

        } else {
//            owner.getPersistentData().putInt("CatCount", catCount - 1);
//            if (!level.isClientSide())
//                CHANNEL.sendTo(new SCNetworking(catCount - 1), (EntityPlayerMP) owner);
            this.setOwnerName("");
        }
    }

    @Override
    public boolean canMate(AnimalEntity target) {
        if (target == this)
            return false;
        if (!(target instanceof SimplyCatEntity))
            return false;
        if (target.isBaby() || this.isBaby())
            return false;
        if (this.isOrderedToSit() || ((SimplyCatEntity) target).isOrderedToSit())
            return false;

        SimplyCatEntity mate = (SimplyCatEntity) target;
        if (mate.isFixed() || this.isFixed())
            return false;

        if ((this.getSex() == Genetics.Sex.MALE && this.getMateTimer() == 0)) // if (this) is male & not on a cooldown
            return (mate.getSex() == Genetics.Sex.FEMALE && mate.getBreedingStatus("inheat")); // returns true if (mate) is female & in heat
        else
            return false;
    }

    @Override
    public boolean isFood(ItemStack item) {
        return SCReference.catFoodItems(item);
    }

    @Nullable
    @Override
    public AgeableEntity getBreedOffspring(ServerWorld world, AgeableEntity parFather) {
        SimplyCatEntity father = ((SimplyCatEntity) parFather);
        SimplyCatEntity child = SimplyCats.CAT.get().create(level);

        child.getGenome().inheritGenes(getGenome(), ((SimplyCatEntity) parFather).getGenome());

        int eyesMin;
        int eyesMax;
        int matEye = getGenome().getMarking(FelineGenome.Marking.eye_color);
        int patEye = father.getGenome().getMarking(FelineGenome.Marking.eye_color);
        if (matEye > patEye) {
            eyesMin = patEye - 1;
            eyesMax = matEye;
        } else {
            eyesMin = matEye - 1;
            eyesMax = patEye;
        }
        eyesMin = eyesMin < 0 ? 0 : (eyesMin >= 4 ? 3 : eyesMin);
        if (child.getGenome().hasAllele(FelineGenome.Gene.white, CatAlleles.DOMINANT_WHITE)) eyesMax = 4;
        else eyesMax = eyesMax >= 4 ? (eyesMin < 3 ? eyesMin + 1 : 3) : eyesMax;
        int eyeColor = (matEye == 4 && patEye == 4 ? (eyesMax == 4 ? 4 : random.nextInt(4)) : random.nextInt((eyesMax - eyesMin) + 1) + eyesMin);
        if (child.getGenome().isHomozygous(FelineGenome.Gene.colorpoint, CatAlleles.COLORPOINT)) eyeColor = 4;
        else if (child.getGenome().isAlbino())
            eyeColor = child.getGenome().hasAllele(FelineGenome.Gene.colorpoint, CatAlleles.BLUE_EYED_ALBINO) ? 5 : 6;

        child.getGenome().setMarking(FelineGenome.Marking.eye_color, eyeColor);

        if (isTame() && getOwnerUUID() != null) { // checks if mother is tamed & her owner's UUID exists
            PlayerEntity owner = level.getPlayerByUUID(getOwnerUUID()); // grabs owner by UUID
            if (owner != null && child.canBeTamed(owner)) { // checks if owner is not null (is online), and is able to tame the kitten OR if the tame limit is disabled
                child.setTamed(isTame(), owner); // sets tamed by owner
                if (getHomePos().isPresent()) // checks mother's home point
                    child.setHomePos(getHomePos().get()); // sets kitten's home point to mother's
            }
        }

        return child;
    }

    @Override
    public ActionResultType mobInteract(PlayerEntity player, Hand hand) {
        ItemStack stack = player.getItemInHand(hand);
        Item item = stack.getItem();
        if (this.level.isClientSide) {
            if (this.isTame() && this.isOwnedBy(player))
                return ActionResultType.SUCCESS;
            else if (item == Items.BONE || (item == SCItems.TREAT_BAG.get() && !this.isTame()))
                return ActionResultType.SUCCESS;
            else
                return !this.isFood(stack) || !(this.getHealth() < this.getMaxHealth()) && this.isTame() ? ActionResultType.PASS : ActionResultType.SUCCESS;

        } else if (!stack.isEmpty()) {
            if (item == Items.BLAZE_POWDER && player.isDiscrete() && this.isTame() && this.isOwnedBy(player)) {
                if (this.isBaby()) {
                    this.usePlayerItem(player, stack);
                    this.ageUp((int) ((float) (-this.getAge() / 20) * 0.8F), true);

                } else if (!this.isFixed() && this.getMateTimer() != 0) {
                    this.usePlayerItem(player, stack);
                    this.setMateTimer(this.getMateTimer() / 2);
                }
                return ActionResultType.CONSUME;
            }

            if (isFood(stack) && this.getHealth() < this.getMaxHealth()) {
                this.usePlayerItem(player, stack);
                this.heal(1.0F);
                return ActionResultType.CONSUME;
            }

            if (item == Items.BONE && player.isDiscrete()) {
                if (this.getSex() == Genetics.Sex.FEMALE && this.getBreedingStatus("ispregnant"))
                    player.displayClientMessage(new TranslationTextComponent("chat.info.kitten_count", this.getKittens()), true);
                if (this.isBaby())
                    player.displayClientMessage(new StringTextComponent(this.getAge() + " // " + this.getAgeTracker() + " // " + this.getMatureTimer()), true);
                return ActionResultType.CONSUME;
            }

            if (item == SCItems.TREAT_BAG.get() && player.distanceToSqr(this) < 9.0D && (!this.isTame() || this.isOwnedBy(player))) {
                if (player.isDiscrete()) {
                    if (this.getHomePos().isPresent()) {
                        this.resetHomePos();
                        player.displayClientMessage(new TranslationTextComponent("chat.info.remove_home", this.getName()), true);
                    } else {
                        this.setHomePos(this.getOnPos());
                        player.displayClientMessage(new TranslationTextComponent("chat.info.set_home", this.getName(), getHomePos().get().getX(), getHomePos().get().getY(), getHomePos().get().getZ()), true);
                    }
                    return ActionResultType.SUCCESS;
                } else if (this.getHomePos().isPresent())
                    player.displayClientMessage(new StringTextComponent(getHomePos().get().getX() + ", " + getHomePos().get().getY() + ", " + getHomePos().get().getZ()), true);
            }
        }

        if (this.isOwnedBy(player) && !player.isDiscrete()) {
            this.setOrderedToSit(!this.isOrderedToSit());
            this.getNavigation().stop();
            this.setTarget(null);
        }

        return super.mobInteract(player, hand);
    }

    @Override
    public boolean causeFallDamage(float distance, float damageMultiplier) {
        return false;
    }

    public boolean isAngry() {
        return ((this.entityData.get(DATA_FLAGS_ID)) & 2) != 0;
    }

    public void setAngry(boolean angry) {
        byte b0 = this.entityData.get(DATA_FLAGS_ID);

        if (angry)
            this.entityData.set(DATA_FLAGS_ID, (byte) (b0 | 2));
        else
            this.entityData.set(DATA_FLAGS_ID, (byte) (b0 & -3));
    }

    @Nullable
    @Override
    protected SoundEvent getAmbientSound() {
        if (this.isAngry()) {
            return this.random.nextInt(10) == 0 ? SoundEvents.CAT_HISS : null;
        } else if (this.isInLove() /*|| this.PURR*/) {
            return SoundEvents.CAT_PURR;
        } else {
            if (this.isTame())
                return this.random.nextInt(10) == 0 ? SoundEvents.CAT_PURREOW : SoundEvents.CAT_AMBIENT;
            return SoundEvents.CAT_STRAY_AMBIENT;
        }
    }

    @Override
    public int getAmbientSoundInterval() {
        return 240;
    }

    @Nullable
    @Override
    protected SoundEvent getHurtSound(DamageSource damageSource) {
        return SoundEvents.CAT_HURT;
    }

    @Nullable
    @Override
    protected SoundEvent getDeathSound() {
        return SoundEvents.CAT_DEATH;
    }

    @Override
    public void setCustomName(@Nullable ITextComponent name) {
//        if (this.getOwnerUUID() != null) {
//            String key = SCReference.getCustomCats().get(this.getOwnerUUID());
//            if (key != null && key.equalsIgnoreCase(name.getString())) {
//                SimplyCatEntity cat = this;
//                switch (name.getString().toLowerCase()) {
//                    case "penny":
//                        cat.setGenotype(FUR_LENGTH, "L-l");
//                        cat.setGenotype(EUMELANIN, "B-B");
//                        cat.setGenotype(PHAEOMELANIN, "XO-Xo");
//                        cat.setGenotype(DILUTION, "D-d");
//                        cat.setGenotype(DILUTE_MOD, "dm-dm");
//                        cat.setGenotype(AGOUTI, "A-a");
//                        cat.setGenotype(TABBY, "mc-mc");
//                        cat.setGenotype(SPOTTED, "sp-sp");
//                        cat.setGenotype(TICKED, "ta-ta");
//                        cat.setGenotype(INHIBITOR, "i-i");
//                        cat.setGenotype(COLORPOINT, "C-cs");
//                        cat.setGenotype(WHITE, "Ws-w");
//                        cat.setGenotype(BOBTAIL, "Jb-Jb");
//                        cat.selectWhiteMarkings();
//                        cat.setGenotype(EYE_COLOR, "green");
//                        cat.setFixed((byte) 1);
//
//                    case "spinny":
//                        cat.setGenotype(FUR_LENGTH, "L-l");
//                        cat.setGenotype(EUMELANIN, "B-B");
//                        cat.setGenotype(PHAEOMELANIN, "Xo-Xo");
//                        cat.setGenotype(DILUTION, "D-d");
//                        cat.setGenotype(DILUTE_MOD, "dm-dm");
//                        cat.setGenotype(AGOUTI, "a-a");
//                        cat.setGenotype(TABBY, "mc-mc");
//                        cat.setGenotype(SPOTTED, "sp-sp");
//                        cat.setGenotype(TICKED, "ta-ta");
//                        cat.setGenotype(INHIBITOR, "i-i");
//                        cat.setGenotype(COLORPOINT, "C-cs");
//                        cat.setGenotype(WHITE, "w-w");
//                        cat.setGenotype(BOBTAIL, "Jb-Jb");
//                        cat.selectWhiteMarkings();
//                        cat.setGenotype(EYE_COLOR, "gold");
//                }
//            }
//        }
        super.setCustomName(name);
    }
}
