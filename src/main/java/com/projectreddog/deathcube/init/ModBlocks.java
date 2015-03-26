package com.projectreddog.deathcube.init;

import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.model.ModelResourceLocation;
import net.minecraft.item.Item;
import net.minecraftforge.fml.common.registry.GameRegistry;

import com.projectreddog.deathcube.block.BlockCapturePoint;
import com.projectreddog.deathcube.block.BlockDeathCube;
import com.projectreddog.deathcube.reference.Reference;

@GameRegistry.ObjectHolder(Reference.MOD_ID)
public class ModBlocks {
	public static final BlockDeathCube capturepoint = new BlockCapturePoint();
	
	public static void init(){
		GameRegistry.registerBlock(capturepoint, Reference.MODBLOCK_CAPTURE_POINT);
	}
	
	public static void initBlockRenderer() {
		Minecraft.getMinecraft().getRenderItem().getItemModelMesher().register(Item.getItemFromBlock(capturepoint), 0, new ModelResourceLocation(Reference.MOD_ID + ":" + Reference.MODBLOCK_CAPTURE_POINT, "inventory"));
	}
}
