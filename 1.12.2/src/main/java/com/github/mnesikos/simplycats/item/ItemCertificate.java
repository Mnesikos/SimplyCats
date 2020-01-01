package com.github.mnesikos.simplycats.item;

import com.github.mnesikos.simplycats.SimplyCats;
import com.github.mnesikos.simplycats.entity.EntityCat;
import net.minecraft.client.resources.I18n;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.passive.EntityTameable;
import net.minecraft.entity.passive.EntityWolf;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumHand;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.NonNullList;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nullable;
import java.util.List;

public class ItemCertificate extends ItemBase {
    private static final String[] TYPES = new String[] { "adopt", "release" };
    protected String name;

    public ItemCertificate(String name) {
        super(name);
        this.name = name;
        setHasSubtypes(true);
        setMaxDamage(0);
        setMaxStackSize(1);
    }

    @Override
    public void registerItemModel() {
        SimplyCats.PROXY.registerItemRenderer(this, 0, (name + "_adopt"));
        SimplyCats.PROXY.registerItemRenderer(this, 1, (name + "_release"));
    }

    @Override
    public boolean itemInteractionForEntity(ItemStack stack, EntityPlayer player, EntityLivingBase target, EnumHand hand) {
        if (target instanceof EntityCat || target instanceof EntityWolf) {
            if (stack.getMetadata() == 0) {
                if (((EntityTameable) target).isTamed()) {
                    return false;
                } else {
                    ((EntityTameable) target).setTamed(true);
                    ((EntityTameable) target).getNavigator().clearPath();
                    ((EntityTameable) target).setOwnerId(player.getUniqueID());
                    target.setHealth(target.getMaxHealth());
                    if (player.world.isRemote)
                        player.sendMessage(new TextComponentString(new TextComponentTranslation("chat.info.adopt_usage").getFormattedText() + " " + target.getName() + "!"));
                    this.playTameEffect(true, target.world, (EntityTameable)target);

                    if (!player.capabilities.isCreativeMode) {
                        stack.shrink(1);
                        if (stack.getCount() <= 0)
                            player.inventory.setInventorySlotContents(player.inventory.currentItem, ItemStack.EMPTY);
                    }
                }
                return true;

            } else if (stack.getMetadata() == 1) {
                if (((EntityTameable) target).isOwner(player)) {
                    ((EntityTameable) target).setTamed(false);
                    ((EntityTameable) target).getNavigator().clearPath();
                    ((EntityTameable) target).setOwnerId(null);
                    if (player.world.isRemote)
                        player.sendMessage(new TextComponentString(target.getName() + " " + new TextComponentTranslation("chat.info.release_usage").getFormattedText()));
                    this.playTameEffect(false, target.world, (EntityTameable)target);

                    if (!player.capabilities.isCreativeMode) {
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

    protected void playTameEffect(boolean play, World world, EntityTameable entity) {
        EnumParticleTypes enumparticletypes = EnumParticleTypes.HEART;

        if (!play)
            enumparticletypes = EnumParticleTypes.SMOKE_NORMAL;

        for (int i = 0; i < 7; ++i) {
            double d0 = world.rand.nextGaussian() * 0.02D;
            double d1 = world.rand.nextGaussian() * 0.02D;
            double d2 = world.rand.nextGaussian() * 0.02D;
            world.spawnParticle(enumparticletypes, entity.posX + (double)(world.rand.nextFloat() * entity.width * 2.0F) - (double)entity.width, entity.posY + 0.5D + (double)(world.rand.nextFloat() * entity.height), entity.posZ + (double)(world.rand.nextFloat() * entity.width * 2.0F) - (double)entity.width, d0, d1, d2);
        }
    }

    @Override @SideOnly(Side.CLIENT)
    public void getSubItems(CreativeTabs tab, NonNullList<ItemStack> items) {
        if (tab == this.getCreativeTab()) {
            for (int meta = 0; meta < 2; meta++) {
                items.add(new ItemStack(this, 1, meta));
            }
        }
    }

    @Override
    public String getTranslationKey(ItemStack stack) {
        int meta = stack.getMetadata();
        switch (meta) {
            case 0:
            default:
                return "item.certificate." + TYPES[0];
            case 1:
                return "item.certificate." + TYPES[1];
        }
    }

    @Override @SideOnly(Side.CLIENT)
    public void addInformation(ItemStack stack, @Nullable World worldIn, List<String> tooltip, ITooltipFlag flagIn) {
        int i = MathHelper.clamp(stack.getItemDamage(), 0, 1);
        tooltip.add(I18n.format("tooltip.certificate." + TYPES[i] + ".desc"));
    }
}
