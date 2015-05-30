package com.projectreddog.deathcube.entity;

import net.minecraft.entity.DataWatcher;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.IRangedAttackMob;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.EntityAIArrowAttack;
import net.minecraft.entity.ai.EntityAIWatchClosest;
import net.minecraft.entity.monster.EntityMob;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.DamageSource;
import net.minecraft.world.World;

import com.projectreddog.deathcube.reference.Reference;

public class EntityTurret extends EntityMob implements IRangedAttackMob {

	public float topRotation = 0;
	public int team = 1; // 1 = Red , 2 = Green , 3= Blue, 4 = yellow

	public int state = 0; // 0= not firing 1 = start of fire 60 = end of fire & needs rest to 0
	public static double MAX_TURN_RATE = 0.05d;
	public boolean hasTarget;

	public Entity target;

	public EntityTurret(World world) {
		super(world);
		this.setSize(1.0F, 1.0F);
		this.getDataWatcher().updateObject(20, this.team);
		this.tasks.addTask(0, new EntityAIArrowAttack(this, 0, 10, 10));// should act like a skely
		this.tasks.addTask(6, new EntityAIWatchClosest(this, EntityPlayer.class, 8.0F));
		// this.onUpdate();

	}

	public boolean isAIEnabled() {
		return true;
	}

	protected void applyEntityAttributes() {

		super.applyEntityAttributes();
		this.getEntityAttribute(SharedMonsterAttributes.followRange).setBaseValue(5d);
		this.getEntityAttribute(SharedMonsterAttributes.movementSpeed).setBaseValue(0d);
		this.getEntityAttribute(SharedMonsterAttributes.maxHealth).setBaseValue(20d);
		this.getEntityAttribute(SharedMonsterAttributes.attackDamage).setBaseValue(5d);

	}

	@Override
	public void onUpdate() {
		if (worldObj.isRemote) {
			DataWatcher dw = this.getDataWatcher();
			// this.topRotation = dw.getWatchableObjectFloat(22);
			// this.state = dw.getWatchableObjectInt(21);
			// this.team = dw.getWatchableObjectInt(20);

		} else {

			// server
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

			this.state = this.state + 1;
			if (this.state > Reference.TURRET_RECOIL_TICKS) {
				this.state = 0;
			}
			// this.getDataWatcher().updateObject(20, this.team);
			// this.getDataWatcher().updateObject(22, this.topRotation);
			// this.getDataWatcher().updateObject(21, this.state);

		}
	}

	@Override
	protected void entityInit() {
		// TODO Auto-generated method stub

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
		elb.attackEntityFrom(DamageSource.generic, 5);
	}
}
