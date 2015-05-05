package com.projectreddog.deathcube.init;

import net.minecraftforge.fml.common.registry.GameRegistry;

import com.projectreddog.deathcube.reference.Reference;
import com.projectreddog.deathcube.tileentity.TileEntityCapturePoint;
import com.projectreddog.deathcube.tileentity.TileEntityForceFieldGen;
import com.projectreddog.deathcube.tileentity.TileEntityGameController;
import com.projectreddog.deathcube.tileentity.TileEntitySpawnPoint;
import com.projectreddog.deathcube.tileentity.TileEntityStartingGearConfig;

public class ModTileEntities {

	public static void init() {
		GameRegistry.registerTileEntity(TileEntitySpawnPoint.class, Reference.MODBLOCK_SPAWN_POINT);
		GameRegistry.registerTileEntity(TileEntityCapturePoint.class, Reference.MODBLOCK_CAPTURE_POINT);
		GameRegistry.registerTileEntity(TileEntityGameController.class, Reference.MODBLOCK_GAME_CONTROLLER);
		GameRegistry.registerTileEntity(TileEntityStartingGearConfig.class, Reference.MODBLOCK_STARTING_GEAR_CONFIG);
		GameRegistry.registerTileEntity(TileEntityForceFieldGen.class, Reference.MODBLOCK_FORCE_FIELD_GEN);
	}
}
