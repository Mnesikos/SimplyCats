package com.github.mnesikos.simplycats;

import com.github.mnesikos.simplycats.entity.core.Genetics;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.datafix.IFixableData;

import java.util.Random;

public class CatDataFixer implements IFixableData {
    @Override
    public int getFixVersion() {
        return 2;
    }

    @Override
    public NBTTagCompound fixTagCompound(NBTTagCompound compound) {
        if(compound.hasKey("id")) {
            String id = compound.getString("id");
            if(id.equals("simplycats:cat")) {
                boolean red = false;
                boolean dilute = false;

                if (compound.hasKey("eyes")) {
                    compound.setString("EyeColor", Genetics.EyeColor.init(compound.getInteger("eyes")));
                    compound.removeTag("eyes");
                }

                if (compound.hasKey("Type")) {
                    compound.setString("FurLength", Genetics.FurLength.init() + "-" + Genetics.FurLength.init());
                    compound.removeTag("Type");
                }

                if (compound.hasKey("Base")) {
                    String base = Genetics.Eumelanin.init() + "-" + Genetics.Eumelanin.init();
                    switch (compound.getInteger("Base")) {
                        case 0:
                            base = Genetics.Eumelanin.BLACK.getAllele() + "-" + Genetics.Eumelanin.init();
                            break;
                        case 1:
                            dilute = true;
                            break;
                        case 3:
                            red = true;
                            dilute = true;
                            break;
                        case 2:
                            red = true;
                            break;
                    }
                    compound.setString("Eumelanin", base);
                    compound.removeTag("Base");
                }

                if (compound.hasKey("Sex")) {
                    if (compound.getInteger("Sex") == 0) {
                        if (compound.hasKey("tortie") && compound.getInteger("tortie") != 0)
                            compound.setString("Phaeomelanin", Genetics.Phaeomelanin.RED.getAllele() + "-" + Genetics.Phaeomelanin.NOT_RED.getAllele());
                        else if (red)
                            compound.setString("Phaeomelanin", Genetics.Phaeomelanin.RED.getAllele() + "-" + Genetics.Phaeomelanin.RED.getAllele());
                        else
                            compound.setString("Phaeomelanin", Genetics.Phaeomelanin.NOT_RED.getAllele() + "-" + Genetics.Phaeomelanin.NOT_RED.getAllele());
                    }
                    if (compound.getInteger("Sex") == 1) {
                        if (red)
                            compound.setString("Phaeomelanin", Genetics.Phaeomelanin.RED.getAllele() + "-" + Genetics.Phaeomelanin.MALE.getAllele());
                        else
                            compound.setString("Phaeomelanin", Genetics.Phaeomelanin.NOT_RED.getAllele() + "-" + Genetics.Phaeomelanin.MALE.getAllele());
                    }
                    compound.removeTag("Sex");
                }

                if (dilute)
                    compound.setString("Dilution", Genetics.Dilution.DILUTE.getAllele() + "-" + Genetics.Dilution.DILUTE.getAllele());
                else
                    compound.setString("Dilution", Genetics.Dilution.NON_DILUTE.getAllele() + "-" + Genetics.Dilution.init());

                compound.setString("DiluteMod", Genetics.DiluteMod.NORMAL.getAllele() + "-" + Genetics.DiluteMod.init());

                if (compound.hasKey("tabby")) {
                    int origTabby = compound.getInteger("tabby");
                    if (origTabby >= 3)
                        compound.setString("Agouti", Genetics.Agouti.init() + "-" + Genetics.Agouti.init());
                    if (origTabby != 0 && origTabby <= 2)
                        compound.setString("Agouti", Genetics.Agouti.TABBY.getAllele() + "-" + Genetics.Agouti.init());
                    if (origTabby == 0)
                        compound.setString("Agouti", Genetics.Agouti.SOLID.getAllele() + "-" + Genetics.Agouti.SOLID.getAllele());
                    compound.setString("Tabby", Genetics.Tabby.MACKEREL.getAllele() + "-" + Genetics.Tabby.init());
                    compound.setString("Spotted", Genetics.Spotted.NORMAL.getAllele() + "-" + Genetics.Spotted.init());
                    compound.setString("Ticked", Genetics.Ticked.NORMAL.getAllele() + "-" + Genetics.Ticked.init());
                    compound.removeTag("tabby");
                }

                compound.setString("Colorpoint", Genetics.Colorpoint.NOT_POINTED.getAllele() + "-" + Genetics.Colorpoint.init());

                if (compound.hasKey("white")) {
                    String white = Genetics.White.init() + "-" + Genetics.White.init();
                    switch (compound.getInteger("white")) {
                        case 0:
                            white = Genetics.White.NONE.getAllele() + "-" + Genetics.White.NONE.getAllele();
                            break;
                        case 1:
                        case 2:
                        case 3:
                            white = Genetics.White.SPOTTING.getAllele() + "-" + Genetics.White.NONE.getAllele();
                            break;
                        case 4:
                        case 5:
                            white = Genetics.White.SPOTTING.getAllele() + "-" + Genetics.White.SPOTTING.getAllele();
                        case 6:
                            Random rand = new Random();
                            if (rand.nextInt(8) == 0)
                                white = Genetics.White.DOMINANT.getAllele() + "-" + Genetics.White.DOMINANT.getAllele();
                            break;
                    }
                    compound.setString("White", white);
                    this.setWhiteMarkings(compound);
                    compound.removeTag("white");
                }
            }
        }
        return compound;
    }


    private void setWhiteMarkings(NBTTagCompound compound) {
        int base;
        int body = 0;
        int face = 0;
        int tail = 0;
        Random rand = new Random();

        compound.setString("WhitePaws_0", "");
        compound.setString("WhitePaws_1", "");
        compound.setString("WhitePaws_2", "");
        compound.setString("WhitePaws_3", "");

        switch (compound.getString("White")) {
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
                    this.setWhitePaws(compound, base);
                if (base == 3)
                    face = rand.nextInt(5) + 1;
                break;

            default:
                throw new IllegalArgumentException("Error selecting white markings; " + compound.getString("White"));
        }

        compound.setString("White_0", (body == 0 ? "" : "white_" + base + "_body" + body));
        compound.setString("White_1", (face == 0 ? "" : "white_" + (base == 3 || base == 4 ? 34 : base) + "_face" + face));
        compound.setString("White_2", (tail == 0 ? "" : "white_" + base + "_tail" + tail));
    }

    private void setWhitePaws(NBTTagCompound compound, int base) {
        Random rand = new Random();
        if (rand.nextInt(4) <= 2)
            compound.setString("WhitePaws_0", "white_" + base + "_paw1");

        if (rand.nextInt(4) <= 2)
            compound.setString("WhitePaws_1", "white_" + base + "_paw2");

        if (rand.nextInt(4) <= 2)
            compound.setString("WhitePaws_2", "white_" + base + "_paw3");

        if (rand.nextInt(4) <= 2)
            compound.setString("WhitePaws_3", "white_" + base + "_paw4");
    }
}
