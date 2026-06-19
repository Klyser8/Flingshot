package com.tomrom.flingshot.client.renderer;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import com.tomrom.flingshot.FlingshotConstants;
import com.tomrom.flingshot.client.model.GlimmerGooModel;
import com.tomrom.flingshot.entity.GlimmerGoo;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;

public class GlimmerGooRenderer extends EntityRenderer<GlimmerGoo> {

    private static final int FULL_BRIGHT = 0xF000F0;
    private static final ResourceLocation TEXTURE = FlingshotConstants.id("textures/entity/misc/glimmer_goo.png");
    private final GlimmerGooModel model;

    public GlimmerGooRenderer(EntityRendererProvider.Context context) {
        super(context);
        this.model = new GlimmerGooModel(GlimmerGooModel.createBodyLayer().bakeRoot());
    }

    @Override
    public void render(GlimmerGoo entity, float entityYaw, float partialTick, PoseStack poseStack, MultiBufferSource buffer, int packedLight) {
        poseStack.pushPose();
        poseStack.mulPose(Axis.YP.rotationDegrees(entity.getYRot() - 90.0f));
        poseStack.mulPose(Axis.ZP.rotationDegrees(entity.getXRot()));
        if (entity.getCollisionAge() == 0) {
            poseStack.mulPose(Axis.XP.rotationDegrees((entity.tickCount + partialTick) * 35.0f));
        }
        model.renderToBuffer(poseStack, buffer.getBuffer(model.renderType(TEXTURE)), FULL_BRIGHT, OverlayTexture.NO_OVERLAY);
        poseStack.popPose();
        super.render(entity, entityYaw, partialTick, poseStack, buffer, packedLight);
    }

    @Override
    public ResourceLocation getTextureLocation(GlimmerGoo entity) {
        return TEXTURE;
    }
}
