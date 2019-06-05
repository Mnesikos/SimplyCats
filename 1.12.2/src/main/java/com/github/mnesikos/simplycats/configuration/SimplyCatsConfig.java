package com.github.mnesikos.simplycats.configuration;

import com.github.mnesikos.simplycats.util.Ref;
import net.minecraftforge.common.config.Config;
import net.minecraftforge.common.config.Config.*;
import net.minecraftforge.common.config.ConfigManager;
import net.minecraftforge.fml.client.event.ConfigChangedEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

@Config(modid = Ref.MODID, name = "SimplyCats/" + Ref.MODID + "-" + Ref.VERSION)
@LangKey("config." + Ref.MODID + ".title")
public class SimplyCatsConfig {
    private static final String PREFIX = "config." + Ref.MODID;

    @Name("Pregnancy Timer")
    @Comment({
            "Number of minecraft ticks before a pregnant cat will give birth.",
            "Default: 96000 (4 full minecraft days)"
    })
    @LangKey(PREFIX + ".pregnancy_timer")
    public static int prengancyTimer = 24000 * 4;

    @Name("Heat Timer")
    @Comment({
            "Number of minecraft ticks that a cat will be in heat.",
            "Default: 48000 (2 full minecraft days)"
    })
    @LangKey(PREFIX + ".heat_timer")
    public static int heatTimer = 24000 * 2;

    @Name("Heat Cooldown")
    @Comment({
            "Number of minecraft ticks that a cat will not go into heat.",
            "Default: 384000 (16 full minecraft days)"
    })
    @LangKey(PREFIX + ".heat_cooldown")
    public static int heatCooldown = 24000 * 16;

    @Name("Male Cooldown")
    @Comment({
            "Number of minecraft ticks that a male cat will not try to breed after breeding once already.",
            "Default: 6000 (1/4th minecraft day)"
    })
    @LangKey(PREFIX + ".male_cooldown")
    public static int maleCooldown = 24000 / 4;

    @Mod.EventBusSubscriber(modid = Ref.MODID)
    private static class EventHandler {
        @SubscribeEvent
        public static void onConfigChanged(ConfigChangedEvent.OnConfigChangedEvent event) {
            if (event.getModID().equals(Ref.MODID)) {
                ConfigManager.sync(Ref.MODID, Config.Type.INSTANCE);
            }
        }
    }
}
