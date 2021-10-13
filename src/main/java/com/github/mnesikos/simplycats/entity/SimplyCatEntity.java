package com.github.mnesikos.simplycats.entity;

import com.github.mnesikos.simplycats.SCReference;
import com.github.mnesikos.simplycats.SimplyCats;
import com.github.mnesikos.simplycats.configuration.SCConfig;
import com.github.mnesikos.simplycats.entity.core.Genetics;
import com.github.mnesikos.simplycats.entity.core.Genetics.*;
import com.github.mnesikos.simplycats.entity.goal.CatSitOnBlockGoal;
import com.github.mnesikos.simplycats.entity.goal.*;
import com.github.mnesikos.simplycats.event.SCEvents;
import com.github.mnesikos.simplycats.item.SCItems;
import com.google.common.collect.ImmutableList;
import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.entity.*;
import net.minecraft.entity.ai.attributes.AttributeModifierMap;
import net.minecraft.entity.ai.attributes.Attributes;
import net.minecraft.entity.ai.goal.*;
import net.minecraft.entity.monster.IMob;
import net.minecraft.entity.passive.AnimalEntity;
import net.minecraft.entity.passive.TameableEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.item.crafting.Ingredient;
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
import net.minecraft.world.Difficulty;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.IServerWorld;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import javax.annotation.Nullable;
import java.util.Optional;
import java.util.UUID;
import java.util.function.Predicate;

public class SimplyCatEntity extends TameableEntity {
    private static final DataParameter<String> EYE_COLOR = EntityDataManager.defineId(SimplyCatEntity.class, DataSerializers.STRING);
    private static final DataParameter<String> FUR_LENGTH = EntityDataManager.defineId(SimplyCatEntity.class, DataSerializers.STRING);
    private static final DataParameter<String> EUMELANIN = EntityDataManager.defineId(SimplyCatEntity.class, DataSerializers.STRING);
    private static final DataParameter<String> PHAEOMELANIN = EntityDataManager.defineId(SimplyCatEntity.class, DataSerializers.STRING);
    private static final DataParameter<String> DILUTION = EntityDataManager.defineId(SimplyCatEntity.class, DataSerializers.STRING);
    private static final DataParameter<String> DILUTE_MOD = EntityDataManager.defineId(SimplyCatEntity.class, DataSerializers.STRING);
    private static final DataParameter<String> AGOUTI = EntityDataManager.defineId(SimplyCatEntity.class, DataSerializers.STRING);
    private static final DataParameter<String> TABBY = EntityDataManager.defineId(SimplyCatEntity.class, DataSerializers.STRING);
    private static final DataParameter<String> SPOTTED = EntityDataManager.defineId(SimplyCatEntity.class, DataSerializers.STRING);
    private static final DataParameter<String> TICKED = EntityDataManager.defineId(SimplyCatEntity.class, DataSerializers.STRING);
    private static final DataParameter<String> COLORPOINT = EntityDataManager.defineId(SimplyCatEntity.class, DataSerializers.STRING);
    private static final DataParameter<String> WHITE = EntityDataManager.defineId(SimplyCatEntity.class, DataSerializers.STRING);
    private static final DataParameter<String> BOBTAIL = EntityDataManager.defineId(SimplyCatEntity.class, DataSerializers.STRING);
    private static final DataParameter<String> WHITE_0 = EntityDataManager.defineId(SimplyCatEntity.class, DataSerializers.STRING);
    private static final DataParameter<String> WHITE_1 = EntityDataManager.defineId(SimplyCatEntity.class, DataSerializers.STRING);
    private static final DataParameter<String> WHITE_2 = EntityDataManager.defineId(SimplyCatEntity.class, DataSerializers.STRING);
    private static final DataParameter<String> WHITE_PAWS_0 = EntityDataManager.defineId(SimplyCatEntity.class, DataSerializers.STRING);
    private static final DataParameter<String> WHITE_PAWS_1 = EntityDataManager.defineId(SimplyCatEntity.class, DataSerializers.STRING);
    private static final DataParameter<String> WHITE_PAWS_2 = EntityDataManager.defineId(SimplyCatEntity.class, DataSerializers.STRING);
    private static final DataParameter<String> WHITE_PAWS_3 = EntityDataManager.defineId(SimplyCatEntity.class, DataSerializers.STRING);
    private final String[] whiteTexturesArray = new String[3];
    private final String[] whitePawTexturesArray = new String[4];
    private String texturePrefix;
    private final String[] catTexturesArray = new String[12];

