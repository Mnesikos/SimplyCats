package com.github.mnesikos.simplycats.entity;

import com.github.mnesikos.simplycats.Ref;
import com.github.mnesikos.simplycats.configuration.SimplyCatsConfig;
import com.github.mnesikos.simplycats.entity.core.Genetics;
import com.github.mnesikos.simplycats.entity.core.Genetics.*;
import com.google.common.base.Optional;
import net.minecraft.entity.EntityAgeable;
import net.minecraft.entity.IEntityLivingData;
import net.minecraft.entity.monster.AbstractSkeleton;
import net.minecraft.entity.monster.EntityZombie;
import net.minecraft.entity.passive.EntityTameable;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EnumHand;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nullable;
import java.util.List;

public abstract class AbstractCat extends EntityTameable {
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

    private static final DataParameter<Optional<BlockPos>> HOME_POSITION;
    private boolean PURR;
    private int PURR_TIMER;

    public AbstractCat(World world) {
        super(world);
        setPhenotype();
    }

    @Nullable
    @Override
    public IEntityLivingData onInitialSpawn(DifficultyInstance difficulty, @Nullable IEntityLivingData livingdata) {
        //this.setPhenotype();
        return super.onInitialSpawn(difficulty, livingdata);
    }

    @Override
    protected void entityInit() {
        super.entityInit();
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

        this.dataManager.register(HOME_POSITION, Optional.absent());
    }

    private void setPhenotype() {
        this.setGenotype(FUR_LENGTH, FurLength.init() + "-" + FurLength.init());
        this.setGenotype(EUMELANIN, Eumelanin.init() + "-" + Eumelanin.init());
        this.setGenotype(PHAEOMELANIN, Phaeomelanin.init());
        this.setGenotype(DILUTION, Dilution.init() + "-" + Dilution.init());
        this.setGenotype(DILUTE_MOD, DiluteMod.init() + "-" + DiluteMod.init());
        this.setGenotype(AGOUTI, Agouti.init() + "-" + Agouti.init());
        this.setGenotype(TABBY, Tabby.init() + "-" + Tabby.init());
        this.setGenotype(SPOTTED, Spotted.init() + "-" + Spotted.init());
        this.setGenotype(TICKED, Ticked.init() + "-" + Ticked.init());
        this.setGenotype(COLORPOINT, Colorpoint.init() + "-" + Colorpoint.init());
        this.setGenotype(WHITE, White.init() + "-" + White.init());
        this.selectWhiteMarkings();
        this.setGenotype(EYE_COLOR, selectEyeColor());
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
        return this.dataManager.get(HOME_POSITION).or(this.getPosition());
    }

    public void setHomePos(BlockPos position) {
        this.dataManager.set(HOME_POSITION, Optional.of(position));
    }

    public void resetHomePos() {
        this.dataManager.set(HOME_POSITION, Optional.absent());
    }

    @Override
    public void writeEntityToNBT(NBTTagCompound compound) {
        super.writeEntityToNBT(compound);
        compound.setString("EyeColor", this.getGenotype(EYE_COLOR));
        compound.setString("FurLength", this.getGenotype(FUR_LENGTH));
        compound.setString("Eumelanin", this.getGenotype(EUMELANIN));
        compound.setString("Phaeomelanin", this.getGenotype(PHAEOMELANIN));
        compound.setString("Dilution", this.getGenotype(DILUTION));
        compound.setString("DiluteMod", this.getGenotype(DILUTE_MOD));
        compound.setString("Agouti", this.getGenotype(AGOUTI));
        compound.setString("Tabby", this.getGenotype(TABBY));
        compound.setString("Spotted", this.getGenotype(SPOTTED));
        compound.setString("Ticked", this.getGenotype(TICKED));
        compound.setString("Colorpoint", this.getGenotype(COLORPOINT));
        compound.setString("White", this.getGenotype(WHITE));
        for (int i = 0; i <= 2; i++)
            compound.setString("White_" + i, this.getWhiteTextures(i));
        for (int i = 0; i <= 3; i++)
            compound.setString("WhitePaws_" + i, this.getWhitePawTextures(i));
        if (this.hasHomePos()) {
            compound.setInteger("HomePosX", this.getHomePos().getX());
            compound.setInteger("HomePosY", this.getHomePos().getY());
            compound.setInteger("HomePosZ", this.getHomePos().getZ());
        }
    }

