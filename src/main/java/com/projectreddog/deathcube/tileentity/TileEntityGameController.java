package com.projectreddog.deathcube.tileentity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Random;
import java.util.Set;

import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.gui.IUpdatePlayerListBox;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockPos;

import com.projectreddog.deathcube.DeathCube;
import com.projectreddog.deathcube.entity.EntityWaypoint;
import com.projectreddog.deathcube.game.GameTeam;
import com.projectreddog.deathcube.init.ModConfig;
import com.projectreddog.deathcube.init.ModNetwork;
import com.projectreddog.deathcube.network.MessageHandleClientGameUpdate;
import com.projectreddog.deathcube.network.MessageHandleTextUpdate;
import com.projectreddog.deathcube.reference.Reference;
import com.projectreddog.deathcube.reference.Reference.FieldStates;
import com.projectreddog.deathcube.reference.Reference.GameStates;
import com.projectreddog.deathcube.utility.Log;

public class TileEntityGameController extends TileEntityDeathCube implements IUpdatePlayerListBox {

	/**
	 * GUI Variables
	 */
	private String mapName = "Map Name";
	private int numTeamsFromGUI = 4;

	long currentTime;
	long timeRemaining;

	/**
	 * Spawn and Capture Point Variables
	 */
	private List<BlockPos> spawnPointsList = new ArrayList<BlockPos>();
	private List<BlockPos> capturePointsList = new ArrayList<BlockPos>();
	private int numTeamsInGame = 0;
	private int mostPointsCaptured = 0;

	/**
	 * Scoring Variables
	 */
	private String winningTeamColor = "trayer4";
	boolean last_displayScoreboard = false;
	String[] last_teamNames;
	int[] last_activeTeamPoints;
	double[] last_activeTeamPointTimes;

	public TileEntityGameController() {
		if (DeathCube.gameState == null) {
			DeathCube.gameState = GameStates.Lobby;
			DeathCube.fieldState = FieldStates.Inactive;

			Log.info("GameController Constructor - GameState: NULL - Initialized to Lobby.");
		} else {
			Log.info("GameController Constructor Call.  GameState: " + DeathCube.gameState);
		}

		if (this.pos != null && this.pos != new BlockPos(0, 0, 0)) {
			DeathCube.gameControllerPos = this.pos;
			Log.info("Found GameController Position: " + this.pos.toString());
			Log.info("Set Master GameController Position: " + DeathCube.gameControllerPos.toString());
		}
	}

