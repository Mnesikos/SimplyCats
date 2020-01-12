package com.github.mnesikos.simplycats.entity;

import com.github.mnesikos.simplycats.Ref;
//import com.github.mnesikos.simplycats.configuration.SimplyCatsConfig;
import com.github.mnesikos.simplycats.SimplyCats;
import com.github.mnesikos.simplycats.entity.core.Genetics;
import com.github.mnesikos.simplycats.entity.core.Genetics.*;
import net.minecraft.entity.AgeableEntity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.ILivingEntityData;
import net.minecraft.entity.SpawnReason;
import net.minecraft.entity.monster.AbstractSkeletonEntity;
import net.minecraft.entity.monster.ZombieEntity;
import net.minecraft.entity.passive.TameableEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.util.*;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.IWorld;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import javax.annotation.Nullable;
import java.util.List;
import java.util.Optional;

public abstract class AbstractCat extends TameableEntity {
    private static final DataParameter<String> EYE_COLOR;
    private static final DataParameter<String> FUR_LENGTH;
    private static final DataParameter<String> EUMELANIN;
    private static final DataParameter<String> PHAEOMELANIN;
    private static final DataParameter<String> DILUTION;
    private static final DataParameter<String> DILUTE_MOD;
    private static final DataParameter<String> AGOUTI;
    private static final DataParameter<String> TABBY;
    private static final DataParameter<String> SPOTTED;
    private static final DataParameter<String> TICKED;
    private static final DataParameter<String> COLORPOINT;
    private static final DataParameter<String> WHITE;
    private static final DataParameter<String> WHITE_0;
    private static final DataParameter<String> WHITE_1;
    private static final DataParameter<String> WHITE_2;
    private static final DataParameter<String> WHITE_PAWS_0;
    private static final DataParameter<String> WHITE_PAWS_1;
    private static final DataParameter<String> WHITE_PAWS_2;
    private static final DataParameter<String> WHITE_PAWS_3;
    private final String[] whiteTexturesArray = new String[3];
    private final String[] whitePawTexturesArray = new String[4];

    private String texturePrefix;
    private final String[] catTexturesArray = new String[12];

    private static final DataParameter<Optional<BlockPos>> HOME_POSITION = EntityDataManager.createKey(AbstractCat.class, DataSerializers.OPTIONAL_BLOCK_POS);
    private boolean PURR;
    private int PURR_TIMER;

    public AbstractCat(EntityType<? extends AbstractCat> entityType, World world) {
        super(entityType, world);
    }

    @Nullable
    @Override
    public ILivingEntityData onInitialSpawn(IWorld worldIn, DifficultyInstance difficultyIn, SpawnReason reason, @Nullable ILivingEntityData spawnDataIn, @Nullable CompoundNBT dataTag) {
        this.setPhenotype();
        return super.onInitialSpawn(worldIn, difficultyIn, reason, spawnDataIn, dataTag);
    }

    @Override
    protected void registerData() {
        super.registerData();
        this.dataManager.register(EYE_COLOR, EyeColor.COPPER.toString());
        this.dataManager.register(FUR_LENGTH, "L-L");
        this.dataManager.register(EUMELANIN, "B-B");
        this.dataManager.register(PHAEOMELANIN, "Xo-Xo");
        this.dataManager.register(DILUTION, "D-D");
        this.dataManager.register(DILUTE_MOD, "dm-dm");
        this.dataManager.register(AGOUTI, "a-a");
        this.dataManager.register(TABBY, "Mc-Mc");
        this.dataManager.register(SPOTTED, "sp-sp");
        this.dataManager.register(TICKED, "ta-ta");
        this.dataManager.register(COLORPOINT, "C-C");
        this.dataManager.register(WHITE, "w-w");
        this.dataManager.register(WHITE_0, "");
        this.dataManager.register(WHITE_1, "");
        this.dataManager.register(WHITE_2, "");
        this.dataManager.register(WHITE_PAWS_0, "");
        this.dataManager.register(WHITE_PAWS_1, "");
        this.dataManager.register(WHITE_PAWS_2, "");
        this.dataManager.register(WHITE_PAWS_3, "");

        this.dataManager.register(HOME_POSITION, Optional.empty());
    }

