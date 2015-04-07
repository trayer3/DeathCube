package com.projectreddog.deathcube.event;

import com.projectreddog.deathcube.utility.Log;

import net.minecraft.block.Block;
import net.minecraftforge.event.world.BlockEvent.BreakEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class DeathCubeEventHandler {

	@SubscribeEvent
	public void onBlockBreak(BreakEvent event) {
		if (!event.state.getBlock().equals(Block.getBlockFromName("cobblestone")) && !event.getPlayer().capabilities.isCreativeMode) {
			/**
			 * If Block is not Cobblestone and player not in Creative, cancel block break event.
			 */
			event.setCanceled(true);

			// Test Logging
			// Log.info("Player: " + event.getPlayer().getName() +
			// " - Cancel Block Break");
		}
	}
}