    private static final DataParameter<Optional<BlockPos>> HOME_POSITION = EntityDataManager.defineId(SimplyCatEntity.class, DataSerializers.OPTIONAL_BLOCK_POS);
    public static final DataParameter<String> OWNER_NAME = EntityDataManager.defineId(SimplyCatEntity.class, DataSerializers.STRING);
    private static final DataParameter<Byte> FIXED = EntityDataManager.defineId(SimplyCatEntity.class, DataSerializers.BYTE);
    private static final DataParameter<Byte> IN_HEAT = EntityDataManager.defineId(SimplyCatEntity.class, DataSerializers.BYTE);
    private static final DataParameter<Byte> IS_PREGNANT = EntityDataManager.defineId(SimplyCatEntity.class, DataSerializers.BYTE);
    private static final DataParameter<Integer> MATE_TIMER = EntityDataManager.defineId(SimplyCatEntity.class, DataSerializers.INT);
    private static final DataParameter<Integer> KITTENS = EntityDataManager.defineId(SimplyCatEntity.class, DataSerializers.INT);
    private static final DataParameter<Optional<UUID>> MOTHER = EntityDataManager.defineId(SimplyCatEntity.class, DataSerializers.OPTIONAL_UUID);
    private static final DataParameter<Optional<UUID>> FATHER = EntityDataManager.defineId(SimplyCatEntity.class, DataSerializers.OPTIONAL_UUID);
    private static final DataParameter<Integer> AGE_TRACKER = EntityDataManager.defineId(SimplyCatEntity.class, DataSerializers.INT);
    private static final DataParameter<Float> MATURE_TIMER = EntityDataManager.defineId(SimplyCatEntity.class, DataSerializers.FLOAT);

    public final Predicate<LivingEntity> PREY_SELECTOR = (entity) -> {
        EntityType<?> entityType = entity.getType();
        if (entity instanceof TameableEntity && ((TameableEntity) entity).isTame())
            return false;

        return entityType != EntityType.PLAYER && !(entity instanceof IMob) && !this.isAlliedTo(entity) && SCEvents.isEntityPrey(entity);
    };
    private SimplyCatEntity followParent;
    private TemptGoal temptGoal;
    private CatTargetNearestGoal aiTargetNearest;
    private Vector3d nearestLaser;

    public SimplyCatEntity(EntityType<? extends TameableEntity> type, World world) {
        super(type, world);
        this.setPhenotype();
    }

    @Override
    protected void registerGoals() {
        this.temptGoal = new TemptGoal(this, 1.2D, Ingredient.of(SCItems.CATNIP.get(), SCItems.TREAT_BAG.get()), false);
        this.goalSelector.addGoal(1, new SwimGoal(this));
        this.goalSelector.addGoal(1, new CatSitGoal(this));
        this.goalSelector.addGoal(3, this.temptGoal);
        this.goalSelector.addGoal(4, new CatFollowParentGoal(this, 1.0D));
        this.goalSelector.addGoal(5, new CatSitOnBlockGoal(this, 1.0D, 8));
        this.goalSelector.addGoal(6, new CatBirthGoal(this));
        this.goalSelector.addGoal(7, new LeapAtTargetGoal(this, 0.4F));
        this.goalSelector.addGoal(8, new CatAttackGoal(this));
        if (!this.isFixed())
            this.goalSelector.addGoal(9, new CatMateGoal(this, 1.2D));
        this.goalSelector.addGoal(10, new CatWanderGoal(this, 1.0D));
        this.goalSelector.addGoal(11, new LookAtGoal(this, LivingEntity.class, 7.0F));
        this.goalSelector.addGoal(12, new LookRandomlyGoal(this));
        if (SCConfig.ATTACK_AI) {
            this.aiTargetNearest = new CatTargetNearestGoal<>(this, LivingEntity.class, true, PREY_SELECTOR);
            this.targetSelector.addGoal(1, this.aiTargetNearest);
        }
    }

    public static AttributeModifierMap.MutableAttribute createAttributes() {
        return MobEntity.createMobAttributes().add(Attributes.MAX_HEALTH, 14.0D).add(Attributes.MOVEMENT_SPEED, 0.3F).add(Attributes.KNOCKBACK_RESISTANCE, 0.7D).add(Attributes.ATTACK_DAMAGE, 2.0D);
    }

