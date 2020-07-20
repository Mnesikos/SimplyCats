package com.github.mnesikos.simplycats.item;

import com.github.mnesikos.simplycats.Ref;
import com.github.mnesikos.simplycats.entity.EntityCat;
import com.github.mnesikos.simplycats.init.ModItems;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.passive.EntityTameable;
import net.minecraft.entity.passive.EntityWolf;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.ChatComponentTranslation;
import net.minecraft.util.IIcon;
import net.minecraft.util.StatCollector;
import net.minecraft.world.World;

import java.util.List;

public class ItemCertificate extends Item {
    public IIcon[] icons = new IIcon[2];
    private static final String[] TYPES = new String[] { "adopt", "release" };

    public ItemCertificate() {
        setUnlocalizedName(Ref.MODID + ":certificate");
        setCreativeTab(ModItems.CREATIVE_TAB);
        setHasSubtypes(true);
        setMaxDamage(0);
        setMaxStackSize(1);
    }

    @Override
    public boolean itemInteractionForEntity(ItemStack stack, EntityPlayer player, EntityLivingBase target) {
        if (target instanceof EntityCat || target instanceof EntityWolf) {
            if (stack.getItemDamage() == 0) {
                if (((EntityTameable) target).isTamed()) {
                    return false;
                } else {
                    ((EntityTameable) target).setTamed(true);
                    ((EntityTameable) target).getNavigator().clearPathEntity();
                    ((EntityTameable) target).func_152115_b(player.getUniqueID().toString());
                    target.setHealth(target.getMaxHealth());
                    if (player.worldObj.isRemote)
                        player.addChatComponentMessage(new ChatComponentText(new ChatComponentTranslation("chat.info.adopt_usage").getFormattedText() + " " + target.getCommandSenderName() + "!"));
                    this.playTameEffect(true, target.worldObj, (EntityTameable)target);

                    if (!player.capabilities.isCreativeMode) {
                        --stack.stackSize;
                        if (stack.stackSize <= 0)
                            player.inventory.setInventorySlotContents(player.inventory.currentItem, null);
                    }
                }
                return true;

            } else if (stack.getItemDamage() == 1) {
                if (((EntityTameable) target).getOwner() == player) {
                    ((EntityTameable) target).setTamed(false);
                    ((EntityTameable) target).getNavigator().clearPathEntity();
                    ((EntityTameable) target).func_152115_b("");
                    if (player.worldObj.isRemote)
                        player.addChatComponentMessage(new ChatComponentText(target.getCommandSenderName() + " " + new ChatComponentTranslation("chat.info.release_usage").getFormattedText()));
                    this.playTameEffect(false, target.worldObj, (EntityTameable)target);

                    if (!player.capabilities.isCreativeMode) {
                        --stack.stackSize;
                        if (stack.stackSize <= 0)
                            player.inventory.setInventorySlotContents(player.inventory.currentItem, null);
                    }
                }
                return true;
            }
        }
        return false;
    }

    protected void playTameEffect(boolean play, World world, EntityTameable entity) {
        String enumparticletypes = "heart";

        if (!play)
            enumparticletypes = "smoke";

        for (int i = 0; i < 7; ++i) {
            double d0 = world.rand.nextGaussian() * 0.02D;
            double d1 = world.rand.nextGaussian() * 0.02D;
            double d2 = world.rand.nextGaussian() * 0.02D;
            world.spawnParticle(enumparticletypes, entity.posX + (double)(world.rand.nextFloat() * entity.width * 2.0F) - (double)entity.width, entity.posY + 0.5D + (double)(world.rand.nextFloat() * entity.height), entity.posZ + (double)(world.rand.nextFloat() * entity.width * 2.0F) - (double)entity.width, d0, d1, d2);
        }
    }

    @Override @SideOnly(Side.CLIENT)
    public void getSubItems(Item item, CreativeTabs tab, List items) {
        if (tab == this.getCreativeTab()) {
            for (int meta = 0; meta < 2; meta++) {
                items.add(new ItemStack(this, 1, meta));
            }
        }
    }

    @Override
    public String getItemStackDisplayName(ItemStack stack) {
        int meta = stack.getItemDamage();
        switch (meta) {
            case 0:
            default:
                return "item.certificate." + TYPES[0] + ".name";
            case 1:
                return "item.certificate." + TYPES[1] + ".name";
        }
    }

    @Override
    public void registerIcons(IIconRegister reg) {
        for (int i = 0; i < 2; i++)
            this.icons[i] = reg.registerIcon(Ref.MODID + ":certificate_" + TYPES[i]);
    }

    @Override
    public IIcon getIconFromDamage(int meta) {
        return this.icons[meta];
    }

    @Override @SideOnly(Side.CLIENT)
    public void addInformation(ItemStack stack, EntityPlayer player, List tooltip, boolean isAdvanced) {
        tooltip.add(StatCollector.translateToLocal("tooltip.certificate." + TYPES[stack.getItemDamage()] + ".desc"));
    }
}
