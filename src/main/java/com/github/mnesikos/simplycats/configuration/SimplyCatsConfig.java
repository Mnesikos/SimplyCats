package com.github.mnesikos.simplycats.configuration;

import com.electronwill.nightconfig.core.file.CommentedFileConfig;
import com.electronwill.nightconfig.core.io.WritingMode;
import com.github.mnesikos.simplycats.Ref;
import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;

import java.nio.file.Path;

@Mod.EventBusSubscriber
public class SimplyCatsConfig {
    private static final String PREFIX = "config." + Ref.MODID;
    /*public static final String CATEGORY_BLOCKS = "blocks";
    public static final String CATEGORY_ITEMS = "items";*/
    public static final String CATEGORY_CATS = "cats";
    public static final String SUBCATEGORY_TIMERS = "timers";

    private static final ForgeConfigSpec.Builder COMMON_BUILDER = new ForgeConfigSpec.Builder();

    public static ForgeConfigSpec COMMON_CONFIG;

    public static ForgeConfigSpec.BooleanValue COMMAND_BECKON;
    public static ForgeConfigSpec.BooleanValue NAME_TAG_RECIPE;
    public static ForgeConfigSpec.BooleanValue ADOPT_A_DOG;

    /*@RequiresMcRestart*/
    public static ForgeConfigSpec.DoubleValue WANDER_AREA_LIMIT;
    public static ForgeConfigSpec.IntValue BREEDING_LIMIT;

    public static ForgeConfigSpec.IntValue KITTEN_MATURE_TIMER;
    public static ForgeConfigSpec.IntValue PREGNANCY_TIMER;
    public static ForgeConfigSpec.IntValue HEAT_TIMER;
    public static ForgeConfigSpec.IntValue HEAT_COOLDOWN;
    public static ForgeConfigSpec.IntValue MALE_COOLDOWN;

    static {

        COMMON_BUILDER.comment("Cat Settings").push(CATEGORY_CATS);
        setupCatConfig();
        COMMON_BUILDER.pop();

        /*COMMON_BUILDER.comment("Block Settings").push(CATEGORY_BLOCKS);
        COMMON_BUILDER.pop();

        COMMON_BUILDER.comment("Item Settings").push(CATEGORY_ITEMS);
        COMMON_BUILDER.pop();*/

        COMMAND_BECKON = COMMON_BUILDER.comment(
                "ONLY enable this if you know what you're doing.",
                "This command can and will corrupt your world if used incorrectly.")
                .translation(PREFIX + ".command_beckon")
                .define("command_beckon", false);
        ADOPT_A_DOG = COMMON_BUILDER.comment("Disabling this will remove the villager trade to get a one-time-use pet carrier containing a dog.")
                .translation(PREFIX + ".adopt_a_dog")
                .define("adopt_a_dog", true);
        NAME_TAG_RECIPE = COMMON_BUILDER.comment(
                "You can easily disable the name tag recipe added by this mod by setting this value to false.")
                .translation(PREFIX + ".name_tag_recipe")
                .define("name_tag_recipe", true);

        COMMON_CONFIG = COMMON_BUILDER.build();
    }

    private static void setupCatConfig() {
        COMMON_BUILDER.comment("Timer Settings").push(SUBCATEGORY_TIMERS);
        KITTEN_MATURE_TIMER = COMMON_BUILDER.comment(
                "Number of minecraft ticks before a kitten becomes an adult.",
                "Default: 168000 (7 full minecraft days)")
                .translation(PREFIX + ".kitten_mature_timer")
                .defineInRange("kitten_mature_timer", 24000 * 7, 0, Integer.MAX_VALUE);
        PREGNANCY_TIMER = COMMON_BUILDER.comment(
                "Number of minecraft ticks before a pregnant cat will give birth.",
                "Default: 96000 (4 full minecraft days)")
                .translation(PREFIX + ".pregnancy_timer")
                .defineInRange("pregnancy_timer", 24000 * 4, 0, Integer.MAX_VALUE);
        HEAT_TIMER = COMMON_BUILDER.comment(
                "Number of minecraft ticks that a cat will be in heat.",
                "Default: 48000 (2 full minecraft days)")
                .translation(PREFIX + ".heat_timer")
                .defineInRange("heat_timer", 24000 * 2, 0, Integer.MAX_VALUE);
        HEAT_COOLDOWN = COMMON_BUILDER.comment(
                "Number of minecraft ticks that a cat will not go into heat.",
                "Default: 384000 (16 full minecraft days)")
                .translation(PREFIX + ".heat_cooldown")
                .defineInRange("heat_cooldown", 24000 * 16, 0, Integer.MAX_VALUE);
        MALE_COOLDOWN = COMMON_BUILDER.comment(
                "Number of minecraft ticks that a male cat will not try to breed after breeding once already.",
                "Default: 6000 (1/4th minecraft day)")
                .translation(PREFIX + ".male_cooldown")
                .defineInRange("male_cooldown", 24000 / 4, 0, Integer.MAX_VALUE);
        COMMON_BUILDER.pop();

        WANDER_AREA_LIMIT = COMMON_BUILDER.comment(
                "When a cat's home is set, this is the distance in blocks they are allowed to roam.")
                .translation(PREFIX + ".wander_area_limit")
                .defineInRange("wander_area_limit", 400.0D, 1.0D, 600.0D);
        BREEDING_LIMIT = COMMON_BUILDER.comment(
                "This number is used to limit cat breeding; if more than this amount of cats are nearby, automatic breeding will be disabled.")
                .translation(PREFIX + ".breeding_limit")
                .defineInRange("breeding_limit", 20, 0, Integer.MAX_VALUE);
    }

    public static void loadConfig(ForgeConfigSpec spec, Path path) {
        final CommentedFileConfig configData = CommentedFileConfig.builder(path).sync().autosave().writingMode(WritingMode.REPLACE).build();
        configData.load();
        spec.setConfig(configData);
    }

    @SubscribeEvent
    public static void onLoad(final ModConfig.Loading configLoad) {

    }

    @SubscribeEvent
    public static void onReload(final ModConfig.ConfigReloading configReload) {

    }
}