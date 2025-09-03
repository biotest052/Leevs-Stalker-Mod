package com.nextsecret.leevsstalker;

import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.Mod;
import net.neoforged.neoforge.event.tick.PlayerTickEvent;

import java.util.Random;

public class StalkerHandler {
    private static final Random RANDOM = new Random();
    private static final int SPAWN_CHANCE = 5000;

    @SubscribeEvent
    public void handlePlayerTickPost(PlayerTickEvent.Post event) {
        Player player = event.getEntity();
        if (player.level().isClientSide()) return;

        ServerLevel serverLevel = (ServerLevel) player.level();

        if (RANDOM.nextInt(SPAWN_CHANCE) == 0) {
            BlockPos spawnPos = player.blockPosition().offset(
                    RANDOM.nextInt(10) - 5,
                    0,
                    RANDOM.nextInt(10) - 5
            );

            Mob stalker = EntityType.ZOMBIE.create(serverLevel);
            if (stalker != null) {
                stalker.setPos(spawnPos.getX() + 0.5, spawnPos.getY(), spawnPos.getZ() + 0.5);
                serverLevel.addFreshEntity(stalker);
            }

            BlockState blockState = LeevsStalkerMod.EXAMPLE_BLOCK.get().defaultBlockState();
            serverLevel.setBlock(spawnPos, blockState, 3);

            LeevsStalkerMod.LOGGER.info("Stalker spawned at: {} {} {}", spawnPos.getX(), spawnPos.getY(), spawnPos.getZ());
        }
    }
}
