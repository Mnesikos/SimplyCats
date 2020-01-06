package com.github.mnesikos.simplycats.item;

import com.github.mnesikos.simplycats.SimplyCats;
import com.github.mnesikos.simplycats.entity.EntityCat;
import net.minecraft.client.resources.I18n;
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
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import javax.annotation.Nullable;
import java.util.List;

public class ItemCertificate extends ItemBase {
    private static final String[] TYPES = new String[] { "adopt", "release" };
    protected String name;

    public ItemCertificate(String name) {
        super(name, new Item.Properties().group(SimplyCats.GROUP).maxStackSize(1).defaultMaxDamage(1));
        this.name = name;
    }

    @Override
    public boolean itemInteractionForEntity(ItemStack stack, PlayerEntity player, LivingEntity target, Hand hand) {
        if (target instanceof EntityCat || target instanceof WolfEntity) {
            if (stack.getDamage() == 0) {
                if (((TameableEntity) target).isTamed()) {
                    return false;
                } else {
                    ((TameableEntity) target).setTamed(true);
                    ((TameableEntity) target).getNavigator().clearPath();
                    ((TameableEntity) target).setOwnerId(player.getUniqueID());
                    target.setHealth(target.getMaxHealth());
                    if (player.world.isRemote)
                        player.sendMessage(new StringTextComponent(new TranslationTextComponent("chat.info.adopt_usage").getFormattedText() + " " + target.getName() + "!"));
                    this.playTameEffect(true, target.world, (TameableEntity)target);

                    if (!player.abilities.isCreativeMode) {
                        stack.shrink(1);
                        if (stack.getCount() <= 0)
                            player.inventory.setInventorySlotContents(player.inventory.currentItem, ItemStack.EMPTY);
                    }
                }
                return true;

            } else if (stack.getDamage() == 1) {
                if (((TameableEntity) target).isOwner(player)) {
                    ((TameableEntity) target).setTamed(false);
                    ((TameableEntity) target).getNavigator().clearPath();
                    ((TameableEntity) target).setOwnerId(null);
                    if (player.world.isRemote)
                        player.sendMessage(new StringTextComponent(target.getName() + " " + new TranslationTextComponent("chat.info.release_usage").getFormattedText()));
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
            world.addParticle(enumparticletypes, entity.posX + (double)(world.rand.nextFloat() * entity.getWidth() * 2.0F) - (double)entity.getWidth(), entity.posY + 0.5D + (double)(world.rand.nextFloat() * entity.getHeight()), entity.posZ + (double)(world.rand.nextFloat() * entity.getWidth() * 2.0F) - (double)entity.getWidth(), d0, d1, d2);
        }
    }

    @Override
    public String getTranslationKey(ItemStack stack) {
        int meta = stack.getDamage();
        switch (meta) {
            case 0:
            default:
                return "item.certificate." + TYPES[0];
            case 1:
                return "item.certificate." + TYPES[1];
        }
    }

    @Override @OnlyIn(Dist.CLIENT)
    public void addInformation(ItemStack stack, @Nullable World worldIn, List<ITextComponent> tooltip, ITooltipFlag flagIn) {
        int i = MathHelper.clamp(stack.getDamage(), 0, 1);
        tooltip.add(new TranslationTextComponent("tooltip.certificate." + TYPES[i] + ".desc"));
    }
}
