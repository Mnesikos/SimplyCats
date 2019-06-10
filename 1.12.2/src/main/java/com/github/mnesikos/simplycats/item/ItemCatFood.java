package com.github.mnesikos.simplycats.item;

import com.github.mnesikos.simplycats.Ref;
import com.github.mnesikos.simplycats.SimplyCats;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;
import net.minecraft.util.math.MathHelper;
import net.minecraft.client.resources.I18n;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nullable;
import java.util.List;

public class ItemCatFood extends Item {
    private static final String[] TYPES = new String[] { "fishy", "salmon", "chicken", "beef" };

    public ItemCatFood() {
        setTranslationKey("cat_food");
        setRegistryName(Ref.MODID + ":cat_food");
        setCreativeTab(SimplyCats.PROXY.SIMPLYCATS);
        setHasSubtypes(true);
        setMaxDamage(0);
    }

    @Override @SideOnly(Side.CLIENT)
    public void getSubItems(CreativeTabs tab, NonNullList<ItemStack> items) {
        if (tab == this.getCreativeTab()) {
            for (int meta = 0; meta < 4; meta++) {
                items.add(new ItemStack(this, 1, meta));
            }
        }
    }

    @Override @SideOnly(Side.CLIENT)
    public void addInformation(ItemStack stack, @Nullable World worldIn, List<String> tooltip, ITooltipFlag flagIn) {
        int i = MathHelper.clamp(stack.getItemDamage(), 0, 3);
        tooltip.add(I18n.format("tooltip.cat_food." + TYPES[i] + ".desc"));
    }

}
