package com.github.mnesikos.simplycats.proxy;

import com.github.mnesikos.simplycats.Ref;
import com.github.mnesikos.simplycats.client.render.entity.RenderCat;
import com.github.mnesikos.simplycats.entity.EntityCat;
import com.github.mnesikos.simplycats.init.ModItems;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.world.World;
import net.minecraftforge.client.event.ModelRegistryEvent;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.fml.client.registry.RenderingRegistry;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

@Mod.EventBusSubscriber
public class ClientProxy extends CommonProxy {
    public static int RENDER_BOWL_ID;

    @Override
    public void preInit(FMLPreInitializationEvent e) {
        super.preInit(e);

        RenderingRegistry.registerEntityRenderingHandler(EntityCat.class, RenderCat::new);

        /*RenderBowl renderBowl = new RenderBowl();
        RENDER_BOWL_ID = RenderingRegistry.getNextAvailableRenderId();
        RenderingRegistry.registerBlockHandler(RENDER_BOWL_ID, renderBowl);*/
    }

    @SubscribeEvent
    public static void registerItemModels(ModelRegistryEvent e) {
        for (Item item : ModItems.ITEMS)
            ModelLoader.setCustomModelResourceLocation(item, 0, new ModelResourceLocation(item.getRegistryName(), "inventory"));

        for (int i = 0; i < 4; i++)
            ModelLoader.setCustomModelResourceLocation(ModItems.CAT_FOOD, i, new ModelResourceLocation(Ref.MODID + ":cat_food_" + i));
        for (int i = 0; i < 5; i++)
            ModelLoader.setCustomModelResourceLocation(ModItems.PET_CARRIER, i, new ModelResourceLocation(Ref.MODID + ":pet_carrier"));

        ModelLoader.setCustomModelResourceLocation(ModItems.CERTIFICATE, 0, new ModelResourceLocation(Ref.MODID + ":certificate_adopt"));
        ModelLoader.setCustomModelResourceLocation(ModItems.CERTIFICATE, 1, new ModelResourceLocation(Ref.MODID + ":certificate_release"));
    }

    @Override
    public Object getClientGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z) {
        /*TileEntity te = world.getTileEntity(x, y, z);
        if (te != null) {
            if (ID == BlockBowl.guiID) {
                if (!(te instanceof TileEntityBowl))
                    return null;
                else
                    return new GuiBowl((TileEntityBowl)te, player);
            }
        }*/
        return null;
    }
}
