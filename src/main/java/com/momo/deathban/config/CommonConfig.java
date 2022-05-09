package com.momo.deathban.config;

import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.common.ForgeConfigSpec.Builder;
import org.apache.commons.lang3.tuple.Pair;

public class CommonConfig {

    public static final ForgeConfigSpec CONFIG;
    private static final CommonConfig INSTANCE;

    static {
        final Pair<CommonConfig, ForgeConfigSpec> pair = new Builder().configure(CommonConfig::new);
        CONFIG = pair.getRight();
        INSTANCE = pair.getLeft();
    }

    public static CommonConfig getInstance() {
        return INSTANCE;
    }

//    public final IntValue dayDuration;
//    public final IntValue hourDuration;
//    public final IntValue minuteDuration;
//    public final IntValue secondDuration;

    private CommonConfig(Builder builder) {
        builder.comment("Common configuration settings").push("common");
        builder.pop();
    }

}
