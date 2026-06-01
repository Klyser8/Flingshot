package com.tomrom.flingshot.platform;

import com.tomrom.flingshot.FlingshotConstants;
import com.tomrom.flingshot.platform.services.IRegistryHelper;
import net.minecraft.advancements.CriterionTrigger;
import net.fabricmc.fabric.api.particle.v1.FabricParticleTypes;
import net.minecraft.core.Registry;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
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

public class FabricRegistryHelper implements IRegistryHelper {

    @Override
    public <T extends Item> Supplier<T> registerItem(String name, Function<Item.Properties, T> item, UnaryOperator<Item.Properties> properties) {
        T registered = Registry.register(
                BuiltInRegistries.ITEM,
                FlingshotConstants.id(name),
                item.apply(properties.apply(new Item.Properties()).setId(ResourceKey.create(Registries.ITEM, FlingshotConstants.id(name))))
        );
        return () -> registered;
    }

    @Override
    public <T extends Block> Supplier<T> registerBlock(String name, Function<BlockBehaviour.Properties, T> block, UnaryOperator<BlockBehaviour.Properties> properties) {
        T registered = Registry.register(
                BuiltInRegistries.BLOCK,
                FlingshotConstants.id(name),
                block.apply(properties.apply(BlockBehaviour.Properties.of()).setId(ResourceKey.create(Registries.BLOCK, FlingshotConstants.id(name))))
        );
        return () -> registered;
    }

    @Override
    public <T extends CreativeModeTab> Supplier<T> registerCreativeTab(String name, Supplier<T> tab) {
        T registered = Registry.register(BuiltInRegistries.CREATIVE_MODE_TAB, FlingshotConstants.id(name), tab.get());
        return () -> registered;
    }

    @Override
    public <T extends SoundEvent> Supplier<T> registerSoundEvent(String name, Supplier<T> soundEvent) {
        T registered = Registry.register(BuiltInRegistries.SOUND_EVENT, FlingshotConstants.id(name), soundEvent.get());
        return () -> registered;
    }

    @Override
    public <T extends Entity> Supplier<EntityType<T>> registerEntityType(String name, Supplier<EntityType<T>> entityType) {
        EntityType<T> registered = Registry.register(BuiltInRegistries.ENTITY_TYPE, FlingshotConstants.id(name), entityType.get());
        return () -> registered;
    }

    @Override
    public Supplier<SimpleParticleType> registerParticle(String name) {
        SimpleParticleType registered = Registry.register(BuiltInRegistries.PARTICLE_TYPE, FlingshotConstants.id(name), FabricParticleTypes.simple());
        return () -> registered;
    }

    @Override
    public <T extends FeatureConfiguration> Supplier<Feature<T>> registerFeature(String name, Supplier<Feature<T>> feature) {
        Feature<T> registered = Registry.register(BuiltInRegistries.FEATURE, FlingshotConstants.id(name), feature.get());
        return () -> registered;
    }

    @Override
    public <T extends CriterionTrigger<?>> Supplier<T> registerCriterionTrigger(String name, Supplier<T> trigger) {
        T registered = Registry.register(BuiltInRegistries.TRIGGER_TYPES, FlingshotConstants.id(name), trigger.get());
        return () -> registered;
    }
}
