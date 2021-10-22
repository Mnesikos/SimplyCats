package com.github.mnesikos.simplycats.client.render.entity;

import com.github.mnesikos.simplycats.SCReference;
import com.github.mnesikos.simplycats.SimplyCats;
import com.github.mnesikos.simplycats.client.model.entity.SimplyCatModel;
import com.github.mnesikos.simplycats.entity.SimplyCatEntity;
import com.github.mnesikos.simplycats.entity.core.Genetics;
import com.google.common.collect.Maps;
import com.mojang.blaze3d.matrix.MatrixStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.vector.Matrix4f;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.ForgeHooksClient;

import java.util.Map;

@OnlyIn(Dist.CLIENT)
public class SimplyCatRenderer extends MobRenderer<SimplyCatEntity, SimplyCatModel<SimplyCatEntity>> {
    private static final Map<String, ResourceLocation> LAYERED_LOCATION_CACHE = Maps.newHashMap();

    public SimplyCatRenderer(EntityRendererManager manager) {
        super(manager, new SimplyCatModel<>(), 0.4f);
    }

    @Override
    public void render(SimplyCatEntity cat, float v, float v1, MatrixStack matrixStack, IRenderTypeBuffer renderTypeBuffer, int i) {
        this.model.isBobtail = cat.isBobtail();
        this.model.isLongFur = cat.isLongFur();
        this.model.ageScale = cat.getAgeScale();

        double distance = this.entityRenderDispatcher.distanceToSqr(cat);
        if (ForgeHooksClient.isNameplateInRenderDistance(cat, distance) && this.entityRenderDispatcher.camera.getEntity().isShiftKeyDown() && !cat.isFixed() && !cat.isBaby()) {
            boolean catNotSneaking = !cat.isDiscrete();
            float height = cat.getBbHeight() + 0.62F;

            matrixStack.pushPose();
            matrixStack.translate(0.0D, height, 0.0D);
            matrixStack.mulPose(this.entityRenderDispatcher.cameraOrientation());
            matrixStack.scale(-0.012F, -0.012F, 0.012F);
            Matrix4f matrix4f = matrixStack.last().pose();

            float backgroundOpacity = Minecraft.getInstance().options.getBackgroundOpacity(0.25F);
            int j = (int) (backgroundOpacity * 255.0F) << 24;

            TranslationTextComponent info = new TranslationTextComponent((cat.getSex() == Genetics.Sex.FEMALE ? (cat.getBreedingStatus("inheat") ? "chat.info.in_heat" : "chat.info.not_in_heat") : "chat.info.male"), cat.getMateTimer());
            if (cat.getBreedingStatus("ispregnant"))
                info = new TranslationTextComponent(cat.getBreedingStatus("inheat") ? "chat.info.pregnant_heat" : "chat.info.pregnant", cat.getMateTimer());

            FontRenderer fontRenderer = this.getFont();
            float centeredPos = (float) (-fontRenderer.width(info) / 2);

            fontRenderer.drawInBatch(info, centeredPos, 0, 553648127, false, matrix4f, renderTypeBuffer, catNotSneaking, j, i);
            if (catNotSneaking)
                fontRenderer.drawInBatch(info, centeredPos, 0, -1, false, matrix4f, renderTypeBuffer, false, 0, i);

            matrixStack.popPose();
        }

        super.render(cat, v, v1, matrixStack, renderTypeBuffer, i);
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
