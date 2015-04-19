package com.projectreddog.deathcube.tileentity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Random;
import java.util.Set;

import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.gui.IUpdatePlayerListBox;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockPos;
import net.minecraft.world.WorldSettings;

import com.projectreddog.deathcube.DeathCube;
import com.projectreddog.deathcube.block.BlockForceField;
import com.projectreddog.deathcube.game.GameTeam;
import com.projectreddog.deathcube.init.ModNetwork;
import com.projectreddog.deathcube.network.MessageHandleTextUpdate;
import com.projectreddog.deathcube.reference.Reference;
import com.projectreddog.deathcube.reference.Reference.FieldStates;
import com.projectreddog.deathcube.reference.Reference.GameStates;
import com.projectreddog.deathcube.utility.Log;

public class TileEntityGameController extends TileEntityDeathCube implements IUpdatePlayerListBox {

	/**
	 * GUI Variables
	 */
	private int numTeamsFromGUI = 4;
	private int forceFieldx = 10;
	private int forceFieldz = 10;
	private int forceFieldyUp = 10;
	private int forceFieldyDown = 10;
	private int forceFieldStrength = 10;

	/**
	 * Spawn and Capture Point Variables
	 */
	private List<BlockPos> spawnPointsList = new ArrayList<BlockPos>();
	private List<BlockPos> capturePointsList = new ArrayList<BlockPos>();
	public static BlockPos lobbySpawnPos;

	/**
	 * Scoring Variables
	 */
	private String winningTeamColor = "trayer4";

	public TileEntityGameController() {
		
	}

