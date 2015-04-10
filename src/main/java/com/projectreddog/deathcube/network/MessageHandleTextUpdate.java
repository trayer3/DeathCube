package com.projectreddog.deathcube.network;

import io.netty.buffer.ByteBuf;

import java.nio.ByteBuffer;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.fml.common.network.ByteBufUtils;

import org.omg.CORBA.portable.InputStream;
import org.omg.CORBA.portable.OutputStream;

import com.projectreddog.deathcube.client.gui.GuiDeathCube;
import com.projectreddog.deathcube.tileentity.TileEntityDeathCube;
import com.sun.corba.se.impl.protocol.giopmsgheaders.FragmentMessage;
import com.sun.corba.se.spi.ior.iiop.GIOPVersion;

public class MessageHandleTextUpdate extends MessageXYZ {
	private int id;
	private String text;

	public MessageHandleTextUpdate() {
	}

	public MessageHandleTextUpdate(TileEntityDeathCube te, int id, String text) {
		super(te);
		this.id = id;
		this.text = text;
	}

	@Override
	public void fromBytes(ByteBuf buf) {
		super.fromBytes(buf);
		id = buf.readInt();
		text = ByteBufUtils.readUTF8String(buf);
	}

	@Override
	public void toBytes(ByteBuf buf) {
		super.toBytes(buf);
		buf.writeInt(id);
		ByteBufUtils.writeUTF8String(buf, text);
	}

	@Override
	public void handleClientSide(MessageHandleTextUpdate message, EntityPlayer player) {
		handleServerSide(message, player);
		GuiScreen gui = Minecraft.getMinecraft().currentScreen;// <-- Warning, this will crash when tested on server, will be discussed next episode.
		if (gui instanceof GuiDeathCube) {
			((GuiDeathCube) gui).onTextfieldUpdate(message.id);
		}
	}

	@Override
	public void handleServerSide(MessageHandleTextUpdate message, EntityPlayer player) {
		TileEntity te = message.getTileEntity(player.worldObj);
		if (te instanceof TileEntityDeathCube) {
			((TileEntityDeathCube) te).onGuiTextfieldUpdate(message.id, message.text);
		}
	}

	@Override
	public FragmentMessage createFragmentMessage() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public GIOPVersion getGIOPVersion() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public int getSize() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int getType() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public boolean isLittleEndian() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean moreFragmentsToFollow() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void read(InputStream arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setSize(ByteBuffer arg0, int arg1) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void write(OutputStream arg0) {
		// TODO Auto-generated method stub
		
	}

}
