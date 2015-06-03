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

public class MessageHandleClientDeath implements IMessage, IMessageHandler<MessageHandleClientDeath, MessageHandleClientDeath> {
	private boolean playerIsDead;
	private int playerRespawnTime;

	public MessageHandleClientDeath() {
	}

	public MessageHandleClientDeath(boolean playerIsDead, int playerRespawnTime) {
		//Log.info("Message Constructor - " + displayScoreboard);
		//Log.info("Message Constructor - " + numberOfTeams + " teams. " + gameTimeStartClient + " time flag.");
		this.playerIsDead = playerIsDead;
		this.playerRespawnTime = playerRespawnTime;
	}

	@Override
	public void fromBytes(ByteBuf buf) {
		//Log.info("Message fromBytes");
		playerIsDead = buf.readBoolean();
		playerRespawnTime = buf.readInt();
	}

	@Override
	public void toBytes(ByteBuf buf) {
		//Log.info("Message toBytes");
		buf.writeBoolean(playerIsDead);
		buf.writeInt(playerRespawnTime);
	}

	@Override
	public MessageHandleClientDeath onMessage(MessageHandleClientDeath message, MessageContext ctx) {
		if (ctx.side == Side.SERVER) {
			//Log.info("Server received message: Display Scoreboard - " + message.displayScoreboard);
			handleServerSide(message, ctx.getServerHandler().playerEntity);
		} else {
			//Log.info("Client received message: Display Scoreboard - " + message.displayScoreboard);
			handleClientSide(message, DeathCube.proxy.getClientPlayer());
		}
		return null;
	}
	
	public void handleClientSide(MessageHandleClientDeath message, EntityPlayer player) {
		/**
		 * Set DeathCube.java values for Client Side.
		 */
		//Log.info("Message Client Side value-setting - " + message.displayScoreboard);
		DeathCube.playerIsDead_client = message.playerIsDead;
		DeathCube.playerRespawnTime_client = message.playerRespawnTime;
	}

	public void handleServerSide(MessageHandleClientDeath message, EntityPlayer player) {
		/**
		 * Do nothing for server side.
		 */
		//Log.info("Message Server Side do nothing.");
	}
}
