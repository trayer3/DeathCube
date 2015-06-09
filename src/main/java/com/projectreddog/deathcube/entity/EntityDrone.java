package com.projectreddog.deathcube.entity;

import net.minecraft.entity.Entity;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.BlockPos;
import net.minecraft.world.World;

public class EntityDrone extends Entity {

	public BlockPos targetPos;
	public boolean hasDeliveredPayload = false;

	public EntityDrone(World world) {
		super(world);
		this.setSize(.90F, 1.7F);
		this.noClip = true;
		// this.onUpdate();

	}

	public EntityDrone(World world, BlockPos targetPos) {
		super(world);
		this.setSize(.90F, 1.7F);
		this.noClip = true;
		this.targetPos = targetPos;
	}


	@Override
	public void onUpdate() {
		super.onUpdate();
		if (!this.worldObj.isRemote) {
			if (hasDeliveredPayload == false) {
				if (targetPos.getY() < this.posY) {
					this.posY = this.posY -.75d;
				} else {
					// fire !
					hasDeliveredPayload = true;
					EntityRPGRocket entityRPGRocket = new EntityRPGRocket(this.worldObj);

					float offsetHeight = -1f;

					// only run if open to sky
					entityRPGRocket.setLocationAndAngles(this.posX + .5d, this.posY + offsetHeight, this.posZ + .5d, 0, -90);
					entityRPGRocket.setVelocity(0, entityRPGRocket.getVelocity() * -1, 0);
					boolean rtn = this.worldObj.spawnEntityInWorld(entityRPGRocket);

					entityRPGRocket = new EntityRPGRocket(this.worldObj);
					entityRPGRocket.setLocationAndAngles(this.posX + .5d + 2, this.posY + offsetHeight, this.posZ + .5d, 0, -90);
					entityRPGRocket.setVelocity(0, entityRPGRocket.getVelocity() * -1, 0);
					rtn = rtn && this.worldObj.spawnEntityInWorld(entityRPGRocket);

					entityRPGRocket = new EntityRPGRocket(this.worldObj);
					entityRPGRocket.setLocationAndAngles(this.posX + .5d - 2, this.posY + offsetHeight, this.posZ + .5d, 0, -90);
					entityRPGRocket.setVelocity(0, entityRPGRocket.getVelocity() * -1, 0);
					rtn = rtn && this.worldObj.spawnEntityInWorld(entityRPGRocket);

					entityRPGRocket = new EntityRPGRocket(this.worldObj);
					entityRPGRocket.setLocationAndAngles(this.posX + .5d, this.posY + offsetHeight, this.posZ + .5d + 2, 0, -90);
					entityRPGRocket.setVelocity(0, entityRPGRocket.getVelocity() * -1, 0);
					rtn = rtn && this.worldObj.spawnEntityInWorld(entityRPGRocket);

					entityRPGRocket = new EntityRPGRocket(this.worldObj);
					entityRPGRocket.setLocationAndAngles(this.posX + .5d, this.posY + offsetHeight, this.posZ + .5d - 2, 0, -90);
					entityRPGRocket.setVelocity(0, entityRPGRocket.getVelocity() * -1, 0);
					rtn = rtn && this.worldObj.spawnEntityInWorld(entityRPGRocket);

				}
			} else {
				if (128 > this.posY) {
					//
this.posY = this.posY +.5d;
				} else {
					// out of world set to dead X X
					// ---
					this.setDead();
				}
			}
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
	protected void entityInit() {
		// TODO Auto-generated method stub
		
	}

}
