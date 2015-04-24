package com.projectreddog.deathcube.block;

import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.World;

import com.projectreddog.deathcube.reference.Reference;

public class BlockLobby extends BlockDeathCube{
	
	public BlockLobby() {
		super();

		this.setUnlocalizedName(Reference.MODBLOCK_EXAMPLE);
		this.setStepSound(soundTypeMetal);
		this.setBlockUnbreakable();
		this.setResistance(18000004.0f);
	}
	
	@Override
	public IBlockState onBlockPlaced(World worldIn, BlockPos pos, EnumFacing facing, float hitX, float hitY, float hitZ, int meta, EntityLivingBase placer)
    {
		/**
		 * Write position to data file here.  ???
		 * - Check if there is already a lobby position in the data file?
		 * - Remove from data file onBlockDestroyedByPlayer/ByExplosion?
		 */
		
		
        return this.getStateFromMeta(meta);
    }
}
