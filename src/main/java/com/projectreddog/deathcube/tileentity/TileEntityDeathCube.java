package com.projectreddog.deathcube.tileentity;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import net.minecraft.network.Packet;
import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.fml.common.network.internal.FMLProxyPacket;

import com.projectreddog.deathcube.network.DescriptionHandler;

public class TileEntityDeathCube extends TileEntity {
	@Override
    public Packet getDescriptionPacket(){
        ByteBuf buf = Unpooled.buffer();
        buf.writeInt(xCoord);
        buf.writeInt(yCoord);
        buf.writeInt(zCoord);
        writeToPacket(buf);
        return new FMLProxyPacket(buf, DescriptionHandler.CHANNEL);
    }

    public void writeToPacket(ByteBuf buf){

    }

    public void readFromPacket(ByteBuf buf){

    }

    public void onGuiButtonPress(int id){}

    public void onGuiTextfieldUpdate(int id, String text){}
}
