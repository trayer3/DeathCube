package com.projectreddog.deathcube.tileentity;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.gui.IUpdatePlayerListBox;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockPos;

import com.projectreddog.deathcube.game.GameTeam;
import com.projectreddog.deathcube.init.ModNetwork;
import com.projectreddog.deathcube.network.MessageHandleTextUpdate;
import com.projectreddog.deathcube.reference.Reference;
import com.projectreddog.deathcube.utility.Log;

public class TileEntityGameController extends TileEntityDeathCube implements IUpdatePlayerListBox {
	
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
	
	/**
	 * Team variables
	 */
	private GameTeam[] gameTeams;
	private int numPossibleTeams = 4;
	
	/**
	 * Spawn and Capture Point Variables
	 */
	private List<BlockPos> spawnPointsList = new ArrayList<BlockPos>();
	private List<BlockPos> capturePointsList = new ArrayList<BlockPos>();

	
	public TileEntityGameController() {
		
	}
	
	public void onTextRequest() {
		if(this.worldObj != null) {
			if(!this.worldObj.isRemote) {
				Log.info("Server sending requested text. Num points: " + numPossibleTeams);
					ModNetwork.simpleNetworkWrapper.sendToAll(new MessageHandleTextUpdate(this.pos, Reference.MESSAGE_FIELD1_ID, String.valueOf(numPossibleTeams)));
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
		 * Save Game Controller Data
		 */
		Log.info("Game Controller sees Text Update: " + text);
		if(fieldID == Reference.MESSAGE_FIELD1_ID) {
			try {
				numPossibleTeams = Integer.parseInt(text);
        		if(!this.worldObj.isRemote) {
    				/**
    				 * If a server message (not remote), update the Clients too.
    				 */
        			ModNetwork.simpleNetworkWrapper.sendToAll(new MessageHandleTextUpdate(this.pos, Reference.MESSAGE_FIELD1_ID, String.valueOf(numPossibleTeams)));
    			}
        	} catch (NumberFormatException e) {
        		Log.warn("Tried to parse non-Integer: " + text);
        	}
            markDirty();
        } 
	}
	
	public int getNumTeams() {
		return numPossibleTeams;
	}
	
	@Override
    public void readFromNBT(NBTTagCompound tag){
        super.readFromNBT(tag);
        numPossibleTeams = tag.getInteger("team");        
        Log.info("Game Controller - NBT Read :: Number of Teams: " + numPossibleTeams);
    }

    @Override
    public void writeToNBT(NBTTagCompound tag){
        super.writeToNBT(tag);
        tag.setInteger("team", numPossibleTeams);
        Log.info("Game Controller - NBT Write :: Number of Teams: " + numPossibleTeams);
    }
	
	/**
	 * Needs to be !isRemote?
	 */
	@Override
    public void onGuiButtonPress(int buttonID){
        if(buttonID == Reference.BUTTON_START_GAME) {
        	/**
			 * Start Button Pressed - Start Game!
			 */
			Log.info("Server sees Start Button Pressed");
			Log.info("Tile Entity starting game.");
			Log.info("isRemote? " + this.worldObj.isRemote);
			if (TileEntityGameController.gameState != null) {
				if (TileEntityGameController.gameState == GameStates.Lobby) {
					TileEntityGameController.gameState = GameStates.GameWarmup;
				} else if (TileEntityGameController.gameState == GameStates.Running) {
					TileEntityGameController.gameState = GameStates.PostGame;
				}
			}
        }
    }

	/**
	 * Needs to be !isRemote?
	 */
	@Override
	public void update() {
		if (gameState == GameStates.Lobby) {
			/**
			 * Lobby actions:
			 * Make sure the force field is Inactive.
			 * Make sure the timer is not running.
			 */
			if (fieldState != FieldStates.Inactive)
				fieldState = FieldStates.Inactive;
			if (gameTimer >= 0)
				gameTimer = -1;
			
			/**
			 * TODO: Count the number of Spawn Points in current DeathCube Force Field boundaries.
			 * 
			 * Or store this information elsewhere?  A game setup Tile Entity?  Game Controller
			 * 		used only by players when starting the game.
			 */
			spawnPointsList.clear();
			capturePointsList.clear();
			
			List<TileEntity> tileEntities = this.worldObj.loadedTileEntityList;
			for(TileEntity te : tileEntities) {
				if(te instanceof TileEntitySpawnPoint) {
					/**
					 * Track Spawn Points
					 */
					spawnPointsList.add(((TileEntitySpawnPoint) te).getPos());
					
					//Log.info("Number of Spawn Points: " + spawnPointsList.size());
				}
				else if(te instanceof TileEntityCapturePoint) {
					/**
					 * Track Capture Points
					 */
					//if(!capturePointsList.contains((TileEntityCapturePoint) te)) {
						capturePointsList.add(((TileEntityCapturePoint) te).getPos());
					//}
					
					//Log.info("Number of Capture Points: " + capturePointsList.size());
					
				}
			}
			
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
		 *  - Array of Possible Teams by Color
		 *  - List of Colors used.  (Not random - to make this random, will need to allow random color for
		 *  		all points and teams.  Map making would have to associate a point with a team by another
		 *  		designator, e.g. A, B, C... - rather than color.  Then, color would be assign during
		 *  		the StartGame() method.)
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
		 * Add Spawn and Capture Points to GameTeam objects.
		 *  - TODO: Make sure there is at least 1 point of each kind for each team.
		 */
		for(GameTeam team : gameTeams) {
			Log.info("Loop for team: " + team.getTeamColor());
			for(BlockPos pointPos : spawnPointsList) {
				TileEntitySpawnPoint lookupTE = (TileEntitySpawnPoint) this.worldObj.getTileEntity(pointPos);
				Log.info("Spawn Point color: " + lookupTE.getSpawnPointTeamColor());
				if(lookupTE.getSpawnPointTeamColor().equals(team.getTeamColor().toLowerCase())){
					team.addSpawnPoint(pointPos);
					Log.info("Point matches team!");
				}
			}
			for(BlockPos pointPos : capturePointsList) {
				TileEntityCapturePoint lookupTE = (TileEntityCapturePoint) this.worldObj.getTileEntity(pointPos);
				Log.info("Capture Point color: " + lookupTE.getCapturePointTeamColor());
				if(lookupTE.getCapturePointTeamColor().equals(team.getTeamColor().toLowerCase())){
					team.addCapturePoint(pointPos);
					Log.info("Point matches team!");
				}
			}
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
			//Random rand = new Random();
			int teamIndex = 0;//rand.nextInt(gameTeams.length);
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
			BlockPos spawnLocation = gameTeams[teamIndex].getSpawnLocation();
			player.setPositionAndUpdate(spawnLocation.getX() + 0.5d, spawnLocation.getY() + 1, spawnLocation.getZ() + 0.5d);
			
			/**
			 * Play sound at Game Start
			 *  - TODO: Get Custom sound.  This call doesn't work.
			 */
			player.playSound("mob.bat.death", 1.0f, 1.0f);
		}
	}

}
