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

import com.nextsecret.leevsstalker.entity.ModEntities;
import com.nextsecret.leevsstalker.entity.custom.StalkerEntity;

public class StalkerHandler {
    private static final Random RANDOM = new Random();

    @SubscribeEvent
    public void handlePlayerTickPost(PlayerTickEvent.Post event) {
        Player player = event.getEntity();
        if (player.level().isClientSide()) return;

        ServerLevel serverLevel = (ServerLevel) player.level();

        Integer spawnChance = Config.STALKER_SPAWN_CHANCE.get();
        
        if (RANDOM.nextInt(Math.clamp(spawnChance, 0, spawnChance)) == 0) {
            BlockPos spawnPos = player.blockPosition().offset(
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
                        player.getYRot(),
                        0
                );

                serverLevel.addFreshEntity(stalker);
            }
            
            // BlockState blockState = LeevsStalkerMod.EXAMPLE_BLOCK.get().defaultBlockState();
            // serverLevel.setBlock(spawnPos, blockState, 3);

            LeevsStalkerMod.LOGGER.info("Stalker spawned at: {} {} {}", spawnPos.getX(), spawnPos.getY(), spawnPos.getZ());
        }
    }
}
