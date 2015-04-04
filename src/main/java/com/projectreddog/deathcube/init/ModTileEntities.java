package com.projectreddog.deathcube.init;

import net.minecraftforge.fml.common.registry.GameRegistry;

import com.projectreddog.deathcube.reference.Reference;
import com.projectreddog.deathcube.tileentity.TileEntitySpawnPoint;

public class ModTileEntities {

	public static void init() {
		GameRegistry.registerTileEntity(TileEntitySpawnPoint.class, Reference.MODBLOCK_SPAWN_POINT);
	}
}
