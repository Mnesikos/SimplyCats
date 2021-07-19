package com.github.mnesikos.simplycats.client.model.entity;

import com.github.mnesikos.simplycats.entity.SimplyCatEntity;
import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.vertex.IVertexBuilder;
import net.minecraft.client.renderer.entity.model.EntityModel;
import net.minecraft.client.renderer.model.ModelRenderer;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class SimplyCatModel extends EntityModel<SimplyCatEntity> {
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

    public SimplyCatModel() {
        this.head1 = new ModelRenderer(this, 0, 0);
        this.head1.setPos(0.0F, 14.0F, -6.5F);
        this.head1.addBox(-2.5F, -2.0F, -5.0F, 5, 4, 5, 0.0F);
        this.nose1 = new ModelRenderer(this, 21, 0);
        this.nose1.setPos(0.0F, 1.01F, -4.5F);
        this.nose1.addBox(-1.5F, -1.0F, -2.0F, 3, 2, 2, 0.0F);
        this.whiskers1 = new ModelRenderer(this, 2, 9);
        this.whiskers1.setPos(0.0F, -0.1F, -1.2F);
        this.whiskers1.addBox(-4.0F, -1.0F, -0.5F, 8, 2, 0, 0.0F);
        this.earLeft1 = new ModelRenderer(this, 26, 9);
        this.earLeft1.setPos(1.6F, -1.5F, -0.8F);
        this.earLeft1.addBox(-1.0F, -2.0F, -1.0F, 2, 3, 2, 0.0F);
        this.earRight1 = new ModelRenderer(this, 18, 9);
        this.earRight1.setPos(-1.6F, -1.5F, -0.8F);
        this.earRight1.addBox(-1.0F, -2.0F, -1.0F, 2, 3, 2, 0.0F);
        this.head2 = new ModelRenderer(this, 0, 0);
        this.head2.setPos(0.0F, 14.0F, -6.5F);
        this.head2.addBox(-2.5F, -2.0F, -5.0F, 5, 4, 5, 0.0F);
        this.nose2 = new ModelRenderer(this, 21, 0);
        this.nose2.setPos(0.0F, 1.01F, -4.5F);
        this.nose2.addBox(-1.5F, -1.0F, -2.0F, 3, 2, 2, 0.0F);
        this.whiskers2 = new ModelRenderer(this, 2, 9);
        this.whiskers2.setPos(0.0F, -0.1F, -1.2F);
        this.whiskers2.addBox(-4.0F, -1.0F, -0.5F, 8, 2, 0, 0.0F);
        this.earLeft2 = new ModelRenderer(this, 26, 9);
        this.earLeft2.setPos(1.6F, -1.5F, -0.8F);
        this.earLeft2.addBox(-1.0F, -2.0F, -1.0F, 2, 3, 2, 0.0F);
        this.earRight2 = new ModelRenderer(this, 18, 9);
        this.earRight2.setPos(-1.6F, -1.5F, -0.8F);
        this.earRight2.addBox(-1.0F, -2.0F, -1.0F, 2, 3, 2, 0.0F);
        this.earLeftTuft = new ModelRenderer(this, 27, 9);
        this.earLeftTuft.setPos(0.0F, -1.5F, 0.0F);
        this.earLeftTuft.addBox(-0.5F, -1.0F, 0.0F, 1, 1, 0, 0.0F);
        this.earRightTuft = new ModelRenderer(this, 19, 9);
        this.earRightTuft.setPos(0.0F, -1.5F, 0.0F);
        this.earRightTuft.addBox(-0.5F, -1.0F, 0.0F, 1, 1, 0, 0.0F);
        this.faceTuftLeft = new ModelRenderer(this, 7, 3);
        this.faceTuftLeft.setPos(1.9F, -0.8F, -3.0F);
        this.faceTuftLeft.addBox(0.0F, 0.0F, -1.5F, 1, 3, 3, 0.0F);
        this.setRotateAngle(faceTuftLeft, 0.0F, 0.0F, -0.13962634015954636F);
        this.faceTuftRight = new ModelRenderer(this, 7, 3);
        this.faceTuftRight.setPos(-1.9F, -0.8F, -3.0F);
        this.faceTuftRight.addBox(-1.0F, 0.0F, -1.5F, 1, 3, 3, true);
        this.setRotateAngle(faceTuftRight, 0.0F, 0.0F, 0.13962634015954636F);
        this.body1 = new ModelRenderer(this, 20, 0);
        this.body1.setPos(0.0F, 18.0F, -5.0F);
        this.body1.addBox(-2.5F, -4.0F, -1.5F, 5, 5, 15, 0.0F);
        this.body2 = new ModelRenderer(this, 20, 0);
        this.body2.setPos(0.0F, 18.0F, -5.0F);
        this.body2.addBox(-2.5F, -3.5F, -1.6F, 5, 5, 15, 0.0F);
        this.frontLeftLegPoint = new ModelRenderer(this, 1, 11);
        this.frontLeftLegPoint.setPos(2.2F, -0.5F, -0.7F);
        this.frontLeftLegPoint.addBox(-1.5F, 0.0F, -1.0F, 0, 0, 0, 0.0F);
        this.frontLeftLeg = new ModelRenderer(this, 1, 11);
        this.frontLeftLeg.setPos(0.0F, -1.5F, 0.0F);
        this.frontLeftLeg.addBox(-1.5F, 0.0F, -1.0F, 2, 8, 2, 0.0F);
        this.frontRightLegPoint = new ModelRenderer(this, 9, 11);
        this.frontRightLegPoint.setPos(-1.2F, -0.5F, -0.7F);
        this.frontRightLegPoint.addBox(-1.5F, 0.0F, -1.0F, 0, 0, 0, 0.0F);
        this.frontRightLeg = new ModelRenderer(this, 9, 11);
        this.frontRightLeg.setPos(0.0F, -1.5F, 0.0F);
        this.frontRightLeg.addBox(-1.5F, 0.0F, -1.0F, 2, 8, 2, 0.0F);
        this.backLeftLegPoint = new ModelRenderer(this, 1, 22);
        this.backLeftLegPoint.setPos(1.7F, -0.5F, 11.5F);
        this.backLeftLegPoint.addBox(-1.0F, -1.5F, -1.0F, 0, 0, 0, 0.0F);
        this.backLeftLeg = new ModelRenderer(this, 1, 22);
        this.backLeftLeg.setPos(0.0F, -1.5F, 0.0F);
        this.backLeftLeg.addBox(-1.0F, 0.0F, -1.0F, 2, 8, 2, 0.0F);
        this.backRightLegPoint = new ModelRenderer(this, 9, 22);
        this.backRightLegPoint.setPos(-1.7F, -0.5F, 11.5F);
        this.backRightLegPoint.addBox(-1.0F, -1.5F, -1.0F, 0, 0, 0, 0.0F);
        this.backRightLeg = new ModelRenderer(this, 9, 22);
        this.backRightLeg.setPos(0.0F, -1.5F, 0.0F);
        this.backRightLeg.addBox(-1.0F, 0.0F, -1.0F, 2, 8, 2, 0.0F);
        this.tail1 = new ModelRenderer(this, 20, 22);
        this.tail1.setPos(0.0F, 15.0F, 7.6F);
        this.tail1.addBox(-1.0F, 0.0F, -1.0F, 2, 8, 2, 0.0F);
        this.setRotateAngle(tail1, (float) (180 / (180 / Math.PI)), -0.0F, 0.0F);
        this.tailBobbed = new ModelRenderer(this, 20, 22);
        this.tailBobbed.setPos(0.0F, 15.0F, 7.6F);
        this.tailBobbed.addBox(-1.0F, 0.0F, -1.0F, 2, 2, 2, 0.0F);
        this.setRotateAngle(tailBobbed, (float) (135 / (180 / Math.PI)), -0.0F, 0.0F);
        this.tail2 = new ModelRenderer(this, 28, 22);
        this.tail2.setPos(0.0F, 7.8F, 0.0F);
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
    public void setupAnim(SimplyCatEntity entity, float speed, float walkSpeed, float v2, float headAngleY, float headAngleX) {
        head1.xRot = headAngleX / (180F / (float) Math.PI);
        head1.yRot = headAngleY / (180F / (float) Math.PI);
    }

    @Override
    public void renderToBuffer(MatrixStack matrixStack, IVertexBuilder iVertexBuilder, int packedLightIn, int packedOverlayIn, float red, float green, float blue, float alpha) {
        matrixStack.pushPose();
        matrixStack.scale(1.01f, 1.01f, 1.01f);
        head1.render(matrixStack, iVertexBuilder, packedLightIn, packedOverlayIn, red, green, blue, alpha);
        matrixStack.popPose();
        body1.render(matrixStack, iVertexBuilder, packedLightIn, packedOverlayIn, red, green, blue, alpha);
        tail1.render(matrixStack, iVertexBuilder, packedLightIn, packedOverlayIn, red, green, blue, alpha);
    }

    private void setRotateAngle(ModelRenderer modelRenderer, float x, float y, float z) {
        modelRenderer.xRot = x;
        modelRenderer.yRot = y;
        modelRenderer.zRot = z;
    }
}
