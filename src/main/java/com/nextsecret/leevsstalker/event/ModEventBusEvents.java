package com.nextsecret.leevsstalker.event;

import com.nextsecret.leevsstalker.LeevsStalkerMod;
import com.nextsecret.leevsstalker.entity.ModEntities;
import com.nextsecret.leevsstalker.entity.client.StalkerModel;
import com.nextsecret.leevsstalker.entity.custom.StalkerEntity;

import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.EntityRenderersEvent;
import net.neoforged.neoforge.event.entity.EntityAttributeCreationEvent;

@EventBusSubscriber(modid = LeevsStalkerMod.MODID, bus = EventBusSubscriber.Bus.MOD)
public class ModEventBusEvents {
	
	@SubscribeEvent
	public static void registerLayers(EntityRenderersEvent.RegisterLayerDefinitions event) {
		event.registerLayerDefinition(StalkerModel.LAYER_LOCATION, StalkerModel::createBodyLayer);
	}
	
	@SubscribeEvent
	public static void registerAttributes(EntityAttributeCreationEvent event) {
		event.put(ModEntities.STALKER.get(), StalkerEntity.createAttributes().build());
	}
	
}
