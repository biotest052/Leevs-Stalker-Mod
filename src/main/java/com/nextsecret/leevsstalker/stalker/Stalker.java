package com.nextsecret.leevsstalker.stalker;

import net.minecraft.client.model.EntityModel;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.CubeDeformation;
import net.minecraft.client.model.geom.builders.CubeListBuilder;
import net.minecraft.client.model.geom.builders.LayerDefinition;
import net.minecraft.client.model.geom.builders.MeshDefinition;
import net.minecraft.client.model.geom.builders.PartDefinition;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.Difficulty;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;

import java.util.EnumSet;
import java.util.Random;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;

//ENTITYLOGIC

public class Stalker extends PathfinderMob {

	private int stareTicks = 0;
	private static boolean despawnedThisNight = false;

	public Stalker(EntityType<? extends PathfinderMob> type, Level level) {
		super(type, level);
	}

	@Override
	protected void registerGoals(){
		this.goalSelector.getAvailableGoals().clear();
		this.targetSelector.getAvailableGoals().clear();
		this.goalSelector.addGoal(0, new StalkPlayerGoal(this, 1.2D, 15.0F, 32.0F));
	}

	@Override
	public MobType getMobType() {
		return MobType.UNDEFINED;
	}

	@Override
	public boolean isPersistenceRequired() {
		return true;
	}

	public static boolean checkSpawnRules(EntityType<Stalker> type, LevelAccessor level, Difficulty difficulty, MobSpawnType reason, BlockPos pos, Random random) {
		return ((Level) level).isNight() && !despawnedThisNight;
	}

	@Override
	public void tick() {
		super.tick();

		if (!this.level().isClientSide) {
			Player player = this.level().getNearestPlayer(this, 32.0D);
			if (player != null) {
				boolean looking = StalkPlayerGoal.isPlayerLookingAtMob(player, this);

				if (looking) {
					stareTicks++;
					if (stareTicks >= 60) {
						this.level().playSound(null, this.blockPosition(),
						SoundEvents.ENDERMAN_SCREAM, SoundSource.HOSTILE,
						1.0F, 0.5F);

						despawnedThisNight = true;
						this.discard();
					}
				} else {
					stareTicks = 0;
				}
			}
		}

		//reset lock at day yup
		if (!this.level().isClientSide && this.level().isDay()) {
			despawnedThisNight = false;
		}
	}

	//inner goal class yup
	private static class StalkPlayerGoal extends Goal {
		private final PathfinderMob mob;
		private final double speedModifier;
		private final float minDist;
		private final float maxDist;
		private Player targetPlayer;

		public StalkPlayerGoal(PathfinderMob mob, double speedModifier, float minDist, float maxDist) {
			this.mob = mob;
			this.speedModifier = speedModifier;
			this.minDist = minDist;
			this.maxDist = maxDist;
			this.setFlags(EnumSet.of(Goal.Flag.MOVE, Goal.Flag.LOOK));
		}

		@Override
		public boolean canUse() {
			Player nearest = this.mob.level().getNearestPlayer(this.mob, maxDist);
			if (nearest != null) {
				this.targetPlayer = nearest;
				return true;
			}
			return false;
		}

		@Override
		public boolean canContinueToUse() {
			return this.targetPlayer != null && this.targetPlayer.isAlive()
			&& this.mob.distanceToSqr(this.targetPlayer) <= (double)(this.maxDist * this.minDist);
		}

		@Override
		public void tick() {
			if (this.targetPlayer == null) return;

			//always stareing yyup
			this.mob.getLookControl().setLookAt(this.targetPlayer, 30.0F, 30.0F);

			double dist = this.mob.distanceTo(this.targetPlayer);
			boolean playerLooking = isPlayerLookingAtMob(this.targetPlayer, this.mob);

			if (!playerLooking) {
				if (dist > maxDist) {
					this.mob.getNavigation().moveTo(this.targetPlayer, speedModifier);
				} else if (dist < minDist) {
					double awayX = this.mob.getX() - (this.targetPlayer.getX() - this.mob.getX());
                    	double awayZ = this.mob.getZ() - (this.targetPlayer.getZ() - this.mob.getZ());
					this.mob.getNavigation().moveTo(awayX, this.mob.getY(), awayZ, speedModifier);
				} else {
					this.mob.getNavigation().stop();
				}
			} else {
				this.mob.getNavigation().stop();
			}
		}

		@Override
		public void stop() {
			this.targetPlayer = null;
			this.mob.getNavigation().stop();
		}

		public static boolean isPlayerLookingAtMob(Player player, PathfinderMob mob) {
          	var playerLookVec = player.getLookAngle().normalize();
          	var directionToMob = mob.position().subtract(player.position()).normalize();
          	double dot = playerLookVec.dot(directionToMob);
          	return dot > 0.95D; // within ~30° cone
		}
	}
}

//ENTITYMODEL

public class StalkerModel<T extends Entity> extends EntityModel<T> {
    public static final ModelLayerLocation LAYER_LOCATION =
            new ModelLayerLocation(ResourceLocation.fromNamespaceAndPath("leevsstalker", "stalker"), "main");
    private final ModelPart bb_main;

    public StalkerModel(ModelPart root) {
        this.bb_main = root.getChild("bb_main");
    }

    public static LayerDefinition createBodyLayer() {
        MeshDefinition meshdefinition = new MeshDefinition();
        PartDefinition partdefinition = meshdefinition.getRoot();

        PartDefinition bb_main = partdefinition.addOrReplaceChild("bb_main",
                CubeListBuilder.create()
                        .texOffs(0, 0)
                        .addBox(-8.0F, -16.0F, 0.0F,
                                16.0F, 16.0F, 0.0F,
                                new CubeDeformation(0.0F)),
                PartPose.offset(0.0F, 24.0F, 0.0F));

        return LayerDefinition.create(meshdefinition, 16, 16);
    }

    @Override
    public void setupAnim(T entity, float limbSwing, float limbSwingAmount,
                          float ageInTicks, float netHeadYaw, float headPitch) {
        if (this.bb_main != null) {
            this.bb_main.yRot = netHeadYaw * ((float)Math.PI / 180F);
            this.bb_main.xRot = headPitch * ((float)Math.PI / 180F);
        }
    }

    @Override
    public void renderToBuffer(PoseStack poseStack, VertexConsumer buffer,
                               int packedLight, int packedOverlay, int color) {
        bb_main.render(poseStack, buffer, packedLight, packedOverlay, color);
    }
}