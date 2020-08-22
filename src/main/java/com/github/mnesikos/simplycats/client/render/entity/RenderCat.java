package com.github.mnesikos.simplycats.client.render.entity;

import com.github.mnesikos.simplycats.client.model.entity.ModelCat;
import com.github.mnesikos.simplycats.entity.EntityCat;
import com.google.common.collect.Maps;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.client.renderer.texture.LayeredTexture;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import org.lwjgl.opengl.GL11;

import javax.annotation.Nullable;
import java.util.Map;

@OnlyIn(Dist.CLIENT)
public class RenderCat extends MobRenderer<EntityCat, ModelCat<EntityCat>> {
    private static final Map<String, ResourceLocation> LAYERED_LOCATION_CACHE = Maps.newHashMap();

    public RenderCat(EntityRendererManager render) {
        super(render, new ModelCat<>(), 0.2F);
    }

    @Override
    protected void preRenderCallback(EntityCat entity, float partialTickTime) {
        GL11.glScalef(0.8F, 0.9F, 0.8F);
        super.preRenderCallback(entity, partialTickTime);
    }

    @Nullable
    @Override
    protected ResourceLocation getEntityTexture(EntityCat entity) {
        String s = entity.getCatTexture();
        ResourceLocation resourceLocation = LAYERED_LOCATION_CACHE.get(s);

        if (resourceLocation == null) {
            resourceLocation = new ResourceLocation(s);
            Minecraft.getInstance().getTextureManager().loadTexture(resourceLocation, new LayeredTexture(entity.getTexturePaths()));
            LAYERED_LOCATION_CACHE.put(s, resourceLocation);
        }

        return resourceLocation;
    }
}