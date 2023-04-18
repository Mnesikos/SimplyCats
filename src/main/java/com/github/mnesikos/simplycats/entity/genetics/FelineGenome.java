package com.github.mnesikos.simplycats.entity.genetics;

import com.github.mnesikos.simplycats.SimplyCats;
import com.github.mnesikos.simplycats.entity.SimplyCatEntity;
import com.google.common.collect.ImmutableList;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import java.util.Arrays;
import java.util.List;

public class FelineGenome extends Genome {
    // The improved readability from snake case is worth breaking the enum
    // convention of all caps for genes
    public enum Gene {
        fur_length,
        eumelanin,
        phaeomelanin,
        dilution,
        dilute_mod,
        agouti,
        tabby,
        spotted,
        ticked,
        inhibitor,
        colorpoint,
        white,
        bobtail;

        public final List<Float> distribution() {
            if (!CatAlleles.frequencies.containsKey(this)) return ImmutableList.of(1f);
            return CatAlleles.frequencies.get(this);
        }
    }

    public enum Marking {
        eye_color,
        white_base,
        white_body,
        white_face,
        white_tail,
        white_paw1,
        white_paw2,
        white_paw3,
        white_paw4;
    }

    public FelineGenome(SimplyCatEntity entity) {
        super(entity);
    }

    @Override
    public List<Enum> listGenes() {
        return Arrays.asList(Gene.values());
    }

    public boolean isLonghair() {
        return isHomozygous(Gene.fur_length, CatAlleles.LONGHAIR);
    }

    public boolean isMale() {
        return hasAllele(Gene.phaeomelanin, CatAlleles.Y_CHROMOSOME);
    }

    public boolean isRed() {
        int allele0 = getAllele(Gene.phaeomelanin, 0);
        int allele1 = getAllele(Gene.phaeomelanin, 1);
        return (allele0 == CatAlleles.X_RED || allele0 == CatAlleles.Y_CHROMOSOME)
                && (allele1 == CatAlleles.X_RED || allele1 == CatAlleles.Y_CHROMOSOME);
    }

    public boolean isTortoiseshell() {
        return hasAllele(Gene.phaeomelanin, CatAlleles.X_RED) && hasAllele(Gene.phaeomelanin, CatAlleles.X_NOT_RED);
    }

    public boolean isDilute() {
        return isHomozygous(Gene.dilution, CatAlleles.DILUTE);
    }

    public boolean isDiluteMod() {
        return isDilute() && hasAllele(Gene.dilute_mod, CatAlleles.CARAMELIZED);
    }

    public boolean isAgouti() {
        return hasAllele(Gene.agouti, CatAlleles.AGOUTI);
    }

    public boolean hasMackerelTabby() {
        return hasAllele(Gene.tabby, CatAlleles.MACKEREL);
    }

    public boolean isTickedTabby() {
        return hasAllele(Gene.ticked, CatAlleles.TICKED);
    }

    public boolean isSilver() {
        return hasAllele(Gene.inhibitor, CatAlleles.SILVER);
    }

    public boolean isAlbino() {
        boolean hetAlbino = hasAllele(Gene.colorpoint, CatAlleles.RED_EYED_ALBINO) && hasAllele(Gene.colorpoint, CatAlleles.BLUE_EYED_ALBINO);
        return !isHomozygous(Gene.colorpoint, CatAlleles.RED_EYED_ALBINO) && !isHomozygous(Gene.colorpoint, CatAlleles.BLUE_EYED_ALBINO)
                && !hetAlbino;
    }

    public boolean isBobtail() {
        return isHomozygous(Gene.bobtail, CatAlleles.BOBTAIL);
    }

    // Distribution should be a series of floats increasing from
    // 0.0 to 1.0, where the probability of choosing allele i is
    // the chance that a random uniform number between 0 and 1
    // is greater than distribution[i-1] but less than distribution[i].
    protected int chooseRandomAllele(List<Float> distribution) {
        float n = this.entity.getRandom().nextFloat();
        for (int i = 0; i < distribution.size(); ++i) {
            if (n < distribution.get(i)) {
                return i;
            }
        }
        // In case of floating point rounding errors
        return distribution.size() - 1;
    }

