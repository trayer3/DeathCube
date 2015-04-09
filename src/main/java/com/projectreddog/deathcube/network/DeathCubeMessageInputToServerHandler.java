package com.projectreddog.deathcube.network;

import com.projectreddog.deathcube.reference.Reference;
import com.projectreddog.deathcube.tileentity.TileEntityGameController;
import com.projectreddog.deathcube.tileentity.TileEntityGameController.GameStates;
import com.projectreddog.deathcube.utility.Log;

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

		if(message.buttonID == Reference.BUTTON_START_GAME) {
			// Start Game
			Log.info("Server sees Start Button Pressed");
			if (TileEntityGameController.gameState == GameStates.Lobby) {
				TileEntityGameController.gameState = GameStates.GameWarmup;
			} else if(TileEntityGameController.gameState == GameStates.Running) {
				TileEntityGameController.gameState = GameStates.PostGame;
			}
		}
	}
}
