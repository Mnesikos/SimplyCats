package com.github.mnesikos.simplycats;

import com.github.mnesikos.simplycats.entity.core.Genetics;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.datafix.IFixableData;

public class CatDataFixer implements IFixableData {
    @Override
    public int getFixVersion() {
        return 6;
    }

    @Override
    public NBTTagCompound fixTagCompound(NBTTagCompound compound) {
        if (compound.hasKey("id")) {
            String id = compound.getString("id");
            if (id.equals("simplycats:cat")) {
                if (compound.hasKey("AgeTracker") && !compound.hasKey("MatureTimer"))
                    compound.setFloat("MatureTimer", 168000f);
                if (!compound.hasKey("Inhibitor") || compound.getString("Inhibitor").isEmpty())
                    compound.setString("Inhibitor", Genetics.Inhibitor.NORMAL.getAllele() + "-" + Genetics.Inhibitor.init());
            }
        }
        return compound;
    }
}
