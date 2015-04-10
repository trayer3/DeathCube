package com.projectreddog.deathcube.network;

import com.projectreddog.deathcube.reference.Reference;
import com.projectreddog.deathcube.tileentity.TileEntityGameController;
import com.projectreddog.deathcube.tileentity.TileEntityGameController.GameStates;
import com.projectreddog.deathcube.utility.Log;

import net.minecraft.client.gui.GuiTextField;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

public class DeathCubeMessageInputToServerHandler implements IMessageHandler<DeathCubeMessageInputToServer, IMessage> {

	@Override
	public IMessage onMessage(final DeathCubeMessageInputToServer message, final MessageContext ctx) {

		ctx.getServerHandler().playerEntity.getServerForPlayer().addScheduledTask(new Runnable() {
			public void run() {
				processMessage(message, ctx);
			}
		});
		return null;
	}

	public void processMessage(DeathCubeMessageInputToServer message, MessageContext ctx) {

		if (message.sourceType == Reference.MESSAGE_SOURCE_GUI) {
			if (message.sourceID == Reference.GUI_GAME_CONTROLLER) {
				/**
				 * Save Game Controller Data
				 *  - textField1 = Button ID
				 *  - textField1 = Number of Teams
				 *  
				 *  Will need Force Field Dimensions
				 */
				if (Integer.parseInt(message.textField1) == Reference.BUTTON_START_GAME) {
					/**
					 * Start Button Pressed - Start Game!
					 */
					Log.info("Server sees Start Button Pressed");
					if (TileEntityGameController.gameState != null) {
						if (TileEntityGameController.gameState == GameStates.Lobby) {
							TileEntityGameController.gameState = GameStates.GameWarmup;
						} else if (TileEntityGameController.gameState == GameStates.Running) {
							TileEntityGameController.gameState = GameStates.PostGame;
						}
					}
				}
			} else if (message.sourceID == Reference.GUI_SPAWN_POINT) {
				/**
				 * Save Spawn Point Data
				 */
				
			} else if (message.sourceID == Reference.GUI_CAPTURE_POINT) {
				/**
				 * Save Capture Point Data
				 *  - textField1 = Point Name
				 *  - textField2 = Point Team
				 *  - textField3 = Point Radius
				 *  - textField4 = Point Capture Order Number
				 */
				
			}
		}
	}
}
