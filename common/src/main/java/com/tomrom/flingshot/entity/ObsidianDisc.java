package com.tomrom.flingshot.entity;

import com.tomrom.flingshot.registry.FlingshotAdvancementTriggers;
import com.tomrom.flingshot.registry.FlingshotEntities;
import com.tomrom.flingshot.registry.FlingshotItems;
import net.minecraft.core.Holder;
import net.minecraft.core.particles.ItemParticleOption;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.core.registries.Registries;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;

public class ObsidianDisc extends AbstractBuck {

    public ObsidianDisc(EntityType<? extends ObsidianDisc> entityType, Level level) {
        super(entityType, level);
    }

    public ObsidianDisc(Level level, LivingEntity owner, ItemStack pickupItemStack, ItemStack weapon) {
        super(FlingshotEntities.OBSIDIAN_DISC.get(), level, owner, pickupItemStack, weapon);
    }

    @Override
    protected void onHitEntity(EntityHitResult hitResult) {
        Entity hitEntity = hitResult.getEntity();
        Entity owner = getOwner();
        if (level() instanceof ServerLevel serverLevel) {
            if (hitEntity instanceof LivingEntity living) {
                ItemStack useItem = living.getUseItem();
                if (useItem.is(Items.SHIELD) && living.isBlocking()) {
                    living.stopUsingItem();
                    level().playSound(null, living.getX(), living.getY(), living.getZ(), SoundEvents.SHIELD_BLOCK, living.getSoundSource(), 1.0f, 1.0f);
                    level().playSound(null, living.getX(), living.getY(), living.getZ(), SoundEvents.SHIELD_BREAK, living.getSoundSource(), 1.0f, 1.0f);
                    if (living instanceof Player player) {
                        player.getCooldowns().addCooldown(useItem, 100);
                    }
                    if (owner instanceof ServerPlayer player) {
                        FlingshotAdvancementTriggers.BREAK_SHIELD_WITH_OBSIDIAN_DISC.get().trigger(player);
                    }
                    return;
                }
            }
            DamageSource source = damageSources().mobProjectile(this, owner instanceof LivingEntity livingOwner ? livingOwner : null);
            double damage = (int) getFinalDamage(6.0 + getRandom().nextDouble() * 2.0);
            // Deals extra damage to entities with armor or armor toughness
            if (hitEntity instanceof LivingEntity living) {
                AttributeInstance armorAttribute = living.getAttribute(Attributes.ARMOR);
                double armorValue = armorAttribute == null ? 0 : armorAttribute.getValue();

                AttributeInstance toughnessAttribute = living.getAttribute(Attributes.ARMOR_TOUGHNESS);
                double toughnessValue = toughnessAttribute == null ? 0 : toughnessAttribute.getValue();
                // Calculate total level of protection enchantments
                int totalProtection = getTotalProtectionLevel(living);
                damage *=  1 + (1.0 * Math.min(armorValue / 20.0, 1) * Math.min(toughnessValue / 12.0, 1)) + 1 * Math.min(totalProtection / 16.0, 1);
            }
            hitEntity.hurtServer(serverLevel, source, (float) getFinalDamage(damage));
        }

        breakDisc();
    }

    @Override
    protected void onHit(HitResult hitResult) {
        super.onHit(hitResult);
        if (hitResult.getType() == HitResult.Type.BLOCK) {
            breakDisc();
        }
    }

    @Override
    protected void playParticleTrail() {
    }

    @Override
    protected SoundEvent getDefaultHitGroundSoundEvent() {
        return SoundEvents.COPPER_GRATE_BREAK;
    }

    @Override
    protected ItemStack getDefaultPickupItem() {
        return new ItemStack(FlingshotItems.OBSIDIAN_DISC.get());
    }

    private int getTotalProtectionLevel(LivingEntity living) {
        Holder<Enchantment> protection = living.level()
                .registryAccess()
                .lookupOrThrow(Registries.ENCHANTMENT)
                .getOrThrow(Enchantments.PROTECTION);

        int total = 0;

        total += EnchantmentHelper.getItemEnchantmentLevel(protection, living.getItemBySlot(EquipmentSlot.HEAD));
        total += EnchantmentHelper.getItemEnchantmentLevel(protection, living.getItemBySlot(EquipmentSlot.CHEST));
        total += EnchantmentHelper.getItemEnchantmentLevel(protection, living.getItemBySlot(EquipmentSlot.LEGS));
        total += EnchantmentHelper.getItemEnchantmentLevel(protection, living.getItemBySlot(EquipmentSlot.FEET));

        return total; // Should be up to 16
    }

    private void breakDisc() {
        if (isRemoved()) {
            return;
        }

        playSound(SoundEvents.BASALT_BREAK, 0.75f, 0.75f);
        if (!(level() instanceof ServerLevel serverLevel)) {
            return;
        }

        Vec3 pos = position();
        ItemParticleOption itemParticle = new ItemParticleOption(ParticleTypes.ITEM, FlingshotItems.OBSIDIAN_DISC.get());
        serverLevel.sendParticles(itemParticle, pos.x, pos.y, pos.z, 25, 0.0, 0.0, 0.0, 0.1);
        discard();
    }
}
