package com.tomrom.flingshot.registry;

import com.tomrom.flingshot.FlingshotConstants;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.damagesource.DamageType;

public class FlingshotDamageTypes {

    public static final ResourceKey<DamageType> COPPER_BUCK = ResourceKey.create(Registries.DAMAGE_TYPE, FlingshotConstants.id("copper_buck"));
    public static final ResourceKey<DamageType> SHIMMER_SHELL = ResourceKey.create(Registries.DAMAGE_TYPE, FlingshotConstants.id("shimmer_shell"));
}
