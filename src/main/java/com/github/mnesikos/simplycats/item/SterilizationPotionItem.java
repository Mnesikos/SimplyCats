package com.github.mnesikos.simplycats.item;

import com.github.mnesikos.simplycats.entity.SimplyCatEntity;
import com.github.mnesikos.simplycats.entity.core.Genetics;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import javax.annotation.Nullable;
import java.util.List;

public class SterilizationPotionItem extends Item {
    public SterilizationPotionItem() {
        super(new Item.Properties());
    }

    @Override
    public InteractionResult interactLivingEntity(ItemStack stack, Player player, LivingEntity target, InteractionHand hand) {
        if (target instanceof SimplyCatEntity) {
            SimplyCatEntity cat = (SimplyCatEntity) target;
            if ((!cat.isTame() || (cat.isTame() && cat.isOwnedBy(player))) && player.isCrouching() && !cat.isFixed()) {
                cat.setFixed((byte) 1);
                for (int i = 0; i < 7; ++i) {
                    double d0 = cat.getRandom().nextGaussian() * 0.02D;
                    double d1 = cat.getRandom().nextGaussian() * 0.02D;
                    double d2 = cat.getRandom().nextGaussian() * 0.02D;
                    cat.level().addParticle(ParticleTypes.HAPPY_VILLAGER, cat.getRandomX(1.0D), cat.getRandomY() + 0.5D, cat.getRandomZ(1.0D), d0, d1, d2);
                }
                player.displayClientMessage(Component.translatable(cat.getSex() == Genetics.Sex.FEMALE ? "chat.info.success_fixed_female" : "chat.info.success_fixed_male", cat.getName()), true);

                if (!player.isCreative()) {
                    ItemStack emptyBottle = new ItemStack(Items.GLASS_BOTTLE);
                    stack.shrink(1);
                    if (stack.isEmpty())
                        player.setItemInHand(hand, emptyBottle);
                    else if (!player.getInventory().add(emptyBottle))
                        player.drop(emptyBottle, false);
                }
            }
        }
        return super.interactLivingEntity(stack, player, target, hand);
    }

    @OnlyIn(Dist.CLIENT)
    @Override
    public void appendHoverText(ItemStack stack, @Nullable Level worldIn, List<Component> tooltip, TooltipFlag flagIn) {
        tooltip.add(Component.translatable("tooltip.sterilization_potion.usage"));
    }
}
