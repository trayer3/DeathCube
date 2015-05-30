package com.projectreddog.deathcube.entity;

import java.util.List;

import net.minecraft.entity.DataWatcher;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.world.World;

import com.projectreddog.deathcube.DeathCube;
import com.projectreddog.deathcube.reference.Reference;
import com.sun.org.apache.bcel.internal.generic.IXOR;

public class EntityTurret extends Entity {

	public float topRotation = 0;
	public int team = 1; // 1 = Red , 2 = Green , 3= Blue, 4 = yellow

	public int state = 0; // 0= not firing 1 = start of fire 60 = end of fire & needs rest to 0
	public static double MAX_TURN_RATE = 0.05d;
	public boolean hasTarget;

	public Entity target;

	public EntityTurret(World world) {
		super(world);
		this.setSize(1.0F, 1.0F);
		this.onUpdate();
		this.getDataWatcher().updateObject(20, this.team);

	}

	@Override
	public void onUpdate() {
		if (worldObj.isRemote) {
			DataWatcher dw = this.getDataWatcher();
			this.topRotation = dw.getWatchableObjectFloat(22);
			this.state = dw.getWatchableObjectInt(21);
			this.team = dw.getWatchableObjectInt(20);

		} else {

			// server
			if (target == null) {
				hasTarget = false;
			} else if (target instanceof EntityLiving) {
				if (((EntityLiving) target).getHealth() == 0) {
					hasTarget = false;
				}
			}
			if (!hasTarget) {
				// look for player in range.
				// look for players on server
				// 5x5
				int offset = 5;

				AxisAlignedBB aabb = new AxisAlignedBB(this.posX - offset, this.posY, this.posZ - offset, this.posX + offset, this.posY + 2, this.posZ + offset);

				List<EntityPlayer> Entities = worldObj.getEntitiesWithinAABB(EntityPlayer.class, aabb);

				for (EntityPlayer player : Entities) {
					if ( !hasTarget){
						for (int i = 0; i < DeathCube.gameTeams.length; i++) {
							if ( DeathCube.gameTeams[i].isPlayerOnTeam(player )){
								// 1 = Red , 2 = Green , 3= Blue, 4 = yellow
								if ( DeathCube.gameTeams[i].getTeamColor() =="Red" && this.team == 1 ){
									// on same team
								}else if ( DeathCube.gameTeams[i].getTeamColor() =="Green" && this.team == 2 ){
									// on same team
								}else if ( DeathCube.gameTeams[i].getTeamColor() =="Blue" && this.team == 3 ){
									// on same team
								}else if ( DeathCube.gameTeams[i].getTeamColor() =="Yellow" && this.team ==4 ){
									// on same team
								}	else {
									target = player;
									hasTarget = true;
								}
							}
						}
					}
				}

			}

		}

		
		 if (hasTarget){
			 target.posX
			 
			 
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
		this.getDataWatcher().updateObject(20, this.team);
		this.getDataWatcher().updateObject(22, this.topRotation);
		this.getDataWatcher().updateObject(21, this.state);

	}
}

@Override
protected void entityInit() {
	// TODO Auto-generated method stub

}

@Override
protected void readEntityFromNBT(NBTTagCompound tagCompund) {
	// TODO Auto-generated method stub

}

@Override
protected void writeEntityToNBT(NBTTagCompound tagCompound) {
	// TODO Auto-generated method stub

}
}
