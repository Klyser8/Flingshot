package com.tomrom.flingshot.client.particle;

import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.particle.ParticleProvider;
import net.minecraft.client.particle.SimpleAnimatedParticle;
import net.minecraft.client.particle.SpriteSet;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.util.RandomSource;

public class GreasePopParticle extends SimpleAnimatedParticle {

    protected GreasePopParticle(ClientLevel level, double x, double y, double z, double xSpeed, double ySpeed, double zSpeed, SpriteSet sprites) {
        super(level, x, y, z, sprites, 0.0f);
        xd = xSpeed;
        yd = ySpeed;
        zd = zSpeed;
        quadSize = 0.15f;
        lifetime = 14 + random.nextInt(2);
        hasPhysics = false;
        alpha = 0.55f;
        setSpriteFromAge(sprites);
    }

    public static class Factory implements ParticleProvider<SimpleParticleType> {

        private final SpriteSet sprites;

        public Factory(SpriteSet sprites) {
            this.sprites = sprites;
        }

        @Override
        public Particle createParticle(SimpleParticleType type, ClientLevel level, double x, double y, double z, double xSpeed, double ySpeed, double zSpeed, RandomSource random) {
            return new GreasePopParticle(level, x, y, z, xSpeed, ySpeed, zSpeed, sprites);
        }
    }
}
