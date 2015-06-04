package com.projectreddog.deathcube.block;

import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.BlockPos;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumWorldBlockLayer;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import com.projectreddog.deathcube.reference.Reference;
import com.projectreddog.deathcube.utility.WorldActionHelper;

public class BlockMine extends BlockBasePassThrough {

	protected BlockMine(Material materialIn) {
		super(materialIn);

		this.setUnlocalizedName(Reference.MODBLOCK_MINE);
		this.setResistance(0.0f);
	}

	public BlockMine() {
		super(Material.rock);
	}

	/**
	 * Called When an Entity Collided with the Block
	 */
	public void onEntityCollidedWithBlock(World worldIn, BlockPos pos, IBlockState state, Entity entityIn) {
		/**
		 * Summon Explosion (that does not damage terrain - how?)
		 * - Remove block
		 */
		WorldActionHelper.doFakeExpolsion(worldIn, pos, 1, DamageSource.generic);

	}

	/**
	 * Find function for when block is removed by anything, player or another explosion.
	 * - Create an explosion, i.e. chain reaction of explosions
	 */
	@Override
	public boolean removedByPlayer(World world, BlockPos pos, EntityPlayer player, boolean willHarvest) {
		if (!world.isRemote && !player.capabilities.isCreativeMode) {
			world.createExplosion(null, pos.getX() + 0.5, pos.getY() + 0.5, pos.getZ() + 0.5, 3.0F, true);
		}
		return world.setBlockToAir(pos);
	}

	@Override
	public boolean onBlockActivated(World worldIn, BlockPos pos, IBlockState state, net.minecraft.entity.player.EntityPlayer playerIn, EnumFacing side, float hitX, float hitY, float hitZ) {
		if (!worldIn.isRemote) {
			worldIn.createExplosion(null, pos.getX() + 0.5, pos.getY() + 0.5, pos.getZ() + 0.5, 3.0F, true);
		}
		return false; // Or true?
	}

	@SideOnly(Side.CLIENT)
	public EnumWorldBlockLayer getBlockLayer() {
		return EnumWorldBlockLayer.TRANSLUCENT;
	}

	public boolean isFullCube() {
		return false;
	}

	public void setBlockBoundsBasedOnState(IBlockAccess worldIn, BlockPos pos) {
		boolean flag = true; // this.getRedstoneStrength(worldIn.getBlockState(pos)) > 0;
		float x_z_border = 0.0625F;
		float y_state1 = 0.03125F;
		float y_state2 = 0.0625F;

		if (flag) {
			this.setBlockBounds(x_z_border, 0.0F, 0.f, 1.0F - x_z_border, y_state1, 1.0F - x_z_border);
		} else {
			this.setBlockBounds(x_z_border, 0.0F, 0.f, 1.0F - x_z_border, y_state2, 1.0F - x_z_border);
		}
	}
}
