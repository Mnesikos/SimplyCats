package com.github.mnesikos.simplycats.entity;

import com.github.mnesikos.simplycats.Ref;
import com.github.mnesikos.simplycats.configuration.SimplyCatsConfig;
import com.github.mnesikos.simplycats.entity.core.Genetics;
import com.github.mnesikos.simplycats.entity.core.Genetics.*;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.entity.DataWatcher;
import net.minecraft.entity.EntityAgeable;
import net.minecraft.entity.IEntityLivingData;
import net.minecraft.entity.monster.EntitySkeleton;
import net.minecraft.entity.monster.EntityZombie;
import net.minecraft.entity.passive.EntityTameable;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.StatCollector;
import net.minecraft.world.World;

import javax.annotation.Nullable;
import java.util.List;

public abstract class AbstractCat extends EntityTameable {
    private static final int EYE_COLOR = 18;
    private static final int FUR_LENGTH = 19;
    private static final int EUMELANIN = 20;
    private static final int PHAEOMELANIN = 21;
    private static final int DILUTION = 22;
    private static final int DILUTE_MOD = 23;
    private static final int AGOUTI = 24;
    private static final int TABBY = 25;
    private static final int SPOTTED = 26;
    private static final int TICKED = 27;
    private static final int COLORPOINT = 28;
    private static final int WHITE = 29;
    private static final String WHITE_0 = "White_0";
    private static final String WHITE_1 = "White_1";
    private static final String WHITE_2 = "White_2";
    private static final String WHITE_PAWS_0 = "WhitePaws_0";
    private static final String WHITE_PAWS_1 = "WhitePaws_1";
    private static final String WHITE_PAWS_2 = "WhitePaws_2";
    private static final String WHITE_PAWS_3 = "WhitePaws_3";
    private final String[] whiteTexturesArray = new String[3];
    private final String[] whitePawTexturesArray = new String[4];

    private String texturePrefix;
    private final String[] catTexturesArray = new String[12];

    private static final String HOME_POSITION_X = "HomePosX";
    private static final String HOME_POSITION_Y = "HomePosY";
    private static final String HOME_POSITION_Z = "HomePosZ";
    private static final int HAS_HOME_POSITION = 30;
    private boolean PURR;
    private int PURR_TIMER;

    public AbstractCat(World world) {
        super(world);
        setPhenotype();
    }

    @Override
    public IEntityLivingData onSpawnWithEgg(IEntityLivingData p_110161_1_) {
        this.setPhenotype();
        return super.onSpawnWithEgg(p_110161_1_);
    }

    @Override
    protected void entityInit() { // VANILLA: 6-12, 16-17; THIS: 18-30
        super.entityInit();
        this.dataWatcher.addObject(EYE_COLOR, EyeColor.COPPER.toString());
        this.dataWatcher.addObject(FUR_LENGTH, "L-L");
        this.dataWatcher.addObject(EUMELANIN, "B-B");
        this.dataWatcher.addObject(PHAEOMELANIN, "Xo-Xo");
        this.dataWatcher.addObject(DILUTION, "D-D");
        this.dataWatcher.addObject(DILUTE_MOD, "dm-dm");
        this.dataWatcher.addObject(AGOUTI, "a-a");
        this.dataWatcher.addObject(TABBY, "Mc-Mc");
        this.dataWatcher.addObject(SPOTTED, "sp-sp");
        this.dataWatcher.addObject(TICKED, "ta-ta");
        this.dataWatcher.addObject(COLORPOINT, "C-C");
        this.dataWatcher.addObject(WHITE, "w-w");
        /*this.dataWatcher.addObject(WHITE_0, "");
        this.dataWatcher.addObject(WHITE_1, "");
        this.dataWatcher.addObject(WHITE_2, "");
        this.dataWatcher.addObject(WHITE_PAWS_0, "");
        this.dataWatcher.addObject(WHITE_PAWS_1, "");
        this.dataWatcher.addObject(WHITE_PAWS_2, "");
        this.dataWatcher.addObject(WHITE_PAWS_3, "");*/

        /*this.dataWatcher.addObject(HOME_POSITION_X, Optional.absent());
        this.dataWatcher.addObject(HOME_POSITION_Y, Optional.absent());
        this.dataWatcher.addObject(HOME_POSITION_Z, Optional.absent());*/
        this.dataWatcher.addObject(HAS_HOME_POSITION, 0);
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

        if (this.getGenotype(WHITE).equals("Wd-Wd") || this.getGenotype(WHITE).equals("Wd-w") || this.getGenotype(WHITE).equals("Wd-Ws") ||
                this.getGenotype(WHITE).equals("w-Wd") || this.getGenotype(WHITE).equals("Ws-Wd")) {
            base = 6;
            body = 1;
            face = 0;
            tail = 0;

        } else if (this.getGenotype(WHITE).equals("w-w")) {
            base = 0;
            body = 0;
            face = 0;
            tail = 0;

        } else if (this.getGenotype(WHITE).equals("Ws-Ws")) {
            base = rand.nextInt(2) + 4; //4-5
            if (base == 5) {
                body = rand.nextInt(4) + 1;
                face = rand.nextInt(6) + 1;
                if (body > 1)
                    tail = rand.nextInt(3) + 1;
            } else if (base == 4) {
                body = 1;
                face = rand.nextInt(5) + 1;
            }
            if (rand.nextInt(10) == 0) { //10% chance for solid white
                base = 6;
                body = 1;
                face = 0;
                tail = 0;
            }

        } else if (this.getGenotype(WHITE).equals("Ws-w") || this.getGenotype(WHITE).equals("w-Ws")) {
            base = rand.nextInt(3) + 1; //1-3
            body = 1;
            if (base == 2 || base == 3)
                this.selectWhitePaws(base);
            if (base == 3)
                face = rand.nextInt(5) + 1;

        } else {
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
                return this.getEntityData().getString(WHITE_0);
            case 1:
                return this.getEntityData().getString(WHITE_1);
            case 2:
                return this.getEntityData().getString(WHITE_2);
            default:
                return "";
        }
    }