    protected void randomizeGenes() {
        for (Enum gene : listGenes()) {
            int allele0 = chooseRandomAllele(((Gene) gene).distribution());
            int allele1 = chooseRandomAllele(((Gene) gene).distribution());
            setAllele(gene, 0, allele0);
            setAllele(gene, 1, allele1);
        }
    }

    protected void randomizeMarkings() {
        int eyeColor = random.nextInt(4);
        if (hasAllele(Gene.white, CatAlleles.DOMINANT_WHITE)) eyeColor = random.nextInt(5);
        if (isHomozygous(Gene.colorpoint, CatAlleles.COLORPOINT)) eyeColor = 4;
        if (isAlbino()) eyeColor = hasAllele(Gene.colorpoint, CatAlleles.BLUE_EYED_ALBINO) ? 5 : 6;
        setMarking(Marking.eye_color, eyeColor);

        int whiteBase;
        int whiteBody = 0;
        int whiteFace = 0;
        int whiteTail = 0;
        for (int j = 4; j <= 7; j++) this.markingsTexturesArray[j] = "";
        setMarking(Marking.white_paw1, 0);
        setMarking(Marking.white_paw2, 0);
        setMarking(Marking.white_paw3, 0);
        setMarking(Marking.white_paw4, 0);
        if (hasAllele(Gene.white, CatAlleles.DOMINANT_WHITE)) {
            whiteBase = 6;
            whiteBody = 1;
        } else if (isHomozygous(Gene.white, CatAlleles.NO_WHITE)) {
            whiteBase = 0;
        } else if (isHomozygous(Gene.white, CatAlleles.WHITE_SPOTTING)) {
            whiteBase = random.nextInt(2) + 4; // 4-5
            if (whiteBase == 5) {
                whiteBody = random.nextInt(4) + 1;
                whiteFace = random.nextInt(6) + 1;
                if (whiteBody > 1) whiteTail = random.nextInt(3) + 1;
            } else {
                whiteBody = 1;
                whiteFace = random.nextInt(5) + 1;
            }
            if (random.nextInt(10) == 0) { // 10% chance for solid white
                whiteBase = 6;
                whiteBody = 1;
                whiteFace = 0;
                whiteTail = 0;
            }
        } else {
            whiteBase = random.nextInt(3) + 1; // 1-3
            whiteBody = 1;
            if (whiteBase == 2 || whiteBase == 3) {
                // for all 4 paws to be white, more common than only random 1-4 white paws
                boolean allPaws = random.nextInt(4) <= 2;
                if (allPaws || random.nextInt(4) <= 2) {
                    this.markingsTexturesArray[4] = "white_" + whiteBase + "_paw1";
                    setMarking(Marking.white_paw1, whiteBase);
                }
                if (allPaws || random.nextInt(4) <= 2) {
                    this.markingsTexturesArray[5] = "white_" + whiteBase + "_paw2";
                    setMarking(Marking.white_paw2, whiteBase);
                }
                if (allPaws || random.nextInt(4) <= 2) {
                    this.markingsTexturesArray[6] = "white_" + whiteBase + "_paw3";
                    setMarking(Marking.white_paw3, whiteBase);
                }
                if (allPaws || random.nextInt(4) <= 2) {
                    this.markingsTexturesArray[7] = "white_" + whiteBase + "_paw4";
                    setMarking(Marking.white_paw4, whiteBase);
                }
            }
            if (whiteBase == 3)
                whiteFace = random.nextInt(5) + 1;
        }
        this.markingsTexturesArray[1] = whiteBody == 0 ? "" : "white_" + whiteBase + "_body" + whiteBody;
        setMarking(Marking.white_base, whiteBase);
        setMarking(Marking.white_body, whiteBody);
        this.markingsTexturesArray[2] = whiteFace == 0 ? "" : "white_" + (whiteBase == 3 || whiteBase == 4 ? 34 : whiteBase) + "_face" + whiteFace;
        setMarking(Marking.white_face, whiteFace);
        this.markingsTexturesArray[3] = whiteTail == 0 ? "" : "white_" + whiteBase + "_tail" + whiteTail;
        setMarking(Marking.white_tail, whiteTail);
    }

