package com.tomrom.flingshot.registry;

import com.tomrom.flingshot.FlingshotConstants;
import com.tomrom.flingshot.platform.CommonPlatformHelper;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.EnchantedBookItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentInstance;

import java.util.List;
import java.util.function.Supplier;

public class FlingshotCreativeTabs {

    public static final ResourceKey<CreativeModeTab> ITEMS_KEY = ResourceKey.create(Registries.CREATIVE_MODE_TAB, FlingshotConstants.id("items"));

    public static final Supplier<CreativeModeTab> ITEMS = CommonPlatformHelper.registerCreativeTab(
            "items",
            () -> CreativeModeTab.builder(CreativeModeTab.Row.TOP, 0)
                    .title(Component.translatable("itemGroup." + FlingshotConstants.MOD_ID + ".items"))
                    .icon(() -> FlingshotItems.FLINGSHOT.get().getDefaultInstance())
                    .displayItems((parameters, output) -> displayItems(parameters, output))
                    .build()
    );

    public static void displayItems(CreativeModeTab.ItemDisplayParameters parameters, CreativeModeTab.Output output) {
        List.of(
                FlingshotItems.FLINGSHOT.get(),
                FlingshotItems.COPPER_BUCK.get(),
                FlingshotItems.FROST_BLAST.get(),
                FlingshotItems.OBSIDIAN_DISC.get(),
                Items.FIRE_CHARGE,
                FlingshotItems.GLIMMER_GOO.get(),
                FlingshotItems.SHIMMER_SHELL.get()
        ).forEach(output::accept);

        addEnchantedBook(parameters, output, FlingshotEnchantments.PRECISION, 5);
        addEnchantedBook(parameters, output, FlingshotEnchantments.FORCE, 3);
        addEnchantedBook(parameters, output, FlingshotEnchantments.AGILITY, 3);
        addEnchantedBook(parameters, output, FlingshotEnchantments.AUTOMATION, 1);
        addEnchantedBook(parameters, output, FlingshotEnchantments.VERSATILITY, 1);
    }

    private static void addEnchantedBook(CreativeModeTab.ItemDisplayParameters parameters, CreativeModeTab.Output output, ResourceKey<Enchantment> enchantment, int level) {
        Holder<Enchantment> holder = parameters.holders().lookupOrThrow(Registries.ENCHANTMENT).getOrThrow(enchantment);
        ItemStack book = EnchantedBookItem.createForEnchantment(new EnchantmentInstance(holder, level));
        output.accept(book);
    }

    public static void init() {
    }
}
