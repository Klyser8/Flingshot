package com.tomrom.flingshot.client.renderer;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import com.tomrom.flingshot.FlingshotConstants;
import com.tomrom.flingshot.client.model.ObsidianDiscModel;
import com.tomrom.flingshot.entity.ObsidianDisc;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.state.level.CameraRenderState;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.Identifier;

public class ObsidianDiscRenderer extends EntityRenderer<ObsidianDisc, BuckRenderState> {

    private static final Identifier TEXTURE = FlingshotConstants.id("textures/entity/misc/obsidian_disc.png");
    private final ObsidianDiscModel model;

    public ObsidianDiscRenderer(EntityRendererProvider.Context context) {
        super(context);
        this.model = new ObsidianDiscModel(ObsidianDiscModel.createBodyLayer().bakeRoot());
    }

    @Override
    public void submit(BuckRenderState state, PoseStack poseStack, SubmitNodeCollector submitNodeCollector, CameraRenderState camera) {
        poseStack.pushPose();
        poseStack.mulPose(Axis.YP.rotationDegrees(state.yRot - 90.0f));
        poseStack.mulPose(Axis.ZP.rotationDegrees(state.xRot));
        if (state.collisionAge == 0) {
            poseStack.mulPose(Axis.YP.rotationDegrees((state.tickCount + state.partialTicks) * 60.0f));
        }
        submitNodeCollector.submitModel(
                model,
                state,
                poseStack,
                TEXTURE,
                state.lightCoords,
                OverlayTexture.NO_OVERLAY,
                state.outlineColor,
                null
        );
        poseStack.popPose();
        super.submit(state, poseStack, submitNodeCollector, camera);
    }

    @Override
    public BuckRenderState createRenderState() {
        return new BuckRenderState();
    }

    @Override
    public void extractRenderState(ObsidianDisc entity, BuckRenderState state, float partialTicks) {
        super.extractRenderState(entity, state, partialTicks);
        state.xRot = entity.getXRot(partialTicks);
        state.yRot = entity.getYRot(partialTicks);
        state.shake = entity.shakeTime - partialTicks;
        state.tickCount = entity.tickCount;
        state.collisionAge = entity.getCollisionAge();
        state.partialTicks = partialTicks;
    }
}
