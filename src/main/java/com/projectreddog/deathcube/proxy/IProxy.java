package com.projectreddog.deathcube.proxy;

import net.minecraft.entity.player.EntityPlayer;


public interface IProxy {
	
	void registerRenderers();

	EntityPlayer getClientPlayer();
	
}
