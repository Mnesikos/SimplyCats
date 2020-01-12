package com.github.mnesikos.simplycats.entity.core;

import java.util.Random;

public class Genetics {
    public Genetics() {
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
                return SHORT.getAllele(); // 75% chance
            else
                return LONG.getAllele(); // 25% chance
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
                return BLACK.getAllele(); // 80% chance
            else if (chance > 0.80F && chance <= 0.96F)
                return CHOCOLATE.getAllele(); // 16% chance
            else
                return CINNAMON.getAllele(); // 4% chance
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
        Phaeomelanin() {}

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

        public static String getPhenotype(String phaeomelanin) {
            switch (phaeomelanin) {
                case "XO-Y": case "XO-XO":
                    return RED.toString().toLowerCase();
                case "Xo-Y": case "Xo-Xo":
                    return NOT_RED.toString().toLowerCase();
                case "XO-Xo": case "Xo-XO":
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

        public static String init(Random rand) {
            float chance = rand.nextFloat();
            if (chance <= 0.60f)
                return NON_DILUTE.getAllele(); // 60% chance
            else
                return DILUTE.getAllele(); // 40% chance
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
        NORMAL("dm"),
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
                return NORMAL.getAllele(); // 96% chance
            else
                return CARAMELIZED.getAllele(); // 4% chance
        }

        public static String getPhenotype(String diluteMod) {
            String[] value = diluteMod.split("-");
            if (value[0].equals("Dm") || value[1].equals("Dm"))
                return CARAMELIZED.toString().toLowerCase();
            else
                return NORMAL.toString().toLowerCase();
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

        public static String init(Random rand) {
            float chance = rand.nextFloat();
            if (chance <= 0.50f)
                return MACKEREL.getAllele(); // 50% chance
            else
                return CLASSIC.getAllele(); // 50% chance
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
        NORMAL("sp"),
        SPOTTED("Sp"),
        BROKEN;

        private String allele;

        Spotted(String allele) {
            this.allele = allele;
        }
        Spotted() {}

        public String getAllele() {
            return allele;
        }

        public static String init(Random rand) {
            float chance = rand.nextFloat();
            if (chance <= 0.80f)
                return NORMAL.getAllele(); // 80% chance
            else
                return SPOTTED.getAllele(); // 20% chance
        }

        public static String getPhenotype(String spotted) {
            switch (spotted) {
                case "Sp-Sp":
                    return SPOTTED.toString().toLowerCase();
                case "Sp-sp": case "sp-Sp":
                    return BROKEN.toString().toLowerCase();
                case "sp-sp":
                    return NORMAL.toString().toLowerCase();
                default:
                    throw new IllegalArgumentException("Invalid spotted: " + spotted);
            }
        }
    }

    public enum Ticked {
        NORMAL("ta"),
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
                return NORMAL.getAllele(); // 96% chance
            else
                return TICKED.getAllele(); // 4% chance
        }

        public static String getPhenotype(String ticked) {
            String[] value = ticked.split("-");
            if (value[0].equals("Ta") || value[1].equals("Ta"))
                return TICKED.toString().toLowerCase();
            else
                return NORMAL.toString().toLowerCase();
        }
    }

    // bengal modifier

    // silver/smoke

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
        Colorpoint() {}

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

        public static String getPhenotype(String colorpoint) {
            switch (colorpoint) {
                case "C-C": case "C-cs": case "C-cb":
                case "cs-C": case "cb-C":
                    return NOT_POINTED.toString().toLowerCase();
                case "cs-cs":
                    return COLORPOINT.toString().toLowerCase();
                case "cs-cb": case "cb-cs":
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

        public static String init(Random rand) {
            float chance = rand.nextFloat();
            if (chance <= 0.49F)
                return NONE.getAllele(); // 49% chance
            else if (chance > 0.49F && chance <= 0.98F)
                return SPOTTING.getAllele(); // 49% chance
            else
                return DOMINANT.getAllele(); // 2% chance
        }

        public static String getPhenotype(String white) {
            switch (white) {
                case "Wd-Wd": case "Wd-w": case "Wd-Ws":
                case "w-Wd": case "Ws-Wd":
                    return DOMINANT.toString().toLowerCase();
                case "Ws-Ws": case "Ws-w": case "w-Ws":
                    return SPOTTING.toString().toLowerCase();
                case "w-w":
                    return NONE.toString().toLowerCase();
                default:
                    throw new IllegalArgumentException("Invalid white: " + white);
            }
        }
    }
}