    private void setPhenotype() {
        this.setGenotype(FUR_LENGTH, FurLength.init(this.rand) + "-" + FurLength.init(this.rand));
        this.setGenotype(EUMELANIN, Eumelanin.init(this.rand) + "-" + Eumelanin.init(this.rand));
        this.setGenotype(PHAEOMELANIN, Phaeomelanin.init(this.rand));
        this.setGenotype(DILUTION, Dilution.init(this.rand) + "-" + Dilution.init(this.rand));
        this.setGenotype(DILUTE_MOD, DiluteMod.init(this.rand) + "-" + DiluteMod.init(this.rand));
        this.setGenotype(AGOUTI, Agouti.init(this.rand) + "-" + Agouti.init(this.rand));
        this.setGenotype(TABBY, Tabby.init(this.rand) + "-" + Tabby.init(this.rand));
        this.setGenotype(SPOTTED, Spotted.init(this.rand) + "-" + Spotted.init(this.rand));
        this.setGenotype(TICKED, Ticked.init(this.rand) + "-" + Ticked.init(this.rand));
        this.setGenotype(COLORPOINT, Colorpoint.init(this.rand) + "-" + Colorpoint.init(this.rand));
        this.setGenotype(WHITE, White.init(this.rand) + "-" + White.init(this.rand));
        this.selectWhiteMarkings();
        this.setGenotype(EYE_COLOR, selectEyeColor());
        this.resetTexturePrefix();
    }

    private String selectEyeColor() {
        String color = EyeColor.init(rand.nextInt(4));
        // todo change whiteCheck == White.Spotting to a better check for proper white face check
        if (getGenotype(WHITE).contains(White.DOMINANT.getAllele()))
            color = EyeColor.init(rand.nextInt(5));
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
            case "Wd-Wd": case "Wd-w": case "Wd-Ws":
            case "w-Wd": case "Ws-Wd":
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
                base = rand.nextInt(2) + 4; //4-5
                if (base == 5) {
                    body = rand.nextInt(4) + 1;
                    face = rand.nextInt(6) + 1;
                    if (body > 1)
                        tail = rand.nextInt(3) + 1;
                }
                else if (base == 4) {
                    body = 1;
                    face = rand.nextInt(5) + 1;
                }
                if (rand.nextInt(10) == 0) { //10% chance for solid white
                    base = 6;
                    body = 1;
                    face = 0;
                    tail = 0;
                }
                break;

            case "Ws-w": case "w-Ws":
                base = rand.nextInt(3) + 1; //1-3
                body = 1;
                if (base == 2 || base == 3)
                    this.selectWhitePaws(base);
                if (base == 3)
                    face = rand.nextInt(5) + 1;
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
        boolean all = rand.nextInt(4) <= 2;

        if (all || rand.nextInt(4) <= 2) {
            this.whitePawTexturesArray[0] = "white_" + base + "_paw1";
            this.setWhitePawTextures(0, whitePawTexturesArray[0]);
        }

        if (all || rand.nextInt(4) <= 2) {
            this.whitePawTexturesArray[1] = "white_" + base + "_paw2";
            this.setWhitePawTextures(1, whitePawTexturesArray[1]);
        }

        if (all || rand.nextInt(4) <= 2) {
            this.whitePawTexturesArray[2] = "white_" + base + "_paw3";
            this.setWhitePawTextures(2, whitePawTexturesArray[2]);
        }

        if (all || rand.nextInt(4) <= 2) {
            this.whitePawTexturesArray[3] = "white_" + base + "_paw4";
            this.setWhitePawTextures(3, whitePawTexturesArray[3]);
        }
    }

    private String getWhiteTextures(int i) {
        switch (i) {
            case 0:
                return this.dataManager.get(WHITE_0);
            case 1:
                return this.dataManager.get(WHITE_1);
            case 2:
                return this.dataManager.get(WHITE_2);
            default:
                return "";
        }
    }

