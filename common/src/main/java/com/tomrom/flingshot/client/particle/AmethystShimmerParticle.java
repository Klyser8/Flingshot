package com.tomrom.flingshot.client.particle;

import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.BaseAshSmokeParticle;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.particle.ParticleProvider;
import net.minecraft.client.particle.ParticleRenderType;
import net.minecraft.client.particle.SpriteSet;
import net.minecraft.core.particles.SimpleParticleType;

public class AmethystShimmerParticle extends BaseAshSmokeParticle {

    private static final int FULL_BRIGHT = 0xF000F0;
    private static final int MAX_AGE = 200;

    private final float deltaAlpha;
    private boolean increasingAlpha;

    protected AmethystShimmerParticle(
            ClientLevel level,
            double x,
            double y,
            double z,
            double xSpeed,
            double ySpeed,
            double zSpeed,
            float quadSizeMultiplier,
            SpriteSet sprites
    ) {
        super(level, x, y, z, 0.01f, 0.0f, 0.01f, xSpeed, ySpeed, zSpeed, quadSizeMultiplier, sprites, 1.0f, MAX_AGE, 0.05f, true);
        lifetime += random.nextInt(100);
        setSpriteFromAge(sprites);
        rCol = 1.0f;
        gCol = 1.0f;
        bCol = 1.0f;
        alpha = 0.8f;
        deltaAlpha = random.nextFloat() / 10.0f + 0.05f;
    }

    @Override
    public void tick() {
        if (alpha <= 0.25f) {
            increasingAlpha = true;
        } else if (alpha >= 1.0f) {
            increasingAlpha = false;
        }

        float nextAlpha = increasingAlpha ? alpha + deltaAlpha : alpha - deltaAlpha;
        setAlpha(clamp(nextAlpha, 0.25f, 1.0f));
        super.tick();
    }

    @Override
    public ParticleRenderType getRenderType() {
        return ParticleRenderType.PARTICLE_SHEET_TRANSLUCENT;
    }

    @Override
    protected int getLightColor(float partialTick) {
        return FULL_BRIGHT;
    }

    private static float clamp(float value, float min, float max) {
        return Math.max(min, Math.min(max, value));
    }

    public static class Factory implements ParticleProvider<SimpleParticleType> {

        private final SpriteSet sprites;

        public Factory(SpriteSet sprites) {
            this.sprites = sprites;
        }

        @Override
        public Particle createParticle(SimpleParticleType options, ClientLevel level, double x, double y, double z, double xAux, double yAux, double zAux) {
            return new AmethystShimmerParticle(level, x, y, z, xAux, yAux, zAux, 1.0f, sprites);
        }
    }
}
