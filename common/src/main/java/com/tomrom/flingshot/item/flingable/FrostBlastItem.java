package com.tomrom.flingshot.item.flingable;

import com.tomrom.flingshot.entity.FrostBlast;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;

import java.util.List;

public class FrostBlastItem extends Item implements Flingable<FrostBlast> {

    public FrostBlastItem(Properties properties) {
        super(properties);
    }

    @Override
    public FrostBlast flingshot$getFlingableEntity(Level level, LivingEntity owner, ItemStack projectileStack, ItemStack weapon) {
        return new FrostBlast(level, owner, projectileStack.copyWithCount(1), weapon);
    }

    @Override
    public void appendHoverText(ItemStack stack, TooltipContext context, List<Component> tooltip, TooltipFlag flag) {
        FlingableTooltips.add(tooltip::add, "frost_blast", "damage", "freezes");
    }
}
