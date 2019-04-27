package com.github.mnesikos.simplycats.item;

import java.util.List;

import net.minecraft.block.Block;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityList;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.passive.EntityTameable;
import net.minecraft.entity.passive.EntityWolf;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.*;
import net.minecraft.util.*;
import net.minecraft.world.World;

import com.github.mnesikos.simplycats.init.ModItems;
import com.github.mnesikos.simplycats.entity.EntityCat;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class ItemPetCarrier extends Item {

	public ItemPetCarrier() {
		this.setMaxStackSize(1);
		this.setHasSubtypes(true);
	}
	
	@Override
    public boolean itemInteractionForEntity(ItemStack stack, EntityPlayer player, EntityLivingBase target) {
		if (target instanceof EntityCat || target instanceof EntityWolf) {
			if (((EntityTameable) target).getOwner() == player) {
				if (stack.hasTagCompound()) {
					if (target.worldObj.isRemote)
		    			player.addChatComponentMessage(new ChatComponentTranslation("chat.pet_carrier.full"));
				} else {
					stack = player.getHeldItem();
			        NBTTagCompound tags = new NBTTagCompound();
		    		target.writeEntityToNBT(tags);
		    		target.setDead();
		    		
		    		tags.setString("id", (String) EntityList.classToStringMapping.get(target.getClass()));
		    		if (((EntityLiving) target).hasCustomNameTag())
		    			tags.setString("customName", ((EntityLiving) target).getCustomNameTag());
		    		tags.setString("ownerName", player.getDisplayName());
		    		
		    		if (target.worldObj.isRemote)
						player.addChatComponentMessage(new ChatComponentTranslation("chat.pet_carrier.retrieve_pet"));
		    		
		    		stack.setTagCompound(tags);
		    		player.swingItem();
		    		if (target instanceof EntityCat)
		    			stack.setItemDamage(1);
		    		else
		    			stack.setItemDamage(2);
				}
			}
    	}
    	return true;
    }
	
	@Override
    public boolean onItemUse(ItemStack item, EntityPlayer player, World world, int x, int y, int z, int side, float hitX, float hitY, float hitZ) {
		if (!item.hasTagCompound() || item.getItemDamage() == 0) {
			if (world.isRemote)
				player.addChatComponentMessage(new ChatComponentTranslation("chat.pet_carrier.empty"));
            return false;
        }
		
		if (world.isRemote) {
			if (item.getItemDamage() == 1 || item.getItemDamage() == 2)
		    	player.addChatComponentMessage(new ChatComponentTranslation("chat.pet_carrier.release_pet"));
			return false;
		}

		Block block = world.getBlock(x, y, z);
		x += Facing.offsetsXForSide[side];
		y += Facing.offsetsYForSide[side];
		z += Facing.offsetsZForSide[side];
		double d0 = 0.0D;
		if (side == 1 && block != null && block.getRenderType() == 11) {
			d0 = 0.5D;
		}

		if (item.getItemDamage() == 3 || item.getItemDamage() == 4) {
			newPet(item, player, world, x, y + (int)d0, z, side);
			if (!player.capabilities.isCreativeMode) {
                --item.stackSize;
                if (item.stackSize <= 0)
                    player.inventory.setInventorySlotContents(player.inventory.currentItem, null);
            }
		} else {
			NBTTagCompound tags;
			tags = item.getTagCompound();
			
			tags.setTag("Pos", this.newDoubleNBTList(x + 0.5, y + d0, z + 0.5));
			tags.setTag("Rotation", this.newFloatNBTList(MathHelper.wrapAngleTo180_float(world.rand.nextFloat() * 360.0F), 0.0F));
			tags.setTag("Motion", this.newDoubleNBTList(0.0, 0.0, 0.0));
			tags.setFloat("FallDistance", 0.0f);
			
			if (item.getItemDamage() == 1 || item.getItemDamage() == 2) {
				Entity entity = EntityList.createEntityFromNBT(tags, world);
	    		world.spawnEntityInWorld(entity);
			    item.setTagCompound(null);
			    item.setItemDamage(0);
			}
		}
		player.swingItem();
		return true;
    }
	
	private NBTTagList newDoubleNBTList(final double... par1ArrayOfDouble) {
		final NBTTagList nbttaglist = new NBTTagList();
		for (final double d1 : par1ArrayOfDouble)
			nbttaglist.appendTag(new NBTTagDouble(d1));
		return nbttaglist;
	}

	private NBTTagList newFloatNBTList(float... numbers) {
		NBTTagList nbttaglist = new NBTTagList();
		for (float f : numbers)
			nbttaglist.appendTag(new NBTTagFloat(f));
		return nbttaglist;
	}
    
	private void newPet(ItemStack item, EntityPlayer player, World world, int x, int y, int z, int side) {
		EntityTameable pet = null;
		int health = 1;
		if (item.getItemDamage() == 3) {
			pet = new EntityCat(world);
			health = 10;
		} else if (item.getItemDamage() == 4) {
			pet = new EntityWolf(world);
			health = 20;
		}

		if (pet != null) {
			pet.setLocationAndAngles(x + 0.5, y, z + 0.5, MathHelper.wrapAngleTo180_float(world.rand.nextFloat() * 360.0F), 0.0F);
			world.spawnEntityInWorld(pet);
			pet.setTamed(true);
			pet.func_152115_b(player.getUniqueID().toString());
			pet.setHealth(health);
		}
	}
	
	@Override
	public String getItemStackDisplayName(ItemStack item) {
		if (!item.hasTagCompound() || item.getItemDamage() == 0)
			return StatCollector.translateToLocal(this.getUnlocalizedNameInefficiently(item) + ".empty.name");
		else
			return StatCollector.translateToLocal(this.getUnlocalizedNameInefficiently(item) + ".full.name");
	}

    @SuppressWarnings({ "rawtypes", "unchecked" })
	@Override @SideOnly(Side.CLIENT)
    public void getSubItems(Item item, CreativeTabs tab, List itemList) {
    	ItemStack cat = new ItemStack(ModItems.PET_CARRIER, 1);
    	cat.setTagCompound(new NBTTagCompound());
    	cat.setItemDamage(3);
		itemList.add(cat);
		itemList.add(new ItemStack(item, 1, 0));
	}
	
    @SuppressWarnings({ "rawtypes", "unchecked" })
    @SideOnly(Side.CLIENT)
	public void addInformation(ItemStack item, EntityPlayer player, List tooltip, boolean isAdvanced) {
    	NBTTagCompound nbt = item.getTagCompound();
		if (nbt != null) {
			String cat = StatCollector.translateToLocal("tooltip.pet_carrier.adopt_cat");
			String dog = StatCollector.translateToLocal("tooltip.pet_carrier.adopt_dog");
			if (item.getItemDamage() == 3)
				tooltip.add(EnumChatFormatting.ITALIC + cat);
			else if (item.getItemDamage() == 4)
				tooltip.add(EnumChatFormatting.ITALIC + dog);

			else if (item.getItemDamage() != 0) {
				String species = StatCollector.translateToLocal("entity." + nbt.getString("id") + ".name");
				String specificCat = StatCollector.translateToLocal("cat.type." + nbt.getInteger("Type") + ".name");
				String catSex = StatCollector.translateToLocal("cat.sex." + nbt.getString("Sex") + ".name");

				String base = StatCollector.translateToLocal("cat.base." + nbt.getInteger("Base") + ".name");
				String tabby = StatCollector.translateToLocal("cat.tabby.name");
				String white = StatCollector.translateToLocal("cat.white.name");
				String catPheno;

				if (nbt.getInteger("tabby") != 0 || nbt.getInteger("white") != 0) {
					if (nbt.getInteger("white") == 0)
						catPheno = base + " " + tabby;
					else if (nbt.getInteger("tabby") == 0)
						catPheno = base + " " + white;
					else
						catPheno = base + " " + tabby + " " + white;
				} else
					catPheno = base;

				String owner = StatCollector.translateToLocal("tooltip.pet_carrier.owner");
				if (nbt.hasKey("customName"))
					tooltip.add(EnumChatFormatting.AQUA + "\"" + nbt.getString("customName") + "\"");
				tooltip.add(EnumChatFormatting.ITALIC + (item.getItemDamage() == 2 ? species : (item.getItemDamage() == 1 && nbt.getInteger("Type") == 3 ? specificCat : (item.getItemDamage() == 1 ? catPheno + " " + catSex + " " + species : null))));
				tooltip.add(owner + " " + nbt.getString("ownerName"));
			} else
				return;
		} else {
			String empty = StatCollector.translateToLocal("tooltip.pet_carrier.empty");
			tooltip.add(EnumChatFormatting.AQUA + empty);
		}
    }
}
