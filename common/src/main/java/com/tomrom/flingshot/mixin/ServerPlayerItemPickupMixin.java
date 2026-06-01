package com.tomrom.flingshot.mixin;

import com.tomrom.flingshot.item.FlingshotItem;
import com.tomrom.flingshot.registry.FlingshotAdvancementTriggers;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.item.ItemEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ServerPlayer.class)
public abstract class ServerPlayerItemPickupMixin {

    @Inject(method = "onItemPickup", at = @At("TAIL"))
    private void flingshot$triggerFlungItemPickup(ItemEntity entity, CallbackInfo ci) {
        if (entity.entityTags().contains(FlingshotItem.FLUNG_ITEM_TAG)) {
            FlingshotAdvancementTriggers.FLUNG_ITEM_PICKED_UP.get().trigger((ServerPlayer) (Object) this);
        }
    }
}
