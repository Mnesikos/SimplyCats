package com.github.mnesikos.simplycats.data;

import com.github.mnesikos.simplycats.SimplyCats;
import com.github.mnesikos.simplycats.block.SCBlocks;
import net.minecraft.data.PackOutput;
import net.minecraft.world.level.block.Block;
import net.neoforged.neoforge.client.model.generators.BlockStateProvider;
import net.neoforged.neoforge.client.model.generators.ConfiguredModel;
import net.neoforged.neoforge.client.model.generators.ModelFile;
import net.neoforged.neoforge.common.data.ExistingFileHelper;
import net.minecraft.core.registries.BuiltInRegistries;

public class SCBlockStates extends BlockStateProvider {
    public SCBlockStates(PackOutput output, ExistingFileHelper exFileHelper) {
        super(output, SimplyCats.MOD_ID, exFileHelper);
    }

    @Override
    protected void registerStatesAndModels() {
        block(SCBlocks.POTTED_CATNIP.get());
    }

    public void block(Block block) {
        ModelFile model = models().getExistingFile(BuiltInRegistries.BLOCK.getKey(block));
        getVariantBuilder(block).partialState().addModels(new ConfiguredModel(model));
    }
}
