package com.projectreddog.deathcube.entity;

import net.minecraft.entity.DataWatcher;
import net.minecraft.entity.Entity;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;

public class EntityTurret extends Entity {

	public int topRotation = 0;

	public EntityTurret(World world) {
		super(world);
		this.setSize(1.0F, 1.0F);
		this.onUpdate();

	}

	@Override
	public void onUpdate() {
		if (worldObj.isRemote) {
			DataWatcher dw = this.getDataWatcher();
			this.topRotation = dw.getWatchableObjectInt(20);
		}
	}

	@Override
	protected void readEntityFromNBT(NBTTagCompound tagCompund) {

	}

	@Override
	protected void writeEntityToNBT(NBTTagCompound tagCompound) {

	}

	@Override
	protected void entityInit() {
		// TODO Auto-generated method stub

	}
}
