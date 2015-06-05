package com.projectreddog.deathcube.stats;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * 
 * A class to hold statistics about each Player in the DeathCube Game.
 * 
 */

public class StatDeathCubePlayer {

	private String playerName;
	private boolean isInGame;
	
	protected int playerKills;
	protected int mobKills;
	protected int deaths;
	protected int suicides;
	
	protected int pointCaptures;
	
	protected HashMap<String, Integer> TargetKilledbyPlayer_Amount;
	protected HashMap<String, Integer> PlayerKilledbySource_Amount;
	protected List<Integer> pointsCapturedbyPlayer;
	
	public StatDeathCubePlayer(String playerName) {
		this.playerName = playerName;
		this.isInGame = true;
		
		this.playerKills = 0;
		this.mobKills = 0;
		this.deaths = 0;
		this.suicides = 0;
		
		this.pointCaptures = 0;
		
		TargetKilledbyPlayer_Amount = new HashMap<String, Integer>();
		PlayerKilledbySource_Amount = new HashMap<String, Integer>();
		pointsCapturedbyPlayer = new ArrayList<Integer>();
	}
	
	public String getPlayerName() {
		return playerName;
	}
	
	public void flagPlayerInGame(boolean isInGame) {
		this.isInGame = isInGame;
	}
	
	public boolean isPlayerInGame() {
		return isInGame;
	}
	
	public void addPlayerKill(String targetPlayerName) {
		playerKills++;
		
		if (TargetKilledbyPlayer_Amount.get(targetPlayerName) != null) {
			int currentKillsPerTarget = TargetKilledbyPlayer_Amount.get(targetPlayerName);
			TargetKilledbyPlayer_Amount.put(targetPlayerName, currentKillsPerTarget + 1);
		} else {
			TargetKilledbyPlayer_Amount.put(targetPlayerName, 1);
		}
	}
	
	public void addMobKill() {
		mobKills++;
		TargetKilledbyPlayer_Amount.put("Mob", mobKills);
	}
	
	public void addDeath(String sourceName) {
		deaths++;
		
		if (PlayerKilledbySource_Amount.get(sourceName) != null) {
			int currentKillsPerTarget = PlayerKilledbySource_Amount.get(sourceName);
			PlayerKilledbySource_Amount.put(sourceName, currentKillsPerTarget + 1);
		} else {
			PlayerKilledbySource_Amount.put(sourceName, 1);
		}
	}
	
	public void addSuicide() {
		suicides++;
		PlayerKilledbySource_Amount.put("Self", suicides);
	}
	
	public void addPointCapture(int pointIndex) {
		pointCaptures++;
		pointsCapturedbyPlayer.add(pointIndex + 1);
	}
}
