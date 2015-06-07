package com.projectreddog.deathcube.proxy;

import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.client.registry.RenderingRegistry;

import com.projectreddog.deathcube.DeathCube;
import com.projectreddog.deathcube.entity.EntityRPGRocket;
import com.projectreddog.deathcube.entity.EntityTurret;
import com.projectreddog.deathcube.entity.EntityWaypoint;
import com.projectreddog.deathcube.init.ModBlocks;
import com.projectreddog.deathcube.init.ModItems;
import com.projectreddog.deathcube.renderer.entity.RenderRPGRocket;
import com.projectreddog.deathcube.renderer.entity.RenderTurret;
import com.projectreddog.deathcube.renderer.entity.RenderWaypoint;
import com.projectreddog.deathcube.renderer.overlay.RenderOverlayHandler;

public class ClientProxy extends CommonProxy {

	@Override
	public void registerRenderers() {
		DeathCube.renderHelperYOffset = 0;
		DeathCube.renderHelperYDirection = -1;
		DeathCube.renderHelperRotation = 0d;

		ModBlocks.initBlockRenderer();
		ModItems.initItemRender();
		RenderingRegistry.registerEntityRenderingHandler(EntityWaypoint.class, new RenderWaypoint(Minecraft.getMinecraft().getRenderManager()));
		RenderingRegistry.registerEntityRenderingHandler(EntityTurret.class, new RenderTurret(Minecraft.getMinecraft().getRenderManager()));
		RenderingRegistry.registerEntityRenderingHandler(EntityRPGRocket.class, new RenderRPGRocket(Minecraft.getMinecraft().getRenderManager()));

		// register event for overlay
		MinecraftForge.EVENT_BUS.register(new RenderOverlayHandler());

	}

	@Override
	public EntityPlayer getClientPlayer() {
		return Minecraft.getMinecraft().thePlayer;
	}
}
