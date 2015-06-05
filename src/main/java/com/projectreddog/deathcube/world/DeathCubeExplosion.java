package com.projectreddog.deathcube.world;

import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Random;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.enchantment.EnchantmentProtection;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityTNTPrimed;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.BlockPos;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.MathHelper;
import net.minecraft.util.Vec3;
import net.minecraft.world.World;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;

public class DeathCubeExplosion {
	private final Random explosionRNG;
	private final World worldObj;
	private final double explosionX;
	private final double explosionY;
	private final double explosionZ;
	private final Entity exploder;
	private final DamageSource damageSource;
	private final float explosionSize;
	/** A list of ChunkPositions of blocks affected by this explosion */
	private final List affectedBlockPositions;
	private final Map mapPlayerToVector;
	private static final String __OBFID = "CL_00000134";
	private final Vec3 position;

	public DeathCubeExplosion(World worldIn, Entity exploder_entity, DamageSource damageSource, double explosionX, double explosionY, double explosionZ, float explosionSize) {
		if(!worldIn.isRemote) {
	    	worldIn.setBlockToAir(new BlockPos(explosionX, explosionY, explosionZ));
	    }
		
		this.explosionRNG = new Random();
		this.affectedBlockPositions = Lists.newArrayList();
		this.mapPlayerToVector = Maps.newHashMap();
		this.worldObj = worldIn;
		this.exploder = exploder_entity;
		this.damageSource = damageSource;
		this.explosionSize = explosionSize;
		this.explosionX = explosionX;
		this.explosionY = explosionY;
		this.explosionZ = explosionZ;
		this.position = new Vec3(this.explosionX, this.explosionY, this.explosionZ);
		
		this.doExplosionA();
	    this.doExplosionB(true);
	}

	/**
	 * Does the first part of the explosion (destroy blocks)
	 */
	public void doExplosionA() {
		HashSet hashset = Sets.newHashSet();
		boolean flag = true;
		int j;
		int k;

		for (int i = 0; i < 16; ++i) {
			for (j = 0; j < 16; ++j) {
				for (k = 0; k < 16; ++k) {
					if (i == 0 || i == 15 || j == 0 || j == 15 || k == 0 || k == 15) {
						double d0 = (double) ((float) i / 15.0F * 2.0F - 1.0F);
						double d1 = (double) ((float) j / 15.0F * 2.0F - 1.0F);
						double d2 = (double) ((float) k / 15.0F * 2.0F - 1.0F);
						double d3 = Math.sqrt(d0 * d0 + d1 * d1 + d2 * d2);
						d0 /= d3;
						d1 /= d3;
						d2 /= d3;
						float f = this.explosionSize * (0.7F + this.worldObj.rand.nextFloat() * 0.6F);
						double d4 = this.explosionX;
						double d6 = this.explosionY;
						double d8 = this.explosionZ;

						for (float f1 = 0.3F; f > 0.0F; f -= 0.22500001F) {
							BlockPos blockpos = new BlockPos(d4, d6, d8);
							IBlockState iblockstate = this.worldObj.getBlockState(blockpos);

							if (iblockstate.getBlock().getMaterial() != Material.air) {
								float f2 = iblockstate.getBlock().getExplosionResistance((Entity) null);
								f -= (f2 + 0.3F) * 0.3F;
							}

							if (f > 0.0F) {
								hashset.add(blockpos);
							}

							d4 += d0 * 0.30000001192092896D;
							d6 += d1 * 0.30000001192092896D;
							d8 += d2 * 0.30000001192092896D;
						}
					}
				}
			}
		}

		this.affectedBlockPositions.addAll(hashset);
		float f3 = this.explosionSize * 2.0F;
		j = MathHelper.floor_double(this.explosionX - (double) f3 - 1.0D);
		k = MathHelper.floor_double(this.explosionX + (double) f3 + 1.0D);
		int j1 = MathHelper.floor_double(this.explosionY - (double) f3 - 1.0D);
		int l = MathHelper.floor_double(this.explosionY + (double) f3 + 1.0D);
		int k1 = MathHelper.floor_double(this.explosionZ - (double) f3 - 1.0D);
		int i1 = MathHelper.floor_double(this.explosionZ + (double) f3 + 1.0D);
		List list = this.worldObj.getEntitiesWithinAABBExcludingEntity(this.exploder, new AxisAlignedBB((double) j, (double) j1, (double) k1, (double) k, (double) l, (double) i1));
		/**
		 * I wonder if this event is important ... :-)
		 */
		// net.minecraftforge.event.ForgeEventFactory.onExplosionDetonate(this.worldObj, vanillaExplosion, list, f3);
		Vec3 vec3 = new Vec3(this.explosionX, this.explosionY, this.explosionZ);

		/**
		 * Attack entities in list (within range?)
		 */
		for (int l1 = 0; l1 < list.size(); ++l1) {
			Entity entity = (Entity) list.get(l1);

			if (!entity.func_180427_aV()) {
				double d12 = entity.getDistance(this.explosionX, this.explosionY, this.explosionZ) / (double) f3;

				if (d12 <= 1.0D) {
					double d5 = entity.posX - this.explosionX;
					double d7 = entity.posY + (double) entity.getEyeHeight() - this.explosionY;
					double d9 = entity.posZ - this.explosionZ;
					double d13 = (double) MathHelper.sqrt_double(d5 * d5 + d7 * d7 + d9 * d9);

					if (d13 != 0.0D) {
						d5 /= d13;
						d7 /= d13;
						d9 /= d13;
						double d14 = (double) this.worldObj.getBlockDensity(vec3, entity.getEntityBoundingBox());
						double d10 = (1.0D - d12) * d14;
						entity.attackEntityFrom(damageSource, (float) ((int) ((d10 * d10 + d10) / 2.0D * 8.0D * (double) f3 + 1.0D)));
						double d11 = EnchantmentProtection.func_92092_a(entity, d10);
						entity.motionX += d5 * d11;
						entity.motionY += d7 * d11;
						entity.motionZ += d9 * d11;

						if (entity instanceof EntityPlayer) {
							this.mapPlayerToVector.put((EntityPlayer) entity, new Vec3(d5 * d10, d7 * d10, d9 * d10));
						}
					}
				}
			}
		}
	}

