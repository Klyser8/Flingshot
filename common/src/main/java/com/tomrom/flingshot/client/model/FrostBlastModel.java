package com.tomrom.flingshot.client.model;

import com.tomrom.flingshot.client.renderer.BuckRenderState;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.*;
import net.minecraft.client.renderer.rendertype.RenderTypes;

public class FrostBlastModel extends EntityModel<BuckRenderState> {

	public FrostBlastModel(ModelPart root) {
		super(root, RenderTypes::entityTranslucent);
	}

	public static LayerDefinition createBodyLayer() {
		MeshDefinition meshdefinition = new MeshDefinition();
		PartDefinition partdefinition = meshdefinition.getRoot();

		PartDefinition bone = partdefinition.addOrReplaceChild("bone", CubeListBuilder.create().texOffs(0, 0).addBox(-1.5F, -0.7F, -1.5F, 3.0F, 2.0F, 3.0F, new CubeDeformation(0.0F))
		.texOffs(9, 0).addBox(-0.5F, 1.3F, -0.5F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F))
		.texOffs(6, 6).addBox(-1.0F, 0.8F, -1.0F, 2.0F, 1.0F, 2.0F, new CubeDeformation(0.0F))
		.texOffs(0, 5).addBox(-1.0F, -1.7F, -1.0F, 2.0F, 1.0F, 2.0F, new CubeDeformation(0.0F))
		.texOffs(0, 8).addBox(-0.5F, -2.7F, -0.5F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.2F, 0.0F, 0.0F, 0.0F, 0.0F, -1.5708F));

		return LayerDefinition.create(meshdefinition, 16, 16);
	}

}
