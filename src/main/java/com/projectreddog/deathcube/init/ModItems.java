package com.projectreddog.deathcube.init;

import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.model.ModelResourceLocation;
import net.minecraftforge.fml.common.registry.GameRegistry;

import com.projectreddog.deathcube.item.ItemDeathCube;
import com.projectreddog.deathcube.item.ItemExample;
import com.projectreddog.deathcube.reference.Reference;

@GameRegistry.ObjectHolder(Reference.MOD_ID)
public class ModItems {

	public static final ItemDeathCube example_item = new ItemExample();

	public static void init() {
		GameRegistry.registerItem(example_item, "example_item");
	}

	public static void initItemRender() {
		Minecraft.getMinecraft().getRenderItem().getItemModelMesher().register(example_item, 0, new ModelResourceLocation(Reference.MOD_ID + ":" + "example_item", "inventory"));
	}
}
