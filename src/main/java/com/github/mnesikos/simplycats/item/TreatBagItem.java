package com.github.mnesikos.simplycats.item;

import com.github.mnesikos.simplycats.entity.SimplyCatEntity;
import com.github.mnesikos.simplycats.event.SCSounds;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

public class TreatBagItem extends Item {
    public TreatBagItem() {
        super(new Item.Properties());
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level world, Player player, InteractionHand hand) {
        for (SimplyCatEntity cat : world.getEntitiesOfClass(SimplyCatEntity.class, player.getBoundingBox().inflate(128)))
            cat.onBagShake(player);

        player.playSound(SCSounds.SHAKE_TREATS.get(), 1.0F, 1.0F);
        return InteractionResultHolder.success(player.getItemInHand(hand));
    }
}
