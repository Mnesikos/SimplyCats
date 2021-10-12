package com.github.mnesikos.simplycats.item;

import com.github.mnesikos.simplycats.SimplyCats;
import com.github.mnesikos.simplycats.entity.SimplyCatEntity;
import com.github.mnesikos.simplycats.entity.core.Genetics;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.passive.ParrotEntity;
import net.minecraft.entity.passive.TameableEntity;
import net.minecraft.entity.passive.WolfEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.ListNBT;
import net.minecraft.particles.ParticleTypes;
import net.minecraft.util.*;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import javax.annotation.Nullable;
import java.util.List;

public class PetCarrierItem extends Item {
    public PetCarrierItem() {
        super(new Item.Properties().tab(SimplyCats.ITEM_GROUP).stacksTo(1));
    }

    // The number of item models used by the client
    public int getNumModels() {
        return 5;
    }

    @Override
    public ActionResultType interactLivingEntity(ItemStack stack, PlayerEntity player, LivingEntity target, Hand hand) {
        if (target instanceof SimplyCatEntity || target instanceof WolfEntity || target instanceof ParrotEntity) {
            if (((TameableEntity) target).getOwner() == player) {
                if (stack.hasTag()) {
                    player.displayClientMessage(new TranslationTextComponent("chat.pet_carrier.full"), true);
                } else {
                    stack = player.getItemInHand(hand);
                    CompoundNBT tags = new CompoundNBT();
                    target.saveWithoutId(tags);
                    target.remove();

                    tags.putString("id", EntityList.getEntityString(target));
                    ResourceLocation key = EntityList.getKey(target.getClass());
                    tags.putString("Entity", key.toString());
                    if (!(target instanceof SimplyCatEntity))
                        tags.putString("OwnerName", player.getDisplayNameString());

                    player.displayClientMessage(new TranslationTextComponent("chat.pet_carrier.retrieve_pet"), true);

                    stack.setTag(tags);
                    if (target instanceof SimplyCatEntity)
                        stack.setItemDamage(1);
                    else
                        stack.setItemDamage(2);
                }
            }
        }
        return true;
    }

    @Override
    public ActionResult<ItemStack> use(World world, PlayerEntity player, Hand hand) {
        ItemStack item = player.getItemInHand(hand);
        if (!item.hasTag() || item.getItemDamage() == 0) {
            player.displayClientMessage(new TranslationTextComponent("chat.pet_carrier.empty"), true);
            return ActionResult.fail(player.getItemInHand(hand));
        }

        /*if (item.getItemDamage() == 3 && player.capabilities.isCreativeMode && player.isSneaking()) {
            BlockPos blockpos = pos.offset(facing);
            player.openGui(SimplyCats.instance, GUI_ID, player.world, blockpos.getX(), blockpos.getY(), blockpos.getZ());
        }*/

        if (!world.isClientSide) {
            BlockPos blockpos = pos.offset(facing);
            double d0 = this.getYOffset(world, blockpos);

            if (item.getItemDamage() == 3 || item.getItemDamage() == 4) {
//                if (!(player.capabilities.isCreativeMode && player.isSneaking())) {
                newPet(item, player, world, (double) blockpos.getX() + 0.5D, (double) blockpos.getY() + d0, (double) blockpos.getZ() + 0.5D);
                if (!player.isCreative())
                    item.shrink(1);
//                }
            } else {
                CompoundNBT tags;
                tags = item.getTag();

                tags.put("Pos", this.newDoubleNBTList((double) blockpos.getX() + 0.5D, (double) blockpos.getY() + d0, (double) blockpos.getZ() + 0.5D));
                tags.put("Rotation", this.newFloatNBTList(MathHelper.wrapDegrees(world.random.nextFloat() * 360.0F), 0.0F));
                tags.put("Motion", this.newDoubleNBTList(0.0, 0.0, 0.0));
                tags.putFloat("FallDistance", 0.0f);

                if (item.getItemDamage() == 1 || item.getItemDamage() == 2) {
                    Entity entity = EntityList.createEntityByIDFromName(new ResourceLocation(tags.getString("Entity")), world);
                    entity.load(tags);
                    world.spawnEntity(entity);
                    item.setTag(null);
                    item.setItemDamage(0);
                    player.displayClientMessage(new TranslationTextComponent("chat.pet_carrier.release_pet"), true);
                }
            }
        }
        return ActionResult.success(player.getItemInHand(hand));
    }

    private ListNBT newDoubleNBTList(final double... par1ArrayOfDouble) {
        final ListNBT nbttaglist = new ListNBT();
        for (final double d1 : par1ArrayOfDouble)
            nbttaglist.add(new NBTTagDouble(d1));
        return nbttaglist;
    }

