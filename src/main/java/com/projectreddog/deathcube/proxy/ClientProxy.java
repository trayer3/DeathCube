package com.projectreddog.deathcube.proxy;

import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.client.registry.RenderingRegistry;

import com.projectreddog.deathcube.entity.EntityWaypoint;
import com.projectreddog.deathcube.init.ModBlocks;
import com.projectreddog.deathcube.renderer.entity.RenderWaypoint;
import com.projectreddog.deathcube.renderer.overlay.RenderOverlayHandler;

public class ClientProxy extends CommonProxy {

	@Override
	public void registerRenderers(){
		
		ModBlocks.initBlockRenderer();
		RenderingRegistry.registerEntityRenderingHandler(EntityWaypoint.class, new RenderWaypoint(Minecraft.getMinecraft().getRenderManager()));

		// register event for overlay
		MinecraftForge.EVENT_BUS.register(new RenderOverlayHandler());

	}

	@Override
	public EntityPlayer getClientPlayer() {
		return Minecraft.getMinecraft().thePlayer;
	}
}
