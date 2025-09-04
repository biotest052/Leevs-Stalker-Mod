package com.nextsecret.leevsstalker.entity.client;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.nextsecret.leevsstalker.LeevsStalkerMod;

import net.minecraft.client.model.EntityModel;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.CubeDeformation;
import net.minecraft.client.model.geom.builders.CubeListBuilder;
import net.minecraft.client.model.geom.builders.LayerDefinition;
import net.minecraft.client.model.geom.builders.MeshDefinition;
import net.minecraft.client.model.geom.builders.PartDefinition;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.animal.Animal;

public class StalkerModel<T extends Animal> extends EntityModel<T> {
	public static final ModelLayerLocation LAYER_LOCATION =
            new ModelLayerLocation(ResourceLocation.fromNamespaceAndPath(LeevsStalkerMod.MODID, "stalker"), "main");
    private final ModelPart bb_main;

    public StalkerModel(ModelPart root) {
        this.bb_main = root.getChild("bb_main");
    }

    public static LayerDefinition createBodyLayer() {
        MeshDefinition mesh = new MeshDefinition();
        PartDefinition root = mesh.getRoot();

        root.addOrReplaceChild("bb_main",
            CubeListBuilder.create()
                .mirror(false)
                .texOffs(0, 0)
                .addBox(-8.0F, -16.0F, 0.0F,
                        16.0F, 16.0F, 0.0F,
                        new CubeDeformation(0.0F)),
            PartPose.offset(0.0F, 24.0F, 0.0F)
        );

        return LayerDefinition.create(mesh, 32, 32);
    }


    @Override
    public void setupAnim(T entity, float limbSwing, float limbSwingAmount,
                          float ageInTicks, float netHeadYaw, float headPitch) {
        if (this.bb_main != null) {
            this.bb_main.yRot = netHeadYaw * ((float)Math.PI / 180F);
            this.bb_main.xRot = headPitch * ((float)Math.PI / 180F);
        }
    }

    @Override
    public void renderToBuffer(PoseStack poseStack, VertexConsumer vertexConsumer,
                               int packedLight, int packedOverlay,
                               int color) {
        bb_main.render(poseStack, vertexConsumer, packedLight, packedOverlay, color);
    }
   
}
