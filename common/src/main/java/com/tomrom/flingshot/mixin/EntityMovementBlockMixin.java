package com.tomrom.flingshot.mixin;

import com.tomrom.flingshot.block.GlimmerGooSplatBlock;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Entity.class)
public abstract class EntityMovementBlockMixin {

    @Shadow
    public abstract Level level();

    @Unique
    private long flingshot$lastGreasePopTick = Long.MIN_VALUE;

    @Inject(method = "tick", at = @At("TAIL"))
    private void flingshot$spawnGreasePopParticles(CallbackInfo ci) {
        Entity entity = (Entity) (Object) this;
        if (GlimmerGooSplatBlock.findFloorSplat(level(), entity) != null) {
            flingshot$spawnGreasePopOncePerTick(entity);
        }
    }

    @Inject(method = "getBlockPosBelowThatAffectsMyMovement", at = @At("RETURN"), cancellable = true)
    private void flingshot$useGlimmerGooAsMovementBlock(CallbackInfoReturnable<BlockPos> cir) {
        if ((Object) this instanceof ItemEntity) {
            return;
        }

        BlockPos glimmerGooPos = GlimmerGooSplatBlock.findFloorSplat(level(), (Entity) (Object) this);
        if (glimmerGooPos != null) {
            cir.setReturnValue(glimmerGooPos);
        }
    }

    @Unique
    private void flingshot$spawnGreasePopOncePerTick(Entity entity) {
        long gameTime = level().getGameTime();
        if (flingshot$lastGreasePopTick == gameTime) {
            return;
        }

        flingshot$lastGreasePopTick = gameTime;
        GlimmerGooSplatBlock.spawnStepParticles(level(), entity);
    }
}
