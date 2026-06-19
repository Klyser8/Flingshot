package com.tomrom.flingshot.item;

import com.tomrom.flingshot.FlingshotConstants;
import com.tomrom.flingshot.config.FlingshotConfig;
import com.tomrom.flingshot.entity.AbstractBuck;
import com.tomrom.flingshot.item.flingable.Flingable;
import com.tomrom.flingshot.platform.Services;
import com.tomrom.flingshot.registry.FlingshotAdvancementTriggers;
import com.tomrom.flingshot.registry.FlingshotEnchantments;
import com.tomrom.flingshot.registry.FlingshotItems;
import com.tomrom.flingshot.registry.FlingshotSoundEvents;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundSource;
import net.minecraft.stats.Stats;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.UseAnim;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import com.tomrom.flingshot.registry.FlingshotTiers.FlingshotTier;

import java.util.function.Predicate;

public class FlingshotItem extends Item {

    public static final Predicate<ItemStack> FLINGSHOT_PROJECTILES = stack -> stack.getItem() instanceof Flingable<?>;
    public static final int CHARGE_TIME = 15;
    public static final int MAX_CHARGE_TIME = 72000;
    public static final String FLUNG_ITEM_TAG = "flingshot.flung_item";

    private static final float MIN_PULL_TO_SHOOT = 0.90f;
    private static final float BASE_VELOCITY = 1.25f;
    private static final float INACCURACY = 5.0f;
    private static final double PROJECTILE_SPAWN_DISTANCE = 0.6;

    private final FlingshotTier material;

    public FlingshotItem(FlingshotTier material, Properties properties) {
        super(properties);
        this.material = material;
    }

    @Override
    public int getUseDuration(ItemStack itemStack, LivingEntity user) {
        if (hasAutomation(itemStack, user)) {
            return CHARGE_TIME;
        }
        return MAX_CHARGE_TIME;
    }

    @Override
    public void releaseUsing(ItemStack flingshot, Level level, LivingEntity user, int remainingUseTicks) {
        if (!hasAutomation(flingshot, user)) {
            handleFlingshotUse(flingshot, level, user, remainingUseTicks);
        }
    }

    @Override
    public ItemStack finishUsingItem(ItemStack flingshot, Level level, LivingEntity user) {
        if (hasAutomation(flingshot, user)) {
            handleFlingshotUse(flingshot, level, user, 0);
        }
        return flingshot;
    }

    private boolean handleFlingshotUse(ItemStack flingshot, Level level, LivingEntity user, int remainingUseTicks) {
        if (!(user instanceof Player player)) {
            return false;
        }

        ItemStack projectileStack = getHeldProjectile(flingshot, player);
        if (projectileStack.isEmpty()) {
            return false;
        }

        int ticksPassed = getUseDuration(flingshot, user) - remainingUseTicks;
        float pull = Math.min(1.0f, (float) ticksPassed / CHARGE_TIME);
        if (pull < MIN_PULL_TO_SHOOT) {
            return false;
        }

        if (level instanceof ServerLevel serverLevel) {
            shootProjectile(serverLevel, player, flingshot, projectileStack, player.getUsedItemHand(), pull);
        }

        level.playSound(
                null,
                player.getX(),
                player.getY(),
                player.getZ(),
                FlingshotSoundEvents.FLINGSHOT_SHOOT.get(),
                SoundSource.PLAYERS,
                1.0f,
                1.0f + level.getRandom().nextFloat() / 4.0f
        );
        player.awardStat(Stats.ITEM_USED.get(this));
        return true;
    }

    @Override
    public UseAnim getUseAnimation(ItemStack itemStack) {
        return UseAnim.BOW;
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
        ItemStack flingshot = player.getItemInHand(hand);
        if (getHeldProjectile(flingshot, player).isEmpty()) {
            return InteractionResultHolder.fail(flingshot);
        }

        player.startUsingItem(hand);
        return InteractionResultHolder.consume(flingshot);
    }

    @Override
    public boolean isValidRepairItem(ItemStack stack, ItemStack repairCandidate) {
        return repairCandidate.is(material.repairItems());
    }

    private void shootProjectile(ServerLevel level, Player shooter, ItemStack flingshot, ItemStack projectileStack, InteractionHand hand, float pull) {
        Item projectileItem = projectileStack.getItem();
        if (!(projectileItem instanceof Flingable<?> flingable)) {
            if (Services.PLATFORM.isDevelopmentEnvironment() && projectileStack.is(FlingshotItems.GLIMMER_GOO.get())) {
                FlingshotConstants.LOG.warn(
                        "Glimmer goo was not treated as Flingable on {}. Item class: {}",
                        Services.PLATFORM.getPlatformName(),
                        projectileItem.getClass().getName()
                );
            }
            shootItemStack(level, shooter, flingshot, projectileStack, hand, pull);
            return;
        }

        Projectile projectile = flingable.flingshot$getFlingableEntity(level, shooter, projectileStack, flingshot);
        if (Services.PLATFORM.isDevelopmentEnvironment() && projectileStack.is(FlingshotItems.GLIMMER_GOO.get())) {
            FlingshotConstants.LOG.info(
                    "Created glimmer goo projectile on {}. Projectile class: {}, entityType: {}",
                    Services.PLATFORM.getPlatformName(),
                    projectile.getClass().getName(),
                    projectile.getType()
            );
        }
        if (projectile instanceof AbstractBuck buck) {
            buck.setPullFactor(pull);
        }

        Vec3 eyePos = shooter.getEyePosition();
        Vec3 direction = shooter.getViewVector(1.0f).normalize();
        float velocity = calculateVelocity(level, flingshot, pull);
        float inaccuracy = calculateInaccuracy(level, flingshot);

        projectile.setPos(eyePos.add(direction.scale(PROJECTILE_SPAWN_DISTANCE)));
        projectile.setOwner(shooter);
        projectile.shootFromRotation(shooter, shooter.getXRot(), shooter.getYRot(), 0.0f, velocity, inaccuracy);
        projectile.hurtMarked = true;
        level.addFreshEntity(projectile);
        if (projectile instanceof AbstractBuck && shooter instanceof ServerPlayer serverPlayer) {
            FlingshotAdvancementTriggers.SHOOT_BUCK.get().trigger(serverPlayer);
        }

        projectileStack.consume(1, shooter);
        flingshot.hurtAndBreak(1, shooter, equipmentSlot(hand));
    }

