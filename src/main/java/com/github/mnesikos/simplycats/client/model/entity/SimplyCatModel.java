package com.github.mnesikos.simplycats.client.model.entity;

import com.github.mnesikos.simplycats.SimplyCats;
import com.github.mnesikos.simplycats.entity.SimplyCatEntity;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.CubeListBuilder;
import net.minecraft.client.model.geom.builders.LayerDefinition;
import net.minecraft.client.model.geom.builders.MeshDefinition;
import net.minecraft.client.model.geom.builders.PartDefinition;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class SimplyCatModel<T extends SimplyCatEntity> extends EntityModel<T> {
    public static final ModelLayerLocation LAYER_LOCATION = new ModelLayerLocation(new ResourceLocation(SimplyCats.MOD_ID, "cat"), "cat");
    public ModelPart body1;
    public ModelPart body2;
    public ModelPart head1;
    public ModelPart head2;
    public ModelPart tail1;
    public ModelPart tailBobbed;
    public ModelPart frontRightLegPoint;
    public ModelPart frontLeftLegPoint;
    public ModelPart backRightLegPoint;
    public ModelPart backLeftLegPoint;
    public ModelPart nose1;
    public ModelPart earLeft1;
    public ModelPart earRight1;
    public ModelPart whiskers1;
    public ModelPart nose2;
    public ModelPart earLeft2;
    public ModelPart earRight2;
    public ModelPart earLeftTuft;
    public ModelPart earRightTuft;
    public ModelPart whiskers2;
    public ModelPart faceTuftLeft;
    public ModelPart faceTuftRight;
    public ModelPart tail2;
    public ModelPart frontRightLeg;
    public ModelPart frontLeftLeg;
    public ModelPart backRightLeg;
    public ModelPart backLeftLeg;
    public boolean isBobtail;
    public boolean isLongFur;
    public float ageScale;

    public SimplyCatModel(ModelPart root) {
        this.head1 = root.getChild("head1");
        this.head2 = root.getChild("head2");
        this.body1 = root.getChild("body1");
        this.body2 = root.getChild("body2");
        this.tail1 = root.getChild("tail1");
        this.tailBobbed = root.getChild("tailBobbed");
        this.nose1 = this.head1.getChild("nose1");
        this.whiskers1 = this.nose1.getChild("whiskers1");
        this.earLeft1 = this.head1.getChild("earLeft1");
        this.earRight1 = this.head1.getChild("earRight1");
        this.nose2 = this.head2.getChild("nose2");
        this.whiskers2 = this.nose2.getChild("whiskers2");
        this.earLeft2 = this.head2.getChild("earLeft2");
        this.earRight2 = this.head2.getChild("earRight2");
        this.faceTuftLeft = this.head2.getChild("faceTuftLeft");
        this.faceTuftRight = this.head2.getChild("faceTuftRight");
        this.earLeftTuft = this.earLeft2.getChild("earLeftTuft");
        this.earRightTuft = this.earRight2.getChild("earRightTuft");
        this.frontLeftLegPoint = this.body1.getChild("frontLeftLegPoint");
        this.frontLeftLeg = this.frontLeftLegPoint.getChild("frontLeftLeg");
        this.frontRightLegPoint = this.body1.getChild("frontRightLegPoint");
        this.frontRightLeg = this.frontRightLegPoint.getChild("frontRightLeg");
        this.backLeftLegPoint = this.body1.getChild("backLeftLegPoint");
        this.backLeftLeg = this.backLeftLegPoint.getChild("backLeftLeg");
        this.backRightLegPoint = this.body1.getChild("backRightLegPoint");
        this.backRightLeg = this.backRightLegPoint.getChild("backRightLeg");
        this.tail2 = this.tail1.getChild("tail2");
    }

    public static LayerDefinition createBodyLayer() {
        MeshDefinition meshDefinition = new MeshDefinition();
        PartDefinition partDefinition = meshDefinition.getRoot();

        PartDefinition head1Def = partDefinition.addOrReplaceChild("head1", CubeListBuilder.create().texOffs(0, 0).addBox(-2.5F, -2.0F, -5.0F, 5, 4, 5), PartPose.offset(0.0F, 14.0F, -6.5F));
        PartDefinition nose1Def = head1Def.addOrReplaceChild("nose1", CubeListBuilder.create().texOffs(21, 0).addBox(-1.5F, -1.0F, -2.0F, 3, 2, 2), PartPose.offset(0.0F, 1.01F, -4.5F));
        PartDefinition whiskers1Def = nose1Def.addOrReplaceChild("whiskers1", CubeListBuilder.create().texOffs(2, 9).addBox(-4.0F, -1.0F, -0.5F, 8, 2, 0), PartPose.offset(0.0F, -0.1F, -1.2F));
        PartDefinition earLeft1Def = head1Def.addOrReplaceChild("earLeft1", CubeListBuilder.create().texOffs(26, 9).addBox(-1.0F, -2.0F, -1.0F, 2, 3, 2), PartPose.offset(1.6F, -1.5F, -0.8F));
        PartDefinition earRight1Def = head1Def.addOrReplaceChild("earRight1", CubeListBuilder.create().texOffs(18, 9).addBox(-1.0F, -2.0F, -1.0F, 2, 3, 2), PartPose.offset(-1.6F, -1.5F, -0.8F));
        PartDefinition head2Def = partDefinition.addOrReplaceChild("head2", CubeListBuilder.create().texOffs(0, 0).addBox(-2.5F, -2.0F, -5.0F, 5, 4, 5), PartPose.offset(0.0F, 14.0F, -6.5F));
        PartDefinition nose2Def = head2Def.addOrReplaceChild("nose2", CubeListBuilder.create().texOffs(21, 0).addBox(-1.5F, -1.0F, -2.0F, 3, 2, 2), PartPose.offset(0.0F, 1.01F, -4.5F));
        PartDefinition whiskers2Def = nose2Def.addOrReplaceChild("whiskers2", CubeListBuilder.create().texOffs(2, 9).addBox(-4.0F, -1.0F, -0.5F, 8, 2, 0), PartPose.offset(0.0F, -0.1F, -1.2F));
        PartDefinition earLeft2Def = head2Def.addOrReplaceChild("earLeft2", CubeListBuilder.create().texOffs(26, 9).addBox(-1.0F, -2.0F, -1.0F, 2, 3, 2), PartPose.offset(1.6F, -1.5F, -0.8F));
        PartDefinition earRight2Def = head2Def.addOrReplaceChild("earRight2", CubeListBuilder.create().texOffs(18, 9).addBox(-1.0F, -2.0F, -1.0F, 2, 3, 2), PartPose.offset(-1.6F, -1.5F, -0.8F));
        PartDefinition earLeftTuftDef = earLeft2Def.addOrReplaceChild("earLeftTuft", CubeListBuilder.create().texOffs(27, 9).addBox(-0.5F, -1.0F, 0.0F, 1, 1, 0), PartPose.offset(0.0F, -1.5F, 0.0F));
        PartDefinition earRightTuftDef = earRight2Def.addOrReplaceChild("earRightTuft", CubeListBuilder.create().texOffs(19, 9).addBox(-0.5F, -1.0F, 0.0F, 1, 1, 0), PartPose.offset(0.0F, -1.5F, 0.0F));
        PartDefinition faceTuftLeftDef = head2Def.addOrReplaceChild("faceTuftLeft", CubeListBuilder.create().texOffs(7, 3).addBox(0.0F, 0.0F, -1.5F, 1, 3, 3), PartPose.offsetAndRotation(1.9F, -0.8F, -3.0F, 0.0F, 0.0F, -0.13962634015954636F));
        PartDefinition faceTuftRightDef = head2Def.addOrReplaceChild("faceTuftRight", CubeListBuilder.create().texOffs(7, 3).addBox(-1.0F, 0.0F, -1.5F, 1, 3, 3, true), PartPose.offsetAndRotation(-1.9F, -0.8F, -3.0F, 0.0F, 0.0F, 0.13962634015954636F));
        PartDefinition body1Def = partDefinition.addOrReplaceChild("body1", CubeListBuilder.create().texOffs(20, 0).addBox(-2.5F, -4.0F, -1.5F, 5, 5, 15), PartPose.offset(0.0F, 18.0F, -5.0F));
        PartDefinition body2Def = partDefinition.addOrReplaceChild("body2", CubeListBuilder.create().texOffs(20, 0).addBox(-2.5F, -3.5F, -1.6F, 5, 5, 15), PartPose.offset(0.0F, 18.0F, -5.0F));
        PartDefinition frontLeftLegPointDef = body1Def.addOrReplaceChild("frontLeftLegPoint", CubeListBuilder.create().texOffs(1, 11).addBox(-1.5F, 0.0F, -1.0F, 0, 0, 0), PartPose.offset(2.2F, -0.5F, -0.7F));
        PartDefinition frontLeftLegDef = frontLeftLegPointDef.addOrReplaceChild("frontLeftLeg", CubeListBuilder.create().texOffs(1, 11).addBox(-1.5F, 0.0F, -1.0F, 2, 8, 2), PartPose.offset(0.0F, -1.5F, 0.0F));
        PartDefinition frontRightLegPointDef = body1Def.addOrReplaceChild("frontRightLegPoint", CubeListBuilder.create().texOffs(9, 11).addBox(-1.5F, 0.0F, -1.0F, 0, 0, 0), PartPose.offset(-1.2F, -0.5F, -0.7F));
        PartDefinition frontRightLegDef = frontRightLegPointDef.addOrReplaceChild("frontRightLeg", CubeListBuilder.create().texOffs(9, 11).addBox(-1.5F, 0.0F, -1.0F, 2, 8, 2), PartPose.offset(0.0F, -1.5F, 0.0F));
        PartDefinition backLeftLegPointDef = body1Def.addOrReplaceChild("backLeftLegPoint", CubeListBuilder.create().texOffs(1, 22).addBox(-1.0F, -1.5F, -1.0F, 0, 0, 0), PartPose.offset(1.7F, -0.5F, 11.5F));
        PartDefinition backLeftLegDef = backLeftLegPointDef.addOrReplaceChild("backLeftLeg", CubeListBuilder.create().texOffs(1, 22).addBox(-1.0F, 0.0F, -1.0F, 2, 8, 2), PartPose.offset(0.0F, -1.5F, 0.0F));
        PartDefinition backRightLegPointDef = body1Def.addOrReplaceChild("backRightLegPoint", CubeListBuilder.create().texOffs(9, 22).addBox(-1.0F, -1.5F, -1.0F, 0, 0, 0), PartPose.offset(-1.7F, -0.5F, 11.5F));
        PartDefinition backRightLegDef = backRightLegPointDef.addOrReplaceChild("backRightLeg", CubeListBuilder.create().texOffs(9, 22).addBox(-1.0F, 0.0F, -1.0F, 2, 8, 2), PartPose.offset(0.0F, -1.5F, 0.0F));
        PartDefinition tail1Def = partDefinition.addOrReplaceChild("tail1", CubeListBuilder.create().texOffs(20, 22).addBox(-1.0F, 0.0F, -1.0F, 2, 8, 2), PartPose.offsetAndRotation(0.0F, 15.0F, 7.6F, (float) Math.toRadians(180), -0.0F, 0.0F));
        PartDefinition tailBobbedDef = partDefinition.addOrReplaceChild("tailBobbed", CubeListBuilder.create().texOffs(20, 22).addBox(-1.0F, 0.0F, -1.0F, 2, 2, 2), PartPose.offsetAndRotation(0.0F, 15.0F, 7.6F, (float) Math.toRadians(135), -0.0F, 0.0F));
        PartDefinition tail2Def = tail1Def.addOrReplaceChild("tail2", CubeListBuilder.create().texOffs(28, 22).addBox(-1.0F, 0.0F, -1.0F, 2, 3, 2), PartPose.offsetAndRotation(0.0F, 7.8F, 0.0F, (float) Math.toRadians(10), -0.0F, 0.0F));

        return LayerDefinition.create(meshDefinition, 64, 32);
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
    public void setupAnim(T entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float netHeadPitch) {
        ModelPart head = !entity.isBaby() && entity.isLongFur() ? head2 : head1;
        head.xRot = netHeadPitch * ((float) Math.PI / 180F);
        head.yRot = netHeadYaw * ((float) Math.PI / 180F);
    }

    @Override
    public void prepareMobModel(T entity, float limbSwing, float limbSwingAmount, float partialTick) {
        ModelPart tailType = entity.isBobtail() ? tailBobbed : tail1;
        ModelPart head = !entity.isBaby() && entity.isLongFur() ? head2 : head1;
        ModelPart body = !entity.isBaby() && entity.isLongFur() ? body2 : body1;

        head.zRot = 0.0F;
        head.setPos(0.0F, 14.0F, -6.5F);
        body.setRotation(0.0F, 0.0F, 0.0F);
        body.y = 18.0F;
        frontLeftLegPoint.setRotation(0.0F, 0.0F, 0.0F);
        frontRightLegPoint.setRotation(0.0F, 0.0F, 0.0F);
        frontLeftLegPoint.y = frontRightLegPoint.y = -0.5F;
        frontRightLegPoint.z = -0.7F;
        backLeftLegPoint.setRotation(0.0F, 0.0F, 0.0F);
        backRightLegPoint.setRotation(0.0F, 0.0F, 0.0F);
        backLeftLegPoint.y = backRightLegPoint.y = -0.5F;
        backLeftLegPoint.x = 1.7F;
        backRightLegPoint.x = -1.7F;
        frontLeftLeg.xRot = Mth.cos(limbSwing * 0.6662F) * 0.5F * limbSwingAmount;
        backRightLeg.xRot = Mth.cos(limbSwing * 0.6662F + 1.5F) * 0.5F * limbSwingAmount;
        frontRightLeg.xRot = Mth.cos(limbSwing * 0.6662F + 3.0F) * 0.5F * limbSwingAmount;
        backLeftLeg.xRot = Mth.cos(limbSwing * 0.6662F + 4.5F) * 0.5F * limbSwingAmount;
        tailType.setPos(0.0F, 15.0F, 7.6F);
        tail1.xRot = (float) Math.toRadians(180);
        tail2.xRot = (float) Math.toRadians(10);
        tailBobbed.xRot = (float) Math.toRadians(135);
        earLeft1.setRotation(0.0F, 0.0F, 0.0F);
        earRight1.setRotation(0.0F, 0.0F, 0.0F);

        if (entity.isAngry() || entity.isCrouching()) {
            earLeft1.xRot = (float) Math.toRadians(67);
            earLeft1.yRot = (float) Math.toRadians(-145);
            earRight1.xRot = (float) Math.toRadians(67);
            earRight1.yRot = (float) Math.toRadians(145);
        }

        if (entity.isCrouching()) {
            head.y += 2.5F;
            body.y += 2.0F;
            backLeftLegPoint.y -= 2.0F;
            backRightLegPoint.y -= 2.0F;
            frontLeftLegPoint.y -= 2.0F;
            frontRightLegPoint.y -= 2.0F;
            tailType.y += 2.0F;
            tailType.xRot = ((float) Math.PI / 3F);

        } else if (entity.isSleeping/*isResting*/()) {
            // loaf todo
            body.y = 23F;
            head.xRot = (float) Math.toRadians(16);
            head.y = 19F;
            head.z = -4.5F;
            frontRightLegPoint.setRotation((float) Math.toRadians(96), (float) Math.toRadians(8), 0.0F);
            frontRightLegPoint.y = 0F;
            frontLeftLegPoint.setRotation((float) Math.toRadians(96), (float) Math.toRadians(-8), 0.0F);
            frontLeftLegPoint.y = 0F;
            backRightLegPoint.setRotation((float) Math.toRadians(-86), (float) Math.toRadians(-8), 0.0F);
            backRightLegPoint.x = -2.10F;
            backRightLegPoint.y = 0F;
            backLeftLegPoint.setRotation((float) Math.toRadians(-86), (float) Math.toRadians(8), 0.0F);
            backLeftLegPoint.x = 2.10F;
            backLeftLegPoint.y = 0F;
            tailType.y = 20F;
            tail1.xRot = (float) Math.toRadians(67);
            tail2.xRot = (float) Math.toRadians(12);

            // lay pose 1 todo
            body.y = 23F;
            head.xRot = (float) Math.toRadians(12);
            head.y = 19F;
            frontRightLegPoint.xRot = (float) Math.toRadians(-85);
            frontLeftLegPoint.xRot = (float) Math.toRadians(-85);
            backRightLegPoint.setRotation((float) Math.toRadians(-85), (float) Math.toRadians(20), 0.0F);
            backLeftLegPoint.setRotation((float) Math.toRadians(-85), (float) Math.toRadians(-20), 0.0F);
            tailType.y = 20F;
            tail1.xRot = (float) Math.toRadians(67);
            tail2.xRot = (float) Math.toRadians(18);

            // lay pose 2 todo
            body.zRot = (float) Math.toRadians(-45);
            body.y = 23F;
            head.setRotation((float) Math.toRadians(18), (float) Math.toRadians(-8), (float) Math.toRadians(-8));
            head.x = -3F;
            head.y = 20.5F;
            frontRightLegPoint.setRotation((float) Math.toRadians(-82), (float) Math.toRadians(-12), 0.0F);
            frontRightLegPoint.z = 0.3F;
            frontLeftLegPoint.setRotation((float) Math.toRadians(-40), 0.0F, (float) Math.toRadians(-24));
            backRightLegPoint.setRotation((float) Math.toRadians(-32), 0.0F, (float) Math.toRadians(-50));
            backLeftLegPoint.setRotation((float) Math.toRadians(-8), 0.0F, (float) Math.toRadians(-29));
            tailType.x = -2F;
            tailType.y = 21F;
            tail1.setRotation((float) Math.toRadians(50), 0.0F, (float) Math.toRadians(-66));
            tail2.xRot = (float) Math.toRadians(18);

            // roll pose todo
            body.zRot = (float) Math.toRadians(180);
            body.y = 20F;
            head.setRotation(0.0F, (float) Math.toRadians(20), (float) Math.toRadians(180));
            head.y = 22F;
            earLeft1.setRotation((float) Math.toRadians(86), (float) Math.toRadians(-56), 0.0F);
            earRight1.setRotation((float) Math.toRadians(86), (float) Math.toRadians(56), 0.0F);
            frontRightLegPoint.xRot = (float) Math.toRadians(-85);
            frontRightLeg.setRotation((float) Math.toRadians(8), 0.0F, (float) Math.toRadians(32));
            frontLeftLegPoint.xRot = (float) Math.toRadians(-85);
            frontLeftLeg.setRotation((float) Math.toRadians(-4), 0.0F, (float) Math.toRadians(-5));
            backRightLegPoint.setRotation((float) Math.toRadians(-85), (float) Math.toRadians(20), 0.0F);
            backRightLeg.xRot = (float) Math.toRadians(4);
            backLeftLegPoint.setRotation((float) Math.toRadians(-85), (float) Math.toRadians(-20), 0.0F);
            backLeftLeg.xRot = (float) Math.toRadians(12);
            tailType.y = 23F;
            tail1.setRotation((float) Math.toRadians(90), (float) Math.toRadians(-16), (float) Math.toRadians(180));
            tail2.zRot = (float) Math.toRadians(-12);

        } else if (entity.isInSittingPose()) {
            if (young) {
                float tailScale = ageScale * (1f - 0.35f) + 0.35f;
                float bodyScale = ageScale * (1f - 0.5f) + 0.5f;
                tailType.y += 6.5F + (24F * (1f - bodyScale) + 21.5F * (1f - tailScale)) / 16F; //todo
            } else
                tailType.y = 21.5F;
            head.z = -4.5F;
            body1.xRot = (float) Math.toRadians(-28);
            if (entity.isLongFur()) body2.xRot = (float) Math.toRadians(-24.3);
            frontLeftLeg.xRot = frontRightLeg.xRot = (float) Math.toRadians(28);
            backLeftLegPoint.xRot = backRightLegPoint.xRot = (float) Math.toRadians(-62.5);
            backLeftLeg.xRot = backRightLeg.xRot = 0.0F;
            tailType.xRot = (float) Math.toRadians(79);
        }
    }
}
