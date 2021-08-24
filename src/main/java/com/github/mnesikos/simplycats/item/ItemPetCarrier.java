package com.github.mnesikos.simplycats.item;

import com.github.mnesikos.simplycats.entity.EntityCat;
import com.github.mnesikos.simplycats.entity.core.Genetics;
import net.minecraft.client.resources.I18n;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityList;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.passive.EntityParrot;
import net.minecraft.entity.passive.EntityTameable;
import net.minecraft.entity.passive.EntityWolf;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
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
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nullable;
import java.util.List;

public class ItemPetCarrier extends Item {
//    public static final int GUI_ID = 2;

    public ItemPetCarrier() {
        super();
        this.setMaxStackSize(1);
        this.setHasSubtypes(true);
    }

    // The number of item models used by the client
    public int getNumModels() {
        return 5;
    }

    @Override
    public boolean itemInteractionForEntity(ItemStack stack, EntityPlayer player, EntityLivingBase target, EnumHand hand) {
        if (target instanceof EntityCat || target instanceof EntityWolf || target instanceof EntityParrot) {
            if (((EntityTameable) target).getOwner() == player) {
                if (stack.hasTagCompound()) {
                    player.sendStatusMessage(new TextComponentTranslation("chat.pet_carrier.full"), true);
                } else {
                    stack = player.getHeldItem(hand);
                    NBTTagCompound tags = new NBTTagCompound();
                    target.writeToNBT(tags);
                    target.setDead();

                    tags.setString("id", EntityList.getEntityString(target));
                    ResourceLocation key = EntityList.getKey(target.getClass());
                    tags.setString("Entity", key.toString());
                    if (!(target instanceof EntityCat))
                        tags.setString("OwnerName", player.getDisplayNameString());

                    player.sendStatusMessage(new TextComponentTranslation("chat.pet_carrier.retrieve_pet"), true);

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
            player.sendStatusMessage(new TextComponentTranslation("chat.pet_carrier.empty"), true);
            return EnumActionResult.FAIL;
        }

        /*if (item.getItemDamage() == 3 && player.capabilities.isCreativeMode && player.isSneaking()) {
            BlockPos blockpos = pos.offset(facing);
            player.openGui(SimplyCats.instance, GUI_ID, player.world, blockpos.getX(), blockpos.getY(), blockpos.getZ());
        }*/

        if (!world.isRemote) {
            BlockPos blockpos = pos.offset(facing);
            double d0 = this.getYOffset(world, blockpos);

            if (item.getItemDamage() == 3 || item.getItemDamage() == 4) {
//                if (!(player.capabilities.isCreativeMode && player.isSneaking())) {
                newPet(item, player, world, (double) blockpos.getX() + 0.5D, (double) blockpos.getY() + d0, (double) blockpos.getZ() + 0.5D);
                if (!player.capabilities.isCreativeMode) {
                    item.shrink(1);
                    if (item.getCount() <= 0)
                        player.inventory.setInventorySlotContents(player.inventory.currentItem, ItemStack.EMPTY);
                }
//                }
            } else {
                NBTTagCompound tags;
                tags = item.getTagCompound();

                tags.setTag("Pos", this.newDoubleNBTList((double) blockpos.getX() + 0.5D, (double) blockpos.getY() + d0, (double) blockpos.getZ() + 0.5D));
                tags.setTag("Rotation", this.newFloatNBTList(MathHelper.wrapDegrees(world.rand.nextFloat() * 360.0F), 0.0F));
                tags.setTag("Motion", this.newDoubleNBTList(0.0, 0.0, 0.0));
                tags.setFloat("FallDistance", 0.0f);

                if (item.getItemDamage() == 1 || item.getItemDamage() == 2) {
                    Entity entity = EntityList.createEntityByIDFromName(new ResourceLocation(tags.getString("Entity")), world);
                    entity.readFromNBT(tags);
                    world.spawnEntity(entity);
                    item.setTagCompound(null);
                    item.setItemDamage(0);
                    player.sendStatusMessage(new TextComponentTranslation("chat.pet_carrier.release_pet"), true);
                }
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

    private double getYOffset(World world, BlockPos pos) {
        AxisAlignedBB axisalignedbb = (new AxisAlignedBB(pos)).expand(0.0D, -1.0D, 0.0D);
        List<AxisAlignedBB> list = world.getCollisionBoxes(null, axisalignedbb);

        if (list.isEmpty()) {
            return 0.0D;
        } else {
            double d0 = axisalignedbb.minY;

            for (AxisAlignedBB axisalignedbb1 : list) {
                d0 = Math.max(axisalignedbb1.maxY, d0);
            }

            return d0 - (double) pos.getY();
        }
    }

    private void newPet(ItemStack item, EntityPlayer player, World world, double x, double y, double z) {
        EntityTameable pet = null;
        if (item.getItemDamage() == 3) {
            pet = new EntityCat(world);
        } else if (item.getItemDamage() == 4) {
            pet = new EntityWolf(world);
        }

        if (pet instanceof EntityCat && !((EntityCat) pet).canBeTamed(player)) {
            player.sendStatusMessage(new TextComponentTranslation("chat.info.tamed_limit_reached"), true);

        } else if (pet != null) {
            pet.setLocationAndAngles(x, y, z, MathHelper.wrapDegrees(world.rand.nextFloat() * 360.0F), 0.0F);
            pet.rotationYawHead = pet.rotationYaw;
            pet.renderYawOffset = pet.rotationYaw;
            world.spawnEntity(pet);
            if (pet instanceof EntityCat)
                ((EntityCat) pet).setTamed(true, player);
            else
                pet.setTamed(true);

            if (world.isRemote) {
                for (int i = 0; i < 7; ++i) {
                    double d0 = world.rand.nextGaussian() * 0.02D;
                    double d1 = world.rand.nextGaussian() * 0.02D;
                    double d2 = world.rand.nextGaussian() * 0.02D;
                    world.spawnParticle(EnumParticleTypes.HEART,
                            pet.posX + (double) (world.rand.nextFloat() * pet.width * 2.0F) - (double) pet.width,
                            pet.posY + 0.5D + (double) (world.rand.nextFloat() * pet.height),
                            pet.posZ + (double) (world.rand.nextFloat() * pet.width * 2.0F) - (double) pet.width,
                            d0, d1, d2);
                }
            }

            if (pet instanceof EntityCat) {
                ((EntityCat) pet).setHomePos(new BlockPos(x, y, z));
                player.sendStatusMessage(new TextComponentTranslation("chat.info.set_home", pet.getName(), x, y, z), true);
            }
            pet.getNavigator().clearPath();
            pet.setOwnerId(player.getUniqueID());
            pet.onInitialSpawn(world.getDifficultyForLocation(new BlockPos(pet)), null);
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
        return new TextComponentTranslation(unlocalizedName).getUnformattedText();
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void getSubItems(CreativeTabs tab, NonNullList<ItemStack> itemList) {
        if (tab == this.getCreativeTab()) {
            ItemStack cat = new ItemStack(CatItems.PET_CARRIER, 1);
            cat.setTagCompound(new NBTTagCompound());
            cat.setItemDamage(3);
            itemList.add(cat);
            itemList.add(new ItemStack(CatItems.PET_CARRIER, 1, 0));
        }
    }

    @SideOnly(Side.CLIENT)
    public void addInformation(ItemStack item, @Nullable World worldIn, List<String> tooltip, ITooltipFlag flagIn) {
        NBTTagCompound nbt = item.getTagCompound();
        if (nbt != null) {
            TextComponentTranslation cat = new TextComponentTranslation("tooltip.pet_carrier.adopt_cat");
            TextComponentTranslation dog = new TextComponentTranslation("tooltip.pet_carrier.adopt_dog");
            if (item.getItemDamage() == 3)
                tooltip.add(TextFormatting.ITALIC + cat.getFormattedText());
            else if (item.getItemDamage() == 4)
                tooltip.add(TextFormatting.ITALIC + dog.getFormattedText());

            else if (item.getItemDamage() != 0) {
                TextComponentTranslation species = new TextComponentTranslation("entity." + nbt.getString("id") + ".name");

                String owner = I18n.format("tooltip.pet_carrier.owner", nbt.getString("OwnerName"));
                if (nbt.hasKey("CustomName"))
                    tooltip.add(TextFormatting.AQUA + "\"" + nbt.getString("CustomName") + "\"");
                else
                    tooltip.add(TextFormatting.AQUA + species.getUnformattedText());

                if (item.getItemDamage() == 1)
                    tooltip.add(TextFormatting.ITALIC + Genetics.getPhenotypeDescription(nbt, true));

                tooltip.add(owner);
            }
        } else {
            TextComponentTranslation empty = new TextComponentTranslation("tooltip.pet_carrier.empty");
            tooltip.add(TextFormatting.AQUA + empty.getFormattedText());
        }
    }
}
