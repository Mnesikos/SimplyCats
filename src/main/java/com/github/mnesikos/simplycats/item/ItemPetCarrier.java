package com.github.mnesikos.simplycats.item;

import java.util.List;

import com.github.mnesikos.simplycats.Ref;
import com.github.mnesikos.simplycats.entity.core.Genetics;
import net.minecraft.block.Block;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityList;
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
		setUnlocalizedName("pet_carrier");
		setTextureName(Ref.MODID + ":pet_carrier");
		setCreativeTab(ModItems.CREATIVE_TAB);
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
		    		target.writeToNBT(tags);
		    		target.setDead();
		    		
		    		tags.setString("id", (String) EntityList.classToStringMapping.get(target.getClass()));
					/*Iterator var5 = ForgeRegistries.ENTITIES.getEntries().iterator();
					while(var5.hasNext()) {
						Map.Entry<ResourceLocation, EntityEntry> f = (Map.Entry)var5.next();
						if (((EntityEntry)f.getValue()).getEntityClass() == target.getClass()) {
							tags.setString("Entity", String.valueOf(f.getKey()));
						}
					}*/
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
			return true;
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
		if (item.getItemDamage() == 3) {
			pet = new EntityCat(world);
		} else if (item.getItemDamage() == 4) {
			pet = new EntityWolf(world);
		}

		if (pet != null) {
			pet.setLocationAndAngles(x + 0.5, y, z + 0.5, MathHelper.wrapAngleTo180_float(world.rand.nextFloat() * 360.0F), 0.0F);
			pet.rotationYawHead = pet.rotationYaw;
			pet.renderYawOffset = pet.rotationYaw;
			world.spawnEntityInWorld(pet);
			pet.setTamed(true);

			if (world.isRemote) {
				for (int i = 0; i < 7; ++i) {
					double d0 = world.rand.nextGaussian() * 0.02D;
					double d1 = world.rand.nextGaussian() * 0.02D;
					double d2 = world.rand.nextGaussian() * 0.02D;
					world.spawnParticle("heart",
							pet.posX + (double) (world.rand.nextFloat() * pet.width * 2.0F) - (double) pet.width,
							pet.posY + 0.5D + (double) (world.rand.nextFloat() * pet.height),
							pet.posZ + (double) (world.rand.nextFloat() * pet.width * 2.0F) - (double) pet.width,
							d0, d1, d2);
				}
			}

			if (pet instanceof EntityCat) {
				((EntityCat) pet).setHomePos(x, y, z);
				/*if (world.isRemote)
					player.addChatComponentMessage(new ChatComponentText(pet.getCommandSenderName() +
						new ChatComponentTranslation("chat.info.set_home").getFormattedText() +
						" " + x + ", " + y + ", " + z));*/ // todo
				((EntityCat) pet).onInitialSpawn();
			}
			pet.getNavigator().clearPathEntity();
			pet.func_152115_b(player.getUniqueID().toString());
			float health = pet.getMaxHealth();
			pet.setHealth(health);
		}
	}
	
	@Override
	public String getItemStackDisplayName(ItemStack item) {
		String unlocalizedName = this.getUnlocalizedNameInefficiently(item);
		if (!item.hasTagCompound() || item.getItemDamage() == 0)
			unlocalizedName += ".empty.name";
		else
			unlocalizedName += ".full.name";
		return StatCollector.translateToLocal(unlocalizedName);
	}

    @SuppressWarnings({ "rawtypes", "unchecked" })
	@Override @SideOnly(Side.CLIENT)
    public void getSubItems(Item item, CreativeTabs tab, List itemList) {
		if (tab == this.getCreativeTab()) {
			ItemStack cat = new ItemStack(ModItems.PET_CARRIER, 1);
			cat.setTagCompound(new NBTTagCompound());
			cat.setItemDamage(3);
			itemList.add(cat);
			itemList.add(new ItemStack(item, 1, 0));
		}
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

			else if (item.getItemDamage() != 0) { // todo
				String species = StatCollector.translateToLocal("entity." + nbt.getString("id") + ".name");
                /*String specificCat = I18n.format("cat.type." + nbt.getInteger("Type") + ".name");
                String catSex = I18n.format("cat.sex." + nbt.getByte("Sex") + "b.name");

                String base = I18n.format("cat.base." + nbt.getInteger("Base") + ".name");
                String tabby = I18n.format("cat.tabby.name");
                String white = I18n.format("cat.white.name");
                String catPheno;*/

                /*if (nbt.getInteger("tabby") != 0 || nbt.getInteger("white") != 0) {
                    if (nbt.getInteger("white") == 0)
                        catPheno = base + " " + tabby;
                    else if (nbt.getInteger("tabby") == 0)
                        catPheno = base + " " + white;
                    else
                        catPheno = base + " " + tabby + " " + white;
                } else
                    catPheno = base;*/


				String owner = StatCollector.translateToLocal("tooltip.pet_carrier.owner");
				String sex = StatCollector.translateToLocal("cat.sex." + (nbt.getString("Phaeomelanin").contains(Genetics.Phaeomelanin.MALE.getAllele()) ? "male" : "female") + ".name");
				if (nbt.hasKey("CustomName"))
					tooltip.add(EnumChatFormatting.AQUA + "\"" + nbt.getString("CustomName") + "\"" + " " + sex);
				if (item.getItemDamage() == 2)
					tooltip.add(EnumChatFormatting.ITALIC + species);
				else if (item.getItemDamage() == 1) {
					String eumelanin = Genetics.Eumelanin.getPhenotype(nbt.getString("Eumelanin"));
					String phaeomelanin = Genetics.Phaeomelanin.getPhenotype(nbt.getString("Phaeomelanin"));
					String dilution = Genetics.Dilution.getPhenotype(nbt.getString("Dilution"));
					String diluteMod = Genetics.DiluteMod.getPhenotype(nbt.getString("DiluteMod"));
					String base = StatCollector.translateToLocal("cat.base." + eumelanin + (phaeomelanin.equals(Genetics.Phaeomelanin.NOT_RED.toString().toLowerCase()) ? "" : "_" + phaeomelanin) + ".name");
					if (dilution.equals(Genetics.Dilution.DILUTE.toString().toLowerCase())) {
						base = StatCollector.translateToLocal("cat.base." + eumelanin + "_" + phaeomelanin + "_" + dilution + ".name");
						if (diluteMod.equals(Genetics.DiluteMod.CARAMELIZED.toString().toLowerCase()))
							base = StatCollector.translateToLocal("cat.base." + eumelanin + "_" + phaeomelanin + "_" + diluteMod + ".name");
					}
					if (phaeomelanin.equals(Genetics.Phaeomelanin.RED.toString().toLowerCase())) {
						base = StatCollector.translateToLocal("cat.base." + phaeomelanin + ".name");
						if (dilution.equals(Genetics.Dilution.DILUTE.toString().toLowerCase())) {
							base = StatCollector.translateToLocal("cat.base." + phaeomelanin + "_" + dilution + ".name");
							if (diluteMod.equals(Genetics.DiluteMod.CARAMELIZED.toString().toLowerCase()))
								base = StatCollector.translateToLocal("cat.base." + phaeomelanin + "_" + diluteMod + ".name");
						}
					}

					String agouti = Genetics.Agouti.getPhenotype(nbt.getString("Agouti"));
					String tabby1 = Genetics.Tabby.getPhenotype(nbt.getString("Tabby"));
					String spotted = Genetics.Spotted.getPhenotype(nbt.getString("Spotted"));
					String ticked = Genetics.Ticked.getPhenotype(nbt.getString("Ticked"));
					String tabby = StatCollector.translateToLocal("");
					if (agouti.equals(Genetics.Agouti.TABBY.toString().toLowerCase()) || phaeomelanin.equals(Genetics.Phaeomelanin.RED.toString().toLowerCase())) {
						tabby = StatCollector.translateToLocal("cat.tabby." + tabby1 + ".name");
						if (spotted.equals(Genetics.Spotted.BROKEN.toString().toLowerCase()) || spotted.equals(Genetics.Spotted.SPOTTED.toString().toLowerCase()))
							tabby = StatCollector.translateToLocal("cat.tabby." + spotted + ".name");
						if (ticked.equals(Genetics.Ticked.TICKED.toString().toLowerCase()))
							tabby = StatCollector.translateToLocal("cat.tabby." + ticked + ".name");
					}

					String colorpoint = Genetics.Colorpoint.getPhenotype(nbt.getString("Colorpoint"));
					String point = StatCollector.translateToLocal("");
					if (!colorpoint.equals(Genetics.Colorpoint.NOT_POINTED.toString().toLowerCase())) {
						point = StatCollector.translateToLocal("cat.point." + colorpoint + ".name");
					}

					tooltip.add(EnumChatFormatting.ITALIC + base +
							(tabby.equals("") ? "" : " " + tabby) +
							(point.equals("") ? "" : " " + point) +
							" " + species);
				}

				tooltip.add(owner + " " + nbt.getString("ownerName"));
			}
		} else {
			String empty = StatCollector.translateToLocal("tooltip.pet_carrier.empty");
			tooltip.add(EnumChatFormatting.AQUA + empty);
		}
    }
}
