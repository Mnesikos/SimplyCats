package com.github.mnesikos.simplycats.client.model.entity;

import com.github.mnesikos.simplycats.configuration.SCConfig;
import com.github.mnesikos.simplycats.entity.EntityCat;
import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.math.MathHelper;

public class ModelCat extends ModelBase {
    private final ModelRenderer body1;
    private final ModelRenderer body2;
    private final ModelRenderer head1;
    private final ModelRenderer head2;
    private final ModelRenderer tail1;
    private final ModelRenderer tailBobbed;
    private final ModelRenderer frontRightLegPoint;
    private final ModelRenderer frontLeftLegPoint;
    private final ModelRenderer backRightLegPoint;
    private final ModelRenderer backLeftLegPoint;
    private final ModelRenderer nose1;
    private final ModelRenderer earLeft1;
    private final ModelRenderer earRight1;
    private final ModelRenderer whiskers1;
    private final ModelRenderer nose2;
    private final ModelRenderer earLeft2;
    private final ModelRenderer earRight2;
    private final ModelRenderer earLeftTuft;
    private final ModelRenderer earRightTuft;
    private final ModelRenderer whiskers2;
    private final ModelRenderer faceTuftLeft;
    private final ModelRenderer faceTuftRight;
    private final ModelRenderer tail2;
    private final ModelRenderer frontRightLeg;
    private final ModelRenderer frontLeftLeg;
    private final ModelRenderer backRightLeg;
    private final ModelRenderer backLeftLeg;

