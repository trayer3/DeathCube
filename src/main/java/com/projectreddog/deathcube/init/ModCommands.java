package com.projectreddog.deathcube.init;

import net.minecraftforge.client.ClientCommandHandler;

import com.projectreddog.deathcube.command.CommandGame;

public class ModCommands {

	public static final CommandGame commandgame = new CommandGame();
	
	public static void init() {
		ClientCommandHandler.instance.registerCommand(commandgame);
	}
	
}
