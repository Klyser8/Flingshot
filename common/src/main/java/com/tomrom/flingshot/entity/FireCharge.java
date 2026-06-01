package com.tomrom.flingshot.entity;

import com.tomrom.flingshot.registry.FlingshotAdvancementTriggers;
import com.tomrom.flingshot.registry.FlingshotEntities;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BaseFireBlock;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.Vec3;

public class FireCharge extends AbstractBuck {

    private static final int FIRE_TICKS = 300;

    public FireCharge(EntityType<? extends FireCharge> entityType, Level level) {
        super(entityType, level);
    }

    public FireCharge(Level level, LivingEntity owner, ItemStack pickupItemStack, ItemStack weapon) {
        super(FlingshotEntities.FIRE_CHARGE.get(), level, owner, pickupItemStack, weapon);
    }

    @Override
    protected void onHitEntity(EntityHitResult hitResult) {
        Entity hitEntity = hitResult.getEntity();
        Entity owner = getOwner();
        hitEntity.setRemainingFireTicks(Math.max(FIRE_TICKS, hitEntity.getRemainingFireTicks()));
        if (level() instanceof ServerLevel serverLevel) {
            DamageSource source = damageSources().mobProjectile(this, owner instanceof LivingEntity livingOwner ? livingOwner : null);
            hitEntity.hurtServer(serverLevel, source, (float) getFinalDamage(4.0 + getRandom().nextDouble() * 2.0));
        }
        discard();
    }

    @Override
    protected void onHitBlock(BlockHitResult hitResult) {
        super.onHitBlock(hitResult);
        if (level() instanceof ServerLevel serverLevel) {
            BlockPos firePos = hitResult.getBlockPos().relative(hitResult.getDirection());
            if (serverLevel.mayInteract(this, firePos) && BaseFireBlock.canBePlacedAt(serverLevel, firePos, hitResult.getDirection())) {
                serverLevel.setBlockAndUpdate(firePos, BaseFireBlock.getState(serverLevel, firePos));
                if (serverLevel.getBlockState(firePos).is(Blocks.NETHER_PORTAL) && getOwner() instanceof ServerPlayer player) {
                    FlingshotAdvancementTriggers.LIGHT_NETHER_PORTAL.get().trigger(player);
                }
            }
        }
        discard();
    }

    @Override
    protected void playParticleTrail() {
        if (!level().isClientSide() || getCollisionAge() > 0) {
            return;
        }

        Vec3 velocity = getDeltaMovement().normalize();
        Vec3 center = getBoundingBox().getCenter();
        for (int i = 0; i < 3; ++i) {
            Vec3 origin = new Vec3(
                    getX(0.5) - velocity.x,
                    getRandomY() / 2 + getRandomY() / 2 - velocity.y,
                    getZ(0.5) - velocity.z
            );
            level().addParticle(
                    ParticleTypes.SMALL_FLAME,
                    center.x + velocity.x * i / 4.0,
                    center.y + velocity.y * i / 8.0,
                    center.z + velocity.z * i / 4.0,
                    -velocity.x / 6.0,
                    (-velocity.y + 0.2) / 6.0,
                    -velocity.z / 6.0
            );
        }
    }

    @Override
    protected SoundEvent getDefaultHitGroundSoundEvent() {
        return SoundEvents.FIRECHARGE_USE;
    }

    @Override
    protected ItemStack getDefaultPickupItem() {
        return new ItemStack(Items.FIRE_CHARGE);
    }
}