    public ModelCat() {
        this.textureWidth = 64;
        this.textureHeight = 32;
        this.head1 = new ModelRenderer(this, 0, 0);
        this.head1.setRotationPoint(0.0F, 14.0F, -6.5F);
        this.head1.addBox(-2.5F, -2.0F, -5.0F, 5, 4, 5, 0.0F);
        this.nose1 = new ModelRenderer(this, 21, 0);
        this.nose1.setRotationPoint(0.0F, 1.01F, -4.5F);
        this.nose1.addBox(-1.5F, -1.0F, -2.0F, 3, 2, 2, 0.0F);
        this.whiskers1 = new ModelRenderer(this, 2, 9);
        this.whiskers1.setRotationPoint(0.0F, -0.1F, -1.2F);
        this.whiskers1.addBox(-4.0F, -1.0F, -0.5F, 8, 2, 0, 0.0F);
        this.earLeft1 = new ModelRenderer(this, 26, 9);
        this.earLeft1.setRotationPoint(1.6F, -1.5F, -0.8F);
        this.earLeft1.addBox(-1.0F, -2.0F, -1.0F, 2, 3, 2, 0.0F);
        this.earRight1 = new ModelRenderer(this, 18, 9);
        this.earRight1.setRotationPoint(-1.6F, -1.5F, -0.8F);
        this.earRight1.addBox(-1.0F, -2.0F, -1.0F, 2, 3, 2, 0.0F);
        this.head2 = new ModelRenderer(this, 0, 0);
        this.head2.setRotationPoint(0.0F, 14.0F, -6.5F);
        this.head2.addBox(-2.5F, -2.0F, -5.0F, 5, 4, 5, 0.0F);
        this.nose2 = new ModelRenderer(this, 21, 0);
        this.nose2.setRotationPoint(0.0F, 1.01F, -4.5F);
        this.nose2.addBox(-1.5F, -1.0F, -2.0F, 3, 2, 2, 0.0F);
        this.whiskers2 = new ModelRenderer(this, 2, 9);
        this.whiskers2.setRotationPoint(0.0F, -0.1F, -1.2F);
        this.whiskers2.addBox(-4.0F, -1.0F, -0.5F, 8, 2, 0, 0.0F);
        this.earLeft2 = new ModelRenderer(this, 26, 9);
        this.earLeft2.setRotationPoint(1.6F, -1.5F, -0.8F);
        this.earLeft2.addBox(-1.0F, -2.0F, -1.0F, 2, 3, 2, 0.0F);
        this.earRight2 = new ModelRenderer(this, 18, 9);
        this.earRight2.setRotationPoint(-1.6F, -1.5F, -0.8F);
        this.earRight2.addBox(-1.0F, -2.0F, -1.0F, 2, 3, 2, 0.0F);
        this.earLeftTuft = new ModelRenderer(this, 27, 9);
        this.earLeftTuft.setRotationPoint(0.0F, -1.5F, 0.0F);
        this.earLeftTuft.addBox(-0.5F, -1.0F, 0.0F, 1, 1, 0, 0.0F);
        this.earRightTuft = new ModelRenderer(this, 19, 9);
        this.earRightTuft.setRotationPoint(0.0F, -1.5F, 0.0F);
        this.earRightTuft.addBox(-0.5F, -1.0F, 0.0F, 1, 1, 0, 0.0F);
        this.faceTuftLeft = new ModelRenderer(this, 7, 3);
        this.faceTuftLeft.setRotationPoint(1.9F, -0.8F, -3.0F);
        this.faceTuftLeft.addBox(0.0F, 0.0F, -1.5F, 1, 3, 3, 0.0F);
        this.setRotateAngle(faceTuftLeft, 0.0F, 0.0F, -0.13962634015954636F);
        this.faceTuftRight = new ModelRenderer(this, 7, 3);
        this.faceTuftRight.setRotationPoint(-1.9F, -0.8F, -3.0F);
        this.faceTuftRight.addBox(-1.0F, 0.0F, -1.5F, 1, 3, 3, true);
        this.setRotateAngle(faceTuftRight, 0.0F, 0.0F, 0.13962634015954636F);
        this.body1 = new ModelRenderer(this, 20, 0);
        this.body1.setRotationPoint(0.0F, 18.0F, -5.0F);
        this.body1.addBox(-2.5F, -4.0F, -1.5F, 5, 5, 15, 0.0F);
        this.body2 = new ModelRenderer(this, 20, 0);
        this.body2.setRotationPoint(0.0F, 18.0F, -5.0F);
        this.body2.addBox(-2.5F, -3.5F, -1.6F, 5, 5, 15, 0.0F);
        this.frontLeftLegPoint = new ModelRenderer(this, 1, 11);
        this.frontLeftLegPoint.setRotationPoint(2.2F, -0.5F, -0.7F);
        this.frontLeftLegPoint.addBox(-1.5F, 0.0F, -1.0F, 0, 0, 0, 0.0F);
        this.frontLeftLeg = new ModelRenderer(this, 1, 11);
        this.frontLeftLeg.setRotationPoint(0.0F, -1.5F, 0.0F);
        this.frontLeftLeg.addBox(-1.5F, 0.0F, -1.0F, 2, 8, 2, 0.0F);
        this.frontRightLegPoint = new ModelRenderer(this, 9, 11);
        this.frontRightLegPoint.setRotationPoint(-1.2F, -0.5F, -0.7F);
        this.frontRightLegPoint.addBox(-1.5F, 0.0F, -1.0F, 0, 0, 0, 0.0F);
        this.frontRightLeg = new ModelRenderer(this, 9, 11);
        this.frontRightLeg.setRotationPoint(0.0F, -1.5F, 0.0F);
        this.frontRightLeg.addBox(-1.5F, 0.0F, -1.0F, 2, 8, 2, 0.0F);
        this.backLeftLegPoint = new ModelRenderer(this, 1, 22);
        this.backLeftLegPoint.setRotationPoint(1.7F, -0.5F, 11.5F);
        this.backLeftLegPoint.addBox(-1.0F, -1.5F, -1.0F, 0, 0, 0, 0.0F);
        this.backLeftLeg = new ModelRenderer(this, 1, 22);
        this.backLeftLeg.setRotationPoint(0.0F, -1.5F, 0.0F);
        this.backLeftLeg.addBox(-1.0F, 0.0F, -1.0F, 2, 8, 2, 0.0F);
        this.backRightLegPoint = new ModelRenderer(this, 9, 22);
        this.backRightLegPoint.setRotationPoint(-1.7F, -0.5F, 11.5F);
        this.backRightLegPoint.addBox(-1.0F, -1.5F, -1.0F, 0, 0, 0, 0.0F);
        this.backRightLeg = new ModelRenderer(this, 9, 22);
        this.backRightLeg.setRotationPoint(0.0F, -1.5F, 0.0F);
        this.backRightLeg.addBox(-1.0F, 0.0F, -1.0F, 2, 8, 2, 0.0F);
        this.tail1 = new ModelRenderer(this, 20, 22);
        this.tail1.setRotationPoint(0.0F, 15.0F, 7.6F);
        this.tail1.addBox(-1.0F, 0.0F, -1.0F, 2, 8, 2, 0.0F);
        this.setRotateAngle(tail1, (float) (180 / (180 / Math.PI)), -0.0F, 0.0F);
        this.tailBobbed = new ModelRenderer(this, 20, 22);
        this.tailBobbed.setRotationPoint(0.0F, 15.0F, 7.6F);
        this.tailBobbed.addBox(-1.0F, 0.0F, -1.0F, 2, 2, 2, 0.0F);
        this.setRotateAngle(tailBobbed, (float) (135 / (180 / Math.PI)), -0.0F, 0.0F);
        this.tail2 = new ModelRenderer(this, 28, 22);
        this.tail2.setRotationPoint(0.0F, 7.8F, 0.0F);
        this.tail2.addBox(-1.0F, 0.0F, -1.0F, 2, 3, 2, 0.0F);
        this.setRotateAngle(tail2, (float) (10 / (180 / Math.PI)), -0.0F, 0.0F);
        this.head1.addChild(this.nose1);
        this.nose1.addChild(this.whiskers1);
        this.head1.addChild(this.earLeft1);
        this.head1.addChild(this.earRight1);
        this.head2.addChild(this.nose2);
        this.nose2.addChild(this.whiskers2);
        this.head2.addChild(this.earLeft2);
        this.head2.addChild(this.earRight2);
        this.head2.addChild(this.faceTuftLeft);
        this.head2.addChild(this.faceTuftRight);
        this.earLeft2.addChild(this.earLeftTuft);
        this.earRight2.addChild(this.earRightTuft);
        this.body1.addChild(this.frontLeftLegPoint);
        this.frontLeftLegPoint.addChild(this.frontLeftLeg);
        this.body1.addChild(this.frontRightLegPoint);
        this.frontRightLegPoint.addChild(this.frontRightLeg);
        this.body1.addChild(this.backLeftLegPoint);
        this.backLeftLegPoint.addChild(this.backLeftLeg);
        this.body1.addChild(this.backRightLegPoint);
        this.backRightLegPoint.addChild(this.backRightLeg);
        this.tail1.addChild(this.tail2);
    }

