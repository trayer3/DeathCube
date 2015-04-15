package com.projectreddog.deathcube.game;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.BlockPos;

import com.projectreddog.deathcube.tileentity.TileEntityCapturePoint;
import com.projectreddog.deathcube.tileentity.TileEntitySpawnPoint;
import com.projectreddog.deathcube.utility.Log;

/**
 * Original DeathCube used this Class for:
 *  - Spawn & DeathPenalty Locations
 *  - Base Force Field
 *  - Chat and Armor Color
 *  - List of capture points, active point needed
 *  - Team player count 
 * 
 *
 */

public class GameTeam {

	private List<EntityPlayer> playerList = new ArrayList<EntityPlayer>();
	private List<BlockPos> spawnPointsList = new ArrayList<BlockPos>();
	private List<BlockPos> capturePointsList = new ArrayList<BlockPos>();
	
	private String teamColor;
	private int teamHandicap = 0;
	
	public GameTeam(String teamColor) {
		this.teamColor = teamColor;
	}
	
	public String getTeamColor() {
		return teamColor;
	}
	
	public int getTeamSize() {
		return playerList.size();
	}
	
	/**
	 * If teams are uneven, enhance smaller team's equipment?
	 * 	- Return to normal if teams are no longer uneven.
	 */
	public int getTeamHandicap() {
		return teamHandicap;
	}
	
	public void setTeamHandicap(int newHandicap){
		this.teamHandicap = newHandicap;
	}
	
	/**
	 * Add a player to this team.
	 */
	public void addPlayer(EntityPlayer player) {
		playerList.add(player);
	}
	
	/**
	 * Remove a player from this team.
	 */
	public void removePlayer(EntityPlayer player) {
		if(playerList.contains(player)){
			playerList.remove(player);
		}
	}
	
	/**
	 * Check if Player is on this Team.  Used when a player comes into range of a Capture Point.
	 *  - TODO: Check:  Do EntityPlayers change over time?  Will the current player equal the one originally added to the list?
	 */
	public boolean isPlayerOnTeam(EntityPlayer player) {
		if(playerList.contains(player)){
			return true;
		} else {
			return false;
		}
	}
	
	
	/**
	 * Just make these public static instead of these methods?  Especially if there will ever be
	 * 		a need to clear and re-populate the list?
	 */
	public void addSpawnPoint(BlockPos spawnPoint) {
		this.spawnPointsList.add(spawnPoint);
	}
	
	public void addCapturePoint(BlockPos capturePoint) {
		this.capturePointsList.add(capturePoint);
	}

	public BlockPos getSpawnLocation() {
		int numSpawnPoints = spawnPointsList.size();
		if(numSpawnPoints > 1)
		{
			Random rand = new Random();
			return spawnPointsList.get(rand.nextInt(numSpawnPoints));
		} else {
			Log.info("One spawn point found.");
			return spawnPointsList.get(numSpawnPoints-1);
		}
		
	}
}
