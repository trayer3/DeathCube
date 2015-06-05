package com.projectreddog.deathcube.network;

import io.netty.buffer.ByteBuf;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import net.minecraftforge.fml.relauncher.Side;

import com.projectreddog.deathcube.DeathCube;
import com.projectreddog.deathcube.init.ModNetwork;
import com.projectreddog.deathcube.utility.Log;

public class MessageHandleCrossPlayerMovement implements IMessage, IMessageHandler<MessageHandleCrossPlayerMovement, MessageHandleCrossPlayerMovement> {
	private int targetPlayerID;
	private float addVelocityX;
	private float addVelocityY;
	private float addVelocityZ;

	public MessageHandleCrossPlayerMovement() {
	}

	public MessageHandleCrossPlayerMovement(int targetPlayerID, float addVelocityX, float addVelocityY, float addVelocityZ) {
		// Log.info("Message Constructor - " + displayScoreboard);
		// Log.info("Message Constructor - " + numberOfTeams + " teams. " + gameTimeStartClient + " time flag.");
		this.targetPlayerID = targetPlayerID;
		this.addVelocityX = addVelocityX;
		this.addVelocityY = addVelocityY;
		this.addVelocityZ = addVelocityZ;
	}

	@Override
	public void fromBytes(ByteBuf buf) {
		// Log.info("Message fromBytes");
		targetPlayerID = buf.readInt();
		addVelocityX = buf.readFloat();
		addVelocityY = buf.readFloat();
		addVelocityZ = buf.readFloat();
	}

	@Override
	public void toBytes(ByteBuf buf) {
		// Log.info("Message toBytes");
		buf.writeInt(targetPlayerID);
		buf.writeFloat(addVelocityX);
		buf.writeFloat(addVelocityY);
		buf.writeFloat(addVelocityZ);
	}

	@Override
	public MessageHandleCrossPlayerMovement onMessage(MessageHandleCrossPlayerMovement message, MessageContext ctx) {
		if (ctx.side == Side.SERVER) {
			// Log.info("Server received message: Display Scoreboard - " + message.displayScoreboard);
			handleServerSide(message, ctx.getServerHandler().playerEntity);
		} else {
			// Log.info("Client received message: Display Scoreboard - " + message.displayScoreboard);
			handleClientSide(message, DeathCube.proxy.getClientPlayer());
		}
		return null;
	}

	public void handleClientSide(MessageHandleCrossPlayerMovement message, EntityPlayer player) {
		/**
		 * Set DeathCube.java values for Client Side.
		 */
		Log.info("Client receive");

		player.addVelocity(message.addVelocityX, message.addVelocityY, message.addVelocityZ);

	}

	public void handleServerSide(MessageHandleCrossPlayerMovement message, EntityPlayer player) {
		/**
		 * Do nothing for server side.
		 */
		// Log.info("Message Server Side do nothing.");
		Entity TargetEntity;
		TargetEntity = player.worldObj.getEntityByID(message.targetPlayerID);
		Log.info("Checking  not null");
		if (TargetEntity != null) {
			// / found entity apply velocity
			Log.info("target not null");
			Log.info(message.addVelocityX);
			Log.info(57);

			Log.info(message.addVelocityZ);

			if (TargetEntity instanceof EntityPlayerMP) {
				Log.info("EPMP");
				ModNetwork.sendTo(message, ((EntityPlayerMP) TargetEntity));
			}
			TargetEntity.addVelocity(message.addVelocityX, message.addVelocityY, message.addVelocityZ);
		}

	}
}
