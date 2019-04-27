package com.github.mnesikos.simplycats;

import com.github.mnesikos.simplycats.proxy.CommonProxy;
import cpw.mods.fml.common.SidedProxy;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.event.FMLServerStartingEvent;
import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.Mod.EventHandler;
import cpw.mods.fml.common.event.FMLInitializationEvent;

@Mod(modid = SimplyCats.MODID, name = SimplyCats.MODNAME, version = SimplyCats.VERSION,
        acceptedMinecraftVersions = "1.7.10")
public class SimplyCats {
    public static final String MODID = "simplycats";
    public static final String MODNAME = "Simply Cats";
    public static final String VERSION = "1.7.10-0.0.1.0-beta1";

    @Mod.Instance
    public static SimplyCats instance;

    @SidedProxy(clientSide="com.github.mnesikos.simplycats.proxy.ClientProxy",
            serverSide="com.github.mnesikos.simplycats.proxy.ServerProxy")
    public static CommonProxy proxy;

    @EventHandler
    public void preInit(FMLPreInitializationEvent e) {
        proxy.preInit(e);
    }

    @EventHandler
    public void init(FMLInitializationEvent e) {
        proxy.init(e);
    }

    @EventHandler
    public void postInit(FMLPostInitializationEvent e) {
        proxy.postInit(e);
    }

    @EventHandler
    public void serverLoad(FMLServerStartingEvent event) {
        event.registerServerCommand(new CommandBeckon());
    }
}
