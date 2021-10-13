package com.github.mnesikos.simplycats.item;

import com.github.mnesikos.simplycats.SimplyCats;
import com.github.mnesikos.simplycats.entity.SimplyCatEntity;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.passive.ParrotEntity;
import net.minecraft.entity.passive.TameableEntity;
import net.minecraft.entity.passive.WolfEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.particles.IParticleData;
import net.minecraft.particles.ParticleTypes;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Hand;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import javax.annotation.Nullable;
import java.util.List;

public class CertificateItem extends Item {
    private final boolean adoption;

    public CertificateItem(boolean adoption) {
        super(new Item.Properties().tab(SimplyCats.ITEM_GROUP));
        this.adoption = adoption;
    }

    @Override
    public ActionResultType interactLivingEntity(ItemStack stack, PlayerEntity player, LivingEntity target, Hand hand) {
        if (target instanceof SimplyCatEntity || target instanceof WolfEntity || target instanceof ParrotEntity) {
            TameableEntity tameable = (TameableEntity) target;
            if (this.adoption) {
                if ((tameable instanceof SimplyCatEntity && ((SimplyCatEntity) tameable).canBeTamed(player)) || (!(tameable instanceof SimplyCatEntity) && !tameable.isTame())) {
                    if (tameable instanceof SimplyCatEntity)
                        ((SimplyCatEntity) tameable).setTamed(true, player);
                    else
                        tameable.tame(player);
                    tameable.getNavigation().stop();
                    tameable.setHealth(tameable.getMaxHealth());
                    player.displayClientMessage(new TranslationTextComponent("chat.info.adopt_usage", tameable.getName()), true);
                    if (player.level.isClientSide)
                        this.playTameEffect(true, tameable.level, tameable);

                    if (!player.isCreative())
                        stack.shrink(1);

                } else if (tameable instanceof SimplyCatEntity && !tameable.isTame())
                    player.displayClientMessage(new TranslationTextComponent("chat.info.tamed_limit_reached"), true);

            } else {
                if (tameable.isOwnedBy(player)) {
                    if (tameable instanceof SimplyCatEntity)
                        ((SimplyCatEntity) tameable).setTamed(false, player);
                    else
                        tameable.setTame(false);
                    tameable.getNavigation().stop();
                    tameable.setOwnerUUID(null);
                    player.displayClientMessage(new TranslationTextComponent("chat.info.release_usage", tameable.getName()), true);
                    this.playTameEffect(false, tameable.level, tameable);

                    if (!player.isCreative())
                        stack.shrink(1);
                }
                return ActionResultType.SUCCESS;
            }
        }
        return ActionResultType.FAIL;
    }

    protected void playTameEffect(boolean play, World world, TameableEntity entity) {
        IParticleData iparticledata = ParticleTypes.HEART;
        if (!play)
            iparticledata = ParticleTypes.SMOKE;

        for (int i = 0; i < 7; ++i) {
            double d0 = world.random.nextGaussian() * 0.02D;
            double d1 = world.random.nextGaussian() * 0.02D;
            double d2 = world.random.nextGaussian() * 0.02D;
            world.addParticle(iparticledata, entity.getRandomX(1.0D), entity.getRandomY() + 0.5D, entity.getRandomZ(1.0D), d0, d1, d2);
        }
    }

    @OnlyIn(Dist.CLIENT)
    @Override
    public void appendHoverText(ItemStack stack, @Nullable World worldIn, List<ITextComponent> tooltip, ITooltipFlag flagIn) {
        tooltip.add(new TranslationTextComponent("tooltip.certificate_" + (this.adoption ? "adopt" : "release") + ".desc"));
    }
}
