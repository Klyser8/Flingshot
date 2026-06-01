package com.tomrom.flingshot.item.flingable;

import com.tomrom.flingshot.entity.GlimmerGoo;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.component.TooltipDisplay;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.Level;

import java.util.function.Consumer;

public class GlimmerGooItem extends BlockItem implements Flingable<GlimmerGoo> {

    public GlimmerGooItem(Block block, Properties properties) {
        super(block, properties);
    }

    @Override
    public GlimmerGoo flingshot$getFlingableEntity(Level level, LivingEntity owner, ItemStack projectileStack, ItemStack weapon) {
        return new GlimmerGoo(level, owner, projectileStack.copyWithCount(1), weapon);
    }

    @Override
    public void appendHoverText(ItemStack stack, TooltipContext context, TooltipDisplay display, Consumer<Component> tooltip, TooltipFlag flag) {
        FlingableTooltips.add(tooltip, "glimmer_goo", "damage", "places", "glows");
    }
}
