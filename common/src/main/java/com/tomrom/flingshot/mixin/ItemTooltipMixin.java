package com.tomrom.flingshot.mixin;

import com.tomrom.flingshot.item.flingable.FlingableTooltips;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.component.TooltipDisplay;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.function.Consumer;

@Mixin(Item.class)
public class ItemTooltipMixin {

    @Inject(method = "appendHoverText", at = @At("TAIL"))
    private void flingshot$appendFireChargeTooltip(ItemStack stack, Item.TooltipContext context, TooltipDisplay display, Consumer<Component> tooltip, TooltipFlag flag, CallbackInfo ci) {
        if (stack.is(Items.FIRE_CHARGE)) {
            FlingableTooltips.add(tooltip, "fire_charge", "damage", "sets_target_ablaze", "ignites_blocks");
        }
    }
}
