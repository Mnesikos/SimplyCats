package com.github.mnesikos.simplycats.item;

import com.github.mnesikos.simplycats.entity.EntityCat;
import com.github.mnesikos.simplycats.init.ModSounds;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.world.World;

public class ItemTreatBag extends ItemBase {
    public ItemTreatBag() {
        super();
    }

    @Override
    public ActionResult<ItemStack> onItemRightClick(World worldIn, PlayerEntity playerIn, Hand handIn) {
        for (EntityCat cat : worldIn.getEntitiesWithinAABB(EntityCat.class, playerIn.getBoundingBox().grow(128)))
            cat.onBagShake(playerIn);

        playerIn.playSound(ModSounds.SHAKE_TREATS, 1.0F, 1.0F);
        playerIn.swingArm(handIn);
        return super.onItemRightClick(worldIn, playerIn, handIn);
    }
}
