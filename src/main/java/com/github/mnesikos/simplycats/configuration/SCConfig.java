package com.github.mnesikos.simplycats.configuration;

import com.github.mnesikos.simplycats.SimplyCats;
import net.minecraftforge.common.ForgeConfigSpec;

import java.util.Arrays;
import java.util.List;

public class SCConfig {
    public static final ForgeConfigSpec.Builder BUILDER = new ForgeConfigSpec.Builder();
    public static final Common COMMON = new Common(BUILDER);
    private static final String PREFIX = "config." + SimplyCats.MOD_ID;

    public static class Common {
        public static ForgeConfigSpec.BooleanValue join_message;
        public static ForgeConfigSpec.BooleanValue attack_ai;
        public static ForgeConfigSpec.ConfigValue<List<? extends String>> prey_list;
        public static ForgeConfigSpec.DoubleValue wander_area_limit;
        public static ForgeConfigSpec.IntValue tamed_limit;
        public static ForgeConfigSpec.IntValue breeding_limit;
        public static ForgeConfigSpec.IntValue kitten_mature_timer;
        public static ForgeConfigSpec.IntValue pregnancy_timer;
        public static ForgeConfigSpec.IntValue heat_timer;
        public static ForgeConfigSpec.IntValue heat_cooldown;
        public static ForgeConfigSpec.IntValue male_cooldown;

        Common(final ForgeConfigSpec.Builder builder) {
            builder.push("common");
            join_message = builder
                    .comment("Join Message")
                    .comment("Enable or disables the initial join message with a player's cat count.")
                    .translation(PREFIX + ".join_message")
                    .define("join_message", false);

            attack_ai = builder
                    .comment("Cat Attack AI")
                    .comment("Disabling this will not allow cats to attack entities in their prey list, essentially a peaceful mode for cats.")
                    .translation(PREFIX + ".attack_ai")
                    .define("ATTACK_AI", true);

            prey_list = builder
                    .comment("Cats' Prey List")
                    .comment("This is a list of entities all cats will attack on sight if cat attack AI is enabled.")
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
                            "animania:chick_wyandotte"), o -> o instanceof String);

            wander_area_limit = builder.worldRestart()
                    .comment("Wander Area Limit")
                    .comment("When a cat's home is set, this is the distance in blocks they are allowed to roam.")
                    .translation(PREFIX + ".wander_area_limit")
                    .defineInRange("wander_area_limit", 400.0D, 10.0D, Double.MAX_VALUE);

            tamed_limit = builder
                    .comment("Tamed Cats Limit")
                    .comment("Sets a limit of cats each player is allowed to have tamed, setting this to 0 will disable the limit.")
                    .translation(PREFIX + ".tamed_limit")
                    .defineInRange("tamed_limit", 0, 0, Integer.MAX_VALUE);

            breeding_limit = builder
                    .comment("Cats in Area Breeding Limit")
                    .comment("This number is used to limit cat breeding; if more than this amount of cats are nearby, automatic breeding will be disabled.")
                    .translation(PREFIX + ".breeding_limit")
                    .defineInRange("breeding_limit", 20, 0, Integer.MAX_VALUE);

            kitten_mature_timer = builder
                    .comment("Kitten Mature Timer")
                    .comment("Number of minecraft ticks before a kitten becomes an adult.",
                            "Default: 168000 (7 full minecraft days)")
                    .translation(PREFIX + ".kitten_mature_timer")
                    .defineInRange("kitten_mature_timer", 24000 * 7, 0, Integer.MAX_VALUE);

            pregnancy_timer = builder
                    .comment("Pregnancy Timer")
                    .comment("Number of minecraft ticks before a pregnant cat will give birth.",
                            "Default: 96000 (4 full minecraft days)")
                    .translation(PREFIX + ".pregnancy_timer")
                    .defineInRange("pregnancy_timer", 24000 * 4, 0, Integer.MAX_VALUE);

            heat_timer = builder
                    .comment("Heat Timer")
                    .comment("Number of minecraft ticks that a cat will be in heat.",
                            "Default: 48000 (2 full minecraft days)")
                    .translation(PREFIX + ".heat_timer")
                    .defineInRange("heat_timer", 24000 * 2, 0, Integer.MAX_VALUE);

            heat_cooldown = builder
                    .comment("Heat Cooldown")
                    .comment("Number of minecraft ticks that a cat will not go into heat.",
                            "Default: 384000 (16 full minecraft days)")
                    .translation(PREFIX + ".heat_cooldown")
                    .defineInRange("heat_cooldown", 24000 * 16, 0, Integer.MAX_VALUE);

            male_cooldown = builder
                    .comment("Male Cooldown")
                    .comment("Number of minecraft ticks that a male cat will not try to breed after breeding once already.",
                            "Default: 6000 (1/4th minecraft day)")
                    .translation(PREFIX + ".male_cooldown")
                    .defineInRange("male_cooldown", 24000 / 4, 0, Integer.MAX_VALUE);
            builder.pop();
        }
    }

    public static final ForgeConfigSpec SPEC = BUILDER.build();

}
