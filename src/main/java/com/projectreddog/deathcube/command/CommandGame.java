package com.projectreddog.deathcube.command;

import java.util.List;

import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.command.WrongUsageException;
import net.minecraft.util.BlockPos;

import com.projectreddog.deathcube.DeathCube;
import com.projectreddog.deathcube.game.GameTeam;
import com.projectreddog.deathcube.reference.Reference.FieldStates;
import com.projectreddog.deathcube.reference.Reference.GameStates;
import com.projectreddog.deathcube.utility.Log;

public class CommandGame extends CommandBase {

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
	public void execute(ICommandSender sender, String[] args) throws CommandException {
		if (args.length != 1) {
			throw new WrongUsageException("commands.game.usage", new Object[0]);
		} else {
			if (args[0].equals("start")) {
				if (DeathCube.gameState == GameStates.Lobby) {
					DeathCube.gameState = GameStates.GameWarmup;
					DeathCube.gameTimeStart = System.currentTimeMillis();
					DeathCube.gameTimeCheck = System.currentTimeMillis();
					Log.info("Game now Warming Up.");
				} else {
					throw new WrongUsageException("Game must be in Lobby to start.", new Object[0]);
				}
			} else if (args[0].equals("end")) {
				if (DeathCube.gameState == GameStates.Running) {
					for (GameTeam team : DeathCube.gameTeams) {
						team.setAllPointsActive(false);
					}

					DeathCube.gameTimeStart = System.currentTimeMillis();
					DeathCube.gameTimeCheck = System.currentTimeMillis();
					DeathCube.gameState = GameStates.PostGame;
					
					if (DeathCube.fieldState != FieldStates.Inactive)
						DeathCube.fieldState = FieldStates.Inactive;
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
