package com.tomrom.flingshot.client.model;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.tomrom.flingshot.entity.ShimmerShell;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.CubeDeformation;
import net.minecraft.client.model.geom.builders.CubeListBuilder;
import net.minecraft.client.model.geom.builders.LayerDefinition;
import net.minecraft.client.model.geom.builders.MeshDefinition;
import net.minecraft.client.model.geom.builders.PartDefinition;
import net.minecraft.client.renderer.RenderType;

public class ShimmerShellModel extends EntityModel<ShimmerShell> {

    private final ModelPart root;

    public ShimmerShellModel(ModelPart root) {
        super(RenderType::entityTranslucent);
        this.root = root;
    }

    @Override
    public void setupAnim(ShimmerShell entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
    }

    @Override
    public void renderToBuffer(PoseStack poseStack, VertexConsumer vertexConsumer, int packedLight, int packedOverlay, int color) {
        root.render(poseStack, vertexConsumer, packedLight, packedOverlay, color);
    }

    public static LayerDefinition createBodyLayer() {
        MeshDefinition meshDefinition = new MeshDefinition();
        PartDefinition root = meshDefinition.getRoot();

        root.addOrReplaceChild(
                "main",
                CubeListBuilder.create()
                        .texOffs(0, 0).addBox(-2.0f, -2.0f, -2.0f, 4.0f, 4.0f, 4.0f, new CubeDeformation(0.0f))
                        .texOffs(9, 9).addBox(-1.5f, 1.5f, -1.5f, 3.0f, 1.0f, 3.0f, new CubeDeformation(0.0f))
                        .texOffs(0, 8).addBox(-1.5f, -2.5f, -1.5f, 3.0f, 1.0f, 3.0f, new CubeDeformation(0.0f))
                        .texOffs(8, 13).addBox(-2.5f, -1.5f, -1.5f, 1.0f, 3.0f, 3.0f, new CubeDeformation(0.0f))
                        .texOffs(0, 12).addBox(1.5f, -1.5f, -1.5f, 1.0f, 3.0f, 3.0f, new CubeDeformation(0.0f))
                        .texOffs(16, 4).addBox(-1.5f, -1.5f, 1.5f, 3.0f, 3.0f, 1.0f, new CubeDeformation(0.0f))
                        .texOffs(12, 0).addBox(-1.5f, -1.5f, -2.5f, 3.0f, 3.0f, 1.0f, new CubeDeformation(0.0f))
                        .texOffs(0, 0).addBox(-3.0f, -0.5f, -0.5f, 1.0f, 1.0f, 1.0f, new CubeDeformation(0.0f))
                        .texOffs(0, 2).addBox(2.0f, -0.5f, -0.5f, 1.0f, 1.0f, 1.0f, new CubeDeformation(0.0f))
                        .texOffs(20, 0).addBox(-0.5f, -0.5f, -3.0f, 1.0f, 1.0f, 1.0f, new CubeDeformation(0.0f))
                        .texOffs(20, 2).addBox(-0.5f, -0.5f, 2.0f, 1.0f, 1.0f, 1.0f, new CubeDeformation(0.0f))
                        .texOffs(5, 13).addBox(-0.5f, -3.0f, -0.5f, 1.0f, 1.0f, 1.0f, new CubeDeformation(0.0f))
                        .texOffs(13, 14).addBox(-0.5f, 2.0f, -0.5f, 1.0f, 1.0f, 1.0f, new CubeDeformation(0.0f)),
                PartPose.offset(0.0f, 2.2f, 0.0f)
        );

        return LayerDefinition.create(meshDefinition, 32, 32);
    }
}
