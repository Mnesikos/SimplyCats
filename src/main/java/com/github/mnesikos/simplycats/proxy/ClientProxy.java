package com.github.mnesikos.simplycats.proxy;

import com.github.mnesikos.simplycats.Ref;
import com.github.mnesikos.simplycats.block.BlockCatBowl;
import com.github.mnesikos.simplycats.client.gui.GuiBowl;
import com.github.mnesikos.simplycats.client.gui.GuiCatBook;
import com.github.mnesikos.simplycats.client.render.entity.RenderCat;
import com.github.mnesikos.simplycats.entity.AbstractCat;
import com.github.mnesikos.simplycats.entity.EntityCat;
import com.github.mnesikos.simplycats.item.ItemCatBook;
import com.github.mnesikos.simplycats.tileentity.TileEntityCatBowl;
import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.fml.client.registry.RenderingRegistry;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.ArrayList;
import java.util.List;

public class ClientProxy extends CommonProxy {
    public static final List<ItemVariant> VARIANTS = new ArrayList<>();

    @Override
    public void preInit(FMLPreInitializationEvent e) {
        super.preInit(e);

        RenderingRegistry.registerEntityRenderingHandler(EntityCat.class, RenderCat::new);
    }

    @Override
    public void registerItemRenderer(Item item, int meta, String id) {
        VARIANTS.add(new ItemVariant(meta, id, item));
    }

    @Override
    public void registerVariants() {
        super.registerVariants();
        for (ItemVariant variant : VARIANTS) {
            ModelLoader.setCustomModelResourceLocation(variant.item, variant.meta, new ModelResourceLocation(Ref.MODID + ":" + variant.name, "inventory"));
        }
    }

    @Override
    public Object getClientGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z) {
        switch (ID) {
            case BlockCatBowl.GUI_ID:
                return new GuiBowl(getServerGuiElement(ID, player, world, x, y, z), (TileEntityCatBowl)world.getTileEntity(new BlockPos(x, y, z)));
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

    private static class ItemVariant {
        private final int meta;
        private final String name;
        private final Item item;

        public ItemVariant(int meta, String name, Item item) {
            this.meta = meta;
            this.name = name;
            this.item = item;
        }
    }
}
