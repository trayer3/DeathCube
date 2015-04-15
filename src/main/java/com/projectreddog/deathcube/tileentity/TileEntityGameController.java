package com.projectreddog.deathcube.tileentity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Random;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.gui.IUpdatePlayerListBox;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockPos;

import com.projectreddog.deathcube.DeathCube;
import com.projectreddog.deathcube.game.GameTeam;
import com.projectreddog.deathcube.init.ModNetwork;
import com.projectreddog.deathcube.network.MessageHandleTextUpdate;
import com.projectreddog.deathcube.reference.Reference;
import com.projectreddog.deathcube.reference.Reference.FieldStates;
import com.projectreddog.deathcube.reference.Reference.GameStates;
import com.projectreddog.deathcube.utility.Log;

public class TileEntityGameController extends TileEntityDeathCube implements IUpdatePlayerListBox {

	/**
	 * Game Controller Variables.
	 */

	/**
	 * Team Variables
	 */
	private int numTeamsFromGUI = 4;

	/**
	 * Spawn and Capture Point Variables
	 */
	private List<BlockPos> spawnPointsList = new ArrayList<BlockPos>();
	private List<BlockPos> capturePointsList = new ArrayList<BlockPos>();

	/**
	 * Scoring Variables
	 */
	private String winningTeamColor = "trayer4";

	public TileEntityGameController() {
		DeathCube.gameState = GameStates.Lobby;
		DeathCube.fieldState = FieldStates.Off;
		DeathCube.gameTimer = -1;
	}

	public void onTextRequest() {
		if (this.worldObj != null) {
			if (!this.worldObj.isRemote) {
				Log.info("Server sending requested text. Num points: " + numTeamsFromGUI);
				ModNetwork.simpleNetworkWrapper.sendToAll(new MessageHandleTextUpdate(this.pos, Reference.MESSAGE_FIELD1_ID, String.valueOf(numTeamsFromGUI)));
			} else {
				Log.info("World is remote - text request.");
			}
		} else {
			Log.info("World object null - text request.");
		}
	}

	@Override
	public void onGuiTextfieldUpdate(int fieldID, String text) {
		/**
		 * Save Game Controller Data
		 */
		Log.info("Game Controller sees Text Update: " + text);
		if (fieldID == Reference.MESSAGE_FIELD1_ID) {
			try {
				numTeamsFromGUI = Integer.parseInt(text);
				if (!this.worldObj.isRemote) {
					/**
					 * If a server message (not remote), update the Clients too.
					 */
					ModNetwork.simpleNetworkWrapper.sendToAll(new MessageHandleTextUpdate(this.pos, Reference.MESSAGE_FIELD1_ID, String.valueOf(numTeamsFromGUI)));
				}
			} catch (NumberFormatException e) {
				Log.warn("Tried to parse non-Integer: " + text);
			}
			markDirty();
		}
	}

	public int getNumTeams() {
		return numTeamsFromGUI;
	}

	@Override
	public void readFromNBT(NBTTagCompound tag) {
		super.readFromNBT(tag);
		numTeamsFromGUI = tag.getInteger("team");
		Log.info("Game Controller - NBT Read :: Number of Teams: " + numTeamsFromGUI);
	}

	@Override
	public void writeToNBT(NBTTagCompound tag) {
		super.writeToNBT(tag);
		tag.setInteger("team", numTeamsFromGUI);
		Log.info("Game Controller - NBT Write :: Number of Teams: " + numTeamsFromGUI);
	}

	/**
	 * Needs to be !isRemote?
	 */
	@Override
	public void onGuiButtonPress(int buttonID) {
		if (!this.worldObj.isRemote) {
			if (buttonID == Reference.BUTTON_START_GAME) {
				/**
				 * Start Button Pressed - Start Game!
				 */
				Log.info("Server sees Start Button Pressed");
				Log.info("Tile Entity starting game.");
				Log.info("isRemote? " + this.worldObj.isRemote);
				if (DeathCube.gameState != null) {
					if (DeathCube.gameState == GameStates.Lobby) {
						DeathCube.gameState = GameStates.GameWarmup;
					} else if (DeathCube.gameState == GameStates.Running) {
						DeathCube.gameState = GameStates.PostGame;
					}
				}
			}
		}
	}

