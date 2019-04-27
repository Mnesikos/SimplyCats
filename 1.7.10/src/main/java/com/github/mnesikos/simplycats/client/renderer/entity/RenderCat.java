package com.github.mnesikos.simplycats.client.renderer.entity;

import java.util.List;

import com.github.mnesikos.simplycats.SimplyCats;
import com.github.mnesikos.simplycats.entity.EntityCat;
import net.minecraft.client.model.ModelBase;
import net.minecraft.client.renderer.entity.RenderLiving;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.ResourceLocation;

import org.lwjgl.opengl.GL11;

import com.google.common.collect.Lists;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class RenderCat extends RenderLiving {
	private static final ResourceLocation MAV = new ResourceLocation(SimplyCats.MODID + ":textures/entity/cat/maverick.png");

	private static final List<ResourceLocation> BASES = Lists.newArrayList();
	protected static final List<ResourceLocation> TABBY = Lists.newArrayList();
	protected static final List<ResourceLocation> WHITE = Lists.newArrayList();
	protected static final List<ResourceLocation> EYES = Lists.newArrayList();

	public RenderCat(ModelBase modelbase, float shadowsize) {
		super(modelbase, shadowsize);
		for (int i = 0; i < 4; i++)
			BASES.add(i, new ResourceLocation(SimplyCats.MODID + ":textures/entity/cat/base/base_" + (i + 1) + ".png"));
		for (int i = 0; i < 4; i++)
			TABBY.add(i, new ResourceLocation(SimplyCats.MODID + ":textures/entity/cat/tabby/mackerel_" + (i + 1) + ".png"));
		for (int i = 0; i < 6; i++)
			WHITE.add(i, new ResourceLocation(SimplyCats.MODID + ":textures/entity/cat/white/white_" + (i + 1) + ".png"));
		for (int i = 0; i < 5; i++)
			EYES.add(i, new ResourceLocation(SimplyCats.MODID + ":textures/entity/cat/base/eyes_" + (i) + ".png"));
	}

	@Override
	protected void preRenderCallback(EntityLivingBase entity, float partialTickTime) {
		GL11.glScalef(0.8F, 0.9F, 0.8F);
		super.preRenderCallback(entity, partialTickTime);
	}

	@Override
	protected ResourceLocation getEntityTexture(Entity entity) {
		EntityCat cat = (EntityCat) entity;
		ResourceLocation base = BASES.get(cat.getBase()) != null ? BASES.get(cat.getBase()) : BASES.get(0);
		return cat.getType() == 3 ? MAV : getBaseTexture(cat, base);
	}

	private ResourceLocation getBaseTexture(EntityCat cat, ResourceLocation baseLoc) {
		Integer b = cat.getBase();
		Integer t = cat.getMarkingNum("tabby");
		Integer w = cat.getMarkingNum("white");
		Integer e = cat.getMarkingNum("eyes");
		ResourceLocation newLoc = new ResourceLocation(SimplyCats.MODID + ":textures/entity/cat/temp/" + b + t + w + e + ".png");
		TextureHelper.createTexture(cat, baseLoc, newLoc);
		return newLoc;
	}

}