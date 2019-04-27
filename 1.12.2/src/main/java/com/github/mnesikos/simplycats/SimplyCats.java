package com.github.mnesikos.simplycats;

import com.github.mnesikos.simplycats.proxy.CommonProxy;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.event.FMLServerStartingEvent;

@Mod(modid = SimplyCats.MODID, name = SimplyCats.MODNAME, version = SimplyCats.VERSION,
        acceptedMinecraftVersions = "1.12.2")
public class SimplyCats {
    public static final String MODID = "simplycats";
    public static final String MODNAME = "Simply Cats";
    public static final String VERSION = "1.12.2-0.0.1.0-beta1";

    @Mod.Instance
    public static SimplyCats instance;

    @SidedProxy(clientSide="com.github.mnesikos.simplycats.proxy.ClientProxy",
            serverSide="com.github.mnesikos.simplycats.proxy.ServerProxy")
    public static CommonProxy PROXY;

    @EventHandler
    public void preInit(FMLPreInitializationEvent e) {
        PROXY.preInit(e);
    }

    @EventHandler
    public void init(FMLInitializationEvent e) {
        PROXY.init(e);
    }

    @EventHandler
    public void postInit(FMLPostInitializationEvent e) {
        PROXY.postInit(e);
    }

    @EventHandler
    public void serverLoad(FMLServerStartingEvent event) {
        event.registerServerCommand(new CommandBeckon());
    }
}
