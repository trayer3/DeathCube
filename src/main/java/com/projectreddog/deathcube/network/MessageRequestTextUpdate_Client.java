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

import org.omg.CORBA.portable.InputStream;
import org.omg.CORBA.portable.OutputStream;

import com.projectreddog.deathcube.DeathCube;
import com.projectreddog.deathcube.client.gui.GuiDeathCube;
import com.projectreddog.deathcube.init.ModNetwork;
import com.projectreddog.deathcube.reference.Reference;
import com.projectreddog.deathcube.tileentity.TileEntityDeathCube;
import com.projectreddog.deathcube.utility.Log;
import com.sun.corba.se.impl.protocol.giopmsgheaders.FragmentMessage;
import com.sun.corba.se.spi.ior.iiop.GIOPVersion;

public class MessageRequestTextUpdate_Client implements IMessage, IMessageHandler<MessageRequestTextUpdate_Client, MessageRequestTextUpdate_Client> {
	private int x, y, z;

	public MessageRequestTextUpdate_Client() {
	}

	public MessageRequestTextUpdate_Client(BlockPos pos) {
		this.x = pos.getX();
		this.y = pos.getY();
		this.z = pos.getZ();
	}

	@Override
	public void fromBytes(ByteBuf buf) {
		x = buf.readInt();
		y = buf.readInt();
		z = buf.readInt();
	}

	@Override
	public void toBytes(ByteBuf buf) {
		buf.writeInt(x);
		buf.writeInt(y);
		buf.writeInt(z);
	}
	
	protected TileEntity getTileEntity(World world) {
		return world.getTileEntity(new BlockPos(x, y, z));
	}

	@Override
	public MessageRequestTextUpdate_Client onMessage(MessageRequestTextUpdate_Client message, MessageContext ctx) {
		if (ctx.side == Side.SERVER) {
			Log.info("Server received text update request.");
			handleServerSide(message, ctx.getServerHandler().playerEntity);
		}
		return null;
	}
	
	public void handleClientSide(MessageRequestTextUpdate_Client message, EntityPlayer player) {
		
	}

	public void handleServerSide(MessageRequestTextUpdate_Client message, EntityPlayer player) {
		TileEntity lookupTE = player.worldObj.getTileEntity(new BlockPos(message.x, message.y, message.z));
		if (lookupTE instanceof TileEntityDeathCube) {
			((TileEntityDeathCube) lookupTE).onTextRequest();
		}
	}
}
