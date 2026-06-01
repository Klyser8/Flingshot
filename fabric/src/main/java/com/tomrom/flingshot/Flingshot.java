package com.tomrom.flingshot;

import com.tomrom.flingshot.worldgen.FlingshotFabricWorldgen;
import net.fabricmc.api.ModInitializer;

public class Flingshot implements ModInitializer {
    
    @Override
    public void onInitialize() {
        FlingshotConstants.LOG.info("Loading {} for Fabric.", FlingshotConstants.MOD_NAME);
        FlingshotCommon.init();
        FlingshotFabricWorldgen.init();
    }
}
