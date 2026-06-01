package com.tomrom.flingshot.registry;

import com.tomrom.flingshot.FlingshotConstants;
import com.tomrom.flingshot.platform.CommonPlatformHelper;
import net.minecraft.sounds.SoundEvent;

import java.util.function.Supplier;

public class FlingshotSoundEvents {

    public static final Supplier<SoundEvent> FLINGSHOT_SHOOT = CommonPlatformHelper.registerSoundEvent(
            "flingshot_shoot",
            () -> SoundEvent.createVariableRangeEvent(FlingshotConstants.id("flingshot_shoot"))
    );

    public static void init() {
    }
}
