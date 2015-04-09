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
import com.projectreddog.deathcube.network.DeathCubeMessageInputToServer;
import com.projectreddog.deathcube.network.DeathCubeMessageInputToServerHandler;
import com.projectreddog.deathcube.reference.Reference;

public class ModNetwork {

	public static SimpleNetworkWrapper simpleNetworkWrapper;

	public static void init() {
		simpleNetworkWrapper = NetworkRegistry.INSTANCE.newSimpleChannel(Reference.MOD_ID);

		// Message to Server
		simpleNetworkWrapper.registerMessage(DeathCubeMessageInputToServerHandler.class, DeathCubeMessageInputToServer.class, 0, Side.SERVER);

		// Message to Client
		// If needed

		NetworkRegistry.INSTANCE.registerGuiHandler(DeathCube.instance, new GuiHandler());
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
}
