package com.tomrom.flingshot.mixin;

import com.tomrom.flingshot.registry.FlingshotBlocks;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Entity.class)
public abstract class EntityMovementBlockMixin {

    @Shadow
    public abstract Level level();

    @Shadow
    public abstract BlockPos blockPosition();

    @Inject(method = "getBlockPosBelowThatAffectsMyMovement", at = @At("HEAD"), cancellable = true)
    private void flingshot$useGlimmerGooAsMovementBlock(CallbackInfoReturnable<BlockPos> cir) {
        BlockPos pos = blockPosition();
        if (level().getBlockState(pos).is(FlingshotBlocks.GLIMMER_GOO_SPLAT.get())) {
            cir.setReturnValue(pos);
        }
    }
}
