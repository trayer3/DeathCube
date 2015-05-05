package com.projectreddog.deathcube.tileentity;

import net.minecraft.block.Block;
import net.minecraft.command.IEntitySelector;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.server.gui.IUpdatePlayerListBox;
import net.minecraft.util.BlockPos;

import com.projectreddog.deathcube.DeathCube;
import com.projectreddog.deathcube.init.ModBlocks;
import com.projectreddog.deathcube.init.ModNetwork;
import com.projectreddog.deathcube.network.MessageHandleTextUpdate;
import com.projectreddog.deathcube.reference.Reference;
import com.projectreddog.deathcube.reference.Reference.FieldStates;
import com.projectreddog.deathcube.reference.Reference.GameStates;
import com.projectreddog.deathcube.utility.Log;

public class TileEntityForceFieldGen extends TileEntityDeathCube implements IUpdatePlayerListBox {

	/**
	 * GUI Variables
	 */
	private int forceFieldx = Reference.FORCE_FIELD_MIN_DIMENSION;
	private int forceFieldz = Reference.FORCE_FIELD_MIN_DIMENSION;
	private int forceFieldyUp = Reference.FORCE_FIELD_MIN_DIMENSION;
	private int forceFieldyDown = Reference.FORCE_FIELD_MIN_DIMENSION;
	private int forceFieldStrength = 100;
	private Block forceFieldBlock = ModBlocks.forcefield;
	
	/**
	 * Tile Entity Variables
	 */
	

	public TileEntityForceFieldGen() {
		//Log.info("Capture Point Constructor");
	}

	public void onTextRequest() {
		if (this.worldObj != null) {
			if (!this.worldObj.isRemote) {
				Log.info("Server sending requested text - Force Field");
				ModNetwork.sendToAll(new MessageHandleTextUpdate(this.pos, Reference.MESSAGE_FIELD2_ID, String.valueOf(forceFieldx)));
				ModNetwork.sendToAll(new MessageHandleTextUpdate(this.pos, Reference.MESSAGE_FIELD3_ID, String.valueOf(forceFieldz)));
				ModNetwork.sendToAll(new MessageHandleTextUpdate(this.pos, Reference.MESSAGE_FIELD4_ID, String.valueOf(forceFieldyUp)));
				ModNetwork.sendToAll(new MessageHandleTextUpdate(this.pos, Reference.MESSAGE_FIELD5_ID, String.valueOf(forceFieldyDown)));
				ModNetwork.sendToAll(new MessageHandleTextUpdate(this.pos, Reference.MESSAGE_FIELD6_ID, String.valueOf(forceFieldStrength)));
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
		 * Save Capture Point Data
		 * - private GuiTextField text_ForceFieldx;
		 * - private GuiTextField text_ForceFieldz;
		 * - private GuiTextField text_ForceFieldyUp;
		 * - private GuiTextField text_ForceFieldyDown;
		 * - private GuiTextField text_ForceFieldStrength;
		 */
		Log.info("Capture Point sees Text Update: " + text);
		toggleForceField(false);
		if (fieldID == Reference.MESSAGE_FIELD2_ID) {
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
		}
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
		forceFieldx = tag.getInteger("x");
		forceFieldz = tag.getInteger("z");
		forceFieldyUp = tag.getInteger("y_up");
		forceFieldyDown = tag.getInteger("y_down");
		forceFieldStrength = tag.getInteger("strength");
		Log.info("Force Field Gen - NBT Read");
	}

	@Override
	public void writeToNBT(NBTTagCompound tag) {
		super.writeToNBT(tag);
		tag.setInteger("x", forceFieldx);
		tag.setInteger("z", forceFieldz);
		tag.setInteger("y_up", forceFieldyUp);
		tag.setInteger("y_down", forceFieldyDown);
		tag.setInteger("strength", forceFieldStrength);
		//Log.info("Game Controller - NBT Write :: Number of Teams: " + numTeamsFromGUI);
	}

	@Override
	public void update() {
		
	}
	
	/**
	 * Needs to be !isRemote?
	 */
	@Override
	public void onGuiButtonPress(int buttonID) {
		if (!this.worldObj.isRemote) {
			if (buttonID == Reference.BUTTON_3) {
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
			int oddx, oddz;
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
				oddx = 0;
				Log.info("Force Field X is Even");
			} else {
				// Odd X
				halfx = (forceFieldx - 1) / 2;
				oddx = 1;
				Log.info("Force Field X is Odd");
			}

			if (forceFieldz % 2 == 0) {
				// Even Z
				oddz = 0;
				halfz = forceFieldz / 2;
			} else {
				// Odd Z
				oddz = 1;
				halfz = (forceFieldz - 1) / 2;
			}

			/**
			 * North Wall
			 */
			startingPos = new BlockPos(gameControllerPos.north(forceFieldz + 1).west(halfx).down(forceFieldyDown));
			endingPos = new BlockPos(gameControllerPos.north(forceFieldz + 1).east(halfx + oddx).up(forceFieldyUp));

			Log.info("Half X: " + halfx);
			Log.info("Y Up: " + forceFieldyUp + " - Y Down: " + forceFieldyDown);
			Log.info("Starting Pos: " + startingPos.toString());
			Log.info("Ending Pos: " + endingPos.toString());

			westToEastWall(startingPos, endingPos, generateCube);
			
			/**
			 * South Wall
			 */
			startingPos = new BlockPos(gameControllerPos.north().west(halfx).down(forceFieldyDown));
			endingPos = new BlockPos(gameControllerPos.north().east(halfx + oddx).up(forceFieldyUp));

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
			startingPos = new BlockPos(gameControllerPos.north(forceFieldz + 1).east(halfx + oddx).down(forceFieldyDown));
			endingPos = new BlockPos(gameControllerPos.north().east(halfx + oddx).up(forceFieldyUp));

			Log.info("Half Z: " + halfz);
			Log.info("Y Up: " + forceFieldyUp + " - Y Down: " + forceFieldyDown);
			Log.info("Starting Pos: " + startingPos.toString());
			Log.info("Ending Pos: " + endingPos.toString());

			northToSouthWall(startingPos, endingPos, generateCube);

			/**
			 * Top Cube Face
			 */
			startingPos = new BlockPos(gameControllerPos.north(forceFieldz + 1).west(halfx).up(forceFieldyUp));
			endingPos = new BlockPos(gameControllerPos.north().east(halfx + oddx).up(forceFieldyUp));

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
			endingPos = new BlockPos(gameControllerPos.north().east(halfx + oddx).down(forceFieldyDown));

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
}
