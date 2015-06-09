package com.projectreddog.deathcube.item;

import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.BlockPos;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.util.Vec3;
import net.minecraft.world.World;

import com.projectreddog.deathcube.entity.EntityDrone;
import com.projectreddog.deathcube.utility.DeathCubeHelpers;
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
			// EntityRPGRocket entityRPGRocket = new EntityRPGRocket(worldIn);

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

			if (DeathCubeHelpers.isOpenToSky(bp, worldIn)) {
				// // only run if open to sky
				// entityRPGRocket.setLocationAndAngles(bp.getX() + .5d, bp.getY() + offsetHeight, bp.getZ() + .5d, 0, -90);
				// entityRPGRocket.setVelocity(0, entityRPGRocket.getVelocity() * -1, 0);
				// boolean rtn = worldIn.spawnEntityInWorld(entityRPGRocket);
				//
				// entityRPGRocket = new EntityRPGRocket(worldIn);
				// entityRPGRocket.setLocationAndAngles(bp.getX() + .5d + 2, bp.getY() + offsetHeight, bp.getZ() + .5d, 0, -90);
				// entityRPGRocket.setVelocity(0, entityRPGRocket.getVelocity() * -1, 0);
				// rtn = rtn && worldIn.spawnEntityInWorld(entityRPGRocket);
				//
				// entityRPGRocket = new EntityRPGRocket(worldIn);
				// entityRPGRocket.setLocationAndAngles(bp.getX() + .5d - 2, bp.getY() + offsetHeight, bp.getZ() + .5d, 0, -90);
				// entityRPGRocket.setVelocity(0, entityRPGRocket.getVelocity() * -1, 0);
				// rtn = rtn && worldIn.spawnEntityInWorld(entityRPGRocket);
				//
				// entityRPGRocket = new EntityRPGRocket(worldIn);
				// entityRPGRocket.setLocationAndAngles(bp.getX() + .5d, bp.getY() + offsetHeight, bp.getZ() + .5d + 2, 0, -90);
				// entityRPGRocket.setVelocity(0, entityRPGRocket.getVelocity() * -1, 0);
				// rtn = rtn && worldIn.spawnEntityInWorld(entityRPGRocket);
				//
				// entityRPGRocket = new EntityRPGRocket(worldIn);
				// entityRPGRocket.setLocationAndAngles(bp.getX() + .5d, bp.getY() + offsetHeight, bp.getZ() + .5d - 2, 0, -90);
				// entityRPGRocket.setVelocity(0, entityRPGRocket.getVelocity() * -1, 0);
				// rtn = rtn && worldIn.spawnEntityInWorld(entityRPGRocket);

				// summon the entity drone
				EntityDrone entityDrone = new EntityDrone(worldIn, new BlockPos(bp.getX(), bp.getY() + 20, bp.getZ()));
				entityDrone.posX = bp.getX();
				entityDrone.posY = 128;// worldIn.getActualHeight();
				entityDrone.posZ = bp.getZ();

				entityDrone.forceSpawn = true;
				entityDrone.targetPos = bp.offset(EnumFacing.UP,20);
				boolean rtn = worldIn.spawnEntityInWorld(entityDrone);

				if (rtn && !playerIn.capabilities.isCreativeMode) {
					// spawned in world so reduce stack size
					// only if they are not in creative .. because FUN !
					itemStackIn.stackSize--;
				}
				Log.info(rtn);
			} else {
				Minecraft.getMinecraft().thePlayer.addChatMessage(new ChatComponentText("Invalid Location. It is not open to the sky!"));

			}

		}

		return itemStackIn;
	}
}
