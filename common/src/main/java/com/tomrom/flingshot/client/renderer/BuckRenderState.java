package com.tomrom.flingshot.client.renderer;

import net.minecraft.client.renderer.entity.state.ArrowRenderState;

public class BuckRenderState extends ArrowRenderState {

    public int tickCount;
    public int collisionAge;
    public float partialTicks;

    // Tbh this might be a bit of a hack but it works so meh
    public float spinAxisX = 0.0f;
    public float spinAxisY = 0.0f;
    public float spinAxisZ = 0.0f;
    public float spinSpeed = 0.0f;
}