	/**
	 * Needs to be !isRemote?
	 */
	@Override
	public void update() {
		if (!this.worldObj.isRemote) {
			if (DeathCube.gameState == GameStates.Lobby) {
				/**
				 * Lobby actions:
				 * Make sure the force field is Inactive.
				 * Make sure the timer is not running.
				 */
				if (DeathCube.fieldState != FieldStates.Inactive)
					DeathCube.fieldState = FieldStates.Inactive;
				if (DeathCube.gameTimer >= 0)
					DeathCube.gameTimer = -1;

				/**
				 * Count the number of Spawn Points in current DeathCube Force Field boundaries.
				 * 
				 * Or store this information elsewhere? A game setup Tile Entity? Game Controller
				 * used only by players when starting the game.
				 */
				spawnPointsList.clear();
				capturePointsList.clear();

				List<TileEntity> tileEntities = this.worldObj.loadedTileEntityList;
				for (TileEntity te : tileEntities) {
					if (te instanceof TileEntitySpawnPoint) {
						/**
						 * Track Spawn Points
						 */
						spawnPointsList.add(((TileEntitySpawnPoint) te).getPos());

						// Log.info("Number of Spawn Points: " + spawnPointsList.size());
					} else if (te instanceof TileEntityCapturePoint) {
						/**
						 * Track Capture Points
						 */
						// if(!capturePointsList.contains((TileEntityCapturePoint) te)) {
						capturePointsList.add(((TileEntityCapturePoint) te).getPos());
						// }

						// Log.info("Number of Capture Points: " + capturePointsList.size());

					}
				}

			} else if (DeathCube.gameState == GameStates.GameWarmup) {
				if (DeathCube.gameTimer < 0) {
					DeathCube.gameTimer = 200;
					Log.info("Game now Warming Up.");
				} else if (DeathCube.gameTimer > 0) {
					/**
					 * Decrement Warm-up Timer
					 */
					DeathCube.gameTimer--;

					/**
					 * TODO: Broadcast Time Until Game Start
					 */

				} else if (DeathCube.gameTimer == 0) {
					/**
					 * Timer is Up - Start Game!
					 */
					DeathCube.gameTimer = -1;
					DeathCube.gameState = GameStates.Running;

					Log.info("Game now Running.");

					startGame();
				} else {
					/**
					 * This condition show not be reached, ever.
					 */
					Log.info("Invalid Game State! " + DeathCube.gameState + " - Timer: " + DeathCube.gameTimer);
				}
			} else if (DeathCube.gameState == GameStates.Running) {
				/**
				 * Main Game actions.
				 */
				if (DeathCube.fieldState != FieldStates.Active)
					DeathCube.fieldState = FieldStates.Active;

				/**
				 * Check if a point has been captured.
				 * - If so, set the next point as active.
				 * - Or, check for Game Over condition.
				 */
				if (DeathCube.isOrderedCapture) {
					/**
					 * Check if current point for each team has been captured.
					 */
					for (GameTeam team : DeathCube.gameTeams) {
						TileEntityCapturePoint lookupTE = (TileEntityCapturePoint) this.worldObj.getTileEntity(team.getCurrentPointPos());
						if (lookupTE.getIsCaptured()) {
							if (team.hasCapturedAllPoints()) {
								winningTeamColor = team.getTeamColor();
								stopGame();
							} else {
								team.setNextCapturePointActive();
							}
						}
					}
				} else {
					/**
					 * Check if all points have been captured.
					 */
				}

			} else if (DeathCube.gameState == GameStates.PostGame) {
				/**
				 * Post Game actions.
				 */
				if (DeathCube.gameTimer < 0 || DeathCube.gameTimer > 200) {
					DeathCube.gameTimer = 200;

					if (DeathCube.fieldState != FieldStates.Inactive)
						DeathCube.fieldState = FieldStates.Inactive;

					Log.info("Game has ended.");
				} else if (DeathCube.gameTimer > 0 && DeathCube.gameTimer <= 200) {
					/**
					 * Decrement Timer
					 */
					DeathCube.gameTimer--;

					/**
					 * Other Post Game Stuff
					 */

				} else if (DeathCube.gameTimer == 0) {
					/**
					 * Timer is Up - Return to Lobby GameState
					 */
					DeathCube.gameState = GameStates.Lobby;

					Log.info("Game now in Lobby.");
				} else {
					/**
					 * This condition show not be reached, ever.
					 */
					Log.info("Invalid Game State! " + DeathCube.gameState + " - Timer: " + DeathCube.gameTimer);
				}

			} else if (DeathCube.gameState == GameStates.GameOver) {
				/**
				 * Game Over actions.
				 * TODO: Vote on next Map?
				 */

				if (DeathCube.fieldState != FieldStates.Off)
					DeathCube.fieldState = FieldStates.Off;
			}
		}
	}

