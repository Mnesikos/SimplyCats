package com.github.mnesikos.simplycats.client.renderer.entity;

import com.github.mnesikos.simplycats.entity.EntityCat;
import com.google.common.collect.Maps;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.ModelBase;
import net.minecraft.client.renderer.entity.RenderLiving;
import net.minecraft.client.renderer.texture.LayeredTexture;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.ResourceLocation;

import org.lwjgl.opengl.GL11;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

import java.util.Map;

@SideOnly(Side.CLIENT)
public class RenderCat extends RenderLiving {
	private static final Map<String, ResourceLocation> LAYERED_LOCATION_CACHE = Maps.<String, ResourceLocation>newHashMap();

	public RenderCat(ModelBase modelbase, float shadowsize) {
		super(modelbase, shadowsize);
	}

	@Override
	protected void preRenderCallback(EntityLivingBase entity, float partialTickTime) {
		GL11.glScalef(0.8F, 0.9F, 0.8F);
		super.preRenderCallback(entity, partialTickTime);
	}

	@Override
	protected ResourceLocation getEntityTexture(Entity entity) {
		EntityCat cat = (EntityCat) entity;
		String s = cat.getCatTexture();
		ResourceLocation resourceLocation = LAYERED_LOCATION_CACHE.get(s);

		if (resourceLocation == null) {
			resourceLocation = new ResourceLocation(s);
			Minecraft.getMinecraft().getTextureManager().loadTexture(resourceLocation, new LayeredTexture(cat.getTexturePaths()));
			LAYERED_LOCATION_CACHE.put(s, resourceLocation);
		}

		return resourceLocation;
	}
}