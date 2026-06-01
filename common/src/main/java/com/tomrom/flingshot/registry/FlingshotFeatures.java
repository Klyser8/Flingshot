package com.tomrom.flingshot.registry;

import com.tomrom.flingshot.FlingshotConstants;
import com.tomrom.flingshot.platform.CommonPlatformHelper;
import com.tomrom.flingshot.worldgen.GlimmerGooPatchConfiguration;
import com.tomrom.flingshot.worldgen.GlimmerGooPatchFeature;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.placement.PlacedFeature;

import java.util.function.Supplier;

public class FlingshotFeatures {

    public static final ResourceKey<ConfiguredFeature<?, ?>> GLIMMER_GOO_PATCH_CONFIGURED = ResourceKey.create(
            Registries.CONFIGURED_FEATURE,
            FlingshotConstants.id("glimmer_goo_patch")
    );
    public static final ResourceKey<PlacedFeature> GLIMMER_GOO_PATCH_PLACED = ResourceKey.create(
            Registries.PLACED_FEATURE,
            FlingshotConstants.id("glimmer_goo_patch")
    );

    public static final Supplier<Feature<GlimmerGooPatchConfiguration>> GLIMMER_GOO_PATCH = CommonPlatformHelper.registerFeature(
            "glimmer_goo_patch",
            () -> new GlimmerGooPatchFeature(GlimmerGooPatchConfiguration.CODEC)
    );

    public static void init() {
    }
}
