package com.projectreddog.deathcube;

import java.util.List;
import java.util.Map;

import net.minecraft.server.MinecraftServer;
import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.event.FMLServerStartingEvent;

import com.projectreddog.deathcube.event.DeathCubeEventHandler;
import com.projectreddog.deathcube.game.GameTeam;
import com.projectreddog.deathcube.init.ModBlocks;
import com.projectreddog.deathcube.init.ModCommands;
import com.projectreddog.deathcube.init.ModNetwork;
import com.projectreddog.deathcube.init.ModTileEntities;
import com.projectreddog.deathcube.network.MessageRequestTextUpdate_Client;
import com.projectreddog.deathcube.proxy.IProxy;
import com.projectreddog.deathcube.reference.Reference;
import com.projectreddog.deathcube.reference.Reference.FieldStates;
import com.projectreddog.deathcube.reference.Reference.GameStates;
import com.projectreddog.deathcube.tileentity.TileEntityGameController;
import com.projectreddog.deathcube.utility.Log;

@Mod(modid = Reference.MOD_ID, name = Reference.MOD_NAME)
public class DeathCube {
	@SidedProxy(clientSide = Reference.CLIENT_PROXY_CLASS, serverSide = Reference.SERVER_PROXY_CLASS)
	public static IProxy proxy;

	@Mod.Instance(Reference.MOD_ID)
	public static DeathCube instance;
	
	/**
	 * Globally used variables.
	 */
	public static GameStates gameState;
	public static FieldStates fieldState;
	public static boolean useForceField = true;
	public static int forceFieldStrength = 5;
	public static int gameTimer;
	public static GameTeam[] gameTeams;
	public static Map<String, Integer> teamColorToIndex;
	public static Map<String, String> playerToTeamColor;
	public static Map<String, Long> playerAwaitingRespawn;
	public static boolean isOrderedCapture = true;

	/**
	 * PreInitialization: Network handling, Mod Configs, Register items and blocks
	 */
	@Mod.EventHandler
	public void preInit(FMLPreInitializationEvent event) {
		
		// Initialize Classes
		ModBlocks.init();
		ModTileEntities.init();
		ModNetwork.init();

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

		// Register Event Handling
		MinecraftForge.EVENT_BUS.register(new DeathCubeEventHandler());
		FMLCommonHandler.instance().bus().register(new DeathCubeEventHandler());


		// Log Completion
		Log.info("Initialization Complete!");
	}

	
	@Mod.EventHandler
	public void serverStart (FMLServerStartingEvent event){
		// Register Commands
		ModCommands.init(event);
		Log.info("FMLServerStartingEvent Now.");
		List<TileEntity> teList = MinecraftServer.getServer().getEntityWorld().tickableTileEntities;
		if(teList != null) {
			Log.info("Entities found at serverStart(): " + teList.size());
			
			for(TileEntity te : teList) {
				Log.info("Entities type: " + te.getBlockType().toString());
				if(te instanceof TileEntityGameController) {
					/**
					 * TODO:  Is this a good place for these initial statements?
					 */
					DeathCube.gameState = GameStates.Lobby;
					DeathCube.fieldState = FieldStates.Inactive;
					DeathCube.gameTimer = -1;
					
					TileEntityGameController gameController = (TileEntityGameController) te;
					Log.info("Game controller found at: " + gameController.getPos().getX() + "x, " + gameController.getPos().getY() + "y, " + gameController.getPos().getZ() + "z");
					gameController.lobbySpawnPos = gameController.getPos();
					
					/**
					 * TODO: Does this work properly from here?
					 */
					ModNetwork.simpleNetworkWrapper.sendToServer(new MessageRequestTextUpdate_Client(gameController.getPos()));
				}
			}
		}
		
	}
	
	/**
	 * PostInitialization: Run after other mods are finished
	 */
	@Mod.EventHandler
	public void postInit(FMLPostInitializationEvent event) {

		// Log Completion
		Log.info("PostInitialization Complete!");
	}
}
