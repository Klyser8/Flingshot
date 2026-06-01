package com.tomrom.flingshot.client.particle;

import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.particle.ParticleProvider;
import net.minecraft.client.particle.ParticleRenderType;
import net.minecraft.client.particle.SimpleAnimatedParticle;
import net.minecraft.client.particle.SpriteSet;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.util.RandomSource;

public class AmethystCritParticle extends SimpleAnimatedParticle {

    private static final int MAX_AGE = 10;

    protected AmethystCritParticle(ClientLevel level, double x, double y, double z, double xSpeed, double ySpeed, double zSpeed, SpriteSet sprites) {
        super(level, x, y, z, sprites, 0.75f);
        this.xd = xSpeed;
        this.yd = ySpeed;
        this.zd = zSpeed;
        lifetime = MAX_AGE;
        setSpriteFromAge(sprites);
    }

    @Override
    public void tick() {
        if (age > 5) {
            setAlpha(1.0f - (float) age / lifetime / 2.0f);
        }
        super.tick();
    }

    @Override
    public ParticleRenderType getGroup() {
        return ParticleRenderType.SINGLE_QUADS;
    }

    public static class Factory implements ParticleProvider<SimpleParticleType> {

        private final SpriteSet sprites;

        public Factory(SpriteSet sprites) {
            this.sprites = sprites;
        }

        @Override
        public Particle createParticle(SimpleParticleType type, ClientLevel level, double x, double y, double z, double xSpeed, double ySpeed, double zSpeed, RandomSource random) {
            return new AmethystCritParticle(level, x, y, z, xSpeed, ySpeed, zSpeed, sprites);
        }
    }
}
