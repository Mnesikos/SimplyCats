package com.github.mnesikos.simplycats.configuration;

import com.github.mnesikos.simplycats.Ref;
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

    @Name("Enable Beckon Command")
    @Comment({
            "ONLY enable this if you know what you're doing.",
            "This command can and will corrupt your world if used incorrectly."
    })
    @LangKey(PREFIX + ".command_beckon")
    public static boolean COMMAND_BECKON = false;

    @Name("Enable Adopt-a-Dog")
    @Comment("Disabling this will remove the villager trade to get a one-time-use pet carrier containing a dog.")
    @LangKey(PREFIX + ".adopt_a_dog")
    public static boolean ADOPT_A_DOG = true;

    @Name("Cats in Area Breeding Limit")
    @Comment("This number is used to limit cat breeding; if more than this amount of cats are nearby, automatic breeding will be disabled.")
    @LangKey(PREFIX + ".breeding_limit")
    public static int BREEDING_LIMIT = 12;

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
