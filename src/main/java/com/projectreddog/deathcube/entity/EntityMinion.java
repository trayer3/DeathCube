package com.projectreddog.deathcube.entity;

import net.minecraft.entity.DataWatcher;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.IRangedAttackMob;
import net.minecraft.entity.ai.EntityAIArrowAttack;
import net.minecraft.entity.monster.EntityMob;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.world.World;

import com.projectreddog.deathcube.DeathCube;
import com.projectreddog.deathcube.reference.Reference;

public class EntityMinion extends EntityMob implements IRangedAttackMob {

	// TODO create a gameprofile with players name / id
	// TOOD create a networkplayerinfo(gameprofile) // use getLocationSkin in render code to get the texture ! (client only stuff)

	// TODO or getLocationSkin from AbstractClientPlayer (client only) can be cast from entityplayer ! ooooohhhh..
	public float topRotation = 0;
	public int team = 1; // 1 = Red , 2 = Green , 3= Blue, 4 = yellow
	private EntityAIArrowAttack aiArrowAttack = new EntityAIArrowAttack(this, 1.0D, 20, 60, 15.0F);

	public EntityPlayer player;
	public int state = 0; // 0= not firing 1 = start of fire 60 = end of fire & needs rest to 0
	public static double MAX_TURN_RATE = 0.05d;
	public boolean hasTarget;

	public Entity target;

	public EntityMinion(World world) {

		super(world);
		this.setSize(.90F, 1.7F);
		this.getDataWatcher().updateObject(20, this.team);

	}

	public EntityMinion(World world, EntityPlayer player) {

		super(world);
		this.setSize(.90F, 1.7F);
		this.getDataWatcher().updateObject(20, this.team);
		this.player = player;
		this.getDataWatcher().updateObject(21, this.player.getName());
	}

	@Override
	public void onUpdate() {
		super.onUpdate();
		if (worldObj.isRemote) {
			DataWatcher dw = this.getDataWatcher();
			this.team = dw.getWatchableObjectInt(20);
			if (dw.hasObjectChanged()) {
				// only update player if the object has changed because of the expense in looking up a player (assumption)
				if (dw.getWatchableObjectString(21) != null) {
					this.player = this.worldObj.getPlayerEntityByName(dw.getWatchableObjectString(21));
				}
			}

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
			if (this.player != null) {
				this.getDataWatcher().updateObject(21, this.player.getName());
			}

		}
	}

	public boolean isAIEnabled() {
		return true;
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
