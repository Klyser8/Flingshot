package com.tomrom.flingshot.entity;

import com.tomrom.flingshot.FlingshotConstants;
import com.tomrom.flingshot.block.GlimmerGooSplatBlock;
import com.tomrom.flingshot.platform.Services;
import com.tomrom.flingshot.registry.FlingshotAdvancementTriggers;
import com.tomrom.flingshot.registry.FlingshotBlocks;
import com.tomrom.flingshot.registry.FlingshotEntities;
import com.tomrom.flingshot.registry.FlingshotItems;
import com.tomrom.flingshot.registry.FlingshotParticles;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;

public class GlimmerGoo extends AbstractBuck {

    private static final int GLOW_DURATION_TICKS = 200;

    public GlimmerGoo(EntityType<? extends GlimmerGoo> entityType, Level level) {
        super(entityType, level);
    }

    public GlimmerGoo(Level level, LivingEntity owner, ItemStack pickupItemStack, ItemStack weapon) {
        super(FlingshotEntities.GLIMMER_GOO.get(), level, owner, pickupItemStack, weapon);
    }

    @Override
    protected void onHitEntity(EntityHitResult hitResult) {
        Entity hitEntity = hitResult.getEntity();
        if (level() instanceof ServerLevel) {
            hitEntity.hurt(buckProjectileDamageSource(), (float) getFinalDamage(2.0 + getRandom().nextDouble() * 2.0));
        }

        if (hitEntity instanceof LivingEntity living) {
            living.addEffect(new MobEffectInstance(MobEffects.GLOWING, GLOW_DURATION_TICKS));
        }
        splash();
    }

    @Override
    protected void onHit(HitResult hitResult) {
        super.onHit(hitResult);
        if (!(hitResult instanceof BlockHitResult blockHitResult)) {
            return;
        }
        if (hitResult.getType() != HitResult.Type.BLOCK) { // Guess in 1.21 I have to check whether the hit result is a block hit, or else air counts as a hit too? huh
            return;
        }
        placeSplat(blockHitResult);
    }

    @Override
    protected void playParticleTrail() {
        if (!level().isClientSide() || getCollisionAge() > 0) {
            return;
        }

        Vec3 velocity = getDeltaMovement().normalize();
        Vec3 center = getBoundingBox().getCenter();
        level().addParticle(
                FlingshotParticles.GREASE_CHUNK.get(),
                center.x - velocity.x * 0.35,
                center.y + 0.1 - velocity.y * 0.35,
                center.z - velocity.z * 0.35,
                0.0,
                0.0,
                0.0
        );
    }

    @Override
    protected SoundEvent getDefaultHitGroundSoundEvent() {
        return SoundEvents.SLIME_BLOCK_HIT;
    }

    @Override
    protected ItemStack getDefaultPickupItem() {
        return new ItemStack(FlingshotItems.GLIMMER_GOO.get());
    }

    private void splash() {
        if (isRemoved()) {
            return;
        }

        playSound(SoundEvents.SLIME_BLOCK_BREAK, 0.8f, 1.35f);
        if (level() instanceof ServerLevel serverLevel) {
            serverLevel.sendParticles(FlingshotParticles.GREASE_CHUNK.get(), getX(), getY(), getZ(), 18, 0.08, 0.08, 0.08, 0.08);
            discard();
        }
    }

    private void placeSplat(BlockHitResult hitResult) {
        if (isRemoved() || isInLava()) {
            return;
        }

        Level level = level();
        if (!(level instanceof ServerLevel serverLevel)) {
            return;
        }

        BlockPos placePos = hitResult.getBlockPos().relative(hitResult.getDirection());
        BlockState oldPlaceState = level.getBlockState(placePos);
        GlimmerGooSplatBlock splatBlock = FlingshotBlocks.GLIMMER_GOO_SPLAT.get();
        BlockState splatState = splatBlock.withFace(oldPlaceState, level, placePos, hitResult.getDirection().getOpposite());

        discard();
        boolean canReplace = oldPlaceState.is(splatBlock) || oldPlaceState.canBeReplaced() || oldPlaceState.getFluidState().isSourceOfType(Fluids.WATER);
        if (splatState == null || !canReplace) {
            if (Services.PLATFORM.isDevelopmentEnvironment()) {
                FlingshotConstants.LOG.warn(
                        "Glimmer goo failed to place splat: hitBlock={}, hitDirection={}, placePos={}, oldPlaceState={}, splatState={}, canReplace={}, tickCount={}, owner={}",
                        hitResult.getBlockPos(),
                        hitResult.getDirection(),
                        placePos,
                        oldPlaceState,
                        splatState,
                        canReplace,
                        tickCount,
                        getOwner()
                );
            }
            ItemEntity drop = new ItemEntity(level, getX(), getY(), getZ(), getDefaultPickupItem());
            drop.setPickUpDelay(20);
            serverLevel.addFreshEntity(drop);
            return;
        }

        level.setBlock(placePos, splatState, Block.UPDATE_ALL);
        if (hitResult.getDirection() == Direction.DOWN
                && getOwner() instanceof ServerPlayer player
                && placePos.getY() - player.blockPosition().getY() >= 10) {
            FlingshotAdvancementTriggers.CEILING_GLIMMER_GOO.get().trigger(player);
        }
        playSound(SoundEvents.HONEY_BLOCK_PLACE, 1.0f, 1.5f);
        serverLevel.sendParticles(FlingshotParticles.GREASE_CHUNK.get(), getX(), getY(), getZ(), 40, 0.0, 0.0, 0.0, 0.1);
    }
}
