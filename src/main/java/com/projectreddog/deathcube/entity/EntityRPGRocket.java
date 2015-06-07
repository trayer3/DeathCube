package com.projectreddog.deathcube.entity;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.projectile.EntityThrowable;
import net.minecraft.util.DamageSource;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.world.World;

import com.projectreddog.deathcube.world.DeathCubeExplosion;

public class EntityRPGRocket extends EntityThrowable {

	public int state = 0; // 0= not firing 1 = start of fire 60 = end of fire & needs rest to 0
	public static double MAX_TURN_RATE = 0.05d;
	public boolean hasTarget;

	public Entity target;

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
	}

	@Override
	protected void entityInit() {
	}

	@Override
	protected void onImpact(MovingObjectPosition mop) {

		if (mop.entityHit != null) {

			new DeathCubeExplosion(this.worldObj, null, DamageSource.generic, mop.entityHit.posX, mop.entityHit.posY, mop.entityHit.posZ, 3.0F);

		}
		new DeathCubeExplosion(this.worldObj, null, DamageSource.generic, mop.getBlockPos().getX() + 0.5, mop.getBlockPos().getY() + 0.5, mop.getBlockPos().getZ() + 0.5, 3.0F);

	}

}
