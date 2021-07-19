package com.github.mnesikos.simplycats.entity.core;

import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.text.TranslationTextComponent;

import java.util.Random;

public class Genetics {
    public Genetics() {
    }

    public static String getPhenotypeDescription(CompoundNBT nbt, boolean includeSex) {
        String sex = Sex.getPrettyName(nbt.getString("Phaeomelanin").contains(Phaeomelanin.MALE.getAllele()) ? "male" : "female");

        String eumelanin = Eumelanin.getPhenotype(nbt.getString("Eumelanin"));
        String phaeomelanin = Phaeomelanin.getPhenotype(nbt.getString("Phaeomelanin"));
        String dilution = Dilution.getPhenotype(nbt.getString("Dilution"));
        String diluteMod = DiluteMod.getPhenotype(nbt.getString("DiluteMod"));
        TranslationTextComponent base = new TranslationTextComponent("cat.base." + eumelanin + (phaeomelanin.equals(Phaeomelanin.NOT_RED.toString().toLowerCase()) ? "" : "_" + phaeomelanin) + ".name");
        boolean dilute = dilution.equals(Dilution.DILUTE.toString().toLowerCase());
        boolean caramelized = diluteMod.equals(DiluteMod.CARAMELIZED.toString().toLowerCase());
        if (dilute) {
            base = new TranslationTextComponent("cat.base." + eumelanin + "_" + phaeomelanin + "_" + dilution + ".name");
            if (caramelized)
                base = new TranslationTextComponent("cat.base." + eumelanin + "_" + phaeomelanin + "_" + diluteMod + ".name");
        }
        boolean red = phaeomelanin.equals(Phaeomelanin.RED.toString().toLowerCase());
        if (red) {
            base = new TranslationTextComponent("cat.base." + phaeomelanin + ".name");
            if (dilute) {
                base = new TranslationTextComponent("cat.base." + phaeomelanin + "_" + dilution + ".name");
                if (caramelized)
                    base = new TranslationTextComponent("cat.base." + phaeomelanin + "_" + diluteMod + ".name");
            }
        }

        String agouti = Agouti.getPhenotype(nbt.getString("Agouti"));
        String tabby1 = Tabby.getPhenotype(nbt.getString("Tabby"));
        String spotted = Spotted.getPhenotype(nbt.getString("Spotted"));
        String ticked = Ticked.getPhenotype(nbt.getString("Ticked"));
        TranslationTextComponent tabby = new TranslationTextComponent("");
        if (agouti.equals(Agouti.TABBY.toString().toLowerCase()) || red) {
            tabby = new TranslationTextComponent("cat.tabby." + tabby1 + ".name");
            if (spotted.equals(Spotted.BROKEN.toString().toLowerCase()) || spotted.equals(Spotted.SPOTTED.toString().toLowerCase()))
                tabby = new TranslationTextComponent("cat.tabby." + spotted + ".name");
            if (ticked.equals(Ticked.TICKED.toString().toLowerCase()))
                tabby = new TranslationTextComponent("cat.tabby." + ticked + ".name");
        }

        String colorpoint = Colorpoint.getPhenotype(nbt.getString("Colorpoint"));
        TranslationTextComponent point = new TranslationTextComponent("");
        if (!colorpoint.equals(Colorpoint.NOT_POINTED.toString().toLowerCase())) {
            point = new TranslationTextComponent("cat.point." + colorpoint + ".name");
        }

        String white = White.getPhenotype(nbt.getString("White"));
        TranslationTextComponent whiteText = new TranslationTextComponent("");
        if (!white.equals(White.NONE.toString().toLowerCase())) {
            if (white.equals(White.DOMINANT.toString().toLowerCase()) || nbt.getString("White_0").contains("6")) {
                whiteText = new TranslationTextComponent("cat.white.solid_white.name");
                return whiteText.plainCopy() + (includeSex ? (" " + sex) : "");
            }
            if (nbt.getString("White_0").contains("5")) {
                whiteText = new TranslationTextComponent("cat.white.mostly_white.name");
                return whiteText.plainCopy() + " " + base.plainCopy() +
                        (tabby.plainCopy().equals("") ? "" : " " + tabby.plainCopy()) +
                        (point.plainCopy().equals("") ? "" : " " + point.plainCopy()) +
                        (includeSex ? (" " + sex) : "");
            } else
                whiteText = new TranslationTextComponent("cat.white.some_white.name");
        }

        return base.plainCopy() +
                (tabby.plainCopy().equals("") ? "" : " " + tabby.plainCopy()) +
                (point.plainCopy().equals("") ? "" : " " + point.plainCopy()) +
                " " + whiteText.plainCopy() + (includeSex ? (" " + sex) : "");
    }

