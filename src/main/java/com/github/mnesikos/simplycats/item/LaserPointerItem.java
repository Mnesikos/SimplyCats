package com.github.mnesikos.simplycats.item;

import com.github.mnesikos.simplycats.entity.SimplyCatEntity;
import net.minecraft.ChatFormatting;
import net.minecraft.core.particles.DustParticleOptions;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import javax.annotation.Nullable;
import java.util.List;

/**
 * @author SoggyMustache, ported to 1.16.5+ by Mnesikos
 */
public class LaserPointerItem extends Item {
    public LaserPointerItem() {
        super(new Item.Properties().stacksTo(1));
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level worldIn, Player playerIn, InteractionHand handIn) {
        ItemStack stack = playerIn.getItemInHand(handIn);
        if (stack.getTag() == null) {//Check if we do NOT have NBT data
            stack.setTag(new CompoundTag()); //Give the item new data and set it to true since this would be the first right click
            stack.getTag().putBoolean("On", true);
        } else {
            //If we already have data just flip the On tag
            stack.getTag().putBoolean("On", !stack.getTag().getBoolean("On"));
        }

        //After we set the value above we check if its off
        if (!stack.getTag().getBoolean("On")) {
            //If the pointer is off we want to get the cats within the area + a little more than whats defined below
            //and set the nearest pointer to null
            List<SimplyCatEntity> cats = worldIn.getEntitiesOfClass(SimplyCatEntity.class, playerIn.getBoundingBox().inflate(14.0D));
            cats.forEach(eb -> eb.setNearestLaser(null));
        }
        return super.use(worldIn, playerIn, handIn);
    }

    @OnlyIn(Dist.CLIENT)
    @Override
    public void appendHoverText(ItemStack stack, @Nullable Level worldIn, List<Component> tooltip, TooltipFlag flagIn) {
        if (stack.getTag() != null)
            tooltip.add(Component.literal(stack.getTag().getBoolean("On") ? "On" : "Off").withStyle(ChatFormatting.ITALIC));
        super.appendHoverText(stack, worldIn, tooltip, flagIn);
    }

    @Override
    public void inventoryTick(ItemStack stack, Level worldIn, Entity e, int itemSlot, boolean isSelected) {
        if (isSelected) {
            if (stack.getTag() != null) {
                if (stack.getTag().getBoolean("On")) {
                    //Get all cats within a 2 block radius of the player
//					List<SimplyCatEntity> cats = worldIn.getEntitiesWithinAABB(SimplyCatEntity.class, e.getEntityBoundingBox().grow(2.0D));

                    if (e instanceof Player) {
                        //Check to make sure the entity using this is a player and
                        //ray trace the look position
                        Player player = (Player) e;
                        BlockHitResult mop = getPlayerPOVHitResult(worldIn, player, ClipContext.Fluid.NONE);
                        //If the trace returns a point (it hit something and isnt going off thousands of metres away
                        if (mop != null && mop.getLocation() != null) {
                            if (e.tickCount % 5 == 0) //About every 5 ticks we put the particle, just so it isnt spammed too much and kinda goes away on time
                                worldIn.addParticle(DustParticleOptions.REDSTONE, mop.getLocation().x, mop.getLocation().y, mop.getLocation().z, 0, 0, 0);
                            //Get all cats within a 2 block radius of the laser position
                            List<SimplyCatEntity> cats = worldIn.getEntitiesOfClass(SimplyCatEntity.class, new AABB(mop.getBlockPos()).inflate(4.0D));
                            //Most important is here
                            //Go through all the cats and set the hit vector to the laser
                            cats.forEach(eb -> eb.setNearestLaser(mop.getLocation()));
                        }
                    }
                }
            }
        }
        super.inventoryTick(stack, worldIn, e, itemSlot, isSelected);
    }
}
