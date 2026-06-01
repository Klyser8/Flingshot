package com.tomrom.flingshot.item.flingable;

import com.tomrom.flingshot.entity.CopperBuck;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.component.TooltipDisplay;
import net.minecraft.world.level.Level;

import java.util.function.Consumer;

public class CopperBuckItem extends Item implements Flingable<CopperBuck> {

    public CopperBuckItem(Properties properties) {
        super(properties);
    }

    @Override
    public CopperBuck flingshot$getFlingableEntity(Level level, LivingEntity owner, ItemStack projectileStack, ItemStack weapon) {
        return new CopperBuck(level, owner, projectileStack.copyWithCount(1), weapon);
    }

    @Override
    public void appendHoverText(ItemStack stack, TooltipContext context, TooltipDisplay display, Consumer<Component> tooltip, TooltipFlag flag) {
        FlingableTooltips.add(tooltip, "copper_buck", "damage");
    }
}
