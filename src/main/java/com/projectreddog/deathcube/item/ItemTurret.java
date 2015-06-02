package com.projectreddog.deathcube.item;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.World;

import com.projectreddog.deathcube.DeathCube;
import com.projectreddog.deathcube.entity.EntityTurret;
import com.projectreddog.deathcube.reference.Reference;
import com.projectreddog.deathcube.utility.Log;

public class ItemTurret extends ItemDeathCube {

	public ItemTurret() {
		super();
		this.setUnlocalizedName("turret");
		this.maxStackSize = 1;
	}

	/**
	 * This is called when the item is used, before the block is activated.
	 * 
	 * @param stack
	 *            The Item Stack
	 * @param player
	 *            The Player that used the item
	 * @param world
	 *            The Current World
	 * @param pos
	 *            Target position
	 * @param side
	 *            The side of the target hit
	 * @return Return true to prevent any further processing.
	 */
	@Override
	public boolean onItemUseFirst(ItemStack stack, EntityPlayer player, World world, BlockPos pos, EnumFacing side, float hitX, float hitY, float hitZ) {

		if (!world.isRemote) {
			int x = pos.getX();
			int y = pos.getY();
			int z = pos.getZ();
			EntityTurret et = new EntityTurret(world);
			et.setPosition(x + .5d, y + 1.0d, z + .5d);
			et.prevPosX = x + .5d;
			et.prevPosY = y + 1.0d;
			et.prevPosZ = z + .5d;
			// 1 = Red , 2 = Green , 3= Blue, 4 = yellow
			if (DeathCube.gameState == Reference.GameStates.Running) {
				if (DeathCube.playerToTeamColor.get(player.getName()).equalsIgnoreCase("Red")) {
					et.team = 1;
				} else if (DeathCube.playerToTeamColor.get(player.getName()).equalsIgnoreCase("Green")) {
					et.team = 2;
				} else if (DeathCube.playerToTeamColor.get(player.getName()).equalsIgnoreCase("Blue")) {
					et.team = 3;
				} else if (DeathCube.playerToTeamColor.get(player.getName()).equalsIgnoreCase("Yellow")) {
					et.team = 4;
				}
			}
			boolean rtn = world.spawnEntityInWorld(et);

			Log.info(rtn);
		}

		return false;
	}
}
