package com.tomrom.flingshot.entity;

import com.tomrom.flingshot.registry.FlingshotAdvancementTriggers;
import com.tomrom.flingshot.registry.FlingshotEntities;
import com.tomrom.flingshot.registry.FlingshotItems;
import com.tomrom.flingshot.registry.FlingshotParticles;
import net.minecraft.core.particles.DustColorTransitionOptions;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.monster.Creeper;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.joml.Vector3f;

import java.util.List;

public class ShimmerShell extends AbstractBuck {

    private static final EntityDataAccessor<Integer> MAX_EXPLOSION_DAMAGE =
            SynchedEntityData.defineId(ShimmerShell.class, EntityDataSerializers.INT);

    public ShimmerShell(EntityType<? extends ShimmerShell> entityType, Level level) {
        super(entityType, level);
        this.pickup = Pickup.DISALLOWED;
    }

    public ShimmerShell(Level level, LivingEntity owner, ItemStack pickupItemStack, ItemStack weapon) {
        super(FlingshotEntities.SHIMMER_SHELL.get(), level, owner, pickupItemStack, weapon);
        this.pickup = Pickup.DISALLOWED;
    }

    private final double baseDamage = 3;
    private final double baseDamageMultiplier = 2;


    @Override
    protected void onHitEntity(EntityHitResult result) {
        Entity entityHit = result.getEntity();
        if (entityHit instanceof Player player && player.isSpectator()) {
            return;
        }

        setDeltaMovement(getDeltaMovement().reverse().scale(0.1));
        if (entityHit instanceof LivingEntity living) {
            boolean shouldExplode = true;
            if (getOwner() != null && getOwner().equals(living)) {
                // Make sure it shouldExplode is false if the entity is younger than 3 ticks
                shouldExplode = tickCount > 2;
            }
            if (shouldExplode) {
                explode(living);
            }
        }
    }

    @Override
    protected @NotNull SoundEvent getDefaultHitGroundSoundEvent() {
        return SoundEvents.TRIDENT_HIT;
    }

    @Override
    protected void playParticleTrail() {
        Vec3 vel = getDeltaMovement().normalize();
        Vec3 center = getBoundingBox().getCenter();
        if (level().isClientSide() && getCollisionAge() == 0) {
            for (int i = 0; i < 3; ++i) {
                Vec3 origin = new Vec3( //subtracting vel.x/y/z will have the trail start more forward/back
                        getX(0.5) - vel.x,
                        getRandomY() / 2 + getRandomY() / 2 - vel.y,
                        getZ(0.5) - vel.z);
                level().addParticle(FlingshotParticles.AMETHYST_CRIT.get(),
                        center.x + vel.x * i / 4.0,
                        center.y + vel.y * i / 8.0,
                        center.z + vel.z * i / 4.0,
                        -vel.x / 2, (-vel.y + 0.2) / 2, -vel.z / 2);
            }
        }
    }

    private void explode(@Nullable LivingEntity hitEntity) {
        boolean stationary = getCollisionAge() > 0 || inGroundTime > 0;
        int killedCreepers = 0;
        playSound(SoundEvents.GENERIC_EXPLODE.value(), 1.0f, 2.0f);
        List<Entity> entities = level().getEntities(this, AABB.ofSize(getEyePosition(), 4, 4, 4),
                entity -> entity instanceof LivingEntity livingEntity
                        && !livingEntity.isDeadOrDying()
                        && !(livingEntity instanceof Player player && player.isSpectator())
                        && livingEntity.hasLineOfSight(this));
        for (Entity entity : entities) {
            boolean isHitEntity = entity.equals(hitEntity);
            float dmg = Math.max(getMaxExplosionDamage() / distanceTo(entity), (float) (getMaxExplosionDamage() / getEyePosition().distanceTo(entity.getEyePosition())));
            if (isHitEntity) {
                dmg += (float) getRandomBaseDamage();
            }
            if (dmg > getRandomBaseDamage() + getMaxExplosionDamage()) {
                dmg = (float) (getRandomBaseDamage() + getMaxExplosionDamage());
            }
            if (!level().isClientSide()) {
                boolean wasLivingCreeper = entity instanceof Creeper creeper && !creeper.isDeadOrDying();
                entity.hurt(buckExplosionDamageSource(), dmg);
                if (stationary && wasLivingCreeper && entity instanceof Creeper creeper && creeper.isDeadOrDying()) {
                    killedCreepers++;
                }
            }
        }
        if (!level().isClientSide()) {
            ServerLevel serverLevel = (ServerLevel) level();
            gameEvent(GameEvent.EXPLODE);
            serverLevel.sendParticles(FlingshotParticles.AMETHYST_SHIMMER.get(),
                    getX(), getY(), getZ(), 50, 0, 0, 0, 0.1);
            DustColorTransitionOptions effect = new DustColorTransitionOptions(
                    new Vector3f(201.0f / 255.0f, 94.0f / 255.0f, 0.0f),
                    new Vector3f(204.0f / 255.0f, 204.0f / 255.0f, 204.0f / 255.0f),
                    1.5f
            );
            serverLevel.sendParticles(effect,
                    getX(), getY() + 0.5, getZ(), 25, 0.5, 0.5, 0.5, 0);
            serverLevel.sendParticles(ParticleTypes.POOF,
                    getX(), getY(), getZ(), 25, 0, 0, 0, 0.1);
            if (stationary && killedCreepers >= 2 && getOwner() instanceof ServerPlayer player) {
                FlingshotAdvancementTriggers.STATIONARY_SHIMMER_SHELL_DOUBLE_CREEPER.get().trigger(player);
            }
            discard();
        }
    }

    @Override
    public void tick() {
        super.tick();

        // Explode if any entity (other than the owner for the first few ticks) touches the shimmer shell
        List<Entity> entities = level().getEntities(this, AABB.ofSize(getEyePosition(), 0.5, 0.5, 0.5), entity -> {
            if (!(entity instanceof LivingEntity livingEntity)) return false;
            if (livingEntity.isDeadOrDying()) return false;
            if (livingEntity instanceof Player player && player.isSpectator()) return false;
            if (!livingEntity.hasLineOfSight(this)) return false;
            // Prevent the owner from immediately triggering the explosion if they just fried it
            if (getOwner() != null && getOwner().equals(livingEntity) && tickCount <= 2) return false;
            return true;
        });
        if (!entities.isEmpty()) {
            explode(null);
            return; // exploded and discarded on server; avoid further behavior this tick
        }

        // Explode automatically after 5 seconds (100 ticks)
        if (tickCount >= 100) {
            explode(null);
            return;
        }

        if (getCollisionAge() > 40 && pickup == Pickup.CREATIVE_ONLY) {
            discard();
        }
    }

    @Override
    protected void onHit(HitResult result) {
        super.onHit(result);
        this.pickup = Pickup.DISALLOWED;
    }

    @Override
    protected @NotNull ItemStack getDefaultPickupItem() {
        return new ItemStack(FlingshotItems.SHIMMER_SHELL.get());
    }

    @Override
    protected void defineSynchedData(SynchedEntityData.Builder builder) {
        super.defineSynchedData(builder);
        builder.define(MAX_EXPLOSION_DAMAGE, 12);
    }

    public int getMaxExplosionDamage() {
        return this.entityData.get(MAX_EXPLOSION_DAMAGE);
    }

    public void setMaxExplosionDamage(int damage) {
        this.entityData.set(MAX_EXPLOSION_DAMAGE, damage);
    }

    private double getRandomBaseDamage() {
        return baseDamage + random.nextDouble() * baseDamageMultiplier;
    }
}
