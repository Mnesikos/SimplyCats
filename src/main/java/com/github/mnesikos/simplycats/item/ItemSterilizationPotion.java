package com.github.mnesikos.simplycats.item;

import com.github.mnesikos.simplycats.entity.EntityCat;
import com.github.mnesikos.simplycats.entity.core.Genetics;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumHand;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.text.TextComponentTranslation;

import java.util.Random;

public class ItemSterilizationPotion extends ItemBase {
    protected String name;
    protected Random rand;

    public ItemSterilizationPotion(String name) {
        super(name);
        this.rand = new Random();
    }

    @Override
    public boolean itemInteractionForEntity(ItemStack stack, EntityPlayer player, EntityLivingBase target, EnumHand hand) {
        if (target instanceof EntityCat) {
            EntityCat cat = (EntityCat) target;
            if ((!cat.isTamed() || (cat.isTamed() && cat.isOwner(player))) && player.isSneaking() && !cat.isFixed()) {
                cat.setFixed((byte) 1);
                for (int i = 0; i < 7; ++i) {
                    double d0 = this.rand.nextGaussian() * 0.02D;
                    double d1 = this.rand.nextGaussian() * 0.02D;
                    double d2 = this.rand.nextGaussian() * 0.02D;
                    cat.world.spawnParticle(EnumParticleTypes.VILLAGER_HAPPY, cat.posX + (double)(this.rand.nextFloat() * cat.width * 2.0F) - (double)cat.width, cat.posY + 0.5D + (double)(this.rand.nextFloat() * cat.height), cat.posZ + (double)(this.rand.nextFloat() * cat.width * 2.0F) - (double)cat.width, d0, d1, d2);
                }
                if (cat.world.isRemote)
                    player.sendMessage(new TextComponentTranslation(cat.getSex() == Genetics.Sex.FEMALE ? "chat.info.success_fixed_female" : "chat.info.success_fixed_male", cat.getName()));

                if (!player.capabilities.isCreativeMode) {
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
}
