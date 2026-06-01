package com.tomrom.flingshot.item.flingable;

import com.tomrom.flingshot.FlingshotConstants;
import com.tomrom.flingshot.config.FlingshotConfig;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;

import java.util.function.Consumer;

public class FlingableTooltips {

    public static void add(Consumer<Component> tooltip, String itemName, String... detailLines) {
        if (!FlingshotConfig.enableAmmoTooltips()) {
            return;
        }

        tooltip.accept(Component.translatable("tooltip." + FlingshotConstants.MOD_ID + ".used_with_flingshot").withStyle(ChatFormatting.GRAY));
        for (String detailLine : detailLines) {
            String value = highlightedValue(itemName, detailLine);
            MutableComponent line = value == null
                    ? Component.translatable("tooltip." + FlingshotConstants.MOD_ID + "." + itemName + "." + detailLine)
                    : Component.translatable(
                            "tooltip." + FlingshotConstants.MOD_ID + "." + itemName + "." + detailLine,
                            Component.literal(value).withStyle(ChatFormatting.WHITE)
                    );
            tooltip.accept(line.withStyle(ChatFormatting.DARK_GRAY));
        }
    }

    private static String highlightedValue(String itemName, String detailLine) {
        return switch (itemName + "." + detailLine) {
            case "copper_buck.damage" -> "7-10";
            case "shimmer_shell.damage" -> "16";
            case "glimmer_goo.damage" -> "2-4";
            case "frost_blast.damage" -> "8";
            case "obsidian_disc.damage" -> "6-8";
            case "obsidian_disc.armor_damage" -> "3x";
            case "fire_charge.damage" -> "4-6";
            case "fire_charge.sets_target_ablaze" -> "15";
            default -> null;
        };
    }
}
