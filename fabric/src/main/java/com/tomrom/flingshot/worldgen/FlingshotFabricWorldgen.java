package com.tomrom.flingshot.worldgen;

import com.tomrom.flingshot.registry.FlingshotFeatures;
import net.fabricmc.fabric.api.biome.v1.BiomeModifications;
import net.fabricmc.fabric.api.biome.v1.BiomeSelectors;
import net.minecraft.world.level.biome.Biomes;
import net.minecraft.world.level.levelgen.GenerationStep;

public class FlingshotFabricWorldgen {

    public static void init() {
        BiomeModifications.addFeature(
                BiomeSelectors.includeByKey(Biomes.DRIPSTONE_CAVES),
                GenerationStep.Decoration.UNDERGROUND_DECORATION,
                FlingshotFeatures.GLIMMER_GOO_PATCH_PLACED
        );
    }
}
