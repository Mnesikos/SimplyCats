package com.github.mnesikos.simplycats.item;

import com.github.mnesikos.simplycats.SimplyCats;
import com.github.mnesikos.simplycats.entity.SimplyCatEntity;
import com.sun.javafx.geom.Vec3d;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.particles.ParticleTypes;
import net.minecraft.particles.RedstoneParticleData;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.math.RayTraceContext;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import javax.annotation.Nullable;
import java.util.List;

/**
 * @author SoggyMustache, ported to 1.16.5 by Mnesikos
 */
public class LaserPointerItem extends Item {
    public LaserPointerItem() {
        super(new Item.Properties().tab(SimplyCats.ITEM_GROUP).stacksTo(1));
    }

    @Override
    public ActionResult<ItemStack> use(World worldIn, PlayerEntity playerIn, Hand handIn) {
        ItemStack stack = playerIn.getItemInHand(handIn);
        if (stack.getTag() == null) {//Check if we do NOT have NBT data
            stack.setTag(new CompoundNBT()); //Give the item new data and set it to true since this would be the first right click
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
    public void appendHoverText(ItemStack stack, @Nullable World worldIn, List<ITextComponent> tooltip, ITooltipFlag flagIn) {
        //This is just for testing to see if its on or not in game
        if (stack.getTag() != null)
            tooltip.add(new StringTextComponent("On = " + stack.getTag().getBoolean("On")));
        super.appendHoverText(stack, worldIn, tooltip, flagIn);
    }

    @Override
    public void inventoryTick(ItemStack stack, World worldIn, Entity e, int itemSlot, boolean isSelected) {
        if (isSelected) {
            if (stack.getTag() != null) {
                if (stack.getTag().getBoolean("On")) {
                    //Get all cats within a 2 block radius of the player
//					List<SimplyCatEntity> cats = worldIn.getEntitiesWithinAABB(SimplyCatEntity.class, e.getEntityBoundingBox().grow(2.0D));

                    if (e instanceof PlayerEntity) {
                        //Check to make sure the entity using this is a player and
                        //ray trace the look position
                        PlayerEntity player = (PlayerEntity) e;
                        BlockRayTraceResult mop = getPlayerPOVHitResult(worldIn, player, RayTraceContext.FluidMode.NONE);
                        //If the trace returns a point (it hit something and isnt going off thousands of metres away
                        if (mop != null && mop.getLocation() != null) {
                            if (e.tickCount % 5 == 0) //About every 5 ticks we put the particle, just so it isnt spammed too much and kinda goes away on time
                                worldIn.addParticle(RedstoneParticleData.REDSTONE, mop.getLocation().x, mop.getLocation().y, mop.getLocation().z, 0, 0, 0);
                            //Get all cats within a 2 block radius of the laser position
                            List<SimplyCatEntity> cats = worldIn.getEntitiesOfClass(SimplyCatEntity.class, new AxisAlignedBB(mop.getBlockPos()).inflate(4.0D));
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
