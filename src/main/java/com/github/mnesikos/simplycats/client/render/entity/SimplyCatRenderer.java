package com.github.mnesikos.simplycats.client.render.entity;

import com.github.mnesikos.simplycats.SCReference;
import com.github.mnesikos.simplycats.SimplyCats;
import com.github.mnesikos.simplycats.client.model.entity.SimplyCatModel;
import com.github.mnesikos.simplycats.entity.SimplyCatEntity;
import com.github.mnesikos.simplycats.entity.core.Genetics;
import com.google.common.collect.Maps;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.ForgeHooksClient;
import org.joml.Matrix4f;

import java.util.Map;

@OnlyIn(Dist.CLIENT)
public class SimplyCatRenderer extends MobRenderer<SimplyCatEntity, SimplyCatModel<SimplyCatEntity>> {
    private static final Map<String, ResourceLocation> LAYERED_LOCATION_CACHE = Maps.newHashMap();

    public SimplyCatRenderer(EntityRendererProvider.Context context) {
        super(context, new SimplyCatModel<>(context.bakeLayer(SimplyCatModel.LAYER_LOCATION)), 0.4f);
    }

    @Override
    public void render(SimplyCatEntity cat, float v, float v1, PoseStack matrixStack, MultiBufferSource renderTypeBuffer, int i) {
        this.model.isBobtail = cat.isBobtail();
        this.model.isLongFur = cat.isLongFur();
        this.model.ageScale = cat.getAgeScale();
        super.render(cat, v, v1, matrixStack, renderTypeBuffer, i);
    }

    @Override
    protected void scale(SimplyCatEntity entity, PoseStack matrixStack, float partialTickTime) {
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
    protected void renderNameTag(SimplyCatEntity cat, Component textComponent, PoseStack matrixStack, MultiBufferSource renderTypeBuffer, int p_225629_5_) { // todo
        super.renderNameTag(cat, textComponent, matrixStack, renderTypeBuffer, p_225629_5_);

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

            Component info = Component.translatable((cat.getSex() == Genetics.Sex.FEMALE ? (cat.getBreedingStatus("inheat") ? "chat.info.in_heat" : "chat.info.not_in_heat") : "chat.info.male"), cat.getMateTimer());
            if (cat.getBreedingStatus("ispregnant"))
                info = Component.translatable(cat.getBreedingStatus("inheat") ? "chat.info.pregnant_heat" : "chat.info.pregnant", cat.getMateTimer());

            Font fontRenderer = this.getFont();
            float centeredPos = (float) (-fontRenderer.width(info) / 2);

            fontRenderer.drawInBatch(info, centeredPos, 0, 553648127, false, matrix4f, renderTypeBuffer, catNotSneaking ? Font.DisplayMode.SEE_THROUGH : Font.DisplayMode.NORMAL, j, p_225629_5_);
            if (catNotSneaking)
                fontRenderer.drawInBatch(info, centeredPos, 0, -1, false, matrix4f, renderTypeBuffer, Font.DisplayMode.NORMAL, 0, p_225629_5_);

            matrixStack.popPose();
        }
    }
}
