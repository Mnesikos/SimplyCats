package com.github.mnesikos.simplycats.item;

import com.github.mnesikos.simplycats.entity.EntityCat;
import com.github.mnesikos.simplycats.init.CatSounds;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.world.World;

public class ItemTreatBag extends Item {
    @Override
    public ActionResult<ItemStack> onItemRightClick(World world, EntityPlayer player, EnumHand hand) {
        for (EntityCat cat : world.getEntitiesWithinAABB(EntityCat.class, player.getEntityBoundingBox().grow(128)))
            cat.onBagShake(player);

        player.playSound(CatSounds.SHAKE_TREATS, 1.0F, 1.0F);
        return ActionResult.newResult(EnumActionResult.SUCCESS, player.getHeldItem(hand));
    }
}
