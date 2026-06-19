package com.tomrom.flingshot.entity;

import com.tomrom.flingshot.config.FlingshotConfig;
import com.tomrom.flingshot.registry.FlingshotDamageTypes;
import com.tomrom.flingshot.registry.FlingshotEntities;
import com.tomrom.flingshot.registry.FlingshotItems;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;

public class CopperBuck extends AbstractBuck {

    public CopperBuck(EntityType<? extends CopperBuck> entityType, Level level) {
        super(entityType, level);
    }

    public CopperBuck(Level level, LivingEntity owner, ItemStack pickupItemStack, ItemStack weapon) {
        super(FlingshotEntities.COPPER_BUCK.get(), level, owner, pickupItemStack, weapon);
    }

    @Override
    protected void onHitEntity(EntityHitResult hitResult) {
        Entity hitEntity = hitResult.getEntity();
        if (level() instanceof ServerLevel) {
            hitEntity.hurt(buckDamageSource(FlingshotDamageTypes.COPPER_BUCK), (float) getFinalDamage(7.0 + getRandom().nextDouble() * 3.0));
        }
        setDeltaMovement(getDeltaMovement().reverse().scale(0.1));
        pickup = Pickup.CREATIVE_ONLY;
    }

    @Override
    protected void onHit(HitResult hitResult) {
        super.onHit(hitResult);
        if (hitResult.getType() != HitResult.Type.ENTITY && pickup == Pickup.DISALLOWED) {
            pickup = FlingshotConfig.recoverableCopperBuckPickup() ? Pickup.ALLOWED : Pickup.CREATIVE_ONLY;
        }
    }

    @Override
    public void tick() {
        super.tick();
        if (inGroundTime > 40 && pickup == Pickup.CREATIVE_ONLY) {
            discard();
        }
    }

    @Override
    protected void playParticleTrail() {
        if (!level().isClientSide() || inGroundTime > 0) {
            return;
        }

        Vec3 velocity = getDeltaMovement().normalize();
        for (int i = 0; i < 3; ++i) {
            level().addParticle(
                    ParticleTypes.CRIT,
                    getX(0.5) - velocity.x + velocity.x * i / 4.0,
                    getRandomY() / 2 + getRandomY() / 2 - velocity.y + velocity.y * i / 8.0,
                    getZ(0.5) - velocity.z + velocity.z * i / 4.0,
                    -velocity.x / 2,
                    (-velocity.y + 0.2) / 2,
                    -velocity.z / 2
            );
        }
    }

    @Override
    protected SoundEvent getDefaultHitGroundSoundEvent() {
        return SoundEvents.TRIDENT_HIT;
    }

    @Override
    protected ItemStack getDefaultPickupItem() {
        return new ItemStack(FlingshotItems.COPPER_BUCK.get());
    }
}
