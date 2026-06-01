package com.tomrom.flingshot.platform;

import com.tomrom.flingshot.FlingshotConstants;
import com.tomrom.flingshot.platform.services.IRegistryHelper;
import net.minecraft.advancements.CriterionTrigger;
import net.minecraft.core.particles.ParticleType;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.configurations.FeatureConfiguration;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.function.Function;
import java.util.function.Supplier;
import java.util.function.UnaryOperator;

public class NeoForgeRegistryHelper implements IRegistryHelper {

    private static final DeferredRegister.Items ITEMS = DeferredRegister.createItems(FlingshotConstants.MOD_ID);
    private static final DeferredRegister.Blocks BLOCKS = DeferredRegister.createBlocks(FlingshotConstants.MOD_ID);
    private static final DeferredRegister<CreativeModeTab> CREATIVE_TABS = DeferredRegister.create(BuiltInRegistries.CREATIVE_MODE_TAB, FlingshotConstants.MOD_ID);
    private static final DeferredRegister<SoundEvent> SOUND_EVENTS = DeferredRegister.create(BuiltInRegistries.SOUND_EVENT, FlingshotConstants.MOD_ID);
    private static final DeferredRegister<EntityType<?>> ENTITY_TYPES = DeferredRegister.create(BuiltInRegistries.ENTITY_TYPE, FlingshotConstants.MOD_ID);
    private static final DeferredRegister<ParticleType<?>> PARTICLE_TYPES = DeferredRegister.create(BuiltInRegistries.PARTICLE_TYPE, FlingshotConstants.MOD_ID);
    private static final DeferredRegister<Feature<?>> FEATURES = DeferredRegister.create(BuiltInRegistries.FEATURE, FlingshotConstants.MOD_ID);
    private static final DeferredRegister<CriterionTrigger<?>> CRITERION_TRIGGERS = DeferredRegister.create(BuiltInRegistries.TRIGGER_TYPES, FlingshotConstants.MOD_ID);

    public static void register(IEventBus eventBus) {
        BLOCKS.register(eventBus);
        ITEMS.register(eventBus);
        CREATIVE_TABS.register(eventBus);
        SOUND_EVENTS.register(eventBus);
        ENTITY_TYPES.register(eventBus);
        PARTICLE_TYPES.register(eventBus);
        FEATURES.register(eventBus);
        CRITERION_TRIGGERS.register(eventBus);
    }

    @Override
    public <T extends Item> Supplier<T> registerItem(String name, Function<Item.Properties, T> item, UnaryOperator<Item.Properties> properties) {
        return ITEMS.registerItem(name, item, properties);
    }

    @Override
    public <T extends Block> Supplier<T> registerBlock(String name, Function<BlockBehaviour.Properties, T> block, UnaryOperator<BlockBehaviour.Properties> properties) {
        return BLOCKS.registerBlock(name, block, properties);
    }

    @Override
    public <T extends CreativeModeTab> Supplier<T> registerCreativeTab(String name, Supplier<T> tab) {
        return CREATIVE_TABS.register(name, tab);
    }

    @Override
    public <T extends SoundEvent> Supplier<T> registerSoundEvent(String name, Supplier<T> soundEvent) {
        return SOUND_EVENTS.register(name, soundEvent);
    }

    @Override
    public <T extends Entity> Supplier<EntityType<T>> registerEntityType(String name, Supplier<EntityType<T>> entityType) {
        return ENTITY_TYPES.register(name, entityType);
    }

    @Override
    public Supplier<SimpleParticleType> registerParticle(String name) {
        return PARTICLE_TYPES.register(name, () -> new SimpleParticleType(false) {});
    }

    @Override
    public <T extends FeatureConfiguration> Supplier<Feature<T>> registerFeature(String name, Supplier<Feature<T>> feature) {
        return FEATURES.register(name, feature);
    }

    @Override
    public <T extends CriterionTrigger<?>> Supplier<T> registerCriterionTrigger(String name, Supplier<T> trigger) {
        return CRITERION_TRIGGERS.register(name, trigger);
    }
}
