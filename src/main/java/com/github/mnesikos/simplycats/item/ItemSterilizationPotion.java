package com.github.mnesikos.simplycats.item;

import com.github.mnesikos.simplycats.entity.EntityCat;
import com.github.mnesikos.simplycats.entity.core.Genetics;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.particles.ParticleTypes;
import net.minecraft.util.Hand;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.World;

import javax.annotation.Nullable;
import java.util.List;
import java.util.Random;

public class ItemSterilizationPotion extends ItemBase {
    protected Random rand;

    public ItemSterilizationPotion() {
        super();
        this.rand = new Random();
    }

    @Override
    public boolean itemInteractionForEntity(ItemStack stack, PlayerEntity player, LivingEntity target, Hand hand) {
        if (target instanceof EntityCat) {
            EntityCat cat = (EntityCat) target;
            if ((!cat.isTamed() || (cat.isTamed() && cat.isOwner(player))) && player.isSneaking() && !cat.isFixed()) {
                cat.setFixed((byte) 1);
                for (int i = 0; i < 7; ++i) {
                    double d0 = this.rand.nextGaussian() * 0.02D;
                    double d1 = this.rand.nextGaussian() * 0.02D;
                    double d2 = this.rand.nextGaussian() * 0.02D;
                    cat.world.addParticle(ParticleTypes.HAPPY_VILLAGER, cat.getPosX() + (double)(cat.world.rand.nextFloat() * cat.getWidth() * 2.0F) - (double)cat.getWidth(), cat.getPosY() + 0.5D + (double)(cat.world.rand.nextFloat() * cat.getHeight()), cat.getPosZ() + (double)(cat.world.rand.nextFloat() * cat.getWidth() * 2.0F) - (double)cat.getWidth(), d0, d1, d2);
                }
                if (cat.world.isRemote)
                    player.sendMessage(new TranslationTextComponent(cat.getSex() == Genetics.Sex.FEMALE ? "chat.info.success_fixed_female" : "chat.info.success_fixed_male", cat.getName()));

                if (!player.abilities.isCreativeMode) {
                    ItemStack emptyBottle = new ItemStack(Items.GLASS_BOTTLE);
                    stack.shrink(1);
                    if (stack.isEmpty())
                        player.setHeldItem(hand, emptyBottle);
                    else if (!player.inventory.addItemStackToInventory(emptyBottle))
                        player.dropItem(emptyBottle, false);
                }
            }
        }
        return super.itemInteractionForEntity(stack, player, target, hand);
    }

    @Override
    public void addInformation(ItemStack stack, @Nullable World worldIn, List<ITextComponent> tooltip, ITooltipFlag flagIn) {
        tooltip.add(new TranslationTextComponent("tooltip.sterilization_potion.usage"));
    }
}
