package com.tomrom.flingshot.client.model;

import net.minecraft.client.model.EntityModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.CubeDeformation;
import net.minecraft.client.model.geom.builders.CubeListBuilder;
import net.minecraft.client.model.geom.builders.LayerDefinition;
import net.minecraft.client.model.geom.builders.MeshDefinition;
import net.minecraft.client.model.geom.builders.PartDefinition;
import net.minecraft.client.renderer.entity.state.ArrowRenderState;
import net.minecraft.client.renderer.rendertype.RenderTypes;

public class GenericBuckModel extends EntityModel<ArrowRenderState> {

    public GenericBuckModel(ModelPart root) {
        super(root, RenderTypes::entityTranslucent);
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
                PartPose.offset(0.0f, 1.5f, 0.0f)
        );

        return LayerDefinition.create(meshDefinition, 16, 16);
    }
}
