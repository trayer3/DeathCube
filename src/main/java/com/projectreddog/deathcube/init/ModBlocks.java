package com.projectreddog.deathcube.init;

import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.model.ModelResourceLocation;
import net.minecraft.item.Item;
import net.minecraftforge.fml.common.registry.GameRegistry;

import com.projectreddog.deathcube.block.BlockCapturePoint;
import com.projectreddog.deathcube.block.BlockDeathCube;
import com.projectreddog.deathcube.block.BlockExample;
import com.projectreddog.deathcube.block.BlockGameController;
import com.projectreddog.deathcube.block.BlockSpawnPoint;
import com.projectreddog.deathcube.reference.Reference;
import com.projectreddog.deathcube.utility.Log;

@GameRegistry.ObjectHolder(Reference.MOD_ID)
public class ModBlocks {
	public static final BlockDeathCube example = new BlockExample();
	public static final Block capturepoint = new BlockCapturePoint();
	public static final Block spawnpoint = new BlockSpawnPoint();
	public static final Block gamecontroller = new BlockGameController();

	public static void init() {
		GameRegistry.registerBlock(example, Reference.MODBLOCK_EXAMPLE);
		GameRegistry.registerBlock(capturepoint, Reference.MODBLOCK_CAPTURE_POINT);
		GameRegistry.registerBlock(spawnpoint, Reference.MODBLOCK_SPAWN_POINT);
		GameRegistry.registerBlock(gamecontroller, Reference.MODBLOCK_GAME_CONTROLLER);
		
		Log.info("Modblocks initialized");
	}

	public static void initBlockRenderer() {
		Minecraft.getMinecraft().getRenderItem().getItemModelMesher().register(Item.getItemFromBlock(example), 0, new ModelResourceLocation(Reference.MOD_ID + ":" + Reference.MODBLOCK_EXAMPLE, "inventory"));
		Minecraft.getMinecraft().getRenderItem().getItemModelMesher().register(Item.getItemFromBlock(capturepoint), 0, new ModelResourceLocation(Reference.MOD_ID + ":" + Reference.MODBLOCK_CAPTURE_POINT, "inventory"));
		Minecraft.getMinecraft().getRenderItem().getItemModelMesher().register(Item.getItemFromBlock(spawnpoint), 0, new ModelResourceLocation(Reference.MOD_ID + ":" + Reference.MODBLOCK_SPAWN_POINT, "inventory"));
		Minecraft.getMinecraft().getRenderItem().getItemModelMesher().register(Item.getItemFromBlock(gamecontroller), 0, new ModelResourceLocation(Reference.MOD_ID + ":" + Reference.MODBLOCK_GAME_CONTROLLER, "inventory"));
	}
}