    @Override
    public void readEntityFromNBT(NBTTagCompound compound) {
        super.readEntityFromNBT(compound);
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
        if (compound.hasKey("HomePosX"))
            this.setHomePos(new BlockPos(compound.getInteger("HomePosX"), compound.getInteger("HomePosY"), compound.getInteger("HomePosZ")));
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

    public boolean canWander() {
        if (this.hasHomePos())
            return this.getDistanceSq(this.getHomePos()) < SimplyCatsConfig.WANDER_AREA_LIMIT;
        else
            return true;
    }

    private void resetTexturePrefix() {
        this.texturePrefix = null;
    }

    @SideOnly(Side.CLIENT)
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

    @SideOnly(Side.CLIENT)
    public String getCatTexture() {
        if (this.texturePrefix == null)
            this.setCatTexturePaths();

        return this.texturePrefix;
    }

    @SideOnly(Side.CLIENT)
    public String[] getTexturePaths() {
        if (this.texturePrefix == null)
            this.setCatTexturePaths();

        return this.catTexturesArray;
    }

    @Override
    public void onUpdate() {
        super.onUpdate();

        if (this.PURR) {
            if (PURR_TIMER == 0) {
                this.PURR = false;
                this.PURR_TIMER = 0;
            }
        }

        if (this.getAttackTarget() == null) {
            List<EntityZombie> zombies = this.world.getEntitiesWithinAABB(EntityZombie.class, this.getEntityBoundingBox().grow(4.0D, 3.0D, 4.0D));
            List<AbstractSkeleton> skeletons = this.world.getEntitiesWithinAABB(AbstractSkeleton.class, this.getEntityBoundingBox().grow(4.0D, 3.0D, 4.0D));
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
    public void onLivingUpdate() {
        super.onLivingUpdate();

        if (this.PURR && PURR_TIMER > 0) {
            --PURR_TIMER;
        }
    }

    @Override
    public boolean processInteract(EntityPlayer player, EnumHand hand) {
        ItemStack stack = player.getHeldItem(hand);
        if (!stack.isEmpty()) {
            if (stack.getItem() == Items.STRING && player.isSneaking()) {
                if (this.world.isRemote) {
                    player.sendMessage(new TextComponentString(this.getGenotype(EYE_COLOR)));
                    player.sendMessage(new TextComponentString(this.getGenotype(FUR_LENGTH) + ": " + getPhenotype(FUR_LENGTH)));
                    player.sendMessage(new TextComponentString(this.getGenotype(EUMELANIN) + ": " + getPhenotype(EUMELANIN)));
                    player.sendMessage(new TextComponentString(this.getGenotype(PHAEOMELANIN) + ": " + getPhenotype(PHAEOMELANIN)));
                    player.sendMessage(new TextComponentString(this.getGenotype(DILUTION) + ": " + getPhenotype(DILUTION)));
                    player.sendMessage(new TextComponentString(this.getGenotype(DILUTE_MOD) + ": " + getPhenotype(DILUTE_MOD)));
                    player.sendMessage(new TextComponentString(this.getGenotype(AGOUTI) + ": " + getPhenotype(AGOUTI)));
                    player.sendMessage(new TextComponentString(this.getGenotype(TABBY) + ": " + getPhenotype(TABBY)));
                    player.sendMessage(new TextComponentString(this.getGenotype(SPOTTED) + ": " + getPhenotype(SPOTTED)));
                    player.sendMessage(new TextComponentString(this.getGenotype(TICKED) + ": " + getPhenotype(TICKED)));
                    player.sendMessage(new TextComponentString(this.getGenotype(COLORPOINT) + ": " + getPhenotype(COLORPOINT)));
                    player.sendMessage(new TextComponentString(this.getGenotype(WHITE) + ": " + getPhenotype(WHITE)));
                    player.sendMessage(new TextComponentString(this.getWhiteTextures(0) + ", " + this.getWhiteTextures(1)
                            + ", " + this.getWhiteTextures(2)));
                    player.sendMessage(new TextComponentString(this.getWhitePawTextures(0) + ", " + this.getWhitePawTextures(1)
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
    public EntityAgeable createChild(EntityAgeable parFather) {
        EntityDataManager father = parFather.getDataManager();
        EntityDataManager mother = this.getDataManager();
        EntityCat child = new EntityCat(this.world);

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
    public String getName() {
        if (this.hasCustomName())
            return this.getCustomNameTag();
        else
            return this.isTamed() ? new TextComponentTranslation("entity.Cat.name").getFormattedText() : super.getName();
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

        HOME_POSITION = EntityDataManager.createKey(AbstractCat.class, DataSerializers.OPTIONAL_BLOCK_POS);
    }
}
