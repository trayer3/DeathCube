package com.projectreddog.deathcube.game;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.BlockPos;
import net.minecraft.world.World;

import com.projectreddog.deathcube.DeathCube;
import com.projectreddog.deathcube.entity.EntityWaypoint;
import com.projectreddog.deathcube.tileentity.TileEntityCapturePoint;
import com.projectreddog.deathcube.utility.Log;

/**
 * Original DeathCube used this Class for:
 * - Spawn & DeathPenalty Locations
 * - Base Force Field
 * - Chat and Armor Color
 * - List of capture points, active point needed
 * - Team player count
 */

public class GameTeam {

	private List<EntityPlayer> playerList = new ArrayList<EntityPlayer>();
	private List<BlockPos> spawnPointsList = new ArrayList<BlockPos>();
	private List<BlockPos> capturePointsList = new ArrayList<BlockPos>();
	private List<BlockPos> pointsCapturedList = new ArrayList<BlockPos>();

	private String teamColor;
	private World worldObj;
	private int teamHandicap = 0;
	private int currentCaptureIndex = 0;
	private int nextCaptureIndex = 0;
	
	private EntityWaypoint waypoint;
	private boolean needsSpawned = true;

	public GameTeam(String teamColor, World inWorldObj) {
		this.teamColor = teamColor;
		this.worldObj = inWorldObj;
		this.waypoint = new EntityWaypoint(inWorldObj);
	}

	public String getTeamColor() {
		return teamColor;
	}

	public int getTeamSize() {
		return playerList.size();
	}

	/**
	 * If teams are uneven, enhance smaller team's equipment?
	 * - Return to normal if teams are no longer uneven.
	 */
	public int getTeamHandicap() {
		return teamHandicap;
	}

	public void setTeamHandicap(int newHandicap) {
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
		if (playerList.contains(player)) {
			playerList.remove(player);
		}
	}

	/**
	 * Check if Player is on this Team. Used when a player comes into range of a Capture Point.
	 * - TODO: Check: Do EntityPlayers change over time? Will the current player equal the one originally added to the list?
	 */
	public boolean isPlayerOnTeam(EntityPlayer player) {
		if (playerList.contains(player)) {
			return true;
		} else {
			return false;
		}
	}
	
	public int getPlayerCount() {
		return playerList.size();
	}

	/**
	 * Just make these public static instead of these methods? Especially if there will ever be
	 * a need to clear and re-populate the list?
	 */
	public void addSpawnPoint(BlockPos spawnPoint) {
		this.spawnPointsList.add(spawnPoint);
	}

	public BlockPos getSpawnLocation() {
		int numSpawnPoints = spawnPointsList.size();
		if (numSpawnPoints > 1) {
			Log.info("Multiple spawn points found.");
			Random rand = new Random();
			return spawnPointsList.get(rand.nextInt(numSpawnPoints));
		} else {
			Log.info("One spawn point found.");
			return spawnPointsList.get(numSpawnPoints - 1);
		}
	}
	
	public int getNumSpawnPoints() {
		return spawnPointsList.size();
	}
	
	public void addCapturePoint(BlockPos capturePoint) {
		this.capturePointsList.add(capturePoint);
	}
	
	public int getNumCapturePoints() {
		return capturePointsList.size();
	}
	
	/**
	 * TODO:  Is there a better way to get the worldObj here?
	 */
	public void setNextCapturePointActive() {
		if(DeathCube.isOrderedCapture) {
			if(nextCaptureIndex < capturePointsList.size()) {
				Log.info("Setting a capture point as active for Team: " + teamColor);
				BlockPos capturePos = capturePointsList.get(nextCaptureIndex);
				TileEntityCapturePoint captureTE = (TileEntityCapturePoint) this.worldObj.getTileEntity(capturePos);
				captureTE.setIsActive(true);
				currentCaptureIndex = nextCaptureIndex;
				nextCaptureIndex++;
			}
		} else {
			/**
			 * Unordered captures allowed.
			 */
			setAllPointsActive(true);
		}
	}
	
	public int getCurrentCaptureIndex() {
		return currentCaptureIndex;
	}
	
	public BlockPos getCurrentPointPos() {
		if(capturePointsList != null && capturePointsList.size() != 0)
			return capturePointsList.get(currentCaptureIndex);
		else
			return null;
	}

	public boolean hasCapturedAllPoints() {
		boolean capturedAll = true;
		
		for(BlockPos pos : capturePointsList) {
			TileEntityCapturePoint captureTE = (TileEntityCapturePoint) this.worldObj.getTileEntity(pos);
			
			if(!captureTE.getIsCaptured()) {
				capturedAll = false;
			}
		}
		
		return capturedAll;
	}
	
	public void setAllPointsActive(boolean active) {
		for(BlockPos pos : capturePointsList) {
			TileEntityCapturePoint captureTE = (TileEntityCapturePoint) this.worldObj.getTileEntity(pos);
			captureTE.setIsActive(active);
			if(!active) {
				captureTE.setIsBeingCaptured(false);
				captureTE.setIsCaptured(false);
			}
		}
	}
	
	public void updateWaypoint() {
		if(getCurrentPointPos() != null) {
			double x = getCurrentPointPos().getX() + 0.5d;
			double y = getCurrentPointPos().getY() + 1.5d;
			double z = getCurrentPointPos().getZ() + 0.5d;
			
			waypoint.setPosition(x,y,z);
			
			if(needsSpawned) {
				boolean result = this.worldObj.spawnEntityInWorld(this.waypoint);
				if(result) {
					Log.info("Team " + teamColor + " Waypoint Spawned Successfully!");
				} else {
					Log.warn("Team " + teamColor + " Waypoint Failed to Spawn ...");
				}
				needsSpawned = false;
			}
		}
	}
	
	public void removeWaypoint() {
		waypoint.setDead();
	}
}
