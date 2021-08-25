package com.github.mnesikos.simplycats;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.datafix.IFixableData;

public class CatDataFixer implements IFixableData {
    @Override
    public int getFixVersion() {
        return 4;
    }

    @Override
    public NBTTagCompound fixTagCompound(NBTTagCompound compound) {
        if (compound.hasKey("id")) {
            String id = compound.getString("id");
            if (id.equals("simplycats:cat")) {
                if (compound.hasKey("Mother") && !compound.getString("Mother").isEmpty()) {
                    compound.setString("Mother", "");
                }
                if (compound.hasKey("Father") && !compound.getString("Father").isEmpty()) {
                    compound.setString("Father", "");
                }
            }
        }
        return compound;
    }
}
