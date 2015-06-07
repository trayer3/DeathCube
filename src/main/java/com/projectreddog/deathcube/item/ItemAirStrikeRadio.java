package com.projectreddog.deathcube.item;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.BlockPos;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.util.Vec3;
import net.minecraft.world.World;

import com.projectreddog.deathcube.entity.EntityRPGRocket;
import com.projectreddog.deathcube.utility.Log;

public class ItemAirStrikeRadio extends ItemDeathCube {

	public ItemAirStrikeRadio() {
		super();
		this.setUnlocalizedName("airstrikeradio");
		this.maxStackSize = 1;
	}

	@Override
	public ItemStack onItemRightClick(ItemStack itemStackIn, World worldIn, EntityPlayer playerIn) {
		if (!worldIn.isRemote) {
			float offsetHeight = 20f;
			EntityRPGRocket entityRPGRocket = new EntityRPGRocket(worldIn);

			Vec3 vec3 = new Vec3(playerIn.posX, playerIn.posY + (double) playerIn.getEyeHeight(), playerIn.posZ);
			Vec3 vec31 = playerIn.getLook(1);
			Vec3 vec32 = vec3.addVector(vec31.xCoord * 128, vec31.yCoord * 128, vec31.zCoord * 128);
			worldIn.rayTraceBlocks(vec3, vec32, false, false, true);

			MovingObjectPosition movingObjectPosition = worldIn.rayTraceBlocks(vec3, vec32, false, false, true);

			BlockPos bp;
			if (movingObjectPosition.entityHit != null) {
				bp = new BlockPos(movingObjectPosition.entityHit);
			} else {
				bp = movingObjectPosition.getBlockPos();
			}
			entityRPGRocket.setLocationAndAngles(bp.getX() + .5d, bp.getY() + offsetHeight, bp.getZ() + .5d, 0, -90);
			boolean rtn = worldIn.spawnEntityInWorld(entityRPGRocket);

			entityRPGRocket = new EntityRPGRocket(worldIn);
			entityRPGRocket.setLocationAndAngles(bp.getX() + .5d + 2, bp.getY() + offsetHeight, bp.getZ() + .5d, 0, -90);
			rtn = rtn && worldIn.spawnEntityInWorld(entityRPGRocket);
			entityRPGRocket = new EntityRPGRocket(worldIn);
			entityRPGRocket.setLocationAndAngles(bp.getX() + .5d - 2, bp.getY() + offsetHeight, bp.getZ() + .5d, 0, -90);
			rtn = rtn && worldIn.spawnEntityInWorld(entityRPGRocket);
			entityRPGRocket = new EntityRPGRocket(worldIn);
			entityRPGRocket.setLocationAndAngles(bp.getX() + .5d, bp.getY() + offsetHeight, bp.getZ() + .5d + 2, 0, -90);
			rtn = rtn && worldIn.spawnEntityInWorld(entityRPGRocket);
			entityRPGRocket = new EntityRPGRocket(worldIn);
			entityRPGRocket.setLocationAndAngles(bp.getX() + .5d, bp.getY() + offsetHeight, bp.getZ() + .5d - 2, 0, -90);
			rtn = rtn && worldIn.spawnEntityInWorld(entityRPGRocket);
			if (rtn && !playerIn.capabilities.isCreativeMode) {
				// spawned in world so reduce stack size
				// only if they are not in creative .. because FUN !
				itemStackIn.stackSize--;
			}
			Log.info(rtn);
		}

		return itemStackIn;
	}
}
