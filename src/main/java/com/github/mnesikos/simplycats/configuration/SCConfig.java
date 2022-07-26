package com.github.mnesikos.simplycats.configuration;

import com.github.mnesikos.simplycats.SimplyCats;
import net.minecraftforge.common.ForgeConfigSpec;

import java.util.Arrays;
import java.util.List;

public class SCConfig {
    public static final ForgeConfigSpec SPEC;
    private static final String PREFIX = "config." + SimplyCats.MOD_ID;
    public static ForgeConfigSpec.BooleanValue join_message;
    public static ForgeConfigSpec.BooleanValue attack_ai;
    public static ForgeConfigSpec.BooleanValue replace_tamed_vanilla;
    public static ForgeConfigSpec.BooleanValue stop_vanilla_spawns;
//    public static ForgeConfigSpec.BooleanValue intact_stray_spawns;
    public static ForgeConfigSpec.ConfigValue<Double> wander_area_limit;
    public static ForgeConfigSpec.ConfigValue<Integer> tamed_limit;
    public static ForgeConfigSpec.ConfigValue<Integer> breeding_limit;
    public static ForgeConfigSpec.ConfigValue<Integer> kitten_mature_timer;
    public static ForgeConfigSpec.ConfigValue<Integer> pregnancy_timer;
    public static ForgeConfigSpec.ConfigValue<Integer> heat_timer;
    public static ForgeConfigSpec.ConfigValue<Integer> heat_cooldown;
    public static ForgeConfigSpec.ConfigValue<Integer> male_cooldown;
    public static ForgeConfigSpec.ConfigValue<List<? extends String>> prey_list;

    static {
        ForgeConfigSpec.Builder configBuilder = new ForgeConfigSpec.Builder();
        setupConfig(configBuilder);
        SPEC = configBuilder.build();
    }

