package com.github.mnesikos.simplycats.client.model.entity;

import org.lwjgl.opengl.GL11;

import com.github.mnesikos.simplycats.entity.EntityCat;

import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.MathHelper;

public class ModelCat extends ModelBase {
    private ModelRenderer body;
    public ModelRenderer head;
    private ModelRenderer tail;
    private ModelRenderer frightlegpoint;
    private ModelRenderer fleftlegpoint;
    private ModelRenderer brightlegpoint;
    private ModelRenderer bleftlegpoint;
    private ModelRenderer nose;
    private ModelRenderer lear;
    private ModelRenderer rear;
    private ModelRenderer whiskers;
    private ModelRenderer tail2;
    private ModelRenderer frightleg;
    private ModelRenderer fleftleg;
    private ModelRenderer brightleg;
    private ModelRenderer bleftleg;
    
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
    public void render(Entity entity, float parSpeed, float parWalkSpeed, float par4, float parHeadAngleY, float parHeadAngleX, float par7) { 
    	this.setRotationAngles(parSpeed, parWalkSpeed, par4, parHeadAngleY, parHeadAngleX, par7, entity);
    	
    	EntityCat cat = (EntityCat)entity;
    	int varType = cat.getType();
    	boolean varMav = varType == 3;

		if(this.isChild) {
    		float var8 = 2.0F;
    		GL11.glPushMatrix();
    		GL11.glScalef(1.25F / var8, 1.25F / var8, 1.25F / var8);
    		GL11.glTranslatef(0.0F, 15.6F * par7, 2.5F * par7);
    		this.head.render(par7);
    		GL11.glPopMatrix();
            
    		GL11.glPushMatrix();
    		GL11.glScalef(0.7F / var8, 0.7F / var8, 0.7F / var8);
    		GL11.glTranslatef(0.0F, 41.0F * par7, 1.4F * par7);
            this.tail.render(par7);
    		GL11.glPopMatrix();
    		
    		GL11.glPushMatrix();
            GL11.glScalef(1.0F / var8, 1.0F / var8, 0.8F / var8);
            GL11.glTranslatef(0.0F, 24.0F * par7, 0.0F);
            this.body.render(par7);
            GL11.glPopMatrix();

    	} else if (varMav) {
            this.head.render(par7);
            GL11.glPushMatrix();
            GL11.glTranslatef(this.body.offsetX, this.body.offsetY, this.body.offsetZ);
            GL11.glTranslatef(this.body.rotationPointX * par7, this.body.rotationPointY * par7, this.body.rotationPointZ * par7);
            GL11.glScaled(1.4D, 1.1D, 1.0D);
            GL11.glTranslatef(-this.body.offsetX, -this.body.offsetY, -this.body.offsetZ);
            GL11.glTranslatef(-this.body.rotationPointX * par7, -this.body.rotationPointY * par7, -this.body.rotationPointZ * par7);
            this.body.render(par7);
            GL11.glPopMatrix();
            this.tail.render(par7);

    	} else {
    		GL11.glPushMatrix();
    		GL11.glScaled(1.01D, 1.01D, 1.01D); // scaling head slightly larger for no texture clip hopefully
    		this.head.render(par7);
    		GL11.glPopMatrix();
	        this.body.render(par7);
	        this.tail.render(par7);
    	}
    }
	
	public void setRotationAngles(float parSpeed, float parWalkSpeed, float par4, float parHeadAngleY, float parHeadAngleX, float par7, Entity parEntity) {
		super.setRotationAngles(parSpeed, parWalkSpeed, par4, parHeadAngleY, parHeadAngleX, par7, parEntity);
		this.head.rotateAngleX = parHeadAngleX / (180F / (float)Math.PI);
		this.head.rotateAngleY = parHeadAngleY / (180F / (float)Math.PI);
	}

	public void setLivingAnimations(EntityLivingBase entity, float parSpeed, float parWalkSpeed, float f4) {
		EntityCat cat = (EntityCat)entity;
		
		if (cat.getIsAngry()) {
			this.lear.rotateAngleX = (float) (67 / (180 /Math.PI)); this.lear.rotateAngleY = (float) (-145 / (180 / Math.PI));
			this.rear.rotateAngleX = (float) (67 / (180 /Math.PI)); this.rear.rotateAngleY = (float) (145 / (180 / Math.PI));
		} else {
			this.lear.rotateAngleX = 0.0F; this.lear.rotateAngleY = 0.0F;
			this.rear.rotateAngleX = 0.0F; this.rear.rotateAngleY = 0.0F;
		}

		if (cat.isSitting()) {
			if(this.isChild) {
				this.tail.rotationPointY = 23.5F;
			} else {
				this.tail.rotationPointY = 21.5F;
			}
			this.head.rotationPointZ = -4.5F;
			this.body.rotateAngleX = (float) (-28 / (180 /Math.PI));
			this.fleftleg.rotateAngleX = this.frightleg.rotateAngleX = (float) (28 / (180 /Math.PI));
			this.bleftlegpoint.rotateAngleX = this.brightlegpoint.rotateAngleX = (float) (-62.5 / (180 /Math.PI));
			this.bleftleg.rotateAngleX = this.brightleg.rotateAngleX = 0.0F;
			this.tail.rotateAngleX = (float) (79 / (180 /Math.PI));

		} else {
			this.head.rotationPointZ = -6.5F;
			this.body.rotateAngleX = 0.0F;
			this.fleftlegpoint.rotateAngleX = this.frightlegpoint.rotateAngleX = 0.0F;
			this.bleftlegpoint.rotateAngleX = this.brightlegpoint.rotateAngleX = 0.0F;
			this.fleftleg.rotateAngleX = MathHelper.cos(parSpeed * 0.6662F) * 0.5F * parWalkSpeed;
			this.brightleg.rotateAngleX = MathHelper.cos(parSpeed * 0.6662F + 1.5F) * 0.5F * parWalkSpeed;
			this.frightleg.rotateAngleX = MathHelper.cos(parSpeed * 0.6662F + 3.0F) * 0.5F * parWalkSpeed;
			this.bleftleg.rotateAngleX = MathHelper.cos(parSpeed * 0.6662F + 4.5F) * 0.5F * parWalkSpeed;
			this.tail.rotationPointY = 15.0F;
			this.tail.rotateAngleX = (float) (180 / (180 /Math.PI));
		}
	}

    private void setRotateAngle(ModelRenderer modelRenderer, float x, float y, float z) {
        modelRenderer.rotateAngleX = x;
        modelRenderer.rotateAngleY = y;
        modelRenderer.rotateAngleZ = z;
    }
}
