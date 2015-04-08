package com.projectreddog.deathcube.command;

import java.util.List;

import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommand;
import net.minecraft.command.ICommandSender;
import net.minecraft.command.WrongUsageException;
import net.minecraft.util.BlockPos;

import com.projectreddog.deathcube.tileentity.TileEntityGameController;
import com.projectreddog.deathcube.tileentity.TileEntityGameController.GameStates;

public class CommandGame extends CommandBase {//implements ICommand {

	@Override
	public int compareTo(Object arg0) {
		return 0;
	}

	@Override
	public String getName() {
		return "game";
	}

	@Override
	public String getCommandUsage(ICommandSender sender) {
		return "commands.game.usage";
	}

	@Override
	public List getAliases() {
		return null;
	}

	@Override
	public void execute(ICommandSender sender, String[] args) throws CommandException {
		if (args.length != 1) {
			throw new WrongUsageException("commands.game.usage", new Object[0]);
		} else {
			if (args[0].equals("start")) {
				if (TileEntityGameController.gameState == GameStates.Lobby) {
					TileEntityGameController.gameState = GameStates.GameWarmup;
				} else {
					throw new WrongUsageException("Game must be in Lobby to start.", new Object[0]);
				}
			} else if (args[0].equals("end")) {
				if (TileEntityGameController.gameState == GameStates.Running) {
					TileEntityGameController.gameState = GameStates.PostGame;
					TileEntityGameController.gameTimer = -1;
				} else {
					throw new WrongUsageException("Game must be Running to end.", new Object[0]);
				}
			} else {
				throw new WrongUsageException("commands.game.usage", new Object[0]);
			}
		}
	}

	@Override
	public boolean canCommandSenderUse(ICommandSender sender) {
		return true;
	}

	@Override
	public List addTabCompletionOptions(ICommandSender sender, String[] args, BlockPos pos) {
		return null;
	}

	@Override
	public boolean isUsernameIndex(String[] args, int index) {
		return false;
	}

}
