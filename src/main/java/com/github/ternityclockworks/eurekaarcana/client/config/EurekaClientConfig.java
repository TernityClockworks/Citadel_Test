package com.github.ternityclockworks.eurekaarcana.client.config;

import net.minecraftforge.common.ForgeConfigSpec;

public class EurekaClientConfig {

    public final ForgeConfigSpec.BooleanValue exampleBoolConfig;

    public EurekaClientConfig(final ForgeConfigSpec.Builder builder) {
        builder.push("example");
        exampleBoolConfig = builder.comment("example human-readable comment text.").translation("example_boolean_config").define("example_bool_config", true);
        builder.pop();
    }
}