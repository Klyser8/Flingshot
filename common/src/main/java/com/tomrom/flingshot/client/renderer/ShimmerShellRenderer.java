package com.tomrom.flingshot.client.renderer;

import com.mojang.blaze3d.vertex.PoseStack;
import com.tomrom.flingshot.FlingshotConstants;
import com.tomrom.flingshot.client.model.ShimmerShellModel;
import com.tomrom.flingshot.entity.ShimmerShell;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.phys.Vec3;
import org.joml.Quaternionf;

public class ShimmerShellRenderer extends EntityRenderer<ShimmerShell> {

    private static final ResourceLocation TEXTURE = FlingshotConstants.id("textures/entity/misc/shimmer_shell.png");
    private final ShimmerShellModel model;
    private static final float MODEL_PIVOT_Y_OFFSET = 2.2f;

    public ShimmerShellRenderer(EntityRendererProvider.Context context) {
        super(context);
        this.model = new ShimmerShellModel(ShimmerShellModel.createBodyLayer().bakeRoot());
    }

    @Override
    public void render(ShimmerShell entity, float entityYaw, float partialTick, PoseStack poseStack, MultiBufferSource buffer, int packedLight) {
        poseStack.pushPose();
        Spin spin = spin(entity);
        if (entity.getCollisionAge() == 0 && Math.abs(spin.speed) > 0.0001) {
            float angle = (float) Math.toRadians((entity.tickCount + partialTick) * spin.speed);

            poseStack.translate(0.0f, MODEL_PIVOT_Y_OFFSET / 16.0f, 0.0f);
            poseStack.mulPose(new Quaternionf().rotationAxis(angle, spin.axisX, spin.axisY, spin.axisZ));
            poseStack.translate(0.0f, -MODEL_PIVOT_Y_OFFSET / 16.0f, 0.0f);
        }

        model.renderToBuffer(poseStack, buffer.getBuffer(model.renderType(TEXTURE)), packedLight, OverlayTexture.NO_OVERLAY);
        poseStack.popPose();
        super.render(entity, entityYaw, partialTick, poseStack, buffer, packedLight);
    }

    @Override
    public ResourceLocation getTextureLocation(ShimmerShell entity) {
        return TEXTURE;
    }

    private static Spin spin(ShimmerShell entity) {
        Vec3 vel = entity.getDeltaMovement();
        double speed = vel.length();
        if (speed < 0.0001) {
            return new Spin(0.0f, 0.0f, 1.0f, 0.0f);
        }

        Vec3 travel = vel.normalize();
        Vec3 axis = travel.cross(new Vec3(0.0, 1.0, 0.0));
        if (axis.lengthSqr() < 0.0001) {
            double yawRad = Math.toRadians(entity.getYRot());
            axis = new Vec3(Math.cos(yawRad), 0.0, Math.sin(yawRad));
        } else {
            axis = axis.normalize();
        }

        double yawRad = Math.toRadians(entity.getYRot());
        Vec3 groundForward = new Vec3(-Math.sin(yawRad), 0.0, Math.cos(yawRad));
        double dot = groundForward.x * vel.x + groundForward.z * vel.z;
        float sign = dot >= 0.0 ? -1.0f : 1.0f;

        return new Spin((float) axis.x, (float) axis.y, (float) axis.z, (float) (speed * 30.0) * sign);
    }

    private record Spin(float axisX, float axisY, float axisZ, float speed) {
    }
}
