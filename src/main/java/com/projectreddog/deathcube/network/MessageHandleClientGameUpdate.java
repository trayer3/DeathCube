package com.projectreddog.deathcube.network;

import io.netty.buffer.ByteBuf;

import java.nio.ByteBuffer;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.server.MinecraftServer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.network.ByteBufUtils;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import org.omg.CORBA.portable.InputStream;
import org.omg.CORBA.portable.OutputStream;

import com.projectreddog.deathcube.DeathCube;
import com.projectreddog.deathcube.client.gui.GuiDeathCube;
import com.projectreddog.deathcube.reference.Reference.GameStates;
import com.projectreddog.deathcube.tileentity.TileEntityDeathCube;
import com.projectreddog.deathcube.utility.Log;
import com.sun.corba.se.impl.protocol.giopmsgheaders.FragmentMessage;
import com.sun.corba.se.spi.ior.iiop.GIOPVersion;

public class MessageHandleClientGameUpdate implements IMessage, IMessageHandler<MessageHandleClientGameUpdate, MessageHandleClientGameUpdate> {
	private boolean displayScoreboard;
	private String[] gameTeamsNames;
	private int[] gameTeamsActivePoints;
	private double[] gameTeamsPointTimes;
	private long gameTimeStartClient;

	public MessageHandleClientGameUpdate() {
	}

	public MessageHandleClientGameUpdate(boolean displayScoreboard, String[] gameTeamsNames, int[] gameTeamsActivePoints, double[] gameTeamsPointTimes, long gameTimeStartClient) {
		this.displayScoreboard = displayScoreboard;
		this.gameTeamsNames = gameTeamsNames.clone();
		this.gameTeamsActivePoints = gameTeamsActivePoints.clone();
		this.gameTeamsPointTimes = gameTeamsPointTimes.clone();
		this.gameTimeStartClient = gameTimeStartClient;
	}

	@Override
	public void fromBytes(ByteBuf buf) {
		displayScoreboard = buf.readBoolean();
		for(int i = 0; i < gameTeamsNames.length; i++) {
			gameTeamsNames[i] = ByteBufUtils.readUTF8String(buf);
		}
		for(int i = 0; i < gameTeamsActivePoints.length; i++) {
			gameTeamsActivePoints[i] = buf.readInt();
		}
		for(int i = 0; i < gameTeamsPointTimes.length; i++) {
			gameTeamsPointTimes[i] = buf.readDouble();
		}
		gameTimeStartClient = buf.readLong();
	}

	@Override
	public void toBytes(ByteBuf buf) {
		buf.writeBoolean(displayScoreboard);
		for(int i = 0; i < gameTeamsNames.length; i++) {
			ByteBufUtils.writeUTF8String(buf, gameTeamsNames[i]);
		}
		for(int i = 0; i < gameTeamsActivePoints.length; i++) {
			buf.writeInt(gameTeamsActivePoints[i]);
		}
		for(int i = 0; i < gameTeamsPointTimes.length; i++) {
			buf.writeDouble(gameTeamsPointTimes[i]);
		}
		buf.writeLong(gameTimeStartClient);
	}

	@Override
	public MessageHandleClientGameUpdate onMessage(MessageHandleClientGameUpdate message, MessageContext ctx) {
		if (ctx.side == Side.SERVER) {
			Log.info("Server received message: Display Scoreboard - " + message.displayScoreboard);
			handleServerSide(message, ctx.getServerHandler().playerEntity);
		} else {
			Log.info("Client received message: Display Scoreboard - " + message.displayScoreboard);
			handleClientSide(message, DeathCube.proxy.getClientPlayer());
		}
		return null;
	}
	
	public void handleClientSide(MessageHandleClientGameUpdate message, EntityPlayer player) {
		/**
		 * Set DeathCube.java values for Client Side.
		 */
		DeathCube.displayScoreboard_client = displayScoreboard;
		DeathCube.gameTeams_names = gameTeamsNames;
		DeathCube.gameTeams_activePoints = gameTeamsActivePoints;
		DeathCube.gameTeams_pointTimes = gameTeamsPointTimes;
		DeathCube.gameTimeStart_client = gameTimeStartClient;
	}

	public void handleServerSide(MessageHandleClientGameUpdate message, EntityPlayer player) {
		/**
		 * Do nothing for server side.
		 */
	}
}
