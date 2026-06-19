package com.tomrom.flingshot.mixin.client;

import com.tomrom.flingshot.item.FlingshotItem;
import com.tomrom.flingshot.registry.FlingshotEnchantments;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.Constant;
import org.spongepowered.asm.mixin.injection.ModifyConstant;

@Mixin(LocalPlayer.class)
public abstract class LocalPlayerUseSpeedMixin {

    @ModifyConstant(method = "aiStep", constant = @Constant(floatValue = 0.2F))
    private float flingshot$applyAgilityUseSpeed(float vanillaMultiplier) {
        LocalPlayer player = (LocalPlayer) (Object) this;
        ItemStack stack = player.getUseItem();
        if (!(stack.getItem() instanceof FlingshotItem)) {
            return vanillaMultiplier;
        }

        int agilityLevel = FlingshotEnchantments.getLevel(player.registryAccess(), FlingshotEnchantments.AGILITY, stack);
        if (agilityLevel <= 0) {
            return vanillaMultiplier;
        }

        float restoredSlowdown = vanillaMultiplier + (1.0F - vanillaMultiplier) * (0.25F * agilityLevel);
        return restoredSlowdown > 1.0F ? 1.0F : restoredSlowdown;
    }
}
