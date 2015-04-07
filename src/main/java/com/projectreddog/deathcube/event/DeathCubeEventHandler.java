package com.projectreddog.deathcube.event;

import java.util.Collection;

import com.projectreddog.deathcube.utility.Log;

import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraftforge.event.CommandEvent;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;
import net.minecraftforge.event.entity.player.PlayerDropsEvent;
import net.minecraftforge.event.world.BlockEvent;
import net.minecraftforge.event.world.BlockEvent.BreakEvent;
import net.minecraftforge.event.world.BlockEvent.PlaceEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

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

	public void onPlayerJoin(EntityJoinWorldEvent event) {
		if (event.entity instanceof EntityPlayer) {
			/**
			 * If Entity is a Player, perform an action based on Gamestate.
			 * 
			 * TODO: Action on Join per Gamestate.
			 */
		}
	}

	public void onCommandEntered(CommandEvent event) {
		/**
		 * TODO: Custom Commands for DeathCube.
		 * 
		 * Try to use a GUI, where possible or more convenient.
		 */
	}
}
