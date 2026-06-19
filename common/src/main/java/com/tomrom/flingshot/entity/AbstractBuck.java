package com.tomrom.flingshot.entity;

import com.tomrom.flingshot.registry.FlingshotItems;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.DamageType;
import net.minecraft.world.damagesource.DamageTypes;
import net.minecraft.world.entity.Entity;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.HitResult;

public abstract class AbstractBuck extends AbstractArrow {

    private static final EntityDataAccessor<Integer> COLLISION_AGE = SynchedEntityData.defineId(AbstractBuck.class, EntityDataSerializers.INT);

    private float pullFactor = 1.0f;

    protected AbstractBuck(EntityType<? extends AbstractBuck> entityType, Level level) {
        super(entityType, level);
    }

    protected AbstractBuck(EntityType<? extends AbstractBuck> entityType, Level level, LivingEntity owner, ItemStack pickupItemStack, ItemStack weapon) {
        super(entityType, owner, level, pickupItemStack, weapon);
        this.pickup = Pickup.DISALLOWED;
    }

    public void setPullFactor(float pullFactor) {
        this.pullFactor = pullFactor;
    }

    public double getFinalDamage(double baseDamage) {
        return baseDamage * pullFactor;
    }

    protected DamageSource buckProjectileDamageSource() {
        return buckDamageSource(DamageTypes.MOB_PROJECTILE);
    }

    protected DamageSource buckExplosionDamageSource() {
        return buckDamageSource(getOwner() instanceof Player ? DamageTypes.PLAYER_EXPLOSION : DamageTypes.EXPLOSION);
    }

    protected DamageSource buckDamageSource(ResourceKey<DamageType> type) {
        Entity owner = getOwner();
        return new FlingshotBuckDamageSource(
                registryAccess().lookupOrThrow(Registries.DAMAGE_TYPE).getOrThrow(type),
                this,
                owner == null ? this : owner,
                getDisplayWeaponItem(),
                getDisplayProjectileItem()
        );
    }

    private ItemStack getDisplayWeaponItem() {
        ItemStack weapon = getWeaponItem();
        return weapon == null || weapon.isEmpty() ? new ItemStack(FlingshotItems.FLINGSHOT.get()) : weapon;
    }

    private ItemStack getDisplayProjectileItem() {
        ItemStack projectile = getPickupItemStackOrigin();
        return projectile.isEmpty() ? getDefaultPickupItem() : projectile;
    }

    public int getCollisionAge() {
        return entityData.get(COLLISION_AGE);
    }

    protected abstract void playParticleTrail();

    @Override
    protected void defineSynchedData(SynchedEntityData.Builder builder) {
        super.defineSynchedData(builder);
        builder.define(COLLISION_AGE, 0);
    }

    @Override
    public void tick() {
        playParticleTrail();
        super.tick();

        if (getCollisionAge() == 0 && inGroundTime > 0) {
            setCollisionAge(1);
        }

        if (getCollisionAge() > 0) {
            setCollisionAge(getCollisionAge() + 1);
        }
    }

    @Override
    protected void onHit(HitResult hitResult) {
        super.onHit(hitResult);
        if (hitResult.getType() != HitResult.Type.MISS && getCollisionAge() == 0) {
            setCollisionAge(1);
        }
    }

    private void setCollisionAge(int collisionAge) {
        entityData.set(COLLISION_AGE, collisionAge);
    }
}
