package com.projectreddog.deathcube.item;

import java.util.Random;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.DamageSource;
import net.minecraft.world.World;

import com.projectreddog.deathcube.entity.EntityRPGRocket;
import com.projectreddog.deathcube.reference.Reference;
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

			EntityRPGRocket entityRPGRocket = new EntityRPGRocket(worldIn, playerIn);

			entityRPGRocket.damageSource = new DamageSource(randomDeathMessage());

			boolean rtn = worldIn.spawnEntityInWorld(entityRPGRocket);
			if (rtn && !playerIn.capabilities.isCreativeMode) {
				// spawned in world so reduce stack size
				// only if they are not in creative .. because FUN !
				itemStackIn.stackSize--;
			}
			Log.info(rtn);
		}

		return itemStackIn;
	}

	public String randomDeathMessage() {
		Random rand = new Random();

		return Reference.MOD_ID + ":" + "RPG_ROCKET_DEATH_" + (rand.nextInt(9) + 1);
	}
}
