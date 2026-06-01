package com.tomrom.flingshot.mixin;

import com.tomrom.flingshot.entity.FireCharge;
import com.tomrom.flingshot.item.flingable.Flingable;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.FireChargeItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(FireChargeItem.class)
public class FireChargeItemMixin implements Flingable<FireCharge> {

    @Override
    public FireCharge flingshot$getFlingableEntity(Level level, LivingEntity owner, ItemStack projectileStack, ItemStack weapon) {
        return new FireCharge(level, owner, projectileStack.copyWithCount(1), weapon);
    }
}