    public void finalizeGenes() {
        // Make half the cats male
        if (random.nextBoolean()) {
            setAllele(Gene.phaeomelanin, 1, CatAlleles.Y_CHROMOSOME);
        }
    }

    public void randomize() {
        randomizeGenes();
        finalizeGenes();
        randomizeMarkings();
    }

    @OnlyIn(Dist.CLIENT)
    public void setTexturePaths() {
        String solid;
        if (isRed()) solid = "red";
        else if (hasAllele(Gene.eumelanin, CatAlleles.BLACK)) solid = "black";
        else if (hasAllele(Gene.eumelanin, CatAlleles.CHOCOLATE)) solid = "chocolate";
        else solid = "cinnamon";

        if (isDilute()) {
            solid += "_dilute";
            if (isDiluteMod())
                solid += "_caramelized";
        }

        String smoke = isSilver() ? "silver" : "";
        if (isSilver()) {
            if (isRed())
                smoke = "silver_red";
            else if (isDilute())
                smoke = "silver_dilute";
        }

        String tabby = (hasMackerelTabby() ? "mackerel" : "classic") + "_" + solid + (isSilver() ? "_silver" : "");
        if (hasAllele(Gene.spotted, CatAlleles.SPOTTED)) {
            if (isHomozygous(Gene.spotted, CatAlleles.SPOTTED)) tabby = "spotted_" + tabby;
            else tabby = "broken_" + tabby;
        }
        if (isTickedTabby())
            tabby = "ticked" + (hasAllele(Gene.ticked, CatAlleles.NON_TICKED) ? "_residual" : "") + "_" + solid + (isSilver() ? "_silver" : "");

        String tortie = "";
        if (isTortoiseshell()) {
            tortie = "tortoiseshell_" + (tabby.replace(("_" + solid), ""));
            if (isDilute()) {
                tortie += "_dilute";
                if (isDiluteMod())
                    tortie += "_caramelized";
            }
        }

        if (!isRed() && !isAgouti())
            tabby = "";

        String colorpoint = "";
        if (!hasAllele(Gene.colorpoint, CatAlleles.NOT_POINTED)) {
            if (hasAllele(Gene.colorpoint, CatAlleles.SEPIA) && hasAllele(Gene.colorpoint, CatAlleles.COLORPOINT))
                colorpoint = "mink";
            else if (hasAllele(Gene.colorpoint, CatAlleles.COLORPOINT)) colorpoint = "colorpoint";
            else if (hasAllele(Gene.colorpoint, CatAlleles.SEPIA)) colorpoint = "sepia";
            else colorpoint = "albino";
            if (!isAlbino()) {
                if (!tabby.isEmpty() && !isRed())
                    colorpoint += "_tabby";
                else if (solid.equalsIgnoreCase("black"))
                    colorpoint += "_black";
                else if (isRed())
                    colorpoint += "_red";
                if (!tortie.isEmpty())
                    tortie += "_point";
            }
        }

        String eyeColor = "hazel";
        if (getMarking(Marking.eye_color) == 0) eyeColor = "copper";
        else if (getMarking(Marking.eye_color) == 1) eyeColor = "gold";
        else if (getMarking(Marking.eye_color) == 2) eyeColor = "hazel";
        else if (getMarking(Marking.eye_color) == 3) eyeColor = "green";
        else if (getMarking(Marking.eye_color) == 4) eyeColor = "blue";
        else if (getMarking(Marking.eye_color) == 5) eyeColor = "albino_blue";
        else if (getMarking(Marking.eye_color) == 6) eyeColor = "albino_red";

        this.texturesArray[0] = SimplyCats.MOD_ID + ":textures/entity/cat/solid/" + solid + ".png";
        this.texturesArray[1] = tabby.equals("") ? null : (SimplyCats.MOD_ID + ":textures/entity/cat/tabby/" + tabby + ".png");
        this.texturesArray[2] = smoke.equals("") ? null : (SimplyCats.MOD_ID + ":textures/entity/cat/solid/" + smoke + ".png");
        this.texturesArray[3] = tortie.equals("") ? null : (SimplyCats.MOD_ID + ":textures/entity/cat/tortie/" + tortie + ".png");
        this.texturesArray[4] = colorpoint.equals("") ? null : (SimplyCats.MOD_ID + ":textures/entity/cat/colorpoint/" + colorpoint + ".png");
        this.texturesArray[5] = getMarking(Marking.white_body) == 0 ? null : (SimplyCats.MOD_ID + ":textures/entity/cat/white/" + markingsTexturesArray[1] + ".png");
        this.texturesArray[6] = getMarking(Marking.white_face) == 0 ? null : (SimplyCats.MOD_ID + ":textures/entity/cat/white/" + markingsTexturesArray[2] + ".png");
        this.texturesArray[7] = getMarking(Marking.white_tail) == 0 ? null : (SimplyCats.MOD_ID + ":textures/entity/cat/white/" + markingsTexturesArray[3] + ".png");
        this.texturesArray[8] = getMarking(Marking.white_paw1) == 0 ? null : (SimplyCats.MOD_ID + ":textures/entity/cat/white/" + markingsTexturesArray[4] + ".png");
        this.texturesArray[9] = getMarking(Marking.white_paw2) == 0 ? null : (SimplyCats.MOD_ID + ":textures/entity/cat/white/" + markingsTexturesArray[5] + ".png");
        this.texturesArray[10] = getMarking(Marking.white_paw3) == 0 ? null : (SimplyCats.MOD_ID + ":textures/entity/cat/white/" + markingsTexturesArray[6] + ".png");
        this.texturesArray[11] = getMarking(Marking.white_paw4) == 0 ? null : (SimplyCats.MOD_ID + ":textures/entity/cat/white/" + markingsTexturesArray[7] + ".png");
        this.texturesArray[12] = SimplyCats.MOD_ID + ":textures/entity/cat/eyes/" + eyeColor + ".png";
        this.texturePrefix = "cat/" + solid + tabby + smoke + tortie + colorpoint +
                markingsTexturesArray[1] + markingsTexturesArray[2] + markingsTexturesArray[3] +
                markingsTexturesArray[4] + markingsTexturesArray[5] + markingsTexturesArray[6] + markingsTexturesArray[7] +
                eyeColor;
    }