    @Override
    public void render(Entity entity, float parSpeed, float parWalkSpeed, float par4, float parHeadAngleY, float parHeadAngleX, float scale) {
        this.setRotationAngles(parSpeed, parWalkSpeed, par4, parHeadAngleY, parHeadAngleX, scale, entity);
        if (entity instanceof EntityCat) {
            EntityCat entityCat = (EntityCat) entity;
            ModelRenderer tailType = entityCat.isBobtail() ? tailBobbed : tail1;
            if (this.isChild) {
                float ageScale = entityCat.getAge() / (float) SCConfig.KITTEN_MATURE_TIMER + 1;
                float headScale = ageScale * (1f - 0.625f) + 0.625f;
                GlStateManager.pushMatrix();
                GlStateManager.scale(headScale, headScale, headScale);
                GlStateManager.translate(0.0f, 0.975 * (1.0f - headScale), 0.25f - 0.25f * headScale);
                this.head1.render(scale);
                GlStateManager.popMatrix();

                float tailScale = ageScale * (1f - 0.35f) + 0.35f;
                GlStateManager.pushMatrix();
                GlStateManager.scale(tailScale, tailScale, tailScale);
                GlStateManager.translate(0.0f, 7.32f - 7.32f * tailScale, 0.25f - 0.25f * tailScale);
                tailType.render(scale);
                GlStateManager.popMatrix();

                float bodyScale = ageScale * (1f - 0.5f) + 0.5f;
                GlStateManager.pushMatrix();
                GlStateManager.scale(bodyScale, bodyScale, ageScale * (1f - 0.4f) + 0.4f);
                GlStateManager.translate(0.0f, 1.5f - 1.5f * bodyScale, 0.0F);
                this.body1.render(scale);
                GlStateManager.popMatrix();
            } else {
                ModelRenderer head = entityCat.isLongFur() ? head2 : head1;
                GlStateManager.pushMatrix();
                GlStateManager.scale(1.01D, 1.01D, 1.01D);
                head.render(scale);
                GlStateManager.popMatrix();
                this.body1.render(scale);
                if (entityCat.isLongFur()) {
                    GlStateManager.pushMatrix();
                    GlStateManager.translate(this.body2.offsetX, this.body2.offsetY, this.body2.offsetZ);
                    GlStateManager.translate(this.body2.rotationPointX * scale, this.body2.rotationPointY * scale, this.body2.rotationPointZ * scale);
                    GlStateManager.scale(1.02D, 1.2D, 1.01D);
                    GlStateManager.translate(-this.body2.offsetX, -this.body2.offsetY, -this.body2.offsetZ);
                    GlStateManager.translate(-this.body2.rotationPointX * scale, -this.body2.rotationPointY * scale, -this.body2.rotationPointZ * scale);
                    this.body2.render(scale);
                    GlStateManager.popMatrix();
                    GlStateManager.pushMatrix();
                    GlStateManager.translate(tailType.offsetX, tailType.offsetY, tailType.offsetZ);
                    GlStateManager.translate(tailType.rotationPointX * scale, tailType.rotationPointY * scale, tailType.rotationPointZ * scale);
                    GlStateManager.scale(1.25D, 1.0D, 1.25D);
                    GlStateManager.translate(-tailType.offsetX, -tailType.offsetY, -tailType.offsetZ);
                    GlStateManager.translate(-tailType.rotationPointX * scale, -tailType.rotationPointY * scale, -tailType.rotationPointZ * scale);
                    tailType.render(scale);
                    GlStateManager.popMatrix();
                } else tailType.render(scale);
            }
        }
    }

