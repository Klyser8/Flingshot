package com.tomrom.flingshot.registry;

import com.tomrom.flingshot.advancement.FlingshotPlayerTrigger;
import com.tomrom.flingshot.platform.CommonPlatformHelper;

import java.util.function.Supplier;

public class FlingshotAdvancementTriggers {

    public static final Supplier<FlingshotPlayerTrigger> SHOOT_BUCK = register("shoot_buck");
    public static final Supplier<FlingshotPlayerTrigger> FLING_FLINGSHOT = register("fling_flingshot");
    public static final Supplier<FlingshotPlayerTrigger> STATIONARY_SHIMMER_SHELL_DOUBLE_CREEPER = register("stationary_shimmer_shell_double_creeper");
    public static final Supplier<FlingshotPlayerTrigger> LIGHT_NETHER_PORTAL = register("light_nether_portal");
    public static final Supplier<FlingshotPlayerTrigger> CEILING_GLIMMER_GOO = register("ceiling_glimmer_goo");
    public static final Supplier<FlingshotPlayerTrigger> BREAK_SHIELD_WITH_OBSIDIAN_DISC = register("break_shield_with_obsidian_disc");
    public static final Supplier<FlingshotPlayerTrigger> FLUNG_ITEM_PICKED_UP = register("flung_item_picked_up");

    private static Supplier<FlingshotPlayerTrigger> register(String name) {
        return CommonPlatformHelper.registerCriterionTrigger(name, FlingshotPlayerTrigger::new);
    }

    public static void init() {
    }
}
