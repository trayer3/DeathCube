package com.projectreddog.deathcube.tileentity;

import net.minecraft.server.gui.IUpdatePlayerListBox;
import net.minecraft.tileentity.TileEntity;

public class TileEntitySpawnPoint extends TileEntityDeathCube implements IUpdatePlayerListBox {

	public int spawnPointID;
	public String spawnPointTeamColor;
	
	public TileEntitySpawnPoint() {

	}

	@Override
	public void update() {
		// TODO Auto-generated method stub
		
	}

	public Object getSpawnPointTeamColor() {
		return spawnPointTeamColor;
	}
}
