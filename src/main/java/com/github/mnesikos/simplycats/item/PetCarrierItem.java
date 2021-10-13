package com.github.mnesikos.simplycats.item;

import com.github.mnesikos.simplycats.SimplyCats;
import com.github.mnesikos.simplycats.entity.SimplyCatEntity;
import com.github.mnesikos.simplycats.entity.core.Genetics;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.passive.ParrotEntity;
import net.minecraft.entity.passive.TameableEntity;
import net.minecraft.entity.passive.WolfEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUseContext;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.*;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.util.math.shapes.VoxelShapes;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import javax.annotation.Nullable;
import java.util.List;
import java.util.stream.Stream;

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

                    stack.shrink(1);
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

            if (item.getDamageValue() == 3 || item.getDamageValue() == 4) {
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
                    if (entity != null) {
                        entity.absMoveTo(blockPos.getX() + 0.5D, blockPos.getY(), blockPos.getZ() + 0.5D, context.getRotation(), 0);
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

    private double getYOffset(World world, BlockPos pos, AxisAlignedBB axisAlignedBB) {
        AxisAlignedBB axisalignedbb = (new AxisAlignedBB(pos)).expandTowards(0.0D, -1.0D, 0.0D);
        Stream<VoxelShape> stream = world.getCollisions(null, axisalignedbb, (entity) -> true);

        return 1.0D + VoxelShapes.collide(Direction.Axis.Y, axisAlignedBB, stream, 0.0D);
    }

    private void newPet(ItemStack item, PlayerEntity player, World world, BlockPos blockPos) {
        TameableEntity pet = null;
        if (item.getDamageValue() == 3)
            pet = SimplyCats.CAT.get().create(world);
        else if (item.getDamageValue() == 4)
            pet = EntityType.WOLF.create(world);

        if (pet instanceof SimplyCatEntity && !((SimplyCatEntity) pet).canBeTamed(player)) {
            player.displayClientMessage(new TranslationTextComponent("chat.info.tamed_limit_reached"), true);

        } else if (pet != null) {
            pet.absMoveTo(blockPos.getX() + 0.5D, blockPos.getY(), blockPos.getZ() + 0.5D, MathHelper.wrapDegrees(world.random.nextFloat() * 360.0F), 0);
            world.addFreshEntity(pet);
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
            TranslationTextComponent cat = new TranslationTextComponent("tooltip.pet_carrier.adopt_cat");
            TranslationTextComponent dog = new TranslationTextComponent("tooltip.pet_carrier.adopt_dog");
            if (item.getDamageValue() == 3)
                tooltip.add(cat.withStyle(TextFormatting.ITALIC));
            else if (item.getDamageValue() == 4)
                tooltip.add(dog.withStyle(TextFormatting.ITALIC));

            else if (item.getDamageValue() != 0) {
                TranslationTextComponent species = new TranslationTextComponent(Util.makeDescriptionId("entity", new ResourceLocation(nbt.getString("id"))));

                TranslationTextComponent owner = new TranslationTextComponent("tooltip.pet_carrier.owner", nbt.getString("OwnerName"));
                if (nbt.contains("DisplayName"))
                    tooltip.add(new StringTextComponent("\"" + nbt.getString("DisplayName") + "\"").withStyle(TextFormatting.AQUA));
                else
                    tooltip.add(species.withStyle(TextFormatting.AQUA));

                if (item.getDamageValue() == 1)
                    tooltip.add(Genetics.getPhenotypeDescription(nbt, true).withStyle(TextFormatting.ITALIC));

                tooltip.add(owner);
            }
        } else {
            TranslationTextComponent empty = new TranslationTextComponent("tooltip.pet_carrier.empty");
            tooltip.add(empty.withStyle(TextFormatting.AQUA));
        }
    }
}
