package com.github.mnesikos.simplycats.item;

import com.github.mnesikos.simplycats.SimplyCats;
import com.github.mnesikos.simplycats.entity.SimplyCatEntity;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.animal.Parrot;
import net.minecraft.world.entity.TamableAnimal;
import net.minecraft.world.entity.animal.Wolf;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.InteractionHand;
import net.minecraft.network.chat.Component;
import net.minecraft.world.level.Level;
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
    public InteractionResult interactLivingEntity(ItemStack stack, Player player, LivingEntity target, InteractionHand hand) {
        if (target instanceof SimplyCatEntity || target instanceof Wolf || target instanceof Parrot) {
            TamableAnimal tameable = (TamableAnimal) target;
            if (this.adoption) {
                if ((tameable instanceof SimplyCatEntity && ((SimplyCatEntity) tameable).canBeTamed(player)) || (!(tameable instanceof SimplyCatEntity) && !tameable.isTame())) {
                    if (tameable instanceof SimplyCatEntity)
                        ((SimplyCatEntity) tameable).setTamed(true, player);
                    else
                        tameable.tame(player);
                    tameable.getNavigation().stop();
                    tameable.setOrderedToSit(true);
                    tameable.setHealth(tameable.getMaxHealth());
                    player.displayClientMessage(Component.translatable("chat.info.adopt_usage", tameable.getName()), true);
                    if (player.level.isClientSide)
                        this.playTameEffect(true, tameable.level, tameable);

                    if (!player.isCreative())
                        stack.shrink(1);

                    return InteractionResult.SUCCESS;

                } else if (tameable instanceof SimplyCatEntity && !tameable.isTame()) {
                    player.displayClientMessage(Component.translatable("chat.info.tamed_limit_reached"), true);
                    return InteractionResult.PASS;
                }

            } else {
                if (tameable.isOwnedBy(player)) {
                    if (tameable instanceof SimplyCatEntity)
                        ((SimplyCatEntity) tameable).setTamed(false, player);
                    else
                        tameable.setTame(false);
                    tameable.getNavigation().stop();
                    tameable.setOwnerUUID(null);
                    player.displayClientMessage(Component.translatable("chat.info.release_usage", tameable.getName()), true);
                    this.playTameEffect(false, tameable.level, tameable);

                    if (!player.isCreative())
                        stack.shrink(1);

                    return InteractionResult.SUCCESS;
                }
            }
        }
        return InteractionResult.PASS;
    }

    protected void playTameEffect(boolean play, Level world, TamableAnimal entity) {
        ParticleOptions iparticledata = ParticleTypes.HEART;
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
    public void appendHoverText(ItemStack stack, @Nullable Level worldIn, List<Component> tooltip, TooltipFlag flagIn) {
        tooltip.add(Component.translatable("tooltip.certificate_" + (this.adoption ? "adopt" : "release") + ".desc"));
    }
}