    private void setWhiteTextures(int i, String value) {
        switch (i) {
            case 0:
                this.dataManager.set(WHITE_0, value);
                break;
            case 1:
                this.dataManager.set(WHITE_1, value);
                break;
            case 2:
                this.dataManager.set(WHITE_2, value);
                break;
        }
    }

    private String getWhitePawTextures(int i) {
        switch (i) {
            case 0:
                return this.dataManager.get(WHITE_PAWS_0);
            case 1:
                return this.dataManager.get(WHITE_PAWS_1);
            case 2:
                return this.dataManager.get(WHITE_PAWS_2);
            case 3:
                return this.dataManager.get(WHITE_PAWS_3);
            default:
                return "";
        }
    }

    private void setWhitePawTextures(int i, String value) {
        switch (i) {
            case 0:
                this.dataManager.set(WHITE_PAWS_0, value);
                break;
            case 1:
                this.dataManager.set(WHITE_PAWS_1, value);
                break;
            case 2:
                this.dataManager.set(WHITE_PAWS_2, value);
                break;
            case 3:
                this.dataManager.set(WHITE_PAWS_3, value);
                break;
        }
    }

    private String getGenotype(DataParameter<String> parameter) {
        return this.dataManager.get(parameter);
    }

    void setGenotype(DataParameter<String> parameter, String value) {
        this.dataManager.set(parameter, value);
    }

    public String getSex() {
        return this.dataManager.get(PHAEOMELANIN).contains(Phaeomelanin.MALE.getAllele()) ? Sex.MALE.getName() : Sex.FEMALE.getName();
    }

    public boolean hasHomePos() {
        return this.dataManager.get(HOME_POSITION).isPresent();
    }

    public BlockPos getHomePos() {
        return this.dataManager.get(HOME_POSITION).orElse(this.getPosition());
    }

    public void setHomePos(BlockPos position) {
        this.dataManager.set(HOME_POSITION, Optional.of(position));
    }

    public void resetHomePos() {
        this.dataManager.set(HOME_POSITION, Optional.empty());
    }

    @Override
    public void writeAdditional(CompoundNBT compound) {
        super.writeAdditional(compound);
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
        for (int i = 0; i <= 2; i++)
            compound.putString("White_" + i, this.getWhiteTextures(i));
        for (int i = 0; i <= 3; i++)
            compound.putString("WhitePaws_" + i, this.getWhitePawTextures(i));
        if (this.hasHomePos()) {
            compound.putInt("HomePosX", this.getHomePos().getX());
            compound.putInt("HomePosY", this.getHomePos().getY());
            compound.putInt("HomePosZ", this.getHomePos().getZ());
        }
    }

    @Override
    public void readAdditional(CompoundNBT compound) {
        super.readAdditional(compound);
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
        for (int i = 0; i <= 2; i++)
            this.setWhiteTextures(i, compound.getString("White_" + i));
        for (int i = 0; i <= 3; i++)
            this.setWhitePawTextures(i, compound.getString("WhitePaws_" + i));
        if (compound.contains("HomePosX"))
            this.setHomePos(new BlockPos(compound.getInt("HomePosX"), compound.getInt("HomePosY"), compound.getInt("HomePosZ")));
    }

    private String getPhenotype(DataParameter<String> dataParameter) {
        if (dataParameter == FUR_LENGTH)
            return Genetics.FurLength.getPhenotype(this.getGenotype(dataParameter));
        else if (dataParameter == EUMELANIN)
            return Genetics.Eumelanin.getPhenotype(this.getGenotype(dataParameter));
        else if (dataParameter == PHAEOMELANIN)
            return Genetics.Phaeomelanin.getPhenotype(this.getGenotype(dataParameter));
        else if (dataParameter == DILUTION)
            return Genetics.Dilution.getPhenotype(this.getGenotype(dataParameter));
        else if (dataParameter == DILUTE_MOD)
            return Genetics.DiluteMod.getPhenotype(this.getGenotype(dataParameter));
        else if (dataParameter == AGOUTI)
            return Genetics.Agouti.getPhenotype(this.getGenotype(dataParameter));
        else if (dataParameter == TABBY)
            return Genetics.Tabby.getPhenotype(this.getGenotype(dataParameter));
        else if (dataParameter == SPOTTED)
            return Genetics.Spotted.getPhenotype(this.getGenotype(dataParameter));
        else if (dataParameter == TICKED)
            return Genetics.Ticked.getPhenotype(this.getGenotype(dataParameter));
        else if (dataParameter == COLORPOINT)
            return Genetics.Colorpoint.getPhenotype(this.getGenotype(dataParameter));
        else if (dataParameter == WHITE)
            return Genetics.White.getPhenotype(this.getGenotype(dataParameter));
        else // EYES
            return this.getGenotype(EYE_COLOR);
    }

