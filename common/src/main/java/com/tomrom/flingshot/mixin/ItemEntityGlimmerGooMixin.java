package com.tomrom.flingshot.mixin;

import com.tomrom.flingshot.block.GlimmerGooSplatBlock;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.phys.Vec3;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ItemEntity.class)
public class ItemEntityGlimmerGooMixin {

    @Inject(method = "tick", at = @At("TAIL"))
    private void flingshot$slideOnGlimmerGoo(CallbackInfo ci) {
        Entity entity = (Entity) (Object) this;
        if (!entity.onGround() || GlimmerGooSplatBlock.findFloorSplat(entity.level(), entity) == null) {
            return;
        }

        Vec3 movement = entity.getDeltaMovement();
        if (movement.horizontalDistanceSqr() < 1.0E-6) {
            return;
        }

        entity.setDeltaMovement(movement.x * 1.65, movement.y, movement.z * 1.65);
    }
}
