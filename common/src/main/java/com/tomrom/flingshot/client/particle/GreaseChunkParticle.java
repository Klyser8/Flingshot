package com.tomrom.flingshot.client.particle;

import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.BaseAshSmokeParticle;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.particle.ParticleProvider;
import net.minecraft.client.particle.SpriteSet;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.util.RandomSource;

public class GreaseChunkParticle extends BaseAshSmokeParticle {

    private static final int MAX_AGE = 25;
    private static final int MAX_GROUND_AGE = 10;

    private int groundAge;

    protected GreaseChunkParticle(ClientLevel level, double x, double y, double z, double xSpeed, double ySpeed, double zSpeed, SpriteSet sprites) {
        super(level, x, y, z, 0.1f, 0.1f, 0.1f, xSpeed, ySpeed, zSpeed, 1.0f, sprites, 1.0f, MAX_AGE, 1.0f, true);
        rCol = 1.0f;
        gCol = 1.0f;
        bCol = 1.0f;
        alpha = 0.8f;
    }

    @Override
    public void tick() {
        if (onGround) {
            groundAge++;
            setAlpha(Math.max(1.0f - ((groundAge / (float) MAX_GROUND_AGE) + 0.2f), 0.0f));
            if (groundAge >= MAX_GROUND_AGE) {
                remove();
            }
        }
        super.tick();
    }

    public static class Factory implements ParticleProvider<SimpleParticleType> {

        private final SpriteSet sprites;

        public Factory(SpriteSet sprites) {
            this.sprites = sprites;
        }

        @Override
        public Particle createParticle(SimpleParticleType type, ClientLevel level, double x, double y, double z, double xSpeed, double ySpeed, double zSpeed, RandomSource random) {
            return new GreaseChunkParticle(level, x, y, z, xSpeed, ySpeed, zSpeed, sprites);
        }
    }
}
