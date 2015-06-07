package com.projectreddog.deathcube.item;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

import com.projectreddog.deathcube.entity.EntityRPGRocket;
import com.projectreddog.deathcube.utility.Log;

public class ItemRPGLauncher extends ItemDeathCube {

	public ItemRPGLauncher() {
		super();
		this.setUnlocalizedName("rpglauncher");
		this.maxStackSize = 1;
	}

	@Override
	public ItemStack onItemRightClick(ItemStack itemStackIn, World worldIn, EntityPlayer playerIn) {
		if (!worldIn.isRemote) {

			boolean rtn = worldIn.spawnEntityInWorld(new EntityRPGRocket(worldIn, playerIn));
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
