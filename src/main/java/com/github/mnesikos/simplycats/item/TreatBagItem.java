package com.github.mnesikos.simplycats.item;

import com.github.mnesikos.simplycats.SimplyCats;
import com.github.mnesikos.simplycats.entity.SimplyCatEntity;
import com.github.mnesikos.simplycats.event.SCSounds;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.world.World;

public class TreatBagItem extends Item {
    public TreatBagItem() {
        super(new Item.Properties().tab(SimplyCats.ITEM_GROUP));
    }

    @Override
    public ActionResult<ItemStack> use(World world, PlayerEntity player, Hand hand) {
        for (SimplyCatEntity cat : world.getEntitiesOfClass(SimplyCatEntity.class, player.getBoundingBox().inflate(128)))
            cat.onBagShake(player);

        player.playSound(SCSounds.SHAKE_TREATS, 1.0F, 1.0F);
        return ActionResult.success(player.getItemInHand(hand));
    }
}
