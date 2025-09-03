package com.nextsecret.leevsstalker;

import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.Mod;
import net.neoforged.neoforge.event.tick.PlayerTickEvent;

import java.util.Random;

public class StalkerHandler {
    private static final Random RANDOM = new Random();
    private static final int SPAWN_CHANCE = 5000; // Lower = more frequent

    @SubscribeEvent
    public void handlePlayerTickPost(PlayerTickEvent.Post event) {
        // after tick logic
    }
}
