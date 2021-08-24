package com.github.mnesikos.simplycats.item;

import com.github.mnesikos.simplycats.entity.EntityCat;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

import java.util.List;

/**
 * @author SoggyMustache
 */
public class ItemLaserPointer extends Item {
    @Override
    public ActionResult<ItemStack> onItemRightClick(World worldIn, EntityPlayer playerIn, EnumHand handIn) {
        ItemStack stack = playerIn.getHeldItem(handIn);
        if (stack.getTagCompound() == null) {//Check if we do NOT have NBT data
            stack.setTagCompound(new NBTTagCompound()); //Give the item new data and set it to true since this would be the first right click
            stack.getTagCompound().setBoolean("On", true);
        } else {
            //If we already have data just flip the On tag
            stack.getTagCompound().setBoolean("On", !stack.getTagCompound().getBoolean("On"));
        }

        //After we set the value above we check if its off
        if (!stack.getTagCompound().getBoolean("On")) {
            //If the pointer is off we want to get the cats within the area + a little more than whats defined below
            //and set the nearest pointer to null
            List<EntityCat> cats = worldIn.getEntitiesWithinAABB(EntityCat.class, playerIn.getEntityBoundingBox().grow(14.0D));
            cats.forEach(eb -> eb.setNearestLaser(null));
        }
        return super.onItemRightClick(worldIn, playerIn, handIn);
    }

    @Override
    public void addInformation(ItemStack stack, World worldIn, List<String> tooltip, ITooltipFlag flagIn) {
        //This is just for testing to see if its on or not in game
        if (stack.getTagCompound() != null)
            tooltip.add("On = " + stack.getTagCompound().getBoolean("On"));
        super.addInformation(stack, worldIn, tooltip, flagIn);
    }

    @Override
    public void onUpdate(ItemStack stack, World worldIn, Entity e, int itemSlot, boolean isSelected) {
        if (isSelected) {
            if (stack.getTagCompound() != null) {
                if (stack.getTagCompound().getBoolean("On")) {
                    //Get all cats within a 2 block radius of the player
//					List<EntityCat> cats = worldIn.getEntitiesWithinAABB(EntityCat.class, e.getEntityBoundingBox().grow(2.0D));

                    if (e instanceof EntityPlayer) {
                        //Check to make sure the entity using this is a player and
                        //ray trace the look position
                        EntityPlayer player = (EntityPlayer) e;
                        Vec3d vec3 = player.getPositionEyes(1.0F);
                        Vec3d vec3a = player.getLook(1.0F);
                        int distance = 9;
                        Vec3d vec3b = vec3.addVector(vec3a.x * distance, vec3a.y * distance, vec3a.z * distance);
                        RayTraceResult mop = worldIn.rayTraceBlocks(vec3, vec3b);
                        //If the trace returns a point (it hit something and isnt going off thousands of metres away
                        if (mop != null && mop.hitVec != null) {
                            if (e.ticksExisted % 5 == 0) //About every 5 ticks we put the particle, just so it isnt spammed too much and kinda goes away on time
                                worldIn.spawnParticle(EnumParticleTypes.REDSTONE, mop.hitVec.x, mop.hitVec.y, mop.hitVec.z, 0, 0, 0);
                            //Get all cats within a 2 block radius of the laser position
                            List<EntityCat> cats = worldIn.getEntitiesWithinAABB(EntityCat.class, new AxisAlignedBB(mop.getBlockPos()).grow(4.0D));
                            //Most important is here
                            //Go through all the cats and set the hit vector to the laser
                            cats.forEach(eb -> eb.setNearestLaser(mop.hitVec));
                        }
                    }
                }
            }
        }
        super.onUpdate(stack, worldIn, e, itemSlot, isSelected);
    }
}
