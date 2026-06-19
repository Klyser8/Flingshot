package com.tomrom.flingshot.client.renderer;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import com.tomrom.flingshot.FlingshotConstants;
import com.tomrom.flingshot.client.model.FrostBlastModel;
import com.tomrom.flingshot.entity.FrostBlast;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;

public class FrostBlastRenderer extends EntityRenderer<FrostBlast> {

    private static final ResourceLocation TEXTURE = FlingshotConstants.id("textures/entity/misc/frost_blast.png");
    private final FrostBlastModel model;

    public FrostBlastRenderer(EntityRendererProvider.Context context) {
        super(context);
        this.model = new FrostBlastModel(FrostBlastModel.createBodyLayer().bakeRoot());
    }

    @Override
    public void render(FrostBlast entity, float entityYaw, float partialTick, PoseStack poseStack, MultiBufferSource buffer, int packedLight) {
        poseStack.pushPose();
        poseStack.mulPose(Axis.YP.rotationDegrees(entity.getYRot() - 90.0f));
        poseStack.mulPose(Axis.ZP.rotationDegrees(entity.getXRot()));
        if (entity.getCollisionAge() == 0) {
            poseStack.mulPose(Axis.XP.rotationDegrees((entity.tickCount + partialTick) * 45.0f));
        }
        model.renderToBuffer(poseStack, buffer.getBuffer(model.renderType(TEXTURE)), packedLight, OverlayTexture.NO_OVERLAY);
        poseStack.popPose();
        super.render(entity, entityYaw, partialTick, poseStack, buffer, packedLight);
    }

    @Override
    public ResourceLocation getTextureLocation(FrostBlast entity) {
        return TEXTURE;
    }
}
