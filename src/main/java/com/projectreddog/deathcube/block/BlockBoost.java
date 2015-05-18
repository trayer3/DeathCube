package com.projectreddog.deathcube.block;

import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumWorldBlockLayer;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import com.projectreddog.deathcube.reference.Reference;
import com.projectreddog.deathcube.utility.Log;

public class BlockBoost extends BlockBasePassThrough {

	protected BlockBoost(Material materialIn) {
		super(materialIn);

		this.setUnlocalizedName(Reference.MODBLOCK_BOOST);
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
		if(entityIn.motionY < 1.0d) {
			entityIn.addVelocity(0.0d, 0.5d, 0.0d);
			entityIn.fallDistance = 0;
		}
	}
	
	@SideOnly(Side.CLIENT)
	public EnumWorldBlockLayer getBlockLayer() {
		return EnumWorldBlockLayer.TRANSLUCENT;
	}
	
	public boolean isFullCube()
    {
        return false;
    }
}
