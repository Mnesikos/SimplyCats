package com.github.mnesikos.simplycats.item;

import com.github.mnesikos.simplycats.client.gui.CatBookScreen;
import com.github.mnesikos.simplycats.entity.SimplyCatEntity;
import net.minecraft.client.Minecraft;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import javax.annotation.Nullable;
import java.util.List;

public class CatBookItem extends Item {
    public CatBookItem() {
        super(new Item.Properties().stacksTo(1));
    }

    public void addBookmarkedPage(int id) {
        // todo
    }

    @Override
    public InteractionResult interactLivingEntity(ItemStack stack, Player player, LivingEntity target, InteractionHand hand) {
        if (target instanceof SimplyCatEntity && player.isDiscrete()) {
            SimplyCatEntity cat = (SimplyCatEntity) target;
            stack = player.getItemInHand(hand);

            CompoundTag compound = stack.getOrCreateTag();

            ListTag tagList;
            boolean catExists = false;
            int catInList = 0;
            if (compound.contains("pages")) {
                tagList = compound.getList("pages", 10);
                for (int i = 0; i < tagList.size(); ++i) {
                    if (tagList.getCompound(i).getUUID("UUID").equals(cat.getUUID())) {
                        catExists = true;
                        catInList = i;
                        break;
                    }
                }
            } else {
                tagList = new ListTag();
                compound.put("pages", tagList);
            }

            CompoundTag catTag = new CompoundTag();
            cat.save(catTag);

            ResourceLocation key = EntityType.getKey(cat.getType());
            catTag.putString("id", key.toString());
            if (cat.hasCustomName()) catTag.putString("DisplayName", cat.getDisplayName().getString());

            if (!catExists) {
                tagList.add(catTag);
                player.displayClientMessage(Component.translatable("chat.book.save_cat_data", cat.getName()), true);
            } else {
                tagList.setTag(catInList, catTag);
                player.displayClientMessage(Component.translatable("chat.book.update_cat_data", cat.getName()), true);
            }

            stack.setTag(compound);

            /*if (player.level.isClientSide) // todo ???
                Minecraft.getInstance().setScreen(new CatBookScreen(compound, player.level, catInList));*/

            return InteractionResult.SUCCESS;
        }

        return InteractionResult.PASS;
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level world, Player player, InteractionHand hand) {
        CompoundTag bookTag = player.getItemInHand(hand).getTag();
        if (bookTag == null || bookTag.isEmpty())
            player.displayClientMessage(Component.translatable("chat.book.empty_book"), true);
        else if (world.isClientSide)
            this.openCatBook(bookTag, world);

        return super.use(world, player, hand);
    }

    @OnlyIn(Dist.CLIENT)
    public void openCatBook(CompoundTag bookTag, Level world) {
        Minecraft.getInstance().setScreen(new CatBookScreen(bookTag, world));
    }

    @OnlyIn(Dist.CLIENT)
    @Override
    public void appendHoverText(ItemStack stack, @Nullable Level worldIn, List<Component> tooltip, TooltipFlag flagIn) {
        tooltip.add(Component.translatable("tooltip.cat_book.usage"));
    }
}
