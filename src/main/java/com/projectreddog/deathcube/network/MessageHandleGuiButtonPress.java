package com.projectreddog.deathcube.network;

import io.netty.buffer.ByteBuf;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import net.minecraftforge.fml.relauncher.Side;

import com.projectreddog.deathcube.DeathCube;
import com.projectreddog.deathcube.tileentity.TileEntityDeathCube;

public class MessageHandleGuiButtonPress implements IMessage, IMessageHandler<MessageHandleGuiButtonPress, MessageHandleGuiButtonPress> {
	private int buttonID, x, y, z;

	public MessageHandleGuiButtonPress() {
	}

	public MessageHandleGuiButtonPress(BlockPos pos, int id) {
		this.buttonID = id;
		this.x = pos.getX();
		this.y = pos.getY();
		this.z = pos.getZ();
	}

	@Override
	public void fromBytes(ByteBuf buf) {
		buttonID = buf.readInt();
		x = buf.readInt();
		y = buf.readInt();
		z = buf.readInt();
	}

	@Override
	public void toBytes(ByteBuf buf) {
		buf.writeInt(buttonID);
		buf.writeInt(x);
		buf.writeInt(y);
		buf.writeInt(z);
	}
	
	protected TileEntity getTileEntity(World world) {
		return world.getTileEntity(new BlockPos(x, y, z));
	}

	@Override
	public MessageHandleGuiButtonPress onMessage(MessageHandleGuiButtonPress message, MessageContext ctx) {
		if (ctx.side == Side.SERVER) {
			handleServerSide(message, ctx.getServerHandler().playerEntity);
		} else {
			handleClientSide(message, DeathCube.proxy.getClientPlayer());
		}
		return null;
	}

	public void handleClientSide(MessageHandleGuiButtonPress message, EntityPlayer player) {

	}

	public void handleServerSide(MessageHandleGuiButtonPress message, EntityPlayer player) {
		TileEntity lookupTE = player.worldObj.getTileEntity(new BlockPos(message.x, message.y, message.z));
		if (lookupTE instanceof TileEntityDeathCube) {
			((TileEntityDeathCube) lookupTE).onGuiButtonPress(message.buttonID);
		}
	}

}
