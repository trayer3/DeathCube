package com.projectreddog.deathcube.proxy;

import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.common.MinecraftForge;

import com.projectreddog.deathcube.init.ModBlocks;
import com.projectreddog.deathcube.render.overlay.RenderOverlayHandler;

public class ClientProxy extends CommonProxy {

	@Override
	public void registerRenderers(){
		
		ModBlocks.initBlockRenderer();

		// register event for overlay
		MinecraftForge.EVENT_BUS.register(new RenderOverlayHandler());

	}

	@Override
	public EntityPlayer getClientPlayer() {
		return Minecraft.getMinecraft().thePlayer;
	}
}
