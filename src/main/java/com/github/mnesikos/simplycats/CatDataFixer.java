package com.github.mnesikos.simplycats;

import com.github.mnesikos.simplycats.entity.core.Genetics;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.datafix.IFixableData;

public class CatDataFixer implements IFixableData {
    @Override
    public int getFixVersion() {
        return 3;
    }

    @Override
    public NBTTagCompound fixTagCompound(NBTTagCompound compound) {
        if(compound.hasKey("id")) {
            String id = compound.getString("id");
            if(id.equals("simplycats:cat"))
                if (!compound.hasKey("Bobtail") || compound.getString("Bobtail").isEmpty())
                    compound.setString("Bobtail", Genetics.Bobtail.init() + "-" + Genetics.Bobtail.init());
        }
        return compound;
    }
}
