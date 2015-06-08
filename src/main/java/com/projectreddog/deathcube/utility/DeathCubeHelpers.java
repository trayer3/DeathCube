package com.projectreddog.deathcube.utility;

import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.World;

import com.projectreddog.deathcube.init.ModBlocks;

public class DeathCubeHelpers {

	public static boolean isOpenToSky(BlockPos bp, World world) {
		// loop thru blocks up to world height to ensure every block above is air
		// if it is return true if it is not return false
		for (int i = 1; i + bp.getY() < world.getActualHeight(); ++i) {
			if ((!(world.getBlockState(bp.offset(EnumFacing.UP, i)).getBlock().isAir(world, bp.offset(EnumFacing.UP, i)))) && (!(world.getBlockState(bp.offset(EnumFacing.UP, i)).getBlock() == ModBlocks.forcefield))) {
				return false;

			}

		}
		return true;
	}
}
