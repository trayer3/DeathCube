package com.projectreddog.deathcube.stats;

import java.util.ArrayList;
import java.util.List;

/**
 * 
 * A class to record and display statistics about each Player in the DeathCube Game.
 * 
 * Display:
 * - View 1:  Player Names (rows) per Stats(Kills, Deaths, Captures, Average Time Alive, etc.) (columns)
 * - View 2:  Player Names (rows) per Target(Players, Mobs?, Self?) (columns) = Amount Kills (cells)
 * - View 3:  Player Names (rows) per Source(Players, Mobs?, Self?) (columns) = Amount Deaths (cells)
 * - View 4:  Team, List of Points (rows) per Player Name / Blank (1 column)
 * 
 */

public class StatDeathCube {

	public static List<StatDeathCubePlayer> recordedPlayers;
	
	public StatDeathCube() {
		recordedPlayers = new ArrayList<StatDeathCubePlayer>();
	}
	
	public void buildView1() {
		/**
		 * View 1:  Player Names (rows) per Stats(Kills, Deaths, Captures, Average Time Alive, etc.) (columns)
		 * - When hot-key is pressed, build a view of current statistics.
		 */
	}
}
