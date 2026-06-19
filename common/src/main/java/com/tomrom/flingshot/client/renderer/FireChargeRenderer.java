package com.tomrom.flingshot.client.renderer;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import com.tomrom.flingshot.FlingshotConstants;
import com.tomrom.flingshot.client.model.GenericBuckModel;
import com.tomrom.flingshot.entity.FireCharge;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;

public class FireChargeRenderer extends EntityRenderer<FireCharge> {

    private static final ResourceLocation TEXTURE = FlingshotConstants.id("textures/entity/misc/fire_charge.png");
    private final GenericBuckModel<FireCharge> model;

    public FireChargeRenderer(EntityRendererProvider.Context context) {
        super(context);
        this.model = new GenericBuckModel<>(GenericBuckModel.createBodyLayer().bakeRoot());
    }

    @Override
    public void render(FireCharge entity, float entityYaw, float partialTick, PoseStack poseStack, MultiBufferSource buffer, int packedLight) {
        poseStack.pushPose();
        poseStack.mulPose(Axis.YP.rotationDegrees(entity.getYRot() - 90.0f));
        poseStack.mulPose(Axis.ZP.rotationDegrees(entity.getXRot()));
        model.renderToBuffer(poseStack, buffer.getBuffer(model.renderType(TEXTURE)), packedLight, OverlayTexture.NO_OVERLAY);
        poseStack.popPose();
        super.render(entity, entityYaw, partialTick, poseStack, buffer, packedLight);
    }

    @Override
    public ResourceLocation getTextureLocation(FireCharge entity) {
        return TEXTURE;
    }
}
