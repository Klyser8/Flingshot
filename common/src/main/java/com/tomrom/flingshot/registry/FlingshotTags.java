package com.tomrom.flingshot.registry;

import com.tomrom.flingshot.FlingshotConstants;
import net.minecraft.core.registries.Registries;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;

public class FlingshotTags {

    public static final TagKey<Item> FLINGSHOTS = TagKey.create(Registries.ITEM, FlingshotConstants.id("flingshots"));
    public static final TagKey<Item> FLINGABLE = TagKey.create(Registries.ITEM, FlingshotConstants.id("flingable"));
}
