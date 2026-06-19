package com.tomrom.flingshot.item.flingable;

import com.tomrom.flingshot.entity.ShimmerShell;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;

import java.util.List;

public class ShimmerShellItem extends Item implements Flingable<ShimmerShell> {

    public ShimmerShellItem(Properties properties) {
        super(properties);
    }

    @Override
    public ShimmerShell flingshot$getFlingableEntity(Level level, LivingEntity owner, ItemStack projectileStack, ItemStack weapon) {
        return new ShimmerShell(level, owner, projectileStack.copyWithCount(1), weapon);
    }

    @Override
    public void appendHoverText(ItemStack stack, TooltipContext context, List<Component> tooltip, TooltipFlag flag) {
        FlingableTooltips.add(tooltip::add, "shimmer_shell", "damage", "explodes");
    }
}
