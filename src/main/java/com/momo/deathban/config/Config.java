package com.momo.deathban.config;

import net.minecraftforge.common.ForgeConfigSpec;

public class Config {

    public static final ForgeConfigSpec.Builder BUILDER = new ForgeConfigSpec.Builder();
    public static final ForgeConfigSpec SPEC;

    public static final ForgeConfigSpec.ConfigValue<Integer> weekTime;
    public static final ForgeConfigSpec.ConfigValue<Integer> dayTime;
    public static final ForgeConfigSpec.ConfigValue<Integer> hourTime;
    public static final ForgeConfigSpec.ConfigValue<Integer> minuteTime;


    static {
        BUILDER.push("Configurations for DeathBan Mod");

        weekTime = BUILDER.comment("The week duration of the death ban").define("Weeks", 0);
        dayTime = BUILDER.comment("The day duration of the death ban").define("Days", 0);
        hourTime = BUILDER.comment("The hour duration of the death ban").define("Hours", 0);
        minuteTime = BUILDER.comment("The minute duration of the death ban").define("Minutes", 0);

        BUILDER.pop();

        SPEC = BUILDER.build();
    }

}
