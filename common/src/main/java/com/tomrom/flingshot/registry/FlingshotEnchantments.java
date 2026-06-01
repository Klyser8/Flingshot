package com.tomrom.flingshot.registry;

import com.tomrom.flingshot.FlingshotConstants;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentHelper;

public class FlingshotEnchantments {

    public static final ResourceKey<Enchantment> FORCE = create("force");
    public static final ResourceKey<Enchantment> AGILITY = create("agility");
    public static final ResourceKey<Enchantment> PRECISION = create("precision");
    public static final ResourceKey<Enchantment> AUTOMATION = create("automation");
    public static final ResourceKey<Enchantment> VERSATILITY = create("versatility");

    private static ResourceKey<Enchantment> create(String name) {
        return ResourceKey.create(Registries.ENCHANTMENT, FlingshotConstants.id(name));
    }

    public static int getLevel(HolderLookup.Provider registries, ResourceKey<Enchantment> enchantment, ItemStack stack) {
        Holder<Enchantment> holder = registries.lookupOrThrow(Registries.ENCHANTMENT).getOrThrow(enchantment);
        return EnchantmentHelper.getItemEnchantmentLevel(holder, stack);
    }
}