    public static StringTextComponent getPhenotypeDescription(CompoundNBT nbt, boolean includeSex) {
        ITextComponent sex = Genetics.Sex.getPrettyName(nbt.getString("Phaeomelanin"));

        String eumelanin = Genetics.Eumelanin.getPhenotype(nbt.getString("Eumelanin"));
        String phaeomelanin = Genetics.Phaeomelanin.getPhenotype(nbt.getString("Phaeomelanin"));
        String dilution = Genetics.Dilution.getPhenotype(nbt.getString("Dilution"));
        String diluteMod = Genetics.DiluteMod.getPhenotype(nbt.getString("DiluteMod"));
        boolean isRed = phaeomelanin.equals(Genetics.Phaeomelanin.RED.toString().toLowerCase());
        String redElseBlack = isRed ? phaeomelanin : eumelanin;
        ITextComponent base = new TranslationTextComponent("cat.base." + (redElseBlack) + ".name");
        if (dilution.equals(Genetics.Dilution.DILUTE.toString().toLowerCase())) {
            base = new TranslationTextComponent("cat.base." + (redElseBlack) + "_" + dilution + ".name");
            if (diluteMod.equals(Genetics.DiluteMod.CARAMELIZED.toString().toLowerCase()))
                base = new TranslationTextComponent("cat.base." + (redElseBlack) + "_" + diluteMod + ".name");
        }

        String agouti = Genetics.Agouti.getPhenotype(nbt.getString("Agouti"));
        boolean isAgouti = agouti.equals(Genetics.Agouti.TABBY.toString().toLowerCase());
        String inhibitor = Genetics.Inhibitor.getPhenotype(nbt.getString("Inhibitor"));
        if (inhibitor.equals(Genetics.Inhibitor.SILVER.toString().toLowerCase()))
            base = new StringTextComponent(base.getString() + " " + (new TranslationTextComponent("cat.base." + inhibitor + (isAgouti ? "" : "_smoke") + ".name")).getString());
        if (phaeomelanin.equals(Genetics.Phaeomelanin.TORTOISESHELL.toString().toLowerCase()))
            base = new StringTextComponent(base.getString() + " " + (new TranslationTextComponent("cat.base." + phaeomelanin + ".name")).getString());

        String tabby1 = Genetics.Tabby.getPhenotype(nbt.getString("Tabby"));
        String spotted = Genetics.Spotted.getPhenotype(nbt.getString("Spotted"));
        String ticked = Genetics.Ticked.getPhenotype(nbt.getString("Ticked"));
        ITextComponent tabby = new TranslationTextComponent("");
        if (isAgouti || isRed) {
            tabby = new TranslationTextComponent("cat.tabby." + tabby1 + ".name");
            if (spotted.equals(Genetics.Spotted.BROKEN.toString().toLowerCase()) || spotted.equals(Genetics.Spotted.SPOTTED.toString().toLowerCase()))
                tabby = new TranslationTextComponent("cat.tabby." + spotted + ".name");
            if (ticked.equals(Genetics.Ticked.TICKED.toString().toLowerCase()))
                tabby = new TranslationTextComponent("cat.tabby." + ticked + ".name");
        }

        String colorpoint = Genetics.Colorpoint.getPhenotype(nbt.getString("Colorpoint"));
        ITextComponent point = new TranslationTextComponent("");
        if (!colorpoint.equals(Genetics.Colorpoint.NOT_POINTED.toString().toLowerCase())) {
            point = new TranslationTextComponent("cat.point." + colorpoint + ".name");
            if (colorpoint.equals(Genetics.Colorpoint.ALBINO.toString().toLowerCase())) {
                ITextComponent eyes = new TranslationTextComponent("cat.point.red_eyed.name");
                if (nbt.getString("Colorpoint").contains(Genetics.Colorpoint.BLUE_EYED_ALBINO.getAllele()))
                    eyes = new TranslationTextComponent("cat.point.blue_eyed.name");
                return new StringTextComponent(eyes.getString() + " " + point.getString() + (includeSex ? (" " + sex.getString()) : ""));
            }
        }

        String white = Genetics.White.getPhenotype(nbt.getString("White"));
        ITextComponent whiteText = new TranslationTextComponent("");
        if (!white.equals(Genetics.White.NONE.toString().toLowerCase())) {
            if (white.equals(Genetics.White.DOMINANT.toString().toLowerCase()) || nbt.getString("White_0").contains("6")) {
                whiteText = new TranslationTextComponent("cat.white.solid_white.name");
                return new StringTextComponent(whiteText.getString() + (includeSex ? (" " + sex.getString()) : ""));
            }
            if (nbt.getString("White_0").contains("5")) {
                whiteText = new TranslationTextComponent("cat.white.mostly_white.name");
                return new StringTextComponent(whiteText.getString() + " " + base.getString() +
                        (tabby.getString().equals("") ? "" : " " + tabby.getString()) +
                        (point.getString().equals("") ? "" : " " + point.getString()) +
                        (includeSex ? (" " + sex.getString()) : ""));
            } else
                whiteText = new TranslationTextComponent("cat.white.some_white.name");
        }

        return new StringTextComponent(base.getString() +
                (tabby.getString().equals("") ? "" : " " + tabby.getString()) +
                (point.getString().equals("") ? "" : " " + point.getString()) +
                " " + whiteText.getString() + (includeSex ? (" " + sex.getString()) : ""));
    }
}
