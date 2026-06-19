package com.tomrom.flingshot.mixin;

import com.tomrom.flingshot.item.flingable.FlingableTooltips;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.TooltipFlag;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.List;

@Mixin(Item.class)
public class ItemTooltipMixin {

    @Inject(method = "appendHoverText", at = @At("TAIL"))
    private void flingshot$appendFireChargeTooltip(ItemStack stack, Item.TooltipContext context, List<Component> tooltip, TooltipFlag flag, CallbackInfo ci) {
        if (stack.is(Items.FIRE_CHARGE)) {
            FlingableTooltips.add(tooltip::add, "fire_charge", "damage", "sets_target_ablaze", "ignites_blocks");
        }
    }
}
