package com.github.mnesikos.simplycats.entity.genetics;

import com.github.mnesikos.simplycats.entity.genetics.FelineGenome.Gene;
import com.google.common.collect.ImmutableList;

import java.util.EnumMap;
import java.util.List;
import java.util.Map;

public class CatAlleles {
    // Alleles sorted with wildtype first, then most dominant to least

    // Fur length
    public static final int SHORTHAIR = 0;
    public static final int LONGHAIR = 1;

    // Eumelanin
    public static final int BLACK = 0;
    public static final int CHOCOLATE = 1;
    public static final int CINNAMON = 2;

    // Phaeomelanin
    public static final int X_NOT_RED = 0;
    public static final int Y_CHROMOSOME = 1;
    public static final int X_RED = 2;

    // Dilution
    public static final int NON_DILUTE = 0;
    public static final int DILUTE = 1;

    // Dilute mod
    public static final int NON_CARAMEL = 0;
    public static final int CARAMELIZED = 1;

    // Agouti
    public static final int AGOUTI = 0;
    public static final int NON_AGOUTI = 1;

    // Tabby
    public static final int MACKEREL = 0;
    public static final int CLASSIC = 1;

    // Spotted
    public static final int NON_SPOTTED = 0;
    public static final int SPOTTED = 1;

    // Ticked
    public static final int NON_TICKED = 0;
    public static final int TICKED = 1;

    // Inhibitor
    public static final int NON_SILVER = 0;
    public static final int SILVER = 1;

    // Colorpoint
    public static final int NOT_POINTED = 0;
    public static final int SEPIA = 1;
    public static final int COLORPOINT = 2;
    public static final int BLUE_EYED_ALBINO = 3;
    public static final int RED_EYED_ALBINO = 4;

    // White
    public static final int NO_WHITE = 0;
    public static final int WHITE_SPOTTING = 1;
    public static final int DOMINANT_WHITE = 2;

    // Bobtail
    public static final int FULL_TAIL = 0;
    public static final int BOBTAIL = 1;

    public static final Map<Gene, List<Float>> frequencies = new EnumMap<>(Gene.class);

    static {
        frequencies.put(Gene.fur_length, ImmutableList.of(
                1f
        ));
        frequencies.put(Gene.eumelanin, ImmutableList.of(
                1f
        ));
        frequencies.put(Gene.phaeomelanin, ImmutableList.of(
                0.75f,  // Not red
                1f      // Red
        )); // Don't set Y until finalizeGenes()
        frequencies.put(Gene.dilution, ImmutableList.of(
                0.6f,   // Non dilute
                1f      // Dilute
        ));
        frequencies.put(Gene.dilute_mod, ImmutableList.of(
                0.96f,  // Non caramel
                1f      // Caramelized
        ));
        frequencies.put(Gene.agouti, ImmutableList.of(
                0.2f,   // Agouti
                1f      // Non agouti
        ));
        frequencies.put(Gene.tabby, ImmutableList.of(
                0.5f,   // Mackerel
                1f      // Classic
        ));
        frequencies.put(Gene.spotted, ImmutableList.of(
                0.8f,   // Non spotted
                1f      // Spotted
        ));
        frequencies.put(Gene.ticked, ImmutableList.of(
                0.96f,  // Non ticked
                1f      // Ticked
        ));
        frequencies.put(Gene.inhibitor, ImmutableList.of(
                0.96f,  // Non silver
                1f      // Silver
        ));
        frequencies.put(Gene.colorpoint, ImmutableList.of(
                0.8f,   // Full color
                0.84f,  // Sepia
                0.98f,  // Colorpoint
                0.994f, // Blue-eyed albino
                1f      // Red-eyed albino
        ));
        frequencies.put(Gene.white, ImmutableList.of(
                0.49f,  // No white
                0.98f,  // White spotting
                1f      // Dominant white
        ));
        frequencies.put(Gene.bobtail, ImmutableList.of(
                0.98f,  // Full tail
                1f      // Bobtail
        ));
    }
};
