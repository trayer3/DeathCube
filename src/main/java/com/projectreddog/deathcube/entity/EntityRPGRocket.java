package com.projectreddog.deathcube.entity;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.projectile.EntityThrowable;
import net.minecraft.util.BlockPos;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.world.World;

import com.projectreddog.deathcube.world.DeathCubeExplosion;

public class EntityRPGRocket extends EntityThrowable {

	public int state = 0; // 0= not firing 1 = start of fire 60 = end of fire & needs rest to 0
	public static double MAX_TURN_RATE = 0.05d;
	public boolean hasTarget;

	public Entity target;

	private float currentVelocity;

	public DamageSource damageSource;

	public EntityRPGRocket(World world) {
		super(world);
	}

	public EntityRPGRocket(World world, EntityLivingBase entity) {
		super(world, entity);

	}

	public EntityRPGRocket(World world, double x, double y, double z) {
		super(world, x, y, z);
	}

	@Override
	public void onUpdate() {
		super.onUpdate();

		if (this.worldObj.isRemote) {
			this.worldObj.spawnParticle(EnumParticleTypes.FLAME, this.posX, this.posY, this.posZ, 0, 0, 0);
			this.worldObj.spawnParticle(EnumParticleTypes.CLOUD, this.posX, this.posY, this.posZ, 0, 0, 0);

			this.worldObj.spawnParticle(EnumParticleTypes.CLOUD, this.posX, this.posY, this.posZ, 0, 0, 0);

			this.worldObj.spawnParticle(EnumParticleTypes.CLOUD, this.posX, this.posY, this.posZ, 0, 0, 0);

		}
	}

	@Override
	protected void entityInit() {
	}

	/**
	 * Gets the amount of gravity to apply to the thrown entity with each tick.
	 */
	@Override
	protected float getGravityVelocity() {
		return 0.03F;
	}

	public float getVelocity() {

		return 3F;

	}

	@Override
	protected void onImpact(MovingObjectPosition mop) {

		if (this.damageSource == null) {
			// set default if none is set
			this.damageSource = DamageSource.generic;
		}
		if (mop.entityHit != null) {

			new DeathCubeExplosion(this.worldObj, null, this.damageSource, mop.entityHit.posX, mop.entityHit.posY, mop.entityHit.posZ, 5.0F, false);

		} else {
			if (this.worldObj.isAirBlock(mop.getBlockPos())) {
				// its air so explode here
				new DeathCubeExplosion(this.worldObj, null, this.damageSource, mop.getBlockPos().getX() + 0.5, mop.getBlockPos().getY() + 0.5, mop.getBlockPos().getZ() + 0.5, 5.0F, false);
			} else {
				// we were inside a block when it hapend so the BP is off
				// we need to shift to the side that was hit and explode on that side
				BlockPos bp = mop.getBlockPos().offset(mop.sideHit, 1);
				new DeathCubeExplosion(this.worldObj, null, this.damageSource, bp.getX() + 0.5, bp.getY() + 0.5, bp.getZ() + 0.5, 5.0F, false);

			}

		}
		this.setDead();
	}
}
