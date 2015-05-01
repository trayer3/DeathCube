package com.projectreddog.deathcube.block;

import net.minecraft.block.state.IBlockState;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.InventoryHelper;
import net.minecraft.server.MinecraftServer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.world.World;

import com.projectreddog.deathcube.reference.Reference;

public class BlockLoot extends BlockDeathCube{
	
	public BlockLoot() {
		super();

		this.setUnlocalizedName(Reference.MODBLOCK_LOOT);
		this.setHardness(15f);// not sure on the hardness
		this.setStepSound(soundTypeMetal);
	}
	
	@Override
	public void breakBlock(World worldIn, BlockPos pos, IBlockState state) {

		/**
		 * Capture drops and change to something from preset:
		 * - Config file?
		 * - Config TE?
		 */
		//MinecraftServer.getServer().worldServers[0].spawnParticle(EnumParticleTypes.SMOKE_LARGE, pos.getX(), pos.getY(), pos.getZ(), 3, 0, 0, 0, 1, 1);

		super.breakBlock(worldIn, pos, state);
	}
}
