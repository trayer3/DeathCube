package com.projectreddog.deathcube.block;

import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.BlockPos;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.World;

import com.projectreddog.deathcube.reference.Reference;

public class BlockForceField extends BlockDeathCube{
	public float damageAmount =1;
	public BlockForceField() {
		super();
		// 1.8
		this.setUnlocalizedName(Reference.MODBLOCK_FORCE_FIELD);
		// this.setBlockTextureName(Reference.MODBLOCK_MACHINE_ASSEMBLY_TABLE);
		this.setHardness(15f);// not sure on the hardness
		this.setStepSound(soundTypeAnvil);
		this.setBlockBounds(.1f, .1f, .1f, .8f, .8f, .8f);
	}

	// 	player is touching the block on any face up,down,N,W,S or E
	public void onEntityCollidedWithBlock(World worldIn, BlockPos pos, IBlockState state, Entity entity) {
		if (!(worldIn.isRemote)){
			// only run on the server side
			if (entity instanceof EntityPlayer){
				EntityPlayer entityPlayer = (EntityPlayer) entity;
				applyForceFieldDamage(entityPlayer);
			}
		}
	}
	// 	player Right Clicked the block.. Punish them!
	@Override
	public boolean onBlockActivated(World worldIn, BlockPos pos, IBlockState state, net.minecraft.entity.player.EntityPlayer playerIn, EnumFacing side, float hitX, float hitY, float hitZ) {
		if (!(worldIn.isRemote)){
			// only run on the server side
			applyForceFieldDamage(playerIn);		
		}	
		return true;
	}

	// 	player Left Clicked the block.. Punish them!
	@Override
	public void onBlockClicked(World worldIn, BlockPos pos, EntityPlayer playerIn){
		if (!(worldIn.isRemote)){
			// only run on the server side
			applyForceFieldDamage(playerIn);		
		}		
	}

	public void applyForceFieldDamage(EntityPlayer playerIn){
		playerIn.attackEntityFrom( DamageSource.generic, this.damageAmount );
	}
}
