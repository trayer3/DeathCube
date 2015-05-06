package com.projectreddog.deathcube.block;

import java.util.Random;

import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.projectile.EntityArrow;
import net.minecraft.init.Blocks;
import net.minecraft.util.BlockPos;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumWorldBlockLayer;
import net.minecraft.util.MathHelper;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import com.projectreddog.deathcube.DeathCube;
import com.projectreddog.deathcube.reference.Reference;
import com.projectreddog.deathcube.reference.Reference.FieldStates;
import com.projectreddog.deathcube.reference.Reference.GameStates;

public class BlockForceField extends BlockDeathCube {
	public float damageAmount = 5;

	public BlockForceField() {
		super();

		this.setUnlocalizedName(Reference.MODBLOCK_FORCE_FIELD);
		this.setStepSound(soundTypeAnvil);
		this.setBlockBounds(.05f, .05f, .05f, .95f, .95f, .95f);
		this.setBlockUnbreakable();
		this.setResistance(18000004.0f);
	}

	public int quantityDropped(Random random) {
		return 0;
	}

	@SideOnly(Side.CLIENT)
	public EnumWorldBlockLayer getBlockLayer() {
		return EnumWorldBlockLayer.TRANSLUCENT;
	}

	public boolean isFullCube() {
		return false;
	}

	public boolean isOpaqueCube() {
		return false;
	}

	/**
	 * Player is touching the block on any face up,down,N,W,S or E
	 */
	public void onEntityCollidedWithBlock(World worldIn, BlockPos pos, IBlockState state, Entity entity) {
		if (!(worldIn.isRemote)) {
			// Server-side Only
			if (entity instanceof EntityPlayer) {
				EntityPlayer entityPlayer = (EntityPlayer) entity;
				applyForceFieldDamage(entityPlayer);
			} else if (entity instanceof EntityArrow) {
				if (!(entity.isDead)) {
					EntityArrow entityArrow = (EntityArrow) entity;

					EntityArrow entityarrowNew = new EntityArrow(worldIn);

					entityarrowNew.renderDistanceWeight = 10.0D;
					entityarrowNew.shootingEntity = entityArrow.shootingEntity;

					if (entityArrow.shootingEntity instanceof EntityPlayer) {
						entityarrowNew.canBePickedUp = 1;
					}

					// setup offsets... may not be needed - need more testing & thinking on the math involved... test for isdead may remove need for this
					double offsetX = 0d;
					double offsetY = 0d;
					double offsetZ = 0d;
					if (entityArrow.posX > pos.getX() + .5d) {
						offsetX = +.5d;
					}
					if (entityArrow.posZ > pos.getZ() + .5d) {
						offsetZ = +.5d;
					}
					if (entityArrow.posY > pos.getY() + .5d) {
						offsetY = +.5d;
					}

					if (entityArrow.posX < pos.getX() + .5d) {
						offsetX = -.5d;
					}
					if (entityArrow.posZ > pos.getZ() + .5d) {
						offsetZ = -.5d;
					}
					if (entityArrow.posY > pos.getY() + .5d) {
						offsetY = -.5d;
					}

					entityarrowNew.setLocationAndAngles(entityArrow.posX + offsetX, entityArrow.posY + offsetY, entityArrow.posZ + offsetZ, entityArrow.rotationYaw, entityArrow.rotationPitch);
					entityarrowNew.posX -= (double) (MathHelper.cos(entityarrowNew.rotationYaw / 180.0F * (float) Math.PI) * 0.16F);
					entityarrowNew.posY -= 0.10000000149011612D;
					entityarrowNew.posZ -= (double) (MathHelper.sin(entityarrowNew.rotationYaw / 180.0F * (float) Math.PI) * 0.16F);
					entityarrowNew.setPosition(entityarrowNew.posX, entityarrowNew.posY, entityarrowNew.posZ);
					entityarrowNew.motionX = (double) (-MathHelper.sin(entityarrowNew.rotationYaw / 180.0F * (float) Math.PI) * MathHelper.cos(entityarrowNew.rotationPitch / 180.0F * (float) Math.PI));
					entityarrowNew.motionZ = (double) (MathHelper.cos(entityarrowNew.rotationYaw / 180.0F * (float) Math.PI) * MathHelper.cos(entityarrowNew.rotationPitch / 180.0F * (float) Math.PI));
					entityarrowNew.motionY = (double) (-MathHelper.sin(entityarrowNew.rotationPitch / 180.0F * (float) Math.PI));
					entityarrowNew.setThrowableHeading(entityarrowNew.motionX, entityarrowNew.motionY, entityarrowNew.motionZ, 1 * 1.5F, 1.0F);

					worldIn.spawnEntityInWorld(entityarrowNew);

					entityArrow.setDead();
				}
			}
		}
	}

	@Override
	public boolean onBlockActivated(World worldIn, BlockPos pos, IBlockState state, net.minecraft.entity.player.EntityPlayer playerIn, EnumFacing side, float hitX, float hitY, float hitZ) {

		if (!(worldIn.isRemote) && playerIn.capabilities.isCreativeMode) {
			/**
			 * Player Right-Clicked the block:
			 * - If Lobby, pass through Force Field
			 * -- Check that other side is valid spawning point.
			 * -- Allow both horizontal and vertical pass-through
			 */

			BlockPos destination = pos;

			if (side.getName().equals("down")) {
				destination = pos.up();
			} else if (side.getName().equals("up")) {
				destination = pos.down(2);
			} else if (side.getName().equals("north")) {
				destination = pos.south();
			} else if (side.getName().equals("south")) {
				destination = pos.north();
			} else if (side.getName().equals("east")) {
				destination = pos.west();
			} else if (side.getName().equals("west")) {
				destination = pos.east();
			}

			if (worldIn.getBlockState(destination).getBlock() == Blocks.air && worldIn.getBlockState(destination.up()).getBlock() == Blocks.air) {
				playerIn.setPositionAndUpdate(destination.getX() + 0.5d, destination.getY(), destination.getZ() + 0.5d);
			}
		}
		return true;
	}

	/**
	 * Player Left-Clicked the block:
	 * - Punish them!
	 */
	@Override
	public void onBlockClicked(World worldIn, BlockPos pos, EntityPlayer playerIn) {
		if (!(worldIn.isRemote)) {
			// Server-side Only
			applyForceFieldDamage(playerIn);
		}
	}

	public void applyForceFieldDamage(EntityPlayer playerIn) {
		/**
		 * Only hurt player if field is Active.
		 * - Determined by GameController.
		 */
		if (DeathCube.fieldState == FieldStates.Active) {
			this.damageAmount = (float) DeathCube.forceFieldStrength;
			playerIn.attackEntityFrom(new DamageSource(randomDeathMessage()), this.damageAmount);
		}

		/**
		 * Apply knockback to player in direction away from Force Field block.
		 * - Enough velocity in xz-plane to move player at least 1 block.
		 * - Random +/- velocity in y-direction, smaller than xz amount.
		 * -- Or always do a slight positive y-direction: enhanced jumping at the cost of damage.
		 */
		// TODO: Similar direction math to Arrow bounce?
	}

	public String randomDeathMessage() {
		Random rand = new Random();

		return Reference.MOD_ID + ":" + "FORCE_FIELD_DEATH_" + (rand.nextInt(9) + 1);
	}

}
