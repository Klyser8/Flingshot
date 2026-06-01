package com.tomrom.flingshot.registry;

import com.tomrom.flingshot.item.FlingshotItem;
import com.tomrom.flingshot.item.flingable.CopperBuckItem;
import com.tomrom.flingshot.item.flingable.FrostBlastItem;
import com.tomrom.flingshot.item.flingable.GlimmerGooItem;
import com.tomrom.flingshot.item.flingable.ObsidianDiscItem;
import com.tomrom.flingshot.item.flingable.ShimmerShellItem;
import com.tomrom.flingshot.platform.CommonPlatformHelper;
import net.minecraft.world.item.Item;

import java.util.function.Supplier;

public class FlingshotItems {

    // Weapon introduced by mod, shoots whatever items are tagged as "flingable"
    public static final Supplier<Item> FLINGSHOT = CommonPlatformHelper.registerItem(
            "flingshot",
            properties -> new FlingshotItem(FlingshotTiers.COPPER, properties),
            properties -> properties.stacksTo(1)
                    .durability(FlingshotTiers.COPPER.durability())
                    .repairable(FlingshotTiers.COPPER.repairItems())
                    .enchantable(FlingshotTiers.COPPER.enchantmentValue())
    );

    // Basic buck that does 7-10 damage.
    public static final Supplier<Item> COPPER_BUCK = CommonPlatformHelper.registerItem("copper_buck", CopperBuckItem::new);

    // Buck which explodes instantly upon contact, dealing between 6-8 damage and freezing nearby entities.
    public static final Supplier<Item> FROST_BLAST = CommonPlatformHelper.registerItem("frost_blast", FrostBlastItem::new);

    // Buck which does 6-8 damage on hit, that breaks shields for five seconds, and does up to 3x damage against heavily armored entities
    public static final Supplier<Item> OBSIDIAN_DISC = CommonPlatformHelper.registerItem("obsidian_disc", ObsidianDiscItem::new);

    // Buck which does 2-4 damage on hit, and places a glimmer goo splat on the face of the block hit.
    public static final Supplier<Item> GLIMMER_GOO = CommonPlatformHelper.registerItem(
            "glimmer_goo",
            properties -> new GlimmerGooItem(FlingshotBlocks.GLIMMER_GOO_SPLAT.get(), properties)
    );
    
    // Premium buck that does up explodes on entity hit or after five seconds of being stuck in a black, dealing up to 16 damage.
    public static final Supplier<Item> SHIMMER_SHELL = CommonPlatformHelper.registerItem("shimmer_shell", ShimmerShellItem::new);

    public static void init() {
    }
}
