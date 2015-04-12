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
import com.projectreddog.deathcube.tileentity.TileEntityDeathCube;
import com.projectreddog.deathcube.utility.Log;
import com.sun.corba.se.impl.protocol.giopmsgheaders.FragmentMessage;
import com.sun.corba.se.spi.ior.iiop.GIOPVersion;

public class MessageHandleTextUpdate implements IMessage, IMessageHandler<MessageHandleTextUpdate, MessageHandleTextUpdate> {
	private int fieldID, x, y, z;
	private String text;

	public MessageHandleTextUpdate() {
	}

	public MessageHandleTextUpdate(BlockPos pos, int id, String text) {
		this.fieldID = id;
		this.text = text;
		this.x = pos.getX();
		this.y = pos.getY();
		this.z = pos.getZ();
	}

	@Override
	public void fromBytes(ByteBuf buf) {
		fieldID = buf.readInt();
		x = buf.readInt();
		y = buf.readInt();
		z = buf.readInt();
		text = ByteBufUtils.readUTF8String(buf);
	}

	@Override
	public void toBytes(ByteBuf buf) {
		buf.writeInt(fieldID);
		buf.writeInt(x);
		buf.writeInt(y);
		buf.writeInt(z);
		ByteBufUtils.writeUTF8String(buf, text);
	}
	
	protected TileEntity getTileEntity(World world) {
		return world.getTileEntity(new BlockPos(x, y, z));
	}

	@Override
	public MessageHandleTextUpdate onMessage(MessageHandleTextUpdate message, MessageContext ctx) {
		if (ctx.side == Side.SERVER) {
			Log.info("Server received message: " + message.text);
			handleServerSide(message, ctx.getServerHandler().playerEntity);
		} else {
			Log.info("Client received message: " + message.text);
			handleClientSide(message, DeathCube.proxy.getClientPlayer());
		}
		return null;
	}
	
	public void handleClientSide(MessageHandleTextUpdate message, EntityPlayer player) {
		TileEntity lookupTE = player.worldObj.getTileEntity(new BlockPos(message.x, message.y, message.z));
		if (lookupTE instanceof TileEntityDeathCube) {
			((TileEntityDeathCube) lookupTE).onGuiTextfieldUpdate(message.fieldID, message.text);
		}
	}

	public void handleServerSide(MessageHandleTextUpdate message, EntityPlayer player) {
		TileEntity lookupTE = player.worldObj.getTileEntity(new BlockPos(message.x, message.y, message.z));
		if (lookupTE instanceof TileEntityDeathCube) {
			((TileEntityDeathCube) lookupTE).onGuiTextfieldUpdate(message.fieldID, message.text);
		}
	}
}
