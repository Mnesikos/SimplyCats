package com.github.mnesikos.simplycats.item;

import com.github.mnesikos.simplycats.entity.EntityCat;
import com.github.mnesikos.simplycats.init.ModItems;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityList;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.passive.EntityTameable;
import net.minecraft.entity.passive.EntityWolf;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagDouble;
import net.minecraft.nbt.NBTTagFloat;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.util.*;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.client.resources.I18n;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.registry.EntityEntry;
import net.minecraftforge.fml.common.registry.ForgeRegistries;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nullable;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class ItemPetCarrier extends ModItemBase {

    public ItemPetCarrier() {
        super("pet_carrier");
        this.setMaxStackSize(1);
        this.setHasSubtypes(true);
    }

    @Override
    public boolean itemInteractionForEntity(ItemStack stack, EntityPlayer player, EntityLivingBase target, EnumHand hand) {
        if (target instanceof EntityCat || target instanceof EntityWolf) {
            if (((EntityTameable) target).getOwner() == player) {
                if (stack.hasTagCompound()) {
                    if (target.world.isRemote)
                        player.sendMessage(new TextComponentTranslation("chat.pet_carrier.full"));
                } else {
                    stack = player.getHeldItem(hand);
                    NBTTagCompound tags = new NBTTagCompound();
                    target.writeEntityToNBT(tags);
                    target.setDead();

                    tags.setString("id", EntityList.getEntityString(target));
                    Iterator var5 = ForgeRegistries.ENTITIES.getEntries().iterator();
                    while(var5.hasNext()) {
                        Map.Entry<ResourceLocation, EntityEntry> f = (Map.Entry)var5.next();
                        if (((EntityEntry)f.getValue()).getEntityClass() == target.getClass()) {
                            tags.setString("Entity", String.valueOf(f.getKey()));
                        }
                    }
                    if (target.hasCustomName())
                        tags.setString("customName", target.getCustomNameTag());
                    tags.setString("ownerName", player.getDisplayNameString());
                    if (target.world.isRemote)
                        player.sendMessage(new TextComponentTranslation("chat.pet_carrier.retrieve_pet"));

                    stack.setTagCompound(tags);
                    player.swingArm(hand);
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
    public EnumActionResult onItemUse(EntityPlayer player, World world, BlockPos pos, EnumHand hand, EnumFacing facing, float x, float y, float z) {
        ItemStack item = player.getHeldItem(hand);
        if (!item.hasTagCompound() || item.getItemDamage() == 0) {
            if (world.isRemote)
                player.sendMessage(new TextComponentTranslation("chat.pet_carrier.empty"));
            return EnumActionResult.FAIL;
        }

        if (world.isRemote) {
            if (item.getItemDamage() == 1 || item.getItemDamage() == 2)
                player.sendMessage(new TextComponentTranslation("chat.pet_carrier.release_pet"));
            return EnumActionResult.FAIL;
        }

        BlockPos blockpos = pos.offset(facing);
        double d0 = this.getYOffset(world, blockpos);

        if (item.getItemDamage() == 3 || item.getItemDamage() == 4) {
            newPet(item, player, world, (double)blockpos.getX() + 0.5D, (double)blockpos.getY() + d0, (double)blockpos.getZ() + 0.5D);
            if (!player.capabilities.isCreativeMode) {
                item.shrink(1);
                if (item.getCount() <= 0)
                    player.inventory.setInventorySlotContents(player.inventory.currentItem, ItemStack.EMPTY);
            }
        } else {
            NBTTagCompound tags;
            tags = item.getTagCompound();

            tags.setTag("Pos", this.newDoubleNBTList((double)blockpos.getX() + 0.5D, (double)blockpos.getY() + d0, (double)blockpos.getZ() + 0.5D));
            tags.setTag("Rotation", this.newFloatNBTList(MathHelper.wrapDegrees(world.rand.nextFloat() * 360.0F), 0.0F));
            tags.setTag("Motion", this.newDoubleNBTList(0.0, 0.0, 0.0));
            tags.setFloat("FallDistance", 0.0f);

            if (item.getItemDamage() == 1 || item.getItemDamage() == 2) {
                Entity entity = EntityList.createEntityByIDFromName(new ResourceLocation(tags.getString("Entity")), world);
                entity.readFromNBT(tags);
                world.spawnEntity(entity);
                item.setTagCompound(null);
                item.setItemDamage(0);
            }
        }
        player.swingArm(hand);
        return EnumActionResult.SUCCESS;
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

    private double getYOffset(World p_190909_1_, BlockPos p_190909_2_) {
        AxisAlignedBB axisalignedbb = (new AxisAlignedBB(p_190909_2_)).expand(0.0D, -1.0D, 0.0D);
        List<AxisAlignedBB> list = p_190909_1_.getCollisionBoxes(null, axisalignedbb);

        if (list.isEmpty()) {
            return 0.0D;
        } else {
            double d0 = axisalignedbb.minY;

            for (AxisAlignedBB axisalignedbb1 : list) {
                d0 = Math.max(axisalignedbb1.maxY, d0);
            }

            return d0 - (double)p_190909_2_.getY();
        }
    }

    private void newPet(ItemStack item, EntityPlayer player, World world, double x, double y, double z) {
        EntityTameable pet = null;
        if (item.getItemDamage() == 3) {
            pet = new EntityCat(world);
        } else if (item.getItemDamage() == 4) {
            pet = new EntityWolf(world);
        }

        if (pet != null) {
            pet.setLocationAndAngles(x, y, z, MathHelper.wrapDegrees(world.rand.nextFloat() * 360.0F), 0.0F);
            pet.rotationYawHead = pet.rotationYaw;
            pet.renderYawOffset = pet.rotationYaw;
            world.spawnEntity(pet);
            pet.setTamed(true);
            pet.setOwnerId(player.getUniqueID());
            float health = pet.getMaxHealth();
            pet.setHealth(health);
        }
    }

    @Override
    public String getItemStackDisplayName(ItemStack item) {
        if (!item.hasTagCompound() || item.getItemDamage() == 0)
            return I18n.format(this.getUnlocalizedNameInefficiently(item) + ".empty.name");
        else
            return I18n.format(this.getUnlocalizedNameInefficiently(item) + ".full.name");
    }

    @Override @SideOnly(Side.CLIENT)
    public void getSubItems(CreativeTabs tab, NonNullList<ItemStack> itemList) {
        ItemStack cat = new ItemStack(ModItems.PET_CARRIER, 1);
        cat.setTagCompound(new NBTTagCompound());
        cat.setItemDamage(3);
        itemList.add(cat);
        itemList.add(new ItemStack(ModItems.PET_CARRIER, 1, 0));
    }

    @SideOnly(Side.CLIENT)
    public void addInformation(ItemStack item, @Nullable World worldIn, List<String> tooltip, ITooltipFlag flagIn) {
        NBTTagCompound nbt = item.getTagCompound();
        if (nbt != null) {
            String cat = I18n.format("tooltip.pet_carrier.adopt_cat");
            String dog = I18n.format("tooltip.pet_carrier.adopt_dog");
            if (item.getItemDamage() == 3)
                tooltip.add(TextFormatting.ITALIC + cat);
            else if (item.getItemDamage() == 4)
                tooltip.add(TextFormatting.ITALIC + dog);

            else if (item.getItemDamage() != 0) {
                String species = I18n.format("entity." + nbt.getString("id") + ".name");
                String specificCat = I18n.format("cat.type." + nbt.getInteger("Type") + ".name");
                String catSex = I18n.format("cat.sex." + nbt.getByte("Sex") + "b.name");

                String base = I18n.format("cat.base." + nbt.getInteger("Base") + ".name");
                String tabby = I18n.format("cat.tabby.name");
                String white = I18n.format("cat.white.name");
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

                String owner = I18n.format("tooltip.pet_carrier.owner");
                if (nbt.hasKey("customName"))
                    tooltip.add(TextFormatting.AQUA + "\"" + nbt.getString("customName") + "\"");
                tooltip.add(TextFormatting.ITALIC + (item.getItemDamage() == 2 ? species : (item.getItemDamage() == 1 && nbt.getInteger("Type") == 3 ? specificCat : (item.getItemDamage() == 1 ? catPheno + " " + catSex + " " + species : null))));
                tooltip.add(owner + " " + nbt.getString("ownerName"));
            } else
                return;
        } else {
            String empty = I18n.format("tooltip.pet_carrier.empty");
            tooltip.add(TextFormatting.AQUA + empty);
        }
    }
}
