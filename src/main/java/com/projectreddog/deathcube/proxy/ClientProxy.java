package com.projectreddog.deathcube.proxy;

import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;

import com.projectreddog.deathcube.init.ModBlocks;

public class ClientProxy extends CommonProxy {

	@Override
	public void registerRenderers(){
		
		ModBlocks.initBlockRenderer();
		
	}

	@Override
	public EntityPlayer getClientPlayer() {
		return Minecraft.getMinecraft().thePlayer;
	}
}