    public enum Sex {
        MALE("male"),
        FEMALE("female");

        private String name;

        Sex(String name) {
            this.name = name;
        }

        public String getName() {
            return name;
        }

        public static String getPrettyName(String name) {
            if (name.equalsIgnoreCase(MALE.name))
                return "Male";
            else if (name.equalsIgnoreCase(FEMALE.name))
                return "Female";
            else
                return name;
        }
    }

    public enum EyeColor {
        COPPER,
        GOLD,
        HAZEL,
        GREEN,
        BLUE,
        ODD_LEFT, // todo
        ODD_RIGHT; // am I really gonna do this idek

        EyeColor() {

        }

        public static String init(int value) {
            switch (value) {
                case 0:
                    return COPPER.toString().toLowerCase();
                case 1:
                    return GOLD.toString().toLowerCase();
                case 2:
                    return HAZEL.toString().toLowerCase();
                case 3:
                    return GREEN.toString().toLowerCase();
                case 4:
                    return BLUE.toString().toLowerCase();
                default:
                    throw new IllegalArgumentException("Invalid eye color value: " + value);
            }
        }
    }

    public enum FurLength {
        SHORT("L"),
        LONG("l");

        private String allele;

        FurLength(String allele) {
            this.allele = allele;
        }

        public String getAllele() {
            return allele;
        }

        public static String init(Random rand) {
            float chance = rand.nextFloat();
            if (chance <= 0.75f)
                return String.valueOf(SHORT.ordinal()); // 75% chance
            else
                return String.valueOf(LONG.ordinal()); // 25% chance
        }

        public static String getPhenotype(String furLength) {
            String[] value = furLength.split("-");
            if (value[0].equals("L") || value[1].equals("L"))
                return SHORT.toString().toLowerCase();
            else
                return LONG.toString().toLowerCase();
        }
    }

    public enum Eumelanin {
        BLACK("B"),
        CHOCOLATE("b"),
        CINNAMON("b1");

        private String allele;

        Eumelanin(String allele) {
            this.allele = allele;
        }

        public String getAllele() {
            return allele;
        }

        public static String init(Random rand) {
            float chance = rand.nextFloat();
            if (chance <= 0.80F)
                return String.valueOf(BLACK.ordinal()); // 80% chance
            else if (chance > 0.80F && chance <= 0.96F)
                return String.valueOf(CHOCOLATE.ordinal()); // 16% chance
            else
                return String.valueOf(CINNAMON.ordinal()); // 4% chance
        }

        public static String getPhenotype(String eumelanin) {
            String[] value = eumelanin.split("-");
            if (value[0].equals("B") || value[1].equals("B"))
                return BLACK.toString().toLowerCase();
            else if (value[0].equals("b") || value[1].equals("b"))
                return CHOCOLATE.toString().toLowerCase();
            else
                return CINNAMON.toString().toLowerCase();
        }
    }

    public enum Phaeomelanin {
        NOT_RED("Xo"),
        RED("XO"),
        MALE("Y"),
        TORTOISESHELL;

        private String allele;

        Phaeomelanin(String allele) {
            this.allele = allele;
        }

        Phaeomelanin() {
        }

        public String getAllele() {
            return allele;
        }

        public static String init(Random rand) {
            float chance1 = rand.nextFloat();
            int allele1;
            if (chance1 <= 0.75f)
                allele1 = NOT_RED.ordinal(); // 75% chance
            else
                allele1 = RED.ordinal(); // 25% chance
            float chance2 = rand.nextFloat();
            int allele2;
            if (allele1 == NOT_RED.ordinal()) {
                if (chance2 <= 0.75f)
                    allele2 = NOT_RED.ordinal(); // 75% chance not red
                else
                    allele2 = RED.ordinal(); // 25% chance tortie
            } else {
                if (chance2 <= 0.75f)
                    allele2 = RED.ordinal(); // 75% chance red
                else
                    allele2 = NOT_RED.ordinal(); // 25% chance tortie
            }
            return allele1 + rand.nextInt(2) == 0 ? String.valueOf(MALE.ordinal()) : String.valueOf(allele2); // todo check
        }

