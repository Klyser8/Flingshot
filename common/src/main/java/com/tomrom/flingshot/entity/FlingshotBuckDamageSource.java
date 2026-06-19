package com.tomrom.flingshot.entity;

import com.tomrom.flingshot.registry.FlingshotItems;
import net.minecraft.core.Holder;
import net.minecraft.network.chat.Component;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.DamageType;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;

public class FlingshotBuckDamageSource extends DamageSource {

    private final ItemStack weapon;
    private final ItemStack projectile;

    public FlingshotBuckDamageSource(Holder<DamageType> type, Entity directEntity, Entity causingEntity, ItemStack weapon, ItemStack projectile) {
        super(type, directEntity, causingEntity);
        this.weapon = weapon.isEmpty() ? new ItemStack(FlingshotItems.FLINGSHOT.get()) : weapon.copy();
        this.projectile = projectile.copy();
    }

    @Override
    public ItemStack getWeaponItem() {
        return weapon;
    }

    @Override
    public Component getLocalizedDeathMessage(LivingEntity victim) {
        Entity attacker = getEntity();
        Component weaponName = weapon.getDisplayName();
        Component projectileName = projectile.getDisplayName();

        if (attacker == null || attacker == this.getDirectEntity()) {
            return Component.translatable("death.attack.flingshot.buck", victim.getDisplayName(), weaponName, projectileName);
        }

        return Component.translatable("death.attack.flingshot.buck.player", victim.getDisplayName(), attacker.getDisplayName(), weaponName, projectileName);
    }
}
