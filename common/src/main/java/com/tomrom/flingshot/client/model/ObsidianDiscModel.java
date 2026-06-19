package com.tomrom.flingshot.client.model;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.tomrom.flingshot.entity.ObsidianDisc;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.CubeDeformation;
import net.minecraft.client.model.geom.builders.CubeListBuilder;
import net.minecraft.client.model.geom.builders.LayerDefinition;
import net.minecraft.client.model.geom.builders.MeshDefinition;
import net.minecraft.client.model.geom.builders.PartDefinition;
import net.minecraft.client.renderer.RenderType;

public class ObsidianDiscModel extends EntityModel<ObsidianDisc> {

    private final ModelPart root;

    public ObsidianDiscModel(ModelPart root) {
        super(RenderType::entityTranslucent);
        this.root = root;
    }

    @Override
    public void setupAnim(ObsidianDisc entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
    }

    @Override
    public void renderToBuffer(PoseStack poseStack, VertexConsumer vertexConsumer, int packedLight, int packedOverlay, int color) {
        root.render(poseStack, vertexConsumer, packedLight, packedOverlay, color);
    }

    public static LayerDefinition createBodyLayer() {
        MeshDefinition meshDefinition = new MeshDefinition();
        PartDefinition root = meshDefinition.getRoot();

        root.addOrReplaceChild(
                "bone",
                CubeListBuilder.create()
                        .texOffs(0, 0).addBox(-2.0f, -1.0f, -1.0f, 3.0f, 1.0f, 3.0f, new CubeDeformation(0.0f))
                        .texOffs(9, 0).addBox(-0.999f, -1.001f, 1.5f, 2.0f, 1.0f, 1.0f, new CubeDeformation(0.0f))
                        .texOffs(6, 6).addBox(-2.5f, -1.001f, 0.001f, 1.0f, 1.0f, 2.0f, new CubeDeformation(0.0f))
                        .texOffs(0, 8).addBox(0.5f, -1.001f, -1.001f, 1.0f, 1.0f, 2.0f, new CubeDeformation(0.0f))
                        .texOffs(6, 4).addBox(-2.001f, -1.001f, -1.5f, 2.0f, 1.0f, 1.0f, new CubeDeformation(0.0f))
                        .texOffs(0, 4).addBox(-1.5f, -1.5f, -0.5f, 2.0f, 2.0f, 2.0f, new CubeDeformation(0.0f)),
                PartPose.offset(0.0f, 1.5f, 0.0f)
        );

        return LayerDefinition.create(meshDefinition, 16, 16);
    }
}
