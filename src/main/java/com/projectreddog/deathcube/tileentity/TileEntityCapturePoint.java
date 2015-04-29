package com.projectreddog.deathcube.tileentity;

import net.minecraft.command.IEntitySelector;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.server.gui.IUpdatePlayerListBox;

import com.projectreddog.deathcube.DeathCube;
import com.projectreddog.deathcube.init.ModNetwork;
import com.projectreddog.deathcube.network.MessageHandleTextUpdate;
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
	private boolean isActive = false;
	private boolean isBeingCaptured = false;
	private boolean isCaptured = false;
	private int numPlayersOnPoint = 0;
	private long pointTimerStart = 0, pointTimerCurrent = 0;
	private double remainingTime = 0;

	public TileEntityCapturePoint() {
		//Log.info("Capture Point Constructor");
	}

	public void onTextRequest() {
		if (this.worldObj != null) {
			if (!this.worldObj.isRemote) {
				Log.info("Server sending requested text. Team color: " + capturePointTeamColor);
				ModNetwork.sendToAll(new MessageHandleTextUpdate(this.pos, Reference.MESSAGE_FIELD1_ID, capturePointName));
				ModNetwork.sendToAll(new MessageHandleTextUpdate(this.pos, Reference.MESSAGE_FIELD2_ID, capturePointTeamColor));
				ModNetwork.sendToAll(new MessageHandleTextUpdate(this.pos, Reference.MESSAGE_FIELD3_ID, String.valueOf(captureRadius)));
				ModNetwork.sendToAll(new MessageHandleTextUpdate(this.pos, Reference.MESSAGE_FIELD4_ID, String.valueOf(captureOrderNumber)));
				ModNetwork.sendToAll(new MessageHandleTextUpdate(this.pos, Reference.MESSAGE_FIELD5_ID, String.valueOf(captureTime)));
			} else {
				Log.info("World is remote - text request.");
			}
		} else {
			Log.info("World object null - text request.");
		}
	}

	@Override
	public void onGuiTextfieldUpdate(int fieldID, String text) {
		/**
		 * Save Capture Point Data
		 * - textField1 = Point Name
		 * - textField2 = Point Team
		 * - textField3 = Point Radius
		 * - textField4 = Point Capture Order Number
		 */
		Log.info("Capture Point sees Text Update: " + text);
		if (fieldID == Reference.MESSAGE_FIELD1_ID) {
			capturePointName = text;
			if (!this.worldObj.isRemote) {
				/**
				 * If a server message (not remote), update the Clients too.
				 */
				ModNetwork.sendToAll(new MessageHandleTextUpdate(this.pos, Reference.MESSAGE_FIELD1_ID, capturePointName));
			}
			markDirty();
		} else if (fieldID == Reference.MESSAGE_FIELD2_ID) {
			capturePointTeamColor = text;
			Log.info("Capture Point set Team Color text to: " + text);
			if (!this.worldObj.isRemote) {
				/**
				 * If a server message (not remote), update the Clients too.
				 */
				ModNetwork.sendToAll(new MessageHandleTextUpdate(this.pos, Reference.MESSAGE_FIELD2_ID, capturePointTeamColor));
			}
			markDirty();
		} else if (fieldID == Reference.MESSAGE_FIELD3_ID) {
			try {
				captureRadius = Integer.parseInt(text);
				if (!this.worldObj.isRemote) {
					/**
					 * If a server message (not remote), update the Clients too.
					 */
					ModNetwork.sendToAll(new MessageHandleTextUpdate(this.pos, Reference.MESSAGE_FIELD3_ID, String.valueOf(captureRadius)));
				}
			} catch (NumberFormatException e) {
				Log.warn("Tried to parse non-Integer: " + text);
			}
			markDirty();
		} else if (fieldID == Reference.MESSAGE_FIELD4_ID) {
			try {
				captureOrderNumber = Integer.parseInt(text);
				if (!this.worldObj.isRemote) {
					/**
					 * If a server message (not remote), update the Clients too.
					 */
					ModNetwork.sendToAll(new MessageHandleTextUpdate(this.pos, Reference.MESSAGE_FIELD4_ID, String.valueOf(captureOrderNumber)));
				}
			} catch (NumberFormatException e) {
				Log.warn("Tried to parse non-Integer: " + text);
			}
			markDirty();
		} else if (fieldID == Reference.MESSAGE_FIELD5_ID) {
			try {
				captureTime = Integer.parseInt(text);
				if (!this.worldObj.isRemote) {
					/**
					 * If a server message (not remote), update the Clients too.
					 */
					ModNetwork.sendToAll(new MessageHandleTextUpdate(this.pos, Reference.MESSAGE_FIELD5_ID, String.valueOf(captureTime)));
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
	
	public double getRemainingCaptureTime() {
		return remainingTime;
	}

	public void setIsActive(boolean setPoint) {
		isActive = setPoint;
	}
	
	public boolean getIsActive() {
		return isActive;
	}
	
	public void setIsBeingCaptured(boolean setState) {
		isBeingCaptured = setState;
	}
	
	public boolean getIsBeingCaptured() {
		return isBeingCaptured;
	}
	
	public void setIsCaptured(boolean setState) {
		isCaptured = setState;
	}
	
	public boolean getIsCaptured() {
		return isCaptured;
	}

	@Override
	public void readFromNBT(NBTTagCompound tag) {
		super.readFromNBT(tag);
		capturePointName = tag.getString("name");
		capturePointTeamColor = tag.getString("team");
		captureRadius = tag.getInteger("radius");
		captureOrderNumber = tag.getInteger("order");
		captureTime = tag.getInteger("time");
		Log.info("Capture Point - NBT Read :: Team Color: " + capturePointTeamColor);
	}

	@Override
	public void writeToNBT(NBTTagCompound tag) {
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
		// Log.info("Capture Point Team: "+ getCapturePointTeamColor());
		if (!this.worldObj.isRemote && !isCaptured && isActive) {
			/**
			 * Check if players are nearby, only on Server-side.
			 * 
			 * If in radius and on proper Team, run count-down timer.
			 * - Needs to return a list of players within range.
			 * 
			 * Built-in function for Mob Spawner checks for any player not in Spectate mode. Copy class and
			 * add in check for Team. Also, track which player and how many players are on point.
			 * - See: BlockMobSpawner, TileEntityMobSpawner
			 */
			numPlayersOnPoint = nearbyTeamPlayers((double) this.pos.getX() + 0.5D, (double) this.pos.getY() + 0.5D, (double) this.pos.getZ() + 0.5D, (double) this.captureRadius + 0.5D);
			if (numPlayersOnPoint > 0) {
				if (isBeingCaptured) {
					/**
					 * Being Captured - Calculate Time Remaining
					 */
					pointTimerCurrent = System.currentTimeMillis();
					remainingTime = (double) captureTime - ((double) (pointTimerCurrent - pointTimerStart) / 1000);
					//Log.info("Player in range of Capture Point.  Time until Capture: " + String.format("%.2f", remainingTime));

					if (remainingTime <= 0) {
						isBeingCaptured = false;
						isActive = false;
						isCaptured = true;
						Log.info("Point Captured!");
					}
				} else {
					/**
					 * Starting to be Captured.
					 */
					isBeingCaptured = true;
					pointTimerStart = System.currentTimeMillis();
					pointTimerCurrent = pointTimerStart;
					remainingTime = captureTime - ((pointTimerCurrent - pointTimerStart) / 1000);
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

	public int nearbyTeamPlayers(double xPos, double yPos, double zPos, double radius) {
		int playersOnPoint = 0;

		for (int i = 0; i < this.worldObj.playerEntities.size(); ++i) {
			EntityPlayer entityplayer = (EntityPlayer) this.worldObj.playerEntities.get(i);

			/**
			 * For all EntityPlayers:
			 * - If not spectating, and
			 * - If within range, and
			 * - If on correct Team
			 */
			if (IEntitySelector.NOT_SPECTATING.apply(entityplayer)) {
				if (DeathCube.playerToTeamColor != null) {
					if (DeathCube.playerToTeamColor.get(entityplayer.getName()).equals(capturePointTeamColor)) {
						double d4 = entityplayer.getDistanceSq(xPos, yPos, zPos);

						if (radius < 0.0D || d4 < radius * radius) {
							playersOnPoint++;
						}
					}
				}
			}
		}

		return playersOnPoint;
	}
}
