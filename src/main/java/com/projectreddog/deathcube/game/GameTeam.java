package com.projectreddog.deathcube.game;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.entity.player.EntityPlayer;

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
	
	private int numPlayers = 0;
	private String teamColor;
	private int teamHandicap = 0;
	
	public GameTeam(String teamColor) {
		this.teamColor = teamColor;
	}
	
	public String getTeamColor() {
		return teamColor;
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
		numPlayers++;
	}
	
	/**
	 * Remove a player from this team.
	 */
	public void removePlayer(EntityPlayer player) {
		if(playerList.contains(player)){
			playerList.remove(player);
			numPlayers--;
		}
	}
	
	/**
	 * Check if Player is on this Team.  Used when a player comes into range of a Capture Point.
	 *  - TODO: Check:  Do EntityPlayers change over time?  Will the current player equal the one originally added to the list?
	 */
	public boolean isPlayerOnTeam(EntityPlayer player) {
		if(playerList.contains(player))
			return true;
		else
			return false;
	}
}
