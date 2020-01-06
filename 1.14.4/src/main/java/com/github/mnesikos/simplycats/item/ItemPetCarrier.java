package com.github.mnesikos.simplycats.item;

/*import com.github.mnesikos.simplycats.SimplyCats;
import com.github.mnesikos.simplycats.entity.EntityCat;
import com.github.mnesikos.simplycats.entity.core.Genetics;
import com.github.mnesikos.simplycats.init.ModItems;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.SpawnReason;
import net.minecraft.entity.passive.TameableEntity;
import net.minecraft.entity.passive.WolfEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUseContext;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.particles.ParticleTypes;
import net.minecraft.util.*;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.registries.ForgeRegistries;

import javax.annotation.Nullable;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class ItemPetCarrier extends ItemBase {

    public ItemPetCarrier() {
        super("pet_carrier", new Properties().group(SimplyCats.GROUP).maxStackSize(1));
    }

    @Override
    public boolean itemInteractionForEntity(ItemStack stack, PlayerEntity player, LivingEntity target, Hand hand) {
        if (target instanceof EntityCat || target instanceof WolfEntity) {
            if (((TameableEntity) target).getOwner() == player) {
                if (stack.hasTag()) {
                    if (target.world.isRemote)
                        player.sendMessage(new TranslationTextComponent("chat.pet_carrier.full"));
                } else {
                    stack = player.getHeldItem(hand);
                    CompoundNBT tags = new CompoundNBT();
                    target.writeAdditional(tags);
                    target.remove();

                    tags.putString("id", EntityType.getKey(target.getType()).toString());
                    Iterator var5 = ForgeRegistries.ENTITIES.getEntries().iterator();
                    while(var5.hasNext()) {
                        Map.Entry<ResourceLocation, EntityEntry> f = (Map.Entry)var5.next();
                        if (((EntityEntry)f.getValue()).getEntityClass() == target.getClass()) {
                            tags.putString("Entity", String.valueOf(f.getKey()));
                        }
                    }
                    tags.putString("ownerName", player.getDisplayName().getString());
                    if (target.world.isRemote)
                        player.sendMessage(new TranslationTextComponent("chat.pet_carrier.retrieve_pet"));

                    stack.setTag(tags);
                    player.swingArm(hand);
                    if (target instanceof EntityCat)
                        stack.setDamage(1);
                    else
                        stack.setDamage(2);
                }
            }
        }
        return true;
    }

    @Override
    public ActionResultType onItemUse(ItemUseContext context) {
        PlayerEntity player = context.getPlayer();
        World world = context.getWorld();
        BlockPos pos = context.getPos();
        Direction facing = context.getFace();
        
        ItemStack item = context.getItem();
        if (!item.hasTag() || item.getDamage() == 0) {
            if (world.isRemote)
                player.sendMessage(new TranslationTextComponent("chat.pet_carrier.empty"));
            return ActionResultType.FAIL;
        }

        if (world.isRemote) {
            if (item.getDamage() == 1 || item.getDamage() == 2)
                player.sendMessage(new TranslationTextComponent("chat.pet_carrier.release_pet"));
            return ActionResultType.SUCCESS;
        }

        BlockPos blockpos = pos.offset(facing);
        double d0 = this.getYOffset(world, blockpos);

        if (item.getDamage() == 3 || item.getDamage() == 4) {
            newPet(item, player, world, (double)blockpos.getX() + 0.5D, (double)blockpos.getY() + d0, (double)blockpos.getZ() + 0.5D);
            if (!player.abilities.isCreativeMode) {
                item.shrink(1);
                if (item.getCount() <= 0)
                    player.inventory.setInventorySlotContents(player.inventory.currentItem, ItemStack.EMPTY);
            }
        } else {
            CompoundNBT tags;
            tags = item.getTag();

            tags.setTag("Pos", this.newDoubleNBTList((double)blockpos.getX() + 0.5D, (double)blockpos.getY() + d0, (double)blockpos.getZ() + 0.5D));
            tags.setTag("Rotation", this.newFloatNBTList(MathHelper.wrapDegrees(world.rand.nextFloat() * 360.0F), 0.0F));
            tags.setTag("Motion", this.newDoubleNBTList(0.0, 0.0, 0.0));
            tags.setFloat("FallDistance", 0.0f);

            if (item.getDamage() == 1 || item.getDamage() == 2) {
                Entity entity = EntityList.createEntityByIDFromName(new ResourceLocation(tags.getString("Entity")), world);
                entity.read(tags);
                world.spawnEntity(entity);
                item.setTagCompound(null);
                item.setItemDamage(0);
            }
        }
        player.swingArm(hand);
        return ActionResultType.SUCCESS;
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
        List<AxisAlignedBB> list = p_190909_1_.getCollisionShapes(null, axisalignedbb);

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

    private void newPet(ItemStack item, PlayerEntity player, World world, double x, double y, double z) {
        TameableEntity pet = null;
        if (item.getDamage() == 3) {
            pet = new EntityCat(SimplyCats.CAT, world);
        } else if (item.getDamage() == 4) {
            pet = new WolfEntity(EntityType.WOLF, world);
        }

        if (pet != null) {
            pet.setLocationAndAngles(x, y, z, MathHelper.wrapDegrees(world.rand.nextFloat() * 360.0F), 0.0F);
            pet.rotationYawHead = pet.rotationYaw;
            pet.renderYawOffset = pet.rotationYaw;
            world.addEntity(pet);
            pet.setTamed(true);

            if (world.isRemote) {
                for (int i = 0; i < 7; ++i) {
                    double d0 = world.rand.nextGaussian() * 0.02D;
                    double d1 = world.rand.nextGaussian() * 0.02D;
                    double d2 = world.rand.nextGaussian() * 0.02D;
                    world.addParticle(ParticleTypes.HEART,
                            pet.posX + (double) (world.rand.nextFloat() * pet.getWidth() * 2.0F) - (double) pet.getWidth(),
                            pet.posY + 0.5D + (double) (world.rand.nextFloat() * pet.getHeight()),
                            pet.posZ + (double) (world.rand.nextFloat() * pet.getWidth() * 2.0F) - (double) pet.getWidth(),
                            d0, d1, d2);
                }
            }

            if (pet instanceof EntityCat) {
                ((EntityCat) pet).setHomePos(new BlockPos(x, y, z));
                player.sendMessage(new StringTextComponent(pet.getName() +
                        new TranslationTextComponent("chat.info.set_home").getFormattedText() +
                        " " + x + ", " + y + ", " + z));
            }
            pet.getNavigator().clearPath();
            pet.setOwnerId(player.getUniqueID());
            pet.onInitialSpawn(world, world.getDifficultyForLocation(new BlockPos(pet)), SpawnReason.TRIGGERED, null, pet.getPersistentData());
            float health = pet.getMaxHealth();
            pet.setHealth(health);
        }
    }

    @Override
    public ITextComponent getDisplayName(ItemStack item) {
        String unlocalizedName = this.getTranslationKey(item);
        if (!item.hasTag() || item.getDamage() == 0)
            unlocalizedName += ".empty.name";
        else
            unlocalizedName += ".full.name";
        return new TranslationTextComponent(unlocalizedName);
    }

    /*@Override @SideOnly(Side.CLIENT)
    public void getSubItems(CreativeTabs tab, NonNullList<ItemStack> itemList) {
        if (tab == this.getCreativeTab()) {
            ItemStack cat = new ItemStack(ModItems.PET_CARRIER, 1);
            cat.setTagCompound(new CompoundNBT());
            cat.setItemDamage(3);
            itemList.add(cat);
            itemList.add(new ItemStack(ModItems.PET_CARRIER, 1, 0));
        }
    }*

    @Override @OnlyIn(Dist.CLIENT)
    public void addInformation(ItemStack item, @Nullable World worldIn, List<ITextComponent> tooltip, ITooltipFlag flagIn) {
        CompoundNBT nbt = item.getTag();
        if (nbt != null) {
            TranslationTextComponent cat = new TranslationTextComponent("tooltip.pet_carrier.adopt_cat");
            TranslationTextComponent dog = new TranslationTextComponent("tooltip.pet_carrier.adopt_dog");
            if (item.getDamage() == 3)
                tooltip.add(new StringTextComponent(TextFormatting.ITALIC + cat.getFormattedText()));
            else if (item.getDamage() == 4)
                tooltip.add(new StringTextComponent(TextFormatting.ITALIC + dog.getFormattedText()));

            else if (item.getDamage() != 0) {
                TranslationTextComponent species = new TranslationTextComponent("entity." + nbt.getString("id") + ".name");

                TranslationTextComponent owner = new TranslationTextComponent("tooltip.pet_carrier.owner");
                TranslationTextComponent sex = new TranslationTextComponent("cat.sex." + (nbt.getString("Phaeomelanin").contains(Genetics.Phaeomelanin.MALE.getAllele()) ? "male" : "female") + ".name");
                if (nbt.contains("CustomName"))
                    tooltip.add(new StringTextComponent(TextFormatting.AQUA + "\"" + nbt.getString("CustomName") + "\"" + " " + sex.getFormattedText()));
                if (item.getDamage() == 2)
                    tooltip.add(new StringTextComponent(TextFormatting.ITALIC + species.getUnformattedComponentText()));
                else if (item.getDamage() == 1) {
                    String eumelanin = Genetics.Eumelanin.getPhenotype(nbt.getString("Eumelanin"));
                    String phaeomelanin = Genetics.Phaeomelanin.getPhenotype(nbt.getString("Phaeomelanin"));
                    String dilution = Genetics.Dilution.getPhenotype(nbt.getString("Dilution"));
                    String diluteMod = Genetics.DiluteMod.getPhenotype(nbt.getString("DiluteMod"));
                    TranslationTextComponent base = new TranslationTextComponent("cat.base." + eumelanin + (phaeomelanin.equals(Genetics.Phaeomelanin.NOT_RED.toString().toLowerCase()) ? "" : "_" + phaeomelanin) + ".name");
                    if (dilution.equals(Genetics.Dilution.DILUTE.toString().toLowerCase())) {
                        base = new TranslationTextComponent("cat.base." + eumelanin + "_" + phaeomelanin + "_" + dilution + ".name");
                        if (diluteMod.equals(Genetics.DiluteMod.CARAMELIZED.toString().toLowerCase()))
                            base = new TranslationTextComponent("cat.base." + eumelanin + "_" + phaeomelanin + "_" + diluteMod + ".name");
                    }
                    if (phaeomelanin.equals(Genetics.Phaeomelanin.RED.toString().toLowerCase())) {
                        base = new TranslationTextComponent("cat.base." + phaeomelanin + ".name");
                        if (dilution.equals(Genetics.Dilution.DILUTE.toString().toLowerCase())) {
                            base = new TranslationTextComponent("cat.base." + phaeomelanin + "_" + dilution + ".name");
                            if (diluteMod.equals(Genetics.DiluteMod.CARAMELIZED.toString().toLowerCase()))
                                base = new TranslationTextComponent("cat.base." + phaeomelanin + "_" + diluteMod + ".name");
                        }
                    }

                    String agouti = Genetics.Agouti.getPhenotype(nbt.getString("Agouti"));
                    String tabby1 = Genetics.Tabby.getPhenotype(nbt.getString("Tabby"));
                    String spotted = Genetics.Spotted.getPhenotype(nbt.getString("Spotted"));
                    String ticked = Genetics.Ticked.getPhenotype(nbt.getString("Ticked"));
                    TranslationTextComponent tabby = new TranslationTextComponent("");
                    if (agouti.equals(Genetics.Agouti.TABBY.toString().toLowerCase()) || phaeomelanin.equals(Genetics.Phaeomelanin.RED.toString().toLowerCase())) {
                        tabby = new TranslationTextComponent("cat.tabby." + tabby1 + ".name");
                        if (spotted.equals(Genetics.Spotted.BROKEN.toString().toLowerCase()) || spotted.equals(Genetics.Spotted.SPOTTED.toString().toLowerCase()))
                            tabby = new TranslationTextComponent("cat.tabby." + spotted + ".name");
                        if (ticked.equals(Genetics.Ticked.TICKED.toString().toLowerCase()))
                            tabby = new TranslationTextComponent("cat.tabby." + ticked + ".name");
                    }

                    String colorpoint = Genetics.Colorpoint.getPhenotype(nbt.getString("Colorpoint"));
                    TranslationTextComponent point = new TranslationTextComponent("");
                    if (!colorpoint.equals(Genetics.Colorpoint.NOT_POINTED.toString().toLowerCase())) {
                        point = new TranslationTextComponent("cat.point." + colorpoint + ".name");
                    }

                    tooltip.add(new StringTextComponent(TextFormatting.ITALIC + base.getUnformattedComponentText() +
                            (tabby.getUnformattedComponentText().equals("") ? "" : " " + tabby.getUnformattedComponentText()) +
                            (point.getUnformattedComponentText().equals("") ? "" : " " + point.getUnformattedComponentText()) +
                            " " + species.getFormattedText()));
                }

                tooltip.add(new StringTextComponent(owner.getUnformattedComponentText() + " " + nbt.getString("ownerName")));
            }
        } else {
            TranslationTextComponent empty = new TranslationTextComponent("tooltip.pet_carrier.empty");
            tooltip.add(new StringTextComponent(TextFormatting.AQUA + empty.getFormattedText()));
        }
    }
}
*/