package com.projectreddog.deathcube.block;

import java.util.Random;

import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.projectile.EntityArrow;
import net.minecraft.util.BlockPos;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumWorldBlockLayer;
import net.minecraft.util.MathHelper;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

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
	
	public int quantityDropped(Random random)
    {
        return 0;
    }

    @SideOnly(Side.CLIENT)
    public EnumWorldBlockLayer getBlockLayer()
    {
        return EnumWorldBlockLayer.CUTOUT;
    }

    public boolean isFullCube()
    {
        return false;
    }

	// 	player is touching the block on any face up,down,N,W,S or E
	public void onEntityCollidedWithBlock(World worldIn, BlockPos pos, IBlockState state, Entity entity) {
		if (!(worldIn.isRemote)){
			// only run on the server side
			if (entity instanceof EntityPlayer){
				EntityPlayer entityPlayer = (EntityPlayer) entity;
				applyForceFieldDamage(entityPlayer);
			} else if (entity instanceof EntityArrow){
				if (!(entity.isDead )){
				EntityArrow entityArrow =(EntityArrow) entity;
			
				EntityArrow entityarrowNew = new EntityArrow(worldIn);
				
				entityarrowNew.renderDistanceWeight = 10.0D;
				entityarrowNew.shootingEntity = entityArrow.shootingEntity;

			        if ( entityArrow.shootingEntity instanceof EntityPlayer)
			        {
			        	entityarrowNew.canBePickedUp = 1;
			        }
			        
			        //setup offsets... may not be needed - need more testing & thinking on the math involved... test for isdead may remove need for this
			        double offsetX =0d;
			        double offsetY =0d;
			        double offsetZ =0d;
			        if (entityArrow.posX > pos.getX()+.5d){
			        	offsetX =+.5d;
			        }
			        if (entityArrow.posZ > pos.getZ()+.5d){
			        	offsetZ =+.5d;
			        }
			        if (entityArrow.posY > pos.getY()+.5d){
			        	offsetY =+.5d;
			        }			        
			        
			        if (entityArrow.posX < pos.getX()+.5d){
			        	offsetX =-.5d;
			        }
			        if (entityArrow.posZ > pos.getZ()+.5d){
			        	offsetZ =-.5d;
			        }
			        if (entityArrow.posY > pos.getY()+.5d){
			        	offsetY =-.5d;
			        }			        
			        
			        entityarrowNew.setLocationAndAngles(entityArrow.posX+offsetX, entityArrow.posY+offsetY,   entityArrow.posZ+offsetZ, entityArrow.rotationYaw, entityArrow.rotationPitch);
			        entityarrowNew.posX -= (double)(MathHelper.cos(entityarrowNew.rotationYaw / 180.0F * (float)Math.PI) * 0.16F);
			        entityarrowNew.posY -= 0.10000000149011612D;
			        entityarrowNew.posZ -= (double)(MathHelper.sin(entityarrowNew.rotationYaw / 180.0F * (float)Math.PI) * 0.16F);
			        entityarrowNew.setPosition(entityarrowNew.posX, entityarrowNew.posY, entityarrowNew.posZ);
			        entityarrowNew.motionX = (double)(-MathHelper.sin(entityarrowNew.rotationYaw / 180.0F * (float)Math.PI) * MathHelper.cos(entityarrowNew.rotationPitch / 180.0F * (float)Math.PI));
			        entityarrowNew.motionZ = (double)(MathHelper.cos(entityarrowNew.rotationYaw / 180.0F * (float)Math.PI) * MathHelper.cos(entityarrowNew.rotationPitch / 180.0F * (float)Math.PI));
			        entityarrowNew.motionY = (double)(-MathHelper.sin(entityarrowNew.rotationPitch / 180.0F * (float)Math.PI));
			        entityarrowNew.setThrowableHeading(entityarrowNew.motionX, entityarrowNew.motionY, entityarrowNew.motionZ, 1 * 1.5F, 1.0F);
			        
			        
			        
			        
			        
				
                worldIn.spawnEntityInWorld(entityarrowNew);
                
                entityArrow.setDead();

				
				}
				
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
