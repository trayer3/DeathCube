package com.projectreddog.deathcube.event;

import java.util.Collection;

import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.Blocks;
import net.minecraft.world.WorldSettings;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;
import net.minecraftforge.event.entity.player.PlayerDropsEvent;
import net.minecraftforge.event.world.BlockEvent;
import net.minecraftforge.event.world.BlockEvent.BreakEvent;
import net.minecraftforge.event.world.BlockEvent.PlaceEvent;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.event.FMLServerStartingEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import com.projectreddog.deathcube.DeathCube;
import com.projectreddog.deathcube.command.CommandGame;
import com.projectreddog.deathcube.reference.Reference.GameStates;
import com.projectreddog.deathcube.tileentity.TileEntityGameController;
import com.projectreddog.deathcube.utility.Log;

public class DeathCubeEventHandler {

	@SubscribeEvent
	public void onBlockBreak(BreakEvent event) {
		if (!event.getPlayer().capabilities.isCreativeMode) {
			/**
			 * If player not in Creative, check which blocks they should be allowed to break.
			 */
			Block brokenBlock = event.state.getBlock();

			if (brokenBlock.equals(Blocks.cobblestone) || brokenBlock.equals(Blocks.tallgrass) || brokenBlock.equals(Blocks.yellow_flower) || brokenBlock.equals(Blocks.red_flower) || brokenBlock.equals(Blocks.double_plant) || brokenBlock.equals(Blocks.mob_spawner) || brokenBlock.equals(Blocks.web) || brokenBlock.equals(Blocks.leaves) || brokenBlock.equals(Blocks.leaves2)) {
				/**
				 * Do Nothing. It is OK to break these blocks.
				 */

				/**
				 * Test Logging
				 */
				// Log.info("Player: " + event.getPlayer().getName() + " - Block OK to Break");

			} else {
				/**
				 * If Block is not Cobblestone (or others), cancel block break event.
				 */
				event.setCanceled(true);

				/**
				 * Test Logging
				 */
				// Log.info("Player: " + event.getPlayer().getName() + " - Cancel Block Break");
			}
		}
	}

	@SubscribeEvent
	public void onBlockPlace(PlaceEvent event) {
		if (!event.player.capabilities.isCreativeMode) {
			Block placedBlock = event.placedBlock.getBlock();

			if (placedBlock.equals(Blocks.cobblestone)) {
				/**
				 * Do nothing. It is OK to place these blocks.
				 */
			} else {
				event.setCanceled(true);
			}
		}
	}

	@SubscribeEvent
	public void onBlockDrop(BlockEvent.HarvestDropsEvent event) {
		Block brokenBlock = event.state.getBlock();

		if (brokenBlock.equals(Blocks.cobblestone)) {
			/**
			 * If broken block is Cobblestone, drop block with 50% chance to receive drops.
			 */
			event.dropChance = 0.5f;
		} else {
			/**
			 * For all other blocks, do not drop anything
			 */
			event.drops.clear();
		}
	}

	@SubscribeEvent
	public void onPlayerDeathDrops(PlayerDropsEvent event) {
		if (event.drops.contains(Blocks.cobblestone)) {
			/**
			 * If there is cobblestone, drop only the cobblestone.
			 * 
			 * TODO: Player drops on death. Test: Does this work?
			 */
			event.drops.retainAll((Collection<?>) Blocks.cobblestone);
		} else {
			event.drops.clear();
		}
	}

	@SubscribeEvent
	public void onPlayerJoin(EntityJoinWorldEvent event) {
		//Log.info("Entity joined game.");
		if (!event.world.isRemote && event.entity instanceof EntityPlayer) {  //instanceof EntityPlayerMP
			/**
			 * If Entity is a Player, perform an action based on Gamestate.
			 */
			
			if(DeathCube.gameState != null) {
				if(DeathCube.gameState != GameStates.Running) {
					/**
					 * TODO: If not Running state, teleport to Lobby.
					 */
					TileEntityGameController.sendPlayerToLobby((EntityPlayer) event.entity);
					
					Log.info("Game not Running: Player joined Lobby.");
				} else {
					/**
					 * If Running state, add to team and spawn in game.
					 * - Check if already on team.  This will be called every respawn too.
					 */
					if(DeathCube.playerToTeamColor.containsKey(((EntityPlayer) event.entity).getName())){
						/**
						 * Player already on a Team.  
						 * 
						 * TODO: Perform Death Penalty.  Spectate teammate, or Penalty Box.
						 * - Not just in spectate mode.
						 * 
						 * Debug - Send to Lobby for now.  
						 * - Then make Spectator.  
						 * - Then add to queue to rejoin game.
						 */
						Log.info("Player is on a team.  Debug: Spawning in Lobby for now.");
						TileEntityGameController.sendPlayerToLobby((EntityPlayer) event.entity);
						((EntityPlayer) event.entity).setGameType(WorldSettings.GameType.SPECTATOR);
						//((EntityPlayerMP) event.entity).setSpectatingEntity(teammate);
						
						/**
						 * Add to Queue to respawn in game.
						 */
						DeathCube.playerAwaitingRespawn.put(((EntityPlayer) event.entity).getName(), System.currentTimeMillis());
					} else {
						/**
						 * Player not on a Team yet.  Assign Team.
						 */
						TileEntityGameController.assignPlayerToTeam((EntityPlayer) event.entity);
						Log.info("Assigned new player to a team.");
					}
					Log.info("Player joined Running Game.");
				}
			}
		} else if (!event.world.isRemote && event.entity instanceof EntityPlayerMP) {
			Log.info("Entity is EntityPlayerMP.");
		}
	}
}
