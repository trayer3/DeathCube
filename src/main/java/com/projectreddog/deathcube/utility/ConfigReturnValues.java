package com.projectreddog.deathcube.utility;

import net.minecraft.util.BlockPos;

public class ConfigReturnValues {
	public final String mapName;
    public final BlockPos lobbyPos;

    public ConfigReturnValues(String mapName, BlockPos lobbyPos) {
        this.mapName = mapName;
        this.lobbyPos = lobbyPos;
    }
}
