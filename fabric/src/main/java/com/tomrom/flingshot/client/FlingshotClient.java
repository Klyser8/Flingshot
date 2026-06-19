package com.tomrom.flingshot.client;

import com.tomrom.flingshot.client.particle.AmethystCritParticle;
import com.tomrom.flingshot.client.particle.AmethystShimmerParticle;
import com.tomrom.flingshot.client.particle.GreaseChunkParticle;
import com.tomrom.flingshot.client.particle.GreasePopParticle;
import com.tomrom.flingshot.client.FlingshotItemProperties;
import com.tomrom.flingshot.client.renderer.CopperBuckRenderer;
import com.tomrom.flingshot.client.renderer.FireChargeRenderer;
import com.tomrom.flingshot.client.renderer.FrostBlastRenderer;
import com.tomrom.flingshot.client.renderer.GlimmerGooRenderer;
import com.tomrom.flingshot.client.renderer.ObsidianDiscRenderer;
import com.tomrom.flingshot.client.renderer.ShimmerShellRenderer;
import com.tomrom.flingshot.registry.FlingshotBlocks;
import com.tomrom.flingshot.registry.FlingshotEntities;
import com.tomrom.flingshot.registry.FlingshotParticles;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.blockrenderlayer.v1.BlockRenderLayerMap;
import net.fabricmc.fabric.api.client.particle.v1.ParticleFactoryRegistry;
import net.fabricmc.fabric.api.client.rendering.v1.EntityRendererRegistry;
import net.minecraft.client.renderer.RenderType;

public class FlingshotClient implements ClientModInitializer {

    @Override
    public void onInitializeClient() {
        FlingshotItemProperties.register();

        EntityRendererRegistry.register(FlingshotEntities.COPPER_BUCK.get(), CopperBuckRenderer::new);
        EntityRendererRegistry.register(FlingshotEntities.OBSIDIAN_DISC.get(), ObsidianDiscRenderer::new);
        EntityRendererRegistry.register(FlingshotEntities.SHIMMER_SHELL.get(), ShimmerShellRenderer::new);
        EntityRendererRegistry.register(FlingshotEntities.FROST_BLAST.get(), FrostBlastRenderer::new);
        EntityRendererRegistry.register(FlingshotEntities.GLIMMER_GOO.get(), GlimmerGooRenderer::new);
        EntityRendererRegistry.register(FlingshotEntities.FIRE_CHARGE.get(), FireChargeRenderer::new);

        ParticleFactoryRegistry.getInstance().register(FlingshotParticles.AMETHYST_CRIT.get(), AmethystCritParticle.Factory::new);
        ParticleFactoryRegistry.getInstance().register(FlingshotParticles.AMETHYST_SHIMMER.get(), AmethystShimmerParticle.Factory::new);
        ParticleFactoryRegistry.getInstance().register(FlingshotParticles.GREASE_CHUNK.get(), GreaseChunkParticle.Factory::new);
        ParticleFactoryRegistry.getInstance().register(FlingshotParticles.GREASE_POP.get(), GreasePopParticle.Factory::new);

        BlockRenderLayerMap.INSTANCE.putBlock(FlingshotBlocks.GLIMMER_GOO_SPLAT.get(), RenderType.translucent());
    }
}
