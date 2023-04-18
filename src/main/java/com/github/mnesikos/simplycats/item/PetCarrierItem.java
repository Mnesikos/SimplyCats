package com.github.mnesikos.simplycats.item;

import com.github.mnesikos.simplycats.SimplyCats;
import com.github.mnesikos.simplycats.entity.SimplyCatEntity;
import com.github.mnesikos.simplycats.entity.genetics.FelineGenome;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.SpawnReason;
import net.minecraft.entity.passive.ParrotEntity;
import net.minecraft.entity.passive.RabbitEntity;
import net.minecraft.entity.passive.TameableEntity;
import net.minecraft.entity.passive.WolfEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUseContext;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.*;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import javax.annotation.Nullable;
import java.util.List;

public class PetCarrierItem extends Item {
    public PetCarrierItem() {
        super(new Item.Properties().tab(SimplyCats.ITEM_GROUP).stacksTo(1));
    }

    @Override
    public ActionResultType interactLivingEntity(ItemStack stack, PlayerEntity player, LivingEntity target, Hand hand) {
        if (target instanceof SimplyCatEntity || target instanceof WolfEntity || target instanceof ParrotEntity) {
            if (((TameableEntity) target).getOwner() == player) {
                if (stack.hasTag()) {
                    player.displayClientMessage(new TranslationTextComponent("chat.pet_carrier.full"), true);
                    return ActionResultType.PASS;
                } else {
                    target.revive();
                    if (player.level.isClientSide) return ActionResultType.SUCCESS;

                    CompoundNBT tags = new CompoundNBT();
                    target.save(tags);

                    ResourceLocation key = EntityType.getKey(target.getType());
                    tags.putString("id", key.toString());
                    if (!(target instanceof SimplyCatEntity))
                        tags.putString("OwnerName", player.getName().getString());
                    if (target.hasCustomName()) tags.putString("DisplayName", target.getDisplayName().getString());

                    target.remove();
                    player.displayClientMessage(new TranslationTextComponent("chat.pet_carrier.retrieve_pet"), true);

                    ItemStack newStack = new ItemStack(this);
                    newStack.setTag(tags);
                    if (target instanceof SimplyCatEntity)
                        newStack.setDamageValue(1);
                    else
                        newStack.setDamageValue(2);

                    player.setItemInHand(hand, newStack);
                }
            }
        }
        return ActionResultType.SUCCESS;
    }

    @Override
    public ActionResultType useOn(ItemUseContext context) {
        PlayerEntity player = context.getPlayer();
        World world = context.getLevel();
        ItemStack item = context.getItemInHand();
        if (!item.hasTag() || item.getDamageValue() == 0) {
            player.displayClientMessage(new TranslationTextComponent("chat.pet_carrier.empty"), true);
            return ActionResultType.PASS;
        }

        /*if (item.getDamageValue() == 3 && player.capabilities.isCreativeMode && player.isSneaking()) {
            BlockPos blockpos = pos.offset(facing);
            player.openGui(SimplyCats.instance, GUI_ID, player.world, blockpos.getX(), blockpos.getY(), blockpos.getZ());
        }*/

        if (!world.isClientSide) {
            BlockPos blockPos = new BlockPos(context.getClickedPos()).relative(context.getClickedFace());

            if (item.getDamageValue() >= 3 && item.getDamageValue() <= 6) {
//                if (!(player.capabilities.isCreativeMode && player.isSneaking())) {
                newPet(item, player, world, blockPos);
                if (!player.isCreative())
                    item.shrink(1);
//                }
            } else {
                CompoundNBT tags;
                tags = item.getTag();

                if (item.getDamageValue() == 1 || item.getDamageValue() == 2) {
                    Entity entity = EntityType.loadEntityRecursive(tags, world, entity1 -> entity1);
                    if (entity != null && entity instanceof TameableEntity) {
                        entity.absMoveTo(blockPos.getX() + 0.5D, blockPos.getY(), blockPos.getZ() + 0.5D, context.getRotation(), 0);
                        ((TameableEntity) entity).setOrderedToSit(true);
                        entity.setUUID(tags.getUUID("UUID"));
                        world.addFreshEntity(entity);

                        item.shrink(1);
                        player.setItemInHand(context.getHand(), new ItemStack(this));
                        player.displayClientMessage(new TranslationTextComponent("chat.pet_carrier.release_pet"), true);
                    }
                }
            }
        }
        return ActionResultType.SUCCESS;
    }