    private void setWhiteTextures(int i, String value) {
        switch (i) {
            case 0:
                this.getEntityData().setString(WHITE_0, value);
                break;
            case 1:
                this.getEntityData().setString(WHITE_1, value);
                break;
            case 2:
                this.getEntityData().setString(WHITE_2, value);
                break;
        }
    }

    private String getWhitePawTextures(int i) {
        switch (i) {
            case 0:
                return this.getEntityData().getString(WHITE_PAWS_0);
            case 1:
                return this.getEntityData().getString(WHITE_PAWS_1);
            case 2:
                return this.getEntityData().getString(WHITE_PAWS_2);
            case 3:
                return this.getEntityData().getString(WHITE_PAWS_3);
            default:
                return "";
        }
    }

    private void setWhitePawTextures(int i, String value) {
        switch (i) {
            case 0:
                this.getEntityData().setString(WHITE_PAWS_0, value);
                break;
            case 1:
                this.getEntityData().setString(WHITE_PAWS_1, value);
                break;
            case 2:
                this.getEntityData().setString(WHITE_PAWS_2, value);
                break;
            case 3:
                this.getEntityData().setString(WHITE_PAWS_3, value);
                break;
        }
    }

    private String getGenotype(int parameter) {
        return this.dataWatcher.getWatchableObjectString(parameter);
    }

    void setGenotype(int parameter, String value) {
        this.dataWatcher.updateObject(parameter, value);
    }

    public String getSex() {
        return this.dataWatcher.getWatchableObjectString(PHAEOMELANIN).contains(Phaeomelanin.MALE.getAllele()) ? Sex.MALE.getName() : Sex.FEMALE.getName();
    }

    public boolean hasHomePos() {
        return this.dataWatcher.getWatchableObjectInt(HAS_HOME_POSITION) != 0; // 0 == no home position
    }

    public int getHomePosX() {
        return this.hasHomePos() ? this.getEntityData().getInteger(HOME_POSITION_X) : (int) this.posX;
    }

    public int getHomePosY() {
        return this.hasHomePos() ? this.getEntityData().getInteger(HOME_POSITION_Y) : (int) this.posY;
    }

    public int getHomePosZ() {
        return this.hasHomePos() ? this.getEntityData().getInteger(HOME_POSITION_Z) : (int) this.posZ;
    }

    public void setHomePos(int x, int y, int z) {
        this.dataWatcher.updateObject(HAS_HOME_POSITION, 1);
        this.getEntityData().setInteger(HOME_POSITION_X, x);
        this.getEntityData().setInteger(HOME_POSITION_Y, y);
        this.getEntityData().setInteger(HOME_POSITION_Z, z);
    }

