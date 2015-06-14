package com.projectreddog.deathcube.entity;

import net.minecraft.entity.DataWatcher;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.EntityAIAttackOnCollide;
import net.minecraft.entity.ai.EntityAIHurtByTarget;
import net.minecraft.entity.ai.EntityAILookIdle;
import net.minecraft.entity.ai.EntityAIMoveTowardsRestriction;
import net.minecraft.entity.ai.EntityAISwimming;
import net.minecraft.entity.ai.EntityAIWander;
import net.minecraft.entity.ai.EntityAIWatchClosest;
import net.minecraft.entity.monster.EntityMob;
import net.minecraft.entity.monster.EntityPigZombie;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;

import com.projectreddog.deathcube.entity.ai.EntityAINearestAttackableTargetNotTeam;

public class EntityMinion extends EntityMob {

	public int team = 1; // 1 = Red , 2 = Green , 3= Blue, 4 = yellow

	public EntityPlayer player;
	public boolean hasTarget;
	public Entity target;

	public EntityMinion(World world) {

		super(world);
		this.setSize(.90F, 1.15F);
		this.getDataWatcher().updateObject(20, this.team);

	}

	public EntityMinion(World world, EntityPlayer player) {
		this(world);
		this.player = player;
		this.getDataWatcher().updateObject(21, this.player.getName());
		this.tasks.addTask(0, new EntityAISwimming(this));
		this.tasks.addTask(2, new EntityAIAttackOnCollide(this, EntityPlayer.class, 1.0D, false));
		this.tasks.addTask(2, this.field_175455_a);
		this.tasks.addTask(5, new EntityAIMoveTowardsRestriction(this, 0.750D));
		this.tasks.addTask(7, new EntityAIWander(this, 1.0D));
		this.tasks.addTask(8, new EntityAIWatchClosest(this, EntityPlayer.class, 8.0F));
		this.tasks.addTask(8, new EntityAILookIdle(this));
		this.applyEntityAI();
	}

	protected void applyEntityAI() {
		this.targetTasks.addTask(1, new EntityAIHurtByTarget(this, true, new Class[] { EntityPigZombie.class }));
		this.targetTasks.addTask(2, new EntityAINearestAttackableTargetNotTeam(this, EntityPlayer.class, true, team));
	}

	@Override
	public String getName() {
		// used in display
		if (this.player != null) {
			return "Mini-" + this.player.getName();
		} else
			return "";
	}

	protected void applyEntityAttributes() {
		super.applyEntityAttributes();
		this.getEntityAttribute(SharedMonsterAttributes.followRange).setBaseValue(35.0D);
		this.getEntityAttribute(SharedMonsterAttributes.movementSpeed).setBaseValue(0.35D);
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

			if (target == null) {
				hasTarget = false;
			} else if (target instanceof EntityLiving) {
				if (((EntityLiving) target).getHealth() == 0) {
					hasTarget = false;
				}
			}

			this.getDataWatcher().updateObject(20, this.team);
			if (this.player != null) {
				this.getDataWatcher().updateObject(21, this.player.getName());
			}

		}
	}

	@Override
	public boolean isChild() {
		return true;
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

}
