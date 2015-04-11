package com.projectreddog.deathcube.tileentity;

import net.minecraft.server.gui.IUpdatePlayerListBox;

import com.projectreddog.deathcube.reference.Reference;
import com.projectreddog.deathcube.utility.Log;

public class TileEntityCapturePoint extends TileEntityDeathCube implements IUpdatePlayerListBox {

	//public int capturePointID;
	public int captureOrderNumber;
	public int captureRadius;
	public String capturePointTeamColor = "Red";
	public String capturePointName;
	//public boolean isActive;
	
	private int numPlayersOnPoint = 0;
	private int countDownTimer;
	
	public TileEntityCapturePoint() {
		/**
		 * TODO: On creation or onBlockPlace, register with game.  Network packet?
		 * 
		 * Give/Get capture point ID.
		 * 
		 * Other values set by GUI.
		 */
		//capturePointTeamColor = "Red";
		capturePointName = "Point 1";
		captureRadius = 1;
		captureOrderNumber = 1;
	}
	
	@Override
    public void onGuiTextfieldUpdate(int fieldID, String text){
		/**
		 * Save Capture Point Data
		 *  - textField1 = Point Name
		 *  - textField2 = Point Team
		 *  - textField3 = Point Radius
		 *  - textField4 = Point Capture Order Number
		 */
		Log.info("Capture Point sees Text Update: " + text);
		if(fieldID == Reference.MESSAGE_FIELD1_ID) {
			capturePointName = text;
            markDirty();
        } else if (fieldID == Reference.MESSAGE_FIELD2_ID) {
        	capturePointTeamColor = text;
        	Log.info("Capture Point set Team Color text to: " + text);
            markDirty();
        } else if (fieldID == Reference.MESSAGE_FIELD3_ID) {
        	captureRadius = Integer.parseInt(text);
            markDirty();
        } else if (fieldID == Reference.MESSAGE_FIELD4_ID) {
        	captureOrderNumber = Integer.parseInt(text);
            markDirty();
        }
    }

	public String getCapturePointName() {
		return capturePointName;
	}
	
	public String getCapturePointTeamColor() {
		return capturePointTeamColor;
	}
	
	public int getCaptureRadius() {
		return captureRadius;
	}
	
	public int getCaptureOrderNumber() {
		return captureOrderNumber;
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
