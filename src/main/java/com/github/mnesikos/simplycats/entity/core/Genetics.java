package com.github.mnesikos.simplycats.entity.core;

import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TranslationTextComponent;

import java.util.Random;

public class Genetics {
    public Genetics() {
    }

    public static StringTextComponent getPhenotypeDescription(CompoundNBT nbt, boolean includeSex) {
        ITextComponent sex = Sex.getPrettyName(nbt.getString("Phaeomelanin"));

        String eumelanin = Eumelanin.getPhenotype(nbt.getString("Eumelanin"));
        String phaeomelanin = Phaeomelanin.getPhenotype(nbt.getString("Phaeomelanin"));
        String dilution = Dilution.getPhenotype(nbt.getString("Dilution"));
        String diluteMod = DiluteMod.getPhenotype(nbt.getString("DiluteMod"));
        boolean isRed = phaeomelanin.equals(Phaeomelanin.RED.toString().toLowerCase());
        String redElseBlack = isRed ? phaeomelanin : eumelanin;
        ITextComponent base = new TranslationTextComponent("cat.base." + (redElseBlack) + ".name");
        if (dilution.equals(Dilution.DILUTE.toString().toLowerCase())) {
            base = new TranslationTextComponent("cat.base." + (redElseBlack) + "_" + dilution + ".name");
            if (diluteMod.equals(DiluteMod.CARAMELIZED.toString().toLowerCase()))
                base = new TranslationTextComponent("cat.base." + (redElseBlack) + "_" + diluteMod + ".name");
        }

        String agouti = Agouti.getPhenotype(nbt.getString("Agouti"));
        boolean isAgouti = agouti.equals(Agouti.TABBY.toString().toLowerCase());
        String inhibitor = Inhibitor.getPhenotype(nbt.getString("Inhibitor"));
        if (inhibitor.equals(Inhibitor.SILVER.toString().toLowerCase()))
            base = new StringTextComponent(base.getString() + " " + (new TranslationTextComponent("cat.base." + inhibitor + (isAgouti ? "" : "_smoke") + ".name")).getString());
        if (phaeomelanin.equals(Phaeomelanin.TORTOISESHELL.toString().toLowerCase()))
            base = new StringTextComponent(base.getString() + " " + (new TranslationTextComponent("cat.base." + phaeomelanin + ".name")).getString());

        String tabby1 = Tabby.getPhenotype(nbt.getString("Tabby"));
        String spotted = Spotted.getPhenotype(nbt.getString("Spotted"));
        String ticked = Ticked.getPhenotype(nbt.getString("Ticked"));
        ITextComponent tabby = new TranslationTextComponent("");
        if (isAgouti || isRed) {
            tabby = new TranslationTextComponent("cat.tabby." + tabby1 + ".name");
            if (spotted.equals(Spotted.BROKEN.toString().toLowerCase()) || spotted.equals(Spotted.SPOTTED.toString().toLowerCase()))
                tabby = new TranslationTextComponent("cat.tabby." + spotted + ".name");
            if (ticked.equals(Ticked.TICKED.toString().toLowerCase()))
                tabby = new TranslationTextComponent("cat.tabby." + ticked + ".name");
        }

        String colorpoint = Colorpoint.getPhenotype(nbt.getString("Colorpoint"));
        ITextComponent point = new TranslationTextComponent("");
        if (!colorpoint.equals(Colorpoint.NOT_POINTED.toString().toLowerCase())) {
            point = new TranslationTextComponent("cat.point." + colorpoint + ".name");
        }

        String white = White.getPhenotype(nbt.getString("White"));
        ITextComponent whiteText = new TranslationTextComponent("");
        if (!white.equals(White.NONE.toString().toLowerCase())) {
            if (white.equals(White.DOMINANT.toString().toLowerCase()) || nbt.getString("White_0").contains("6")) {
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

        public static ITextComponent getPrettyName(String phaeomelanin) {
            if (phaeomelanin.contains(Phaeomelanin.MALE.getAllele()))
                return new TranslationTextComponent("cat.sex.male.name");
            else if (!phaeomelanin.contains(Phaeomelanin.MALE.getAllele()))
                return new TranslationTextComponent("cat.sex.female.name");
            else
                return new StringTextComponent(phaeomelanin);
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
                default:
                    return HAZEL.toString().toLowerCase();
                case 3:
                    return GREEN.toString().toLowerCase();
                case 4:
                    return BLUE.toString().toLowerCase();
            }
        }

        public static String getPhenotype(String eyes) {
            switch (eyes) {
                case "copper":
                    return COPPER.toString().toLowerCase();
                case "gold":
                    return GOLD.toString().toLowerCase();
                case "hazel":
                default:
                    return HAZEL.toString().toLowerCase();
                case "green":
                    return GREEN.toString().toLowerCase();
                case "blue":
                    return BLUE.toString().toLowerCase();
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
                return SHORT.getAllele(); // 75% chance
            else
                return LONG.getAllele(); // 25% chance
        }

        public static String getPhenotype(String alleles) {
            if (alleles.isEmpty()) return SHORT.toString().toLowerCase();

            String[] value = alleles.split("-");
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
                return BLACK.getAllele(); // 80% chance
            else if (chance > 0.80F && chance <= 0.96F)
                return CHOCOLATE.getAllele(); // 16% chance
            else
                return CINNAMON.getAllele(); // 4% chance
        }

        public static String getPhenotype(String alleles) {
            if (alleles.isEmpty()) return BLACK.toString().toLowerCase();

            String[] value = alleles.split("-");
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
            String allele1;
            if (chance1 <= 0.75f)
                allele1 = NOT_RED.getAllele(); // 75% chance
            else
                allele1 = RED.getAllele(); // 25% chance
            float chance2 = rand.nextFloat();
            String allele2;
            if (allele1.equals(NOT_RED.getAllele())) {
                if (chance2 <= 0.75f)
                    allele2 = NOT_RED.getAllele(); // 75% chance not red
                else
                    allele2 = RED.getAllele(); // 25% chance tortie
            } else {
                if (chance2 <= 0.75f)
                    allele2 = RED.getAllele(); // 75% chance red
                else
                    allele2 = NOT_RED.getAllele(); // 25% chance tortie
            }
            return allele1 + "-" + (rand.nextInt(2) == 0 ? MALE.getAllele() : allele2);
        }

        public static String getPhenotype(String alleles) {
            switch (alleles) {
                case "XO-Y":
                case "XO-XO":
                    return RED.toString().toLowerCase();
                case "Xo-Y":
                case "Xo-Xo":
                default:
                    return NOT_RED.toString().toLowerCase();
                case "XO-Xo":
                case "Xo-XO":
                    return TORTOISESHELL.toString().toLowerCase();
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

        public static String init(Random rand) {
            float chance = rand.nextFloat();
            if (chance <= 0.60f)
                return NON_DILUTE.getAllele(); // 60% chance
            else
                return DILUTE.getAllele(); // 40% chance
        }

        public static String getPhenotype(String alleles) {
            if (alleles.isEmpty()) return NON_DILUTE.toString().toLowerCase();

            String[] value = alleles.split("-");
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

        public static String init(Random rand) {
            float chance = rand.nextFloat();
            if (chance <= 0.96f)
                return NON_CARAMEL.getAllele(); // 96% chance
            else
                return CARAMELIZED.getAllele(); // 4% chance
        }

        public static String getPhenotype(String alleles) {
            if (alleles.isEmpty()) return NON_CARAMEL.toString().toLowerCase();

            String[] value = alleles.split("-");
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

        public static String init(Random rand) {
            float chance = rand.nextFloat();
            if (chance <= 0.80f)
                return SOLID.getAllele(); // 80% chance
            else
                return TABBY.getAllele(); // 20% chance
        }

        public static String getPhenotype(String alleles) {
            if (alleles.isEmpty()) return SOLID.toString().toLowerCase();

            String[] value = alleles.split("-");
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

        public static String init(Random rand) {
            float chance = rand.nextFloat();
            if (chance <= 0.50f)
                return MACKEREL.getAllele(); // 50% chance
            else
                return CLASSIC.getAllele(); // 50% chance
        }

        public static String getPhenotype(String alleles) {
            if (alleles.isEmpty()) return MACKEREL.toString().toLowerCase();

            String[] value = alleles.split("-");
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

        public static String init(Random rand) {
            float chance = rand.nextFloat();
            if (chance <= 0.80f)
                return NON_SPOTTED.getAllele(); // 80% chance
            else
                return SPOTTED.getAllele(); // 20% chance
        }

        public static String getPhenotype(String alleles) {
            switch (alleles) {
                case "Sp-Sp":
                    return SPOTTED.toString().toLowerCase();
                case "Sp-sp":
                case "sp-Sp":
                    return BROKEN.toString().toLowerCase();
                case "sp-sp":
                default:
                    return NON_SPOTTED.toString().toLowerCase();
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

        public static String init(Random rand) {
            float chance = rand.nextFloat();
            if (chance <= 0.96F)
                return NON_TICKED.getAllele(); // 96% chance
            else
                return TICKED.getAllele(); // 4% chance
        }

        public static String getPhenotype(String alleles) {
            if (alleles.isEmpty()) return NON_TICKED.toString().toLowerCase();

            String[] value = alleles.split("-");
            if (value[0].equals("Ta") || value[1].equals("Ta"))
                return TICKED.toString().toLowerCase();
            else
                return NON_TICKED.toString().toLowerCase();
        }
    }

    // bengal modifier

    public enum Inhibitor {
        NORMAL("i"),
        SILVER("I");

        private String allele;

        Inhibitor(String allele) {
            this.allele = allele;
        }

        public String getAllele() {
            return allele;
        }

        public static String init(Random rand) {
            float chance = rand.nextFloat();
            if (chance <= 0.96f)
                return NORMAL.getAllele(); // 96% chance
            else
                return SILVER.getAllele(); // 4% chance
        }

        public static String getPhenotype(String alleles) {
            if (alleles.isEmpty()) return NORMAL.toString().toLowerCase();

            String[] value = alleles.split("-");
            if (value[0].equals("I") || value[1].equals("I"))
                return SILVER.toString().toLowerCase();
            else
                return NORMAL.toString().toLowerCase();
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

        public static String init(Random rand) {
            float chance = rand.nextFloat();
            if (chance <= 0.80F)
                return NOT_POINTED.getAllele(); // 80% chance
            else if (chance > 0.80F && chance <= 0.96F)
                return COLORPOINT.getAllele(); // 16% chance
            else
                return SEPIA.getAllele(); // 4% chance
        }

        public static String getPhenotype(String alleles) {
            switch (alleles) {
                case "C-C":
                case "C-cs":
                case "C-cb":
                case "cs-C":
                case "cb-C":
                default:
                    return NOT_POINTED.toString().toLowerCase();
                case "cs-cs":
                    return COLORPOINT.toString().toLowerCase();
                case "cs-cb":
                case "cb-cs":
                    return MINK.toString().toLowerCase();
                case "cb-cb":
                    return SEPIA.toString().toLowerCase();
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

        public static String init(Random rand) {
            float chance = rand.nextFloat();
            if (chance <= 0.49F)
                return NONE.getAllele(); // 49% chance
            else if (chance > 0.49F && chance <= 0.98F)
                return SPOTTING.getAllele(); // 49% chance
            else
                return DOMINANT.getAllele(); // 2% chance
        }

        public static String getPhenotype(String alleles) {
            switch (alleles) {
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
                default:
                    return NONE.toString().toLowerCase();
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

        public static String init(Random rand) {
            float chance = rand.nextFloat();
            if (chance <= 0.98F)
                return FULL.getAllele(); // 98% chance
            else
                return BOBTAIL.getAllele(); // 2% chance
        }

        public static boolean isBobtail(String alleles) {
            return !alleles.isEmpty() && alleles.equals("jb-jb");
        }
    }
}
