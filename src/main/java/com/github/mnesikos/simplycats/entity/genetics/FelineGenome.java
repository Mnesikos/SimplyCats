package com.github.mnesikos.simplycats.entity.genetics;

import com.github.mnesikos.simplycats.SimplyCats;
import com.github.mnesikos.simplycats.entity.SimplyCatEntity;
import com.google.common.collect.ImmutableList;
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

    public boolean isCaramelized() {
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

    public String getGenePhenotype(Gene gene) {
        switch (gene) {
            case fur_length:
                if (isLonghair()) return "longhair";
                else return "shorthair";

            case eumelanin:
                if (hasAllele(Gene.eumelanin, CatAlleles.BLACK)) return "black";
                else if (hasAllele(Gene.eumelanin, CatAlleles.CHOCOLATE)) return "chocolate";
                else return "cinnamon";

            case phaeomelanin:
                if (isRed()) return "red";
                else if (isTortoiseshell()) return "tortoiseshell";
                else return "";

            case dilution:
                if (isDilute()) return "dilute";
                else return "";

            case dilute_mod:
                if (isDilute() && isCaramelized()) return "caramelized";
                else return "";

            case agouti:
                if (hasAllele(Gene.agouti, CatAlleles.AGOUTI)) return "tabby";
                else return "";

            case tabby:
                if (hasMackerelTabby()) return "mackerel";
                else return "classic";

            case spotted:
                if (isHomozygous(Gene.spotted, CatAlleles.SPOTTED)) return "spotted";
                else if (hasAllele(Gene.spotted, CatAlleles.SPOTTED)) return "broken";
                else return "";

            case ticked:
                /*if (isTickedTabby() && hasAllele(Gene.ticked, CatAlleles.NON_TICKED)) return "ticked_residual";
                else */
                if (isTickedTabby()) return "ticked";
                else return "";

            case inhibitor:
                if (isSilver()) return "silver";
                else return "";

            case colorpoint:
                if (hasAllele(Gene.colorpoint, CatAlleles.NOT_POINTED)) return "";
                else if (hasAllele(Gene.colorpoint, CatAlleles.SEPIA) && hasAllele(Gene.colorpoint, CatAlleles.COLORPOINT))
                    return "mink";
                else if (hasAllele(Gene.colorpoint, CatAlleles.COLORPOINT)) return "colorpoint";
                else if (hasAllele(Gene.colorpoint, CatAlleles.SEPIA)) return "sepia";
                else return "albino";

            case white:
                if (hasAllele(Gene.white, CatAlleles.DOMINANT_WHITE)) return "white";
                else return "";

            case bobtail:
                if (isBobtail()) return "bobtail";
                else return "";

            default:
                throw new IllegalStateException("Unexpected value: " + gene);
        }
    }

    @OnlyIn(Dist.CLIENT)
    public void setTexturePaths() {
        String solid;
        if (isRed()) solid = getGenePhenotype(Gene.phaeomelanin);
        else solid = getGenePhenotype(Gene.eumelanin);

        if (isDilute()) {
            solid += "_" + getGenePhenotype(Gene.dilution);
            if (isCaramelized())
                solid += "_" + getGenePhenotype(Gene.dilute_mod);
        }

        String smoke = getGenePhenotype(Gene.inhibitor);
        if (isSilver()) {
            if (isRed())
                smoke += "_" + getGenePhenotype(Gene.phaeomelanin);
            else if (isDilute())
                smoke += "_" + getGenePhenotype(Gene.dilution);
        }

        String tabby = getGenePhenotype(Gene.tabby) + "_" + solid + (isSilver() ? "_" + getGenePhenotype(Gene.inhibitor) : "");
        if (hasAllele(Gene.spotted, CatAlleles.SPOTTED)) tabby = getGenePhenotype(Gene.spotted) + "_" + tabby;
        if (isTickedTabby())
            tabby = getGenePhenotype(Gene.ticked) + (hasAllele(Gene.ticked, CatAlleles.NON_TICKED) ? "_residual" : "") + "_" + solid + (isSilver() ? "_" + getGenePhenotype(Gene.inhibitor) : "");

        String tortie = "";
        if (isTortoiseshell()) {
            tortie = getGenePhenotype(Gene.phaeomelanin) + "_" + (tabby.replace(("_" + solid), ""));
            if (isDilute()) {
                tortie += getGenePhenotype(Gene.dilution);
                if (isCaramelized())
                    tortie += getGenePhenotype(Gene.dilute_mod);
            }
        }

        if (!isRed() && !isAgouti())
            tabby = "";

        String colorpoint = getGenePhenotype(Gene.colorpoint);
        if (!hasAllele(Gene.colorpoint, CatAlleles.NOT_POINTED)) {
            if (!isAlbino()) {
                if (!tabby.isEmpty() && !isRed())
                    colorpoint += "_" + getGenePhenotype(Gene.agouti);
                else if (solid.equalsIgnoreCase("black"))
                    colorpoint += "_" + getGenePhenotype(Gene.eumelanin);
                else if (isRed())
                    colorpoint += "_" + getGenePhenotype(Gene.phaeomelanin);
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

    public StringTextComponent getPhenotypeDescription(boolean includeSex) {
        ITextComponent sex = (isMale() ? new TranslationTextComponent("cat.sex.male.name") : new TranslationTextComponent("cat.sex.female.name"));

        String eumelanin = getGenePhenotype(Gene.eumelanin);
        String phaeomelanin = getGenePhenotype(Gene.phaeomelanin);
        String dilution = getGenePhenotype(Gene.dilution);
        String diluteMod = getGenePhenotype(Gene.dilute_mod);
        String redElseBlack = isRed() ? phaeomelanin : eumelanin;
        ITextComponent base = new TranslationTextComponent("cat.base." + redElseBlack + ".name");
        if (isDilute()) {
            base = new TranslationTextComponent("cat.base." + redElseBlack + "_" + dilution + ".name");
            if (isCaramelized())
                base = new TranslationTextComponent("cat.base." + redElseBlack + "_" + diluteMod + ".name");
        }

        String inhibitor = getGenePhenotype(Gene.inhibitor);
        if (isSilver())
            base = new StringTextComponent(base.getString() + " " + (new TranslationTextComponent("cat.base." + inhibitor + (isAgouti() ? "" : "_smoke") + ".name")).getString());
        if (isTortoiseshell())
            base = new StringTextComponent(base.getString() + " " + (new TranslationTextComponent("cat.base." + phaeomelanin + ".name")).getString());

        String tabby1 = getGenePhenotype(Gene.tabby);
        String spotted = getGenePhenotype(Gene.spotted);
        String ticked = getGenePhenotype(Gene.ticked);
        ITextComponent tabby = new TranslationTextComponent("");
        if (isAgouti() || isRed()) {
            tabby = new TranslationTextComponent("cat.tabby." + tabby1 + ".name");
            if (hasAllele(Gene.spotted, CatAlleles.SPOTTED))
                tabby = new TranslationTextComponent("cat.tabby." + spotted + ".name");
            if (isTickedTabby())
                tabby = new TranslationTextComponent("cat.tabby." + ticked + ".name");
        }

        String colorpoint = getGenePhenotype(Gene.colorpoint);
        ITextComponent point = new TranslationTextComponent("");
        if (!hasAllele(Gene.colorpoint, CatAlleles.NOT_POINTED)) {
            point = new TranslationTextComponent("cat.point." + colorpoint + ".name");
            if (isAlbino()) {
                ITextComponent eyes = new TranslationTextComponent("cat.point.red_eyed.name");
                if (hasAllele(Gene.colorpoint, CatAlleles.BLUE_EYED_ALBINO))
                    eyes = new TranslationTextComponent("cat.point.blue_eyed.name");
                return new StringTextComponent(eyes.getString() + " " + point.getString() + (includeSex ? (" " + sex.getString()) : ""));
            }
        }

        ITextComponent white = new TranslationTextComponent("");
        if (!isHomozygous(Gene.white, CatAlleles.NO_WHITE)) {
            if (hasAllele(Gene.white, CatAlleles.DOMINANT_WHITE) || getMarking(Marking.white_base) == 6) {
                white = new TranslationTextComponent("cat.white.solid_white.name");
                return new StringTextComponent(white.getString() + (includeSex ? (" " + sex.getString()) : ""));
            }
            if (getMarking(Marking.white_base) == 5) {
                white = new TranslationTextComponent("cat.white.mostly_white.name");
                return new StringTextComponent(white.getString() + " " + base.getString() +
                        (tabby.getString().equals("") ? "" : " " + tabby.getString()) +
                        (point.getString().equals("") ? "" : " " + point.getString()) +
                        (includeSex ? (" " + sex.getString()) : ""));
            } else
                white = new TranslationTextComponent("cat.white.some_white.name");
        }

        return new StringTextComponent(base.getString() +
                (tabby.getString().equals("") ? "" : " " + tabby.getString()) +
                (point.getString().equals("") ? "" : " " + point.getString()) +
                " " + white.getString() + (includeSex ? (" " + sex.getString()) : ""));
    }
}
