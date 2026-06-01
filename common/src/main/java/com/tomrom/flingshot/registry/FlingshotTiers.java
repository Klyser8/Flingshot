package com.tomrom.flingshot.registry;

import net.minecraft.tags.BlockTags;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.item.ToolMaterial;

public class FlingshotTiers {

    public static final ToolMaterial COPPER = new ToolMaterial(
            BlockTags.INCORRECT_FOR_COPPER_TOOL,
            192,
            3.0f,
            1.5f,
            15,
            ItemTags.COPPER_TOOL_MATERIALS
    );
}
