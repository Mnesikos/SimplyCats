package com.github.mnesikos.simplycats.item;

import com.github.mnesikos.simplycats.SimplyCats;
import com.github.mnesikos.simplycats.client.gui.GuiCatBook;
import com.github.mnesikos.simplycats.entity.EntityCat;
import com.github.mnesikos.simplycats.init.ModItems;
import net.minecraft.client.Minecraft;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.ListNBT;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.NonNullList;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.world.World;
import net.minecraftforge.common.util.Constants;

import javax.annotation.Nullable;
import java.util.List;

public class ItemCatBook extends ItemBase {
    public static final int GUI_ID = 1;

    public ItemCatBook() {
        super(new Properties().maxStackSize(1).group(SimplyCats.GROUP));
    }

    public void addBookmarkedPage(int id) {
        // todo
    }

    @Override
    public boolean itemInteractionForEntity(ItemStack stack, PlayerEntity player, LivingEntity target, Hand hand) {
        if (target instanceof EntityCat) {
            EntityCat cat = (EntityCat) target;
            stack = player.getHeldItem(hand);

            CompoundNBT compound;
            if (stack.hasTag())
                compound = stack.getTag();
            else
                compound = new CompoundNBT();

            ListNBT tagList;
            boolean catExists = false;
            if (compound.contains("pages")) {
                tagList = compound.getList("pages", Constants.NBT.TAG_COMPOUND);
                for (int i = 0; i < tagList.size(); ++i) {
                    if (tagList.getCompound(i).getUniqueId("UUID").equals(cat.getUniqueID())) {
                        catExists = true;
                        break;
                    }
                }
            } else {
                tagList = new ListNBT();
                compound.put("pages", tagList);
            }

            if (!catExists) {
                CompoundNBT catTag = new CompoundNBT();
                cat.writeAdditional(catTag);
                tagList.add(catTag);
            }

            stack.setTag(compound);

            if (player.isSneaking()) {
                if (player.world.isRemote) {
                    GuiCatBook.book = stack;
                    Minecraft.getInstance().displayGuiScreen(new GuiCatBook(cat));
//                    player.openGui(SimplyCats.instance, GUI_ID, player.world, cat.getEntityId(), (int) player.posY, (int) player.posZ);
                }
            }

        }
        return super.itemInteractionForEntity(stack, player, target, hand);
    }

    @Override
    public ActionResult<ItemStack> onItemRightClick(World world, PlayerEntity player, Hand handIn) {
        // todo
        return super.onItemRightClick(world, player, handIn);
    }

    @Override
    public void fillItemGroup(ItemGroup group, NonNullList<ItemStack> items) {
        if (group == this.getGroup()) {
            ItemStack book = new ItemStack(ModItems.CAT_BOOK.get(), 1);
            book.setTag(new CompoundNBT());
            items.add(book);
        }
    }

    @Override
    public void addInformation(ItemStack stack, @Nullable World worldIn, List<ITextComponent> tooltip, ITooltipFlag flagIn) {
        super.addInformation(stack, worldIn, tooltip, flagIn);
    }
}
