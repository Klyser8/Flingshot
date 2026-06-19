package com.tomrom.flingshot;


import com.tomrom.flingshot.platform.NeoForgeRegistryHelper;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.loading.FMLEnvironment;

import java.lang.reflect.Method;

@Mod(FlingshotConstants.MOD_ID)
public class Flingshot {

    public Flingshot(IEventBus eventBus, ModContainer modContainer) {

        FlingshotConstants.LOG.info("Loading {} for NeoForge.", FlingshotConstants.MOD_NAME);
        NeoForgeRegistryHelper.register(eventBus);
        if (FMLEnvironment.dist == Dist.CLIENT) {
            registerClientConfigScreen(modContainer);
        }
        FlingshotCommon.init();

    }

    private static void registerClientConfigScreen(ModContainer modContainer) {
        try {
            Class<?> configScreen = Class.forName("com.tomrom.flingshot.client.FlingshotNeoForgeConfigScreen");
            Method register = configScreen.getMethod("register", ModContainer.class);
            register.invoke(null, modContainer);
        } catch (ReflectiveOperationException exception) {
            FlingshotConstants.LOG.warn("Failed to register Flingshot's NeoForge config screen.", exception);
        }
    }
}
