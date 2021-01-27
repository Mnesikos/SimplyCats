package com.github.mnesikos.simplycats.client.render.entity;

import com.github.mnesikos.simplycats.client.model.entity.ModelCat;
import com.github.mnesikos.simplycats.entity.EntityCat;
import com.github.mnesikos.simplycats.entity.core.Genetics;
import com.google.common.collect.Maps;
import com.mojang.blaze3d.matrix.MatrixStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.Matrix4f;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.client.renderer.texture.LayeredTexture;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import java.util.Map;

@OnlyIn(Dist.CLIENT)
public class RenderCat extends MobRenderer<EntityCat, ModelCat<EntityCat>> {
    private static final Map<String, ResourceLocation> LAYERED_LOCATION_CACHE = Maps.newHashMap();

    public RenderCat(EntityRendererManager render) {
        super(render, new ModelCat<>(), 0.2F);
    }

    @Override
    protected void preRenderCallback(EntityCat entity, MatrixStack matrixStackIn, float partialTickTime) {
        matrixStackIn.scale(0.8F, 0.9F, 0.8F);
        super.preRenderCallback(entity, matrixStackIn, partialTickTime);
    }

    @Override
    public ResourceLocation getEntityTexture(EntityCat entity) {
        String s = entity.getCatTexture();
        ResourceLocation resourceLocation = LAYERED_LOCATION_CACHE.get(s);

        if (resourceLocation == null) {
            resourceLocation = new ResourceLocation(s);
            Minecraft.getInstance().getTextureManager().loadTexture(resourceLocation, new LayeredTexture(entity.getTexturePaths()));
            LAYERED_LOCATION_CACHE.put(s, resourceLocation);
        }

        return resourceLocation;
    }

    @Override
    protected void renderName(EntityCat cat, String displayNameIn, MatrixStack matrixStackIn, IRenderTypeBuffer bufferIn, int packedLightIn) {
        super.renderName(cat, displayNameIn, matrixStackIn, bufferIn, packedLightIn);
        double d0 = this.renderManager.squareDistanceTo(cat);
        if (this.canRenderName(cat) && this.renderManager.pointedEntity.isSneaking() && !cat.isFixed() && !cat.isChild()) {
            if (!(d0 > 4069D)) {
                TranslationTextComponent info = new TranslationTextComponent((cat.getSex() == Genetics.Sex.FEMALE ? (cat.getBreedingStatus("inheat") ? "chat.info.in_heat" : "chat.info.not_in_heat") : "chat.info.male"), cat.getMateTimer());
                if (cat.getBreedingStatus("ispregnant"))
                    info = new TranslationTextComponent(cat.getBreedingStatus("inheat") ? "chat.info.pregnant_heat" : "chat.info.pregnant", cat.getMateTimer());
                boolean flag = !cat.isDiscrete();
                float f = cat.getHeight() + 0.5F - (cat.isSneaking() ? 0.25F : 0.0F);
                matrixStackIn.push();
                matrixStackIn.translate(0, f, 0);
                matrixStackIn.rotate(this.renderManager.getCameraOrientation());
                matrixStackIn.scale(-0.012F, -0.012F, -0.012F);
                Matrix4f matrix4f = matrixStackIn.getLast().getMatrix();
                float f1 = Minecraft.getInstance().gameSettings.getTextBackgroundOpacity(0.25F);
                int j = (int) (f1 * 255) << 24;
                FontRenderer fontRenderer = this.getFontRendererFromRenderManager();
                float width = (float) (-fontRenderer.getStringWidth(displayNameIn) / 2);
                fontRenderer.renderString(String.valueOf(info), width, width + 0.12F, 553648127, false, matrix4f, bufferIn, flag, j, packedLightIn);
                matrixStackIn.pop();
            }
        }
    }
}