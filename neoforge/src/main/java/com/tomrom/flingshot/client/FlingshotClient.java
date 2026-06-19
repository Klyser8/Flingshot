package com.tomrom.flingshot.client;

import com.tomrom.flingshot.FlingshotConstants;
import com.tomrom.flingshot.client.particle.AmethystCritParticle;
import com.tomrom.flingshot.client.particle.AmethystShimmerParticle;
import com.tomrom.flingshot.client.particle.GreaseChunkParticle;
import com.tomrom.flingshot.client.particle.GreasePopParticle;
import com.tomrom.flingshot.client.renderer.CopperBuckRenderer;
import com.tomrom.flingshot.client.renderer.FireChargeRenderer;
import com.tomrom.flingshot.client.renderer.FrostBlastRenderer;
import com.tomrom.flingshot.client.renderer.GlimmerGooRenderer;
import com.tomrom.flingshot.client.renderer.ObsidianDiscRenderer;
import com.tomrom.flingshot.client.renderer.ShimmerShellRenderer;
import com.tomrom.flingshot.registry.FlingshotEntities;
import com.tomrom.flingshot.registry.FlingshotParticles;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.EntityRenderersEvent;
import net.neoforged.neoforge.client.event.RegisterParticleProvidersEvent;

@EventBusSubscriber(modid = FlingshotConstants.MOD_ID, value = Dist.CLIENT, bus = EventBusSubscriber.Bus.MOD)
public class FlingshotClient {

    @SubscribeEvent
    private static void registerEntityRenderers(EntityRenderersEvent.RegisterRenderers event) {
        event.registerEntityRenderer(FlingshotEntities.COPPER_BUCK.get(), CopperBuckRenderer::new);
        event.registerEntityRenderer(FlingshotEntities.OBSIDIAN_DISC.get(), ObsidianDiscRenderer::new);
        event.registerEntityRenderer(FlingshotEntities.SHIMMER_SHELL.get(), ShimmerShellRenderer::new);
        event.registerEntityRenderer(FlingshotEntities.FROST_BLAST.get(), FrostBlastRenderer::new);
        event.registerEntityRenderer(FlingshotEntities.GLIMMER_GOO.get(), GlimmerGooRenderer::new);
        event.registerEntityRenderer(FlingshotEntities.FIRE_CHARGE.get(), FireChargeRenderer::new);
    }

    @SubscribeEvent
    private static void registerParticleProviders(RegisterParticleProvidersEvent event) {
        event.registerSpriteSet(FlingshotParticles.AMETHYST_CRIT.get(), AmethystCritParticle.Factory::new);
        event.registerSpriteSet(FlingshotParticles.AMETHYST_SHIMMER.get(), AmethystShimmerParticle.Factory::new);
        event.registerSpriteSet(FlingshotParticles.GREASE_CHUNK.get(), GreaseChunkParticle.Factory::new);
        event.registerSpriteSet(FlingshotParticles.GREASE_POP.get(), GreasePopParticle.Factory::new);
    }
}
