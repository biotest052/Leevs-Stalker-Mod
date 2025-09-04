package com.nextsecret.leevsstalker.entity.custom;

import com.nextsecret.leevsstalker.entity.ModEntities;

import net.minecraft.server.level.ServerLevel;
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

public class StalkerEntity extends Animal {

	public double speedModifier = 1.0;
	public float lookDistance = 100F;
	
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
	}
}
