package com.nextsecret.leevsstalker.entity.client;

import org.joml.Matrix4f;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
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
	public void render(StalkerEntity entity, float entityYaw, float partialTicks,
	                   PoseStack poseStack, MultiBufferSource buffer, int packedLight) {
	    poseStack.pushPose();

	    poseStack.translate(0.0D, 0.5D, 0.0D);

	    poseStack.mulPose(this.entityRenderDispatcher.cameraOrientation());

	    poseStack.mulPose(Axis.YP.rotationDegrees(180.0F));

	    float size = 3f;
	    poseStack.scale(size, size, size);

	    Matrix4f matrix = poseStack.last().pose();
	    VertexConsumer vc = buffer.getBuffer(RenderType.entityTranslucent(getTextureLocation(entity)));

	    int argb = 0xFFFFFFFF;
	    int overlay = OverlayTexture.NO_OVERLAY;

	    int blockLight = packedLight & 0xFFFF;
	    int skyLight   = (packedLight >> 16) & 0xFFFF;
	    
	    vc.addVertex(matrix, -1f, -1f, 0f).setColor(argb).setUv(0f, 1f).setOverlay(overlay).setUv2(blockLight, skyLight).setNormal(0,0,1);
	    vc.addVertex(matrix, -1f,  1f, 0f).setColor(argb).setUv(0f, 0f).setOverlay(overlay).setUv2(blockLight, skyLight).setNormal(0,0,1);
	    vc.addVertex(matrix,  1f,  1f, 0f).setColor(argb).setUv(1f, 0f).setOverlay(overlay).setUv2(blockLight, skyLight).setNormal(0,0,1);
	    vc.addVertex(matrix,  1f, -1f, 0f).setColor(argb).setUv(1f, 1f).setOverlay(overlay).setUv2(blockLight, skyLight).setNormal(0,0,1);

	    poseStack.popPose();
	}
}
