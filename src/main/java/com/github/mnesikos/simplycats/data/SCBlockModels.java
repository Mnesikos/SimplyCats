package com.github.mnesikos.simplycats.data;

import com.github.mnesikos.simplycats.SimplyCats;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.client.model.generators.BlockModelProvider;
import net.minecraftforge.common.data.ExistingFileHelper;

public class SCBlockModels extends BlockModelProvider {
    public SCBlockModels(PackOutput output, ExistingFileHelper existingFileHelper) {
        super(output, SimplyCats.MOD_ID, existingFileHelper);
    }

    @Override
    protected void registerModels() {
        pottedCross("potted_catnip", modLoc(BLOCK_FOLDER + "/catnip/potted_catnip"));
    }

    public void pottedCross(String name, ResourceLocation plant) {
        withExistingParent(name, mcLoc(BLOCK_FOLDER + "/flower_pot_cross")).texture("plant", plant);
    }
}
