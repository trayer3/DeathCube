package com.projectreddog.deathcube.client.gui;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.network.IGuiHandler;

import com.projectreddog.deathcube.reference.Reference;
import com.projectreddog.deathcube.tileentity.TileEntitySpawnPoint;

public class GuiHandler implements IGuiHandler {

	@Override
	public Object getServerGuiElement(int ID, EntityPlayer player, World world,	int x, int y, int z) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Object getClientGuiElement(int ID, EntityPlayer player, World world,	int x, int y, int z) {
		
		if (ID == Reference.GUI_SPAWN_POINT) {

			TileEntity entity = world.getTileEntity(new BlockPos(x, y, z));
			if (entity != null) {
				if (entity instanceof TileEntitySpawnPoint) {
					return new GuiSpawnPoint((TileEntitySpawnPoint) entity);
				}
			}
		}
		
		return null;
	}

}
