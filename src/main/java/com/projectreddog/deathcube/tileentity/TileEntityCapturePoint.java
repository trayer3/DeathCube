package com.projectreddog.deathcube.tileentity;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.server.gui.IUpdatePlayerListBox;

import com.projectreddog.deathcube.init.ModNetwork;
import com.projectreddog.deathcube.network.MessageHandleTextUpdate;
import com.projectreddog.deathcube.reference.Reference;
import com.projectreddog.deathcube.utility.Log;

public class TileEntityCapturePoint extends TileEntityDeathCube implements IUpdatePlayerListBox {

	//public int capturePointID;
	public int captureOrderNumber;
	public int captureRadius;
	public String capturePointTeamColor = "Blue";
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
		Log.info("This is a client instance:" + this.worldObj.isRemote);
		if(fieldID == Reference.MESSAGE_FIELD1_ID) {
			capturePointName = text;
			if(!this.worldObj.isRemote) {
				/**
				 * If a server message (not remote), update the Clients too.
				 */
				ModNetwork.simpleNetworkWrapper.sendToAll(new MessageHandleTextUpdate(this.pos, Reference.MESSAGE_FIELD1_ID, capturePointName));
			}
            markDirty();
        } else if (fieldID == Reference.MESSAGE_FIELD2_ID) {
        	capturePointTeamColor = text;
        	Log.info("Capture Point set Team Color text to: " + text);
        	if(!this.worldObj.isRemote) {
				/**
				 * If a server message (not remote), update the Clients too.
				 */
				ModNetwork.simpleNetworkWrapper.sendToAll(new MessageHandleTextUpdate(this.pos, Reference.MESSAGE_FIELD2_ID, capturePointTeamColor));
			}
            markDirty();
        } else if (fieldID == Reference.MESSAGE_FIELD3_ID) {
        	try {
        		captureRadius = Integer.parseInt(text);
        		if(!this.worldObj.isRemote) {
    				/**
    				 * If a server message (not remote), update the Clients too.
    				 */
    				ModNetwork.simpleNetworkWrapper.sendToAll(new MessageHandleTextUpdate(this.pos, Reference.MESSAGE_FIELD3_ID, String.valueOf(captureRadius)));
    			}
        	} catch (NumberFormatException e) {
        		Log.warn("Tried to parse non-Integer: " + text);
        	}
            markDirty();
        } else if (fieldID == Reference.MESSAGE_FIELD4_ID) {
        	try {
        		captureOrderNumber = Integer.parseInt(text);
        		if(!this.worldObj.isRemote) {
    				/**
    				 * If a server message (not remote), update the Clients too.
    				 */
        			ModNetwork.simpleNetworkWrapper.sendToAll(new MessageHandleTextUpdate(this.pos, Reference.MESSAGE_FIELD4_ID, String.valueOf(captureOrderNumber)));
    			}
        	} catch (NumberFormatException e) {
        		Log.warn("Tried to parse non-Integer: " + text);
        	}
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
    public void readFromNBT(NBTTagCompound tag){
        super.readFromNBT(tag);
        capturePointName = tag.getString("name");
        capturePointTeamColor = tag.getString("team");
        captureRadius = tag.getInteger("radius");
        captureOrderNumber = tag.getInteger("order");
    }

    @Override
    public void writeToNBT(NBTTagCompound tag){
        super.writeToNBT(tag);
        tag.setString("name", capturePointName);
        tag.setString("team", capturePointTeamColor);
        tag.setInteger("radius", captureRadius);
        tag.setInteger("order", captureOrderNumber);
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