	/**
	 * Does the second part of the explosion (sound, particles, drop spawn)
	 */
	public void doExplosionB(boolean spawnParticles) {
		this.worldObj.playSoundEffect(this.explosionX, this.explosionY, this.explosionZ, "random.explode", 4.0F, (1.0F + (this.worldObj.rand.nextFloat() - this.worldObj.rand.nextFloat()) * 0.2F) * 0.7F);

		if (this.explosionSize >= 2.0F) {
			this.worldObj.spawnParticle(EnumParticleTypes.EXPLOSION_HUGE, this.explosionX, this.explosionY, this.explosionZ, 1.0D, 0.0D, 0.0D, new int[0]);
		} else {
			this.worldObj.spawnParticle(EnumParticleTypes.EXPLOSION_LARGE, this.explosionX, this.explosionY, this.explosionZ, 1.0D, 0.0D, 0.0D, new int[0]);
			this.worldObj.spawnParticle(EnumParticleTypes.FIREWORKS_SPARK, this.explosionX, this.explosionY, this.explosionZ, 1.0D, 0.0D, 0.0D, new int[0]);
		}

		/*
		Iterator iterator;
		BlockPos blockpos;

		iterator = this.affectedBlockPositions.iterator();

		while (iterator.hasNext()) {
			blockpos = (BlockPos) iterator.next();
			Block block = this.worldObj.getBlockState(blockpos).getBlock();

			if (spawnParticles) {
				double d0 = (double) ((float) blockpos.getX() + this.worldObj.rand.nextFloat());
				double d1 = (double) ((float) blockpos.getY() + this.worldObj.rand.nextFloat());
				double d2 = (double) ((float) blockpos.getZ() + this.worldObj.rand.nextFloat());
				double d3 = d0 - this.explosionX;
				double d4 = d1 - this.explosionY;
				double d5 = d2 - this.explosionZ;
				double d6 = (double) MathHelper.sqrt_double(d3 * d3 + d4 * d4 + d5 * d5);
				d3 /= d6;
				d4 /= d6;
				d5 /= d6;
				double d7 = 0.5D / (d6 / (double) this.explosionSize + 0.1D);
				d7 *= (double) (this.worldObj.rand.nextFloat() * this.worldObj.rand.nextFloat() + 0.3F);
				d3 *= d7;
				d4 *= d7;
				d5 *= d7;
				this.worldObj.spawnParticle(EnumParticleTypes.EXPLOSION_NORMAL, (d0 + this.explosionX * 1.0D) / 2.0D, (d1 + this.explosionY * 1.0D) / 2.0D, (d2 + this.explosionZ * 1.0D) / 2.0D, d3, d4, d5, new int[0]);
				this.worldObj.spawnParticle(EnumParticleTypes.SMOKE_NORMAL, d0, d1, d2, d3, d4, d5, new int[0]);
			}
		} */
	}

	public Map func_77277_b() {
		return this.mapPlayerToVector;
	}

	/**
	 * Returns either the entity that placed the explosive block, the entity that caused the explosion or null.
	 */
	public EntityLivingBase getExplosivePlacedBy() {
		return this.exploder == null ? null : (this.exploder instanceof EntityTNTPrimed ? ((EntityTNTPrimed) this.exploder).getTntPlacedBy() : (this.exploder instanceof EntityLivingBase ? (EntityLivingBase) this.exploder : null));
	}

	public void func_180342_d() {
		this.affectedBlockPositions.clear();
	}

	public List func_180343_e() {
		return this.affectedBlockPositions;
	}

	public Vec3 getPosition() {
		return this.position;
	}
}
