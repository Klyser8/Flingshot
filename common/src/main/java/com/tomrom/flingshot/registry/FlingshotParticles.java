package com.tomrom.flingshot.registry;

import com.tomrom.flingshot.platform.CommonPlatformHelper;
import net.minecraft.core.particles.SimpleParticleType;

import java.util.function.Supplier;

public class FlingshotParticles {

    public static final Supplier<SimpleParticleType> AMETHYST_SHIMMER = CommonPlatformHelper.registerParticle("amethyst_shimmer");
    public static final Supplier<SimpleParticleType> AMETHYST_CRIT = CommonPlatformHelper.registerParticle("amethyst_crit");
    public static final Supplier<SimpleParticleType> GREASE_CHUNK = CommonPlatformHelper.registerParticle("grease_chunk");
    public static final Supplier<SimpleParticleType> GREASE_POP = CommonPlatformHelper.registerParticle("grease_pop");

    public static void init() {
    }
}
