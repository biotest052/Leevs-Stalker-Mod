package com.nextsecret.leevsstalker;

import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.neoforge.event.tick.LevelTickEvent;

import java.util.List;
import java.util.Random;

import com.nextsecret.leevsstalker.entity.ModEntities;
import com.nextsecret.leevsstalker.entity.custom.StalkerEntity;

public class StalkerHandler {
    private static final Random RANDOM = new Random();

    private static final boolean TARGET_ALL_PLAYERS = false;

    @SubscribeEvent
    public void handleLevelTick(LevelTickEvent.Post event) {
        if (!(event.getLevel() instanceof ServerLevel serverLevel)) return;

        Integer spawnChance = Config.STALKER_SPAWN_CHANCE.get();
        if (RANDOM.nextInt(Math.max(spawnChance, 100)) != 0) return;

        List<ServerPlayer> players = serverLevel.players();
        if (players.isEmpty()) return;

        if (TARGET_ALL_PLAYERS) {
            for (Player target : players) {
                spawnStalkerNearPlayer(serverLevel, target);
            }
        } else {
            Player target = players.get(RANDOM.nextInt(players.size()));
            spawnStalkerNearPlayer(serverLevel, target);
        }
    }

    private void spawnStalkerNearPlayer(ServerLevel serverLevel, Player target) {
        BlockPos spawnPos = target.blockPosition().offset(
                RANDOM.nextInt(50) - 25,
                0,
                RANDOM.nextInt(50) - 25
        );

        StalkerEntity stalker = ModEntities.STALKER.get().create(serverLevel);
        if (stalker != null) {
            BlockPos safePos = spawnPos;
            for (int i = 0; i < 5; i++) {
                if (serverLevel.isEmptyBlock(safePos) && serverLevel.isEmptyBlock(safePos.above())) {
                    break;
                }
                safePos = spawnPos.offset(RANDOM.nextInt(5) - 2, 0, RANDOM.nextInt(5) - 2);
            }

            while (serverLevel.isEmptyBlock(safePos.below()) && safePos.getY() > 1) {
                safePos = safePos.below();
            }

            stalker.moveTo(
                    safePos.getX() + 0.5,
                    safePos.getY(),
                    safePos.getZ() + 0.5,
                    target.getYRot(),
                    0
            );

            serverLevel.addFreshEntity(stalker);

            LeevsStalkerMod.LOGGER.info("Stalker spawned near {} at: {} {} {}",
                    target.getName().getString(), safePos.getX(), safePos.getY(), safePos.getZ());
        }
    }
}