	public void startGame() {

		/**
		 * Create Team Objects
		 * - Array of Possible Teams by Color
		 * - List of Colors used. (Not random - to make this random, will need to allow random color for
		 * all points and teams. Map making would have to associate a point with a team by another
		 * designator, e.g. A, B, C... - rather than color. Then, color would be assign during
		 * the StartGame() method.)
		 */
		DeathCube.gameTeams = new GameTeam[numTeamsFromGUI];
		DeathCube.teamColorToIndex = new HashMap<String, Integer>();
		DeathCube.playerToTeamColor = new HashMap<String, String>();
		for (int i = 0; i < numTeamsFromGUI; i++) {
			String color = Reference.TEAM_RED;
			if (i == 0)
				color = Reference.TEAM_RED;
			else if (i == 1)
				color = Reference.TEAM_BLUE;
			else if (i == 2)
				color = Reference.TEAM_GREEN;
			else if (i == 3)
				color = Reference.TEAM_YELLOW;

			DeathCube.gameTeams[i] = new GameTeam(color);
			DeathCube.teamColorToIndex.put(color, i);

			Log.info("Team added, color: " + DeathCube.gameTeams[i].getTeamColor());
		}

		/**
		 * Add Spawn and Capture Points to GameTeam objects.
		 * - TODO: Make sure there is at least 1 point of each kind for each team.
		 */
		Log.info("Spawn Point list size: " + spawnPointsList.size());
		Log.info("Capture Point list size: " + capturePointsList.size());

		for (GameTeam team : DeathCube.gameTeams) {
			Log.info("Loop for team: " + team.getTeamColor());
			for (BlockPos pointPos : spawnPointsList) {
				TileEntitySpawnPoint lookupTE = (TileEntitySpawnPoint) this.worldObj.getTileEntity(pointPos);
				Log.info("Spawn Point color: " + lookupTE.getSpawnPointTeamColor());
				Log.info("Spawn Point Location - x: " + pointPos.getX() + " y: " + pointPos.getY() + " z: " + pointPos.getZ());
				if (lookupTE.getSpawnPointTeamColor().equals(team.getTeamColor())) {
					team.addSpawnPoint(pointPos);
					Log.info("Point matches team!");
				}
			}

			if (team.getNumSpawnPoints() < 1) {
				Log.info("No Spawn Points for Team: " + team.getTeamColor());
				stopGame();
				break;
			}

			/**
			 * Find Capture Points for Team.
			 */
			List<BlockPos> tempList = new ArrayList<BlockPos>();
			for (BlockPos pointPos : capturePointsList) {
				TileEntityCapturePoint lookupTE = (TileEntityCapturePoint) this.worldObj.getTileEntity(pointPos);
				Log.info("Capture Point color: " + lookupTE.getCapturePointTeamColor());
				if (lookupTE.getCapturePointTeamColor().equals(team.getTeamColor())) {
					tempList.add(pointPos);
					Log.info("Point matches team!");
				}
			}

			if (tempList.size() < 1) {
				Log.info("No Capture Points for Team: " + team.getTeamColor());
				stopGame();
				break;
			} else {
				Log.info("Found Capture Points for Team: " + team.getTeamColor());
			}

			/**
			 * Sort Capture Points by Point Order value.
			 */
			if (tempList.size() > 1) {
				Log.info("Ordering Points ...");
				while (tempList.size() > 0) {
					int indexOfLowest = 0;
					for (int i = 0; i < tempList.size(); i++) {
						BlockPos iPos = tempList.get(i);
						TileEntityCapturePoint iTE = (TileEntityCapturePoint) this.worldObj.getTileEntity(iPos);

						BlockPos lowPos = tempList.get(indexOfLowest);
						TileEntityCapturePoint lowTE = (TileEntityCapturePoint) this.worldObj.getTileEntity(lowPos);

						if (iTE.captureOrderNumber < lowTE.captureOrderNumber) {
							indexOfLowest = i;
						}
					}

					Log.info("Capture Point: " + ((TileEntityCapturePoint) this.worldObj.getTileEntity(tempList.get(indexOfLowest))).captureOrderNumber);
					team.addCapturePoint(tempList.get(indexOfLowest));
					tempList.remove(indexOfLowest);
				}
			} else {
				Log.info("Only 1 Point.");
				team.addCapturePoint(tempList.get(0));
			}

			team.setNextCapturePointActive();
			Log.info("Point set active.");
		}

		/**
		 * For each player in the Game:
		 */
		List<EntityPlayer> playerList = MinecraftServer.getServer().getConfigurationManager().playerEntityList;
		for (EntityPlayer player : playerList) {
			/**
			 * Assign to a Team
			 */
			Random rand = new Random();
			int teamIndex = rand.nextInt(DeathCube.gameTeams.length);
			DeathCube.gameTeams[teamIndex].addPlayer(player);
			DeathCube.playerToTeamColor.put(player.getName(), DeathCube.gameTeams[teamIndex].getTeamColor());

			Log.info("Added player: " + player.getName() + " to team: " + DeathCube.gameTeams[teamIndex].getTeamColor());

			/**
			 * Set velocity to zero to avoid death falling.
			 * Clear any potion effects from Lobby time.
			 */
			player.setVelocity(0, 0, 0);
			player.fallDistance = 0;
			player.clearActivePotions();

			/**
			 * TODO: Initialize Kill Streak Timers
			 */
			// killStreakTimer.put(p, 0);
			// killStreakMultiplier.put(p, 0);

			/**
			 * Teleport Players to Team Spawn Locations.
			 */
			BlockPos spawnLocation = DeathCube.gameTeams[teamIndex].getSpawnLocation();
			player.setPositionAndUpdate(spawnLocation.getX() + 0.5d, spawnLocation.getY() + 1, spawnLocation.getZ() + 0.5d);

			/**
			 * Play sound at Game Start
			 * - TODO: Get Custom sound. This call doesn't work.
			 */
			player.playSound("mob.bat.death", 1.0f, 1.0f);
		}
	}

	private void stopGame() {
		/**
		 * TODO: Stop Game Processing
		 * - Set all Capture Points to inactive.
		 * - Change Spawn Point to Lobby Area?
		 * - Change GameState to PostGame
		 */
		for (GameTeam team : DeathCube.gameTeams) {
			team.setAllPointInactive();
		}

		DeathCube.gameState = GameStates.PostGame;

		Log.info(winningTeamColor + " has won!");
	}
}
