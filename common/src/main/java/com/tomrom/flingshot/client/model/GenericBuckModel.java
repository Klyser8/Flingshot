package com.tomrom.flingshot.client.model;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.CubeDeformation;
import net.minecraft.client.model.geom.builders.CubeListBuilder;
import net.minecraft.client.model.geom.builders.LayerDefinition;
import net.minecraft.client.model.geom.builders.MeshDefinition;
import net.minecraft.client.model.geom.builders.PartDefinition;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.world.entity.projectile.Projectile;

public class GenericBuckModel<T extends Projectile> extends EntityModel<T> {

    private final ModelPart root;

    public GenericBuckModel(ModelPart root) {
        super(RenderType::entityTranslucent);
        this.root = root;
    }

    @Override
    public void setupAnim(T entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
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
                        .texOffs(0, 0).addBox(-1.5f, -1.5f, -1.5f, 3.0f, 3.0f, 3.0f, new CubeDeformation(0.0f))
                        .texOffs(9, 0).addBox(-0.5f, -0.5f, -2.0f, 1.0f, 1.0f, 1.0f, new CubeDeformation(0.0f))
                        .texOffs(4, 8).addBox(-2.0f, -0.5f, -0.5f, 1.0f, 1.0f, 1.0f, new CubeDeformation(0.0f))
                        .texOffs(0, 6).addBox(-0.5f, 1.0f, -0.5f, 1.0f, 1.0f, 1.0f, new CubeDeformation(0.0f))
                        .texOffs(7, 7).addBox(1.0f, -0.5f, -0.5f, 1.0f, 1.0f, 1.0f, new CubeDeformation(0.0f))
                        .texOffs(0, 8).addBox(-0.5f, -0.5f, 1.0f, 1.0f, 1.0f, 1.0f, new CubeDeformation(0.0f))
                        .texOffs(4, 6).addBox(-0.5f, -2.0f, -0.5f, 1.0f, 1.0f, 1.0f, new CubeDeformation(0.0f)),
                PartPose.ZERO
        );

        return LayerDefinition.create(meshDefinition, 16, 16);
    }
}
