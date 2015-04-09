package com.projectreddog.deathcube.network;

import io.netty.buffer.ByteBuf;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;

public class DeathCubeMessageInputToServer implements IMessage {

	public int buttonID;
	public int numberOfTeams;

	public DeathCubeMessageInputToServer() {

	}

	public DeathCubeMessageInputToServer(int buttonID, int numberOfTeams) {
		super();
		this.buttonID = buttonID;
		this.numberOfTeams = numberOfTeams;
	}

	@Override
	public void fromBytes(ByteBuf buf) {
		this.buttonID = buf.readInt();
		this.numberOfTeams = buf.readInt();
	}

	@Override
	public void toBytes(ByteBuf buf) {
		buf.writeInt(buttonID);
		buf.writeInt(numberOfTeams);
	}

}
