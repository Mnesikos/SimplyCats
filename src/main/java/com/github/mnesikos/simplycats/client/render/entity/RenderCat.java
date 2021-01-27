package com.github.mnesikos.simplycats.client.render.entity;

import com.github.mnesikos.simplycats.Ref;
import com.github.mnesikos.simplycats.client.model.entity.ModelCat;
import com.github.mnesikos.simplycats.entity.EntityCat;
import com.github.mnesikos.simplycats.entity.core.Genetics;
import com.google.common.collect.Maps;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.EntityRenderer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.entity.RenderLiving;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.texture.LayeredTexture;
import net.minecraft.client.resources.I18n;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.lwjgl.opengl.GL11;

import javax.annotation.Nullable;
import java.util.Map;

@SideOnly(Side.CLIENT)
public class RenderCat extends RenderLiving<EntityCat> {
    private static final Map<String, ResourceLocation> LAYERED_LOCATION_CACHE = Maps.newHashMap();
    private static final String[] CUSTOM_CATS = new String[]{"spinny"};

    public RenderCat(RenderManager render) {
        super(render, new ModelCat(), 0.2F);
    }

    @Override
    protected void preRenderCallback(EntityCat entity, float partialTickTime) {
        GL11.glScalef(0.8F, 0.9F, 0.8F);
        super.preRenderCallback(entity, partialTickTime);
    }

    @Nullable
    @Override
    protected ResourceLocation getEntityTexture(EntityCat entity) {
        if (entity.hasCustomName()) {
            for (String customCat : CUSTOM_CATS) {
                if (entity.getCustomNameTag().equalsIgnoreCase(customCat))
                    return new ResourceLocation(Ref.MODID, "textures/entity/cat/custom/" + customCat + ".png");
            }
        }

        String s = entity.getCatTexture();
        ResourceLocation resourceLocation = LAYERED_LOCATION_CACHE.get(s);

        if (resourceLocation == null) {
            resourceLocation = new ResourceLocation(s);
            Minecraft.getMinecraft().getTextureManager().loadTexture(resourceLocation, new LayeredTexture(entity.getTexturePaths()));
            LAYERED_LOCATION_CACHE.put(s, resourceLocation);
        }

        return resourceLocation;
    }

    @Override
    public void renderName(EntityCat cat, double x, double y, double z) {
        if (this.canRenderName(cat)) {
            double d0 = cat.getDistanceSq(this.renderManager.renderViewEntity);
            float range = cat.isSneaking() ? NAME_TAG_RANGE_SNEAK : NAME_TAG_RANGE;

            if (d0 < (double)(range * range)) {
                String name = cat.getDisplayName().getFormattedText();
                GlStateManager.alphaFunc(516, 0.1F);

                boolean flag = cat.isSneaking();
                float f = this.renderManager.playerViewY;
                float f1 = this.renderManager.playerViewX;
                boolean flag1 = this.renderManager.options.thirdPersonView == 2;
                float f2 = cat.height + 0.5F - (flag ? 0.25F : 0.0F);
                EntityRenderer.drawNameplate(this.getFontRendererFromRenderManager(), name, (float)x, (float)y + f2, (float)z, 0, f, f1, flag1, flag);

                if (d0 < (double)(8 * 8)) {
                    if (this.renderManager.renderViewEntity.isSneaking() && !cat.isFixed() && !cat.isChild()) {
                        String info = I18n.format((cat.getSex() == Genetics.Sex.FEMALE ? (cat.getBreedingStatus("inheat") ? "chat.info.in_heat" : "chat.info.not_in_heat") : "chat.info.male"), cat.getMateTimer());
                        if (cat.getBreedingStatus("ispregnant"))
                            info = I18n.format(cat.getBreedingStatus("inheat") ? "chat.info.pregnant_heat" : "chat.info.pregnant", cat.getMateTimer());
                        Ref.drawNameplateScaled(this.getFontRendererFromRenderManager(), info, (float) x, (float) y + f2 + 0.12F, (float) z, 0, f, f1, flag1, flag, -0.012F);
                    }
                }
            }
        }
    }
}