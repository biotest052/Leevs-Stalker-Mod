package com.nextsecret.leevsstalker.event;

import com.nextsecret.leevsstalker.LeevsStalkerMod;
import com.nextsecret.leevsstalker.entity.custom.StalkerEntity;

import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.ServerChatEvent;

@EventBusSubscriber(modid = LeevsStalkerMod.MODID, bus = EventBusSubscriber.Bus.MOD)
public class StalkerChatListener 
{
	@SubscribeEvent
    public static void onPlayerChat(ServerChatEvent event) {
		String message = event.getRawText();
	    var player = event.getPlayer();
	    var level = player.level();

	    var stalkers = level.getEntitiesOfClass(StalkerEntity.class, player.getBoundingBox().inflate(64));
	    
	    if (!stalkers.isEmpty()) {
		    if (message.toLowerCase().contains("hello")) {
		        if (!level.isClientSide) {
	                StalkerEntity stalker = stalkers.get(0);

	                // Calculate distance
	                double distance = stalker.distanceTo(player);

	                int delayTicks = (int) Math.min(100, distance * 4);

	                stalker.sendFakeChatDelayed("What do you want.", delayTicks, distance);
	            }
	        }
		    else if (message.toLowerCase().contains("who are you")) {
		    	if (!level.isClientSide) {
	                StalkerEntity stalker = stalkers.get(0);

	                double distance = stalker.distanceTo(player);

	                int delayTicks = (int) Math.min(100, distance * 4);

	                stalker.sendFakeChatDelayed("Im the stalker.", delayTicks, distance);
	            }
		    }
		    else if (message.toLowerCase().contains("how are you")) {
		    	if (!level.isClientSide) {
	                StalkerEntity stalker = stalkers.get(0);

	                double distance = stalker.distanceTo(player);

	                int delayTicks = (int) Math.min(100, distance * 4);

	                stalker.sendFakeChatDelayed("What do you think.", delayTicks, distance);
	            }
		    }
		    else if (message.toLowerCase().contains("fuck you")) {
		    	if (!level.isClientSide) {
	                StalkerEntity stalker = stalkers.get(0);

	                double distance = stalker.distanceTo(player);

	                int delayTicks = (int) Math.min(100, distance * 4);

	                stalker.sendFakeChatDelayed("WHAT DID YOU SAY", delayTicks, distance);
	                stalker.sendFakeChatDelayed("I SEE YOU", delayTicks + 3, distance);
	                stalker.sendFakeChatDelayed("WHY DID YOU SAY THIS", delayTicks + 6, distance);
	                stalker.sendFakeChatDelayed("WHAT HAVE YOU DONE", delayTicks + 10, distance);
	            }
		    }
	    }
    }
}
