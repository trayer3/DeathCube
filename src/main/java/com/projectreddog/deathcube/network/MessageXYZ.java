package com.projectreddog.deathcube.network;

import io.netty.buffer.ByteBuf;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockPos;
import net.minecraft.world.World;

import com.sun.corba.se.impl.protocol.giopmsgheaders.MessageBase;

public abstract class MessageXYZ extends MessageBase {

	protected int x, y, z;

	public MessageXYZ() {
	}

	public MessageXYZ(int x, int y, int z) {
		this.x = x;
		this.y = y;
		this.z = z;
	}

	public MessageXYZ(TileEntity te) {
		this(te.getPos().getX(), te.getPos().getY(), te.getPos().getZ());
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

	public void handleClientSide(MessageHandleTextUpdate message, EntityPlayer player) {
		// TODO Auto-generated method stub
		
	}

	public void handleServerSide(MessageHandleTextUpdate message, EntityPlayer player) {
		// TODO Auto-generated method stub
		
	}
}
