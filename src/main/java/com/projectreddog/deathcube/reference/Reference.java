package com.projectreddog.deathcube.reference;

import net.minecraft.util.ResourceLocation;

public class Reference {
	public static final String MOD_ID = "deathcube";
	public static final String MOD_NAME = "DeathCube";
	
	public static final String CLIENT_PROXY_CLASS = "com.projectreddog.deathcube.proxy.ClientProxy";
    public static final String SERVER_PROXY_CLASS = "com.projectreddog.deathcube.proxy.ServerProxy";
    
    public static final String MODBLOCK_EXAMPLE = "example";
    public static final String MODBLOCK_CAPTURE_POINT = "capturepoint";
    public static final String MODBLOCK_SPAWN_POINT = "spawnpoint";
    public static final String MODBLOCK_GAME_CONTROLLER = "gamecontroller";
    
    public static final ResourceLocation GUI_SPAWN_POINT_BACKGROUND = new ResourceLocation(Reference.MOD_ID, "textures/gui/spawnpointgui.png");
    public static final ResourceLocation GUI_GAME_CONTROLLER_BACKGROUND = new ResourceLocation(Reference.MOD_ID, "textures/gui/gamecontrollergui.png");
    
    public static final int BUTTON_START_GAME = 1;
    public static final int BUTTON_TEST = 2;
    
    public static final int GUI_GAME_CONTROLLER = 0;
    public static final int GUI_SPAWN_POINT = 1;
	public static final int GUI_CAPTURE_POINT = 2;
	
	public static final String TEAM_RED = "red";
	public static final String TEAM_BLUE = "blue";
	public static final String TEAM_GREEN = "green";
	public static final String TEAM_YELLOW = "yellow";
}
