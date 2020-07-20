package com.github.mnesikos.simplycats.client.renderer.block;

import net.minecraft.block.Block;
import net.minecraft.client.renderer.RenderBlocks;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.util.IIcon;
import net.minecraft.world.IBlockAccess;

import com.github.mnesikos.simplycats.proxy.ClientProxy;

import cpw.mods.fml.client.registry.ISimpleBlockRenderingHandler;

public class RenderBowl {}/*implements ISimpleBlockRenderingHandler {
	@Override
	public void renderInventoryBlock(Block block, int metadata, int modelId, RenderBlocks renderer) {
		return;
	}

	@Override
	public boolean renderWorldBlock(IBlockAccess blockAccess, int posX, int posY, int posZ, Block block, int modelId, RenderBlocks renderer) {
		Tessellator tessellator = Tessellator.instance;
		IIcon iicon;
		float minU; float minV;
		float maxU; float maxV;
		
		tessellator.addTranslation(posX, posY, posZ);
		tessellator.setColorOpaque_F(0.83F, 0.83F, 0.83F);
		tessellator.setBrightness(block.getMixedBrightnessForBlock(blockAccess, posX, posY, posZ));
		
		double minY = 0; double bottomY = 0.0625; double maxY = 0.125;
		double min = (float)(5.5/16); double max = (float)(10.5/16);
		double min2 = (float)(6.5/16); double max2 = (float)(9.5/16);
		//int meta = blockAccess.getBlockMetadata(posX, posY, posZ);
		
		//bottom face
		iicon = block.getIcon(0, 0);
		minU = iicon.getInterpolatedU(5); minV = iicon.getInterpolatedV(2);
		maxU = iicon.getInterpolatedU(10); maxV = iicon.getInterpolatedV(7);
		tessellator.addVertexWithUV(max, minY, min, maxU, maxV);
		tessellator.addVertexWithUV(max, minY, max, maxU, minV);
		tessellator.addVertexWithUV(min, minY, max, minU, minV);
		tessellator.addVertexWithUV(min, minY, min, minU, maxV);
		
		//outside north/front face
		minU = iicon.getInterpolatedU(4); minV = iicon.getInterpolatedV(2);
		maxU = iicon.getInterpolatedU(9); maxV = iicon.getInterpolatedV(4);
		tessellator.addVertexWithUV(max, minY, max, maxU, maxV);
		tessellator.addVertexWithUV(max, maxY, max, maxU, minV);
		tessellator.addVertexWithUV(min, maxY, max, minU, minV);
		tessellator.addVertexWithUV(min, minY, max, minU, maxV);
		
		//outside east/left face
		tessellator.addVertexWithUV(max, minY, min, maxU, maxV);
		tessellator.addVertexWithUV(max, maxY, min, maxU, minV);
		tessellator.addVertexWithUV(max, maxY, max, minU, minV);
		tessellator.addVertexWithUV(max, minY, max, minU, maxV);
		
		//outside south/back face
		tessellator.addVertexWithUV(min, minY, min, maxU, maxV);
		tessellator.addVertexWithUV(min, maxY, min, maxU, minV);
		tessellator.addVertexWithUV(max, maxY, min, minU, minV);
		tessellator.addVertexWithUV(max, minY, min, minU, maxV);
		
		//outside west/right face
		tessellator.addVertexWithUV(min, minY, max, maxU, maxV);
		tessellator.addVertexWithUV(min, maxY, max, maxU, minV);
		tessellator.addVertexWithUV(min, maxY, min, minU, minV);
		tessellator.addVertexWithUV(min, minY, min, minU, maxV);
		
		//north/front top
		minU = iicon.getInterpolatedU(0); minV = iicon.getInterpolatedV(0);
		maxU = iicon.getInterpolatedU(5); maxV = iicon.getInterpolatedV(1);
		tessellator.addVertexWithUV(max, maxY, max, maxU, maxV);
		tessellator.addVertexWithUV(max, maxY, max2, maxU, minV);
		tessellator.addVertexWithUV(min, maxY, max2, minU, minV);
		tessellator.addVertexWithUV(min, maxY, max, minU, maxV);
		
		//south/back top
		tessellator.addVertexWithUV(min, maxY, min, maxU, maxV);
		tessellator.addVertexWithUV(min, maxY, min2, maxU, minV);
		tessellator.addVertexWithUV(max, maxY, min2, minU, minV);
		tessellator.addVertexWithUV(max, maxY, min, minU, maxV);
		
		//east/left top
		minU = iicon.getInterpolatedU(3); minV = iicon.getInterpolatedV(0);
		maxU = iicon.getInterpolatedU(6); maxV = iicon.getInterpolatedV(1);
		tessellator.addVertexWithUV(max, maxY, min2, maxU, maxV);
		tessellator.addVertexWithUV(max2, maxY, min2, maxU, minV);
		tessellator.addVertexWithUV(max2, maxY, max2, minU, minV);
		tessellator.addVertexWithUV(max, maxY, max2, minU, maxV);
		
		//west/right top
		tessellator.addVertexWithUV(min, maxY, max2, maxU, maxV);
		tessellator.addVertexWithUV(min2, maxY, max2, maxU, minV);
		tessellator.addVertexWithUV(min2, maxY, min2, minU, minV);
		tessellator.addVertexWithUV(min, maxY, min2, minU, maxV);
		
		//inside bottom
		minU = iicon.getInterpolatedU(7); minV = iicon.getInterpolatedV(5);
		maxU = iicon.getInterpolatedU(10); maxV = iicon.getInterpolatedV(8);
		tessellator.addVertexWithUV(max2, bottomY, max2, maxU, maxV);
		tessellator.addVertexWithUV(max2, bottomY, min2, maxU, minV);
		tessellator.addVertexWithUV(min2, bottomY, min2, minU, minV);
		tessellator.addVertexWithUV(min2, bottomY, max2, minU, maxV);
		
		//inside north/front
		minU = iicon.getInterpolatedU(6); minV = iicon.getInterpolatedV(3);
		maxU = iicon.getInterpolatedU(9); maxV = iicon.getInterpolatedV(4);
		tessellator.addVertexWithUV(min2, bottomY, max2, maxU, maxV);
		tessellator.addVertexWithUV(min2, maxY, max2, maxU, minV);
		tessellator.addVertexWithUV(max2, maxY, max2, minU, minV);
		tessellator.addVertexWithUV(max2, bottomY, max2, minU, maxV);
		
		//inside east/left
		tessellator.addVertexWithUV(min2, bottomY, min2, maxU, maxV);
		tessellator.addVertexWithUV(min2, maxY, min2, maxU, minV);
		tessellator.addVertexWithUV(min2, maxY, max2, minU, minV);
		tessellator.addVertexWithUV(min2, bottomY, max2, minU, maxV);
		
		//inside south/back
		tessellator.addVertexWithUV(max2, bottomY, min2, maxU, maxV);
		tessellator.addVertexWithUV(max2, maxY, min2, maxU, minV);
		tessellator.addVertexWithUV(min2, maxY, min2, minU, minV);
		tessellator.addVertexWithUV(min2, bottomY, min2, minU, maxV);
		
		//inside west/right
		tessellator.addVertexWithUV(max2, bottomY, max2, maxU, maxV);
		tessellator.addVertexWithUV(max2, maxY, max2, maxU, minV);
		tessellator.addVertexWithUV(max2, maxY, min2, minU, minV);
		tessellator.addVertexWithUV(max2, bottomY, min2, minU, maxV);
		
		tessellator.addTranslation(-posX, -posY, -posZ);
		return true;
	}

	@Override
	public boolean shouldRender3DInInventory(int modelId) {
		return false;
	}

	@Override
	public int getRenderId() {
		return ClientProxy.RENDER_BOWL_ID;
	}

}*/
