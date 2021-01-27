package com.github.mnesikos.simplycats.client.model.entity;

import com.github.mnesikos.simplycats.entity.AbstractCat;
import com.github.mnesikos.simplycats.entity.EntityCat;
import com.google.common.collect.ImmutableList;
import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.vertex.IVertexBuilder;
import net.minecraft.client.renderer.entity.model.AgeableModel;
import net.minecraft.client.renderer.model.ModelRenderer;
import net.minecraft.util.math.MathHelper;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class ModelCat<T extends AbstractCat> extends AgeableModel<T> {
    private final ModelRenderer body;
    private final ModelRenderer head;
    private final ModelRenderer tail;
    private final ModelRenderer tailbobbed;
    private final ModelRenderer frightlegpoint;
    private final ModelRenderer fleftlegpoint;
    private final ModelRenderer brightlegpoint;
    private final ModelRenderer bleftlegpoint;
    private final ModelRenderer nose;
    private final ModelRenderer lear;
    private final ModelRenderer rear;
    private final ModelRenderer whiskers;
    private final ModelRenderer tail2;
    private final ModelRenderer frightleg;
    private final ModelRenderer fleftleg;
    private final ModelRenderer brightleg;
    private final ModelRenderer bleftleg;
//    public boolean isBobtail = false;

    public ModelCat() {
        this.textureWidth = 64;
        this.textureHeight = 32;

        this.head = new ModelRenderer(this, 0, 0);
        this.head.setRotationPoint(0.0F, 14.0F, -6.5F);
        this.head.addBox(-2.5F, -2.0F, -5.0F, 5, 4, 5, 0.0F);
        this.nose = new ModelRenderer(this, 21, 0);
        this.nose.setRotationPoint(0.0F, 1.01F, -4.5F);
        this.nose.addBox(-1.5F, -1.0F, -2.0F, 3, 2, 2, 0.0F);
        this.whiskers = new ModelRenderer(this, 2, 9);
        this.whiskers.setRotationPoint(0.0F, -0.1F, -1.2F);
        this.whiskers.addBox(-4.0F, -1.0F, -0.5F, 8, 2, 0, 0.0F);
        this.lear = new ModelRenderer(this, 26, 9);
        this.lear.setRotationPoint(1.6F, -1.5F, -0.8F);
        this.lear.addBox(-1.0F, -2.0F, -1.0F, 2, 3, 2, 0.0F);
        this.rear = new ModelRenderer(this, 18, 9);
        this.rear.setRotationPoint(-1.6F, -1.5F, -0.8F);
        this.rear.addBox(-1.0F, -2.0F, -1.0F, 2, 3, 2, 0.0F);
        this.body = new ModelRenderer(this, 20, 0);
        this.body.setRotationPoint(0.0F, 18.0F, -5.0F);
        this.body.addBox(-2.5F, -4.0F, -1.5F, 5, 5, 15, 0.0F);
        this.fleftlegpoint = new ModelRenderer(this, 1, 11);
        this.fleftlegpoint.setRotationPoint(2.2F, -0.5F, -0.7F);
        this.fleftlegpoint.addBox(-1.5F, 0.0F, -1.0F, 0, 0, 0, 0.0F);
        this.fleftleg = new ModelRenderer(this, 1, 11);
        this.fleftleg.setRotationPoint(0.0F, -1.5F, 0.0F);
        this.fleftleg.addBox(-1.5F, 0.0F, -1.0F, 2, 8, 2, 0.0F);
        this.frightlegpoint = new ModelRenderer(this, 9, 11);
        this.frightlegpoint.setRotationPoint(-1.2F, -0.5F, -0.7F);
        this.frightlegpoint.addBox(-1.5F, 0.0F, -1.0F, 0, 0, 0, 0.0F);
        this.frightleg = new ModelRenderer(this, 9, 11);
        this.frightleg.setRotationPoint(0.0F, -1.5F, 0.0F);
        this.frightleg.addBox(-1.5F, 0.0F, -1.0F, 2, 8, 2, 0.0F);
        this.bleftlegpoint = new ModelRenderer(this, 1, 22);
        this.bleftlegpoint.setRotationPoint(1.7F, -0.5F, 11.5F);
        this.bleftlegpoint.addBox(-1.0F, -1.5F, -1.0F, 0, 0, 0, 0.0F);
        this.bleftleg = new ModelRenderer(this, 1, 22);
        this.bleftleg.setRotationPoint(0.0F, -1.5F, 0.0F);
        this.bleftleg.addBox(-1.0F, 0.0F, -1.0F, 2, 8, 2, 0.0F);
        this.brightlegpoint = new ModelRenderer(this, 9, 22);
        this.brightlegpoint.setRotationPoint(-1.7F, -0.5F, 11.5F);
        this.brightlegpoint.addBox(-1.0F, -1.5F, -1.0F, 0, 0, 0, 0.0F);
        this.brightleg = new ModelRenderer(this, 9, 22);
        this.brightleg.setRotationPoint(0.0F, -1.5F, 0.0F);
        this.brightleg.addBox(-1.0F, 0.0F, -1.0F, 2, 8, 2, 0.0F);
        this.tail = new ModelRenderer(this, 20, 22);
        this.tail.setRotationPoint(0.0F, 15.0F, 7.6F);
        this.tail.addBox(-1.0F, 0.0F, -1.0F, 2, 8, 2, 0.0F);
        this.setRotateAngle(tail, (float) (180 / (180 / Math.PI)), -0.0F, 0.0F);
        this.tailbobbed = new ModelRenderer(this, 20, 22);
        this.tailbobbed.setRotationPoint(0.0F, 15.0F, 7.6F);
        this.tailbobbed.addBox(-1.0F, 0.0F, -1.0F, 2, 2, 2, 0.0F);
        this.setRotateAngle(tailbobbed, (float) (135 / (180 / Math.PI)), -0.0F, 0.0F);
        this.tail2 = new ModelRenderer(this, 28, 22);
        this.tail2.setRotationPoint(0.0F, 7.8F, 0.0F);
        this.tail2.addBox(-1.0F, 0.0F, -1.0F, 2, 3, 2, 0.0F);
        this.setRotateAngle(tail2, (float) (10 / (180 / Math.PI)), -0.0F, 0.0F);

        this.head.addChild(this.nose); this.nose.addChild(this.whiskers);
        this.head.addChild(this.lear);
        this.head.addChild(this.rear);

        this.body.addChild(this.fleftlegpoint); this.fleftlegpoint.addChild(this.fleftleg);
        this.body.addChild(this.frightlegpoint); this.frightlegpoint.addChild(this.frightleg);
        this.body.addChild(this.bleftlegpoint); this.bleftlegpoint.addChild(this.bleftleg);
        this.body.addChild(this.brightlegpoint); this.brightlegpoint.addChild(this.brightleg);

        this.tail.addChild(this.tail2);
    }

    @Override
    protected Iterable<ModelRenderer> getHeadParts() {
        return ImmutableList.of(this.head);
    }

    @Override
    protected Iterable<ModelRenderer> getBodyParts() {
        return ImmutableList.of(this.body);
    }

    @Override
    public void render(MatrixStack matrixStackIn, IVertexBuilder bufferIn, int packedLightIn, int packedOverlayIn, float red, float green, float blue, float alpha) {
        float scale = 0.625F;
        if (this.isChild) {
            float var8 = 2.0F;
            matrixStackIn.push();
            matrixStackIn.scale(1.25F / var8, 1.25F / var8, 1.25F / var8);
            matrixStackIn.translate(0.0F, 15.6F * scale, 2.5F * scale);
            this.head.render(matrixStackIn, bufferIn, packedLightIn, packedOverlayIn, red, green, blue, alpha);
            matrixStackIn.pop();

            matrixStackIn.push();
            matrixStackIn.scale(0.7F / var8, 0.7F / var8, 0.7F / var8);
            matrixStackIn.translate(0.0F, 41.0F * scale, 1.4F * scale);
            /*if (this.isBobtail) this.tailbobbed.render(matrixStackIn, bufferIn, packedLightIn, packedOverlayIn, red, green, blue, alpha);
            else */this.tail.render(matrixStackIn, bufferIn, packedLightIn, packedOverlayIn, red, green, blue, alpha);
            matrixStackIn.pop();

            matrixStackIn.push();
            matrixStackIn.scale(1.0F / var8, 1.0F / var8, 0.8F / var8);
            matrixStackIn.translate(0.0F, 24.0F * scale, 0.0F);
            this.body.render(matrixStackIn, bufferIn, packedLightIn, packedOverlayIn, red, green, blue, alpha);
            matrixStackIn.pop();

        } else {
            matrixStackIn.push();
            matrixStackIn.scale(1.01F, 1.01F, 1.01F); // scaling head slightly larger for no texture clip hopefully
            this.head.render(matrixStackIn, bufferIn, packedLightIn, packedOverlayIn, red, green, blue, alpha);
            matrixStackIn.pop();
            this.body.render(matrixStackIn, bufferIn, packedLightIn, packedOverlayIn, red, green, blue, alpha);
            /*if (this.isBobtail) this.tailbobbed.render(matrixStackIn, bufferIn, packedLightIn, packedOverlayIn, red, green, blue, alpha);
            else */this.tail.render(matrixStackIn, bufferIn, packedLightIn, packedOverlayIn, red, green, blue, alpha);
        }
    }

    @Override
    public void setRotationAngles(T entityIn, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
        this.head.rotateAngleX = headPitch / (180F / (float)Math.PI);
        this.head.rotateAngleY = netHeadYaw / (180F / (float)Math.PI);
    }

    @Override
    public void setLivingAnimations(T entity, float parSpeed, float parWalkSpeed, float f4) {
        if (entity instanceof EntityCat) {
            EntityCat cat = (EntityCat) entity;
//            this.isBobtail = cat.isBobtail();
            ModelRenderer tailType = /*cat.isBobtail() ? tailbobbed :*/ tail;

            this.head.rotationPointY = 14.0F;
            this.head.rotationPointZ = -6.5F;
            this.body.rotateAngleX = 0.0F;
            this.body.rotationPointY = 18.0F;
            this.fleftlegpoint.rotateAngleX = this.frightlegpoint.rotateAngleX = 0.0F;
            this.bleftlegpoint.rotateAngleX = this.brightlegpoint.rotateAngleX = 0.0F;
            this.bleftlegpoint.rotationPointY = this.brightlegpoint.rotationPointY = -0.5F;
            this.fleftlegpoint.rotationPointY = this.frightlegpoint.rotationPointY = -0.5F;
            this.fleftleg.rotateAngleX = MathHelper.cos(parSpeed * 0.6662F) * 0.5F * parWalkSpeed;
            this.brightleg.rotateAngleX = MathHelper.cos(parSpeed * 0.6662F + 1.5F) * 0.5F * parWalkSpeed;
            this.frightleg.rotateAngleX = MathHelper.cos(parSpeed * 0.6662F + 3.0F) * 0.5F * parWalkSpeed;
            this.bleftleg.rotateAngleX = MathHelper.cos(parSpeed * 0.6662F + 4.5F) * 0.5F * parWalkSpeed;
            tailType.rotationPointY = 15.0F;
            this.tail.rotateAngleX = (float) (180 / (180 / Math.PI));
            this.tail2.rotateAngleX = (float) (10 / (180 / Math.PI));
            this.tailbobbed.rotateAngleX = (float) (135 / (180 / Math.PI));
            this.lear.rotateAngleX = 0.0F;
            this.lear.rotateAngleY = 0.0F;
            this.rear.rotateAngleX = 0.0F;
            this.rear.rotateAngleY = 0.0F;

            if (cat.isAngry() || cat.isSneaking()) {
                this.lear.rotateAngleX = (float) (67 / (180 / Math.PI));
                this.lear.rotateAngleY = (float) (-145 / (180 / Math.PI));
                this.rear.rotateAngleX = (float) (67 / (180 / Math.PI));
                this.rear.rotateAngleY = (float) (145 / (180 / Math.PI));
            }

            if (cat.isSneaking()) {
                this.head.rotationPointY += 2.5F;
                this.body.rotationPointY += 2.0F;
                this.bleftlegpoint.rotationPointY -= 2.0F;
                this.brightlegpoint.rotationPointY -= 2.0F;
                this.fleftlegpoint.rotationPointY -= 2.0F;
                this.frightlegpoint.rotationPointY -= 2.0F;
                tailType.rotationPointY += 2.0F;
                tailType.rotateAngleX = ((float) Math.PI / 3F);
            }

            if (cat.isSitting()) {
                if (this.isChild) {
                    tailType.rotationPointY = 23.5F;
                } else {
                    tailType.rotationPointY = 21.5F;
                }
                this.head.rotationPointZ = -4.5F;
                this.body.rotateAngleX = (float) (-28 / (180 / Math.PI));
                this.fleftleg.rotateAngleX = this.frightleg.rotateAngleX = (float) (28 / (180 / Math.PI));
                this.bleftlegpoint.rotateAngleX = this.brightlegpoint.rotateAngleX = (float) (-62.5 / (180 / Math.PI));
                this.bleftleg.rotateAngleX = this.brightleg.rotateAngleX = 0.0F;
                tailType.rotateAngleX = (float) (79 / (180 / Math.PI));
            }
        }
    }

    private void setRotateAngle(ModelRenderer modelRenderer, float x, float y, float z) {
        modelRenderer.rotateAngleX = x;
        modelRenderer.rotateAngleY = y;
        modelRenderer.rotateAngleZ = z;
    }
}
