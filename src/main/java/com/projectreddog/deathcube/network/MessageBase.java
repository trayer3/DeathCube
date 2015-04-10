package com.projectreddog.deathcube.network;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import net.minecraftforge.fml.relauncher.Side;

import com.projectreddog.deathcube.DeathCube;

public abstract class MessageBase implements IMessage, IMessageHandler<IMessage, IMessage> {

	@Override
	public IMessage onMessage(IMessage message, MessageContext ctx) {
		if (ctx.side == Side.SERVER) {
			handleServerSide(message, ctx.getServerHandler().playerEntity);
		} else {
			handleClientSide(message, DeathCube.proxy.getClientPlayer());
		}
		return null;
	}

	/**
	 * Handle a packet on the client side. Note this occurs after decoding has completed.
	 * 
	 * @param message
	 * @param player
	 *            the player reference
	 */
	public abstract void handleClientSide(IMessage message, EntityPlayer player);

	/**
	 * Handle a packet on the server side. Note this occurs after decoding has completed.
	 * 
	 * @param message
	 * @param player
	 *            the player reference
	 */
	public abstract void handleServerSide(IMessage message, EntityPlayer player);
}
