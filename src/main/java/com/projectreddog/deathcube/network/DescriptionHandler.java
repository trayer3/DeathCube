package com.projectreddog.deathcube.network;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandler.Sharable;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockPos;
import net.minecraftforge.fml.common.network.NetworkRegistry;
import net.minecraftforge.fml.common.network.internal.FMLProxyPacket;

import com.projectreddog.deathcube.DeathCube;
import com.projectreddog.deathcube.reference.Reference;
import com.projectreddog.deathcube.tileentity.TileEntityDeathCube;

@Sharable
public class DescriptionHandler extends SimpleChannelInboundHandler<FMLProxyPacket>{
    public static final String CHANNEL = Reference.MOD_ID + "Description";

    static {
        NetworkRegistry.INSTANCE.newChannel(CHANNEL, new DescriptionHandler());
    }

    public static void init(){
        //not actually doing anything here, apart from loading the class. If the channel registry goes in here, Netty will throw a duplicate
        //channel error.
    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, FMLProxyPacket msg) throws Exception{
        ByteBuf buf = msg.payload();
        int x = buf.readInt();
        int y = buf.readInt();
        int z = buf.readInt();
        BlockPos bp = new BlockPos(x, y, z);
        TileEntity te = DeathCube.proxy.getClientPlayer().worldObj.getTileEntity(bp);
        if(te instanceof TileEntityDeathCube) {
            ((TileEntityDeathCube)te).readFromPacket(buf);
        }
    }

}
