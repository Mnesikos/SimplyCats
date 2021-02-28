package com.github.mnesikos.simplycats.item;

import com.github.mnesikos.simplycats.SimplyCats;
import com.github.mnesikos.simplycats.client.gui.GuiCatBook;
import com.github.mnesikos.simplycats.entity.AbstractCat;
import net.minecraft.client.resources.I18n;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.world.World;
import net.minecraftforge.common.util.Constants;

import javax.annotation.Nullable;
import java.util.List;

public class ItemCatBook extends Item {
    public static final int GUI_ID = 1;

    public ItemCatBook() {
        super();
        this.setMaxStackSize(1);
    }

    public void addBookmarkedPage(int id) {
        // todo
    }

    @Override
    public boolean itemInteractionForEntity(ItemStack stack, EntityPlayer player, EntityLivingBase target, EnumHand hand) {
        if (target instanceof AbstractCat) {
            AbstractCat cat = (AbstractCat) target;
            stack = player.getHeldItem(hand);

            NBTTagCompound compound;
            if (stack.hasTagCompound())
                compound = stack.getTagCompound();
            else
                compound = new NBTTagCompound();

            NBTTagList tagList;
            boolean catExists = false;
            if (compound.hasKey("pages")) {
                tagList = compound.getTagList("pages", Constants.NBT.TAG_COMPOUND);
                for (int i = 0; i < tagList.tagCount(); ++i) {
                    if (tagList.getCompoundTagAt(i).getUniqueId("UUID").equals(cat.getUniqueID())) {
                        catExists = true;
                        break;
                    }
                }
            } else {
                tagList = new NBTTagList();
                compound.setTag("pages", tagList);
            }

            if (!catExists) {
                NBTTagCompound catTag = new NBTTagCompound();
                cat.writeToNBTOptional(catTag);
                tagList.appendTag(catTag);
            }

            stack.setTagCompound(compound);

            if (player.isSneaking()) {
                if (player.world.isRemote)
                    GuiCatBook.book = stack;
                player.openGui(SimplyCats.instance, GUI_ID, player.world, cat.getEntityId(), (int)player.posY, (int)player.posZ);
            }

        }
        return super.itemInteractionForEntity(stack, player, target, hand);
    }

    @Override
    public ActionResult<ItemStack> onItemRightClick(World world, EntityPlayer player, EnumHand hand) {
        /*ItemStack book = player.getHeldItem(hand);

        if (!player.isSneaking()) {
            if (world.isRemote)
                GuiCatBook.book = book;
            player.openGui(SimplyCats.instance, GUI_ID, player.world, 0, (int) player.posY, (int) player.posZ);
        }*/

        return super.onItemRightClick(world, player, hand);
    }

    @Override
    public void addInformation(ItemStack stack, @Nullable World worldIn, List<String> tooltip, ITooltipFlag flagIn) {
        tooltip.add(I18n.format("tooltip.cat_book.usage"));
    }
}
