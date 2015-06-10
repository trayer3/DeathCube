package com.projectreddog.deathcube.stats;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import com.projectreddog.deathcube.utility.Log;

/**
 * 
 * A class to record and display statistics about each Player in the DeathCube Game.
 * 
 * Display:
 * - View 1: Player Names (rows) per Stats(Kills, Deaths, Captures, Average Time Alive, etc.) (columns)
 * - View 2: Player Names (rows) per Target(Players, Mobs?, Self?) (columns) = Amount Kills (cells)
 * - View 3: Player Names (rows) per Source(Players, Mobs?, Self?) (columns) = Amount Deaths (cells)
 * - View 4: Team, List of Points (rows) per Player Name / Blank (1 column)
 * 
 */

public class StatDeathCube {

	public static List<StatDeathCubePlayer> recordedPlayers;
	public static List<String> recordedPlayersNames;
	private int indexCurrent;

	public StatDeathCube() {
		recordedPlayers = new ArrayList<StatDeathCubePlayer>();
		recordedPlayersNames = new ArrayList<String>();
		indexCurrent = 0;
	}

	public void addPlayer(String playerName) {
		if (recordedPlayers.isEmpty() || !recordedPlayersNames.contains(playerName)) {
			recordedPlayers.add(indexCurrent, new StatDeathCubePlayer(playerName));
			recordedPlayersNames.add(indexCurrent, playerName);
			indexCurrent++;
		}
	}
	
	public void recordKill(String attackerName, String victimName) {
		if (recordedPlayersNames.contains(attackerName)) {
			int attackerIndex = recordedPlayersNames.indexOf(attackerName);
			
			if (recordedPlayersNames.contains(victimName)) {
				recordedPlayers.get(attackerIndex).addPlayerKill(victimName);
			} else if (victimName.equals("mob")) {
				recordedPlayers.get(attackerIndex).addMobKill();
			} else {
				recordedPlayers.get(attackerIndex).addPlayerKill("Other");
			}
		}
	}
	
	public void recordDeath(String victimName, String sourceName) {
		if (recordedPlayersNames.contains(victimName)) {
			int victimIndex = recordedPlayersNames.indexOf(victimName);
			
			//if (recordedPlayersNames.contains(sourceName)) {
				recordedPlayers.get(victimIndex).addDeath(sourceName);
			//} else if (sourceName.equals("mob")) {
				//recordedPlayers.get(victimIndex).addMobKill();
			//} else {
				//recordedPlayers.get(victimIndex).addPlayerKill("Other");
			//}
		}
	}
	
	public void recordCapture(String playerName, int pointIndex) {
		if (recordedPlayersNames.contains(playerName)) {
			int playerIndex = recordedPlayersNames.indexOf(playerName);
			
			recordedPlayers.get(playerIndex).addPointCapture(pointIndex);
		}
	}

	public void buildView1() {
		/**
		 * View 1: Player Names (rows) per Stats(Kills, Deaths, Captures, Average Time Alive, etc.) (columns)
		 * - When hot-key is pressed, build a view of current statistics.
		 */
	}
}
