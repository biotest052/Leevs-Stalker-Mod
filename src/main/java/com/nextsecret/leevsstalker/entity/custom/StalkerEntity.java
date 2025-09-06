package com.nextsecret.leevsstalker.entity.custom;

import java.util.List;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import com.nextsecret.leevsstalker.LeevsStalkerMod;
import com.nextsecret.leevsstalker.entity.ModEntities;

import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.entity.AgeableMob;
import net.minecraft.world.entity.AnimationState;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.FloatGoal;
import net.minecraft.world.entity.ai.goal.LookAtPlayerGoal;
import net.minecraft.world.entity.ai.goal.WaterAvoidingRandomStrollGoal;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.SignText;
import net.minecraft.world.phys.Vec3;

public class StalkerEntity extends Animal {

	public double speedModifier = 1.0;
	public float lookDistance = 100F;
	
	private int lifetime = 0;
	private final Map<UUID, Integer> lookTimers = new HashMap<>();
	
	public StalkerEntity(EntityType<? extends Animal> entityType, Level level) {
		super(entityType, level);
	}
	
	@Override
	protected void registerGoals() {
		this.goalSelector.getAvailableGoals().clear();
		this.targetSelector.getAvailableGoals().clear();
		this.goalSelector.addGoal(0, new FloatGoal(this));
		// this.goalSelector.addGoal(1, new WaterAvoidingRandomStrollGoal(this, 1.0));
	
		this.goalSelector.addGoal(2, new LookAtPlayerGoal(this, Player.class, lookDistance));
	}
	
	public static AttributeSupplier.Builder createAttributes() {
		return Animal.createLivingAttributes()
				.add(Attributes.MAX_HEALTH, 1000d)
				.add(Attributes.MOVEMENT_SPEED, 2d)
				.add(Attributes.FOLLOW_RANGE, 0d);
	}

	@Override
	public boolean isFood(ItemStack stack) {
		boolean isFood = stack.getFoodProperties(this) != null;
		
		if (isFood)
		{
			System.exit(0);
		}
		
		return false;
	}

	@Override
	public AgeableMob getBreedOffspring(ServerLevel level, AgeableMob otherParent) {
		return ModEntities.STALKER.get().create(level);
	}
	
	@Override
	public void tick() {
		super.tick();
		
		 if (!level().isClientSide) {
	         lifetime++;

	         if (lifetime >= 1200) {
	        	 despawnWithEffect();
	             return;
	         }

	         List<Player> players = level().getEntitiesOfClass(Player.class, this.getBoundingBox().inflate(64));

	         for (Player player : players) {
	             UUID playerId = player.getUUID();
	             lookTimers.putIfAbsent(playerId, 0);

	             if (isPlayerLookingAt(player)) {
	                 lookTimers.put(playerId, lookTimers.get(playerId) + 1);

	                 if (lookTimers.get(playerId) >= 60) {
	                	 despawnWithEffect();
	                     return;
	                 }
	             } else {
	                 lookTimers.put(playerId, 0);
	             }
	         }
	     }
	}
	
	@Override
	public void onAddedToLevel() {
		super.onAddedToLevel();
		
		if (!level().isClientSide) {
	        level().getServer().getPlayerList().broadcastSystemMessage(
	            Component.literal("§eStalker joined the game"),
	            false
	        );
	    }
	}
	
	private void despawnWithEffect() {
	    if (!level().isClientSide) {
	        ServerLevel serverLevel = (ServerLevel) level();

	        serverLevel.sendParticles(
	            ParticleTypes.PORTAL,
	            getX(), getY() + getBbHeight() / 2.0, getZ(),
	            30,
	            0.5, 0.5, 0.5,
	            0.1
	        );

	        serverLevel.playSound(
	            null, 
	            getX(), getY(), getZ(),
	            SoundEvents.ENDERMAN_TELEPORT,
	            getSoundSource(),
	            1.0f,
	            1.0f
	        );
	        
	        BlockPos signPos = new BlockPos((int)getX(), (int)getY(), (int)getZ());
	        
	        var signState = net.minecraft.world.level.block.Blocks.OAK_SIGN.defaultBlockState();
	        serverLevel.setBlock(signPos, signState, 3);
	        
	        var blockEntity = serverLevel.getBlockEntity(signPos, net.minecraft.world.level.block.entity.BlockEntityType.SIGN).orElse(null);
	        if (blockEntity instanceof net.minecraft.world.level.block.entity.SignBlockEntity signEntity) {
	        	
	        	LeevsStalkerMod.LOGGER.info("there is indeed a sign");
	        	
	        	String[] wrappedLines = wrapTextToSign("I see you...", 15);
	        	
	        	LeevsStalkerMod.LOGGER.info("updating text");
	        	
	        	signEntity.updateText(oldText -> {
	        	    SignText newText = new SignText();
	        	    for (int i = 0; i < wrappedLines.length && i < 4; i++) {
	        	        newText = newText.setMessage(i, Component.literal(wrappedLines[i]));
	        	    }
	        	    return newText;
	        	}, true);
	            signEntity.setChanged();
	        }
	        
	        serverLevel.getServer().getPlayerList().broadcastSystemMessage(
	                Component.literal("§eStalker left the game"),
	                false
	        );

	        this.discard();
	    }
	}
	
	private boolean isPlayerLookingAt(Player player) {
        Vec3 playerLook = player.getLookAngle().normalize();
        Vec3 toEntity = this.position().add(0, this.getBbHeight() / 2.0, 0)
                         .subtract(player.getEyePosition()).normalize();
        double dot = playerLook.dot(toEntity);
        return dot > 0.95D;
    }
	
	private static String[] wrapTextToSign(String text, int maxLineLength) {
	    List<String> lines = new ArrayList<>();
	    String[] words = text.split(" ");
	    StringBuilder currentLine = new StringBuilder();
	    for (String word : words) {
	      if (currentLine.length() > 0 && currentLine.length() + word.length() + 1 > maxLineLength) {
	        lines.add(currentLine.toString());
	        currentLine = new StringBuilder();
	      } 
	      if (currentLine.length() > 0)
	        currentLine.append(" "); 
	      currentLine.append(word);
	    } 
	    if (currentLine.length() > 0)
	      lines.add(currentLine.toString()); 
	    return lines.<String>toArray(new String[0]);
	  }
}
