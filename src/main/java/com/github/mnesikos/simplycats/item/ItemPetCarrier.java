package com.github.mnesikos.simplycats.item;

import com.github.mnesikos.simplycats.SimplyCats;
import com.github.mnesikos.simplycats.entity.EntityCat;
import com.github.mnesikos.simplycats.entity.core.Genetics;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.SpawnReason;
import net.minecraft.entity.passive.TameableEntity;
import net.minecraft.entity.passive.WolfEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUseContext;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.DoubleNBT;
import net.minecraft.nbt.FloatNBT;
import net.minecraft.nbt.ListNBT;
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

import javax.annotation.Nullable;
import java.util.List;
import java.util.Objects;

public class ItemPetCarrier extends ItemBase {

    public ItemPetCarrier() {
        super(new Properties().group(SimplyCats.GROUP).maxStackSize(1));
    }

    @Override
    public boolean itemInteractionForEntity(ItemStack stack, PlayerEntity player, LivingEntity target, Hand hand) {
        if (target instanceof EntityCat || target instanceof WolfEntity) {
            if (((TameableEntity) target).getOwner() == player) {
                if (stack.getDamage() != 0) {
                    if (target.world.isRemote)
                        player.sendMessage(new TranslationTextComponent("chat.pet_carrier.full"));
                } else {
                    stack = player.getHeldItem(hand);
                    CompoundNBT tags = new CompoundNBT();
                    target.writeAdditional(tags);
                    target.remove();

                    tags.putString("id", target.getCachedUniqueIdString());
                    tags.putString("type", EntityType.getKey(target.getType()).toString());
                    tags.putString("OwnerName", player.getDisplayName().getUnformattedComponentText());
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
        BlockPos spawnPos = new BlockPos(blockpos.getX() + 0.5D, blockpos.getY(), blockpos.getZ() + 0.5D);

        if (item.getDamage() == 3 || item.getDamage() == 4) {
            newPet(item, player, world, (double)blockpos.getX() + 0.5D, blockpos.getY(), (double)blockpos.getZ() + 0.5D);
            if (!player.abilities.isCreativeMode) {
                item.shrink(1);
                if (item.getCount() <= 0)
                    player.inventory.setInventorySlotContents(player.inventory.currentItem, ItemStack.EMPTY);
            }
        } else {
            CompoundNBT tags;
            tags = item.getTag();

//            tags.put("Pos", this.newDoubleNBTList((double)blockpos.getX() + 0.5D, (double)blockpos.getY() + 1.0D, (double)blockpos.getZ() + 0.5D));
            tags.put("Rotation", this.newFloatNBTList(MathHelper.wrapDegrees(world.rand.nextFloat() * 360.0F), 0.0F));
            tags.put("Motion", this.newDoubleNBTList(0.0, 0.0, 0.0));
            tags.putFloat("FallDistance", 0.0f);

            if (item.getDamage() == 1 || item.getDamage() == 2) {
                EntityType<?> entitytype = EntityType.byKey(tags.getString("type")).orElse(null);
                if (entitytype != null) {
                    Entity entity = entitytype.create(world, tags, null, null, spawnPos, SpawnReason.TRIGGERED, false, false);
                    entity.read(tags);
                    world.addEntity(entity);
                    item.setTag(null);
                    item.setDamage(0);
                }
            }
        }
        player.swingArm(player.getActiveHand());
        return ActionResultType.SUCCESS;
    }

    /**
     * creates a NBT list from the array of doubles passed to this function
     */
    private ListNBT newDoubleNBTList(final double... numbers) {
        final ListNBT nbttaglist = new ListNBT();
        for (double d1 : numbers)
            nbttaglist.add(DoubleNBT.valueOf(d1));
        return nbttaglist;
    }

    /**
     * Returns a new NBTTagList filled with the specified floats
     */
    private ListNBT newFloatNBTList(float... numbers) {
        ListNBT nbttaglist = new ListNBT();
        for (float f : numbers)
            nbttaglist.add(FloatNBT.valueOf(f));
        return nbttaglist;
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
                            pet.getPosX() + (double) (world.rand.nextFloat() * pet.getWidth() * 2.0F) - (double) pet.getWidth(),
                            pet.getPosY() + 0.5D + (double) (world.rand.nextFloat() * pet.getHeight()),
                            pet.getPosZ() + (double) (world.rand.nextFloat() * pet.getWidth() * 2.0F) - (double) pet.getWidth(),
                            d0, d1, d2);
                }
            }

            if (pet instanceof EntityCat) {
                ((EntityCat) pet).setHomePos(new BlockPos(x, y, z));
                player.sendMessage(new TranslationTextComponent("chat.info.set_home", ((EntityCat)pet).getName(), x, y, z));
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

    @Override
    public void fillItemGroup(ItemGroup group, NonNullList<ItemStack> items) {
        if (group == this.getGroup()) {
            ItemStack cat = new ItemStack(this, 1);
            cat.setTag(new CompoundNBT());
            cat.setDamage(3);
            items.add(cat);
            items.add(new ItemStack(this, 1));
        }
    }

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

                TranslationTextComponent owner = new TranslationTextComponent("tooltip.pet_carrier.owner", nbt.getString("OwnerName"));
                if (nbt.contains("CustomName"))
                    tooltip.add(new StringTextComponent(TextFormatting.AQUA + "\"" + nbt.getString("CustomName") + "\""));
                if (item.getDamage() == 2)
                    tooltip.add(new StringTextComponent(TextFormatting.ITALIC + species.getUnformattedComponentText()));

                if (item.getDamage() == 1) {
                    tooltip.add(new StringTextComponent(TextFormatting.ITALIC + Genetics.getPhenotypeDescription(nbt, true)));
                }

                tooltip.add(owner);
            }
        } else {
            TranslationTextComponent empty = new TranslationTextComponent("tooltip.pet_carrier.empty");
            tooltip.add(new StringTextComponent(TextFormatting.AQUA + empty.getFormattedText()));
        }
    }
}