    public void setRotationAngles(float parSpeed, float parWalkSpeed, float par4, float parHeadAngleY, float parHeadAngleX, float par7, Entity parEntity) {
        super.setRotationAngles(parSpeed, parWalkSpeed, par4, parHeadAngleY, parHeadAngleX, par7, parEntity);
        if (parEntity instanceof EntityCat) {
            EntityCat cat = (EntityCat) parEntity;
            ModelRenderer head = !cat.isChild() && cat.isLongFur() ? head2 : head1;

            head.rotateAngleX = parHeadAngleX / (180F / (float) Math.PI);
            head.rotateAngleY = parHeadAngleY / (180F / (float) Math.PI);
        }
    }

    public void setLivingAnimations(EntityLivingBase entity, float parSpeed, float parWalkSpeed, float f4) {
        if (entity instanceof EntityCat) {
            EntityCat cat = (EntityCat) entity;
            ModelRenderer tailType = cat.isBobtail() ? tailBobbed : tail1;
            ModelRenderer head = !cat.isChild() && cat.isLongFur() ? head2 : head1;

            head.rotationPointY = 14.0F;
            head.rotationPointZ = -6.5F;
            this.body1.rotateAngleX = 0.0F;
            this.body1.rotationPointY = 18.0F;
            if (cat.isLongFur()) {
                this.body2.rotateAngleX = 0.0F;
                this.body2.rotationPointY = 18.0F;
            }
            this.frontLeftLegPoint.rotateAngleX = this.frontRightLegPoint.rotateAngleX = 0.0F;
            this.backLeftLegPoint.rotateAngleX = this.backRightLegPoint.rotateAngleX = 0.0F;
            this.backLeftLegPoint.rotationPointY = this.backRightLegPoint.rotationPointY = -0.5F;
            this.frontLeftLegPoint.rotationPointY = this.frontRightLegPoint.rotationPointY = -0.5F;
            this.frontLeftLeg.rotateAngleX = MathHelper.cos(parSpeed * 0.6662F) * 0.5F * parWalkSpeed;
            this.backRightLeg.rotateAngleX = MathHelper.cos(parSpeed * 0.6662F + 1.5F) * 0.5F * parWalkSpeed;
            this.frontRightLeg.rotateAngleX = MathHelper.cos(parSpeed * 0.6662F + 3.0F) * 0.5F * parWalkSpeed;
            this.backLeftLeg.rotateAngleX = MathHelper.cos(parSpeed * 0.6662F + 4.5F) * 0.5F * parWalkSpeed;
            tailType.rotationPointY = 15.0F;
            this.tail1.rotateAngleX = (float) (180 / (180 / Math.PI));
            this.tail2.rotateAngleX = (float) (10 / (180 / Math.PI));
            this.tailBobbed.rotateAngleX = (float) (135 / (180 / Math.PI));
            this.earLeft1.rotateAngleX = 0.0F;
            this.earLeft1.rotateAngleY = 0.0F;
            this.earRight1.rotateAngleX = 0.0F;
            this.earRight1.rotateAngleY = 0.0F;

            if (cat.isAngry() || cat.isSneaking()) {
                this.earLeft1.rotateAngleX = (float) (67 / (180 / Math.PI));
                this.earLeft1.rotateAngleY = (float) (-145 / (180 / Math.PI));
                this.earRight1.rotateAngleX = (float) (67 / (180 / Math.PI));
                this.earRight1.rotateAngleY = (float) (145 / (180 / Math.PI));
            }

            if (cat.isSneaking()) {
                head.rotationPointY += 2.5F;
                this.body1.rotationPointY += 2.0F;
                if (cat.isLongFur()) this.body2.rotationPointY += 2.0F;
                this.backLeftLegPoint.rotationPointY -= 2.0F;
                this.backRightLegPoint.rotationPointY -= 2.0F;
                this.frontLeftLegPoint.rotationPointY -= 2.0F;
                this.frontRightLegPoint.rotationPointY -= 2.0F;
                tailType.rotationPointY += 2.0F;
                tailType.rotateAngleX = ((float) Math.PI / 3F);
            }

            if (cat.isSitting()) {
                if (this.isChild) {
                    tailType.rotationPointY = 23.5F;
                } else {
                    tailType.rotationPointY = 21.5F;
                }
                head.rotationPointZ = -4.5F;
                this.body1.rotateAngleX = (float) (-28 / (180 / Math.PI));
                if (cat.isLongFur()) this.body2.rotateAngleX = (float) (-24.3 / (180 / Math.PI));
                this.frontLeftLeg.rotateAngleX = this.frontRightLeg.rotateAngleX = (float) (28 / (180 / Math.PI));
                this.backLeftLegPoint.rotateAngleX = this.backRightLegPoint.rotateAngleX = (float) (-62.5 / (180 / Math.PI));
                this.backLeftLeg.rotateAngleX = this.backRightLeg.rotateAngleX = 0.0F;
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
