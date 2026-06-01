package com.tomrom.flingshot.mixin.client;

import com.tomrom.flingshot.item.FlingshotItem;
import com.tomrom.flingshot.registry.FlingshotEnchantments;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(LocalPlayer.class)
public abstract class LocalPlayerUseSpeedMixin {

    @Inject(method = "itemUseSpeedMultiplier", at = @At("RETURN"), cancellable = true)
    private void flingshot$applyAgilityUseSpeed(CallbackInfoReturnable<Float> cir) {
        LocalPlayer player = (LocalPlayer) (Object) this;
        ItemStack stack = player.getUseItem();
        if (!(stack.getItem() instanceof FlingshotItem)) {
            return;
        }

        int agilityLevel = FlingshotEnchantments.getLevel(player.registryAccess(), FlingshotEnchantments.AGILITY, stack);
        if (agilityLevel <= 0) {
            return;
        }

        float vanillaMultiplier = cir.getReturnValueF();
        float restoredSlowdown = vanillaMultiplier + (1.0F - vanillaMultiplier) * (0.25F * agilityLevel);
        cir.setReturnValue(Math.min(1.0F, restoredSlowdown));
    }
}
