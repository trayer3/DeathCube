package com.projectreddog.deathcube.proxy;

import com.projectreddog.deathcube.init.ModBlocks;

public class ClientProxy extends CommonProxy {

	@Override
	public void registerRenderers(){
		
		ModBlocks.initBlockRenderer();
		
	}
}