    public void resetHomePos() {
        this.dataWatcher.updateObject(HAS_HOME_POSITION, 0);
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
        compound.setBoolean("HasHomePos", this.hasHomePos());
        if (this.hasHomePos()) {
            compound.setInteger("HomePosX", this.getHomePosX());
            compound.setInteger("HomePosY", this.getHomePosY());
            compound.setInteger("HomePosZ", this.getHomePosZ());
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
        if (compound.hasKey("HasHomePos")) { // todo ???? check this
            this.setHomePos(compound.getInteger("HomePosX"), compound.getInteger("HomePosY"), compound.getInteger("HomePosZ"));
        }
    }

    private String getPhenotype(int dataParameter) {
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
            return this.getDistanceSq(this.getHomePosX(), this.getHomePosY(), this.getHomePosZ()) < SimplyCatsConfig.WANDER_AREA_LIMIT;
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

        if (this.worldObj.isRemote && this.dataWatcher.hasChanges()) {
            this.dataWatcher.func_111144_e();
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
    public boolean interact(EntityPlayer player) {
        ItemStack stack = player.inventory.getCurrentItem();
        if (stack != null) {
            if (stack.getItem() == Items.string && player.isSneaking()) {
                if (this.worldObj.isRemote) {
                    player.addChatComponentMessage(new ChatComponentText(this.getGenotype(EYE_COLOR)));
                    player.addChatComponentMessage(new ChatComponentText(this.getGenotype(FUR_LENGTH) + ": " + getPhenotype(FUR_LENGTH)));
                    player.addChatComponentMessage(new ChatComponentText(this.getGenotype(EUMELANIN) + ": " + getPhenotype(EUMELANIN)));
                    player.addChatComponentMessage(new ChatComponentText(this.getGenotype(PHAEOMELANIN) + ": " + getPhenotype(PHAEOMELANIN)));
                    player.addChatComponentMessage(new ChatComponentText(this.getGenotype(DILUTION) + ": " + getPhenotype(DILUTION)));
                    player.addChatComponentMessage(new ChatComponentText(this.getGenotype(DILUTE_MOD) + ": " + getPhenotype(DILUTE_MOD)));
                    player.addChatComponentMessage(new ChatComponentText(this.getGenotype(AGOUTI) + ": " + getPhenotype(AGOUTI)));
                    player.addChatComponentMessage(new ChatComponentText(this.getGenotype(TABBY) + ": " + getPhenotype(TABBY)));
                    player.addChatComponentMessage(new ChatComponentText(this.getGenotype(SPOTTED) + ": " + getPhenotype(SPOTTED)));
                    player.addChatComponentMessage(new ChatComponentText(this.getGenotype(TICKED) + ": " + getPhenotype(TICKED)));
                    player.addChatComponentMessage(new ChatComponentText(this.getGenotype(COLORPOINT) + ": " + getPhenotype(COLORPOINT)));
                    player.addChatComponentMessage(new ChatComponentText(this.getGenotype(WHITE) + ": " + getPhenotype(WHITE)));
                    player.addChatComponentMessage(new ChatComponentText(this.getWhiteTextures(0) + ", " + this.getWhiteTextures(1)
                            + ", " + this.getWhiteTextures(2)));
                    player.addChatComponentMessage(new ChatComponentText(this.getWhitePawTextures(0) + ", " + this.getWhitePawTextures(1)
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
        DataWatcher father = parFather.getDataWatcher();
        DataWatcher mother = this.getDataWatcher();
        EntityCat child = new EntityCat(this.worldObj);

        String[] matFur = mother.getWatchableObjectString(FUR_LENGTH).split("-");
        String[] patFur = father.getWatchableObjectString(FUR_LENGTH).split("-");
        String fur = matFur[rand.nextInt(2)] + "-" + patFur[rand.nextInt(2)];

        String[] matEum = mother.getWatchableObjectString(EUMELANIN).split("-");
        String[] patEum = father.getWatchableObjectString(EUMELANIN).split("-");
        String eum = matEum[rand.nextInt(2)] + "-" + patEum[rand.nextInt(2)];

        String[] matPhae = mother.getWatchableObjectString(PHAEOMELANIN).split("-");
        String[] patPhae = father.getWatchableObjectString(PHAEOMELANIN).split("-");
        String phae = matPhae[rand.nextInt(2)] + "-" + patPhae[rand.nextInt(2)];

        String[] matDil = mother.getWatchableObjectString(DILUTION).split("-");
        String[] patDil = father.getWatchableObjectString(DILUTION).split("-");
        String dil = matDil[rand.nextInt(2)] + "-" + patDil[rand.nextInt(2)];

        String[] matDilm = mother.getWatchableObjectString(DILUTE_MOD).split("-");
        String[] patDilm = father.getWatchableObjectString(DILUTE_MOD).split("-");
        String dilm = matDilm[rand.nextInt(2)] + "-" + patDilm[rand.nextInt(2)];

        String[] matAgo = mother.getWatchableObjectString(AGOUTI).split("-");
        String[] patAgo = father.getWatchableObjectString(AGOUTI).split("-");
        String ago = matAgo[rand.nextInt(2)] + "-" + patAgo[rand.nextInt(2)];

        String[] matTab = mother.getWatchableObjectString(TABBY).split("-");
        String[] patTab = father.getWatchableObjectString(TABBY).split("-");
        String tab = matTab[rand.nextInt(2)] + "-" + patTab[rand.nextInt(2)];

        String[] matSpot = mother.getWatchableObjectString(SPOTTED).split("-");
        String[] patSpot = father.getWatchableObjectString(SPOTTED).split("-");
        String spot = matSpot[rand.nextInt(2)] + "-" + patSpot[rand.nextInt(2)];

        String[] matTick = mother.getWatchableObjectString(TICKED).split("-");
        String[] patTick = father.getWatchableObjectString(TICKED).split("-");
        String tick = matTick[rand.nextInt(2)] + "-" + patTick[rand.nextInt(2)];

        String[] matPoint = mother.getWatchableObjectString(COLORPOINT).split("-");
        String[] patPoint = father.getWatchableObjectString(COLORPOINT).split("-");
        String point = matPoint[rand.nextInt(2)] + "-" + patPoint[rand.nextInt(2)];

        String[] matWhite = mother.getWatchableObjectString(WHITE).split("-");
        String[] patWhite = father.getWatchableObjectString(WHITE).split("-");
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
        int matEye = EyeColor.valueOf(mother.getWatchableObjectString(EYE_COLOR).toUpperCase()).ordinal();
        int patEye = EyeColor.valueOf(father.getWatchableObjectString(EYE_COLOR).toUpperCase()).ordinal();
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

        if (this.isTamed() && this.getOwner() != null) {
            child.setTamed(this.isTamed());
            child.func_152115_b(this.getOwner().getUniqueID().toString());
            if (this.hasHomePos())
                child.setHomePos(this.getHomePosX(), this.getHomePosY(), this.getHomePosZ());
        }

        return child;
    }

    @Override
    public void fall(float distance) {
    }

    @Override
    protected boolean canTriggerWalking() {
        return false;
    }

    public boolean isAngry() {
        return (this.dataWatcher.getWatchableObjectByte(16) & 2) != 0;
    }

    public void setAngry(boolean angry) {
        byte b0 = this.dataWatcher.getWatchableObjectByte(16);

        if (angry)
            this.dataWatcher.updateObject(16, (byte)(b0 | 2));
        else
            this.dataWatcher.updateObject(16, (byte)(b0 & -3));
    }

    @Override
    protected String getLivingSound() {
        if (this.isAngry()) {
            if (this.rand.nextInt(10) == 0)
                return "mob.cat.hiss";
            else
                return "";
        } else if (this.isInLove() || this.PURR) {
            return "mob.cat.purr";
        } else {
            if (this.rand.nextInt(10) == 0) {
                if (this.rand.nextInt(10) == 0)
                    return "mob.cat.purreow";
                else
                    return "mob.cat.meow";
            }
        }
        return "";
    }

    @Override
    protected String getHurtSound() {
        return "mob.cat.hitt";
    }

    @Override
    protected String getDeathSound() {
        return "mob.cat.hitt";
    }

    @Override
    protected float getSoundVolume() {
        return 0.4F;
    }

    public String getName() {
        return this.getCommandSenderName();
    }

    public String getCommandSenderName() {
        return this.hasCustomNameTag() ? this.getCustomNameTag() : StatCollector.translateToLocal("entity.simplycats.Cat.name");
    }
}
