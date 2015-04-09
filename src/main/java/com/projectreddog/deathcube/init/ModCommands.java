package com.projectreddog.deathcube.init;

import net.minecraftforge.fml.common.event.FMLServerStartingEvent;

import com.projectreddog.deathcube.command.CommandGame;

public class ModCommands {

	public static final CommandGame commandgame = new CommandGame();

	public static void init(FMLServerStartingEvent event) {
		event.registerServerCommand(commandgame);
	}
	
}
