package com.projectreddog.deathcube.init;

import com.projectreddog.deathcube.entity.EntityWaypoint;
import com.projectreddog.deathcube.reference.Reference;

import net.minecraftforge.fml.common.registry.EntityRegistry;

public class ModEntities {

	
	public static void init(Object mod) {
		 //example
		EntityRegistry.registerModEntity(EntityWaypoint.class, Reference.ENTITY_WAYPOINT_NAME, Reference.ENTITY_WAYPOINT_ID, mod, 80, 1, true);
		 
	 }
}
