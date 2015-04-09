package com.projectreddog.deathcube.tileentity;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.gui.IUpdatePlayerListBox;
import net.minecraft.tileentity.TileEntity;

import com.projectreddog.deathcube.DeathCube;
import com.projectreddog.deathcube.game.GameTeam;
import com.projectreddog.deathcube.reference.Reference;
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
	private int numPossibleTeams = 4;

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
		 * Create Team Objects
		 */
		gameTeams = new GameTeam[numPossibleTeams];
		List<String> usedColors = new ArrayList<String>();
		for(int i=0; i<numPossibleTeams; i++) {
			//String color = (String) RandomChoice.chooseRandom((List<Object>) usedColors);
			String color = Reference.TEAM_RED;
			if(i==0)
				color = Reference.TEAM_RED;
			else if(i==1)
				color = Reference.TEAM_BLUE;
			else if(i==2)
				color = Reference.TEAM_GREEN;
			else if(i==3)
			color = Reference.TEAM_YELLOW;
				
			gameTeams[i] = new GameTeam(color);
			usedColors.add(color);
			
			Log.info("Team added, color: " + gameTeams[i].getTeamColor());
		}

		/**
		 * For each player in the Game:
		 */
		List<EntityPlayer> playerList = MinecraftServer.getServer().getConfigurationManager().playerEntityList;
		for (EntityPlayer player : playerList)
		{
			/**
			 * Assign to a Team
			 */
			Random rand = new Random();
			int teamIndex = rand.nextInt(gameTeams.length);
			gameTeams[teamIndex].addPlayer(player);
			
			Log.info("Added player: " + player.getName() + " to team: " + gameTeams[teamIndex].getTeamColor());
			
			/**
			 *  Set velocity to zero to avoid death falling.
			 *  Clear any potion effects from Lobby time.
			 */
			player.setVelocity(0, 0, 0);
			player.fallDistance = 0;
			player.clearActivePotions();
			
			/**
			 *  TODO: Initialize Kill Streak Timers
			 */
			//killStreakTimer.put(p, 0);
			//killStreakMultiplier.put(p, 0);
			
			/**
			 *  TODO: Teleport Players to Team Spawn Locations.
			 */
			//SpawnPlayerInGame(p);
			//player.playerLocation = this.pos;
			player.setPositionAndUpdate(this.pos.getX() + 0.5d, this.pos.getY() + 1, this.pos.getZ() + 0.5d);
			
			/**
			 * Play sound at Game Start
			 */
			player.playSound("mob.bat.death", 1.0f, 1.0f);
		}
	}

}
