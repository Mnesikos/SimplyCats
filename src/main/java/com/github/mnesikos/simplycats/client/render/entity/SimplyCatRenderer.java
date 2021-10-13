package com.github.mnesikos.simplycats.client.render.entity;

import com.github.mnesikos.simplycats.SCReference;
import com.github.mnesikos.simplycats.SimplyCats;
import com.github.mnesikos.simplycats.client.model.entity.SimplyCatModel;
import com.github.mnesikos.simplycats.entity.SimplyCatEntity;
import com.google.common.collect.Maps;
import com.mojang.blaze3d.matrix.MatrixStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import java.util.Map;

@OnlyIn(Dist.CLIENT)
public class SimplyCatRenderer extends MobRenderer<SimplyCatEntity, SimplyCatModel> {
    private static final Map<String, ResourceLocation> LAYERED_LOCATION_CACHE = Maps.newHashMap();

    public SimplyCatRenderer(EntityRendererManager manager) {
        super(manager, new SimplyCatModel(), 0.4f);
    }

    @Override
    public void render(SimplyCatEntity entity, float v, float v1, MatrixStack matrixStack, IRenderTypeBuffer renderTypeBuffer, int i) {
        this.model.isBobtail = entity.isBobtail();
        this.model.isLongFur = entity.isLongFur();
        this.model.ageScale = entity.getAgeScale();
        super.render(entity, v, v1, matrixStack, renderTypeBuffer, i);
    }

    @Override
    protected void scale(SimplyCatEntity entity, MatrixStack matrixStack, float partialTickTime) {
        matrixStack.scale(0.8F, 0.9F, 0.8F);
        super.scale(entity, matrixStack, partialTickTime);
    }

    @Override
    public ResourceLocation getTextureLocation(SimplyCatEntity entity) {
        if (entity.hasCustomName() && entity.getCustomName() != null) {
            if (entity.getOwnerUUID() != null) {
                String name = SCReference.getCustomCats().get(entity.getOwnerUUID());
                if (name != null && name.equalsIgnoreCase(entity.getCustomName().getString()))
                    return new ResourceLocation(SimplyCats.MOD_ID, "textures/entity/cat/custom/" + name + ".png");
            }
        }

        String s = entity.getCatTexture();
        ResourceLocation resourceLocation = LAYERED_LOCATION_CACHE.get(s);

        if (resourceLocation == null) {
            resourceLocation = new ResourceLocation(s);
            Minecraft.getInstance().getTextureManager().register(resourceLocation, new LayeredTexture(entity.getTexturePaths()));
            LAYERED_LOCATION_CACHE.put(s, resourceLocation);
        }

        return resourceLocation;
    }

    @Override
    protected void renderNameTag(SimplyCatEntity cat, ITextComponent textComponent, MatrixStack matrixStack, IRenderTypeBuffer renderTypeBuffer, int p_225629_5_) { // todo
        super.renderNameTag(cat, textComponent, matrixStack, renderTypeBuffer, p_225629_5_);
    }
}
