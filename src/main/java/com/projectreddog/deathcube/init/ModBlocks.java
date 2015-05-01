package com.projectreddog.deathcube.init;

import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.model.ModelResourceLocation;
import net.minecraft.item.Item;
import net.minecraftforge.fml.common.registry.GameRegistry;

import com.projectreddog.deathcube.block.BlockCapturePoint;
import com.projectreddog.deathcube.block.BlockDeathCube;
import com.projectreddog.deathcube.block.BlockExample;
import com.projectreddog.deathcube.block.BlockForceField;
import com.projectreddog.deathcube.block.BlockGameController;
import com.projectreddog.deathcube.block.BlockLobby;
import com.projectreddog.deathcube.block.BlockLoot;
import com.projectreddog.deathcube.block.BlockSpawnPoint;
import com.projectreddog.deathcube.block.BlockStartingGearConfig;
import com.projectreddog.deathcube.reference.Reference;
import com.projectreddog.deathcube.utility.Log;

@GameRegistry.ObjectHolder(Reference.MOD_ID)
public class ModBlocks {
	public static final BlockDeathCube example = new BlockExample();
	public static final BlockDeathCube lobby = new BlockLobby();
	public static final BlockDeathCube loot_block = new BlockLoot();
	public static final Block startinggearconfig = new BlockStartingGearConfig();
	public static final Block capturepoint = new BlockCapturePoint();
	public static final Block spawnpoint = new BlockSpawnPoint();
	public static final Block gamecontroller = new BlockGameController();
	public static final Block forcefield = new BlockForceField();

	public static void init() {
		GameRegistry.registerBlock(example, Reference.MODBLOCK_EXAMPLE);
		GameRegistry.registerBlock(lobby, Reference.MODBLOCK_LOBBY);
		GameRegistry.registerBlock(loot_block, Reference.MODBLOCK_LOOT);
		GameRegistry.registerBlock(startinggearconfig, Reference.MODBLOCK_STARTING_GEAR_CONFIG);
		GameRegistry.registerBlock(capturepoint, Reference.MODBLOCK_CAPTURE_POINT);
		GameRegistry.registerBlock(spawnpoint, Reference.MODBLOCK_SPAWN_POINT);
		GameRegistry.registerBlock(gamecontroller, Reference.MODBLOCK_GAME_CONTROLLER);
		GameRegistry.registerBlock(forcefield, Reference.MODBLOCK_FORCE_FIELD);

		Log.info("Modblocks initialized");
	}

	public static void initBlockRenderer() {
		Minecraft.getMinecraft().getRenderItem().getItemModelMesher().register(Item.getItemFromBlock(example), 0, new ModelResourceLocation(Reference.MOD_ID + ":" + Reference.MODBLOCK_EXAMPLE, "inventory"));
		Minecraft.getMinecraft().getRenderItem().getItemModelMesher().register(Item.getItemFromBlock(lobby), 0, new ModelResourceLocation(Reference.MOD_ID + ":" + Reference.MODBLOCK_LOBBY, "inventory"));
		Minecraft.getMinecraft().getRenderItem().getItemModelMesher().register(Item.getItemFromBlock(loot_block), 0, new ModelResourceLocation(Reference.MOD_ID + ":" + Reference.MODBLOCK_LOOT, "inventory"));
		Minecraft.getMinecraft().getRenderItem().getItemModelMesher().register(Item.getItemFromBlock(startinggearconfig), 0, new ModelResourceLocation(Reference.MOD_ID + ":" + Reference.MODBLOCK_STARTING_GEAR_CONFIG, "inventory"));
		Minecraft.getMinecraft().getRenderItem().getItemModelMesher().register(Item.getItemFromBlock(capturepoint), 0, new ModelResourceLocation(Reference.MOD_ID + ":" + Reference.MODBLOCK_CAPTURE_POINT, "inventory"));
		Minecraft.getMinecraft().getRenderItem().getItemModelMesher().register(Item.getItemFromBlock(spawnpoint), 0, new ModelResourceLocation(Reference.MOD_ID + ":" + Reference.MODBLOCK_SPAWN_POINT, "inventory"));
		Minecraft.getMinecraft().getRenderItem().getItemModelMesher().register(Item.getItemFromBlock(gamecontroller), 0, new ModelResourceLocation(Reference.MOD_ID + ":" + Reference.MODBLOCK_GAME_CONTROLLER, "inventory"));
		Minecraft.getMinecraft().getRenderItem().getItemModelMesher().register(Item.getItemFromBlock(forcefield), 0, new ModelResourceLocation(Reference.MOD_ID + ":" + Reference.MODBLOCK_FORCE_FIELD, "inventory"));

	}
}
