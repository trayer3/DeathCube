package com.projectreddog.deathcube;

import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;

import com.projectreddog.deathcube.init.ModBlocks;
import com.projectreddog.deathcube.proxy.IProxy;
import com.projectreddog.deathcube.reference.Reference;

@Mod(modid = Reference.MOD_ID, name = Reference.MOD_NAME)
public class DeathCube {
	@SidedProxy(clientSide = Reference.CLIENT_PROXY_CLASS, serverSide = Reference.SERVER_PROXY_CLASS)
    public static IProxy proxy;
	
	@Mod.Instance(Reference.MOD_ID)
    public static DeathCube instance;

	@Mod.EventHandler
	public void preInit(FMLPreInitializationEvent event) {
		ModBlocks.init();
	}
	
	@Mod.EventHandler
	public void init(FMLPreInitializationEvent event) {
		
		proxy.registerRenderers();
	}
	
	@Mod.EventHandler
	public void postInit(FMLPreInitializationEvent event) {
		
	}
}
