package com.projectreddog.deathcube.tileentity;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.server.gui.IUpdatePlayerListBox;

import com.projectreddog.deathcube.init.ModNetwork;
import com.projectreddog.deathcube.network.MessageHandleTextUpdate;
import com.projectreddog.deathcube.network.MessageRequestTextUpdate_Client;
import com.projectreddog.deathcube.reference.Reference;
import com.projectreddog.deathcube.utility.Log;

public class TileEntityCapturePoint extends TileEntityDeathCube implements IUpdatePlayerListBox {

	/**
	 * GUI Variables
	 */
	public String capturePointTeamColor = "Red";
	public String capturePointName = "Point 1";
	public int captureOrderNumber = 1;
	public int captureRadius = 1;
	public int captureTime = 5;
	
	/**
	 * Tile Entity Variables
	 */
	public boolean isBeingCaptured = false;
	private int numPlayersOnPoint = 0;
	private long pointTimerStart, pointTimerCurrent;
	private double remainingTime;
	
	public TileEntityCapturePoint() {
		/**
		 * TODO: On creation or onBlockPlace, register with game.  Network packet?
		 * 
		 * Give/Get capture point ID.
		 * 
		 * Other values set by GUI.
		 */
		Log.info("Capture Point Constructor");
	}
	
	public void onTextRequest() {
		if(this.worldObj != null) {
			if(!this.worldObj.isRemote) {
				Log.info("Server sending requested text. Team color: " + capturePointTeamColor);
					ModNetwork.simpleNetworkWrapper.sendToAll(new MessageHandleTextUpdate(this.pos, Reference.MESSAGE_FIELD1_ID, capturePointName));
					ModNetwork.simpleNetworkWrapper.sendToAll(new MessageHandleTextUpdate(this.pos, Reference.MESSAGE_FIELD2_ID, capturePointTeamColor));
					ModNetwork.simpleNetworkWrapper.sendToAll(new MessageHandleTextUpdate(this.pos, Reference.MESSAGE_FIELD3_ID, String.valueOf(captureRadius)));
					ModNetwork.simpleNetworkWrapper.sendToAll(new MessageHandleTextUpdate(this.pos, Reference.MESSAGE_FIELD4_ID, String.valueOf(captureOrderNumber)));
					ModNetwork.simpleNetworkWrapper.sendToAll(new MessageHandleTextUpdate(this.pos, Reference.MESSAGE_FIELD5_ID, String.valueOf(captureTime)));
			} else {
				Log.info("World is remote - text request.");
			}
		} else {
			Log.info("World object null - text request.");
		}
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
        } else if (fieldID == Reference.MESSAGE_FIELD5_ID) {
        	try {
        		captureTime = Integer.parseInt(text);
        		if(!this.worldObj.isRemote) {
    				/**
    				 * If a server message (not remote), update the Clients too.
    				 */
        			ModNetwork.simpleNetworkWrapper.sendToAll(new MessageHandleTextUpdate(this.pos, Reference.MESSAGE_FIELD5_ID, String.valueOf(captureTime)));
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
	
	public int getCaptureTime() {
		return captureTime;
	}
	
	@Override
    public void readFromNBT(NBTTagCompound tag){
        super.readFromNBT(tag);
        capturePointName = tag.getString("name");        
        capturePointTeamColor = tag.getString("team");
        captureRadius = tag.getInteger("radius");        
        captureOrderNumber = tag.getInteger("order");
        captureTime = tag.getInteger("time");
        Log.info("Capture Point - NBT Read :: Team Color: " + capturePointTeamColor);
    }

    @Override
    public void writeToNBT(NBTTagCompound tag){
        super.writeToNBT(tag);
        tag.setString("name", capturePointName);
        tag.setString("team", capturePointTeamColor);
        tag.setInteger("radius", captureRadius);
        tag.setInteger("order", captureOrderNumber);
        tag.setInteger("time", captureTime);
        Log.info("Capture Point - NBT Write :: Team Color: " + capturePointTeamColor);
    }
	
	@Override
	public void update() {
		//Log.info("Capture Point Team: "+ getCapturePointTeamColor());
		if(!this.worldObj.isRemote) {
			/**
			 * Check if players are nearby, only on Server-side.  
			 * 
			 * If in radius and on proper Team, run count-down timer.
			 * 
			 * Built-in function for Mob Spawner checks for any player not in Spectate mode.  Copy class and
			 * 		add in check for Team.  Also, track which player and how many players are on point.
			 * 		See:  BlockMobSpawner
			 * 			  TileEntityMobSpawner
			 */
			if(this.worldObj.func_175636_b((double)this.pos.getX() + 0.5D, (double)this.pos.getY() + 0.5D, (double)this.pos.getZ() + 0.5D, (double)this.captureRadius + 0.5D)) {
				if(isBeingCaptured) {
					/**
					 * Being Captured - Calculate Time Remaining
					 */
					pointTimerCurrent = System.currentTimeMillis();
					remainingTime = (double)captureTime - ((double)(pointTimerCurrent - pointTimerStart)/1000);
					Log.info("Player in range of Capture Point.  Time until Capture: " + remainingTime);
					
					if(remainingTime <= 0) {
						// Reset timer when captured - for Debug.
						isBeingCaptured = false;
					}
				} else {
					/**
					 * Starting to be Captured.
					 */
					isBeingCaptured = true;
					pointTimerStart = System.currentTimeMillis();
					pointTimerCurrent = pointTimerStart;
					remainingTime = captureTime - ((pointTimerCurrent - pointTimerStart)/1000);
					Log.info("Player in range of Capture Point.  Time until Capture: " + remainingTime);
				}
			} else {
				/**
				 * If no one is on the point, it is not being captured.
				 */
				isBeingCaptured = false;
			}
		}
	}
}