package com.github.mnesikos.simplycats.client.render.entity;

import com.github.mnesikos.simplycats.SimplyCats;
import com.github.mnesikos.simplycats.client.model.entity.SimplyCatModel;
import com.github.mnesikos.simplycats.entity.SimplyCatEntity;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class SimplyCatRenderer extends MobRenderer<SimplyCatEntity, SimplyCatModel> {
    private static final ResourceLocation TEXTURE = new ResourceLocation(SimplyCats.MOD_ID, "textures/entity/cat/custom/penny.png");

    public SimplyCatRenderer(EntityRendererManager manager) {
        super(manager, new SimplyCatModel(), 0.5f);
    }

    @Override
    public ResourceLocation getTextureLocation(SimplyCatEntity entity) {
        return TEXTURE;
    }
}
