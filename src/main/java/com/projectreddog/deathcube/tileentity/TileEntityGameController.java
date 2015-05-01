package com.projectreddog.deathcube.tileentity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Random;
import java.util.Set;

import net.minecraft.block.Block;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.EnumDyeColor;
import net.minecraft.item.ItemDye;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.gui.IUpdatePlayerListBox;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.WorldSettings;

import com.projectreddog.deathcube.DeathCube;
import com.projectreddog.deathcube.entity.EntityWaypoint;
import com.projectreddog.deathcube.game.GameTeam;
import com.projectreddog.deathcube.init.ModBlocks;
import com.projectreddog.deathcube.init.ModConfig;
import com.projectreddog.deathcube.init.ModNetwork;
import com.projectreddog.deathcube.network.MessageHandleClientGameUpdate;
import com.projectreddog.deathcube.network.MessageHandleTextUpdate;
import com.projectreddog.deathcube.network.MessageRequestTextUpdate_Client;
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
	private int forceFieldx = Reference.FORCE_FIELD_MIN_DIMENSION;
	private int forceFieldz = Reference.FORCE_FIELD_MIN_DIMENSION;
	private int forceFieldyUp = Reference.FORCE_FIELD_MIN_DIMENSION;
	private int forceFieldyDown = Reference.FORCE_FIELD_MIN_DIMENSION;
	private int forceFieldStrength = 100;

	long currentTime;
	long timeRemaining;
	
	/**
	 * Spawn and Capture Point Variables
	 */
	private List<BlockPos> spawnPointsList = new ArrayList<BlockPos>();
	private List<BlockPos> capturePointsList = new ArrayList<BlockPos>();
	private Block forceFieldBlock = ModBlocks.forcefield;
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
		}
		
		Log.info("GameController Constructor Call.  GameState: " + DeathCube.gameState);
	}

	public void onTextRequest() {
		if (this.worldObj != null) {
			if (!this.worldObj.isRemote) {
				Log.info("Server sending requested text. Num points: " + numTeamsFromGUI);
				ModNetwork.sendToAll(new MessageHandleTextUpdate(this.pos, Reference.MESSAGE_FIELD1_ID, String.valueOf(numTeamsFromGUI)));
				ModNetwork.sendToAll(new MessageHandleTextUpdate(this.pos, Reference.MESSAGE_FIELD2_ID, String.valueOf(forceFieldx)));
				ModNetwork.sendToAll(new MessageHandleTextUpdate(this.pos, Reference.MESSAGE_FIELD3_ID, String.valueOf(forceFieldz)));
				ModNetwork.sendToAll(new MessageHandleTextUpdate(this.pos, Reference.MESSAGE_FIELD4_ID, String.valueOf(forceFieldyUp)));
				ModNetwork.sendToAll(new MessageHandleTextUpdate(this.pos, Reference.MESSAGE_FIELD5_ID, String.valueOf(forceFieldyDown)));
				ModNetwork.sendToAll(new MessageHandleTextUpdate(this.pos, Reference.MESSAGE_FIELD6_ID, String.valueOf(forceFieldStrength)));
				ModNetwork.sendToAll(new MessageHandleTextUpdate(this.pos, Reference.MESSAGE_FIELD7_ID, String.valueOf(mapName)));
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
		 * Text Fields
		 * - private GuiTextField text_NumTeams;
		 * - private GuiTextField text_ForceFieldx;
		 * - private GuiTextField text_ForceFieldz;
		 * - private GuiTextField text_ForceFieldyUp;
		 * - private GuiTextField text_ForceFieldyDown;
		 * - private GuiTextField text_ForceFieldStrength;
		 */
		Log.info("Game Controller sees Text Update: " + text);
		toggleForceField(false);
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
		} else if (fieldID == Reference.MESSAGE_FIELD2_ID) {
			try {
				forceFieldx = Integer.parseInt(text);
				if (!this.worldObj.isRemote) {
					/**
					 * If a server message (not remote), update the Clients too.
					 */
					ModNetwork.sendToAll(new MessageHandleTextUpdate(this.pos, Reference.MESSAGE_FIELD2_ID, String.valueOf(forceFieldx)));
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
					ModNetwork.sendToAll(new MessageHandleTextUpdate(this.pos, Reference.MESSAGE_FIELD3_ID, String.valueOf(forceFieldz)));
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
					ModNetwork.sendToAll(new MessageHandleTextUpdate(this.pos, Reference.MESSAGE_FIELD4_ID, String.valueOf(forceFieldyUp)));
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
					ModNetwork.sendToAll(new MessageHandleTextUpdate(this.pos, Reference.MESSAGE_FIELD5_ID, String.valueOf(forceFieldyDown)));
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
					ModNetwork.sendToAll(new MessageHandleTextUpdate(this.pos, Reference.MESSAGE_FIELD6_ID, String.valueOf(forceFieldStrength)));
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
		mapName = tag.getString("map");
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
		tag.setString("map", mapName);
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
						DeathCube.gameTimeStart = System.currentTimeMillis();
						DeathCube.gameTimeCheck = System.currentTimeMillis();
						Log.info("Game now Warming Up.");
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

				Log.info("Force Field: " + DeathCube.useForceField);

			} else if (buttonID == Reference.BUTTON_4) {
				/**
				 * Force Field Test Toggle Button Pressed
				 * - Turn Force Field on/off, if in lobby
				 */
				Log.info("Server sees Force Field Toggle Button Pressed");
				if (DeathCube.gameState == GameStates.Lobby) {
					if (DeathCube.fieldState == FieldStates.Off) {
						DeathCube.fieldState = FieldStates.Inactive;
						toggleForceField(true);
					} else {
						DeathCube.fieldState = FieldStates.Off;
						toggleForceField(false);
					}
				}
			}
		}
	}

	private void toggleForceField(boolean generateCube) {
		if (!this.worldObj.isRemote) {
			/**
			 * Generate the Force Field according to the Dimensions from the GUI.
			 * 
			 * TODO: Figure out when to call this. Don't want to check for changes to field state every update().
			 * - Flag on whether field is currently up? Generate if false?
			 * 
			 * TODO: Determine which direction to generate Force Field.  
			 * - Use input field?
			 * - Or, make block directional.
			 */

			int halfx, halfz;
			int miny, maxy;
			BlockPos currentPos1, currentPos2;
			BlockPos startingPos, endingPos;
			BlockPos gameControllerPos = this.getPos();

			/**
			 * Double-check Force Field Dimensions:
			 */
			if(forceFieldx < Reference.FORCE_FIELD_MIN_DIMENSION) {
				forceFieldx = Reference.FORCE_FIELD_MIN_DIMENSION;
			} else if (forceFieldx > Reference.FORCE_FIELD_MAX_DIMENSION) {
				forceFieldx = Reference.FORCE_FIELD_MAX_DIMENSION;
			}
			if(forceFieldz < Reference.FORCE_FIELD_MIN_DIMENSION) {
				forceFieldz = Reference.FORCE_FIELD_MIN_DIMENSION;
			} else if (forceFieldz > Reference.FORCE_FIELD_MAX_DIMENSION) {
				forceFieldz = Reference.FORCE_FIELD_MAX_DIMENSION;
			}
			if(forceFieldyUp < Reference.FORCE_FIELD_MIN_DIMENSION) {
				forceFieldyUp = Reference.FORCE_FIELD_MIN_DIMENSION;
			} else if (forceFieldyUp > Reference.FORCE_FIELD_MAX_DIMENSION_Y) {
				forceFieldyUp = Reference.FORCE_FIELD_MAX_DIMENSION_Y;
			}
			if(forceFieldyDown < Reference.FORCE_FIELD_MIN_DIMENSION) {
				forceFieldyDown = Reference.FORCE_FIELD_MIN_DIMENSION;
			} else if (forceFieldyDown > Reference.FORCE_FIELD_MAX_DIMENSION_Y) {
				forceFieldyDown = Reference.FORCE_FIELD_MAX_DIMENSION_Y;
			}
			if(forceFieldStrength < Reference.FORCE_FIELD_MIN_STRENGTH) {
				forceFieldStrength = Reference.FORCE_FIELD_MIN_STRENGTH;
			} else if (forceFieldStrength > Reference.FORCE_FIELD_MAX_STRENGTH) {
				forceFieldStrength = Reference.FORCE_FIELD_MAX_STRENGTH;
			}
			
			Log.info("GameControllerPos: " + gameControllerPos);
			Log.info("Force Field X-width: " + forceFieldx);
			Log.info("Force Field Z-width: " + forceFieldz);

			if (forceFieldx % 2 == 0) {
				// Even X
				halfx = forceFieldx / 2;
				Log.info("Force Field X is Even");
			} else {
				// Odd X
				halfx = (forceFieldx - 1) / 2;
				Log.info("Force Field X is Odd");
			}

			if (forceFieldz % 2 == 0) {
				// Even Z
				halfz = forceFieldz / 2;
			} else {
				// Odd Z
				halfz = (forceFieldz - 1) / 2;
			}

			/**
			 * North Wall
			 */
			startingPos = new BlockPos(gameControllerPos.north(forceFieldz + 1).west(halfx).down(forceFieldyDown));
			endingPos = new BlockPos(gameControllerPos.north(forceFieldz + 1).east(halfx).up(forceFieldyUp));

			Log.info("Half X: " + halfx);
			Log.info("Y Up: " + forceFieldyUp + " - Y Down: " + forceFieldyDown);
			Log.info("Starting Pos: " + startingPos.toString());
			Log.info("Ending Pos: " + endingPos.toString());

			westToEastWall(startingPos, endingPos, generateCube);
			
			/**
			 * South Wall
			 */
			startingPos = new BlockPos(gameControllerPos.north().west(halfx).down(forceFieldyDown));
			endingPos = new BlockPos(gameControllerPos.north().east(halfx).up(forceFieldyUp));

			Log.info("Half X: " + halfx);
			Log.info("Y Up: " + forceFieldyUp + " - Y Down: " + forceFieldyDown);
			Log.info("Starting Pos: " + startingPos.toString());
			Log.info("Ending Pos: " + endingPos.toString());

			westToEastWall(startingPos, endingPos, generateCube);

			/**
			 * West Wall
			 */
			startingPos = new BlockPos(gameControllerPos.north(forceFieldz + 1).west(halfx).down(forceFieldyDown));
			endingPos = new BlockPos(gameControllerPos.north().west(halfx).up(forceFieldyUp));

			Log.info("Half Z: " + halfz);
			Log.info("Y Up: " + forceFieldyUp + " - Y Down: " + forceFieldyDown);
			Log.info("Starting Pos: " + startingPos.toString());
			Log.info("Ending Pos: " + endingPos.toString());

			northToSouthWall(startingPos, endingPos, generateCube);
			
			/**
			 * East Wall
			 */
			startingPos = new BlockPos(gameControllerPos.north(forceFieldz + 1).east(halfx).down(forceFieldyDown));
			endingPos = new BlockPos(gameControllerPos.north().east(halfx).up(forceFieldyUp));

			Log.info("Half Z: " + halfz);
			Log.info("Y Up: " + forceFieldyUp + " - Y Down: " + forceFieldyDown);
			Log.info("Starting Pos: " + startingPos.toString());
			Log.info("Ending Pos: " + endingPos.toString());

			northToSouthWall(startingPos, endingPos, generateCube);

			/**
			 * Top Cube Face
			 */
			startingPos = new BlockPos(gameControllerPos.north(forceFieldz + 1).west(halfx).up(forceFieldyUp));
			endingPos = new BlockPos(gameControllerPos.north().east(halfx).up(forceFieldyUp));

			Log.info("Half X: " + halfx);
			Log.info("Half Z: " + halfz);
			Log.info("Y Up: " + forceFieldyUp + " - Y Down: " + forceFieldyDown);
			Log.info("Starting Pos: " + startingPos.toString());
			Log.info("Ending Pos: " + endingPos.toString());

			topOrBottomWall(startingPos, endingPos, generateCube);
			
			/**
			 * Bottom Cube Face
			 */
			startingPos = new BlockPos(gameControllerPos.north(forceFieldz + 1).west(halfx).down(forceFieldyDown));
			endingPos = new BlockPos(gameControllerPos.north().east(halfx).down(forceFieldyDown));

			Log.info("Half X: " + halfx);
			Log.info("Half Z: " + halfz);
			Log.info("Y Up: " + forceFieldyUp + " - Y Down: " + forceFieldyDown);
			Log.info("Starting Pos: " + startingPos.toString());
			Log.info("Ending Pos: " + endingPos.toString());

			topOrBottomWall(startingPos, endingPos, generateCube);
		}
	}

	public void westToEastWall(BlockPos startingPos, BlockPos endingPos, boolean generate) {

		BlockPos currentPosY, currentPosX;
		Block currentBlock;

		if (generate) {
			currentBlock = Blocks.air;
		} else {
			currentBlock = forceFieldBlock;
		}
		/**
		 * West-to-East Wall Generation
		 * - West = Negative X
		 * - East = Positive X
		 */
		for (currentPosY = startingPos; currentPosY.getY() <= endingPos.getY(); currentPosY = currentPosY.up()) {
			for (currentPosX = currentPosY; currentPosX.getX() <= endingPos.getX(); currentPosX = currentPosX.east()) {
				if (this.worldObj.getBlockState(currentPosX).getBlock() == currentBlock) {
					if (generate) {
						this.worldObj.setBlockState(currentPosX, forceFieldBlock.getDefaultState());
					} else {
						this.worldObj.setBlockToAir(currentPosX);
					}
				}

				// Debug
				if (currentPosY.getY() % 5 == 0 && currentPosX.getX() % 5 == 0) {
					if (generate) {
						Log.info("YX Block-Gen Position: " + currentPosX.toString());
					} else {
						Log.info("YX Block-Remove Position: " + currentPosX.toString());
					}

				}
			}
		}
	}

	public void northToSouthWall(BlockPos startingPos, BlockPos endingPos, boolean generate) {

		BlockPos currentPosY, currentPosZ;
		Block currentBlock;

		if (generate) {
			currentBlock = Blocks.air;
		} else {
			currentBlock = forceFieldBlock;
		}
		/**
		 * North-to-South Wall Generation
		 * - West = Negative X
		 * - East = Positive X
		 * - North = Negative Z
		 * - South = Positive Z
		 */
		for (currentPosY = startingPos; currentPosY.getY() <= endingPos.getY(); currentPosY = currentPosY.up()) {
			for (currentPosZ = currentPosY; currentPosZ.getZ() <= endingPos.getZ(); currentPosZ = currentPosZ.south()) {
				if (this.worldObj.getBlockState(currentPosZ).getBlock() == currentBlock) {
					if (generate) {
						this.worldObj.setBlockState(currentPosZ, forceFieldBlock.getDefaultState());
					} else {
						this.worldObj.setBlockToAir(currentPosZ);
					}
				}

				// Debug
				if (currentPosY.getY() % 5 == 0 && currentPosZ.getZ() % 5 == 0) {
					if (generate) {
						Log.info("YZ Block-Gen Position: " + currentPosZ.toString());
					} else {
						Log.info("YZ Block-Remove Position: " + currentPosZ.toString());
					}
				}
			}
		}
	}

	public void topOrBottomWall(BlockPos startingPos, BlockPos endingPos, boolean generate) {

		BlockPos currentPosX, currentPosZ;
		Block currentBlock;

		if (generate) {
			currentBlock = Blocks.air;
		} else {
			currentBlock = forceFieldBlock;
		}
		/**
		 * North-to-South Wall Generation
		 * - North = Negative Z
		 * - South = Positive Z
		 */
		for (currentPosX = startingPos; currentPosX.getX() <= endingPos.getX(); currentPosX = currentPosX.east()) {
			for (currentPosZ = currentPosX; currentPosZ.getZ() <= endingPos.getZ(); currentPosZ = currentPosZ.south()) {
				if (this.worldObj.getBlockState(currentPosZ).getBlock() == currentBlock) {
					if (generate) {
						this.worldObj.setBlockState(currentPosZ, forceFieldBlock.getDefaultState());
					} else {
						this.worldObj.setBlockToAir(currentPosZ);
					}
				}

				// Debug
				if (currentPosX.getX() % 5 == 0 && currentPosZ.getZ() % 5 == 0) {
					if (generate) {
						Log.info("XZ Block-Gen Position: " + currentPosZ.toString());
					} else {
						Log.info("XZ Block-Remove Position: " + currentPosZ.toString());
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
					 *   in current DeathCube Force Field boundaries.
					 *   
					 * Better way/time to count and track these things?
					 * 
					 * ######  Not currently tracking at DeathCube.java level.  Just locally.  ######
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
						DeathCube.gameTimeStart = System.currentTimeMillis();;
						DeathCube.gameState = GameStates.Running;
						
						if (DeathCube.fieldState != FieldStates.Active)
							DeathCube.fieldState = FieldStates.Active;

						Log.info("Game now Running.");

						startGame();
					} else {
						/**
						 * TODO: Periodically Broadcast Time Until Game Start ?
						 */
						//Log.info("GameState: " + DeathCube.gameState + " - Time Remaining: " + timeRemaining);
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
							Log.info("Player serving Death Penalty");
							Set<String> keySet = DeathCube.playerAwaitingRespawn.keySet();
							//Log.info("listKeys done.");
							Iterator<String> keyIterator = keySet.iterator();
							//Log.info("keyIterator done.");
							while (keyIterator.hasNext()) {
								String playerNameKey = keyIterator.next();
								if(this.worldObj.getPlayerEntityByName(playerNameKey) != null) {
									long playerDeathTime = DeathCube.playerAwaitingRespawn.get(playerNameKey);
									currentTime = System.currentTimeMillis();
									long timeDiff = Reference.TIME_DEATH_PENALTY - (currentTime - playerDeathTime);
									Log.info(timeDiff + " until Player " + playerNameKey + " respawns.");
									Log.info("Death Penalty Queue Size: " + DeathCube.playerAwaitingRespawn.size());
									if (timeDiff <= 0) {
										Log.info("Found Player waiting to respawn - time to spawn!");
										sendPlayerToTeamSpawn(this.worldObj.getPlayerEntityByName(playerNameKey));
										DeathCube.playerAwaitingRespawn.remove(playerNameKey);
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
										if(team.getCurrentCaptureIndex() > mostPointsCaptured) {
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

			/******************************************************************************************
			 * Other Actions:
			 * - Make it always Day - Change gamerule once, set time once?
			 * - Make it never raining - Could use a flag on processing weather timer?
			 *****************************************************************************************/

			if (MinecraftServer.getServer().worldServers[0].getWorldInfo().getCleanWeatherTime() <= 10) {
				MinecraftServer.getServer().worldServers[0].getWorldInfo().setCleanWeatherTime(5000);
				MinecraftServer.getServer().worldServers[0].getWorldInfo().setRaining(false);
				MinecraftServer.getServer().worldServers[0].getWorldInfo().setThundering(false);
			}
			
			updateClient();
		}
	}

	public void updateClient() {
		if((DeathCube.gameState == GameStates.Running || DeathCube.gameState == GameStates.PostGame) && DeathCube.gameTeams != null && DeathCube.gameTeams.length != 0) {
			/**
			 * Display Scoreboard.  Store values and send updates only when something has changed.
			 */
			boolean sendUpdate = false;
			
			boolean displayScoreboard = false;
			String[] teamNames = new String[DeathCube.gameTeams.length];
			int[] activeTeamPoints = new int[DeathCube.gameTeams.length];
			double[] activeTeamPointTimes = new double[DeathCube.gameTeams.length];
			
			if(DeathCube.gameState == GameStates.Running) {
				for(int i = 0; i < DeathCube.gameTeams.length; i++) {
					teamNames[i] = DeathCube.gameTeams[i].getTeamColor();
					
					activeTeamPoints[i] = DeathCube.gameTeams[i].getCurrentCaptureIndex() + 1;
					
					TileEntityCapturePoint captureTE = (TileEntityCapturePoint) this.worldObj.getTileEntity(DeathCube.gameTeams[i].getCurrentPointPos());
					activeTeamPointTimes[i] = captureTE.getRemainingCaptureTime();
				}
				displayScoreboard = true;
			} else if(DeathCube.gameState == GameStates.PostGame) {
				/**
				 * 
				 * Game is over. Display the winner.
				 * - TODO: Use a different graphic + renderer.  Don't display this in the scoreboard.
				 * - TODO: Display post-game statistics - maybe for a few seconds, then only if player is pressing a hot-key.
				 * 
				 */
				for(int i = 0; i < DeathCube.gameTeams.length; i++) {
					
					if(DeathCube.gameTeams[i].getTeamColor().equals(winningTeamColor)) {
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
			//last_teamNames = new String[DeathCube.gameTeams.length];
			//last_activeTeamPoints = new int[DeathCube.gameTeams.length];
			//last_activeTeamPointTimes = new double[DeathCube.gameTeams.length];
			
			if(last_displayScoreboard != displayScoreboard) {
				sendUpdate = true;
				Log.info("boolean displayScoreboard has changed.  Update scoreboard.");
			}
			
			/**
			 * Check Team Name Changes
			 */
			if(!sendUpdate) {
				if(last_teamNames == null) {
					sendUpdate = true;
					Log.error("last_teamNames is null.");
				} else {
					if(last_teamNames.length != DeathCube.gameTeams.length) {
						sendUpdate = true;
						Log.warn("Check - last_teamNames length is not equal to main GameTeams[] length.");
					} else {
						for(int i = 0; i < DeathCube.gameTeams.length; i++) {
							if(!teamNames[i].equals(last_teamNames[i])) {
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
			if(!sendUpdate) {
				if(last_activeTeamPoints == null) {
					sendUpdate = true;
					Log.error("last_activeTeamPoints is null.");
				} else {
					if(last_activeTeamPoints.length != DeathCube.gameTeams.length) {
						sendUpdate = true;
						Log.warn("Check - last_activeTeamPoints length is not equal to main GameTeams[] length.");
					} else {
						for(int i = 0; i < DeathCube.gameTeams.length; i++) {
							if(activeTeamPoints[i] !=last_activeTeamPoints[i]) {
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
			if(!sendUpdate) {
				if(last_activeTeamPointTimes == null) {
					sendUpdate = true;
					Log.error("last_activeTeamPointTimes is null.");
				} else {
					if(last_activeTeamPointTimes.length != DeathCube.gameTeams.length) {
						sendUpdate = true;
						Log.warn("Check - last_activeTeamPointTimes length is not equal to main GameTeams[] length.");
					} else {
						for(int i = 0; i < DeathCube.gameTeams.length; i++) {
							if(activeTeamPointTimes[i] != last_activeTeamPointTimes[i]) {
								sendUpdate = true;
								Log.error("Point Time " + i + " is different.  Update scoreboard.");
							}
						}
					}
				}
			}
			
			/**
			 * If time is more than 5 sec since last update, send update.
			 */
			if(!sendUpdate) {
				Long currentTime = System.currentTimeMillis();
				double timeCheck = ((double) (currentTime - DeathCube.gameTimeCheck)) / 1000.0f;
				
				if(timeCheck >= 5) {
					sendUpdate = true;
					Log.info("5 seconds since last scoreboard update.");
				}
			}
			
			if(sendUpdate) {
				Log.info("Sending scoreboard update...");
				ModNetwork.sendToAll(new MessageHandleClientGameUpdate(displayScoreboard, DeathCube.gameTeams.length, teamNames, activeTeamPoints, activeTeamPointTimes, DeathCube.gameTimeStart));
				DeathCube.gameTimeCheck = System.currentTimeMillis();
				Log.info("Scoreboard update sent.");
				
				for(GameTeam team : DeathCube.gameTeams) {
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
		
		/**
		 * TODO: Get list of Entities and kill all Waypoints.
		 */
		List<Entity> loadedEntities = MinecraftServer.getServer().getEntityWorld().loadedEntityList;
		for(Entity entity : loadedEntities) {
			if(entity instanceof EntityWaypoint) {
				entity.setDead();
				Log.info("Killed a waypoint.");
			}
		}
		
		
		List<String> foundColors = new ArrayList<String>();
		for(BlockPos spawnPos : this.spawnPointsList) {
			TileEntitySpawnPoint spawnTE = (TileEntitySpawnPoint) this.worldObj.getTileEntity(spawnPos);
			if(!foundColors.contains(spawnTE.spawnPointTeamColor)) {
				foundColors.add(spawnTE.spawnPointTeamColor);
			}
		}
		
		if(foundColors.size() < 2) {
			/**
			 * Map not properly set up.  Must be more than 2  
			 */
			Log.info("Less than two Spawn Point Colors found.");
			stopGame();
		} else if(foundColors.size() >= 2 && foundColors.size() < numTeamsFromGUI) {
			/**
			 * Map is not set up for as many teams as specified in GUI.  Reset to number found.
			 */
			numTeamsInGame = foundColors.size();
			// send message to client gameControllers?
		} else {
			/**
			 * Found number equals GUI number or is greater.  OK to play with fewer teams.
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
			 * - TODO: Get Custom sound.
			 */
			player.playSound("mob.cow.hurt", 1.0f, 1.0f);
		}
	}

	public static void assignPlayerToTeam(EntityPlayer inPlayer) {
		/**
		 * Check if teams are balanced.
		 */
		boolean teamsBalanced = true;
		String smallestTeam = DeathCube.gameTeams[0].getTeamColor();
		int smallestTeamSize = DeathCube.gameTeams[0].getTeamSize();
		for (int i = 1; i < DeathCube.gameTeams.length; i++) {
			/**
			 * Loop through all teams.
			 * - If one has a smaller size, mark it as the smallest.
			 */
			if (DeathCube.gameTeams[i].getTeamSize() < smallestTeamSize) {
				teamsBalanced = false;
				smallestTeam = DeathCube.gameTeams[i].getTeamColor();
			}
		}

		if (teamsBalanced) {
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
		 * Teleport Players to Lobby Location.
		 */

		if (DeathCube.lobbySpawnPos == null) {
			/**
			 * Get Lobby Spawn from data file?
			 */
			Log.info("GameController LobbyPosition: NULL");
		} else if (DeathCube.lobbySpawnPos == new BlockPos(0, 0, 0)) {
			Log.info("GameController LobbyPosition: " + DeathCube.lobbySpawnPos.toString());
		} else {
			preparePlayerToSpawn(inPlayer);
			inPlayer.setPositionAndUpdate(DeathCube.lobbySpawnPos.getX() + 0.5d, DeathCube.lobbySpawnPos.getY() + 1, DeathCube.lobbySpawnPos.getZ() + 0.5d);
			Log.info("GameController - Player sent to Lobby: " + DeathCube.lobbySpawnPos.toString());
		}

	}

	public static void sendPlayerToTeamSpawn(EntityPlayer inPlayer) {
		/**
		 * Teleport Players to Team Spawn Location(s).
		 */
		preparePlayerToSpawn(inPlayer);
		givePlayerGear(inPlayer);
		Log.info("Player attributes set.  Gear given.");

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
		 */
		inPlayer.setGameType(WorldSettings.GameType.SURVIVAL);
		inPlayer.motionX = 0;
		inPlayer.motionY = 0;
		inPlayer.motionZ = 0;
		inPlayer.fallDistance = 0;
		inPlayer.clearActivePotions();
		inPlayer.extinguish();
		inPlayer.setHealth(inPlayer.getMaxHealth());
		inPlayer.addPotionEffect(new PotionEffect(Potion.saturation.getId(), 10));
	}

	public static void givePlayerGear(EntityPlayer inPlayer) {
		/**
		 * Give Player Gear
		 * - Clear inventory. Happens automatically with assignment of new inventory?
		 * - Stone Sword
		 * - Leather Armor (in team color)
		 * - Bow
		 * - Arrows
		 * - Splash Potion of Poison
		 * 
		 **** --This should be customizable. Use TE with inventory. Copy inventory to player on respawn.
		 */
		Log.info("Give Gear - Start");
		World world = MinecraftServer.getServer().worldServers[0];
		String playerInventoryClass = Reference.GEAR_CLASS_WARRIOR;  // Get from player somehow.
		
		for (BlockPos pointPos : DeathCube.gearTEPos) {
			TileEntityStartingGearConfig lookupTE = (TileEntityStartingGearConfig) world.getTileEntity(pointPos);
			Log.info("Gear TE Location - " + pointPos.toString());
			if (lookupTE.getInventoryClass().equals(playerInventoryClass)) {
				for(int i = 0; i < 4; i++) {
					/**
					 * Color Armor if Leather
					 */
					int color = 0;
					String teamColor = DeathCube.playerToTeamColor.get(inPlayer.getName());
					
					if(teamColor.equals(Reference.TEAM_RED)) {
						color = ItemDye.dyeColors[EnumDyeColor.RED.getMetadata()];
					} else if(teamColor.equals(Reference.TEAM_BLUE)) {
						color = ItemDye.dyeColors[EnumDyeColor.BLUE.getMetadata()];
					} else if(teamColor.equals(Reference.TEAM_GREEN)) {
						color = ItemDye.dyeColors[EnumDyeColor.GREEN.getMetadata()];
					} else if(teamColor.equals(Reference.TEAM_YELLOW)) {
						color = ItemDye.dyeColors[EnumDyeColor.YELLOW.getMetadata()];
					}
					
					ItemStack armorPiece = lookupTE.inventory[Reference.GEAR_INVENTORY_SIZE - 1 - i].copy();
					
					if(armorPiece != null && armorPiece.getItem().equals(Items.leather_helmet)) {
						Items.leather_helmet.setColor(armorPiece, color);
					} else if(armorPiece != null && armorPiece.getItem().equals(Items.leather_chestplate)) {
						Items.leather_chestplate.setColor(armorPiece, color);
					} else if(armorPiece != null && armorPiece.getItem().equals(Items.leather_leggings)) {
						Items.leather_leggings.setColor(armorPiece, color);
					} else if(armorPiece != null && armorPiece.getItem().equals(Items.leather_boots)) {
						Items.leather_boots.setColor(armorPiece, color);
					}
					
					inPlayer.inventory.armorInventory[i] = armorPiece;
				}
				
				for(int i = 0; i < (Reference.GEAR_INVENTORY_SIZE - 4); i++) {
					if(lookupTE.inventory[i] != null)
						inPlayer.inventory.mainInventory[i] = lookupTE.inventory[i].copy();
				}
			}
		}
		Log.info("Give Gear - Success");
	}

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