    private static void setupConfig(ForgeConfigSpec.Builder builder) {
        builder.push("Options");
        replace_tamed_vanilla = builder.worldRestart()
                .comment(" If you have existing tamed vanilla cats in your world and want to change them to Simply Cats, enable this and restart your minecraft instance.")
                .translation(PREFIX + ".replace_tamed_vanilla")
                .define("replace_tamed_vanilla", false);

        stop_vanilla_spawns = builder
                .comment(" Disables vanilla cats spawning, set to false to allow them to spawn again.")
                .translation(PREFIX + ".stop_vanilla_spawns")
                .define("stop_vanilla_spawns", true);

        /*intact_stray_spawns = builder
                .comment(" Disable this if you want stray village cats spawning fixed (enabled for intact cat spawns).")
                .translation(PREFIX + ".intact_stray_spawns")
                .define("intact_stray_spawns", true); todo*/

        join_message = builder
                .comment(" Enable or disable the initial join message with a player's cat count.")
                .translation(PREFIX + ".join_message")
                .define("join_message", false);

        attack_ai = builder
                .comment(" Disabling this will not allow cats to attack entities in their prey list, essentially a peaceful mode for cats.")
                .translation(PREFIX + ".attack_ai")
                .define("attack_ai", true);

        wander_area_limit = builder.worldRestart()
                .comment(" When a cat's home is set, this is the distance in blocks they are allowed to roam.",
                        " Default: 400.0")
                .translation(PREFIX + ".wander_area_limit")
                .define("wander_area_limit", 400.0D);

        tamed_limit = builder
                .comment(" Sets a limit of cats each player is allowed to have tamed, setting this to 0 will disable the limit.")
                .translation(PREFIX + ".tamed_limit")
                .define("tamed_limit", 0);

        breeding_limit = builder
                .comment(" This number is used to limit cat breeding; if more than this amount of cats are nearby, automatic breeding will be disabled.",
                        " Default: 20")
                .translation(PREFIX + ".breeding_limit")
                .define("breeding_limit", 20);
        builder.pop();


        builder.push("Timers");
        kitten_mature_timer = builder
                .comment(" Number of minecraft ticks before a kitten becomes an adult.",
                        " Default: 168000 (7 full minecraft days)")
                .translation(PREFIX + ".kitten_mature_timer")
                .define("kitten_mature_timer", 24000 * 7);

        pregnancy_timer = builder
                .comment(" Number of minecraft ticks before a pregnant cat will give birth.",
                        " Default: 96000 (4 full minecraft days)")
                .translation(PREFIX + ".pregnancy_timer")
                .define("pregnancy_timer", 24000 * 4);

        heat_timer = builder
                .comment(" Number of minecraft ticks that a cat will be in heat.",
                        " Default: 48000 (2 full minecraft days)")
                .translation(PREFIX + ".heat_timer")
                .define("heat_timer", 24000 * 2);

        heat_cooldown = builder
                .comment(" Number of minecraft ticks that a cat will not go into heat.",
                        " Default: 384000 (16 full minecraft days)")
                .translation(PREFIX + ".heat_cooldown")
                .define("heat_cooldown", 24000 * 16);

        male_cooldown = builder
                .comment(" Number of minecraft ticks that a male cat will not try to breed after breeding once already.",
                        " Default: 6000 (1/4th minecraft day)")
                .translation(PREFIX + ".male_cooldown")
                .define("male_cooldown", 24000 / 4);
        builder.pop();


        builder.push("Lists");
        prey_list = builder
                .comment(" This is a list of entities all cats will attack on sight if cat attack AI is enabled.")
                .translation(PREFIX + ".prey_list")
                .define("prey_list", Arrays.asList("minecraft:bat", "minecraft:parrot", "minecraft:chicken", "minecraft:rabbit",
                        "minecraft:silverfish", "rats:rat", "zawa:brownrat", "zawa:cockatoo", "zawa:frigate", "zawa:macaw",
                        "zawa:rattlesnake", "zawa:toucan", "zawa:treefrog", "exoticbirds:woodpecker", "birdwmod:brown_booby",
                        "birdwmod:eastern_bluebird", "birdwmod:eurasian_bullfinch", "birdwmod:great_grey_owl", "birdwmod:green_heron",
                        "birdwmod:hoatzin", "birdwmod:killdeer", "birdwmod:kingofsaxony_bird_of_paradise", "birdwmod:northern_mockingbird",
                        "birdwmod:redflanked_bluetail", "birdwmod:rednecked_nightjar", "birdwmod:stellers_eider", "birdwmod:turquoisebrowed_motmot",
                        "exoticbirds:bluejay", "exoticbirds:booby", "exoticbirds:budgerigar", "exoticbirds:cardinal", "exoticbirds:duck",
                        "exoticbirds:gouldianfinch", "exoticbirds:hummingbird", "exoticbirds:kingfisher", "exoticbirds:kiwi",
                        "exoticbirds:kookaburra", "exoticbirds:lyrebird", "exoticbirds:magpie", "exoticbirds:parrot", "exoticbirds:pigeon",
                        "exoticbirds:roadrunner", "exoticbirds:robin", "exoticbirds:toucan", "animania:hamster", "animania:frog",
                        "animania:toad", "animania:buck_cottontail", "animania:doe_cottontail", "animania:kit_cottontail", "animania:buck_chinchilla",
                        "animania:doe_chinchilla", "animania:kit_chinchilla", "animania:buck_dutch", "animania:doe_dutch", "animania:kit_dutch",
                        "animania:buck_havana", "animania:doe_havana", "animania:kit_havana", "animania:buck_jack", "animania:doe_jack", "animania:kit_jack",
                        "animania:buck_new_zealand", "animania:doe_new_zealand", "animania:kit_new_zealand", "animania:buck_rex", "animania:doe_rex",
                        "animania:kit_rex", "animania:buck_lop", "animania:doe_lop", "animania:kit_lop", "animania:rooster_leghorn", "animania:rooster_orpington",
                        "animania:rooster_plymouth_rock", "animania:rooster_rhode_island_red", "animania:rooster_wyandotte", "animania:hen_leghorn",
                        "animania:hen_orpington", "animania:hen_plymouth_rock", "animania:hen_rhode_island_red", "animania:hen_wyandotte",
                        "animania:chick_leghorn", "animania:chick_orpington", "animania:chick_plymouth_rock", "animania:chick_rhode_island_red",
                        "animania:chick_wyandotte"), entry -> true);
        builder.pop();
    }
}
