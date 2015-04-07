package com.projectreddog.deathcube.tileentity;

import java.util.Random;

import com.projectreddog.deathcube.DeathCube;
import com.projectreddog.deathcube.utility.Log;

import net.minecraft.server.gui.IUpdatePlayerListBox;
import net.minecraft.tileentity.TileEntity;

public class TileEntityGameController extends TileEntity implements IUpdatePlayerListBox {

	public static int gameID;
	
	private static enum GameStates {
		Lobby, GameWarmup, Running, PostGame, GameOver
	}

	/**
	 * Field States: 
	 *    Off 		- Removes Force Field Blocks 
	 *    Inactive 	- Places Force Field Blocks, but they are OK to touch. 
	 *    Active 	- Arms Force Field Blocks as dangerous to touch!
	 */
	private static enum FieldStates {
		Off, Inactive, Active
	}

	public static GameStates gameState = GameStates.Lobby;
	public static FieldStates fieldState = FieldStates.Off;

	private int gameTimer = -1;

	public TileEntityGameController() {
		gameID = DeathCube.instance.getGameID();
	}

	@Override
	public void update() {
		if (gameState == GameStates.Lobby) {
			/**
			 * TODO: Lobby actions.
			 */
			if (fieldState != FieldStates.Inactive)
				fieldState = FieldStates.Inactive;
			if (gameTimer >= 0)
				gameTimer = -1;
		} else if (gameState == GameStates.GameWarmup) {
			if (gameTimer < 0)
				
				gameTimer = 1200;
			else if (gameTimer > 0) {
				/**
				 * Decrement Warm-up Timer
				 */
				gameTimer--;
				
				/**
				 * TODO: Broadcast Time Until Game Start
				 */
				
			}
			else if (gameTimer == 0) {
				/**
				 * Timer is Up - Start Game!
				 */
				gameState = GameStates.Running;
				
				StartGame();
			} else
				/**
				 * This condition show not be reached, ever.
				 */
				Log.info("Invalid Game State! " + gameState + " - Timer: " + gameTimer);
		} else if (gameState == GameStates.Running) {
			/**
			 * TODO: Main Game actions.
			 */
			if (fieldState != FieldStates.Active)
				fieldState = FieldStates.Active;
		} else if (gameState == GameStates.PostGame) {
			/**
			 * TODO: Post Game actions.
			 * 		
			 */
			if (fieldState != FieldStates.Inactive)
				fieldState = FieldStates.Inactive;
		} else if (gameState == GameStates.GameOver) {
			/**
			 * TODO: Post Game actions.
			 * 		
			 */
			if (fieldState != FieldStates.Off)
				fieldState = FieldStates.Off;
			
			DeathCube.instance.freeGameID();
		}
	}
	
	public void StartGame(){

		/**
		 * TODO: Play Sound at Game Start
		 */
		//Bukkit.getServer().getWorld("World").playSound(lobbySpawn,  Sound.BAT_DEATH, 100, 1);
		
		/**
		 * TODO: Assign Players to Random Teams
		 * 		Track All Players in Lobby Area
		 * 		New Players need to Right-Click the Game Controller to Join
		 * 		(Or another Block/Tile Entity)
		 */

		/**
		 * For each player in the Game:
		 */
		//for (Player p : Bukkit.getOnlinePlayers())
		//{
			/**
			 *  TODO: Avoid death by falling.
			 */
			//Vector vec = new Vector();
			//p.setVelocity(vec);
			//p.setFallDistance(0);
			
			/**
			 *  TODO: Clear all effects acquired in lobby.
			 */
			//for (PotionEffect effect : p.getActivePotionEffects())
			//{
			//	p.removePotionEffect(effect.getType());
			//}
			
			/**
			 *  TODO: Initialize Kill Streak Timers
			 */
			//killStreakTimer.put(p, 0);
			//killStreakMultiplier.put(p, 0);
			
			/**
			 *  TODO: Teleport Players to Team Spawn Locations.
			 */
			//SpawnPlayerInGame(p);
		//}
	}

}
