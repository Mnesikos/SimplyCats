package com.github.mnesikos.simplycats.client.render.entity;

import com.github.mnesikos.simplycats.util.Ref;
import com.github.mnesikos.simplycats.client.model.entity.ModelCat;
import com.github.mnesikos.simplycats.entity.EntityCat;
import com.google.common.collect.Lists;
import net.minecraft.client.renderer.entity.RenderLiving;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.lwjgl.opengl.GL11;

import java.util.List;

@SideOnly(Side.CLIENT)
public class RenderCat extends RenderLiving<EntityCat> {
    private static final ResourceLocation MAV = new ResourceLocation(Ref.MODID + ":textures/entity/cat/maverick.png");

    private static final List<ResourceLocation> BASES = Lists.newArrayList();
    protected static final List<ResourceLocation> TABBY = Lists.newArrayList();
    protected static final List<ResourceLocation> TORTIE = Lists.newArrayList();
    protected static final List<ResourceLocation> WHITE = Lists.newArrayList();
    protected static final List<ResourceLocation> EYES = Lists.newArrayList();

    public RenderCat(RenderManager render) {
        super(render, new ModelCat(), 0.2F);
        for (int i = 0; i < 4; i++)
            BASES.add(i, new ResourceLocation(Ref.MODID + ":textures/entity/cat/base/base_" + (i + 1) + ".png"));
        for (int i = 0; i < 4; i++)
            TABBY.add(i, new ResourceLocation(Ref.MODID + ":textures/entity/cat/tabby/mackerel_" + (i + 1) + ".png"));
        for (int i = 0; i < 2; i++)
            TORTIE.add(i, new ResourceLocation(Ref.MODID + ":textures/entity/cat/tabby/tortiemack_" + (i + 1) + ".png"));
        for (int i = 0; i < 6; i++)
            WHITE.add(i, new ResourceLocation(Ref.MODID + ":textures/entity/cat/white/white_" + (i + 1) + ".png"));
        for (int i = 0; i < 5; i++)
            EYES.add(i, new ResourceLocation(Ref.MODID + ":textures/entity/cat/base/eyes_" + (i) + ".png"));
    }

    @Override
    protected void preRenderCallback(EntityCat entity, float partialTickTime) {
        GL11.glScalef(0.8F, 0.9F, 0.8F);
        super.preRenderCallback(entity, partialTickTime);
    }

    @Override
    protected ResourceLocation getEntityTexture(EntityCat entity) {
        ResourceLocation base = BASES.get(entity.getBase()) != null ? BASES.get(entity.getBase()) : BASES.get(0);
        return entity.getType() == 3 ? MAV : getBaseTexture(entity, base);
    }

    private ResourceLocation getBaseTexture(EntityCat cat, ResourceLocation baseLoc) {
        Integer b = cat.getBase();
        Integer t = cat.getMarkingNum("tabby");
        Integer to = cat.getMarkingNum("tortie");
        Integer w = cat.getMarkingNum("white");
        Integer e = cat.getMarkingNum("eyes");
        ResourceLocation newLoc = new ResourceLocation(Ref.MODID + ":textures/entity/cat/temp/" + b + t + to + w + e + ".png");
        TextureHelper.createTexture(cat, baseLoc, newLoc);
        return newLoc;
    }

}