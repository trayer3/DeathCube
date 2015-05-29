package com.projectreddog.deathcube.entity;

import net.minecraft.entity.DataWatcher;
import net.minecraft.entity.Entity;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;

import com.projectreddog.deathcube.reference.Reference;

public class EntityTurret extends Entity {

	public float topRotation = 0;
	public int state = 0; // 0= not firing 1 = start of fire 60 = end of fire & needs rest to 0

	public EntityTurret(World world) {
		super(world);
		this.setSize(1.0F, 1.0F);
		this.onUpdate();

	}

	@Override
	public void onUpdate() {
		if (worldObj.isRemote) {
			DataWatcher dw = this.getDataWatcher();
			this.topRotation = dw.getWatchableObjectFloat(20);
			this.state = dw.getWatchableObjectInt(21);

		} else {

			// server
			this.topRotation += 1f;
			if (this.topRotation > 360) {
				this.topRotation = 0;
			}

			this.state = this.state + 1;
			if (this.state > Reference.TURRET_RECOIL_TICKS) {
				this.state = 0;
			}
			this.getDataWatcher().updateObject(20, this.topRotation);
			this.getDataWatcher().updateObject(21, this.state);

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
