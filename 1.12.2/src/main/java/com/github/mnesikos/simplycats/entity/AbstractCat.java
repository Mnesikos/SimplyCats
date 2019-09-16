package com.github.mnesikos.simplycats.entity;

import com.github.mnesikos.simplycats.Ref;
import com.github.mnesikos.simplycats.entity.core.Genetics;
import com.github.mnesikos.simplycats.entity.core.Genetics.*;
import net.minecraft.entity.passive.EntityTameable;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.util.EnumHand;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.ArrayList;
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
    private static final List<DataParameter<String>> GENES = new ArrayList<>(12);
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

    public AbstractCat(World world) {
        super(world);
        GENES.add(EYE_COLOR);
        GENES.add(FUR_LENGTH);
        GENES.add(EUMELANIN);
        GENES.add(PHAEOMELANIN);
        GENES.add(DILUTION);
        GENES.add(DILUTE_MOD);
        GENES.add(AGOUTI);
        GENES.add(TABBY);
        GENES.add(SPOTTED);
        GENES.add(TICKED);
        GENES.add(COLORPOINT);
        GENES.add(WHITE);
        setPhenotype();
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

    private void selectWhiteMarkings() {
        int base;
        int body = 0;
        int face = 0;
        int tail = 0;

        for (int j = 0; j <= 3; j++) {
            this.whitePawTexturesArray[j] = "";
            this.setWhitePawTextures(j, "");
        }

        if (getPhenotype(WHITE).equalsIgnoreCase(White.DOMINANT.toString())) {
            base = 6;
            body = 1;
            face = 0;
            tail = 0;

        } else if (getPhenotype(WHITE).equalsIgnoreCase(White.NONE.toString())) {
            base = 0;
            body = 0;
            face = 0;
            tail = 0;

        } else if (getGenotype(WHITE).equals(White.SPOTTING.getAllele() + "-" + White.SPOTTING.getAllele())) { //Ws-Ws
            base = rand.nextInt(3) + 4; //4-6
            if (base == 5) {
                body = rand.nextInt(4) + 1;
                face = rand.nextInt(6) + 1;
                if (body > 1)
                    tail = rand.nextInt(3) + 1;
            }

            if (base == 4) {
                body = 1;
                face = rand.nextInt(5) + 1;
            }

        } else { //Ws-w && w-Ws
            base = rand.nextInt(4) + 1; //1-4
            body = 1;
            if (base == 2 || base == 3)
                this.selectWhitePaws(base);

            if (base == 3 || base == 4) {
                face = rand.nextInt(5) + 1;
            }
        }

        this.whiteTexturesArray[0] = body == 0 ? "" : "white_" + base + "_body" + body;
        this.setWhiteTextures(0, whiteTexturesArray[0]);

        this.whiteTexturesArray[1] = face == 0 ? "" : "white_" + (base == 3 || base == 4 ? 34 : base) + "_face" + face;
        this.setWhiteTextures(1, whiteTexturesArray[1]);

        this.whiteTexturesArray[2] = tail == 0 ? "" : "white_" + base + "_tail" + tail;
        this.setWhiteTextures(2, whiteTexturesArray[2]);
    }

    private void selectWhitePaws(int base) {
        if (rand.nextInt(4) <= 2) {
            this.whitePawTexturesArray[0] = "white_" + base + "_paw1";
            this.setWhitePawTextures(0, whitePawTexturesArray[0]);
        }
        /*else
            this.whitePawTexturesArray[0] = "";*/

        if (rand.nextInt(4) <= 2) {
            this.whitePawTexturesArray[1] = "white_" + base + "_paw2";
            this.setWhitePawTextures(1, whitePawTexturesArray[1]);
        }

        if (rand.nextInt(4) <= 2) {
            this.whitePawTexturesArray[2] = "white_" + base + "_paw3";
            this.setWhitePawTextures(2, whitePawTexturesArray[2]);
        }

        if (rand.nextInt(4) <= 2) {
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

    private void setGenotype(DataParameter<String> parameter, String value) {
        this.dataManager.set(parameter, value);
    }

    public String getSex() {
        return this.dataManager.get(PHAEOMELANIN).contains(Phaeomelanin.MALE.getAllele()) ? Sex.MALE.getName() : Sex.FEMALE.getName();
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
            if (this.getPhenotype(DILUTE_MOD).equalsIgnoreCase(DiluteMod.CARMELIZED.toString().toLowerCase()))
                solid = solid + "_" + this.getPhenotype(DILUTE_MOD);
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
                if (this.getPhenotype(DILUTE_MOD).equalsIgnoreCase(DiluteMod.CARMELIZED.toString().toLowerCase()))
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
                this.getWhitePawTextures(0) + this.getWhitePawTextures(1) +this.getWhitePawTextures(2) +this.getWhitePawTextures(3) +
                getPhenotype(EYE_COLOR);
        //System.out.println(this.texturePrefix);
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

        if (this.world.isRemote && this.dataManager.isDirty()) {
            this.dataManager.setClean();
            this.resetTexturePrefix();
        }
    }

    @Override
    public boolean processInteract(EntityPlayer player, EnumHand hand) {
        ItemStack stack = player.getHeldItem(hand);
        if (stack != null) {
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
                }
                return true;
            }
        }

        return super.processInteract(player, hand);
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
