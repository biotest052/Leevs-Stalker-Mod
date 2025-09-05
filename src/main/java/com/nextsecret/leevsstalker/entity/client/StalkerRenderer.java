package com.nextsecret.leevsstalker.entity.client;

import org.joml.Matrix4f;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.nextsecret.leevsstalker.LeevsStalkerMod;
import com.nextsecret.leevsstalker.entity.custom.StalkerEntity;

import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.renderer.entity.EntityRendererProvider.Context;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.client.renderer.texture.OverlayTexture;
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
		 
		 poseStack.mulPose(this.entityRenderDispatcher.cameraOrientation());

		 poseStack.scale(3.0F, 3.0F, 3.0F);
		 
		 VertexConsumer vc = buffer.getBuffer(RenderType.entityTranslucent(getTextureLocation(entity)));
	     Matrix4f matrix = poseStack.last().pose();
	     
	     int argb = 0xFFFFFFFF;
	     
	     float u0 = 0f, v0 = 0f, u1 = 1f, v1 = 1f;
	     float nx = 0f, ny = 0f, nz = 1f;
	     
	     vc.addVertex(-1f, -1f, 0f, argb, u0, v1, OverlayTexture.NO_OVERLAY, packedLight, nx, ny, nz);
	     vc.addVertex(-1f,  1f, 0f, argb, u0, v0, OverlayTexture.NO_OVERLAY, packedLight, nx, ny, nz);
	     vc.addVertex( 1f,  1f, 0f, argb, u1, v0, OverlayTexture.NO_OVERLAY, packedLight, nx, ny, nz);
	     vc.addVertex( 1f, -1f, 0f, argb, u1, v1, OverlayTexture.NO_OVERLAY, packedLight, nx, ny, nz);

	     poseStack.popPose();
	}
}
