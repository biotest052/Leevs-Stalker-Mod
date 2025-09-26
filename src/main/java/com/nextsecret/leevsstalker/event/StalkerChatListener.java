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

	                stalker.sendFakeChatDelayed(". . .", delayTicks, distance);
	            }
	        }
		    else if (message.toLowerCase().contains("who are you")) {
		    	if (!level.isClientSide) {
	                StalkerEntity stalker = stalkers.get(0);

	                double distance = stalker.distanceTo(player);

	                int delayTicks = (int) Math.min(100, distance * 4);

	                stalker.sendFakeChatDelayed("LOST SOUL.", delayTicks, distance);
	            }
		    }
		    else if (message.toLowerCase().contains("how are you")) {
		    	if (!level.isClientSide) {
	                StalkerEntity stalker = stalkers.get(0);

	                double distance = stalker.distanceTo(player);

	                int delayTicks = (int) Math.min(100, distance * 4);

	                stalker.sendFakeChatDelayed(". . .", delayTicks, distance);
	            }
		    }
		    else if (message.toLowerCase().contains("friend?")) {
		    	if (!level.isClientSide) {
	                StalkerEntity stalker = stalkers.get(0);

	                double distance = stalker.distanceTo(player);

	                int delayTicks = (int) Math.min(100, distance * 4);

	                for (int i = 0; i <= 8; i += 4) {
		                stalker.sendFakeChatDelayed("§k§cNO", delayTicks + i, distance);
		                stalker.sendFakeChatDelayed("§k§cDIE", delayTicks + i + 1, distance);
		                stalker.sendFakeChatDelayed("§k§cI FEEL YOU", delayTicks + i + 2, distance);
		                stalker.sendFakeChatDelayed("§k§cWHAT HAVE YOU DONE", delayTicks + i + 3, distance);
	                }
	                stalker.sendFakeChatDelayed("LEAVE THE GAME", delayTicks + 9, distance);       
	            }
		    }
	    }
    }
}
