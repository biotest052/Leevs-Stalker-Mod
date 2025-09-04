package com.nextsecret.leevsstalker.entity.client;

import com.mojang.blaze3d.vertex.PoseStack;
import com.nextsecret.leevsstalker.LeevsStalkerMod;
import com.nextsecret.leevsstalker.entity.custom.StalkerEntity;

import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.renderer.entity.EntityRendererProvider.Context;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.resources.ResourceLocation;

public class StalkerRenderer extends MobRenderer<StalkerEntity, StalkerModel<StalkerEntity>>{

	public StalkerRenderer(Context context, StalkerModel<StalkerEntity> model, float shadowRadius) {
		super(context, new StalkerModel<>(context.bakeLayer(StalkerModel.LAYER_LOCATION)), 0.25f);
	}

	@Override
	public ResourceLocation getTextureLocation(StalkerEntity entity) {
		return ResourceLocation.fromNamespaceAndPath(LeevsStalkerMod.MODID, "textures/entity/stalker/stalker_texture.png");
	}
	
	@Override
	public void render(StalkerEntity entity, float entityYaw, float partialTicks, PoseStack poseStack,
			MultiBufferSource buffer, int packedLight) {
		 poseStack.pushPose();

		 poseStack.scale(3.0F, 3.0F, 3.0F);

		 super.render(entity, entityYaw, partialTicks, poseStack, buffer, packedLight);

		 poseStack.popPose();
	}
}
