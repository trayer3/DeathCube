package com.projectreddog.deathcube.proxy;

import net.minecraft.entity.player.EntityPlayer;

public abstract class CommonProxy implements IProxy {
	public void registerRenderers(){
		
	}
	
	public abstract EntityPlayer getClientPlayer();
}
