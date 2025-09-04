package com.nextsecret.leevsstalker;

import com.nextsecret.leevsstalker.entity.ModEntities;
import com.nextsecret.leevsstalker.entity.client.StalkerRenderer;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.entity.EntityRenderers;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.event.lifecycle.FMLClientSetupEvent;
import net.neoforged.neoforge.client.gui.ConfigurationScreen;
import net.neoforged.neoforge.client.gui.IConfigScreenFactory;

@Mod(value = LeevsStalkerMod.MODID, dist = Dist.CLIENT)
@EventBusSubscriber(modid = LeevsStalkerMod.MODID, value = Dist.CLIENT)
public class LeevsStalkerModClient {
    public LeevsStalkerModClient(ModContainer container) {
        container.registerExtensionPoint(IConfigScreenFactory.class, ConfigurationScreen::new);
    }

    @SubscribeEvent
    static void onClientSetup(FMLClientSetupEvent event) {
        LeevsStalkerMod.LOGGER.info("HELLO FROM CLIENT SETUP");
        LeevsStalkerMod.LOGGER.info("MINECRAFT NAME >> {}", Minecraft.getInstance().getUser().getName());
        
        EntityRenderers.register(ModEntities.STALKER.get(),
        	    context -> new StalkerRenderer(context, null, 0));
    }
}
