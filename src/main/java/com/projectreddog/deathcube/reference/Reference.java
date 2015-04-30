package com.projectreddog.deathcube.reference;

import java.util.Arrays;
import java.util.List;

import net.minecraft.server.MinecraftServer;
import net.minecraft.util.ResourceLocation;

public class Reference {
	/**
	 * Main Game Values
	 */
	public static final String MOD_ID = "deathcube";
	public static final String MOD_NAME = "DeathCube";

	public static final String CLIENT_PROXY_CLASS = "com.projectreddog.deathcube.proxy.ClientProxy";
	public static final String SERVER_PROXY_CLASS = "com.projectreddog.deathcube.proxy.ServerProxy";
	
	public static final String CONFIG_FILE_PATH = "saves\\" + MinecraftServer.getServer().getWorldName();

	public static enum GameStates {
		Lobby, GameWarmup, Running, PostGame, GameOver
	}
	
	public static enum FieldStates {
		Off, Inactive, Active
	}
	
	/**
	 * ID's and other Values
	 */
	public static final String MODBLOCK_EXAMPLE = "example";
	public static final String MODBLOCK_LOBBY = "lobby";
	public static final String MODBLOCK_CAPTURE_POINT = "capturepoint";
	public static final String MODBLOCK_SPAWN_POINT = "spawnpoint";
	public static final String MODBLOCK_GAME_CONTROLLER = "gamecontroller";
	public static final String MODBLOCK_STARTING_GEAR_CONFIG = "startinggearconfig";
	public static final String MODBLOCK_FORCE_FIELD = "forcefield";

	public static final int BUTTON_1 = 1;
	public static final int BUTTON_2 = 2;
	public static final int BUTTON_3 = 3;
	public static final int BUTTON_4 = 4;
	public static final int BUTTON_5 = 5;
	
	public static final int FORCE_FIELD_MIN_DIMENSION = 5;
	public static final int FORCE_FIELD_MAX_DIMENSION = 500;
	public static final int FORCE_FIELD_MAX_DIMENSION_Y = 256;
	public static final int FORCE_FIELD_MIN_STRENGTH = 0;
	public static final int FORCE_FIELD_MAX_STRENGTH = 999;

	public static final int GEAR_INVENTORY_SIZE = 40;
	public static final String GEAR_CLASS_WARRIOR = "Warrior";
	public static final String GEAR_CLASS_ARCHER = "Archer";
	public static final String GEAR_CLASS_HYBRID = "Hybrid";
	
	public static final int GUI_GAME_CONTROLLER = 0;
	public static final int GUI_SPAWN_POINT = 1;
	public static final int GUI_CAPTURE_POINT = 2;
	public static final int GUI_STARTING_GEAR_CONFIG = 3;
	
	public static final ResourceLocation GUI_SPAWN_POINT_BACKGROUND = new ResourceLocation(Reference.MOD_ID, "textures/gui/spawnpointgui.png");
	public static final ResourceLocation GUI_GAME_CONTROLLER_BACKGROUND = new ResourceLocation(Reference.MOD_ID, "textures/gui/gamecontrollergui.png");
	public static final ResourceLocation GUI_STARTING_GEAR_CONFIG_BACKGROUND = new ResourceLocation(Reference.MOD_ID, "textures/gui/startinggeargui.png");
	public static final ResourceLocation HUD_SCORE_TEAM_POINTS = new ResourceLocation(Reference.MOD_ID, "textures/hud/hudscorebackground.png");
	
	public static final int MESSAGE_SOURCE_GUI = 0;

	public static final int MESSAGE_FIELD1_ID = 1;
	public static final int MESSAGE_FIELD2_ID = 2;
	public static final int MESSAGE_FIELD3_ID = 3;
	public static final int MESSAGE_FIELD4_ID = 4;
	public static final int MESSAGE_FIELD5_ID = 5;
	public static final int MESSAGE_FIELD6_ID = 6;
	
	public static final int TEAM_NUM_POSSIBLE = 4;
	public static final String TEAM_RED = "Red";
	public static final String TEAM_BLUE = "Blue";
	public static final String TEAM_GREEN = "Green";
	public static final String TEAM_YELLOW = "Yellow";
	
	public static final int TIME_WARMUP = 1000;
	public static final int TIME_MAINGAME = 600000;
	public static final int TIME_POSTGAME = 5000;
	public static final int TIME_DEATH_PENALTY = 5000;
	
	public static final List<String> VERIFY_COLORS_LIST = Arrays.asList(TEAM_RED, TEAM_BLUE, TEAM_GREEN, TEAM_YELLOW);
	public static final int VERIFY_CAPTURE_RADIUS_MIN = 1;
	public static final int VERIFY_CAPTURE_RADIUS_MAX = 10;
	public static final int VERIFY_CAPTURE_TIME_MIN = 0;
	public static final int VERIFY_CAPTURE_TIME_MAX = 60;
	
	public static final int ENTITY_WAYPOINT_ID = 1;
	
	public static final String ENTITY_WAYPOINT_NAME = "waypoint";
	
	public static final String ENTITY_WAYPOINT_TEXTURE_LOCATION = "models/Waypoint.png";
}