    private void shootItemStack(ServerLevel level, Player shooter, ItemStack flingshot, ItemStack projectileStack, InteractionHand hand, float pull) {
        Vec3 eyePos = shooter.getEyePosition();
        Vec3 direction = shooter.getViewVector(1.0f).normalize();
        float velocity = calculateVelocity(level, flingshot, pull);
        float inaccuracy = calculateInaccuracy(level, flingshot);
        Vec3 movement = direction.add(
                level.getRandom().nextGaussian() * 0.0075f * inaccuracy,
                level.getRandom().nextGaussian() * 0.0075f * inaccuracy,
                level.getRandom().nextGaussian() * 0.0075f * inaccuracy
        ).normalize().scale(velocity).add(shooter.getDeltaMovement());
        int count = shooter.isCrouching() ? projectileStack.getCount() : 1;
        ItemEntity itemEntity = new ItemEntity(
                level,
                eyePos.x + direction.x * PROJECTILE_SPAWN_DISTANCE,
                eyePos.y + direction.y * PROJECTILE_SPAWN_DISTANCE,
                eyePos.z + direction.z * PROJECTILE_SPAWN_DISTANCE,
                projectileStack.copyWithCount(count),
                movement.x,
                movement.y,
                movement.z
        );
        itemEntity.setThrower(shooter);
        itemEntity.addTag(FLUNG_ITEM_TAG);
        itemEntity.setPickUpDelay(20);
        level.addFreshEntity(itemEntity);
        if (projectileStack.is(FlingshotItems.FLINGSHOT.get()) && shooter instanceof ServerPlayer serverPlayer) {
            FlingshotAdvancementTriggers.FLING_FLINGSHOT.get().trigger(serverPlayer);
        }

        projectileStack.consume(count, shooter);
        flingshot.hurtAndBreak(1, shooter, equipmentSlot(hand));
    }

    private float calculateVelocity(ServerLevel level, ItemStack flingshot, float pull) {
        int forceLevel = FlingshotEnchantments.getLevel(level.registryAccess(), FlingshotEnchantments.FORCE, flingshot);
        return BASE_VELOCITY * pull * (1.0f + 0.25f * forceLevel);
    }

    private float calculateInaccuracy(ServerLevel level, ItemStack flingshot) {
        int precisionLevel = FlingshotEnchantments.getLevel(level.registryAccess(), FlingshotEnchantments.PRECISION, flingshot);
        if (precisionLevel <= 0) {
            return INACCURACY;
        }
        return Math.max(0.0f, INACCURACY - Math.max(1.0f, 0.8f * precisionLevel));
    }

    private static boolean hasAutomation(ItemStack flingshot, LivingEntity entity) {
        return FlingshotEnchantments.getLevel(entity.registryAccess(), FlingshotEnchantments.AUTOMATION, flingshot) > 0;
    }

    private static EquipmentSlot equipmentSlot(InteractionHand hand) {
        return hand == InteractionHand.MAIN_HAND ? EquipmentSlot.MAINHAND : EquipmentSlot.OFFHAND;
    }

    private static boolean hasVersatility(ItemStack flingshot, LivingEntity entity) {
        return FlingshotConfig.enableVersatilityItemFlinging()
                && FlingshotEnchantments.getLevel(entity.registryAccess(), FlingshotEnchantments.VERSATILITY, flingshot) > 0;
    }

    public static ItemStack getHeldProjectile(ItemStack flingshot, LivingEntity entity) {
        if (hasVersatility(flingshot, entity)) {
            ItemStack mainHandItem = entity.getItemInHand(InteractionHand.MAIN_HAND);
            ItemStack projectileStack;
            if (mainHandItem == flingshot) {
                projectileStack = entity.getItemInHand(InteractionHand.OFF_HAND);
            } else {
                projectileStack = mainHandItem;
            }
            return FlingshotConfig.isVersatilityBlacklisted(projectileStack) ? ItemStack.EMPTY : projectileStack;
        }

        ItemStack offHandItem = entity.getItemInHand(InteractionHand.OFF_HAND);
        if (FLINGSHOT_PROJECTILES.test(offHandItem)) {
            return offHandItem;
        }

        ItemStack mainHandItem = entity.getItemInHand(InteractionHand.MAIN_HAND);
        if (mainHandItem != flingshot && FLINGSHOT_PROJECTILES.test(mainHandItem)) {
            return mainHandItem;
        }

        return ItemStack.EMPTY;
    }
}
