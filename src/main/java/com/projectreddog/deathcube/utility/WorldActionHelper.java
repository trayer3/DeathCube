package com.projectreddog.deathcube.utility;

import java.util.List;

import net.minecraft.enchantment.EnchantmentProtection;
import net.minecraft.entity.Entity;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.BlockPos;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.MathHelper;
import net.minecraft.util.Vec3;
import net.minecraft.world.World;

public class WorldActionHelper {

	/**
	 * Use this to create a fake explosion that will not break blocks in the world but will harm entities in the surrounding area
	 * it will do this by :
	 * 1. finding entties around the BP
	 * 2. damage the players found by damage using damage source
	 * 3 effects paricle & sounds
	 * 
	 * @param world
	 * @param bp
	 * @param size
	 * @param damageSource
	 * @return
	 */
	public static boolean doFakeExpolsion(World world, BlockPos bp, float size, DamageSource damageSource, boolean RemoveBlockAtLocation) {
		if (!world.isRemote) {// fail safe only works on server side :O
			// calculate block center
			double explosionX = bp.getX() + .5d;
			double explosionY = bp.getY() + .5d;
			double explosionZ = bp.getZ() + .5d;

			// get offsets for bounding box
			float f3 = size * 2.0F;
			int j = MathHelper.floor_double(explosionX - (double) f3 - 1.0D);
			int k = MathHelper.floor_double(explosionX + (double) f3 + 1.0D);
			int j1 = MathHelper.floor_double(explosionY - (double) f3 - 1.0D);
			int l = MathHelper.floor_double(explosionY + (double) f3 + 1.0D);
			int k1 = MathHelper.floor_double(explosionZ - (double) f3 - 1.0D);
			int i1 = MathHelper.floor_double(explosionZ + (double) f3 + 1.0D);

			List list = world.getEntitiesWithinAABBExcludingEntity(null, new AxisAlignedBB((double) j, (double) j1, (double) k1, (double) k, (double) l, (double) i1));

			Vec3 vec3 = new Vec3(explosionX, explosionY, explosionZ);

			for (int l1 = 0; l1 < list.size(); ++l1) {
				Entity entity = (Entity) list.get(l1);

				if (!entity.func_180427_aV()) {
					double d12 = entity.getDistance(explosionX, explosionY, explosionZ) / (double) f3;

					if (d12 <= 1.0D) {
						double d5 = entity.posX - explosionX;
						double d7 = entity.posY + (double) entity.getEyeHeight() - explosionY;
						double d9 = entity.posZ - explosionZ;
						double d13 = (double) MathHelper.sqrt_double(d5 * d5 + d7 * d7 + d9 * d9);

						if (d13 != 0.0D) {
							d5 /= d13;
							d7 /= d13;
							d9 /= d13;
							double d14 = (double) world.getBlockDensity(vec3, entity.getEntityBoundingBox());
							double d10 = (1.0D - d12) * d14;
							entity.attackEntityFrom(damageSource, (float) ((int) ((d10 * d10 + d10) / 2.0D * 8.0D * (double) f3 + 1.0D)));
							double d11 = EnchantmentProtection.func_92092_a(entity, d10);

							// knockback effect
							entity.motionX += d5 * d11;
							entity.motionY += d7 * d11;
							entity.motionZ += d9 * d11;
						}
					}
				}
			}
			// sound

			world.playSoundEffect(explosionX, explosionY, explosionZ, "random.explode", 4.0F, (1.0F + (world.rand.nextFloat() - world.rand.nextFloat()) * 0.2F) * 0.7F);

			// particles
			if (size >= 2.0F) {
				world.spawnParticle(EnumParticleTypes.EXPLOSION_HUGE, explosionX, explosionY, explosionZ, 1.0D, 0.0D, 0.0D, new int[0]);
			} else {
				world.spawnParticle(EnumParticleTypes.EXPLOSION_LARGE, explosionX, explosionY, explosionZ, 1.0D, 0.0D, 0.0D, new int[0]);
				world.spawnParticle(EnumParticleTypes.FIREWORKS_SPARK, explosionX, explosionY, explosionZ, 1.0D, 0.0D, 0.0D, new int[0]);
			}

			if (RemoveBlockAtLocation) {
				world.setBlockToAir(bp);
			}
			return true;

		}
		return false;
	}

}
