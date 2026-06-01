package com.tomrom.flingshot.client.renderer;

import com.mojang.blaze3d.vertex.PoseStack;
import com.tomrom.flingshot.FlingshotConstants;
import com.tomrom.flingshot.client.model.ShimmerShellModel;
import com.tomrom.flingshot.entity.ShimmerShell;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.state.level.CameraRenderState;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.Identifier;
import net.minecraft.world.phys.Vec3;
import org.joml.Quaternionf;

public class ShimmerShellRenderer extends EntityRenderer<ShimmerShell, BuckRenderState> {

    private static final Identifier TEXTURE = FlingshotConstants.id("textures/entity/misc/shimmer_shell.png");
    private final ShimmerShellModel model;
    float modelPivotYOffset = 2.2f;

    public ShimmerShellRenderer(EntityRendererProvider.Context context) {
        super(context);
        this.model = new ShimmerShellModel(ShimmerShellModel.createBodyLayer().bakeRoot());
    }

    @Override
    public void submit(BuckRenderState state, PoseStack poseStack, SubmitNodeCollector submitNodeCollector, CameraRenderState camera) {
        poseStack.pushPose();
        if (state.collisionAge == 0 && Math.abs(state.spinSpeed) > 1.0E-4f) {
            float angle = (float) Math.toRadians((state.tickCount + state.partialTicks) * state.spinSpeed);

            poseStack.translate(0.0f, modelPivotYOffset / 16.0f, 0.0f);
            poseStack.mulPose(new Quaternionf().rotationAxis(angle, state.spinAxisX, state.spinAxisY, state.spinAxisZ));
            poseStack.translate(0.0f, -modelPivotYOffset / 16.0f, 0.0f);
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
    public void extractRenderState(ShimmerShell entity, BuckRenderState state, float partialTicks) {
        super.extractRenderState(entity, state, partialTicks);
        state.xRot = entity.getXRot(partialTicks);
        state.yRot = entity.getYRot(partialTicks);
        state.shake = entity.shakeTime - partialTicks;
        state.tickCount = entity.tickCount;
        state.collisionAge = entity.getCollisionAge();
        state.partialTicks = partialTicks;

        Vec3 vel = entity.getDeltaMovement();
        double speed = vel.length();
        if (speed < 1.0E-4) {
            state.spinAxisX = 0.0f;
            state.spinAxisY = 0.0f;
            state.spinAxisZ = 1.0f;
            state.spinSpeed = 0.0f;
            return;
        }

        Vec3 travel = vel.normalize();
        Vec3 axis = travel.cross(new Vec3(0.0, 1.0, 0.0));
        if (axis.lengthSqr() < 1.0E-4) {
            // When fired almost straight up/down, horizontal travel is tiny. Use yaw to
            // keep the visual axle aligned with the shooter's left-right direction.
            double yawRad = Math.toRadians(entity.getYRot());
            axis = new Vec3(Math.cos(yawRad), 0.0, Math.sin(yawRad));
        } else {
            axis = axis.normalize();
        }

        double yawRad = Math.toRadians(entity.getYRot());
        Vec3 groundForward = new Vec3(-Math.sin(yawRad), 0.0, Math.cos(yawRad));
        double dot = groundForward.x * vel.x + groundForward.z * vel.z;
        float sign = dot >= 0.0 ? -1.0f : 1.0f;

        state.spinAxisX = (float) axis.x;
        state.spinAxisY = (float) axis.y;
        state.spinAxisZ = (float) axis.z;
        state.spinSpeed = (float) (speed * 30.0) * sign;
    }
}
