package com.tomrom.flingshot.registry;

import com.tomrom.flingshot.FlingshotConstants;
import com.tomrom.flingshot.entity.CopperBuck;
import com.tomrom.flingshot.entity.FireCharge;
import com.tomrom.flingshot.entity.FrostBlast;
import com.tomrom.flingshot.entity.GlimmerGoo;
import com.tomrom.flingshot.entity.ObsidianDisc;
import com.tomrom.flingshot.entity.ShimmerShell;
import com.tomrom.flingshot.platform.CommonPlatformHelper;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;

import java.util.function.Supplier;

public class FlingshotEntities {

    public static final Supplier<EntityType<CopperBuck>> COPPER_BUCK = CommonPlatformHelper.registerEntityType(
            "copper_buck",
            () -> EntityType.Builder.<CopperBuck>of(CopperBuck::new, MobCategory.MISC)
                    .noLootTable()
                    .sized(0.2f, 0.2f)
                    .eyeHeight(0.13f)
                    .clientTrackingRange(10)
                    .updateInterval(20)
                    .build(ResourceKey.create(Registries.ENTITY_TYPE, FlingshotConstants.id("copper_buck")))
    );

    public static final Supplier<EntityType<ObsidianDisc>> OBSIDIAN_DISC = CommonPlatformHelper.registerEntityType(
            "obsidian_disc",
            () -> EntityType.Builder.<ObsidianDisc>of(ObsidianDisc::new, MobCategory.MISC)
                    .noLootTable()
                    .sized(0.2f, 0.2f)
                    .eyeHeight(0.13f)
                    .clientTrackingRange(10)
                    .updateInterval(20)
                    .build(ResourceKey.create(Registries.ENTITY_TYPE, FlingshotConstants.id("obsidian_disc")))
    );

    public static final Supplier<EntityType<ShimmerShell>> SHIMMER_SHELL = CommonPlatformHelper.registerEntityType(
            "shimmer_shell",
            () -> EntityType.Builder.<ShimmerShell>of(ShimmerShell::new, MobCategory.MISC)
                    .noLootTable()
                    .sized(0.3f, 0.3f)
                    .eyeHeight(0.15f)
                    .clientTrackingRange(10)
                    .updateInterval(20)
                    .build(ResourceKey.create(Registries.ENTITY_TYPE, FlingshotConstants.id("shimmer_shell")))
    );

    public static final Supplier<EntityType<FrostBlast>> FROST_BLAST = CommonPlatformHelper.registerEntityType(
            "frost_blast",
            () -> EntityType.Builder.<FrostBlast>of(FrostBlast::new, MobCategory.MISC)
                    .noLootTable()
                    .sized(0.2f, 0.2f)
                    .eyeHeight(0.13f)
                    .clientTrackingRange(10)
                    .updateInterval(20)
                    .build(ResourceKey.create(Registries.ENTITY_TYPE, FlingshotConstants.id("frost_blast")))
    );

    public static final Supplier<EntityType<GlimmerGoo>> GLIMMER_GOO = CommonPlatformHelper.registerEntityType(
            "glimmer_goo",
            () -> EntityType.Builder.<GlimmerGoo>of(GlimmerGoo::new, MobCategory.MISC)
                    .noLootTable()
                    .sized(0.2f, 0.2f)
                    .eyeHeight(0.13f)
                    .clientTrackingRange(10)
                    .updateInterval(20)
                    .build(ResourceKey.create(Registries.ENTITY_TYPE, FlingshotConstants.id("glimmer_goo")))
    );

    public static final Supplier<EntityType<FireCharge>> FIRE_CHARGE = CommonPlatformHelper.registerEntityType(
            "fire_charge",
            () -> EntityType.Builder.<FireCharge>of(FireCharge::new, MobCategory.MISC)
                    .noLootTable()
                    .sized(0.2f, 0.2f)
                    .eyeHeight(0.13f)
                    .clientTrackingRange(10)
                    .updateInterval(20)
                    .build(ResourceKey.create(Registries.ENTITY_TYPE, FlingshotConstants.id("fire_charge")))
    );

    public static void init() {
    }
}
