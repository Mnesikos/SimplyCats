package com.github.mnesikos.simplycats;

import com.github.mnesikos.simplycats.block.BlockCatBowl;
import com.github.mnesikos.simplycats.client.gui.GuiBowl;
import com.github.mnesikos.simplycats.client.gui.GuiCatBook;
import com.github.mnesikos.simplycats.entity.EntityCat;
import com.github.mnesikos.simplycats.inventory.ContainerBowl;
import com.github.mnesikos.simplycats.item.ItemCatBook;
import com.github.mnesikos.simplycats.tileentity.TileEntityCatBowl;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.network.IGuiHandler;

import javax.annotation.Nullable;

@Mod.EventBusSubscriber
public class NetworkGuiHandler implements IGuiHandler {
    public static final NetworkGuiHandler INSTANCE = new NetworkGuiHandler();

    @Nullable
    @Override
    public Container getServerGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z) {
        if (ID == BlockCatBowl.GUI_ID)
            return new ContainerBowl(player.inventory, (TileEntityCatBowl) world.getTileEntity(new BlockPos(x, y, z)));
        else if (ID == ItemCatBook.GUI_ID) {
        }
        return null;
    }

    @Nullable
    @Override
    public Object getClientGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z) {
        switch (ID) {
            case BlockCatBowl.GUI_ID:
                return new GuiBowl(getServerGuiElement(ID, player, world, x, y, z), (TileEntityCatBowl) world.getTileEntity(new BlockPos(x, y, z)));
            case ItemCatBook.GUI_ID:
                if (x == 0) return new GuiCatBook();
                Entity target = player.world.getEntityByID(x);
                if (!(target instanceof EntityCat)) return null;
                EntityCat cat = (EntityCat) target;
                return new GuiCatBook(cat); // props to Doggy Talents mod for using x as the entity idea thank you
            default:
                return null;
        }
    }
}
