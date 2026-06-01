package com.tomrom.flingshot.worldgen;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.world.level.levelgen.feature.configurations.FeatureConfiguration;

public record GlimmerGooPatchConfiguration(int minBlocks, int maxBlocks, int searchRange) implements FeatureConfiguration {

    public static final Codec<GlimmerGooPatchConfiguration> CODEC = RecordCodecBuilder.create(instance -> instance.group(
            Codec.intRange(1, 2048).fieldOf("min_blocks").forGetter(GlimmerGooPatchConfiguration::minBlocks),
            Codec.intRange(1, 2048).fieldOf("max_blocks").forGetter(GlimmerGooPatchConfiguration::maxBlocks),
            Codec.intRange(1, 64).fieldOf("search_range").forGetter(GlimmerGooPatchConfiguration::searchRange)
    ).apply(instance, GlimmerGooPatchConfiguration::new));
}
