package com.projectreddog.deathcube.tileentity;

import java.util.List;

import net.minecraft.server.gui.IUpdatePlayerListBox;
import net.minecraft.tileentity.TileEntity;

import com.projectreddog.deathcube.game.GameTeam;
import com.projectreddog.deathcube.utility.Log;
import com.projectreddog.deathcube.utility.RandomChoice;

public class TileEntityGameController extends TileEntity implements IUpdatePlayerListBox {
	
	public static enum GameStates {
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

	/**
	 * Game Controller variables.
	 */
	public static GameStates gameState = GameStates.Lobby;
	public static FieldStates fieldState = FieldStates.Off;
	public static int gameTimer = -1;
	
	private GameTeam[] gameTeams;
	private int numPossibleTeams = 2;

	public TileEntityGameController() {
		
	}

	@Override
	public void update() {
		if (gameState == GameStates.Lobby) {
			/**
			 * TODO: Lobby actions.
			 * Make sure the force field is Inactive.
			 * Make sure the timer is not running.
			 */
			if (fieldState != FieldStates.Inactive)
				fieldState = FieldStates.Inactive;
			if (gameTimer >= 0)
				gameTimer = -1;
			
			/**
			 * Count the number of Spawn Points in current DeathCube Force Field boundaries.
			 * 
			 * Or store this information elsewhere?  A game setup Tile Entity?  Game Controller
			 * 		used only by players when starting the game.
			 */
			
		} else if (gameState == GameStates.GameWarmup) {
			if (gameTimer < 0){
				gameTimer = 200;
				Log.info("Game now Warming Up.");
			}
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
				gameTimer = -1;
				gameState = GameStates.Running;
				
				Log.info("Game now Running.");
				
				StartGame();
			} else {
				/**
				 * This condition show not be reached, ever.
				 */
				Log.info("Invalid Game State! " + gameState + " - Timer: " + gameTimer);
			}
		} else if (gameState == GameStates.Running) {
			/**
			 * TODO: Main Game actions.
			 */
			if (fieldState != FieldStates.Active)
				fieldState = FieldStates.Active;
			
		} else if (gameState == GameStates.PostGame) {
			/**
			 * TODO: Post Game actions.
			 */
			if (gameTimer < 0 || gameTimer > 200){
				gameTimer = 200;
				
				if (fieldState != FieldStates.Inactive)
					fieldState = FieldStates.Inactive;
				
				Log.info("Game has ended.");
			}
			else if (gameTimer > 0 && gameTimer <= 200) {
				/**
				 * Decrement Timer
				 */
				gameTimer--;
				
				/**
				 * TODO: Other Post Game Stuff
				 */
				
			}
			else if (gameTimer == 0) {
				/**
				 * Timer is Up - Return to Lobby GameState
				 */
				gameState = GameStates.Lobby;
				
				Log.info("Game now in Lobby.");
			} else {
				/**
				 * This condition show not be reached, ever.
				 */
				Log.info("Invalid Game State! " + gameState + " - Timer: " + gameTimer);
			}
			
		} else if (gameState == GameStates.GameOver) {
			/**
			 * TODO: Game Over actions.
			 * TODO: Vote on next Map?
			 * 		
			 */
			if (fieldState != FieldStates.Off)
				fieldState = FieldStates.Off;
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
		gameTeams = new GameTeam[numPossibleTeams];
		List<String> usedColors;
		for(int i=0; i<=numPossibleTeams; i++) {
			String color = (String) RandomChoice.chooseRandom((List<Object>) usedColors);
			gameTeams[i] = new GameTeam(color);
			usedColors.add(color);
		}

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