	public void onTextRequest() {
		if (this.worldObj != null) {
			if (!this.worldObj.isRemote) {
				Log.info("Server sending requested text. Num points: " + numTeamsFromGUI);
				ModNetwork.sendToAll(new MessageHandleTextUpdate(this.pos, Reference.MESSAGE_FIELD1_ID, String.valueOf(numTeamsFromGUI)));
				ModNetwork.sendToAll(new MessageHandleTextUpdate(this.pos, Reference.MESSAGE_FIELD7_ID, String.valueOf(mapName)));
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
		 * Text Fields
		 * - private GuiTextField text_NumTeams;
		 */
		Log.info("Game Controller sees Text Update: " + text);
		if (fieldID == Reference.MESSAGE_FIELD1_ID) {
			try {
				numTeamsFromGUI = Integer.parseInt(text);
				if (!this.worldObj.isRemote) {
					/**
					 * If a server message (not remote), update the Clients too.
					 */
					ModNetwork.sendToAll(new MessageHandleTextUpdate(this.pos, Reference.MESSAGE_FIELD1_ID, String.valueOf(numTeamsFromGUI)));
				}
			} catch (NumberFormatException e) {
				Log.warn("Tried to parse non-Integer: " + text);
			}
			markDirty();
		} else if (fieldID == Reference.MESSAGE_FIELD7_ID) {
			mapName = text;
			if (!this.worldObj.isRemote) {
				/**
				 * If a server message (not remote), update the Clients too.
				 */
				ModNetwork.sendToAll(new MessageHandleTextUpdate(this.pos, Reference.MESSAGE_FIELD7_ID, mapName));
				ModConfig.updateConfig(mapName);
			}
			markDirty();
		}
	}

	public String getMapName() {
		return mapName;
	}

	public int getNumTeams() {
		return numTeamsFromGUI;
	}

	@Override
	public void readFromNBT(NBTTagCompound tag) {
		super.readFromNBT(tag);
		mapName = tag.getString("map");
		numTeamsFromGUI = tag.getInteger("team");
		Log.info("Game Controller - NBT Read :: Number of Teams: " + numTeamsFromGUI);
	}

	@Override
	public void writeToNBT(NBTTagCompound tag) {
		super.writeToNBT(tag);
		tag.setString("map", mapName);
		tag.setInteger("team", numTeamsFromGUI);
		// Log.info("Game Controller - NBT Write :: Number of Teams: " + numTeamsFromGUI);
	}

	/**
	 * Needs to be !isRemote?
	 */
	@Override
	public void onGuiButtonPress(int buttonID) {
		if (!this.worldObj.isRemote) {
			if (buttonID == Reference.BUTTON_1) {
				/**
				 * Start Button Pressed - Start Game!
				 */
				Log.info("Server sees Start Button Pressed");
				if (DeathCube.gameState != null) {
					if (DeathCube.gameState == GameStates.Lobby) {
						DeathCube.gameState = GameStates.GameWarmup;
						DeathCube.gameTimeStart = System.currentTimeMillis();
						DeathCube.gameTimeCheck = System.currentTimeMillis();
						Log.info("Game now Warming Up.");
					} else if (DeathCube.gameState == GameStates.Running) {
						stopGame();
					}
				}
			}
		}
	}

	@Override
	public void update() {
		if (!this.worldObj.isRemote) {
			if (DeathCube.gameState != null) {
				if (DeathCube.gameState == GameStates.Lobby) {
					/******************************************************************************************
					 * 
					 * Lobby actions:
					 * Count the number of Spawn Points, Capture Points, Gear Config Blocks
					 * in current DeathCube Force Field boundaries.
					 * 
					 * Better way/time to count and track these things?
					 * 
					 * ###### Not currently tracking at DeathCube.java level. Just locally. ######
					 * 
					 *****************************************************************************************/
					spawnPointsList.clear();
					capturePointsList.clear();
					DeathCube.gearTEPos.clear();

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
							capturePointsList.add(((TileEntityCapturePoint) te).getPos());
							// Log.info("Number of Capture Points: " + capturePointsList.size());
						} else if (te instanceof TileEntityStartingGearConfig) {
							/**
							 * Track Gear Config TE's
							 */
							DeathCube.gearTEPos.add(((TileEntityStartingGearConfig) te).getPos());
							// Log.info("Number of Gear Config Blocks: " + gearTEPosList.size());
						}
					}

				} else if (DeathCube.gameState == GameStates.GameWarmup) {
					/******************************************************************************************
					 * 
					 * Warm-up Actions:
					 * 
					 * Check Time
					 *****************************************************************************************/
					currentTime = System.currentTimeMillis();
					timeRemaining = Reference.TIME_WARMUP - (currentTime - DeathCube.gameTimeStart);

					if (timeRemaining <= 0) {
						/**
						 * Timer is Up - Start Game!
						 */
						DeathCube.gameTimeStart = System.currentTimeMillis();
						;
						DeathCube.gameState = GameStates.Running;

						if (DeathCube.fieldState != FieldStates.Active)
							DeathCube.fieldState = FieldStates.Active;

						Log.info("Game now Running.");

						startGame();
					} else {
						/**
						 * TODO: Periodically Broadcast Time Until Game Start ?
						 */
						// Log.info("GameState: " + DeathCube.gameState + " - Time Remaining: " + timeRemaining);
					}
				} else if (DeathCube.gameState == GameStates.Running) {
					/******************************************************************************************
					 * 
					 * Main Game actions.
					 * 
					 * Check Time
					 * - If done, end game
					 * - Else, process game
					 * 
					 * On game over, empty playerAwaitingRespawn list and respawn players?
					 * 
					 *****************************************************************************************/

					/**
					 * Check Time
					 */
					currentTime = System.currentTimeMillis();
					timeRemaining = Reference.TIME_MAINGAME - (currentTime - DeathCube.gameTimeStart);

					if (timeRemaining <= 0) {
						/**
						 * Timer is Up - Stop Game!
						 */
						if (DeathCube.fieldState != FieldStates.Inactive)
							DeathCube.fieldState = FieldStates.Inactive;

						Log.info("Game has ended.");

						stopGame();
					} else {
						/**
						 * Process queue of Players waiting to respawn.
						 * - TODO: Render on-screen a count-down until player respawns.
						 */
						if (DeathCube.playerAwaitingRespawn.size() > 0) {
							// Log.info("Player serving Death Penalty");
							Set<String> keySet = DeathCube.playerAwaitingRespawn.keySet();
							// Log.info("listKeys done.");

							for (Iterator<String> keyIterator = keySet.iterator(); keyIterator.hasNext();) {
								String playerNameKey = keyIterator.next();
								if (this.worldObj.getPlayerEntityByName(playerNameKey) != null) {
									long playerDeathTime = DeathCube.playerAwaitingRespawn.get(playerNameKey);
									currentTime = System.currentTimeMillis();
									long timeDiff = Reference.TIME_DEATH_PENALTY - (currentTime - playerDeathTime);
									// Log.info(timeDiff + " until Player " + playerNameKey + " respawns.");
									// Log.info("Death Penalty Queue Size: " + DeathCube.playerAwaitingRespawn.size());
									if (timeDiff <= 0) {
										// Log.info("Found Player waiting to respawn - time to spawn!");
										String teamColor = DeathCube.playerToTeamColor.get(playerNameKey);
										int teamIndex = DeathCube.teamColorToIndex.get(teamColor);
										DeathCube.gameTeams[teamIndex].sendPlayerToTeamSpawn(this.worldObj.getPlayerEntityByName(playerNameKey));
										keyIterator.remove();
									}
								} else {
									/**
									 * If there is a player in the queue, but they can't be found in the world:
									 * - Remove them from the queue
									 */
									DeathCube.playerAwaitingRespawn.remove(playerNameKey);
								}
							}
						}

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
										List<EntityPlayer> playerList = MinecraftServer.getServer().getConfigurationManager().playerEntityList;
										for (EntityPlayer player : playerList) {
											player.playSound("mob.enderdragon.end", 1.0f, 1.0f);
										}
										stopGame();
									} else {
										team.setNextCapturePointActive();
										if (team.getCurrentCaptureIndex() > mostPointsCaptured) {
											winningTeamColor = team.getTeamColor();
										}
									}
								}
							}
						} else {
							/**
							 * Check if all points have been captured.
							 */
						}
					}
					/**
					 * Tech added this section to update the game render helper variables to make the waypoints bounce & rotate.
					 */
					updateRenderHelpers();
				} else if (DeathCube.gameState == GameStates.PostGame) {
					/*****************************************************************************************
					 * 
					 * Post Game actions.
					 * 
					 *****************************************************************************************/
					/**
					 * Check Time
					 */
					currentTime = System.currentTimeMillis();
					timeRemaining = Reference.TIME_POSTGAME - (currentTime - DeathCube.gameTimeStart);

					if (timeRemaining <= 0) {
						/**
						 * Timer is Up - Return to Lobby GameState
						 */
						DeathCube.gameState = GameStates.Lobby;

						Log.info("Game now in Lobby.");
					}
				} else if (DeathCube.gameState == GameStates.GameOver) {
					/******************************************************************************************
					 * 
					 * Game Over actions.
					 * TODO: Vote on next Map?
					 * 
					 *****************************************************************************************/

					if (DeathCube.fieldState != FieldStates.Off)
						DeathCube.fieldState = FieldStates.Off;
				}
			} else {
				/******************************************************************************************
				 * Null Game State
				 *****************************************************************************************/
				Log.info("Null Gamestate - GameController Update() Position: " + this.pos.toString());

				DeathCube.gameState = GameStates.Lobby;
				DeathCube.fieldState = FieldStates.Inactive;

				Log.info("GameController States Initialized.  Text update request sent.");
			}

			if (DeathCube.firstServerTick && this.pos != null && this.pos != new BlockPos(0, 0, 0)) {
				DeathCube.gameControllerPos = this.pos;
				DeathCube.firstServerTick = false;
				Log.info("Found GameController Position: " + this.pos.toString());
				Log.info("Set Master GameController Position: " + DeathCube.gameControllerPos.toString());
			}

			updateClient();
		}
	}

	private void updateRenderHelpers() {

		if (worldObj.isRemote) {
			/*
			 * update Rotation
			 */
			DeathCube.renderHelperRotation += Reference.RENDER_HELPER_ROTATION_SPEED;
			if (DeathCube.renderHelperRotation > 360d) {
				DeathCube.renderHelperRotation = 0d;
			}
			/*
			 * Update Y offset
			 */

			if (DeathCube.renderHelperYDirection == -1) {
				DeathCube.renderHelperYOffset -= Reference.RENDER_HELPER_Y_OFFSET_SPEED;
				if (DeathCube.renderHelperYOffset < (Reference.RENDER_HELPER_MAX_Y_OFFSET * -1)) {
					DeathCube.renderHelperYDirection *= -1;

				}
			} else {
				DeathCube.renderHelperYOffset += Reference.RENDER_HELPER_Y_OFFSET_SPEED;
				if (DeathCube.renderHelperYOffset > Reference.RENDER_HELPER_MAX_Y_OFFSET) {
					DeathCube.renderHelperYDirection *= -1;
				}
			}
		}

	}

	public void updateClient() {
		if ((DeathCube.gameState == GameStates.Running || DeathCube.gameState == GameStates.PostGame) && DeathCube.gameTeams != null && DeathCube.gameTeams.length != 0) {
			/**
			 * Display Scoreboard. Store values and send updates only when something has changed.
			 */
			boolean sendUpdate = false;

			boolean displayScoreboard = false;
			String[] teamNames = new String[DeathCube.gameTeams.length];
			int[] activeTeamPoints = new int[DeathCube.gameTeams.length];
			double[] activeTeamPointTimes = new double[DeathCube.gameTeams.length];

			if (DeathCube.gameState == GameStates.Running) {
				for (int i = 0; i < DeathCube.gameTeams.length; i++) {
					teamNames[i] = DeathCube.gameTeams[i].getTeamColor();

					activeTeamPoints[i] = DeathCube.gameTeams[i].getCurrentCaptureIndex() + 1;

					TileEntityCapturePoint captureTE = (TileEntityCapturePoint) this.worldObj.getTileEntity(DeathCube.gameTeams[i].getCurrentPointPos());
					activeTeamPointTimes[i] = captureTE.getRemainingCaptureTime();
				}
				displayScoreboard = true;
			} else if (DeathCube.gameState == GameStates.PostGame) {
				/**
				 * 
				 * Game is over. Display the winner.
				 * - TODO: Use a different graphic + renderer. Don't display this in the scoreboard.
				 * - TODO: Display post-game statistics - maybe for a few seconds, then only if player is pressing a hot-key.
				 * 
				 */
				for (int i = 0; i < DeathCube.gameTeams.length; i++) {

					if (DeathCube.gameTeams[i].getTeamColor().equals(winningTeamColor)) {
						teamNames[i] = DeathCube.gameTeams[i].getTeamColor() + " Wins!";
					} else {
						teamNames[i] = DeathCube.gameTeams[i].getTeamColor();
					}

					activeTeamPoints[i] = 0;

					TileEntityCapturePoint captureTE = (TileEntityCapturePoint) this.worldObj.getTileEntity(DeathCube.gameTeams[i].getCurrentPointPos());
					activeTeamPointTimes[i] = 0;
				}
				displayScoreboard = true;
			}

			/**
			 * Check for Changes.
			 * - For first time, need to check if null.
			 */
			// last_teamNames = new String[DeathCube.gameTeams.length];
			// last_activeTeamPoints = new int[DeathCube.gameTeams.length];
			// last_activeTeamPointTimes = new double[DeathCube.gameTeams.length];

			if (last_displayScoreboard != displayScoreboard) {
				sendUpdate = true;
				Log.info("boolean displayScoreboard has changed.  Update scoreboard.");
			}

			/**
			 * Check Team Name Changes
			 */
			if (!sendUpdate) {
				if (last_teamNames == null) {
					sendUpdate = true;
					Log.error("last_teamNames is null.");
				} else {
					if (last_teamNames.length != DeathCube.gameTeams.length) {
						sendUpdate = true;
						Log.warn("Check - last_teamNames length is not equal to main GameTeams[] length.");
					} else {
						for (int i = 0; i < DeathCube.gameTeams.length; i++) {
							if (!teamNames[i].equals(last_teamNames[i])) {
								sendUpdate = true;
								Log.error("teamName " + i + " is different.  Update scoreboard.");
							}
						}
					}
				}
			}

			/**
			 * Check active capture point index changes
			 */
			if (!sendUpdate) {
				if (last_activeTeamPoints == null) {
					sendUpdate = true;
					Log.error("last_activeTeamPoints is null.");
				} else {
					if (last_activeTeamPoints.length != DeathCube.gameTeams.length) {
						sendUpdate = true;
						Log.warn("Check - last_activeTeamPoints length is not equal to main GameTeams[] length.");
					} else {
						for (int i = 0; i < DeathCube.gameTeams.length; i++) {
							if (activeTeamPoints[i] != last_activeTeamPoints[i]) {
								sendUpdate = true;
								Log.error("activeTeamPoint " + i + " is different.  Update scoreboard.");
							}
						}
					}
				}
			}

			/**
			 * Check capture point countdown changes
			 */
			if (!sendUpdate) {
				if (last_activeTeamPointTimes == null) {
					sendUpdate = true;
					Log.error("last_activeTeamPointTimes is null.");
				} else {
					if (last_activeTeamPointTimes.length != DeathCube.gameTeams.length) {
						sendUpdate = true;
						Log.warn("Check - last_activeTeamPointTimes length is not equal to main GameTeams[] length.");
					} else {
						for (int i = 0; i < DeathCube.gameTeams.length; i++) {
							if (activeTeamPointTimes[i] != last_activeTeamPointTimes[i]) {
								sendUpdate = true;
								// Log.error("Point Time " + i + " is different.  Update scoreboard.");
							}
						}
					}
				}
			}

			/**
			 * If time is more than 5 sec since last update, send update.
			 */
			if (!sendUpdate) {
				Long currentTime = System.currentTimeMillis();
				double timeCheck = ((double) (currentTime - DeathCube.gameTimeCheck)) / 1000.0f;

				if (timeCheck >= 5) {
					sendUpdate = true;
					// Log.info("5 seconds since last scoreboard update.");
				}
			}

			if (sendUpdate) {
				// Log.info("Sending scoreboard update...");
				ModNetwork.sendToAll(new MessageHandleClientGameUpdate(displayScoreboard, DeathCube.gameTeams.length, teamNames, activeTeamPoints, activeTeamPointTimes, DeathCube.gameTimeStart));
				DeathCube.gameTimeCheck = System.currentTimeMillis();
				// Log.info("Scoreboard update sent.");

				for (GameTeam team : DeathCube.gameTeams) {
					team.updateWaypoint();
				}

				last_displayScoreboard = displayScoreboard;
				last_teamNames = teamNames.clone();
				last_activeTeamPoints = activeTeamPoints.clone();
				last_activeTeamPointTimes = activeTeamPointTimes.clone();
			}
		}
	}

	public void startGame() {

		/************************************************************************************************
		 * Create Team Objects
		 * - Array of Possible Teams by Color
		 * - List of Colors used. (Not random - to make this random, will need to allow random color for
		 * all points and teams. Map making would have to associate a point with a team by another
		 * designator, e.g. A, B, C... - rather than color. Then, color would be assign during
		 * the StartGame() method.)
		 ************************************************************************************************/

		if (this.pos != null && this.pos != new BlockPos(0, 0, 0)) {
			DeathCube.gameControllerPos = this.pos;
		}

		/**
		 * TODO: Get list of Entities and kill all Waypoints.
		 */
		List<Entity> loadedEntities = MinecraftServer.getServer().getEntityWorld().loadedEntityList;
		for (Entity entity : loadedEntities) {
			if (entity instanceof EntityWaypoint) {
				entity.setDead();
				Log.info("Killed a waypoint.");
			}
		}

		List<String> foundColors = new ArrayList<String>();
		for (BlockPos spawnPos : this.spawnPointsList) {
			TileEntitySpawnPoint spawnTE = (TileEntitySpawnPoint) this.worldObj.getTileEntity(spawnPos);
			if (!foundColors.contains(spawnTE.spawnPointTeamColor)) {
				foundColors.add(spawnTE.spawnPointTeamColor);
			}
		}

		if (foundColors.size() < 2) {
			/**
			 * Map not properly set up. Must be more than 2
			 */
			Log.info("Less than two Spawn Point Colors found.");
			stopGame();
		} else if (foundColors.size() >= 2 && foundColors.size() < numTeamsFromGUI) {
			/**
			 * Map is not set up for as many teams as specified in GUI. Reset to number found.
			 */
			numTeamsInGame = foundColors.size();
			// send message to client gameControllers?
		} else {
			/**
			 * Found number equals GUI number or is greater. OK to play with fewer teams.
			 * - Do nothing.
			 */
			numTeamsInGame = numTeamsFromGUI;
		}

		mostPointsCaptured = 0;
		winningTeamColor = "trayer4";
		DeathCube.gameTeams = new GameTeam[numTeamsInGame];
		DeathCube.teamColorToIndex = new HashMap<String, Integer>();
		DeathCube.playerToTeamColor = new HashMap<String, String>();
		DeathCube.playerAwaitingRespawn = new HashMap<String, Long>();

		for (int i = 0; i < numTeamsInGame; i++) {
			String color = foundColors.get(i);

			DeathCube.gameTeams[i] = new GameTeam(color, this.worldObj);
			DeathCube.teamColorToIndex.put(color, i);

			Log.info("Team added, color: " + DeathCube.gameTeams[i].getTeamColor());
		}

		/*************************************************************************************************
		 * Add Spawn and Capture Points to GameTeam objects.
		 * - Make sure there is at least 1 point of each kind for each team.
		 ************************************************************************************************/
		Log.info("Spawn Point list size: " + spawnPointsList.size());
		Log.info("Capture Point list size: " + capturePointsList.size());
		Log.info("Gear TE list size: " + DeathCube.gearTEPos.size());

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
			List<BlockPos> tempCapturePointList = new ArrayList<BlockPos>();
			for (BlockPos pointPos : capturePointsList) {
				TileEntityCapturePoint lookupTE = (TileEntityCapturePoint) this.worldObj.getTileEntity(pointPos);
				Log.info("Capture Point color: " + lookupTE.getCapturePointTeamColor());
				if (lookupTE.getCapturePointTeamColor().equals(team.getTeamColor())) {
					tempCapturePointList.add(pointPos);
					Log.info("Point matches team!");
				}
			}

			if (tempCapturePointList.size() < 1) {
				Log.info("No Capture Points for Team: " + team.getTeamColor());
				stopGame();
				break;
			} else {
				Log.info("Found Capture Points for Team: " + team.getTeamColor());
			}

			/**
			 * Sort Capture Points by Point Order value.
			 */
			if (tempCapturePointList.size() > 1) {
				Log.info("Ordering Points ...");
				while (tempCapturePointList.size() > 0) {
					int indexOfLowest = 0;
					for (int i = 0; i < tempCapturePointList.size(); i++) {
						BlockPos iPos = tempCapturePointList.get(i);
						TileEntityCapturePoint iTE = (TileEntityCapturePoint) this.worldObj.getTileEntity(iPos);

						BlockPos lowPos = tempCapturePointList.get(indexOfLowest);
						TileEntityCapturePoint lowTE = (TileEntityCapturePoint) this.worldObj.getTileEntity(lowPos);

						if (iTE.captureOrderNumber < lowTE.captureOrderNumber) {
							indexOfLowest = i;
						}
					}

					Log.info("Capture Point: " + ((TileEntityCapturePoint) this.worldObj.getTileEntity(tempCapturePointList.get(indexOfLowest))).captureOrderNumber);
					team.addCapturePoint(tempCapturePointList.get(indexOfLowest));
					tempCapturePointList.remove(indexOfLowest);
				}
			} else {
				Log.info("Only 1 Point.");
				team.addCapturePoint(tempCapturePointList.get(0));
			}

			team.setNextCapturePointActive();
			Log.info("Point set active.");
		}

		/*************************************************************************************************
		 * Set up each player in the Game:
		 * - Assign to a Team
		 * - Set up Kills Score tracking
		 * - Spawn Player in Game
		 ************************************************************************************************/
		List<EntityPlayer> playerList = MinecraftServer.getServer().getConfigurationManager().playerEntityList;
		for (EntityPlayer player : playerList) {
			/**
			 * Assign Team.
			 */
			if (DeathCube.gameTeams != null && DeathCube.gameTeams.length > 1) {
				assignPlayerToTeam(player);
			} else {
				Log.info("Game Teams object is null or empty.");
			}

			/**
			 * TODO: Initialize Kill Streak Timers
			 */
			// killStreakTimer.put(p, 0);
			// killStreakMultiplier.put(p, 0);

			/**
			 * Teleport Players to Team Spawn Locations.
			 */
			String teamColor = DeathCube.playerToTeamColor.get(player.getName());
			int teamIndex = DeathCube.teamColorToIndex.get(teamColor);
			DeathCube.gameTeams[teamIndex].sendPlayerToTeamSpawn(player);

			Log.info("Player sent to Team Spawn.");
			/**
			 * Play sound at Game Start
			 * - TODO: Get Custom sound.
			 */
			player.playSound("mob.cow.hurt", 1.0f, 1.0f);
		}
	}

	public static void assignPlayerToTeam(EntityPlayer inPlayer) {
		/**
		 * Check if teams are balanced.
		 * - Loop through all teams and mark the smallest if not balanced.
		 */
		boolean teamsBalanced = true;
		String smallestTeamColor = DeathCube.gameTeams[0].getTeamColor();
		int smallestTeamSize = DeathCube.gameTeams[0].getTeamSize();
		for (int i = 1; i < DeathCube.gameTeams.length; i++) {
			Log.info("Team " + DeathCube.gameTeams[i].getTeamColor() + " size: " + DeathCube.gameTeams[i].getTeamSize());
			if (DeathCube.gameTeams[i].getTeamSize() < smallestTeamSize) {
				teamsBalanced = false;
				smallestTeamColor = DeathCube.gameTeams[i].getTeamColor();
				Log.info("Smallest Team: " + DeathCube.gameTeams[i].getTeamColor());
			}
		}

		/**
		 * If teams are equal, assign player to a Random Team
		 * 
		 * Otherwise, assign player to the smallest team.
		 * - What if there is a tie for the smallest team?
		 * - Currently, the last team (Yellow) will be populated last.
		 */
		if (teamsBalanced) {
			Random rand = new Random();
			int teamIndex = rand.nextInt(DeathCube.gameTeams.length);
			DeathCube.gameTeams[teamIndex].addPlayer(inPlayer);
			DeathCube.playerToTeamColor.put(inPlayer.getName(), DeathCube.gameTeams[teamIndex].getTeamColor());

			Log.info("Added player: " + inPlayer.getName() + " to random team: " + DeathCube.gameTeams[teamIndex].getTeamColor());
		} else {
			int smallestTeamIndex = DeathCube.teamColorToIndex.get(smallestTeamColor);
			DeathCube.gameTeams[smallestTeamIndex].addPlayer(inPlayer);
			DeathCube.playerToTeamColor.put(inPlayer.getName(), DeathCube.gameTeams[smallestTeamIndex].getTeamColor());

			Log.info("Added player: " + inPlayer.getName() + " to smallest team: " + DeathCube.gameTeams[smallestTeamIndex].getTeamColor());
		}
	}

	/**
	 * public static void sendPlayerToLobby(EntityPlayer inPlayer) {
	 * /**
	 * Teleport Players to Lobby Location.
	 *
	 * 
	 * if (DeathCube.lobbySpawnPos == null) {
	 * /**
	 * Get Lobby Spawn from data file?
	 *
	 * Log.info("GameController LobbyPosition: NULL");
	 * } else if (DeathCube.lobbySpawnPos == new BlockPos(0, 0, 0)) {
	 * Log.info("GameController LobbyPosition: " + DeathCube.lobbySpawnPos.toString());
	 * } else {
	 * preparePlayerToSpawn(inPlayer);
	 * //inPlayer.setPositionAndUpdate(DeathCube.lobbySpawnPos.getX() + 0.5d, DeathCube.lobbySpawnPos.getY() + 1, DeathCube.lobbySpawnPos.getZ() + 0.5d);
	 * //Log.info("GameController - Player sent to Lobby: " + DeathCube.lobbySpawnPos.toString());
	 * }
	 * }
	 */

	public void stopGame() {
		/**
		 * Stop Game Processing
		 * - Set all Capture Points to inactive.
		 * - Change GameState to PostGame
		 */
		for (GameTeam team : DeathCube.gameTeams) {
			team.setAllPointsActive(false);
			team.removeWaypoint();
		}

		DeathCube.gameTimeStart = System.currentTimeMillis();
		DeathCube.gameTimeCheck = System.currentTimeMillis();
		DeathCube.gameState = GameStates.PostGame;

		if (DeathCube.fieldState != FieldStates.Inactive)
			DeathCube.fieldState = FieldStates.Inactive;

		Log.info(winningTeamColor + " has won!");
	}
}