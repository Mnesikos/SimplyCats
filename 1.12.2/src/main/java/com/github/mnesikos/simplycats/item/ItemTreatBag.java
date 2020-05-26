package com.github.mnesikos.simplycats.item;

import com.github.mnesikos.simplycats.entity.EntityCat;
import com.github.mnesikos.simplycats.init.ModSounds;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.world.World;

public class ItemTreatBag extends ItemBase {
    public ItemTreatBag(String name) {
        super(name);
    }

    @Override
    public ActionResult<ItemStack> onItemRightClick(World world, EntityPlayer player, EnumHand hand) {
        for (EntityCat cat : world.getEntitiesWithinAABB(EntityCat.class, player.getEntityBoundingBox().grow(128)))
            cat.onBagShake(player);

        player.playSound(ModSounds.SHAKE_TREATS, 1.0F, 1.0F);
        player.swingArm(hand);
        return super.onItemRightClick(world, player, hand);
    }
}
