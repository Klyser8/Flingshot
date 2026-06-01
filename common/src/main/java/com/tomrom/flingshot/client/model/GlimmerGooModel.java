package com.tomrom.flingshot.client.model;

import com.tomrom.flingshot.client.renderer.BuckRenderState;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.CubeDeformation;
import net.minecraft.client.model.geom.builders.CubeListBuilder;
import net.minecraft.client.model.geom.builders.LayerDefinition;
import net.minecraft.client.model.geom.builders.MeshDefinition;
import net.minecraft.client.model.geom.builders.PartDefinition;
import net.minecraft.client.renderer.rendertype.RenderTypes;

public class GlimmerGooModel extends EntityModel<BuckRenderState> {

    public GlimmerGooModel(ModelPart root) {
        super(root, RenderTypes::entityTranslucent);
    }

    public static LayerDefinition createBodyLayer() {
        MeshDefinition meshDefinition = new MeshDefinition();
        PartDefinition root = meshDefinition.getRoot();

        root.addOrReplaceChild(
                "bone",
                CubeListBuilder.create()
                        .texOffs(0, 0).addBox(-1.5f, -0.7f, -1.5f, 3.0f, 2.0f, 3.0f, new CubeDeformation(0.0f))
                        .texOffs(9, 0).addBox(-0.5f, 1.3f, -0.5f, 1.0f, 1.0f, 1.0f, new CubeDeformation(0.0f))
                        .texOffs(6, 6).addBox(-1.0f, 0.8f, -1.0f, 2.0f, 1.0f, 2.0f, new CubeDeformation(0.0f))
                        .texOffs(0, 5).addBox(-1.0f, -1.7f, -1.0f, 2.0f, 1.0f, 2.0f, new CubeDeformation(0.0f))
                        .texOffs(0, 8).addBox(-0.5f, -2.7f, -0.5f, 1.0f, 1.0f, 1.0f, new CubeDeformation(0.0f)),
                PartPose.offsetAndRotation(-0.2f, 0.0f, 0.0f, 0.0f, 0.0f, 1.5708f)
        );

        return LayerDefinition.create(meshDefinition, 16, 16);
    }
}
