package com.github.mnesikos.simplycats.entity.genetics;

import com.github.mnesikos.simplycats.entity.SimplyCatEntity;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public abstract class Genome {
    public abstract List<Enum> listGenes();

    public static Random random = new Random();
    protected SimplyCatEntity entity;
    protected String texturePrefix;
    protected final String[] texturesArray = new String[13];
    protected final String[] markingsTexturesArray = new String[8];

//    public Genome(Random rand) {
//        this(new FakeGeneticEntity(), rand);
//    }

    public Genome(SimplyCatEntity entityIn) {
        this.entity = entityIn;
    }

    public void resetTexturePrefix() {
        this.texturePrefix = null;
    }

    @OnlyIn(Dist.CLIENT)
    public String getTexture() {
        if (this.texturePrefix == null)
            this.setTexturePaths();

        return this.texturePrefix;
    }

    public abstract void setTexturePaths();

    @OnlyIn(Dist.CLIENT)
    public String[] getTexturePaths() {
        if (this.texturePrefix == null) {
            this.setTexturePaths();
        }

        return this.texturesArray;
    }

    public int getMarking(Enum marking) {
        int index = marking.ordinal();
        if (index >= entity.getMarkingsData().length()) {
            return 0;
        }
        return (int) entity.getMarkingsData().charAt(index);
    }

    public void setMarking(Enum marking, int v) {
        int index = marking.ordinal();
        StringBuffer buffer = new StringBuffer(entity.getMarkingsData());
        // Append null characters until it is long enough
        if (buffer.length() <= index) {
            buffer.setLength(index + 1);
        }
        buffer.setCharAt(index, (char) v);
        entity.setMarkingsData(new String(buffer));
    }

    public int getAllele(Enum gene, int n) {
        // Each gene has two alleles, so double the index
        int index = 2 * gene.ordinal() + n;
        if (index >= entity.getGeneData().length()) {
            return 0;
        }
        return (int) entity.getGeneData().charAt(index);
    }

    public void setAllele(Enum gene, int n, int v) {
        int index = 2 * gene.ordinal() + n;
        StringBuffer buffer = new StringBuffer(entity.getGeneData());
        // Append null characters until it is long enough
        if (buffer.length() <= index) {
            buffer.setLength(index + 1);
        }
        buffer.setCharAt(index, (char) v);
        entity.setGeneData(new String(buffer));
    }

    public List<Integer> getAllowedAlleles(Enum gene) {
        List<Float> frequencies = ((FelineGenome.Gene) gene).distribution();
        List<Integer> allowedAlleles = new ArrayList<>();
        float val = 0;
        for (int i = 0; i < frequencies.size(); ++i) {
            if (val >= 1f) {
                break;
            }
            if (val < frequencies.get(i)) {
                allowedAlleles.add(i);
                val = frequencies.get(i);
            }
        }
        return allowedAlleles;
    }

    // Replace the given allele with a random one.
    // It may be the same as before.
    public void mutateAllele(Enum gene, int n) {
        List<Integer> allowedAlleles = getAllowedAlleles(gene);
        if (allowedAlleles == null) {
            return;
        }
        int size = allowedAlleles.size();
        int v = allowedAlleles.get(random.nextInt(size));
        setAllele(gene, n, v);
    }

    // Will mutate with p probability
    public void mutateAlleleChance(Enum gene, int n, double p) {
        if (random.nextDouble() < p) {
            mutateAllele(gene, n);
        }
    }

    public void mutate() {
        double p = 0.02; // todo: mutation chance
        for (Enum gene : listGenes()) {
            mutateAlleleChance(gene, 0, p);
            mutateAlleleChance(gene, 1, p);
        }
    }

    public boolean hasAllele(Enum gene, int allele) {
        return getAllele(gene, 0) == allele || getAllele(gene, 1) == allele;
    }

    public boolean isHomozygous(Enum gene, int allele) {
        return getAllele(gene, 0) == allele && getAllele(gene, 1) == allele;
    }

    public int countAlleles(Enum gene, int allele) {
        int count = 0;
        if (getAllele(gene, 0) == allele) {
            count++;
        }
        if (getAllele(gene, 1) == allele) {
            count++;
        }
        return count;
    }

    public void inheritGenes(Genome parent1, Genome parent2) {
        for (Enum gene : this.listGenes()) {
            int allele1 = parent1.getAllele(gene, random.nextInt(2));
            int allele2 = parent2.getAllele(gene, random.nextInt(2));
            this.setAllele(gene, 0, allele1);
            this.setAllele(gene, 1, allele2);
        }
        mutate();
    }

    // Returns the gene data as a base 64 string of printable characters
    public String getBase64() {
        String genes = entity.getGeneData();
        StringBuilder builder = new StringBuilder(genes.length());
        for (int i = 0; i < genes.length(); ++i) {
            int v = (int) genes.charAt(i);
            builder.append(toBase64(v));
        }
        return builder.toString();
    }

    // Reads the gene data from a base 64 string
    public void setFromBase64(String base64Genes) {
        StringBuilder builder = new StringBuilder(base64Genes.length());
        for (int i = 0; i < base64Genes.length(); ++i) {
            char c = base64Genes.charAt(i);
            int v = fromBase64(c);
            builder.append((char) v);
        }
        entity.setGeneData(builder.toString());
    }

    public static char toBase64(int v) {
        // Doesn't matter since this will be overwritten
        char c = '\0';
        // A-Z
        if (v < 26) {
            c = (char) (v + (int) 'A');
        }
        // a-z
        else if (v < 52) {
            c = (char) (v - 26 + (int) 'a');
        }
        // 0-9
        else if (v < 62) {
            c = (char) (v - 36 + (int) '0');
        } else if (v == 62) {
            c = '+';
        } else if (v == 63) {
            c = '/';
        } else {
            throw new IllegalArgumentException(
                    "Must be at most 63. Found: " + v + "\n");
        }
        return c;
    }

    public static int fromBase64(char c) {
        // Doesn't matter since this will be overwritten
        int v = 0;
        // A-Z
        if (c >= 'A' && c <= 'Z') {
            v = (int) c - (int) 'A';
        }
        // a-z
        else if (c >= 'a' && c <= 'z') {
            v = 26 + (int) c - (int) 'a';
        }
        // 0-9
        else if (c >= '0' && c <= '9') {
            v = 52 + (int) c - (int) '0';
        } else if (c == '+') {
            v = 62;
        } else if (c == '/') {
            v = 63;
        } else {
            throw new IllegalArgumentException(
                    "Must be a valid base 64 character. Found: " + c + "\n");
        }
        return v;
    }
}