    private ListNBT newFloatNBTList(float... numbers) {
        ListNBT nbttaglist = new ListNBT();
        for (float f : numbers)
            nbttaglist.add(new NBTTagFloat(f));
        return nbttaglist;
    }

    private double getYOffset(World world, BlockPos pos) {
        AxisAlignedBB axisalignedbb = (new AxisAlignedBB(pos)).inflate(0.0D, -1.0D, 0.0D);
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

    private void newPet(ItemStack item, PlayerEntity player, World world, double x, double y, double z) {
        TameableEntity pet = null;
        if (item.getItemDamage() == 3) {
            pet = new SimplyCatEntity(world);
        } else if (item.getItemDamage() == 4) {
            pet = new WolfEntity(world);
        }

        if (pet instanceof SimplyCatEntity && !((SimplyCatEntity) pet).canBeTamed(player)) {
            player.displayClientMessage(new TranslationTextComponent("chat.info.tamed_limit_reached"), true);

        } else if (pet != null) {
            pet.setLocationAndAngles(x, y, z, MathHelper.wrapDegrees(world.random.nextFloat() * 360.0F), 0.0F);
            pet.rotationYawHead = pet.rotationYaw;
            pet.renderYawOffset = pet.rotationYaw;
            world.spawnEntity(pet);
            if (pet instanceof SimplyCatEntity)
                ((SimplyCatEntity) pet).setTamed(true, player);
            else
                pet.setTame(true);

            if (world.isClientSide) {
                for (int i = 0; i < 7; ++i) {
                    double d0 = world.random.nextGaussian() * 0.02D;
                    double d1 = world.random.nextGaussian() * 0.02D;
                    double d2 = world.random.nextGaussian() * 0.02D;
                    world.addParticle(ParticleTypes.HEART, pet.getRandomX(1.0D), pet.getRandomY() + 0.5D, pet.getRandomZ(1.0D), d0, d1, d2);
                }
            }

            if (pet instanceof SimplyCatEntity) {
                ((SimplyCatEntity) pet).setHomePos(new BlockPos(x, y, z));
                player.displayClientMessage(new TranslationTextComponent("chat.info.set_home", pet.getName(), x, y, z), true);
            }
            pet.getNavigation().stop();
            pet.setOwnerUUID(player.getUUID());
            pet.onInitialSpawn(world.getDifficultyForLocation(new BlockPos(pet)), null);
            float health = pet.getMaxHealth();
            pet.setHealth(health);
        }
    }

    @Override
    public String getItemStackDisplayName(ItemStack item) {
        String unlocalizedName = this.getUnlocalizedNameInefficiently(item);
        if (!item.hasTag() || item.getItemDamage() == 0)
            unlocalizedName += ".empty.name";
        else
            unlocalizedName += ".full.name";
        return new TranslationTextComponent(unlocalizedName).getUnformattedText();
    }

    /*@Override
    @SideOnly(Side.CLIENT)
    public void getSubItems(ItemGroup tab, NonNullList<ItemStack> itemList) {
        if (tab == this.getItemCategory()) {
            ItemStack cat = new ItemStack(SCItems.PET_CARRIER.get(), 1);
            cat.setTag(new CompoundNBT());
            cat.setItemDamage(3);
            itemList.add(cat);
            itemList.add(new ItemStack(SCItems.PET_CARRIER.get(), 1, 0));
        }
    }*/

    @OnlyIn(Dist.CLIENT)
    @Override
    public void appendHoverText(ItemStack item, @Nullable World worldIn, List<ITextComponent> tooltip, ITooltipFlag flagIn) {
        CompoundNBT nbt = item.getTag();
        if (nbt != null) {
            TranslationTextComponent cat = new TranslationTextComponent("tooltip.pet_carrier.adopt_cat");
            TranslationTextComponent dog = new TranslationTextComponent("tooltip.pet_carrier.adopt_dog");
            if (item.getItemDamage() == 3)
                tooltip.add(TextFormatting.ITALIC + cat.getFormattedText());
            else if (item.getItemDamage() == 4)
                tooltip.add(TextFormatting.ITALIC + dog.getFormattedText());

            else if (item.getItemDamage() != 0) {
                TranslationTextComponent species = new TranslationTextComponent("entity." + nbt.getString("id") + ".name");

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
            TranslationTextComponent empty = new TranslationTextComponent("tooltip.pet_carrier.empty");
            tooltip.add(TextFormatting.AQUA + empty.getFormattedText());
        }
    }
}
