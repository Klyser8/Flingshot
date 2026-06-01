package com.tomrom.flingshot;

import com.tomrom.flingshot.config.FlingshotConfig;
import com.tomrom.flingshot.platform.Services;
import com.tomrom.flingshot.registry.FlingshotAdvancementTriggers;
import com.tomrom.flingshot.registry.FlingshotBlocks;
import com.tomrom.flingshot.registry.FlingshotCreativeTabs;
import com.tomrom.flingshot.registry.FlingshotEntities;
import com.tomrom.flingshot.registry.FlingshotFeatures;
import com.tomrom.flingshot.registry.FlingshotItems;
import com.tomrom.flingshot.registry.FlingshotParticles;
import com.tomrom.flingshot.registry.FlingshotSoundEvents;

public class FlingshotCommon {

    public static void init() {

        FlingshotConfig.init();
        FlingshotSoundEvents.init();
        FlingshotParticles.init();
        FlingshotAdvancementTriggers.init();
        FlingshotBlocks.init();
        FlingshotFeatures.init();
        FlingshotEntities.init();
        FlingshotItems.init();
        FlingshotCreativeTabs.init();

        FlingshotConstants.LOG.info("{} initialized on {} in a {} environment.",
                FlingshotConstants.MOD_NAME,
                Services.PLATFORM.getPlatformName(),
                Services.PLATFORM.getEnvironmentName());
    }
}
