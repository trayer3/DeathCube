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
	private int numberOfTeams = 0;
	private String[] gameTeamsNames;
	private int[] gameTeamsActivePoints;
	private double[] gameTeamsPointTimes;
	private long gameTimeStartClient;
	private int gameTimeLimitClient;

	public MessageHandleClientGameUpdate() {
	}

	public MessageHandleClientGameUpdate(boolean displayScoreboard, int numberOfTeams, String[] gameTeamsNames, int[] gameTeamsActivePoints, double[] gameTeamsPointTimes, long gameTimeStartClient, int gameTimeLimitClient) {
		//Log.info("Message Constructor - " + displayScoreboard);
		//Log.info("Message Constructor - " + numberOfTeams + " teams. " + gameTimeStartClient + " time flag.");
		this.displayScoreboard = displayScoreboard;
		this.numberOfTeams = numberOfTeams;
		this.gameTeamsNames = gameTeamsNames.clone();
		this.gameTeamsActivePoints = gameTeamsActivePoints.clone();
		this.gameTeamsPointTimes = gameTeamsPointTimes.clone();
		this.gameTimeStartClient = gameTimeStartClient;
		this.gameTimeLimitClient = gameTimeLimitClient;
		
		/**
		for(int i = 0; i < numberOfTeams; i++) {
			Log.info("Message Constructor - Team: " + gameTeamsNames[i] + " Local: " + this.gameTeamsNames[i] + " Point: " + gameTeamsActivePoints[i] + " Local: " + this.gameTeamsActivePoints[i] + " Time: " + gameTeamsPointTimes[i] + " Local: " + this.gameTeamsPointTimes[i]);
		} */
	}

	@Override
	public void fromBytes(ByteBuf buf) {
		//Log.info("Message fromBytes");
		displayScoreboard = buf.readBoolean();
		numberOfTeams = buf.readInt();
		
		gameTeamsNames = new String[numberOfTeams];
		gameTeamsActivePoints = new int[numberOfTeams];
		gameTeamsPointTimes = new double[numberOfTeams];
		
		for(int i = 0; i < numberOfTeams; i++) {
			gameTeamsNames[i] = ByteBufUtils.readUTF8String(buf);
			gameTeamsActivePoints[i] = buf.readInt();
			gameTeamsPointTimes[i] = buf.readDouble();
		}
		gameTimeStartClient = buf.readLong();
		gameTimeLimitClient = buf.readInt();
		
		/**
		Log.info("Message fromBytes - " + displayScoreboard);
		Log.info("Message fromBytes - " + numberOfTeams + " teams. " + gameTimeStartClient + " time flag.");
		for(int i = 0; i < numberOfTeams; i++) {
			Log.info("Message fromBytes - Team: " + gameTeamsNames[i] + " Point: " + gameTeamsActivePoints[i] + " Time: " + gameTeamsPointTimes[i]);
		} */
	}

	@Override
	public void toBytes(ByteBuf buf) {
		//Log.info("Message toBytes");
		buf.writeBoolean(displayScoreboard);
		buf.writeInt(numberOfTeams);
		for(int i = 0; i < numberOfTeams; i++) {
			ByteBufUtils.writeUTF8String(buf, gameTeamsNames[i]);
			buf.writeInt(gameTeamsActivePoints[i]);
			buf.writeDouble(gameTeamsPointTimes[i]);
		}
		buf.writeLong(gameTimeStartClient);
		buf.writeInt(gameTimeLimitClient);
	}

	@Override
	public MessageHandleClientGameUpdate onMessage(MessageHandleClientGameUpdate message, MessageContext ctx) {
		if (ctx.side == Side.SERVER) {
			//Log.info("Server received message: Display Scoreboard - " + message.displayScoreboard);
			handleServerSide(message, ctx.getServerHandler().playerEntity);
		} else {
			//Log.info("Client received message: Display Scoreboard - " + message.displayScoreboard);
			handleClientSide(message, DeathCube.proxy.getClientPlayer());
		}
		return null;
	}
	
	public void handleClientSide(MessageHandleClientGameUpdate message, EntityPlayer player) {
		/**
		 * Set DeathCube.java values for Client Side.
		 */
		//Log.info("Message Client Side value-setting - " + message.displayScoreboard);
		DeathCube.displayScoreboard_client = message.displayScoreboard;
		DeathCube.gameTeams_names_client = message.gameTeamsNames;
		DeathCube.gameTeams_activePoints_client = message.gameTeamsActivePoints;
		DeathCube.gameTeams_pointTimes_client = message.gameTeamsPointTimes;
		DeathCube.gameTimeStart_client = message.gameTimeStartClient;
		DeathCube.gameTimeLimit_client = message.gameTimeLimitClient;
	}

	public void handleServerSide(MessageHandleClientGameUpdate message, EntityPlayer player) {
		/**
		 * Do nothing for server side.
		 */
		//Log.info("Message Server Side do nothing.");
	}
}
