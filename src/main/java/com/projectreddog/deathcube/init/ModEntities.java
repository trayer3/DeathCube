package com.projectreddog.deathcube.init;

import net.minecraftforge.fml.common.registry.EntityRegistry;

import com.projectreddog.deathcube.entity.EntityTurret;
import com.projectreddog.deathcube.entity.EntityWaypoint;
import com.projectreddog.deathcube.reference.Reference;

public class ModEntities {

	public static void init(Object mod) {
		// example
		EntityRegistry.registerModEntity(EntityWaypoint.class, Reference.ENTITY_WAYPOINT_NAME, Reference.ENTITY_WAYPOINT_ID, mod, 80, 1, true);
		EntityRegistry.registerModEntity(EntityTurret.class, Reference.ENTITY_TURRET_NAME, Reference.ENTITY_TURRET_ID, mod, 80, 1, true);
	}
}
