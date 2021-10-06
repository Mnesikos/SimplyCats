package com.github.mnesikos.simplycats.entity;

import com.github.mnesikos.simplycats.SimplyCats;
import com.github.mnesikos.simplycats.configuration.SCConfig;
import com.github.mnesikos.simplycats.entity.core.Genetics.*;
import com.google.common.collect.ImmutableList;
import net.minecraft.entity.*;
import net.minecraft.entity.ai.attributes.AttributeModifierMap;
import net.minecraft.entity.ai.attributes.Attributes;
import net.minecraft.entity.passive.TameableEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.util.DamageSource;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.IServerWorld;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import javax.annotation.Nullable;

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

    public static final DataParameter<String> OWNER_NAME = EntityDataManager.defineId(SimplyCatEntity.class, DataSerializers.STRING);

    public SimplyCatEntity(EntityType<? extends TameableEntity> type, World world) {
        super(type, world);
        this.setPhenotype();
    }

    @Override
    protected void registerGoals() {
//        super.registerGoals();
    }

    @Override
    public ILivingEntityData finalizeSpawn(IServerWorld world, DifficultyInstance difficulty, SpawnReason spawnReason, @Nullable ILivingEntityData entityData, @Nullable CompoundNBT compound) {
        entityData = super.finalizeSpawn(world, difficulty, spawnReason, entityData, compound);
        this.setPhenotype();
        return entityData;
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

        this.entityData.define(OWNER_NAME, "");
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

    // hasHomePos
    // getHomePos
    // resetHomePos

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
        // todo System.out.println(this.texturePrefix);
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

    // onUpdate
    // onLivingUpdate

    public boolean canBeTamed(PlayerEntity player) {
        return (SCConfig.TAMED_LIMIT == 0 || player.getPersistentData().getInt("CatCount") < SCConfig.TAMED_LIMIT) && !this.isTame();
    }

    // processInteract

    /**
     * A custom setTamed method to set the owner's data along with taming or untaming a cat.
     *
     * @param tamed - true is tamed, false is untamed, used for this.setTamed(tamed) call at the end.
     * @param owner - the EntityPlayer who is taming the cat.
     */
    public void setTamed(boolean tamed, PlayerEntity owner) {
        int catCount = owner.getPersistentData().getInt("CatCount");
        if (tamed) {
//            owner.getPersistentData().putInt("CatCount", catCount + 1);
//            if (!level.isClientSide())
//                CHANNEL.sendTo(new SCNetworking(catCount + 1), (EntityPlayerMP) owner);
            this.setOwnerName(owner.getDisplayName().getString());

        } else {
//            owner.getPersistentData().putInt("CatCount", catCount - 1);
//            if (!level.isClientSide())
//                CHANNEL.sendTo(new SCNetworking(catCount - 1), (EntityPlayerMP) owner);
            this.setOwnerName("");
        }

        this.setTame(tamed);
    }

    // onDeath

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
                child.setOwnerUUID(this.getOwnerUUID()); // idk if this is needed, don't feel like testing it
//                if (this.hasHomePos()) // checks mother's home point
//                    child.setHomePos(this.getHomePos()); // sets kitten's home point to mother's
            }
        }

        return child;
    }

    @Override
    public boolean causeFallDamage(float distance, float damageMultiplier) {
        return false;
    }

    // canTriggerWalking
    // isAngry
    // setAngry

    @Nullable
    @Override
    protected SoundEvent getAmbientSound() {
        /*if (this.isAngry()) {
            return this.random.nextInt(10) == 0 ? SoundEvents.CAT_HISS : null;
        } else*/
        if (this.isInLove() /*|| this.PURR*/) {
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

    // getSoundVolume
    // setCustomNameTag
    // getName
    // getLootTable

    public static AttributeModifierMap.MutableAttribute createAttributes() {
        return MobEntity.createMobAttributes().add(Attributes.MAX_HEALTH, 14.0D).add(Attributes.MOVEMENT_SPEED, 0.3F).add(Attributes.KNOCKBACK_RESISTANCE, 0.7D).add(Attributes.ATTACK_DAMAGE, 2.0D);
    }
}
