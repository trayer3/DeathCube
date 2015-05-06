package com.projectreddog.deathcube.event;

import java.util.Iterator;
import java.util.List;

import net.minecraft.block.Block;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.server.MinecraftServer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.WorldServer;
import net.minecraft.world.WorldSettings;
import net.minecraft.world.storage.WorldInfo;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;
import net.minecraftforge.event.entity.player.PlayerDropsEvent;
import net.minecraftforge.event.world.BlockEvent;
import net.minecraftforge.event.world.BlockEvent.BreakEvent;
import net.minecraftforge.event.world.BlockEvent.PlaceEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import com.projectreddog.deathcube.DeathCube;
import com.projectreddog.deathcube.init.ModBlocks;
import com.projectreddog.deathcube.init.ModItems;
import com.projectreddog.deathcube.init.ModNetwork;
import com.projectreddog.deathcube.network.MessageHandleClientGameUpdate;
import com.projectreddog.deathcube.network.MessageRequestTextUpdate_Client;
import com.projectreddog.deathcube.reference.Reference.GameStates;
import com.projectreddog.deathcube.tileentity.TileEntityCapturePoint;
import com.projectreddog.deathcube.tileentity.TileEntityGameController;
import com.projectreddog.deathcube.tileentity.TileEntitySpawnPoint;
import com.projectreddog.deathcube.utility.Log;

public class DeathCubeEventHandler {

	@SubscribeEvent
	public void onBlockBreak(BreakEvent event) {
		if (!event.getPlayer().capabilities.isCreativeMode) {
			/**
			 * If player not in Creative, check which blocks they should be allowed to break.
			 */
			Block brokenBlock = event.state.getBlock();

			if (brokenBlock.equals(Blocks.cobblestone) || brokenBlock.equals(ModBlocks.loot_block) || brokenBlock.equals(Blocks.tallgrass) || brokenBlock.equals(Blocks.yellow_flower) || brokenBlock.equals(Blocks.red_flower) || brokenBlock.equals(Blocks.double_plant) || brokenBlock.equals(Blocks.mob_spawner) || brokenBlock.equals(Blocks.web) || brokenBlock.equals(Blocks.leaves)
					|| brokenBlock.equals(Blocks.leaves2)) {
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

			if (placedBlock.equals(Blocks.cobblestone) || placedBlock.equals(ModBlocks.loot_block)) {
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
		} else if (brokenBlock.equals(ModBlocks.loot_block)) {
			/**
			 * Also OK.
			 */
		} else {
			/**
			 * For all other blocks, do not drop anything
			 */
			event.drops.clear();
		}
	}

	@SubscribeEvent
	public void onPlayerDeathDrops(PlayerDropsEvent event) {
		//Log.info("Number of Drops: " + event.drops.size());

		for (Iterator<EntityItem> keyIterator = event.drops.iterator(); keyIterator.hasNext();) {
			EntityItem eItem = keyIterator.next();
			Item dropItem = eItem.getEntityItem().getItem();
			if (dropItem == Item.getItemFromBlock(Blocks.cobblestone)) {
				//Log.info("Techstack found cobble!");
			} else if (dropItem == ModItems.deathskull) {
				//Log.info("Techstack found a Death Skull!");
			} else if (dropItem == ModItems.lifeskull) {
				//Log.info("Techstack found a Life Skull!");
			} else {
				/**
				 * Not an allowed drop.  Remove from drops.
				 */
				keyIterator.remove();
				// Log.info("Techstack did not find cobble ...");
			}
		}
	}

	@SubscribeEvent
	public void onPlayerJoin(EntityJoinWorldEvent event) {
		// Log.info("Entity joined game.");
		if (!event.world.isRemote && event.entity instanceof EntityPlayer) { // instanceof EntityPlayerMP
			/**
			 * If Entity is a Player, perform an action based on Gamestate.
			 */

			if (DeathCube.gameState != null) {
				if (DeathCube.gameState != GameStates.Running) {
					/**
					 * TODO: If not Running state, teleport to Lobby.
					 */
					TileEntityGameController.sendPlayerToLobby((EntityPlayer) event.entity);

					String[] names = { "None" };
					int[] points = { 0 };
					double[] pointTimes = { 0.0d };
					ModNetwork.simpleNetworkWrapper.sendTo(new MessageHandleClientGameUpdate(false, 0, names, points, pointTimes, 0), (EntityPlayerMP) event.entity);

					Log.info("Game not Running: Player joined Lobby.");

					/**
					 * Loop through Tile Entities & Send Text Update to all players
					 * - Only if game is not Running
					 */
					List<TileEntity> loadedTEList = MinecraftServer.getServer().getEntityWorld().loadedTileEntityList;
					for (TileEntity te : loadedTEList) {
						if (te instanceof TileEntityGameController) {
							ModNetwork.sendToServer(new MessageRequestTextUpdate_Client(te.getPos()));
						} else if (te instanceof TileEntityCapturePoint) {
							ModNetwork.sendToServer(new MessageRequestTextUpdate_Client(te.getPos()));
						} else if (te instanceof TileEntitySpawnPoint) {
							ModNetwork.sendToServer(new MessageRequestTextUpdate_Client(te.getPos()));
						}
					}
				} else {
					/**
					 * If Running state, add to team and spawn in game.
					 * - Check if already on team. This will be called every respawn too.
					 */
					if (DeathCube.playerToTeamColor.containsKey(((EntityPlayer) event.entity).getName())) {
						/**
						 * Player already on a Team.
						 * 
						 * TODO: Perform Death Penalty. Spectate teammate, or Penalty Box.
						 * - Not just in spectate mode.
						 * 
						 * Debug - Send to Lobby for now.
						 * - Then make Spectator.
						 * - Then add to queue to rejoin game.
						 */
						Log.info("Player is on a team.  Debug: Spawning in Lobby for now.");
						TileEntityGameController.sendPlayerToLobby((EntityPlayer) event.entity);
						((EntityPlayer) event.entity).setGameType(WorldSettings.GameType.SPECTATOR);
						// ((EntityPlayerMP) event.entity).setSpectatingEntity(teammate);

						/**
						 * Add to Queue to respawn in game.
						 */
						DeathCube.playerAwaitingRespawn.put(((EntityPlayer) event.entity).getName(), System.currentTimeMillis());
					} else {
						/**
						 * Player not on a Team yet. Assign Team.
						 */
						TileEntityGameController.assignPlayerToTeam((EntityPlayer) event.entity);
						Log.info("Assigned new player to a team.");
					}
					Log.info("Player joined Running Game.");
				}
			}
			
			/**
			 * Set weather to clear every time a player joins / respawns.
			 */
			if(MinecraftServer.getServer().worldServers[0] != null) {
				WorldServer worldserver = MinecraftServer.getServer().worldServers[0];
		        WorldInfo worldinfo = worldserver.getWorldInfo();
		        
		        worldinfo.setCleanWeatherTime(1728000);
		        worldinfo.setRainTime(0);
		        worldinfo.setThunderTime(0);
		        worldinfo.setRaining(false);
		        worldinfo.setThundering(false);
			}
		} else if (!event.world.isRemote && event.entity instanceof EntityPlayerMP) {
			Log.info("Entity is EntityPlayerMP.");
		}
	}
}
