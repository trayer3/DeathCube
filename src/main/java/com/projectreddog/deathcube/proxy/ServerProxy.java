package com.projectreddog.deathcube.proxy;

import net.minecraft.entity.player.EntityPlayer;


public class ServerProxy extends CommonProxy{

	@Override
	public EntityPlayer getClientPlayer() {
		return null;
	}
}
