package com.github.mnesikos.simplycats.item;

import com.github.mnesikos.simplycats.SimplyCats;
import com.github.mnesikos.simplycats.entity.EntityCat;
import com.github.mnesikos.simplycats.entity.core.Genetics;
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
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.registry.EntityEntry;
import net.minecraftforge.fml.common.registry.ForgeRegistries;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nullable;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class ItemPetCarrier extends ItemBase {

    public ItemPetCarrier() {
        super("pet_carrier");
        this.setMaxStackSize(1);
        this.setHasSubtypes(true);
    }

    @Override
    public void registerItemModel() {
        for (int i = 0; i < 5; i++)
            SimplyCats.PROXY.registerItemRenderer(this, i, name);
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
                    target.writeToNBT(tags);
                    target.setDead();

                    tags.setString("id", EntityList.getEntityString(target));
                    Iterator var5 = ForgeRegistries.ENTITIES.getEntries().iterator();
                    while(var5.hasNext()) {
                        Map.Entry<ResourceLocation, EntityEntry> f = (Map.Entry)var5.next();
                        if (((EntityEntry)f.getValue()).getEntityClass() == target.getClass()) {
                            tags.setString("Entity", String.valueOf(f.getKey()));
                        }
                    }
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
            return EnumActionResult.SUCCESS;
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
                player.sendMessage(new TextComponentString(pet.getName() +
                        new TextComponentTranslation("chat.info.set_home").getFormattedText() +
                        " " + x + ", " + y + ", " + z));
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

    @Override @SideOnly(Side.CLIENT)
    public void getSubItems(CreativeTabs tab, NonNullList<ItemStack> itemList) {
        if (tab == this.getCreativeTab()) {
            ItemStack cat = new ItemStack(ModItems.PET_CARRIER, 1);
            cat.setTagCompound(new NBTTagCompound());
            cat.setItemDamage(3);
            itemList.add(cat);
            itemList.add(new ItemStack(ModItems.PET_CARRIER, 1, 0));
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

                TextComponentTranslation owner = new TextComponentTranslation("tooltip.pet_carrier.owner");
                TextComponentTranslation sex = new TextComponentTranslation("cat.sex." + (nbt.getString("Phaeomelanin").contains(Genetics.Phaeomelanin.MALE.getAllele()) ? "male" : "female") + ".name");
                if (nbt.hasKey("CustomName"))
                    tooltip.add(TextFormatting.AQUA + "\"" + nbt.getString("CustomName") + "\"" + " " + sex.getFormattedText());
                if (item.getItemDamage() == 2)
                    tooltip.add(TextFormatting.ITALIC + species.getUnformattedText());
                else if (item.getItemDamage() == 1) {
                    String eumelanin = Genetics.Eumelanin.getPhenotype(nbt.getString("Eumelanin"));
                    String phaeomelanin = Genetics.Phaeomelanin.getPhenotype(nbt.getString("Phaeomelanin"));
                    String dilution = Genetics.Dilution.getPhenotype(nbt.getString("Dilution"));
                    String diluteMod = Genetics.DiluteMod.getPhenotype(nbt.getString("DiluteMod"));
                    TextComponentTranslation base = new TextComponentTranslation("cat.base." + eumelanin + (phaeomelanin.equals(Genetics.Phaeomelanin.NOT_RED.toString().toLowerCase()) ? "" : "_" + phaeomelanin) + ".name");
                    if (dilution.equals(Genetics.Dilution.DILUTE.toString().toLowerCase())) {
                        base = new TextComponentTranslation("cat.base." + eumelanin + "_" + phaeomelanin + "_" + dilution + ".name");
                        if (diluteMod.equals(Genetics.DiluteMod.CARAMELIZED.toString().toLowerCase()))
                            base = new TextComponentTranslation("cat.base." + eumelanin + "_" + phaeomelanin + "_" + diluteMod + ".name");
                    }
                    if (phaeomelanin.equals(Genetics.Phaeomelanin.RED.toString().toLowerCase())) {
                        base = new TextComponentTranslation("cat.base." + phaeomelanin + ".name");
                        if (dilution.equals(Genetics.Dilution.DILUTE.toString().toLowerCase())) {
                            base = new TextComponentTranslation("cat.base." + phaeomelanin + "_" + dilution + ".name");
                            if (diluteMod.equals(Genetics.DiluteMod.CARAMELIZED.toString().toLowerCase()))
                                base = new TextComponentTranslation("cat.base." + phaeomelanin + "_" + diluteMod + ".name");
                        }
                    }

                    String agouti = Genetics.Agouti.getPhenotype(nbt.getString("Agouti"));
                    String tabby1 = Genetics.Tabby.getPhenotype(nbt.getString("Tabby"));
                    String spotted = Genetics.Spotted.getPhenotype(nbt.getString("Spotted"));
                    String ticked = Genetics.Ticked.getPhenotype(nbt.getString("Ticked"));
                    TextComponentTranslation tabby = new TextComponentTranslation("");
                    if (agouti.equals(Genetics.Agouti.TABBY.toString().toLowerCase()) || phaeomelanin.equals(Genetics.Phaeomelanin.RED.toString().toLowerCase())) {
                        tabby = new TextComponentTranslation("cat.tabby." + tabby1 + ".name");
                        if (spotted.equals(Genetics.Spotted.BROKEN.toString().toLowerCase()) || spotted.equals(Genetics.Spotted.SPOTTED.toString().toLowerCase()))
                            tabby = new TextComponentTranslation("cat.tabby." + spotted + ".name");
                        if (ticked.equals(Genetics.Ticked.TICKED.toString().toLowerCase()))
                            tabby = new TextComponentTranslation("cat.tabby." + ticked + ".name");
                    }

                    String colorpoint = Genetics.Colorpoint.getPhenotype(nbt.getString("Colorpoint"));
                    TextComponentTranslation point = new TextComponentTranslation("");
                    if (!colorpoint.equals(Genetics.Colorpoint.NOT_POINTED.toString().toLowerCase())) {
                        point = new TextComponentTranslation("cat.point." + colorpoint + ".name");
                    }

                    tooltip.add(TextFormatting.ITALIC + base.getUnformattedText() +
                            (tabby.getUnformattedText().equals("") ? "" : " " + tabby.getUnformattedText()) +
                            (point.getUnformattedText().equals("") ? "" : " " + point.getUnformattedText()) +
                            " " + species.getFormattedText());
                }

                tooltip.add(owner.getUnformattedText() + " " + nbt.getString("ownerName"));
            }
        } else {
            TextComponentTranslation empty = new TextComponentTranslation("tooltip.pet_carrier.empty");
            tooltip.add(TextFormatting.AQUA + empty.getFormattedText());
        }
    }
}
