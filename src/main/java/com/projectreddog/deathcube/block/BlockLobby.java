package com.projectreddog.deathcube.block;

import java.io.DataOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;

import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.World;

import com.projectreddog.deathcube.DeathCube;
import com.projectreddog.deathcube.init.ModConfig;
import com.projectreddog.deathcube.reference.Reference;

public class BlockLobby extends BlockDeathCube{
	
	public BlockLobby() {
		super();

		this.setUnlocalizedName(Reference.MODBLOCK_LOBBY);
		this.setStepSound(soundTypeMetal);
		this.setBlockUnbreakable();
		this.setResistance(18000004.0f);
	}
	
	@Override
	public IBlockState onBlockPlaced(World worldIn, BlockPos pos, EnumFacing facing, float hitX, float hitY, float hitZ, int meta, EntityLivingBase placer)
    {
		/**
		 * Write position to data file here.  Or call something in DeathCube.java - read/write in same place.
		 * - Check if there is already a lobby position in the data file?
		 * - Remove from data file onBlockDestroyedByPlayer/ByExplosion?
		 */
		ModConfig.updateConfig(pos);  // Should be server only?
		DeathCube.lobbySpawnPos = ModConfig.readConfig().lobbyPos;
		
        return this.getStateFromMeta(meta);
    }
}
