package com.projectreddog.deathcube.entity;

import net.minecraft.entity.DataWatcher;
import net.minecraft.entity.Entity;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;

import com.projectreddog.deathcube.utility.Log;

public class EntityWaypoint extends Entity {

	// using a int instead of a string so we can qickly compare it in code .. comparisons of strings in if's should be more expensive
	// because this if will run in the rendering code it needs to run at least 30 times per second or more ( depending on the FPS) so speed is critical
	public int team = 1; // 1 = Red , 2 = Green , 3= Blue, 4 = yellow

	public boolean firstTick = true;

	public EntityWaypoint(World world, String TeamColor) {

		super(world);
		this.setSize(0.0F, 0.0F);
		this.ignoreFrustumCheck = true;
		if (TeamColor.equalsIgnoreCase("Red")) {
			this.team = 1;
		} else if (TeamColor.equalsIgnoreCase("Green")) {
			this.team = 2;
		} else if (TeamColor.equalsIgnoreCase("Blue")) {
			this.team = 3;
		} else if (TeamColor.equalsIgnoreCase("Yellow")) {
			this.team = 4;
		}
		if (!world.isRemote) {
			this.getDataWatcher().updateObject(20, this.team);

		}
		this.onUpdate();
		Log.info("WAYPOINT TEAM: " + this.team);
	}

	public EntityWaypoint(World world) {
		super(world);
		this.setSize(0.0F, 0.0F);
		this.ignoreFrustumCheck = true;
		this.onUpdate();

	}

	@Override
	public void onUpdate() {
		if (worldObj.isRemote) {
			DataWatcher dw = this.getDataWatcher();
			this.team = dw.getWatchableObjectInt(20);
		}
	}

	public void clientInit() {
		DataWatcher dw = this.getDataWatcher();
		this.team = dw.getWatchableObjectInt(20);
	}

	/**
	 * Checks if the entity is in range to render by using the past in distance and comparing it to its average edge
	 * length * 64 * renderDistanceWeight Args: distance
	 */
	public boolean isInRangeToRenderDist(double d) {
		return true;
	}

	@Override
	protected void entityInit() {

	}

	@Override
	protected void readEntityFromNBT(NBTTagCompound tagCompund) {

	}

	@Override
	protected void writeEntityToNBT(NBTTagCompound tagCompound) {

	}
}
