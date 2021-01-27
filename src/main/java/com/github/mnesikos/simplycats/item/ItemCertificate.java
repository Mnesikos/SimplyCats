package com.github.mnesikos.simplycats.item;

import com.github.mnesikos.simplycats.SimplyCats;
import com.github.mnesikos.simplycats.entity.EntityCat;
import com.github.mnesikos.simplycats.init.ModItems;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.passive.TameableEntity;
import net.minecraft.entity.passive.WolfEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.particles.BasicParticleType;
import net.minecraft.particles.ParticleTypes;
import net.minecraft.util.Hand;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.World;

import javax.annotation.Nullable;
import java.util.List;

public class ItemCertificate extends ItemBase {
    public ItemCertificate() {
        super(new Item.Properties().group(SimplyCats.GROUP));
    }

    @Override
    public boolean itemInteractionForEntity(ItemStack stack, PlayerEntity player, LivingEntity target, Hand hand) {
        if (target instanceof EntityCat || target instanceof WolfEntity) {
            if (stack.getItem() == ModItems.ADOPT_CERTIFICATE.get()) {
                if ((target instanceof EntityCat && ((EntityCat) target).canBeTamed(player)) || (target instanceof WolfEntity && !((WolfEntity) target).isTamed())) {
                    if (target instanceof EntityCat)
                        ((EntityCat) target).setTamed(true, player);
                    else
                        ((TameableEntity) target).setTamed(true);
                    ((TameableEntity) target).getNavigator().clearPath();
                    ((TameableEntity) target).setOwnerId(player.getUniqueID());
                    target.setHealth(target.getMaxHealth());
                    if (player.world.isRemote) {
                        player.sendMessage(new TranslationTextComponent("chat.info.adopt_usage", target.getName()));
                        this.playTameEffect(true, target.world, (TameableEntity) target);
                    }

                    if (!player.abilities.isCreativeMode) {
                        stack.shrink(1);
                        if (stack.getCount() <= 0)
                            player.inventory.setInventorySlotContents(player.inventory.currentItem, ItemStack.EMPTY);
                    }

                } else if (player.world.isRemote && target instanceof EntityCat && !((EntityCat) target).isTamed())
                    player.sendMessage(new TranslationTextComponent("chat.info.tamed_limit_reached"));

            } else if (stack.getItem() == ModItems.RELEASE_CERTIFICATE.get()) {
                if (((TameableEntity) target).isOwner(player)) {
                    if (target instanceof EntityCat)
                        ((EntityCat) target).setTamed(false, player);
                    else
                        ((TameableEntity) target).setTamed(false);
                    ((TameableEntity) target).getNavigator().clearPath();
                    ((TameableEntity) target).setOwnerId(null);
                    if (player.world.isRemote)
                        player.sendMessage(new TranslationTextComponent("chat.info.release_usage", target.getName()));
                    this.playTameEffect(false, target.world, (TameableEntity)target);

                    if (!player.abilities.isCreativeMode) {
                        stack.shrink(1);
                        if (stack.getCount() <= 0)
                            player.inventory.setInventorySlotContents(player.inventory.currentItem, ItemStack.EMPTY);
                    }
                }
                return true;
            }
        }
        return false;
    }

    protected void playTameEffect(boolean play, World world, TameableEntity entity) {
        BasicParticleType enumparticletypes = ParticleTypes.HEART;

        if (!play)
            enumparticletypes = ParticleTypes.SMOKE;

        for (int i = 0; i < 7; ++i) {
            double d0 = world.rand.nextGaussian() * 0.02D;
            double d1 = world.rand.nextGaussian() * 0.02D;
            double d2 = world.rand.nextGaussian() * 0.02D;
            world.addParticle(enumparticletypes, entity.getPosX() + (double)(world.rand.nextFloat() * entity.getWidth() * 2.0F) - (double)entity.getWidth(), entity.getPosY() + 0.5D + (double)(world.rand.nextFloat() * entity.getHeight()), entity.getPosZ() + (double)(world.rand.nextFloat() * entity.getWidth() * 2.0F) - (double)entity.getWidth(), d0, d1, d2);
        }
    }

    @Override
    public void addInformation(ItemStack stack, @Nullable World worldIn, List<ITextComponent> tooltip, ITooltipFlag flagIn) {
        if (stack.getItem() == ModItems.ADOPT_CERTIFICATE.get())
            tooltip.add(new TranslationTextComponent("tooltip.certificate_adopt"));
        else
            tooltip.add(new TranslationTextComponent("tooltip.certificate_release"));
    }
}
