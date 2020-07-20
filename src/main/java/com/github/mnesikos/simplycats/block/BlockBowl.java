package com.github.mnesikos.simplycats.block;

import java.util.ArrayList;
import java.util.Random;

import com.github.mnesikos.simplycats.Ref;
import com.github.mnesikos.simplycats.init.ModItems;
import com.github.mnesikos.simplycats.SimplyCats;
import com.github.mnesikos.simplycats.proxy.ClientProxy;
import com.github.mnesikos.simplycats.tileentity.TileEntityBowl;
import net.minecraft.block.Block;
import net.minecraft.block.BlockContainer;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.IIcon;
import net.minecraft.world.World;

import cpw.mods.fml.common.registry.GameRegistry;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class BlockBowl extends BlockContainer {
	private static final String name = "bowl";
	public static int GUI_ID = 0;
	private final Random rand = new Random();
	private IIcon bowlIcon;
	float min = 0.34375F; float max = 0.65625F;
	
	public BlockBowl() {
		super(Material.wood);
		GameRegistry.registerBlock(this, name);
		setBlockName(name);
		setCreativeTab(ModItems.CREATIVE_TAB);
		setBlockBounds(min, 0.0F, min, max, 0.125F, max);
	}
	
	@Override
	public boolean isOpaqueCube() {
		return false;
	}
	
	@Override
	public boolean renderAsNormalBlock() {
		return false;
	}
	
	@Override
	public int getRenderType() {
		return super.getRenderType();//ClientProxy.RENDER_BOWL_ID;
	}
	
	@SideOnly(Side.CLIENT)
	public IIcon getIcon(int side, int meta) {
		return this.bowlIcon;
	}
	
	@SideOnly(Side.CLIENT)
	public void registerBlockIcons(IIconRegister iconRegistry) {
		this.bowlIcon = iconRegistry.registerIcon(Ref.MODID + ":bowl_texture");
	}
	
	@Override
	public boolean onBlockActivated(World world, int x, int y, int z, EntityPlayer player, int side, float lx, float ly, float lz) {
		if (world.isRemote) return true;
		
		TileEntity tileentity = world.getTileEntity(x, y, z);
		if (tileentity != null && tileentity instanceof TileEntityBowl) {
			player.openGui(SimplyCats.instance, GUI_ID, world, x, y, z);
			return true;
		}
		return false;
	}
	
	@Override
	public void breakBlock(World world, int x, int y, int z, Block block, int par6) {
		if (world.isRemote) return;
		
		ArrayList<ItemStack> drops = new ArrayList<ItemStack>();
		
		TileEntity te = world.getTileEntity(x, y, z);
		
		if (te != null && te instanceof TileEntityBowl) {
			TileEntityBowl teBowl = (TileEntityBowl) te;
			
			for (int i = 0; i < teBowl.getSizeInventory(); i++) {
				ItemStack stack = teBowl.getStackInSlot(i);
				
				if (stack != null) drops.add(stack.copy());
			}
		}
		
		for (int i = 0; i < drops.size(); i++) {
			EntityItem item = new EntityItem(world, x + 0.5, y + 0.5, z + 0.5, drops.get(i));
			item.setVelocity((rand.nextDouble() - 0.5) * 0.25, rand.nextDouble() * 0.5 * 0.25, (rand.nextDouble() - 0.5) * 0.25);
			world.spawnEntityInWorld(item);
		}
	}
	
	public TileEntity createNewTileEntity(World world, int par2) {
		return new TileEntityBowl();
	}

}
