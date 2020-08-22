package com.github.mnesikos.simplycats.proxy;

/*import com.github.mnesikos.simplycats.Ref;
import com.github.mnesikos.simplycats.block.BlockBowl;
import com.github.mnesikos.simplycats.client.gui.GuiBowl;
import com.github.mnesikos.simplycats.client.render.entity.RenderCat;
import com.github.mnesikos.simplycats.entity.EntityCat;
import com.github.mnesikos.simplycats.tileentity.TileEntityBowl;
import net.minecraft.item.Item;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.fml.client.registry.RenderingRegistry;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber
public class ClientProxy extends CommonProxy {
    @Override
    public void preInit(FMLPreInitializationEvent e) {
        super.preInit(e);

        RenderingRegistry.registerEntityRenderingHandler(EntityCat.class, RenderCat::new);
    }

    @Override
    public void registerItemRenderer(Item item, int meta, String id) {
        ModelLoader.setCustomModelResourceLocation(item, meta, new ModelResourceLocation(Ref.MODID + ":" + id, "inventory"));
    }

    @Override
    public Object getClientGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z) {
        switch (ID) {
            case BlockBowl.GUI_ID:
                return new GuiBowl(getServerGuiElement(ID, player, world, x, y, z), (TileEntityBowl)world.getTileEntity(new BlockPos(x, y, z)));
            default:
                return null;
        }
    }
}*/
