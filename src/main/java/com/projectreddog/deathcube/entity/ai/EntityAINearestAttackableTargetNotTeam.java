package com.projectreddog.deathcube.entity.ai;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import net.minecraft.command.IEntitySelector;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityCreature;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.ai.EntityAINearestAttackableTarget;
import net.minecraft.entity.ai.EntityAITarget;
import net.minecraft.entity.player.EntityPlayer;

import com.google.common.base.Predicate;
import com.google.common.base.Predicates;
import com.projectreddog.deathcube.DeathCube;
import com.projectreddog.deathcube.reference.Reference;
import com.projectreddog.deathcube.utility.Log;

public class EntityAINearestAttackableTargetNotTeam extends EntityAITarget {
	protected final Class targetClass;
	private final int targetChance;
	/** Instance of EntityAINearestAttackableTargetSorter. */
	protected final EntityAINearestAttackableTarget.Sorter theNearestAttackableTargetSorter;
	/**
	 * This filter is applied to the Entity search. Only matching entities will be targetted. (null -> no
	 * restrictions)
	 */
	protected Predicate targetEntitySelector;
	protected EntityLivingBase targetEntity;
	public int team;

	public EntityAINearestAttackableTargetNotTeam(EntityCreature p_i45878_1_, Class p_i45878_2_, boolean p_i45878_3_, int team) {
		this(p_i45878_1_, p_i45878_2_, p_i45878_3_, false, team);
	}

	public EntityAINearestAttackableTargetNotTeam(EntityCreature p_i45879_1_, Class p_i45879_2_, boolean p_i45879_3_, boolean p_i45879_4_, int team) {
		this(p_i45879_1_, p_i45879_2_, 10, p_i45879_3_, p_i45879_4_, (Predicate) null, team);
	}

	public EntityAINearestAttackableTargetNotTeam(EntityCreature p_i45880_1_, Class p_i45880_2_, int p_i45880_3_, boolean p_i45880_4_, boolean p_i45880_5_, final Predicate p_i45880_6_, int team) {
		super(p_i45880_1_, p_i45880_4_, p_i45880_5_);
		this.targetClass = p_i45880_2_;
		this.targetChance = p_i45880_3_;
		this.team = team;
		this.theNearestAttackableTargetSorter = new EntityAINearestAttackableTarget.Sorter(p_i45880_1_);
		this.setMutexBits(1);
		this.targetEntitySelector = new Predicate() {
			private static final String __OBFID = "CL_00001621";

			public boolean func_179878_a(EntityLivingBase p_179878_1_) {
				if (p_i45880_6_ != null && !p_i45880_6_.apply(p_179878_1_)) {
					return false;
				} else {
					if (p_179878_1_ instanceof EntityPlayer) {
						double d0 = EntityAINearestAttackableTargetNotTeam.this.getTargetDistance();

						if (p_179878_1_.isSneaking()) {
							d0 *= 0.800000011920929D;
						}

						if (p_179878_1_.isInvisible()) {
							float f = ((EntityPlayer) p_179878_1_).getArmorVisibility();

							if (f < 0.1F) {
								f = 0.1F;
							}

							d0 *= (double) (0.7F * f);
						}

						if ((double) p_179878_1_.getDistanceToEntity(EntityAINearestAttackableTargetNotTeam.this.taskOwner) > d0) {
							return false;
						}
					}

					return EntityAINearestAttackableTargetNotTeam.this.isSuitableTarget(p_179878_1_, false);
				}
			}

			public boolean apply(Object p_apply_1_) {
				return this.func_179878_a((EntityLivingBase) p_apply_1_);
			}
		};
	}

	protected boolean isSuitableTarget(EntityLivingBase entityLivingBase, boolean bool) {

		boolean opposingTeam = true;

		if (entityLivingBase instanceof EntityPlayer) {// 1 = Red , 2 = Green , 3= Blue, 4 = yellow
			if (DeathCube.playerToTeamColor != null && DeathCube.gameState == Reference.GameStates.Running) {
				if ((DeathCube.playerToTeamColor.get(((EntityPlayer) entityLivingBase).getName()).equalsIgnoreCase("Red") && this.team != 1) || (DeathCube.playerToTeamColor.get(((EntityPlayer) entityLivingBase).getName()).equalsIgnoreCase("Green") && this.team != 2) || (DeathCube.playerToTeamColor.get(((EntityPlayer) entityLivingBase).getName()).equalsIgnoreCase("Blue") && this.team != 3)
						|| (DeathCube.playerToTeamColor.get(((EntityPlayer) entityLivingBase).getName()).equalsIgnoreCase("Yellow") && this.team != 4)) {
					// player target is on a different team !
					opposingTeam = true;
					Log.info("Set to true" + " Team:" + this.team + " ELB team:" + DeathCube.playerToTeamColor.get(((EntityPlayer) entityLivingBase).getName()));
				} else {
					// player on same team re-set target?
					opposingTeam = false;
					Log.info("Set to FALSE" + " Team:" + this.team + " ELB team:" + DeathCube.playerToTeamColor.get(((EntityPlayer) entityLivingBase).getName()));

				}

			} else {

				// Log.info("false" + " Team:" + this.team + " ELB team:" + DeathCube.playerToTeamColor.get(((EntityPlayer) entityLivingBase).getName()));

				opposingTeam = false;
			}

		}
		return super.isSuitableTarget(entityLivingBase, bool) & opposingTeam;

	}

	/**
	 * Returns whether the EntityAIBase should begin execution.
	 */
	public boolean shouldExecute() {
		if (this.targetChance > 0 && this.taskOwner.getRNG().nextInt(this.targetChance) != 0) {
			return false;
		} else {
			double d0 = this.getTargetDistance();
			List list = this.taskOwner.worldObj.getEntitiesWithinAABB(this.targetClass, this.taskOwner.getEntityBoundingBox().expand(d0, 4.0D, d0), Predicates.and(this.targetEntitySelector, IEntitySelector.NOT_SPECTATING));
			Collections.sort(list, this.theNearestAttackableTargetSorter);

			if (list.isEmpty()) {
				return false;
			} else {
				this.targetEntity = (EntityLivingBase) list.get(0);
				return true;
			}
		}
	}

	/**
	 * Execute a one shot task or start executing a continuous task
	 */
	public void startExecuting() {
		this.taskOwner.setAttackTarget(this.targetEntity);
		super.startExecuting();
	}

	public static class Sorter implements Comparator {
		private final Entity theEntity;
		private static final String __OBFID = "CL_00001622";

		public Sorter(Entity p_i1662_1_) {
			this.theEntity = p_i1662_1_;
		}

		public int compare(Entity p_compare_1_, Entity p_compare_2_) {
			double d0 = this.theEntity.getDistanceSqToEntity(p_compare_1_);
			double d1 = this.theEntity.getDistanceSqToEntity(p_compare_2_);
			return d0 < d1 ? -1 : (d0 > d1 ? 1 : 0);
		}

		public int compare(Object p_compare_1_, Object p_compare_2_) {
			return this.compare((Entity) p_compare_1_, (Entity) p_compare_2_);
		}
	}
}
