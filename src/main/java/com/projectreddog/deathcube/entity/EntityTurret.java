package com.projectreddog.deathcube.entity;

import net.minecraft.entity.DataWatcher;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.IRangedAttackMob;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.EntityAIArrowAttack;
import net.minecraft.entity.ai.EntityAIHurtByTarget;
import net.minecraft.entity.ai.EntityAINearestAttackableTarget;
import net.minecraft.entity.ai.EntityAIWatchClosest;
import net.minecraft.entity.monster.EntityMob;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.world.World;

import com.projectreddog.deathcube.DeathCube;
import com.projectreddog.deathcube.reference.Reference;

public class EntityTurret extends EntityMob implements IRangedAttackMob {

	public float topRotation = 0;
	public int team = 1; // 1 = Red , 2 = Green , 3= Blue, 4 = yellow
	private EntityAIArrowAttack aiArrowAttack = new EntityAIArrowAttack(this, 1.0D, 20, 60, 15.0F);

	public int state = 0; // 0= not firing 1 = start of fire 60 = end of fire & needs rest to 0
	public static double MAX_TURN_RATE = 0.05d;
	public boolean hasTarget;

	public Entity target;

	public EntityTurret(World world) {
		super(world);
		this.setSize(.90F, 1.7F);
		this.getDataWatcher().updateObject(20, this.team);
		this.tasks.addTask(0, new EntityAIArrowAttack(this, 0, 10, 10));// should act like a skely
		this.tasks.addTask(1, new EntityAIWatchClosest(this, EntityPlayer.class, 8.0F));
		this.targetTasks.addTask(1, new EntityAIHurtByTarget(this, false, new Class[0]));
		this.targetTasks.addTask(2, new EntityAINearestAttackableTarget(this, EntityPlayer.class, true));
		this.targetTasks.addTask(3, aiArrowAttack);
		this.jumpMovementFactor = 0;
		this.stepHeight = 0;

		// this.onUpdate();

	}

	public boolean isAIEnabled() {
		return true;
	}

	protected void applyEntityAttributes() {

		super.applyEntityAttributes();
		this.getEntityAttribute(SharedMonsterAttributes.followRange).setBaseValue(20d);
		this.getEntityAttribute(SharedMonsterAttributes.movementSpeed).setBaseValue(0d);
		this.getEntityAttribute(SharedMonsterAttributes.maxHealth).setBaseValue(20d);
		this.getEntityAttribute(SharedMonsterAttributes.attackDamage).setBaseValue(5d);
		this.getEntityAttribute(SharedMonsterAttributes.knockbackResistance).setBaseValue(10000d);
	}

	@Override
	public void onUpdate() {
		super.onUpdate();
		if (worldObj.isRemote) {
			DataWatcher dw = this.getDataWatcher();
			this.topRotation = dw.getWatchableObjectFloat(22);
			this.state = dw.getWatchableObjectInt(21);
			this.team = dw.getWatchableObjectInt(20);

		} else {

			// server
			if (this.motionY > 0) {
				this.motionY = 0;
				this.posY = this.lastTickPosY;
			}
			if (target == null) {
				hasTarget = false;
			} else if (target instanceof EntityLiving) {
				if (((EntityLiving) target).getHealth() == 0) {
					hasTarget = false;
				}
			}

			//
			this.topRotation += 1f;
			if (this.topRotation > 360) {
				this.topRotation = 0;
			}

			if (this.state > 0) {
				this.state = this.state + 1;
			}
			if (this.state > Reference.TURRET_RECOIL_TICKS) {
				this.state = 0;
			}
			this.getDataWatcher().updateObject(20, this.team);
			this.getDataWatcher().updateObject(22, this.topRotation);
			this.getDataWatcher().updateObject(21, this.state);

		}
	}

	@Override
	public void readEntityFromNBT(NBTTagCompound tagCompund) {
		// TODO Auto-generated method stub

	}

	@Override
	public void writeEntityToNBT(NBTTagCompound tagCompound) {
		// TODO Auto-generated method stub

	}

	@Override
	public void attackEntityWithRangedAttack(EntityLivingBase elb, float p_82196_2_) {
		// TODO Auto-generated method stub
		if (this.state == 0) {
			if (elb instanceof EntityPlayer) {// 1 = Red , 2 = Green , 3= Blue, 4 = yellow
				if (DeathCube.playerToTeamColor != null && DeathCube.gameState == Reference.GameStates.Running) {
					if ((DeathCube.playerToTeamColor.get(((EntityPlayer) elb).getName()).equalsIgnoreCase("Red") && this.team != 1) || (DeathCube.playerToTeamColor.get(((EntityPlayer) elb).getName()).equalsIgnoreCase("Green") && this.team != 2) || (DeathCube.playerToTeamColor.get(((EntityPlayer) elb).getName()).equalsIgnoreCase("Blue") && this.team != 3)
							|| (DeathCube.playerToTeamColor.get(((EntityPlayer) elb).getName()).equalsIgnoreCase("Yellow") && this.team != 4)) {
						// player target is on a different team !
						this.attackEntityAsMob(elb);
						this.state = 1;
						worldObj.spawnParticle(EnumParticleTypes.SMOKE_LARGE, this.posX, this.posY, this.posZ, 0, 0, 0, 0);
						worldObj.playSoundAtEntity(this, "random.explode", 1, 1.5f);
					} else {
						// player on same team re-set target?
						this.setAttackTarget(null);
					}

					return;
				}
			}
			// elb.attackEntityFrom(DamageSource.generic, 5);
			this.attackEntityAsMob(elb);
			this.state = 1;
			worldObj.spawnParticle(EnumParticleTypes.SMOKE_LARGE, this.posX, this.posY, this.posZ, 0, 0, 0, 0);
			worldObj.playSoundAtEntity(this, "random.explode", 1, 1.5f);

		}
	}
}
