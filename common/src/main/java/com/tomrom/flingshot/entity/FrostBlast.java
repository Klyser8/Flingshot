package com.tomrom.flingshot.entity;

import com.tomrom.flingshot.registry.FlingshotEntities;
import com.tomrom.flingshot.registry.FlingshotItems;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class FrostBlast extends AbstractBuck{
    public static final int MAX_FREEZE_TICKS = 2000;
    private static final EntityDataAccessor<Integer> FREEZE_DURATION =
            SynchedEntityData.defineId(FrostBlast.class, EntityDataSerializers.INT);

    public FrostBlast(EntityType<? extends FrostBlast> entityType, Level level) {
        super(entityType, level);
        pickup = Pickup.CREATIVE_ONLY;
    }

    public FrostBlast(Level level, LivingEntity owner, ItemStack pickupItemStack, ItemStack weapon) {
        super(FlingshotEntities.FROST_BLAST.get(), level, owner, pickupItemStack, weapon);
        this.pickup = Pickup.DISALLOWED;
    }

    private final double baseDamage = 6;
    private final double baseDamageMultiplier = 2;

    @Override
    protected @NotNull SoundEvent getDefaultHitGroundSoundEvent() {
        return SoundEvents.TRIDENT_HIT;
    }

    @Override
    protected void onHitEntity(EntityHitResult result) {
        Entity entityHit = result.getEntity();
        DamageSource source = damageSources().explosion(this, getOwner());
        if (!level().isClientSide()) {
            entityHit.hurtServer((ServerLevel) level(), source, (float) getBaseDamage());
        }
        if (entityHit instanceof LivingEntity living) {
            living.setTicksFrozen(living.getTicksFrozen() + getFreezeDuration());
        }
        explode(entityHit);
    }

    @Override
    protected void onHit(HitResult result) {
        super.onHit(result);
        if (result.getType().equals(HitResult.Type.BLOCK)) {
            explode(null);
        }
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
                level().addParticle(ParticleTypes.SNOWFLAKE,
                        center.x + vel.x * i / 4.0,
                        center.y + vel.y * i / 8.0,
                        center.z + vel.z * i / 4.0,
                        -vel.x / 2, (-vel.y + 0.2) / 2, -vel.z / 2);
            }
        }
    }

    @Override
    protected @NotNull ItemStack getDefaultPickupItem() {
        return new ItemStack(FlingshotItems.FROST_BLAST.get());
    }

    @Override
    protected void defineSynchedData(SynchedEntityData.Builder builder) {
        super.defineSynchedData(builder);
        builder.define(FREEZE_DURATION, 500);
    }

    private void explode(@Nullable Entity hitEntity) {
        playSound(SoundEvents.GENERIC_EXPLODE.value(), 0.25f, 2.0f);
        playSound(SoundEvents.GLASS_BREAK, 0.75f, 1.5f);
        playSound(SoundEvents.SNOW_BREAK, 1.0f, 0.75f);

        List<Entity> entities = level().getEntities(this, AABB.ofSize(getEyePosition(), 4, 4, 4),
                entity -> entity instanceof LivingEntity livingEntity && !livingEntity.isDeadOrDying() && livingEntity.hasLineOfSight(this));
        for (Entity entity : entities) {
            boolean isHitEntity = entity.equals(hitEntity);
            if (isHitEntity) {
                continue;
            }

            double dmg = Math.max(getBaseDamage() / distanceTo(entity), (float) (getBaseDamage() / getEyePosition().distanceTo(entity.getEyePosition())));
            int freezeDuration = (int) Math.max(getFreezeDuration() / distanceTo(entity), (getFreezeDuration() / getEyePosition().distanceTo(entity.getEyePosition())));
            if (dmg > getBaseDamage()) {
                dmg = getBaseDamage();
            }
            if (freezeDuration > getFreezeDuration()) {
                freezeDuration = getFreezeDuration();
            }

            DamageSource source = damageSources().explosion(this, getOwner());
            entity.hurt(source, (float) dmg);
            System.out.println("Freeze duration for " + entity + " is " + freezeDuration);
            if (entity.getTicksFrozen() + freezeDuration <= MAX_FREEZE_TICKS) {
                entity.setTicksFrozen(freezeDuration + entity.getTicksFrozen());
            }
        }

        if (!level().isClientSide()) {
            ServerLevel serverLevel = (ServerLevel) level();
            serverLevel.sendParticles(ParticleTypes.SNOWFLAKE,
                    getX(), getY(), getZ(), 50, 0, 0, 0, 0.1);
//            DustColorTransitionOptions effect = new DustColorTransitionOptions(
//                    new Vector3f(201 / 255f, 94 / 255f, 1),
//                    new Vector3f(0.8f, 0.8f, 0.8f), 1.5f);
//            serverLevel.sendParticles(effect,
//                    getX(), getY() + 0.5, getZ(), 25, 0.5, 0.5, 0.5, 0);
            serverLevel.sendParticles(ParticleTypes.POOF,
                    getX(), getY(), getZ(), 25, 0, 0, 0, 0.1);
            discard();
        }
    }

    public int getFreezeDuration() {
        return entityData.get(FREEZE_DURATION);
    }

    private double getBaseDamage() {
        return baseDamage + random.nextDouble() * baseDamageMultiplier;
    }
}