    /*public boolean canWander() {
        if (this.hasHomePos())
            return this.getDistanceSq(this.getHomePos()) < SimplyCatsConfig.WANDER_AREA_LIMIT;
        else
            return true;
    }*/

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
            /*if (this.getPhenotype(DILUTE_MOD).equalsIgnoreCase(DiluteMod.CARAMELIZED.toString().toLowerCase()))
                solid = solid + "_" + this.getPhenotype(DILUTE_MOD);*/
        }

        String tabby = this.getPhenotype(TABBY) + "_" + solid;
        if (this.getGenotype(SPOTTED).contains(Spotted.SPOTTED.getAllele()))
            tabby = this.getPhenotype(SPOTTED) + "_" + tabby;
        if (this.getPhenotype(TICKED).equalsIgnoreCase(Ticked.TICKED.toString().toLowerCase()))
            tabby = this.getPhenotype(TICKED) + "_" + solid;

        String tortie = "";
        if (this.getPhenotype(PHAEOMELANIN).equalsIgnoreCase(Phaeomelanin.TORTOISESHELL.toString().toLowerCase())) {
            tortie = this.getPhenotype(PHAEOMELANIN) + "_" + (tabby.replace(("_" + solid), ""));
            if (this.getPhenotype(DILUTION).equalsIgnoreCase(Dilution.DILUTE.toString().toLowerCase())) {
                tortie = tortie + "_" + this.getPhenotype(DILUTION);
                /*if (this.getPhenotype(DILUTE_MOD).equalsIgnoreCase(DiluteMod.CARAMELIZED.toString().toLowerCase()))
                    tortie = tortie + "_" + this.getPhenotype(DILUTE_MOD);*/
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

        this.catTexturesArray[0] = Ref.MODID + ":textures/entity/cat/solid/" + solid + ".png";
        this.catTexturesArray[1] = tabby.equals("") ? null : (Ref.MODID + ":textures/entity/cat/tabby/" + tabby + ".png");
        this.catTexturesArray[2] = tortie.equals("") ? null : (Ref.MODID + ":textures/entity/cat/tortie/" + tortie + ".png");
        this.catTexturesArray[3] = colorpoint.equals("") ? null : (Ref.MODID + ":textures/entity/cat/colorpoint/" + colorpoint + ".png");
        this.catTexturesArray[4] = this.getWhiteTextures(0).equals("") ? null : (Ref.MODID + ":textures/entity/cat/white/new/" + this.getWhiteTextures(0) + ".png");
        this.catTexturesArray[5] = this.getWhiteTextures(1).equals("") ? null : (Ref.MODID + ":textures/entity/cat/white/new/" + this.getWhiteTextures(1) + ".png");
        this.catTexturesArray[6] = this.getWhiteTextures(2).equals("") ? null : (Ref.MODID + ":textures/entity/cat/white/new/" + this.getWhiteTextures(2) + ".png");
        this.catTexturesArray[7] = this.getWhitePawTextures(0).equals("") ? null : (Ref.MODID + ":textures/entity/cat/white/new/" + this.getWhitePawTextures(0) + ".png");
        this.catTexturesArray[8] = this.getWhitePawTextures(1).equals("") ? null : (Ref.MODID + ":textures/entity/cat/white/new/" + this.getWhitePawTextures(1) + ".png");
        this.catTexturesArray[9] = this.getWhitePawTextures(2).equals("") ? null : (Ref.MODID + ":textures/entity/cat/white/new/" + this.getWhitePawTextures(2) + ".png");
        this.catTexturesArray[10] = this.getWhitePawTextures(3).equals("") ? null : (Ref.MODID + ":textures/entity/cat/white/new/" + this.getWhitePawTextures(3) + ".png");
        this.catTexturesArray[11] = Ref.MODID + ":textures/entity/cat/eyes/" + this.getPhenotype(EYE_COLOR) + ".png";
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

    @Override
    public void tick() {
        super.tick();

        if (this.PURR) {
            if (PURR_TIMER == 0) {
                this.PURR = false;
                this.PURR_TIMER = 0;
            }
        }

        if (this.getAttackTarget() == null) {
            List<ZombieEntity> zombies = this.world.getEntitiesWithinAABB(ZombieEntity.class, this.getBoundingBox().grow(4.0D, 3.0D, 4.0D));
            List<AbstractSkeletonEntity> skeletons = this.world.getEntitiesWithinAABB(AbstractSkeletonEntity.class, this.getBoundingBox().grow(4.0D, 3.0D, 4.0D));
            if ((!zombies.isEmpty() || !skeletons.isEmpty()) && this.rand.nextInt(400) == 0) {
                this.playSound(SoundEvents.ENTITY_CAT_HISS, this.getSoundVolume() / 2.0F, this.getSoundPitch());
            }
        }

        if (this.world.isRemote && this.dataManager.isDirty()) {
            this.dataManager.setClean();
            this.resetTexturePrefix();
        }
    }

    @Override
    public void livingTick() {
        super.livingTick();

        if (this.PURR && PURR_TIMER > 0) {
            --PURR_TIMER;
        }
    }

    @Override
    public boolean processInteract(PlayerEntity player, Hand hand) {
        ItemStack stack = player.getHeldItem(hand);
        if (!stack.isEmpty()) {
            if (stack.getItem() == Items.STRING && player.isSneaking()) {
                if (this.world.isRemote) {
                    player.sendMessage(new StringTextComponent(this.getGenotype(EYE_COLOR)));
                    player.sendMessage(new StringTextComponent(this.getGenotype(FUR_LENGTH) + ": " + getPhenotype(FUR_LENGTH)));
                    player.sendMessage(new StringTextComponent(this.getGenotype(EUMELANIN) + ": " + getPhenotype(EUMELANIN)));
                    player.sendMessage(new StringTextComponent(this.getGenotype(PHAEOMELANIN) + ": " + getPhenotype(PHAEOMELANIN)));
                    player.sendMessage(new StringTextComponent(this.getGenotype(DILUTION) + ": " + getPhenotype(DILUTION)));
                    player.sendMessage(new StringTextComponent(this.getGenotype(DILUTE_MOD) + ": " + getPhenotype(DILUTE_MOD)));
                    player.sendMessage(new StringTextComponent(this.getGenotype(AGOUTI) + ": " + getPhenotype(AGOUTI)));
                    player.sendMessage(new StringTextComponent(this.getGenotype(TABBY) + ": " + getPhenotype(TABBY)));
                    player.sendMessage(new StringTextComponent(this.getGenotype(SPOTTED) + ": " + getPhenotype(SPOTTED)));
                    player.sendMessage(new StringTextComponent(this.getGenotype(TICKED) + ": " + getPhenotype(TICKED)));
                    player.sendMessage(new StringTextComponent(this.getGenotype(COLORPOINT) + ": " + getPhenotype(COLORPOINT)));
                    player.sendMessage(new StringTextComponent(this.getGenotype(WHITE) + ": " + getPhenotype(WHITE)));
                    player.sendMessage(new StringTextComponent(this.getWhiteTextures(0) + ", " + this.getWhiteTextures(1)
                            + ", " + this.getWhiteTextures(2)));
                    player.sendMessage(new StringTextComponent(this.getWhitePawTextures(0) + ", " + this.getWhitePawTextures(1)
                            + ", " + this.getWhitePawTextures(2) + ", " + this.getWhitePawTextures(3)));
                }
                return true;
            }
        }

        if (!this.PURR && this.rand.nextInt(10) == 0) { // 1/10th chance an interaction will result in purrs
            this.PURR = true;
            this.PURR_TIMER = (this.rand.nextInt(61) + 30) * 20; // random range of 600 to 1800 ticks (0.5 to 1.5 IRL minutes)
        }

        return false;
    }

    @Nullable
    @Override
    public AgeableEntity createChild(AgeableEntity parFather) {
        EntityDataManager father = parFather.getDataManager();
        EntityDataManager mother = this.getDataManager();
        EntityCat child = new EntityCat(SimplyCats.CAT, this.world);

        String[] matFur = mother.get(FUR_LENGTH).split("-");
        String[] patFur = father.get(FUR_LENGTH).split("-");
        String fur = matFur[rand.nextInt(2)] + "-" + patFur[rand.nextInt(2)];

        String[] matEum = mother.get(EUMELANIN).split("-");
        String[] patEum = father.get(EUMELANIN).split("-");
        String eum = matEum[rand.nextInt(2)] + "-" + patEum[rand.nextInt(2)];

        String[] matPhae = mother.get(PHAEOMELANIN).split("-");
        String[] patPhae = father.get(PHAEOMELANIN).split("-");
        String phae = matPhae[rand.nextInt(2)] + "-" + patPhae[rand.nextInt(2)];

        String[] matDil = mother.get(DILUTION).split("-");
        String[] patDil = father.get(DILUTION).split("-");
        String dil = matDil[rand.nextInt(2)] + "-" + patDil[rand.nextInt(2)];

        String[] matDilm = mother.get(DILUTE_MOD).split("-");
        String[] patDilm = father.get(DILUTE_MOD).split("-");
        String dilm = matDilm[rand.nextInt(2)] + "-" + patDilm[rand.nextInt(2)];

        String[] matAgo = mother.get(AGOUTI).split("-");
        String[] patAgo = father.get(AGOUTI).split("-");
        String ago = matAgo[rand.nextInt(2)] + "-" + patAgo[rand.nextInt(2)];

        String[] matTab = mother.get(TABBY).split("-");
        String[] patTab = father.get(TABBY).split("-");
        String tab = matTab[rand.nextInt(2)] + "-" + patTab[rand.nextInt(2)];

        String[] matSpot = mother.get(SPOTTED).split("-");
        String[] patSpot = father.get(SPOTTED).split("-");
        String spot = matSpot[rand.nextInt(2)] + "-" + patSpot[rand.nextInt(2)];

        String[] matTick = mother.get(TICKED).split("-");
        String[] patTick = father.get(TICKED).split("-");
        String tick = matTick[rand.nextInt(2)] + "-" + patTick[rand.nextInt(2)];

        String[] matPoint = mother.get(COLORPOINT).split("-");
        String[] patPoint = father.get(COLORPOINT).split("-");
        String point = matPoint[rand.nextInt(2)] + "-" + patPoint[rand.nextInt(2)];

        String[] matWhite = mother.get(WHITE).split("-");
        String[] patWhite = father.get(WHITE).split("-");
        String white = matWhite[rand.nextInt(2)] + "-" + patWhite[rand.nextInt(2)];

        child.setGenotype(FUR_LENGTH, fur);
        child.setGenotype(EUMELANIN, eum);
        child.setGenotype(PHAEOMELANIN, phae);
        child.setGenotype(DILUTION, dil);
        child.setGenotype(DILUTE_MOD, dilm);
        child.setGenotype(AGOUTI, ago);
        child.setGenotype(TABBY, tab);
        child.setGenotype(SPOTTED, spot);
        child.setGenotype(TICKED, tick);
        child.setGenotype(COLORPOINT, point);
        child.setGenotype(WHITE, white);
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
        if (white.contains(White.DOMINANT.getAllele()))
            eyesMax = 4;
        else
            eyesMax = eyesMax >= 4 ? (eyesMin < 3 ? eyesMin+1 : 3) : eyesMax;
        int eyes = rand.nextInt((eyesMax - eyesMin) + 1) + eyesMin;
        String eye = EyeColor.init(matEye == 4 && patEye == 4 ? (eyesMax == 4 ? 4 : rand.nextInt(4)) : eyes);
        if (point.contentEquals(Colorpoint.COLORPOINT.getAllele() + "-" + Colorpoint.COLORPOINT.getAllele()))
            eye = EyeColor.init(4);

        child.setGenotype(EYE_COLOR, eye);

        child.setTamed(this.isTamed());
        if (this.isTamed()) {
            child.setOwnerId(this.getOwnerId());
            if (this.hasHomePos())
                child.setHomePos(this.getHomePos());
        }

        return child;
    }

    @Override
    public void fall(float distance, float damageMultiplier) {
    }

    @Override
    protected boolean canTriggerWalking() {
        return false;
    }

    public boolean isAngry() {
        return ((this.dataManager.get(TAMED)) & 2) != 0;
    }

    public void setAngry(boolean angry) {
        byte b0 = this.dataManager.get(TAMED);

        if (angry)
            this.dataManager.set(TAMED, (byte)(b0 | 2));
        else
            this.dataManager.set(TAMED, (byte)(b0 & -3));
    }

    @Override
    protected SoundEvent getAmbientSound() {
        if (this.isAngry()) {
            if (this.rand.nextInt(10) == 0)
                return SoundEvents.ENTITY_CAT_HISS;
            else
                return null;
        } else if (this.isInLove() || this.PURR) {
            return SoundEvents.ENTITY_CAT_PURR;
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
    public ITextComponent getName() {
        if (this.hasCustomName())
            return this.getCustomName();
        else
            return this.isTamed() ? new TranslationTextComponent(this.getType().getName().getString()) : super.getName();
    }

    @Nullable
    @Override
    protected ResourceLocation getLootTable() {
        return null;
    }

    static {
        EYE_COLOR = EntityDataManager.createKey(AbstractCat.class, DataSerializers.STRING);
        FUR_LENGTH = EntityDataManager.createKey(AbstractCat.class, DataSerializers.STRING);
        EUMELANIN = EntityDataManager.createKey(AbstractCat.class, DataSerializers.STRING);
        PHAEOMELANIN = EntityDataManager.createKey(AbstractCat.class, DataSerializers.STRING);
        DILUTION = EntityDataManager.createKey(AbstractCat.class, DataSerializers.STRING);
        DILUTE_MOD = EntityDataManager.createKey(AbstractCat.class, DataSerializers.STRING);
        AGOUTI = EntityDataManager.createKey(AbstractCat.class, DataSerializers.STRING);
        TABBY = EntityDataManager.createKey(AbstractCat.class, DataSerializers.STRING);
        SPOTTED = EntityDataManager.createKey(AbstractCat.class, DataSerializers.STRING);
        TICKED = EntityDataManager.createKey(AbstractCat.class, DataSerializers.STRING);
        COLORPOINT = EntityDataManager.createKey(AbstractCat.class, DataSerializers.STRING);
        WHITE = EntityDataManager.createKey(AbstractCat.class, DataSerializers.STRING);
        WHITE_0 = EntityDataManager.createKey(AbstractCat.class, DataSerializers.STRING);
        WHITE_1 = EntityDataManager.createKey(AbstractCat.class, DataSerializers.STRING);
        WHITE_2 = EntityDataManager.createKey(AbstractCat.class, DataSerializers.STRING);
        WHITE_PAWS_0 = EntityDataManager.createKey(AbstractCat.class, DataSerializers.STRING);
        WHITE_PAWS_1 = EntityDataManager.createKey(AbstractCat.class, DataSerializers.STRING);
        WHITE_PAWS_2 = EntityDataManager.createKey(AbstractCat.class, DataSerializers.STRING);
        WHITE_PAWS_3 = EntityDataManager.createKey(AbstractCat.class, DataSerializers.STRING);
    }
}
