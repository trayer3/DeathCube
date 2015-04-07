package com.projectreddog.deathcube;

import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.network.NetworkRegistry;

import com.projectreddog.deathcube.client.gui.GuiHandler;
import com.projectreddog.deathcube.event.DeathCubeEventHandler;
import com.projectreddog.deathcube.init.ModBlocks;
import com.projectreddog.deathcube.init.ModTileEntities;
import com.projectreddog.deathcube.proxy.IProxy;
import com.projectreddog.deathcube.reference.Reference;
import com.projectreddog.deathcube.utility.Log;

@Mod(modid = Reference.MOD_ID, name = Reference.MOD_NAME)
public class DeathCube {
	@SidedProxy(clientSide = Reference.CLIENT_PROXY_CLASS, serverSide = Reference.SERVER_PROXY_CLASS)
	public static IProxy proxy;

	@Mod.Instance(Reference.MOD_ID)
	public static DeathCube instance;

	public static int currentAvailableGameID = 1;
	public static int numGameIDs = 0;

	/**
	 * PreInitialization: Network handling, Mod Configs, Register items and blocks
	 */
	@Mod.EventHandler
	public void preInit(FMLPreInitializationEvent event) {

		// Initialize Classes
		ModBlocks.init();
		ModTileEntities.init();

		// Initialize Network Handling
		NetworkRegistry.INSTANCE.registerGuiHandler(DeathCube.instance, new GuiHandler());

		// Register Event Handling
		MinecraftForge.EVENT_BUS.register(new DeathCubeEventHandler());
		FMLCommonHandler.instance().bus().register(new DeathCubeEventHandler());

		// Log Completion
		Log.info("Pre Initialization Complete!");
	}

	/**
	 * Initialization: Register GUI, Register tile entities, Register Recipes
	 */
	@Mod.EventHandler
	public void init(FMLInitializationEvent event) {

		// Register Stuff
		proxy.registerRenderers();

		// Log Completion
		Log.info("Initialization Complete!");
	}

	/**
	 * PostInitialization: Run after other mods are finished
	 */
	@Mod.EventHandler
	public void postInit(FMLPostInitializationEvent event) {

		// Log Completion
		Log.info("PostInitialization Complete!");
	}

	public static int getGameID() {
		/**
		 * Provide a Game ID and change next available.
		 */
		currentAvailableGameID++;
		numGameIDs++;
		return currentAvailableGameID - 1;
	}

	public static void freeGameID() {
		/**
		 * Decrement number of game IDs. Then, if none are in use, reset available ID to 1.
		 */
		if (numGameIDs > 0) {
			numGameIDs--;
			if (numGameIDs <= 0)
				currentAvailableGameID = 1;
		}
	}
}
