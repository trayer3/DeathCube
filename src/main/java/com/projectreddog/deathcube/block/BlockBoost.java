package com.projectreddog.deathcube.block;

import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.util.BlockPos;
import net.minecraft.world.World;

import com.projectreddog.deathcube.reference.Reference;
import com.projectreddog.deathcube.utility.Log;

public class BlockBoost extends BlockBasePassThrough {

	protected BlockBoost(Material materialIn) {
		super(materialIn);

		this.setUnlocalizedName(Reference.MODBLOCK_FORCE_FIELD);
		this.setStepSound(soundTypeAnvil);
		this.setBlockUnbreakable();
		this.setResistance(18000004.0f);
	}
	
	public BlockBoost()
    {
        super(Material.rock);
    }

	/**
	 * Called When an Entity Collided with the Block
	 */
	public void onEntityCollidedWithBlock(World worldIn, BlockPos pos, IBlockState state, Entity entityIn) {
		if (!worldIn.isRemote) {
			entityIn.addVelocity(0.0d, 1.0d, 0.0d);
			Log.info("Jump!");
		}
	}
}
