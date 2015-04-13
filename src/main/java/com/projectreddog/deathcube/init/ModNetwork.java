package com.projectreddog.deathcube.init;

import java.util.List;

import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.network.NetworkRegistry;
import net.minecraftforge.fml.common.network.NetworkRegistry.TargetPoint;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.SimpleNetworkWrapper;
import net.minecraftforge.fml.relauncher.Side;

import com.projectreddog.deathcube.DeathCube;
import com.projectreddog.deathcube.client.gui.GuiHandler;
import com.projectreddog.deathcube.network.MessageHandleGuiButtonPress;
import com.projectreddog.deathcube.network.MessageHandleTextUpdate;
import com.projectreddog.deathcube.network.MessageRequestTextUpdate_Client;
import com.projectreddog.deathcube.reference.Reference;

public class ModNetwork {

	public static SimpleNetworkWrapper simpleNetworkWrapper;

	public static void init() {
		simpleNetworkWrapper = NetworkRegistry.INSTANCE.newSimpleChannel(Reference.MOD_ID);

		/**
		 * Register Messages
		 */
		simpleNetworkWrapper.registerMessage(MessageHandleGuiButtonPress.class, MessageHandleGuiButtonPress.class, 0, Side.SERVER);
		simpleNetworkWrapper.registerMessage(MessageHandleTextUpdate.class, MessageHandleTextUpdate.class, 1, Side.SERVER);
		simpleNetworkWrapper.registerMessage(MessageHandleTextUpdate.class, MessageHandleTextUpdate.class, 2, Side.CLIENT);
		simpleNetworkWrapper.registerMessage(MessageRequestTextUpdate_Client.class, MessageRequestTextUpdate_Client.class, 3, Side.SERVER);

		NetworkRegistry.INSTANCE.registerGuiHandler(DeathCube.instance, new GuiHandler());
	}
	
	public static void sendToServer(IMessage message){
		ModNetwork.simpleNetworkWrapper.sendToServer(message);
    }

    public static void sendTo(IMessage message, EntityPlayerMP player){
    	ModNetwork.simpleNetworkWrapper.sendTo(message, player);
    }

	public static void sendPacketToAllAround(IMessage packet, TargetPoint tp) {
		for (EntityPlayerMP player : (List<EntityPlayerMP>) FMLCommonHandler.instance().getMinecraftServerInstance().getConfigurationManager().playerEntityList) {
			if (player.dimension == tp.dimension) {
				double d4 = tp.x - player.posX;
				double d6 = tp.z - player.posZ;

				// base distance only on the x & Z axis so you can see machines way above / below you. (blast a machine up and you'll understand why
				if (d4 * d4 + d6 * d6 < tp.range * tp.range) {

					ModNetwork.simpleNetworkWrapper.sendTo(packet, player);

				}
			}
		}
	}
	
	public static void sendToAll(IMessage message){
		ModNetwork.simpleNetworkWrapper.sendToAll(message);
    }

    public static void sendToDimension(IMessage message, int dimensionId){
    	ModNetwork.simpleNetworkWrapper.sendToDimension(message, dimensionId);
    }
}
