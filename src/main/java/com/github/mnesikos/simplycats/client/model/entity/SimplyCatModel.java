package com.github.mnesikos.simplycats.client.model.entity;

import com.github.mnesikos.simplycats.SimplyCats;
import com.github.mnesikos.simplycats.entity.SimplyCatEntity;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.*;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class SimplyCatModel<T extends SimplyCatEntity> extends EntityModel<T> {
    public static final ModelLayerLocation LAYER_LOCATION = new ModelLayerLocation(new ResourceLocation(SimplyCats.MOD_ID, "simplycatmodel"), "main");
    private final ModelPart body1;
    private final ModelPart body2;
    private final ModelPart head1;
    private final ModelPart head2;
    private final ModelPart tail1;
    private final ModelPart tailBobbed;
    private final ModelPart frontRightLegPoint;
    private final ModelPart frontLeftLegPoint;
    private final ModelPart backRightLegPoint;
    private final ModelPart backLeftLegPoint;
    private final ModelPart earLeft1;
    private final ModelPart earRight1;
    private final ModelPart tail2;
    private final ModelPart frontRightLeg;
    private final ModelPart frontLeftLeg;
    private final ModelPart backRightLeg;
    private final ModelPart backLeftLeg;
    public boolean isBobtail;
    public boolean isLongFur;
    public float ageScale;

    public SimplyCatModel(ModelPart root) {
        super();
        this.body1 = root.getChild("body1");
        this.head1 = root.getChild("head1");
        this.head2 = root.getChild("head2");
        this.body2 = root.getChild("body2");
        this.tail1 = root.getChild("tail1");
        this.tailBobbed = root.getChild("tailBobbed");
        this.frontRightLegPoint = root.getChild("frontRightLegPoint");
        this.frontLeftLegPoint = root.getChild("frontLeftLegPoint");
        this.backRightLegPoint = root.getChild("backRightLegPoint");
        this.backLeftLegPoint = root.getChild("backLeftLegPoint");
        this.earLeft1 = root.getChild("earLeft1");
        this.earRight1 = root.getChild("earRight1");
        this.tail2 = root.getChild("tail2");
        this.frontRightLeg = root.getChild("frontRightLeg");
        this.frontLeftLeg = root.getChild("frontLeftLeg");
        this.backRightLeg = root.getChild("backRightLeg");
        this.backLeftLeg = root.getChild("backLeftLeg");
    }

    public static LayerDefinition createBodyLayer() {
        MeshDefinition meshdefinition = new MeshDefinition();
        PartDefinition partdefinition = meshdefinition.getRoot();

        PartDefinition body1 = partdefinition.addOrReplaceChild("body1", CubeListBuilder.create().texOffs(20, 0).addBox(-2.5F, -4.0F, -1.5F, 5, 5, 15, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 18.0F, -5.0F));
        PartDefinition head1 = partdefinition.addOrReplaceChild("head1", CubeListBuilder.create().texOffs(0, 0).addBox(-2.5F, -2.0F, -5.0F, 5, 4, 5, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 14.0F, -6.5F));
        PartDefinition nose1 = head1.addOrReplaceChild("nose1", CubeListBuilder.create().texOffs(21, 0).addBox(-1.5F, -1.0F, -2.0F, 3, 2, 2, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 1.01F, -4.5F));
        nose1.addOrReplaceChild("whiskers1", CubeListBuilder.create().texOffs(2, 9).addBox(-4.0F, -1.0F, -0.5F, 8, 2, 0, new CubeDeformation(0.0F)), PartPose.offset(0.0F, -0.1F, -1.2F));
        head1.addOrReplaceChild("earLeft1", CubeListBuilder.create().texOffs(26, 9).addBox(-1.0F, -2.0F, -1.0F, 2, 3, 2, new CubeDeformation(0.0F)), PartPose.offset(1.6F, -1.5F, -0.8F));
        head1.addOrReplaceChild("earRight1", CubeListBuilder.create().texOffs(18, 9).addBox(-1.0F, -2.0F, -1.0F, 2, 3, 2, new CubeDeformation(0.0F)), PartPose.offset(-1.6F, -1.5F, -0.8F));
        PartDefinition head2 = partdefinition.addOrReplaceChild("head2", CubeListBuilder.create().texOffs(0, 0).addBox(-2.5F, -2.0F, -5.0F, 5, 4, 5, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 14.0F, -6.5F));
        PartDefinition nose2 = head2.addOrReplaceChild("nose2", CubeListBuilder.create().texOffs(21, 0).addBox(-1.5F, -1.0F, -2.0F, 3, 2, 2, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 1.01F, -4.5F));
        nose2.addOrReplaceChild("whiskers2", CubeListBuilder.create().texOffs(2, 9).addBox(-4.0F, -1.0F, -0.5F, 8, 2, 0, new CubeDeformation(0.0F)), PartPose.offset(0.0F, -0.1F, -1.2F));
        PartDefinition earLeft2 = head2.addOrReplaceChild("earLeft2", CubeListBuilder.create().texOffs(26, 9).addBox(-1.0F, -2.0F, -1.0F, 2, 3, 2, new CubeDeformation(0.0F)), PartPose.offset(1.6F, -1.5F, -0.8F));
        PartDefinition earRight2 = head2.addOrReplaceChild("earRight2", CubeListBuilder.create().texOffs(18, 9).addBox(-1.0F, -2.0F, -1.0F, 2, 3, 2, new CubeDeformation(0.0F)), PartPose.offset(-1.6F, -1.5F, -0.8F));
        earLeft2.addOrReplaceChild("earLeftTuft", CubeListBuilder.create().texOffs(27, 9).addBox(-0.5F, -1.0F, 0.0F, 1, 1, 0, new CubeDeformation(0.0F)), PartPose.offset(0.0F, -1.5F, 0.0F));
        earRight2.addOrReplaceChild("earRightTuft", CubeListBuilder.create().texOffs(19, 9).addBox(-0.5F, -1.0F, 0.0F, 1, 1, 0, new CubeDeformation(0.0F)), PartPose.offset(0.0F, -1.5F, 0.0F));
        head2.addOrReplaceChild("faceTuftLeft", CubeListBuilder.create().texOffs(7, 3).addBox(0.0F, 0.0F, -1.5F, 1, 3, 3, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(1.9F, -0.8F, -3.0F, 0.0F, 0.0F, -0.13962634015954636F));
        head2.addOrReplaceChild("faceTuftRight", CubeListBuilder.create().texOffs(7, 3).addBox(-1.0F, 0.0F, -1.5F, 1, 3, 3, new CubeDeformation(0.0F)).mirror(), PartPose.offsetAndRotation(-1.9F, -0.8F, -3.0F, 0.0F, 0.0F, 0.13962634015954636F));
        partdefinition.addOrReplaceChild("body2", CubeListBuilder.create().texOffs(20, 0).addBox(-2.5F, -3.5F, -1.6F, 5, 5, 15, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 18.0F, -5.0F));
        PartDefinition frontLeftLegPoint = body1.addOrReplaceChild("frontLeftLegPoint", CubeListBuilder.create().texOffs(1, 11).addBox(-1.5F, 0.0F, -1.0F, 0, 0, 0, new CubeDeformation(0.0F)), PartPose.offset(2.2F, -0.5F, -0.7F));
        frontLeftLegPoint.addOrReplaceChild("frontLeftLeg", CubeListBuilder.create().texOffs(1, 11).addBox(-1.5F, 0.0F, -1.0F, 2, 8, 2, new CubeDeformation(0.0F)), PartPose.offset(0.0F, -1.5F, 0.0F));
        PartDefinition frontRightLegPoint = body1.addOrReplaceChild("frontRightLegPoint", CubeListBuilder.create().texOffs(9, 11).addBox(-1.5F, 0.0F, -1.0F, 0, 0, 0, new CubeDeformation(0.0F)), PartPose.offset(-1.2F, -0.5F, -0.7F));
        frontRightLegPoint.addOrReplaceChild("frontRightLeg", CubeListBuilder.create().texOffs(9, 11).addBox(-1.5F, 0.0F, -1.0F, 2, 8, 2, new CubeDeformation(0.0F)), PartPose.offset(0.0F, -1.5F, 0.0F));
        PartDefinition backLeftLegPoint = body1.addOrReplaceChild("backLeftLegPoint", CubeListBuilder.create().texOffs(1, 22).addBox(-1.0F, -1.5F, -1.0F, 0, 0, 0, new CubeDeformation(0.0F)), PartPose.offset(1.7F, -0.5F, 11.5F));
        backLeftLegPoint.addOrReplaceChild("backLeftLeg", CubeListBuilder.create().texOffs(1, 22).addBox(-1.0F, 0.0F, -1.0F, 2, 8, 2, new CubeDeformation(0.0F)), PartPose.offset(0.0F, -1.5F, 0.0F));
        PartDefinition backRightLegPoint = body1.addOrReplaceChild("backRightLegPoint", CubeListBuilder.create().texOffs(9, 22).addBox(-1.0F, -1.5F, -1.0F, 0, 0, 0, new CubeDeformation(0.0F)), PartPose.offset(-1.7F, -0.5F, 11.5F));
        backRightLegPoint.addOrReplaceChild("backRightLeg", CubeListBuilder.create().texOffs(9, 22).addBox(-1.0F, 0.0F, -1.0F, 2, 8, 2, new CubeDeformation(0.0F)), PartPose.offset(0.0F, -1.5F, 0.0F));
        PartDefinition tail1 = partdefinition.addOrReplaceChild("tail1", CubeListBuilder.create().texOffs(20, 22).addBox(-1.0F, 0.0F, -1.0F, 2, 8, 2, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 15.0F, 7.6F, (float) (180 / (180 / Math.PI)), -0.0F, 0.0F));
        partdefinition.addOrReplaceChild("tailBobbed", CubeListBuilder.create().texOffs(20, 22).addBox(-1.0F, 0.0F, -1.0F, 2, 2, 2, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 15.0F, 7.6F, (float) (135 / (180 / Math.PI)), -0.0F, 0.0F));
        tail1.addOrReplaceChild("tail2", CubeListBuilder.create().texOffs(28, 22).addBox(-1.0F, 0.0F, -1.0F, 2, 3, 2, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 7.8F, 0.0F, (float) (10 / (180 / Math.PI)), -0.0F, 0.0F));

        return LayerDefinition.create(meshdefinition, 64, 32);
    }

    @Override
    public void renderToBuffer(PoseStack matrixStack, VertexConsumer iVertexBuilder, int packedLightIn, int packedOverlayIn, float red, float green, float blue, float alpha) {
        ModelPart tailType = isBobtail ? tailBobbed : tail1;
        if (young) {
            float bodyScale = ageScale * (1f - 0.5f) + 0.5f;
            float headScale = ageScale * (1f - 0.625f) + 0.625f;
            matrixStack.pushPose();
            float yHeadOffset = (16f * (1f - bodyScale) + 4f * (1f - headScale)) / 16F;
            float zHeadOffset = 2.5f * (1f - bodyScale) / 16F;
            matrixStack.translate(0.0f, yHeadOffset, zHeadOffset);
            matrixStack.scale(headScale, headScale, headScale);
            head1.render(matrixStack, iVertexBuilder, packedLightIn, packedOverlayIn, red, green, blue, alpha);
            matrixStack.popPose();

            float tailScale = ageScale * (1f - 0.35f) + 0.35f;
            matrixStack.pushPose();
            float yTailOffset = 21.5f * (1f - tailScale) / 16F;
            float zTailOffset = 0.8f * (1f - tailScale) / 16F;
            matrixStack.translate(0.0f, yTailOffset, zTailOffset);
            matrixStack.scale(tailScale, tailScale, tailScale);
            tailType.render(matrixStack, iVertexBuilder, packedLightIn, packedOverlayIn, red, green, blue, alpha);
            matrixStack.popPose();

            matrixStack.pushPose();
            matrixStack.translate(0.0f, 24f * (1f - bodyScale) / 16F, 0.0f);
            matrixStack.scale(bodyScale, bodyScale, ageScale * (1f - 0.4f) + 0.4f);
            body1.render(matrixStack, iVertexBuilder, packedLightIn, packedOverlayIn, red, green, blue, alpha);
            matrixStack.popPose();
        } else {
            ModelPart head = isLongFur ? head2 : head1;
            matrixStack.pushPose();
            matrixStack.scale(1.01F, 1.01F, 1.01F);
            head.render(matrixStack, iVertexBuilder, packedLightIn, packedOverlayIn, red, green, blue, alpha);
            matrixStack.popPose();
            body1.render(matrixStack, iVertexBuilder, packedLightIn, packedOverlayIn, red, green, blue, alpha);
            if (isLongFur) {
                matrixStack.pushPose();
                matrixStack.translate(body2.x / 16F, body2.y / 16F, body2.z / 16F);
                matrixStack.scale(1.02F, 1.2F, 1.01F);
                matrixStack.translate(-body2.x / 16F, -body2.y / 16F, -body2.z / 16F);
                body2.render(matrixStack, iVertexBuilder, packedLightIn, packedOverlayIn, red, green, blue, alpha);
                matrixStack.popPose();
                matrixStack.pushPose();
                matrixStack.translate(tailType.x / 16F, tailType.y / 16F, tailType.z / 16F);
                matrixStack.scale(1.25F, 1.0F, 1.25F);
                matrixStack.translate(-tailType.x / 16F, -tailType.y / 16F, -tailType.z / 16F);
                tailType.render(matrixStack, iVertexBuilder, packedLightIn, packedOverlayIn, red, green, blue, alpha);
                matrixStack.popPose();
            } else
                tailType.render(matrixStack, iVertexBuilder, packedLightIn, packedOverlayIn, red, green, blue, alpha);
        }
    }

    @Override
    public void setupAnim(T entity, float speed, float walkSpeed, float v2, float headAngleY, float headAngleX) {
        ModelPart head = !entity.isBaby() && entity.isLongFur() ? head2 : head1;

        head.xRot = headAngleX / (180F / (float) Math.PI);
        head.yRot = headAngleY / (180F / (float) Math.PI);
    }

    @Override
    public void prepareMobModel(T cat, float parSpeed, float parWalkSpeed, float f4) {
        ModelPart tailType = cat.isBobtail() ? tailBobbed : tail1;
        ModelPart head = !cat.isBaby() && cat.isLongFur() ? head2 : head1;

        head.y = 14.0F;
        head.z = -6.5F;
        body1.xRot = 0.0F;
        body1.y = 18.0F;
        if (cat.isLongFur()) {
            body2.xRot = 0.0F;
            body2.y = 18.0F;
        }
        frontLeftLegPoint.xRot = frontRightLegPoint.xRot = 0.0F;
        backLeftLegPoint.xRot = backRightLegPoint.xRot = 0.0F;
        backLeftLegPoint.y = backRightLegPoint.y = -0.5F;
        frontLeftLegPoint.y = frontRightLegPoint.y = -0.5F;
        frontLeftLeg.xRot = Mth.cos(parSpeed * 0.6662F) * 0.5F * parWalkSpeed;
        backRightLeg.xRot = Mth.cos(parSpeed * 0.6662F + 1.5F) * 0.5F * parWalkSpeed;
        frontRightLeg.xRot = Mth.cos(parSpeed * 0.6662F + 3.0F) * 0.5F * parWalkSpeed;
        backLeftLeg.xRot = Mth.cos(parSpeed * 0.6662F + 4.5F) * 0.5F * parWalkSpeed;
        tailType.y = 15.0F;
        tail1.xRot = (float) (180 / (180 / Math.PI));
        tail2.xRot = (float) (10 / (180 / Math.PI));
        tailBobbed.xRot = (float) (135 / (180 / Math.PI));
        earLeft1.xRot = 0.0F;
        earLeft1.yRot = 0.0F;
        earRight1.xRot = 0.0F;
        earRight1.yRot = 0.0F;

        if (cat.isAngry() || cat.isCrouching()) {
            earLeft1.xRot = (float) (67 / (180 / Math.PI));
            earLeft1.yRot = (float) (-145 / (180 / Math.PI));
            earRight1.xRot = (float) (67 / (180 / Math.PI));
            earRight1.yRot = (float) (145 / (180 / Math.PI));
        }

        if (cat.isCrouching()) {
            head.y += 2.5F;
            body1.y += 2.0F;
            if (cat.isLongFur()) body2.y += 2.0F;
            backLeftLegPoint.y -= 2.0F;
            backRightLegPoint.y -= 2.0F;
            frontLeftLegPoint.y -= 2.0F;
            frontRightLegPoint.y -= 2.0F;
            tailType.y += 2.0F;
            tailType.xRot = ((float) Math.PI / 3F);
        }

        if (cat.isInSittingPose()) {
            if (young) {
                float tailScale = ageScale * (1f - 0.35f) + 0.35f;
                float bodyScale = ageScale * (1f - 0.5f) + 0.5f;
                tailType.y += 6.5F + (24F * (1f - bodyScale) + 21.5F * (1f - tailScale)) / 16F; //todo
            } else
                tailType.y = 21.5F;
            head.z = -4.5F;
            body1.xRot = (float) (-28 / (180 / Math.PI));
            if (cat.isLongFur()) body2.xRot = (float) (-24.3 / (180 / Math.PI));
            frontLeftLeg.xRot = frontRightLeg.xRot = (float) (28 / (180 / Math.PI));
            backLeftLegPoint.xRot = backRightLegPoint.xRot = (float) (-62.5 / (180 / Math.PI));
            backLeftLeg.xRot = backRightLeg.xRot = 0.0F;
            tailType.xRot = (float) (79 / (180 / Math.PI));
        }
    }
}
