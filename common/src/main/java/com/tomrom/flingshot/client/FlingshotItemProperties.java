package com.tomrom.flingshot.client;

import com.tomrom.flingshot.FlingshotConstants;
import com.tomrom.flingshot.item.FlingshotItem;
import com.tomrom.flingshot.registry.FlingshotItems;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.renderer.item.ItemProperties;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;

public final class FlingshotItemProperties {

    private FlingshotItemProperties() {
    }

    public static void register() {
        ItemProperties.register(FlingshotItems.FLINGSHOT.get(), FlingshotConstants.id("pull"), FlingshotItemProperties::pull);
    }

    private static float pull(ItemStack stack, ClientLevel level, LivingEntity entity, int seed) {
        if (entity == null || entity.getUseItem() != stack) {
            return 0.0f;
        }

        int useTicks = stack.getUseDuration(entity) - entity.getUseItemRemainingTicks();
        return Math.min(1.0f, (float) useTicks / FlingshotItem.CHARGE_TIME);
    }
}
