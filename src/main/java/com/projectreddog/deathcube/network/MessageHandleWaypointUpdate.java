package com.projectreddog.deathcube.network;

import io.netty.buffer.ByteBuf;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import net.minecraftforge.fml.relauncher.Side;

import com.projectreddog.deathcube.DeathCube;
import com.projectreddog.deathcube.entity.EntityWaypoint;
import com.projectreddog.deathcube.init.ModNetwork;
import com.projectreddog.deathcube.tileentity.TileEntityDeathCube;
import com.projectreddog.deathcube.utility.Log;

public class MessageHandleWaypointUpdate implements IMessage, IMessageHandler<MessageHandleWaypointUpdate, MessageHandleWaypointUpdate> {
	private int entityID, color;


	public MessageHandleWaypointUpdate() {
	}

	public MessageHandleWaypointUpdate(int entityID, int color ) {
		this.entityID= entityID;
		this.color = color;
	}
	

	@Override
	public void fromBytes(ByteBuf buf) {
		this.entityID = buf.readInt();
		this.color = buf.readInt();
	}

	@Override
	public void toBytes(ByteBuf buf) {
		buf.writeInt(entityID);
		buf.writeInt(color);
	}
	
	protected Entity getEntity(World world) {
		return world.getEntityByID(entityID);
	}

	@Override
	public MessageHandleWaypointUpdate onMessage(MessageHandleWaypointUpdate message, MessageContext ctx) {
		if (ctx.side == Side.SERVER) {
			Log.info("Server received message WAYPOINT: " + message.entityID + " " + message.color);
			handleServerSide(message, ctx.getServerHandler().playerEntity);
		} else {
			Log.info("Client received message WAYPOINT: "  + message.entityID + " " + message.color);
			handleClientSide(message, DeathCube.proxy.getClientPlayer());
		}
		return null;
	}
	
	public void handleClientSide(MessageHandleWaypointUpdate message, EntityPlayer player) {
		Entity lookupE = player.worldObj.getEntityByID(message.entityID);
		if (lookupE instanceof EntityWaypoint) {
			((EntityWaypoint) lookupE).team = message.color;
		}
	}

	public void handleServerSide(MessageHandleWaypointUpdate message, EntityPlayerMP player) {
		Entity lookupE = player.worldObj.getEntityByID(message.entityID);
		if (lookupE instanceof EntityWaypoint) {
			Log.info("attempt to send message response to client" + lookupE.getEntityId() + " "+ ((EntityWaypoint) lookupE).team);
			ModNetwork.simpleNetworkWrapper.sendTo( new MessageHandleWaypointUpdate(lookupE.getEntityId(),((EntityWaypoint) lookupE).team ),player);
		}else {
			Log.info("Entity was not of type EntityWaypoint");
		}
	}
}