    private void newPet(ItemStack item, PlayerEntity player, World world, BlockPos blockPos) {
        TameableEntity pet = null;
        if (item.getDamageValue() == 3)
            pet = (SimplyCatEntity) SimplyCats.CAT.get().spawn((ServerWorld) world, null, player, blockPos, SpawnReason.SPAWN_EGG, false, false);
        else if (item.getDamageValue() == 4)
            pet = (WolfEntity) EntityType.WOLF.spawn((ServerWorld) world, null, player, blockPos, SpawnReason.SPAWN_EGG, false, false);
        else if (item.getDamageValue() == 5)
            pet = (ParrotEntity) EntityType.PARROT.spawn((ServerWorld) world, null, player, blockPos, SpawnReason.SPAWN_EGG, false, false);

        if (pet instanceof SimplyCatEntity && !((SimplyCatEntity) pet).canBeTamed(player)) {
            player.displayClientMessage(new TranslationTextComponent("chat.info.tamed_limit_reached"), true);

        } else if (pet != null) {
            pet.setOrderedToSit(true);
            if (pet instanceof SimplyCatEntity)
                ((SimplyCatEntity) pet).setTamed(true, player);
            else
                pet.tame(player);

            if (pet instanceof SimplyCatEntity) {
                ((SimplyCatEntity) pet).setHomePos(blockPos);
                player.displayClientMessage(new TranslationTextComponent("chat.info.set_home", pet.getName(), blockPos.getX(), blockPos.getY(), blockPos.getZ()), true);
            }
            pet.getNavigation().stop();
            float health = pet.getMaxHealth();
            pet.setHealth(health);

        } else if (item.getDamageValue() == 6) {
            RabbitEntity rabbit = (RabbitEntity) EntityType.RABBIT.spawn((ServerWorld) world, null, player, blockPos, SpawnReason.SPAWN_EGG, false, false);
            if (rabbit != null) {
                rabbit.getNavigation().stop();
                rabbit.setHealth(rabbit.getMaxHealth());
            }
        }
    }

    @Override
    public ITextComponent getName(ItemStack item) {
        String unlocalizedName = super.getName(item).getString();
        if (!item.hasTag() || item.getDamageValue() == 0)
            unlocalizedName += "_empty";
        else
            unlocalizedName += "_full";
        return new TranslationTextComponent(unlocalizedName);
    }

    @Override
    public void fillItemCategory(ItemGroup tab, NonNullList<ItemStack> itemList) {
        if (tab == this.getItemCategory()) {
            ItemStack cat = new ItemStack(SCItems.PET_CARRIER.get(), 1, new CompoundNBT());
            cat.setDamageValue(3);
            itemList.add(cat);
            itemList.add(new ItemStack(SCItems.PET_CARRIER.get(), 1));
        }
    }

    @OnlyIn(Dist.CLIENT)
    @Override
    public void appendHoverText(ItemStack item, @Nullable World worldIn, List<ITextComponent> tooltip, ITooltipFlag flagIn) {
        CompoundNBT nbt = item.getTag();
        if (nbt != null) {
            if (item.getDamageValue() == 3)
                tooltip.add(new TranslationTextComponent("tooltip.pet_carrier.adopt_cat").withStyle(TextFormatting.ITALIC));
            else if (item.getDamageValue() == 4)
                tooltip.add(new TranslationTextComponent("tooltip.pet_carrier.adopt_dog").withStyle(TextFormatting.ITALIC));
            else if (item.getDamageValue() == 5)
                tooltip.add(new TranslationTextComponent("tooltip.pet_carrier.adopt_parrot").withStyle(TextFormatting.ITALIC));
            else if (item.getDamageValue() == 6)
                tooltip.add(new TranslationTextComponent("tooltip.pet_carrier.adopt_rabbit").withStyle(TextFormatting.ITALIC));

            else if (item.getDamageValue() != 0) {
                TranslationTextComponent species = new TranslationTextComponent(Util.makeDescriptionId("entity", new ResourceLocation(nbt.getString("id"))));

                TranslationTextComponent owner = new TranslationTextComponent("tooltip.pet_carrier.owner", nbt.getString("OwnerName"));
                if (nbt.contains("DisplayName"))
                    tooltip.add(new StringTextComponent("\"" + nbt.getString("DisplayName") + "\"").withStyle(TextFormatting.AQUA));
                else
                    tooltip.add(species.withStyle(TextFormatting.AQUA));

                if (item.getDamageValue() == 1)
                    tooltip.add(FelineGenome.getPhenotypeDescription(nbt, true).withStyle(TextFormatting.ITALIC));

                tooltip.add(owner);
            }
        } else {
            TranslationTextComponent empty = new TranslationTextComponent("tooltip.pet_carrier.empty");
            tooltip.add(empty.withStyle(TextFormatting.AQUA));
        }
    }
}
