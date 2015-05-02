package com.projectreddog.deathcube.tileentity;

import com.projectreddog.deathcube.init.ModNetwork;
import com.projectreddog.deathcube.network.MessageHandleTextUpdate;
import com.projectreddog.deathcube.reference.Reference;
import com.projectreddog.deathcube.utility.Log;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.server.gui.IUpdatePlayerListBox;
import net.minecraft.tileentity.TileEntity;

public class TileEntitySpawnPoint extends TileEntityDeathCube implements IUpdatePlayerListBox {

	public String spawnPointTeamColor = "Red";
	
	public TileEntitySpawnPoint() {

	}
	
	public void onTextRequest() {
		if(this.worldObj != null) {
			if(!this.worldObj.isRemote) {
				Log.info("Server sending requested text. Spawn color: " + spawnPointTeamColor);
					ModNetwork.sendToAll(new MessageHandleTextUpdate(this.pos, Reference.MESSAGE_FIELD1_ID, spawnPointTeamColor));
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
		 * Save Spawn Point Data
		 */
		Log.info("Spawn Point sees Text Update: " + text);
		if(fieldID == Reference.MESSAGE_FIELD1_ID) {
			spawnPointTeamColor = text;
			if(!this.worldObj.isRemote) {
				/**
				 * If a server message (not remote), update the Clients too.
				 */
				ModNetwork.sendToAll(new MessageHandleTextUpdate(this.pos, Reference.MESSAGE_FIELD1_ID, spawnPointTeamColor));
			}
            markDirty();
        }
    }

	public String getSpawnPointTeamColor() {
		return spawnPointTeamColor;
	}
	
	@Override
    public void readFromNBT(NBTTagCompound tag){
        super.readFromNBT(tag);
        spawnPointTeamColor = tag.getString("team");        
        //Log.info("Spawn Point - NBT Read :: Team Color: " + spawnPointTeamColor);
    }

    @Override
    public void writeToNBT(NBTTagCompound tag){
        super.writeToNBT(tag);
        tag.setString("team", spawnPointTeamColor);
        //Log.info("Spawn Point - NBT Write :: Team Color: " + spawnPointTeamColor);
    }
	
	@Override
	public void update() {
		
	}
}
