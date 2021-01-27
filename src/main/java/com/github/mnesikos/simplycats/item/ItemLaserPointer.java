package com.github.mnesikos.simplycats.item;

import com.github.mnesikos.simplycats.entity.EntityCat;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.particles.RedstoneParticleData;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.math.RayTraceContext;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.world.World;

import javax.annotation.Nullable;
import java.util.List;

public class ItemLaserPointer extends ItemBase {
    public ItemLaserPointer() {
        super();
    }

    @Override
    public ActionResult<ItemStack> onItemRightClick(World worldIn, PlayerEntity playerIn, Hand handIn) {
        ItemStack stack = playerIn.getHeldItem(handIn);
        if (stack.getTag() == null) {
            stack.setTag(new CompoundNBT());
            stack.getTag().putBoolean("On", true);
        } else
            stack.getTag().putBoolean("On", !stack.getTag().getBoolean("On"));

        if (!stack.getTag().getBoolean("On")) {
            List<EntityCat> cats = worldIn.getEntitiesWithinAABB(EntityCat.class, playerIn.getBoundingBox().grow(14.0D));
            cats.forEach(eb -> eb.setNearestLaser(null));
        }

        return super.onItemRightClick(worldIn, playerIn, handIn);
    }

    @Override
    public void inventoryTick(ItemStack stack, World worldIn, Entity entityIn, int itemSlot, boolean isSelected) {
        if (isSelected) {
            if (stack.getTag() != null) {
                if (stack.getTag().getBoolean("On")) {
                    //Get all cats within a 6 block radius of the player
                    List<EntityCat> cats = worldIn.getEntitiesWithinAABB(EntityCat.class, entityIn.getBoundingBox().grow(6.0D));

                    if (entityIn instanceof PlayerEntity) {
                        //Check to make sure the entity using this is a player and
                        //ray trace the look position
                        PlayerEntity player = (PlayerEntity) entityIn;
                        Vec3d vec3 = player.getEyePosition(1.0F);
                        Vec3d vec3a = player.getLook(1.0F);
                        int distance = 9;
                        Vec3d vec3b = vec3.add(vec3a.x * distance, vec3a.y * distance, vec3a.z * distance);
                        RayTraceResult mop = worldIn.rayTraceBlocks(new RayTraceContext(vec3, vec3b, RayTraceContext.BlockMode.OUTLINE, RayTraceContext.FluidMode.NONE, player));
                        //If the trace returns a point (it hit something and isnt going off thousands of metres away
                        if (mop != null && mop.getHitVec() != null) {
                            if (entityIn.ticksExisted % 5 == 0) //About every 5 ticks we put the particle, just so it isnt spammed too much and kinda goes away on time
                                worldIn.addParticle(RedstoneParticleData.REDSTONE_DUST, mop.getHitVec().x, mop.getHitVec().y, mop.getHitVec().z, 0, 0, 0);
                            //Most important is here
                            //Go through all the cats and set the hit vector to the laser
                            cats.forEach(eb -> eb.setNearestLaser(mop.getHitVec()));
                        }
                    }
                }
            }
        }
        super.inventoryTick(stack, worldIn, entityIn, itemSlot, isSelected);
    }

    @Override
    public void addInformation(ItemStack stack, @Nullable World worldIn, List<ITextComponent> tooltip, ITooltipFlag flagIn) {
        if (stack.getTag() != null)
            tooltip.add(new StringTextComponent(stack.getTag().getBoolean("On") ? "On" : "Off"));
    }
}