        public static String getPhenotype(String phaeomelanin) {
            switch (phaeomelanin) {
                case "XO-Y":
                case "XO-XO":
                    return RED.toString().toLowerCase();
                case "Xo-Y":
                case "Xo-Xo":
                    return NOT_RED.toString().toLowerCase();
                case "XO-Xo":
                case "Xo-XO":
                    return TORTOISESHELL.toString().toLowerCase();
                default:
                    throw new IllegalArgumentException("Invalid phaeomelanin: " + phaeomelanin);
            }
        }
    }

    public enum Dilution {
        NON_DILUTE("D"),
        DILUTE("d");

        private String allele;

        Dilution(String allele) {
            this.allele = allele;
        }

        public String getAllele() {
            return allele;
        }

        public static int init(Random rand) {
            float chance = rand.nextFloat();
            if (chance <= 0.60f)
                return NON_DILUTE.ordinal(); // 60% chance
            else
                return DILUTE.ordinal(); // 40% chance
        }

        public static String getPhenotype(String dilution) {
            String[] value = dilution.split("-");
            if (value[0].equals("D") || value[1].equals("D"))
                return NON_DILUTE.toString().toLowerCase();
            else
                return DILUTE.toString().toLowerCase();
        }
    }

    public enum DiluteMod {
        NON_CARAMEL("dm"),
        CARAMELIZED("Dm");

        private String allele;

        DiluteMod(String allele) {
            this.allele = allele;
        }

        public String getAllele() {
            return allele;
        }

        public static int init(Random rand) {
            float chance = rand.nextFloat();
            if (chance <= 0.96f)
                return NON_CARAMEL.ordinal(); // 96% chance
            else
                return CARAMELIZED.ordinal(); // 4% chance
        }

        public static String getPhenotype(String diluteMod) {
            String[] value = diluteMod.split("-");
            if (value[0].equals("Dm") || value[1].equals("Dm"))
                return CARAMELIZED.toString().toLowerCase();
            else
                return NON_CARAMEL.toString().toLowerCase();
        }
    }

    public enum Agouti {
        SOLID("a"),
        TABBY("A");

        private String allele;

        Agouti(String allele) {
            this.allele = allele;
        }

        public String getAllele() {
            return allele;
        }

        public static int init(Random rand) {
            float chance = rand.nextFloat();
            if (chance <= 0.80f)
                return SOLID.ordinal(); // 80% chance
            else
                return TABBY.ordinal(); // 20% chance
        }

        public static String getPhenotype(String agouti) {
            String[] value = agouti.split("-");
            if (value[0].equals("A") || value[1].equals("A"))
                return TABBY.toString().toLowerCase();
            else
                return SOLID.toString().toLowerCase();
        }
    }

    public enum Tabby {
        MACKEREL("Mc"),
        CLASSIC("mc");

        private String allele;

        Tabby(String allele) {
            this.allele = allele;
        }

        public String getAllele() {
            return allele;
        }

        public static int init(Random rand) {
            float chance = rand.nextFloat();
            if (chance <= 0.50f)
                return MACKEREL.ordinal(); // 50% chance
            else
                return CLASSIC.ordinal(); // 50% chance
        }

        public static String getPhenotype(String tabby) {
            String[] value = tabby.split("-");
            if (value[0].equals("Mc") || value[1].equals("Mc"))
                return MACKEREL.toString().toLowerCase();
            else
                return CLASSIC.toString().toLowerCase();
        }
    }

    public enum Spotted {
        NON_SPOTTED("sp"),
        SPOTTED("Sp"),
        BROKEN;

        private String allele;

        Spotted(String allele) {
            this.allele = allele;
        }

        Spotted() {
        }

        public String getAllele() {
            return allele;
        }

        public static int init(Random rand) {
            float chance = rand.nextFloat();
            if (chance <= 0.80f)
                return NON_SPOTTED.ordinal(); // 80% chance
            else
                return SPOTTED.ordinal(); // 20% chance
        }

        public static String getPhenotype(String spotted) {
            switch (spotted) {
                case "Sp-Sp":
                    return SPOTTED.toString().toLowerCase();
                case "Sp-sp":
                case "sp-Sp":
                    return BROKEN.toString().toLowerCase();
                case "sp-sp":
                    return NON_SPOTTED.toString().toLowerCase();
                default:
                    throw new IllegalArgumentException("Invalid spotted: " + spotted);
            }
        }
    }

