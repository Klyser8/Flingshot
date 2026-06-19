package com.tomrom.flingshot.platform.services;

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

public interface IRegistryHelper {

    <T extends Item> Supplier<T> registerItem(String name, Function<Item.Properties, T> item, UnaryOperator<Item.Properties> properties);

    <T extends Block> Supplier<T> registerBlock(String name, Function<BlockBehaviour.Properties, T> block, UnaryOperator<BlockBehaviour.Properties> properties);

    <T extends CreativeModeTab> Supplier<T> registerCreativeTab(String name, Supplier<T> tab);

    <T extends SoundEvent> Supplier<T> registerSoundEvent(String name, Supplier<T> soundEvent);

    <T extends Entity> Supplier<EntityType<T>> registerEntityType(String name, Supplier<EntityType<T>> entityType);

    Supplier<SimpleParticleType> registerParticle(String name);

    <T extends FeatureConfiguration> Supplier<Feature<T>> registerFeature(String name, Supplier<Feature<T>> feature);

    <T extends CriterionTrigger<?>> Supplier<T> registerCriterionTrigger(String name, Supplier<T> trigger);
}
