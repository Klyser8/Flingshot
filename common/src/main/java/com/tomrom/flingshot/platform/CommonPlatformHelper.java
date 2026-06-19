package com.tomrom.flingshot.platform;

import net.minecraft.advancements.triggers.CriterionTrigger;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.configurations.FeatureConfiguration;

import java.util.function.Function;
import java.util.function.Supplier;
import java.util.function.UnaryOperator;

public class CommonPlatformHelper {

    public static <T extends Item> Supplier<T> registerItem(String name, Function<Item.Properties, T> item) {
        return registerItem(name, item, UnaryOperator.identity());
    }

    public static <T extends Item> Supplier<T> registerItem(String name, Function<Item.Properties, T> item, UnaryOperator<Item.Properties> properties) {
        return Services.REGISTRY.registerItem(name, item, properties);
    }

    public static <T extends Block> Supplier<T> registerBlock(String name, Function<BlockBehaviour.Properties, T> block, UnaryOperator<BlockBehaviour.Properties> properties) {
        return Services.REGISTRY.registerBlock(name, block, properties);
    }

    public static <T extends CreativeModeTab> Supplier<T> registerCreativeTab(String name, Supplier<T> tab) {
        return Services.REGISTRY.registerCreativeTab(name, tab);
    }

    public static <T extends SoundEvent> Supplier<T> registerSoundEvent(String name, Supplier<T> soundEvent) {
        return Services.REGISTRY.registerSoundEvent(name, soundEvent);
    }

    public static <T extends Entity> Supplier<EntityType<T>> registerEntityType(String name, Supplier<EntityType<T>> entityType) {
        return Services.REGISTRY.registerEntityType(name, entityType);
    }

    public static Supplier<SimpleParticleType> registerParticle(String name) {
        return Services.REGISTRY.registerParticle(name);
    }

    public static <T extends FeatureConfiguration> Supplier<Feature<T>> registerFeature(String name, Supplier<Feature<T>> feature) {
        return Services.REGISTRY.registerFeature(name, feature);
    }

    public static <T extends CriterionTrigger<?>> Supplier<T> registerCriterionTrigger(String name, Supplier<T> trigger) {
        return Services.REGISTRY.registerCriterionTrigger(name, trigger);
    }
}