    @Override
    protected void defineSynchedData() {
        super.defineSynchedData();
        this.entityData.define(EYE_COLOR, EyeColor.COPPER.toString());
        this.entityData.define(FUR_LENGTH, "L-L");
        this.entityData.define(EUMELANIN, "B-B");
        this.entityData.define(PHAEOMELANIN, "Xo-Xo");
        this.entityData.define(DILUTION, "D-D");
        this.entityData.define(DILUTE_MOD, "dm-dm");
        this.entityData.define(AGOUTI, "a-a");
        this.entityData.define(TABBY, "Mc-Mc");
        this.entityData.define(SPOTTED, "sp-sp");
        this.entityData.define(TICKED, "ta-ta");
        this.entityData.define(COLORPOINT, "C-C");
        this.entityData.define(WHITE, "w-w");
        this.entityData.define(BOBTAIL, "Jb-Jb");

        this.entityData.define(WHITE_0, "");
        this.entityData.define(WHITE_1, "");
        this.entityData.define(WHITE_2, "");
        this.entityData.define(WHITE_PAWS_0, "");
        this.entityData.define(WHITE_PAWS_1, "");
        this.entityData.define(WHITE_PAWS_2, "");
        this.entityData.define(WHITE_PAWS_3, "");

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
        if (this.level.getDifficulty() == Difficulty.PEACEFUL || !SCConfig.ATTACK_AI)
            this.targetSelector.removeGoal(aiTargetNearest);

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
        this.setPhenotype();

        if (!this.level.isClientSide)
            if (this.isTame())
                this.setOrderedToSit(!this.isOrderedToSit());
        if (this.getSex() == Genetics.Sex.FEMALE && !this.isFixed())
            this.setTimeCycle("end", random.nextInt(SCConfig.HEAT_COOLDOWN));

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

        if (this.tickCount % 40 == 0) {
            if (!this.level.isClientSide && this.getOwner() != null)
                this.setOwnerName(this.getOwner().getDisplayName().getString());
        }

        if (this.level.isClientSide && this.entityData.isDirty()) {
            this.entityData.clearDirty();
            this.resetTexturePrefix();
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

    public void setPhenotype() {
        this.setGenotype(FUR_LENGTH, FurLength.init(random) + "-" + FurLength.init(random));
        this.setGenotype(EUMELANIN, Eumelanin.init(random) + "-" + Eumelanin.init(random));
        this.setGenotype(PHAEOMELANIN, Phaeomelanin.init(random));
        this.setGenotype(DILUTION, Dilution.init(random) + "-" + Dilution.init(random));
        this.setGenotype(DILUTE_MOD, DiluteMod.init(random) + "-" + DiluteMod.init(random));
        this.setGenotype(AGOUTI, Agouti.init(random) + "-" + Agouti.init(random));
        this.setGenotype(TABBY, Tabby.init(random) + "-" + Tabby.init(random));
        this.setGenotype(SPOTTED, Spotted.init(random) + "-" + Spotted.init(random));
        this.setGenotype(TICKED, Ticked.init(random) + "-" + Ticked.init(random));
        this.setGenotype(COLORPOINT, Colorpoint.init(random) + "-" + Colorpoint.init(random));
        this.setGenotype(WHITE, White.init(random) + "-" + White.init(random));
        this.setGenotype(BOBTAIL, Bobtail.init(random) + "-" + Bobtail.init(random));
        this.selectWhiteMarkings();
        this.setGenotype(EYE_COLOR, selectEyeColor());
    }

    private String selectEyeColor() {
        String color = EyeColor.init(random.nextInt(4));
        // todo change whiteCheck == White.Spotting to a better check for proper white face check
        if (getGenotype(WHITE).contains(White.DOMINANT.getAllele()))
            color = EyeColor.init(random.nextInt(5));
        if (getPhenotype(COLORPOINT).equalsIgnoreCase(Colorpoint.COLORPOINT.toString()))
            color = EyeColor.init(4);
        return color;
    }

    void selectWhiteMarkings() {
        int base;
        int body = 0;
        int face = 0;
        int tail = 0;

        for (int j = 0; j <= 3; j++) {
            this.whitePawTexturesArray[j] = "";
            this.setWhitePawTextures(j, "");
        }

        switch (this.getGenotype(WHITE)) {
            case "Wd-Wd":
            case "Wd-w":
            case "Wd-Ws":
            case "w-Wd":
            case "Ws-Wd":
                base = 6;
                body = 1;
                face = 0;
                tail = 0;
                break;

            case "w-w":
                base = 0;
                body = 0;
                face = 0;
                tail = 0;
                break;

            case "Ws-Ws":
                base = random.nextInt(2) + 4; //4-5
                if (base == 5) {
                    body = random.nextInt(4) + 1;
                    face = random.nextInt(6) + 1;
                    if (body > 1)
                        tail = random.nextInt(3) + 1;
                } else if (base == 4) {
                    body = 1;
                    face = random.nextInt(5) + 1;
                }
                if (random.nextInt(10) == 0) { //10% chance for solid white
                    base = 6;
                    body = 1;
                    face = 0;
                    tail = 0;
                }
                break;

            case "Ws-w":
            case "w-Ws":
                base = random.nextInt(3) + 1; //1-3
                body = 1;
                if (base == 2 || base == 3)
                    this.selectWhitePaws(base);
                if (base == 3)
                    face = random.nextInt(5) + 1;
                break;

            default:
                throw new IllegalArgumentException("Error selecting white markings; " + this.getGenotype(WHITE));
        }

        this.whiteTexturesArray[0] = body == 0 ? "" : "white_" + base + "_body" + body;
        this.setWhiteTextures(0, whiteTexturesArray[0]);

        this.whiteTexturesArray[1] = face == 0 ? "" : "white_" + (base == 3 || base == 4 ? 34 : base) + "_face" + face;
        this.setWhiteTextures(1, whiteTexturesArray[1]);

        this.whiteTexturesArray[2] = tail == 0 ? "" : "white_" + base + "_tail" + tail;
        this.setWhiteTextures(2, whiteTexturesArray[2]);
    }

    private void selectWhitePaws(int base) {
        /*
         * boolean all is set so all 4 paws are white
         * making it more common than only random 1-4 white paws
         */
        boolean all = random.nextInt(4) <= 2;

        if (all || random.nextInt(4) <= 2) {
            this.whitePawTexturesArray[0] = "white_" + base + "_paw1";
            this.setWhitePawTextures(0, whitePawTexturesArray[0]);
        }

        if (all || random.nextInt(4) <= 2) {
            this.whitePawTexturesArray[1] = "white_" + base + "_paw2";
            this.setWhitePawTextures(1, whitePawTexturesArray[1]);
        }

        if (all || random.nextInt(4) <= 2) {
            this.whitePawTexturesArray[2] = "white_" + base + "_paw3";
            this.setWhitePawTextures(2, whitePawTexturesArray[2]);
        }

        if (all || random.nextInt(4) <= 2) {
            this.whitePawTexturesArray[3] = "white_" + base + "_paw4";
            this.setWhitePawTextures(3, whitePawTexturesArray[3]);
        }
    }

    private String getWhiteTextures(int i) {
        switch (i) {
            case 0:
                return this.entityData.get(WHITE_0);
            case 1:
                return this.entityData.get(WHITE_1);
            case 2:
                return this.entityData.get(WHITE_2);
            default:
                return "";
        }
    }

    private void setWhiteTextures(int i, String value) {
        switch (i) {
            case 0:
                this.entityData.set(WHITE_0, value);
                break;
            case 1:
                this.entityData.set(WHITE_1, value);
                break;
            case 2:
                this.entityData.set(WHITE_2, value);
                break;
        }
    }

    private String getWhitePawTextures(int i) {
        switch (i) {
            case 0:
                return this.entityData.get(WHITE_PAWS_0);
            case 1:
                return this.entityData.get(WHITE_PAWS_1);
            case 2:
                return this.entityData.get(WHITE_PAWS_2);
            case 3:
                return this.entityData.get(WHITE_PAWS_3);
            default:
                return "";
        }
    }

    private void setWhitePawTextures(int i, String value) {
        switch (i) {
            case 0:
                this.entityData.set(WHITE_PAWS_0, value);
                break;
            case 1:
                this.entityData.set(WHITE_PAWS_1, value);
                break;
            case 2:
                this.entityData.set(WHITE_PAWS_2, value);
                break;
            case 3:
                this.entityData.set(WHITE_PAWS_3, value);
                break;
        }
    }

    protected String getGenotype(DataParameter<String> parameter) {
        return this.entityData.get(parameter);
    }

    void setGenotype(DataParameter<String> parameter, String value) {
        this.entityData.set(parameter, value);
    }

    public Sex getSex() {
        return this.entityData.get(PHAEOMELANIN).contains(Phaeomelanin.MALE.getAllele()) ? Sex.MALE : Sex.FEMALE;
    }

    public boolean isBobtail() {
        return Bobtail.isBobtail(this.entityData.get(BOBTAIL));
    }

    public boolean isLongFur() {
        return FurLength.getPhenotype(this.entityData.get(FUR_LENGTH)).equalsIgnoreCase(FurLength.LONG.toString());
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

    public void addFather(SimplyCatEntity father, int size) { //todo
        for (int i = 0; i < size; i++) {
            if (!this.getPersistentData().contains("Father" + i) || (this.getPersistentData().contains("Father" + i) && this.getPersistentData().getCompound("Father" + i).isEmpty())) {
                this.getPersistentData().put("Father" + i, father.saveWithoutId(new CompoundNBT()));
                //break;
            }
        }
    }

    private void setFather(int i, INBT father) { //todo
        if (this.getPersistentData().contains("Father" + i))
            this.getPersistentData().put("Father" + i, father);
    }

    public CompoundNBT getFather(int i) {
        return this.getPersistentData().getCompound("Father" + i);
    }

    @Override
    public void addAdditionalSaveData(CompoundNBT compound) {
        super.addAdditionalSaveData(compound);
        compound.putString("EyeColor", this.getGenotype(EYE_COLOR));
        compound.putString("FurLength", this.getGenotype(FUR_LENGTH));
        compound.putString("Eumelanin", this.getGenotype(EUMELANIN));
        compound.putString("Phaeomelanin", this.getGenotype(PHAEOMELANIN));
        compound.putString("Dilution", this.getGenotype(DILUTION));
        compound.putString("DiluteMod", this.getGenotype(DILUTE_MOD));
        compound.putString("Agouti", this.getGenotype(AGOUTI));
        compound.putString("Tabby", this.getGenotype(TABBY));
        compound.putString("Spotted", this.getGenotype(SPOTTED));
        compound.putString("Ticked", this.getGenotype(TICKED));
        compound.putString("Colorpoint", this.getGenotype(COLORPOINT));
        compound.putString("White", this.getGenotype(WHITE));
        compound.putString("Bobtail", this.getGenotype(BOBTAIL));
        for (int i = 0; i <= 2; i++)
            compound.putString("White_" + i, this.getWhiteTextures(i));
        for (int i = 0; i <= 3; i++)
            compound.putString("WhitePaws_" + i, this.getWhitePawTextures(i));
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
        this.setGenotype(EYE_COLOR, compound.getString("EyeColor"));
        this.setGenotype(FUR_LENGTH, compound.getString("FurLength"));
        this.setGenotype(EUMELANIN, compound.getString("Eumelanin"));
        this.setGenotype(PHAEOMELANIN, compound.getString("Phaeomelanin"));
        this.setGenotype(DILUTION, compound.getString("Dilution"));
        this.setGenotype(DILUTE_MOD, compound.getString("DiluteMod"));
        this.setGenotype(AGOUTI, compound.getString("Agouti"));
        this.setGenotype(TABBY, compound.getString("Tabby"));
        this.setGenotype(SPOTTED, compound.getString("Spotted"));
        this.setGenotype(TICKED, compound.getString("Ticked"));
        this.setGenotype(COLORPOINT, compound.getString("Colorpoint"));
        this.setGenotype(WHITE, compound.getString("White"));
        this.setGenotype(BOBTAIL, compound.getString("Bobtail"));
        for (int i = 0; i <= 2; i++)
            this.setWhiteTextures(i, compound.getString("White_" + i));
        for (int i = 0; i <= 3; i++)
            this.setWhitePawTextures(i, compound.getString("WhitePaws_" + i));
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

    private String getPhenotype(DataParameter<String> dataParameter) {
        if (dataParameter == FUR_LENGTH)
            return FurLength.getPhenotype(this.getGenotype(dataParameter));
        else if (dataParameter == EUMELANIN)
            return Eumelanin.getPhenotype(this.getGenotype(dataParameter));
        else if (dataParameter == PHAEOMELANIN)
            return Phaeomelanin.getPhenotype(this.getGenotype(dataParameter));
        else if (dataParameter == DILUTION)
            return Dilution.getPhenotype(this.getGenotype(dataParameter));
        else if (dataParameter == DILUTE_MOD)
            return DiluteMod.getPhenotype(this.getGenotype(dataParameter));
        else if (dataParameter == AGOUTI)
            return Agouti.getPhenotype(this.getGenotype(dataParameter));
        else if (dataParameter == TABBY)
            return Tabby.getPhenotype(this.getGenotype(dataParameter));
        else if (dataParameter == SPOTTED)
            return Spotted.getPhenotype(this.getGenotype(dataParameter));
        else if (dataParameter == TICKED)
            return Ticked.getPhenotype(this.getGenotype(dataParameter));
        else if (dataParameter == COLORPOINT)
            return Colorpoint.getPhenotype(this.getGenotype(dataParameter));
        else if (dataParameter == WHITE)
            return White.getPhenotype(this.getGenotype(dataParameter));
        else // EYES
            return this.getGenotype(EYE_COLOR);
    }

    private void resetTexturePrefix() {
        this.texturePrefix = null;
    }

    @OnlyIn(Dist.CLIENT)
    private void setCatTexturePaths() {
        String solid = this.getPhenotype(EUMELANIN);
        if (this.getPhenotype(PHAEOMELANIN).equalsIgnoreCase(Phaeomelanin.RED.toString().toLowerCase()))
            solid = this.getPhenotype(PHAEOMELANIN);
        if (this.getPhenotype(DILUTION).equalsIgnoreCase(Dilution.DILUTE.toString().toLowerCase())) {
            solid = solid + "_" + this.getPhenotype(DILUTION);
            if (this.getPhenotype(DILUTE_MOD).equalsIgnoreCase(DiluteMod.CARAMELIZED.toString().toLowerCase()))
                solid = solid + "_" + this.getPhenotype(DILUTE_MOD);
        }

        String tabby = this.getPhenotype(TABBY) + "_" + solid;
        if (this.getGenotype(SPOTTED).contains(Spotted.SPOTTED.getAllele()))
            tabby = this.getPhenotype(SPOTTED) + "_" + tabby;
        if (this.getPhenotype(TICKED).equalsIgnoreCase(Ticked.TICKED.toString().toLowerCase()))
            tabby = this.getPhenotype(TICKED) + (this.getGenotype(TICKED).contains(Ticked.NON_TICKED.getAllele()) ? "_residual" : "") + "_" + solid;

        String tortie = "";
        if (this.getPhenotype(PHAEOMELANIN).equalsIgnoreCase(Phaeomelanin.TORTOISESHELL.toString().toLowerCase())) {
            tortie = this.getPhenotype(PHAEOMELANIN) + "_" + (tabby.replace(("_" + solid), ""));
            if (this.getPhenotype(DILUTION).equalsIgnoreCase(Dilution.DILUTE.toString().toLowerCase())) {
                tortie = tortie + "_" + this.getPhenotype(DILUTION);
                if (this.getPhenotype(DILUTE_MOD).equalsIgnoreCase(DiluteMod.CARAMELIZED.toString().toLowerCase()))
                    tortie = tortie + "_" + this.getPhenotype(DILUTE_MOD);
            }
        }

        if (!this.getPhenotype(PHAEOMELANIN).equalsIgnoreCase(Phaeomelanin.RED.toString()) && this.getPhenotype(AGOUTI).equalsIgnoreCase(Agouti.SOLID.toString().toLowerCase()))
            tabby = "";

        String colorpoint = "";
        if (!this.getPhenotype(COLORPOINT).equalsIgnoreCase(Colorpoint.NOT_POINTED.toString().toLowerCase())) {
            colorpoint = this.getPhenotype(COLORPOINT);
            if (!tabby.equals("") && !this.getPhenotype(PHAEOMELANIN).equalsIgnoreCase(Phaeomelanin.RED.toString()))
                colorpoint = colorpoint + "_" + "tabby";
            else if (solid.equalsIgnoreCase(Eumelanin.BLACK.toString()))
                colorpoint = colorpoint + "_" + solid;
            else if (this.getPhenotype(PHAEOMELANIN).equalsIgnoreCase(Phaeomelanin.RED.toString()))
                colorpoint = colorpoint + "_red";
            if (!tortie.equals(""))
                tortie = tortie + "_point";
        }

        this.catTexturesArray[0] = SimplyCats.MOD_ID + ":textures/entity/cat/solid/" + solid + ".png";
        this.catTexturesArray[1] = tabby.equals("") ? null : (SimplyCats.MOD_ID + ":textures/entity/cat/tabby/" + tabby + ".png");
        this.catTexturesArray[2] = tortie.equals("") ? null : (SimplyCats.MOD_ID + ":textures/entity/cat/tortie/" + tortie + ".png");
        this.catTexturesArray[3] = colorpoint.equals("") ? null : (SimplyCats.MOD_ID + ":textures/entity/cat/colorpoint/" + colorpoint + ".png");
        this.catTexturesArray[4] = this.getWhiteTextures(0).equals("") ? null : (SimplyCats.MOD_ID + ":textures/entity/cat/white/" + this.getWhiteTextures(0) + ".png");
        this.catTexturesArray[5] = this.getWhiteTextures(1).equals("") ? null : (SimplyCats.MOD_ID + ":textures/entity/cat/white/" + this.getWhiteTextures(1) + ".png");
        this.catTexturesArray[6] = this.getWhiteTextures(2).equals("") ? null : (SimplyCats.MOD_ID + ":textures/entity/cat/white/" + this.getWhiteTextures(2) + ".png");
        this.catTexturesArray[7] = this.getWhitePawTextures(0).equals("") ? null : (SimplyCats.MOD_ID + ":textures/entity/cat/white/" + this.getWhitePawTextures(0) + ".png");
        this.catTexturesArray[8] = this.getWhitePawTextures(1).equals("") ? null : (SimplyCats.MOD_ID + ":textures/entity/cat/white/" + this.getWhitePawTextures(1) + ".png");
        this.catTexturesArray[9] = this.getWhitePawTextures(2).equals("") ? null : (SimplyCats.MOD_ID + ":textures/entity/cat/white/" + this.getWhitePawTextures(2) + ".png");
        this.catTexturesArray[10] = this.getWhitePawTextures(3).equals("") ? null : (SimplyCats.MOD_ID + ":textures/entity/cat/white/" + this.getWhitePawTextures(3) + ".png");
        this.catTexturesArray[11] = SimplyCats.MOD_ID + ":textures/entity/cat/eyes/" + this.getPhenotype(EYE_COLOR) + ".png";
        this.texturePrefix = "cat/" + solid + tabby + tortie + colorpoint +
                this.getWhiteTextures(0) + this.getWhiteTextures(1) + this.getWhiteTextures(2) +
                this.getWhitePawTextures(0) + this.getWhitePawTextures(1) +
                this.getWhitePawTextures(2) + this.getWhitePawTextures(3) +
                getPhenotype(EYE_COLOR);
    }

    @OnlyIn(Dist.CLIENT)
    public String getCatTexture() {
        if (this.texturePrefix == null)
            this.setCatTexturePaths();

        return this.texturePrefix;
    }

    @OnlyIn(Dist.CLIENT)
    public String[] getTexturePaths() {
        if (this.texturePrefix == null)
            this.setCatTexturePaths();

        return this.catTexturesArray;
    }

    public boolean canBeTamed(PlayerEntity player) {
        return (SCConfig.TAMED_LIMIT == 0 || player.getPersistentData().getInt("CatCount") < SCConfig.TAMED_LIMIT) && !this.isTame();
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
        return item.getItem() == Items.BLAZE_POWDER;
    }

    private String inheritGene(String motherAlleles, String fatherAlleles) {
        String[] maternal = motherAlleles.split("-");
        String[] paternal = fatherAlleles.split("-");
        return maternal[random.nextInt(2)] + "-" + paternal[random.nextInt(2)];
    }

    @Nullable
    @Override
    public AgeableEntity getBreedOffspring(ServerWorld world, AgeableEntity parFather) {
        EntityDataManager father = parFather.getEntityData();
        EntityDataManager mother = this.getEntityData();
        SimplyCatEntity child = SimplyCats.CAT.get().create(this.level);

        ImmutableList<DataParameter<String>> parameters = ImmutableList.of(
                FUR_LENGTH,
                EUMELANIN,
                PHAEOMELANIN,
                DILUTION,
                DILUTE_MOD,
                AGOUTI,
                TABBY,
                SPOTTED,
                TICKED,
                COLORPOINT,
                WHITE,
                BOBTAIL
        );

        for (DataParameter<String> geneParameter : parameters) {
            String inherited = inheritGene(mother.get(geneParameter),
                    father.get(geneParameter));
            child.setGenotype(geneParameter, inherited);
        }

        child.selectWhiteMarkings();

        int eyesMin;
        int eyesMax;
        int matEye = EyeColor.valueOf(mother.get(EYE_COLOR).toUpperCase()).ordinal();
        int patEye = EyeColor.valueOf(father.get(EYE_COLOR).toUpperCase()).ordinal();
        if (matEye > patEye) {
            eyesMin = patEye - 1;
            eyesMax = matEye;
        } else {
            eyesMin = matEye - 1;
            eyesMax = patEye;
        }
        eyesMin = eyesMin < 0 ? 0 : eyesMin;
        String white = child.getGenotype(WHITE);
        if (white.contains(White.DOMINANT.getAllele()))
            eyesMax = 4;
        else
            eyesMax = eyesMax >= 4 ? (eyesMin < 3 ? eyesMin + 1 : 3) : eyesMax;
        int eyes = random.nextInt((eyesMax - eyesMin) + 1) + eyesMin;
        String eye = EyeColor.init(matEye == 4 && patEye == 4 ? (eyesMax == 4 ? 4 : random.nextInt(4)) : eyes);
        String point = child.getGenotype(COLORPOINT);
        if (point.contentEquals(Colorpoint.COLORPOINT.getAllele() + "-" + Colorpoint.COLORPOINT.getAllele()))
            eye = EyeColor.init(4);

        child.setGenotype(EYE_COLOR, eye);

        if (this.isTame() && this.getOwnerUUID() != null) { // checks if mother is tamed & her owner's UUID exists
            PlayerEntity owner = this.level.getPlayerByUUID(this.getOwnerUUID()); // grabs owner by UUID
            if (owner != null && child.canBeTamed(owner)) { // checks if owner is not null (is online), and is able to tame the kitten OR if the tame limit is disabled
                child.setTamed(this.isTame(), owner); // sets tamed by owner
                if (this.getHomePos().isPresent()) // checks mother's home point
                    child.setHomePos(this.getHomePos().get()); // sets kitten's home point to mother's
            }
        }

        return child;
    }

    private boolean isFoodItem(ItemStack item) {
        return SCReference.catFoodItems(item);
    }

    @Override
    public ActionResultType mobInteract(PlayerEntity player, Hand hand) {
        ItemStack stack = player.getItemInHand(hand);

        if (!stack.isEmpty()) {
            if (isFood(stack) && player.isCrouching() && this.isTame() && this.isOwnedBy(player)) {
                if (this.isBaby()) {
                    this.usePlayerItem(player, stack);
                    this.ageUp((int) ((float) (-this.getAge() / 20) * 0.8F), true);

                } else if (!this.isFixed() && this.getMateTimer() != 0) {
                    this.usePlayerItem(player, stack);
                    this.setMateTimer(this.getMateTimer() / 2);
                }
                return ActionResultType.SUCCESS;
            }

            if (isFoodItem(stack) && this.getHealth() < this.getMaxHealth()) {
                this.usePlayerItem(player, stack);
                this.heal(1.0F);
                return ActionResultType.CONSUME;
            }

            if (stack.getItem() == Items.BONE && player.isCrouching()) {
                if (this.getSex() == Genetics.Sex.FEMALE && this.getBreedingStatus("ispregnant"))
                    player.displayClientMessage(new TranslationTextComponent("chat.info.kitten_count", this.getKittens()), true);
                if (this.isBaby())
                    player.displayClientMessage(new StringTextComponent(this.getAge() + " // " + this.getAgeTracker() + " // " + this.getMatureTimer()), true);
                return ActionResultType.SUCCESS;
            }

            if ((this.temptGoal == null || this.temptGoal.isRunning()) && stack.getItem() == SCItems.TREAT_BAG.get() && player.distanceToSqr(this) < 9.0D) {
                if (player.isCrouching()) {
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

        if (this.isOwnedBy(player) && !this.level.isClientSide && !player.isCrouching()) {
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
        if (this.getOwnerUUID() != null) {
            String key = SCReference.getCustomCats().get(this.getOwnerUUID());
            if (key != null && key.equalsIgnoreCase(name.getString())) {
                SimplyCatEntity cat = (SimplyCatEntity) this;
                switch (name.getString().toLowerCase()) {
                    case "penny":
                        cat.setGenotype(FUR_LENGTH, "L-l");
                        cat.setGenotype(EUMELANIN, "B-B");
                        cat.setGenotype(PHAEOMELANIN, "XO-Xo");
                        cat.setGenotype(DILUTION, "D-d");
                        cat.setGenotype(DILUTE_MOD, "dm-dm");
                        cat.setGenotype(AGOUTI, "A-a");
                        cat.setGenotype(TABBY, "mc-mc");
                        cat.setGenotype(SPOTTED, "sp-sp");
                        cat.setGenotype(TICKED, "ta-ta");
                        cat.setGenotype(COLORPOINT, "C-cs");
                        cat.setGenotype(WHITE, "Ws-w");
                        cat.setGenotype(BOBTAIL, "Jb-Jb");
                        cat.selectWhiteMarkings();
                        cat.setGenotype(EYE_COLOR, "green");
                        cat.setFixed((byte) 1);

                    case "spinny":
                        cat.setGenotype(FUR_LENGTH, "L-l");
                        cat.setGenotype(EUMELANIN, "B-B");
                        cat.setGenotype(PHAEOMELANIN, "Xo-Xo");
                        cat.setGenotype(DILUTION, "D-d");
                        cat.setGenotype(DILUTE_MOD, "dm-dm");
                        cat.setGenotype(AGOUTI, "a-a");
                        cat.setGenotype(TABBY, "mc-mc");
                        cat.setGenotype(SPOTTED, "sp-sp");
                        cat.setGenotype(TICKED, "ta-ta");
                        cat.setGenotype(COLORPOINT, "C-cs");
                        cat.setGenotype(WHITE, "w-w");
                        cat.setGenotype(BOBTAIL, "Jb-Jb");
                        cat.selectWhiteMarkings();
                        cat.setGenotype(EYE_COLOR, "gold");
                }
            }
        }
        super.setCustomName(name);
    }
}
