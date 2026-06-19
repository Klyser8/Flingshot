package com.tomrom.flingshot.client.renderer;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import com.tomrom.flingshot.FlingshotConstants;
import com.tomrom.flingshot.client.model.ObsidianDiscModel;
import com.tomrom.flingshot.entity.ObsidianDisc;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;

public class ObsidianDiscRenderer extends EntityRenderer<ObsidianDisc> {

    private static final ResourceLocation TEXTURE = FlingshotConstants.id("textures/entity/misc/obsidian_disc.png");
    private final ObsidianDiscModel model;

    public ObsidianDiscRenderer(EntityRendererProvider.Context context) {
        super(context);
        this.model = new ObsidianDiscModel(ObsidianDiscModel.createBodyLayer().bakeRoot());
    }

    @Override
    public void render(ObsidianDisc entity, float entityYaw, float partialTick, PoseStack poseStack, MultiBufferSource buffer, int packedLight) {
        poseStack.pushPose();
        poseStack.mulPose(Axis.YP.rotationDegrees(entity.getYRot() - 90.0f));
        poseStack.mulPose(Axis.ZP.rotationDegrees(entity.getXRot()));
        if (entity.getCollisionAge() == 0) {
            poseStack.mulPose(Axis.YP.rotationDegrees((entity.tickCount + partialTick) * 60.0f));
        }
        model.renderToBuffer(poseStack, buffer.getBuffer(model.renderType(TEXTURE)), packedLight, OverlayTexture.NO_OVERLAY);
        poseStack.popPose();
        super.render(entity, entityYaw, partialTick, poseStack, buffer, packedLight);
    }

    @Override
    public ResourceLocation getTextureLocation(ObsidianDisc entity) {
        return TEXTURE;
    }
}
