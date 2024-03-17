package com.github.mnesikos.simplycats.item;

import com.github.mnesikos.simplycats.SimplyCats;
import com.github.mnesikos.simplycats.entity.SimplyCatEntity;
import com.github.mnesikos.simplycats.entity.core.Genetics;
import net.minecraft.ChatFormatting;
import net.minecraft.Util;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.animal.Parrot;
import net.minecraft.world.entity.animal.Rabbit;
import net.minecraft.world.entity.animal.Wolf;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import javax.annotation.Nullable;
import java.util.List;

public class PetCarrierItem extends Item {
    public PetCarrierItem() {
        super(new Item.Properties().stacksTo(1));
    }

    @Override
    public InteractionResult interactLivingEntity(ItemStack stack, Player player, LivingEntity target, InteractionHand hand) {
        if (target instanceof SimplyCatEntity || target instanceof Wolf || target instanceof Parrot) {
            if (((TamableAnimal) target).getOwner() == player) {
                if (stack.hasTag()) {
                    player.displayClientMessage(Component.translatable("chat.pet_carrier.full"), true);
                    return InteractionResult.PASS;
                } else {
                    target.revive();
                    if (player.level().isClientSide) return InteractionResult.SUCCESS;

                    CompoundTag tags = new CompoundTag();
                    target.save(tags);

                    ResourceLocation key = EntityType.getKey(target.getType());
                    tags.putString("id", key.toString());
                    if (!(target instanceof SimplyCatEntity))
                        tags.putString("OwnerName", player.getName().getString());
                    if (target.hasCustomName()) tags.putString("DisplayName", target.getDisplayName().getString());

                    target.discard();
                    player.displayClientMessage(Component.translatable("chat.pet_carrier.retrieve_pet"), true);

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
        return InteractionResult.SUCCESS;
    }

    @Override
    public InteractionResult useOn(UseOnContext context) {
        Player player = context.getPlayer();
        Level world = context.getLevel();
        ItemStack item = context.getItemInHand();
        if (!item.hasTag() || item.getDamageValue() == 0) {
            player.displayClientMessage(Component.translatable("chat.pet_carrier.empty"), true);
            return InteractionResult.PASS;
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
                CompoundTag tags;
                tags = item.getTag();

                if (item.getDamageValue() == 1 || item.getDamageValue() == 2) {
                    Entity entity = EntityType.loadEntityRecursive(tags, world, entity1 -> entity1);
                    if (entity != null && entity instanceof TamableAnimal) {
                        entity.absMoveTo(blockPos.getX() + 0.5D, blockPos.getY(), blockPos.getZ() + 0.5D, context.getRotation(), 0);
                        ((TamableAnimal) entity).setOrderedToSit(true);
                        entity.setUUID(tags.getUUID("UUID"));
                        world.addFreshEntity(entity);

                        item.shrink(1);
                        player.setItemInHand(context.getHand(), new ItemStack(this));
                        player.displayClientMessage(Component.translatable("chat.pet_carrier.release_pet"), true);
                    }
                }
            }
        }
        return InteractionResult.SUCCESS;
    }

    private void newPet(ItemStack item, Player player, Level world, BlockPos blockPos) {
        TamableAnimal pet = null;
        if (item.getDamageValue() == 3)
            pet = (SimplyCatEntity) SimplyCats.CAT.get().spawn((ServerLevel) world, null, player, blockPos, MobSpawnType.SPAWN_EGG, false, false);
        else if (item.getDamageValue() == 4)
            pet = (Wolf) EntityType.WOLF.spawn((ServerLevel) world, null, player, blockPos, MobSpawnType.SPAWN_EGG, false, false);
        else if (item.getDamageValue() == 5)
            pet = (Parrot) EntityType.PARROT.spawn((ServerLevel) world, null, player, blockPos, MobSpawnType.SPAWN_EGG, false, false);

        if (pet instanceof SimplyCatEntity && !((SimplyCatEntity) pet).canBeTamed(player)) {
            player.displayClientMessage(Component.translatable("chat.info.tamed_limit_reached"), true);

        } else if (pet != null) {
            pet.setOrderedToSit(true);
            if (pet instanceof SimplyCatEntity)
                ((SimplyCatEntity) pet).setTamed(true, player);
            else
                pet.tame(player);

            if (pet instanceof SimplyCatEntity) {
                ((SimplyCatEntity) pet).setHomePos(blockPos);
                player.displayClientMessage(Component.translatable("chat.info.set_home", pet.getName(), blockPos.getX(), blockPos.getY(), blockPos.getZ()), true);
            }
            pet.getNavigation().stop();
            float health = pet.getMaxHealth();
            pet.setHealth(health);

        } else if (item.getDamageValue() == 6) {
            Rabbit rabbit = (Rabbit) EntityType.RABBIT.spawn((ServerLevel) world, null, player, blockPos, MobSpawnType.SPAWN_EGG, false, false);
            if (rabbit != null) {
                rabbit.getNavigation().stop();
                rabbit.setHealth(rabbit.getMaxHealth());
            }
        }
    }

    @Override
    public Component getName(ItemStack item) {
        String unlocalizedName = super.getName(item).getString();
        if (!item.hasTag() || item.getDamageValue() == 0)
            unlocalizedName += "_empty";
        else
            unlocalizedName += "_full";
        return Component.translatable(unlocalizedName);
    }

    @OnlyIn(Dist.CLIENT)
    @Override
    public void appendHoverText(ItemStack item, @Nullable Level worldIn, List<Component> tooltip, TooltipFlag flagIn) {
        CompoundTag nbt = item.getTag();
        if (nbt != null) {
            if (item.getDamageValue() == 3)
                tooltip.add(Component.translatable("tooltip.pet_carrier.adopt_cat").withStyle(ChatFormatting.ITALIC));
            else if (item.getDamageValue() == 4)
                tooltip.add(Component.translatable("tooltip.pet_carrier.adopt_dog").withStyle(ChatFormatting.ITALIC));
            else if (item.getDamageValue() == 5)
                tooltip.add(Component.translatable("tooltip.pet_carrier.adopt_parrot").withStyle(ChatFormatting.ITALIC));
            else if (item.getDamageValue() == 6)
                tooltip.add(Component.translatable("tooltip.pet_carrier.adopt_rabbit").withStyle(ChatFormatting.ITALIC));

            else if (item.getDamageValue() != 0) {
                MutableComponent species = Component.translatable(Util.makeDescriptionId("entity", new ResourceLocation(nbt.getString("id"))));

                Component owner = Component.translatable("tooltip.pet_carrier.owner", nbt.getString("OwnerName"));
                if (nbt.contains("DisplayName"))
                    tooltip.add(Component.literal("\"" + nbt.getString("DisplayName") + "\"").withStyle(ChatFormatting.AQUA));
                else
                    tooltip.add(species.withStyle(ChatFormatting.AQUA));

                if (item.getDamageValue() == 1)
                    tooltip.add(Genetics.getPhenotypeDescription(nbt, true).withStyle(ChatFormatting.ITALIC));

                tooltip.add(owner);
            }
        } else {
            MutableComponent empty = Component.translatable("tooltip.pet_carrier.empty");
            tooltip.add(empty.withStyle(ChatFormatting.AQUA));
        }
    }
}
