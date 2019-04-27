package com.github.mnesikos.simplycats.item;

import java.util.List;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.MathHelper;
import net.minecraft.util.StatCollector;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class ItemCatFood extends Item {
    private static final String[] TYPES = new String[] { "fishy", "salmon", "chicken", "beef" };
    
	public ItemCatFood() {
		super();
        setHasSubtypes(true);
        setMaxDamage(0);
	}

    @Override @SideOnly(Side.CLIENT)
    public void getSubItems(Item item, CreativeTabs tab, List items) {
        for (int meta = 0; meta < 4; meta++) {
        	items.add(new ItemStack(item, 1, meta));
        }
    }

	@SideOnly(Side.CLIENT)
    public void addInformation(ItemStack stack, EntityPlayer player, List tooltip, boolean isAdvanced) {
        int i = MathHelper.clamp_int(stack.getItemDamage(), 0, 3);
        tooltip.add(StatCollector.translateToLocal("tooltip.catFood." + TYPES[i] + ".desc"));
    }
    
}
