package com.projectreddog.deathcube.reference;

import java.util.Arrays;
import java.util.List;

import net.minecraft.util.ResourceLocation;

public class Reference {
	public static final String MOD_ID = "deathcube";
	public static final String MOD_NAME = "DeathCube";

	public static final String CLIENT_PROXY_CLASS = "com.projectreddog.deathcube.proxy.ClientProxy";
	public static final String SERVER_PROXY_CLASS = "com.projectreddog.deathcube.proxy.ServerProxy";

	public static enum GameStates {
		Lobby, GameWarmup, Running, PostGame, GameOver
	}
	
	/**
	 * Field States:
	 * Off - Removes Force Field Blocks
	 * Inactive - Places Force Field Blocks, but they are OK to touch.
	 * Active - Arms Force Field Blocks as dangerous to touch!
	 */
	public static enum FieldStates {
		Off, Inactive, Active
	}
	
	public static final String MODBLOCK_EXAMPLE = "example";
	public static final String MODBLOCK_CAPTURE_POINT = "capturepoint";
	public static final String MODBLOCK_SPAWN_POINT = "spawnpoint";
	public static final String MODBLOCK_GAME_CONTROLLER = "gamecontroller";
	public static final String MODBLOCK_FORCE_FIELD = "forcefield";

	public static final ResourceLocation GUI_SPAWN_POINT_BACKGROUND = new ResourceLocation(Reference.MOD_ID, "textures/gui/spawnpointgui.png");
	public static final ResourceLocation GUI_GAME_CONTROLLER_BACKGROUND = new ResourceLocation(Reference.MOD_ID, "textures/gui/gamecontrollergui.png");

	public static final int MESSAGE_SOURCE_GUI = 0;

	public static final int MESSAGE_FIELD1_ID = 1;
	public static final int MESSAGE_FIELD2_ID = 2;
	public static final int MESSAGE_FIELD3_ID = 3;
	public static final int MESSAGE_FIELD4_ID = 4;
	public static final int MESSAGE_FIELD5_ID = 5;

	public static final int BUTTON_START_GAME = 1;
	public static final int BUTTON_TEST = 2;

	public static final int GUI_GAME_CONTROLLER = 0;
	public static final int GUI_SPAWN_POINT = 1;
	public static final int GUI_CAPTURE_POINT = 2;

	public static final int TEAM_NUM_POSSIBLE = 4;
	public static final String TEAM_RED = "Red";
	public static final String TEAM_BLUE = "Blue";
	public static final String TEAM_GREEN = "Green";
	public static final String TEAM_YELLOW = "Yellow";
	
	public static final List<String> VERIFY_COLORS_LIST = Arrays.asList(TEAM_RED, TEAM_BLUE, TEAM_GREEN, TEAM_YELLOW);
	public static final int VERIFY_CAPTURE_RADIUS = 10;
	public static final int VERIFY_CAPTURE_TIME = 60;
	
	public static final int TIME_WARMUP = 20;
	public static final int TIME_POSTGAME = 100;
	public static final int TIME_DEATH_PENALTY = 5000;
}
