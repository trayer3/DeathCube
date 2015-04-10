package com.projectreddog.deathcube.network;

import io.netty.buffer.ByteBuf;
import net.minecraftforge.fml.common.network.ByteBufUtils;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;

public class DeathCubeMessageInputToServer implements IMessage {

	public int sourceType;
	public int sourceID;
	
	public String textField1;
	public String textField2;
	public String textField3;
	public String textField4;

	public DeathCubeMessageInputToServer() {

	}

	public DeathCubeMessageInputToServer(int sourceType, int sourceID, String textField1, String textField2, String textField3, String textField4) {
		super();
		this.sourceType = sourceType;
		this.sourceID = sourceID;
		
		this.textField1 = textField1;
		this.textField2 = textField2;
		this.textField3 = textField3;
		this.textField4 = textField4;
	}

	@Override
	public void fromBytes(ByteBuf buf) {
		this.sourceType = buf.readInt();
		this.sourceID = buf.readInt();
		
		this.textField1 = ByteBufUtils.readUTF8String(buf);
		this.textField2 = ByteBufUtils.readUTF8String(buf);
		this.textField3 = ByteBufUtils.readUTF8String(buf);
		this.textField4 = ByteBufUtils.readUTF8String(buf);
	}

	@Override
	public void toBytes(ByteBuf buf) {
		buf.writeInt(sourceType);
		buf.writeInt(sourceID);
		
		ByteBufUtils.writeUTF8String(buf, textField1);
		ByteBufUtils.writeUTF8String(buf, textField2);
		ByteBufUtils.writeUTF8String(buf, textField3);
		ByteBufUtils.writeUTF8String(buf, textField4);
	}

}
