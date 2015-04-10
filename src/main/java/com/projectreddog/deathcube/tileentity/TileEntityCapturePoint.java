package com.projectreddog.deathcube.tileentity;

import net.minecraft.server.gui.IUpdatePlayerListBox;
import net.minecraft.tileentity.TileEntity;

public class TileEntityCapturePoint extends TileEntity implements IUpdatePlayerListBox {

	public int capturePointID;
	public int captureOrderNumber;
	public int captureRadius;
	public String capturePointTeamColor;
	public String capturePointName;
	//public boolean isActive;
	
	private int numPlayersOnPoint = 0;
	private int countDownTimer;
	
	public TileEntityCapturePoint() {
		/**
		 * On creation or onBlockPlace, register with game.  Network packet?
		 * 
		 * Give/Get capture point ID.
		 * 
		 * Other values set by GUI.
		 */
	}

	@Override
	public void update() {
		/**
		 * Check if players are nearby.  
		 * 
		 * If in radius and on proper Team, run count-down timer.
		 */
		
	}
}