    public enum Ticked {
        NON_TICKED("ta"),
        TICKED("Ta");

        private String allele;

        Ticked(String allele) {
            this.allele = allele;
        }

        public String getAllele() {
            return allele;
        }

        public static int init(Random rand) {
            float chance = rand.nextFloat();
            if (chance <= 0.96F)
                return NON_TICKED.ordinal(); // 96% chance
            else
                return TICKED.ordinal(); // 4% chance
        }

        public static String getPhenotype(String ticked) {
            String[] value = ticked.split("-");
            if (value[0].equals("Ta") || value[1].equals("Ta"))
                return TICKED.toString().toLowerCase();
            else
                return NON_TICKED.toString().toLowerCase();
        }
    }

    // bengal modifier

    public enum SilverInhibitor {
        SILVER("I"),
        NON_SILVER("i");

        private String allele;

        SilverInhibitor(String allele) {
            this.allele = allele;
        }

        public String getAllele() {
            return allele;
        }

        public static int init(Random rand) {
            float chance = rand.nextFloat();
            if (chance <= 0.96f)
                return NON_SILVER.ordinal(); // 96% chance
            else
                return SILVER.ordinal(); // 4% chance
        }

        public static String getPhenotype(String diluteMod) {
            String[] value = diluteMod.split("-");
            if (value[0].equals("I") || value[1].equals("I"))
                return SILVER.toString().toLowerCase();
            else
                return NON_SILVER.toString().toLowerCase();
        }
    }

    // wide band

    // amber

    public enum Colorpoint {
        NOT_POINTED("C"),
        COLORPOINT("cs"),
        SEPIA("cb"),
        MINK;

        private String allele;

        Colorpoint(String allele) {
            this.allele = allele;
        }

        Colorpoint() {
        }

        public String getAllele() {
            return allele;
        }

        public static int init(Random rand) {
            float chance = rand.nextFloat();
            if (chance <= 0.80F)
                return NOT_POINTED.ordinal(); // 80% chance
            else if (chance > 0.80F && chance <= 0.96F)
                return COLORPOINT.ordinal(); // 16% chance
            else
                return SEPIA.ordinal(); // 4% chance
        }

        public static String getPhenotype(String colorpoint) {
            switch (colorpoint) {
                case "C-C":
                case "C-cs":
                case "C-cb":
                case "cs-C":
                case "cb-C":
                    return NOT_POINTED.toString().toLowerCase();
                case "cs-cs":
                    return COLORPOINT.toString().toLowerCase();
                case "cs-cb":
                case "cb-cs":
                    return MINK.toString().toLowerCase();
                case "cb-cb":
                    return SEPIA.toString().toLowerCase();
                default:
                    throw new IllegalArgumentException("Invalid colorpoint: " + colorpoint);
            }
        }
    }

    public enum White {
        NONE("w"),
        SPOTTING("Ws"),
        DOMINANT("Wd");

        private String allele;

        White(String allele) {
            this.allele = allele;
        }

        public String getAllele() {
            return allele;
        }

        public static int init(Random rand) {
            float chance = rand.nextFloat();
            if (chance <= 0.49F)
                return NONE.ordinal(); // 49% chance
            else if (chance > 0.49F && chance <= 0.98F)
                return SPOTTING.ordinal(); // 49% chance
            else
                return DOMINANT.ordinal(); // 2% chance
        }

        public static String getPhenotype(String white) {
            switch (white) {
                case "Wd-Wd":
                case "Wd-w":
                case "Wd-Ws":
                case "w-Wd":
                case "Ws-Wd":
                    return DOMINANT.toString().toLowerCase();
                case "Ws-Ws":
                case "Ws-w":
                case "w-Ws":
                    return SPOTTING.toString().toLowerCase();
                case "w-w":
                    return NONE.toString().toLowerCase();
                default:
                    throw new IllegalArgumentException("Invalid white: " + white);
            }
        }
    }

    public enum Bobtail {
        FULL("Jb"),
        BOBTAIL("jb");

        private String allele;

        Bobtail(String allele) {
            this.allele = allele;
        }

        public String getAllele() {
            return allele;
        }

        public static int init(Random rand) {
            float chance = rand.nextFloat();
            if (chance <= 0.98F)
                return FULL.ordinal(); // 98% chance
            else
                return BOBTAIL.ordinal(); // 2% chance
        }

        public static boolean isBobtail(String bobtail) {
            return bobtail.equals("jb-jb");
        }
    }
}
