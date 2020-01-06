package com.github.mnesikos.simplycats.configuration;

import com.github.mnesikos.simplycats.Ref;
/*import net.minecraftforge.common.config.Config;
import net.minecraftforge.common.config.Config.*;
import net.minecraftforge.common.config.ConfigManager;
import net.minecraftforge.fml.client.event.ConfigChangedEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

@Config(modid = Ref.MODID, name = "SimplyCats/" + Ref.MODID + "-" + Ref.VERSION)
@LangKey("config." + Ref.MODID + ".title")*/
public class SimplyCatsConfig {
    private static final String PREFIX = "config." + Ref.MODID;

    /*@Name("Beckon Command")
    @Comment({
            "ONLY enable this if you know what you're doing.",
            "This command can and will corrupt your world if used incorrectly."
    })
    @LangKey(PREFIX + ".command_beckon")*/
    public static boolean COMMAND_BECKON = false;

    /*@Name("Name Tag Recipe")
    @Comment("You can easily disable the name tag recipe added by this mod by setting this value to false.")
    @LangKey(PREFIX + ".name_tag_recipe")
    public static boolean NAME_TAG_RECIPE = true;*/

    /*@Name("Adopt-a-Dog")
    @Comment("Disabling this will remove the villager trade to get a one-time-use pet carrier containing a dog.")
    @LangKey(PREFIX + ".adopt_a_dog")*/
    public static boolean ADOPT_A_DOG = true;

    /*@Name("Wander Area Limit") @RangeDouble(min = 10.0D, max = 600.0D)
    @Comment("When a cat's home is set, this is the distance in blocks they are allowed to roam.")
    @LangKey(PREFIX + ".wander_area_limit") @RequiresMcRestart*/
    public static double WANDER_AREA_LIMIT = 400.0D;

    /*@Name("Cats in Area Breeding Limit")
    @Comment("This number is used to limit cat breeding; if more than this amount of cats are nearby, automatic breeding will be disabled.")
    @LangKey(PREFIX + ".breeding_limit")*/
    public static int BREEDING_LIMIT = 20;

    /*@Name("Kitten Mature Timer")
    @Comment({
            "Number of minecraft ticks before a kitten becomes an adult.",
            "Default: 168000 (7 full minecraft days)"
    })
    @LangKey(PREFIX + ".kitten_mature_timer")*/
    public static int KITTEN_MATURE_TIMER = 24000 * 7;

    /*@Name("Pregnancy Timer")
    @Comment({
            "Number of minecraft ticks before a pregnant cat will give birth.",
            "Default: 96000 (4 full minecraft days)"
    })
    @LangKey(PREFIX + ".pregnancy_timer")*/
    public static int PREGNANCY_TIMER = 24000 * 4;

    /*@Name("Heat Timer")
    @Comment({
            "Number of minecraft ticks that a cat will be in heat.",
            "Default: 48000 (2 full minecraft days)"
    })
    @LangKey(PREFIX + ".heat_timer")*/
    public static int HEAT_TIMER = 24000 * 2;

    /*@Name("Heat Cooldown")
    @Comment({
            "Number of minecraft ticks that a cat will not go into heat.",
            "Default: 384000 (16 full minecraft days)"
    })
    @LangKey(PREFIX + ".heat_cooldown")*/
    public static int HEAT_COOLDOWN = 24000 * 16;

    /*@Name("Male Cooldown")
    @Comment({
            "Number of minecraft ticks that a male cat will not try to breed after breeding once already.",
            "Default: 6000 (1/4th minecraft day)"
    })
    @LangKey(PREFIX + ".male_cooldown")*/
    public static int MALE_COOLDOWN = 24000 / 4;

    /*@Mod.EventBusSubscriber(modid = Ref.MODID)
    private static class EventHandler {
        @SubscribeEvent
        public static void onConfigChanged(ConfigChangedEvent.OnConfigChangedEvent event) {
            if (event.getModID().equals(Ref.MODID)) {
                ConfigManager.sync(Ref.MODID, Config.Type.INSTANCE);
            }
        }
    }*/
}