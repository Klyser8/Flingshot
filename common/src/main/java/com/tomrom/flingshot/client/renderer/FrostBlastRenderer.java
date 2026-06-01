package com.tomrom.flingshot.client.renderer;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import com.tomrom.flingshot.FlingshotConstants;
import com.tomrom.flingshot.client.model.FrostBlastModel;
import com.tomrom.flingshot.entity.FrostBlast;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.state.level.CameraRenderState;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.Identifier;

public class FrostBlastRenderer extends EntityRenderer<FrostBlast, BuckRenderState> {

    private static final Identifier TEXTURE = FlingshotConstants.id("textures/entity/misc/frost_blast.png");
    private final FrostBlastModel model;

    public FrostBlastRenderer(EntityRendererProvider.Context context) {
        super(context);
        this.model = new FrostBlastModel(FrostBlastModel.createBodyLayer().bakeRoot());
    }

    @Override
    public void submit(BuckRenderState state, PoseStack poseStack, SubmitNodeCollector submitNodeCollector, CameraRenderState camera) {
        poseStack.pushPose();
        poseStack.mulPose(Axis.YP.rotationDegrees(state.yRot - 90.0f));
        poseStack.mulPose(Axis.ZP.rotationDegrees(state.xRot));
        if (state.collisionAge == 0) {
            poseStack.mulPose(Axis.XP.rotationDegrees((state.tickCount + state.partialTicks) * state.spinSpeed));
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
    public void extractRenderState(FrostBlast entity, BuckRenderState state, float partialTicks) {
        super.extractRenderState(entity, state, partialTicks);
        state.xRot = entity.getXRot(partialTicks);
        state.yRot = entity.getYRot(partialTicks);
        state.shake = entity.shakeTime - partialTicks;
        state.tickCount = entity.tickCount;
        state.collisionAge = entity.getCollisionAge();
        state.partialTicks = partialTicks;
        state.spinSpeed = 45.0f;
    }
}
