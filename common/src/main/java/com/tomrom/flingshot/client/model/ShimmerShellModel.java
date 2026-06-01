package com.tomrom.flingshot.client.model;


import com.tomrom.flingshot.client.renderer.BuckRenderState;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.*;
import net.minecraft.client.renderer.rendertype.RenderTypes;

public class ShimmerShellModel extends EntityModel<BuckRenderState>{
//	private final ModelPart main;

	public ShimmerShellModel(ModelPart root) {
		super(root, RenderTypes::entityTranslucent);
	}

	public static LayerDefinition createBodyLayer() {
		MeshDefinition meshdefinition = new MeshDefinition();
		PartDefinition partdefinition = meshdefinition.getRoot();

		PartDefinition main = partdefinition.addOrReplaceChild("main", CubeListBuilder.create().texOffs(0, 0).addBox(-2.0F, -2.0F, -2.0F, 4.0F, 4.0F, 4.0F, new CubeDeformation(0.0F))
		.texOffs(9, 9).addBox(-1.5F, 1.5F, -1.5F, 3.0F, 1.0F, 3.0F, new CubeDeformation(0.0F))
		.texOffs(0, 8).addBox(-1.5F, -2.5F, -1.5F, 3.0F, 1.0F, 3.0F, new CubeDeformation(0.0F))
		.texOffs(8, 13).addBox(-2.5F, -1.5F, -1.5F, 1.0F, 3.0F, 3.0F, new CubeDeformation(0.0F))
		.texOffs(0, 12).addBox(1.5F, -1.5F, -1.5F, 1.0F, 3.0F, 3.0F, new CubeDeformation(0.0F))
		.texOffs(16, 4).addBox(-1.5F, -1.5F, 1.5F, 3.0F, 3.0F, 1.0F, new CubeDeformation(0.0F))
		.texOffs(12, 0).addBox(-1.5F, -1.5F, -2.5F, 3.0F, 3.0F, 1.0F, new CubeDeformation(0.0F))
		.texOffs(0, 0).addBox(-3.0F, -0.5F, -0.5F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F))
		.texOffs(0, 2).addBox(2.0F, -0.5F, -0.5F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F))
		.texOffs(20, 0).addBox(-0.5F, -0.5F, -3.0F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F))
		.texOffs(20, 2).addBox(-0.5F, -0.5F, 2.0F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F))
		.texOffs(5, 13).addBox(-0.5F, -3.0F, -0.5F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F))
				.texOffs(13, 14).addBox(-0.5F, 2.0F, -0.5F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 2.2F, 0.0F));

		return LayerDefinition.create(meshdefinition, 32, 32);
	}

}
