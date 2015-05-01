package com.projectreddog.deathcube;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import net.minecraft.util.BlockPos;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.event.FMLServerStartingEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import com.projectreddog.deathcube.event.DeathCubeEventHandler;
import com.projectreddog.deathcube.game.GameTeam;
import com.projectreddog.deathcube.init.ModBlocks;
import com.projectreddog.deathcube.init.ModCommands;
import com.projectreddog.deathcube.init.ModConfig;
import com.projectreddog.deathcube.init.ModEntities;
import com.projectreddog.deathcube.init.ModItems;
import com.projectreddog.deathcube.init.ModNetwork;
import com.projectreddog.deathcube.init.ModTileEntities;
import com.projectreddog.deathcube.proxy.IProxy;
import com.projectreddog.deathcube.reference.Reference;
import com.projectreddog.deathcube.reference.Reference.FieldStates;
import com.projectreddog.deathcube.reference.Reference.GameStates;
import com.projectreddog.deathcube.utility.Log;

@Mod(modid = Reference.MOD_ID, name = Reference.MOD_NAME)
public class DeathCube {
	@SidedProxy(clientSide = Reference.CLIENT_PROXY_CLASS, serverSide = Reference.SERVER_PROXY_CLASS)
	public static IProxy proxy;

	@Mod.Instance(Reference.MOD_ID)
	public static DeathCube instance;

	/**
	 * Globally used variables.
	 * - Game Setup
	 * - Team Setup
	 * - Position Tracking
	 */
	public static GameStates gameState;
	public static FieldStates fieldState;
	public static boolean useForceField = true;
	public static int forceFieldStrength = 5;
	public static boolean isOrderedCapture = true;
	public static boolean firstServerTick = true;

	public static long gameTimeStart = 0;
	public static long gameTimeCheck = 0;
	public static GameTeam[] gameTeams;
	public static Map<String, Integer> teamColorToIndex;
	public static Map<String, String> playerToTeamColor;
	public static Map<String, Long> playerAwaitingRespawn;

	public static BlockPos gameControllerPos;
	public static BlockPos lobbySpawnPos;
	//public static List<BlockPos> spawnPoints = new ArrayList<BlockPos>();
	//public static List<BlockPos> capturePoints = new ArrayList<BlockPos>();
	public static List<BlockPos> gearTEPos = new ArrayList<BlockPos>();
	
	@SideOnly(Side.CLIENT)
	public static boolean displayScoreboard_client;
	@SideOnly(Side.CLIENT)
	public static String[] gameTeams_names_client;
	@SideOnly(Side.CLIENT)
	public static int[] gameTeams_activePoints_client;
	@SideOnly(Side.CLIENT)
	public static double[] gameTeams_pointTimes_client;
	@SideOnly(Side.CLIENT)
	public static long gameTimeStart_client;
	

	/**
	 * PreInitialization: Network handling, Mod Configs, Register items and blocks
	 */
	@Mod.EventHandler
	public void preInit(FMLPreInitializationEvent event) {
		
		// Initialize Classes
		ModBlocks.init();
		ModItems.init();
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
		//register entities before renders
		ModEntities.init(this);

		// Register Stuff
		proxy.registerRenderers();

		// Register Event Handling
		MinecraftForge.EVENT_BUS.register(new DeathCubeEventHandler());
		FMLCommonHandler.instance().bus().register(new DeathCubeEventHandler());

		// Log Completion
		Log.info("Initialization Complete!");
	}

	@Mod.EventHandler
	public void serverStart(FMLServerStartingEvent event) {
		// Register Commands
		ModCommands.init(event);
		ModConfig.init();
		Log.info("FMLServerStartingEvent Now.");

		/***************************************************************************************************
		 * 
		 * Read from config file here.
		 * - Lobby Spawn Position
		 * - GameController Position?
		 * - Position of every Capture and Spawn Point?
		 * 
		 ****************************************************************************************************/
		firstServerTick = true;
		
		lobbySpawnPos = ModConfig.readConfig().lobbyPos;
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