	public void onTextRequest() {
		if (this.worldObj != null) {
			if (!this.worldObj.isRemote) {
				Log.info("Server sending requested text. Num points: " + numTeamsFromGUI);
				ModNetwork.simpleNetworkWrapper.sendToAll(new MessageHandleTextUpdate(this.pos, Reference.MESSAGE_FIELD1_ID, String.valueOf(numTeamsFromGUI)));
				ModNetwork.simpleNetworkWrapper.sendToAll(new MessageHandleTextUpdate(this.pos, Reference.MESSAGE_FIELD2_ID, String.valueOf(forceFieldx)));
				ModNetwork.simpleNetworkWrapper.sendToAll(new MessageHandleTextUpdate(this.pos, Reference.MESSAGE_FIELD3_ID, String.valueOf(forceFieldz)));
				ModNetwork.simpleNetworkWrapper.sendToAll(new MessageHandleTextUpdate(this.pos, Reference.MESSAGE_FIELD4_ID, String.valueOf(forceFieldyUp)));
				ModNetwork.simpleNetworkWrapper.sendToAll(new MessageHandleTextUpdate(this.pos, Reference.MESSAGE_FIELD5_ID, String.valueOf(forceFieldyDown)));
				ModNetwork.simpleNetworkWrapper.sendToAll(new MessageHandleTextUpdate(this.pos, Reference.MESSAGE_FIELD6_ID, String.valueOf(forceFieldStrength)));
				DeathCube.forceFieldStrength = this.forceFieldStrength;
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
		} else if (fieldID == Reference.MESSAGE_FIELD2_ID) {
			try {
				forceFieldx = Integer.parseInt(text);
				if (!this.worldObj.isRemote) {
					/**
					 * If a server message (not remote), update the Clients too.
					 */
					ModNetwork.simpleNetworkWrapper.sendToAll(new MessageHandleTextUpdate(this.pos, Reference.MESSAGE_FIELD2_ID, String.valueOf(forceFieldx)));
				}
			} catch (NumberFormatException e) {
				Log.warn("Tried to parse non-Integer: " + text);
			}
			markDirty();
		} else if (fieldID == Reference.MESSAGE_FIELD3_ID) {
			try {
				forceFieldz = Integer.parseInt(text);
				if (!this.worldObj.isRemote) {
					/**
					 * If a server message (not remote), update the Clients too.
					 */
					ModNetwork.simpleNetworkWrapper.sendToAll(new MessageHandleTextUpdate(this.pos, Reference.MESSAGE_FIELD3_ID, String.valueOf(forceFieldz)));
				}
			} catch (NumberFormatException e) {
				Log.warn("Tried to parse non-Integer: " + text);
			}
			markDirty();
		} else if (fieldID == Reference.MESSAGE_FIELD4_ID) {
			try {
				forceFieldyUp = Integer.parseInt(text);
				if (!this.worldObj.isRemote) {
					/**
					 * If a server message (not remote), update the Clients too.
					 */
					ModNetwork.simpleNetworkWrapper.sendToAll(new MessageHandleTextUpdate(this.pos, Reference.MESSAGE_FIELD4_ID, String.valueOf(forceFieldyUp)));
				}
			} catch (NumberFormatException e) {
				Log.warn("Tried to parse non-Integer: " + text);
			}
			markDirty();
		} else if (fieldID == Reference.MESSAGE_FIELD5_ID) {
			try {
				forceFieldyDown = Integer.parseInt(text);
				if (!this.worldObj.isRemote) {
					/**
					 * If a server message (not remote), update the Clients too.
					 */
					ModNetwork.simpleNetworkWrapper.sendToAll(new MessageHandleTextUpdate(this.pos, Reference.MESSAGE_FIELD5_ID, String.valueOf(forceFieldyDown)));
				}
			} catch (NumberFormatException e) {
				Log.warn("Tried to parse non-Integer: " + text);
			}
			markDirty();
		} else if (fieldID == Reference.MESSAGE_FIELD6_ID) {
			try {
				forceFieldStrength = Integer.parseInt(text);
				if (!this.worldObj.isRemote) {
					/**
					 * If a server message (not remote), update the Clients too.
					 */
					ModNetwork.simpleNetworkWrapper.sendToAll(new MessageHandleTextUpdate(this.pos, Reference.MESSAGE_FIELD6_ID, String.valueOf(forceFieldStrength)));
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
	
	public int getForceFieldx() {
		return forceFieldx;
	}
	
	public int getForceFieldz() {
		return forceFieldz;
	}
	
	public int getForceFieldyUp() {
		return forceFieldyUp;
	}
	
	public int getForceFieldyDown() {
		return forceFieldyDown;
	}
	
	public int getForceFieldStrength() {
		return forceFieldStrength;
	}

	@Override
	public void readFromNBT(NBTTagCompound tag) {
		super.readFromNBT(tag);
		numTeamsFromGUI = tag.getInteger("team");
		forceFieldx = tag.getInteger("x");
		forceFieldz = tag.getInteger("z");
		forceFieldyUp = tag.getInteger("y_up");
		forceFieldyDown = tag.getInteger("y_down");
		forceFieldStrength = tag.getInteger("strength");
		Log.info("Game Controller - NBT Read :: Number of Teams: " + numTeamsFromGUI);
	}

	@Override
	public void writeToNBT(NBTTagCompound tag) {
		super.writeToNBT(tag);
		tag.setInteger("team", numTeamsFromGUI);
		tag.setInteger("x", forceFieldx);
		tag.setInteger("z", forceFieldz);
		tag.setInteger("y_up", forceFieldyUp);
		tag.setInteger("y_down", forceFieldyDown);
		tag.setInteger("strength", forceFieldStrength);
		Log.info("Game Controller - NBT Write :: Number of Teams: " + numTeamsFromGUI);
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
					} else if (DeathCube.gameState == GameStates.Running) {
						stopGame();
					}
				}
			} else if (buttonID == Reference.BUTTON_3) {
				/**
				 * Force Field Button Pressed
				 * - Enable/Disable Force Field for entire game
				 * - Show/Hide text fields for Force Field
				 */
				Log.info("Server sees Force Field Button Pressed");
				if (DeathCube.useForceField) {
					DeathCube.useForceField = false;
				} else {
					DeathCube.useForceField = true;
				}
			} else if (buttonID == Reference.BUTTON_4) {
				/**
				 * Force Field Test Toggle Button Pressed
				 * - Turn Force Field on/off, if in lobby
				 */
				Log.info("Server sees Force Field Toggle Button Pressed");
				if (DeathCube.gameState == GameStates.Lobby) {
					if (DeathCube.fieldState == FieldStates.Off) {
						DeathCube.fieldState = FieldStates.Inactive;
						generateForceField();
					} else {
						DeathCube.fieldState = FieldStates.Off;
						removeForceField();
					}
					
				}
			}
		}
	}

	private void generateForceField() {
		/**
		 * Generate the Force Field according to the Dimensions from the GUI.
		 * - Need to know which side of the GameController to generate the field on.  New field/button?
		 * 
		 * TODO:  Figure out when to call this.  Don't want to check for changes to field state every update().
		 * - Flag on whether field is currently up?  Generate if false?
		 */
		int halfx, minx, maxx;
		int halfz, minz, maxz;
		int miny, maxy;
		BlockPos gameControllerPos = this.getPos();
		
		// Check if x and z field values are even.
		if(forceFieldx % 2 == 0) {
			// Even
			halfx = forceFieldx / 2;
		} else {
			// Odd
			halfx = (forceFieldx - 1) / 2;
			minx = gameControllerPos.getX() - halfx;
			maxx = gameControllerPos.getX() + halfx;
		}
		
		if(forceFieldz % 2 == 0) {
			// Even
			halfz = forceFieldz / 2;
		} else {
			// Odd
			halfz = (forceFieldz - 1) / 2;
			minz = gameControllerPos.getZ() - halfz;
			maxz = gameControllerPos.getZ() + halfz;
		}
		
		miny = gameControllerPos.getY() - forceFieldyDown;
		maxy = gameControllerPos.getY() - forceFieldyUp;
		
		// Select Area based on Min and Max coords and fill
		//  TODO:  Get block placing function, or try to use /command to select area of cube faces
		BlockPos tempPos = new BlockPos(gameControllerPos.north().up());
		BlockForceField forceFieldBlock = new BlockForceField();
		IBlockState state = forceFieldBlock.getDefaultState();
		Log.info("FF Default: " + state.toString());
		this.worldObj.setBlockState(tempPos, state);
		//this.worldObj.setBlockState(tempPos, Blocks.bedrock.getDefaultState());
		
		BlockPos startingX = new BlockPos(gameControllerPos.north().west(minx);
		BlockPos endingX = new BlockPos(gameControllerPos.north().east(maxx);
		BlockPos currentX = startingX;
		
		while(currentX.getX() <= maxx) {
			if(this.worldObj.getBlockFromPos(currentX).type == AIR){
				this.worldObj.setBlockState(tempPos, Blocks.bedrock.getDefaultState());
				currentX = currentX.east();
			}
		}
		
	}
	
	private void removeForceField() {
		/**
		 * Remove the Force Field
		 * - May need to store "Current Force Field Dimensions" to know where to remove field.
		 * 
		 * TODO:  Remove field when turned off.  Also, remove every time dimensions are changed.
		 */
		BlockPos gameControllerPos = this.getPos();
		
		BlockPos tempPos = new BlockPos(gameControllerPos.north().up());
		this.worldObj.setBlockToAir(tempPos);
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
					DeathCube.gameTimer = Reference.TIME_WARMUP;
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
				 * Process queue of Players waiting to respawn.
				 */
				if(DeathCube.playerAwaitingRespawn.size() > 0) {
					Log.info("Player serving Death Penalty");
					Set<String> keySet = DeathCube.playerAwaitingRespawn.keySet();
					Log.info("listKeys done.");
					Iterator<String> keyIterator = keySet.iterator();
					Log.info("keyIterator done.");
					while(keyIterator.hasNext()) {
						String playerNameKey = keyIterator.next();
						Long playerDeathTime = DeathCube.playerAwaitingRespawn.get(playerNameKey);
						Long currentTime = System.currentTimeMillis();
						Long timeDiff = Reference.TIME_DEATH_PENALTY - (currentTime - playerDeathTime);
						Log.info(timeDiff + " until Player " + playerNameKey + " respawns.");
						Log.info("Death Penalty Queue Size: " + DeathCube.playerAwaitingRespawn.size());
						if(timeDiff <= 0) {
							Log.info("Found Player waiting to respawn - time to spawn!");
							sendPlayerToTeamSpawn(this.worldObj.getPlayerEntityByName(playerNameKey));
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
				if (DeathCube.gameTimer < 0 || DeathCube.gameTimer > Reference.TIME_POSTGAME) {
					DeathCube.gameTimer = Reference.TIME_POSTGAME;

					if (DeathCube.fieldState != FieldStates.Inactive)
						DeathCube.fieldState = FieldStates.Inactive;

					Log.info("Game has ended.");
				} else if (DeathCube.gameTimer > 0 && DeathCube.gameTimer <= Reference.TIME_POSTGAME) {
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
		DeathCube.playerAwaitingRespawn = new HashMap<String, Long>();
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

			DeathCube.gameTeams[i] = new GameTeam(color, this.worldObj);
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
			 * Assign Team.
			 */
			assignPlayerToTeam(player);

			/**
			 * TODO: Initialize Kill Streak Timers
			 */
			// killStreakTimer.put(p, 0);
			// killStreakMultiplier.put(p, 0);

			/**
			 * Teleport Players to Team Spawn Locations.
			 */
			sendPlayerToTeamSpawn(player);

			Log.info("Player sent to Team Spawn.");
			/**
			 * Play sound at Game Start
			 * - TODO: Get Custom sound. This call doesn't work.
			 */
			player.playSound("mob.bat.death", 1.0f, 1.0f);
		}
	}
	
	public static void assignPlayerToTeam(EntityPlayer inPlayer) {
		/**
		 * Check if teams are balanced.
		 */
		boolean teamsBalanced = true;
		String smallestTeam = DeathCube.gameTeams[0].getTeamColor();
		int smallestTeamSize = DeathCube.gameTeams[0].getTeamSize();
		for(int i = 1; i < DeathCube.gameTeams.length; i++) {
			/**
			 * Loop through all teams.
			 * - If one has a smaller size, mark it as the smallest.
			 */
			if(DeathCube.gameTeams[i].getTeamSize() < smallestTeamSize) {
				teamsBalanced = false;
				smallestTeam = DeathCube.gameTeams[i].getTeamColor();
			}
		}
		
		if(teamsBalanced) {
			/**
			 * If teams are equal, assign player to a Random Team
			 */
			Random rand = new Random();
			int teamIndex = rand.nextInt(DeathCube.gameTeams.length);
			DeathCube.gameTeams[teamIndex].addPlayer(inPlayer);
			DeathCube.playerToTeamColor.put(inPlayer.getName(), DeathCube.gameTeams[teamIndex].getTeamColor());

			Log.info("Added player: " + inPlayer.getName() + " to random team: " + DeathCube.gameTeams[teamIndex].getTeamColor());
		} else {
			/**
			 * Otherwise, assign player to the smallest team.
			 * - What if there is a tie for the smallest team?
			 * - Currently, the last team (Yellow) will be populated last.
			 */
			int smallestTeamIndex = DeathCube.teamColorToIndex.get(smallestTeam);
			DeathCube.gameTeams[smallestTeamIndex].addPlayer(inPlayer);
			DeathCube.playerToTeamColor.put(inPlayer.getName(), DeathCube.gameTeams[smallestTeamIndex].getTeamColor());
			
			Log.info("Added player: " + inPlayer.getName() + " to smallest team: " + DeathCube.gameTeams[smallestTeamIndex].getTeamColor());
		}
	}
	
	public static void sendPlayerToLobby(EntityPlayer inPlayer) {
		/**
		 * Teleport Players to Team Spawn Locations.
		 */
		preparePlayerToSpawn(inPlayer);
		
		inPlayer.setPositionAndUpdate(lobbySpawnPos.getX() + 0.5d, lobbySpawnPos.getY() + 1, lobbySpawnPos.getZ() + 0.5d);
		Log.info("GameController LobbyPosition: " + lobbySpawnPos.getX() + "x, " + lobbySpawnPos.getY() + "y, " + lobbySpawnPos.getZ() + "z");
	}
	
	public static void sendPlayerToTeamSpawn(EntityPlayer inPlayer) {
		/**
		 * Teleport Players to Team Spawn Locations.
		 */
		preparePlayerToSpawn(inPlayer);
		givePlayerGear(inPlayer);
		Log.info("Player attributes set.  Gear given");
		
		String teamColor = DeathCube.playerToTeamColor.get(inPlayer.getName()); 
		int teamIndex = DeathCube.teamColorToIndex.get(teamColor);
		BlockPos spawnLocation = DeathCube.gameTeams[teamIndex].getSpawnLocation();
		inPlayer.setPositionAndUpdate(spawnLocation.getX() + 0.5d, spawnLocation.getY() + 1, spawnLocation.getZ() + 0.5d);
	}
	
	public static void preparePlayerToSpawn(EntityPlayer inPlayer) {
		/**
		 * Prepare Player to Spawn in Game or Lobby:
		 * - Set velocity to zero to avoid death falling.
		 * - Clear any potion effects from Lobby time.
		 * - Set to full health and saturation to help hunger.
		 * - Clear inventory.  How to do this?  TODO
		 */
		inPlayer.setGameType(WorldSettings.GameType.SURVIVAL);
		inPlayer.setVelocity(0, 0, 0);
		inPlayer.fallDistance = 0;
		inPlayer.clearActivePotions();
		inPlayer.extinguish();
		inPlayer.setHealth(inPlayer.getMaxHealth());
		inPlayer.addPotionEffect(new PotionEffect(Potion.saturation.getId(),10));
	}
	
	public static void givePlayerGear(EntityPlayer inPlayer) {
		/**
		 * Give Player Gear
		 * - Stone Sword
		 * - Leather Armor (in team color)
		 * - Bow
		 * - Arrows
		 * - Splash Potion of Poison
		 * 
		 ****--This should be customizable.  Use TE with inventory.  Copy inventory to player on respawn.
		 */
		
	}

	public void stopGame() {
		/**
		 * TODO: Stop Game Processing
		 * - Set all Capture Points to inactive.
		 * - Change Spawn Point to Lobby Area?
		 * - Change GameState to PostGame
		 */
		for (GameTeam team : DeathCube.gameTeams) {
			team.setAllPointsActive(false);
		}

		DeathCube.gameTimer = -1;
		DeathCube.gameState = GameStates.PostGame;

		Log.info(winningTeamColor + " has won!");
	}
}
