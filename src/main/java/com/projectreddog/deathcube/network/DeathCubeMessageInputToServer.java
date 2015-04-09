package com.projectreddog.deathcube.network;

import io.netty.buffer.ByteBuf;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;

public class DeathCubeMessageInputToServer implements IMessage {

	public int buttonID;

	public DeathCubeMessageInputToServer() {

	}

	public DeathCubeMessageInputToServer(int buttonID) {
		super();
		this.buttonID = buttonID;
	}

	@Override
	public void fromBytes(ByteBuf buf) {
		this.buttonID = buf.readInt();
	}

	@Override
	public void toBytes(ByteBuf buf) {
		buf.writeInt(buttonID);
	}

}
