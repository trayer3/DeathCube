package com.projectreddog.deathcube.init;

import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.model.ModelResourceLocation;
import net.minecraftforge.fml.common.registry.GameRegistry;

import com.projectreddog.deathcube.item.ItemDeathCube;
import com.projectreddog.deathcube.item.ItemDeathSkull;
import com.projectreddog.deathcube.item.ItemLifeSkull;
import com.projectreddog.deathcube.reference.Reference;

@GameRegistry.ObjectHolder(Reference.MOD_ID)
public class ModItems {

	public static final ItemDeathCube deathskull = new ItemDeathSkull();
	public static final ItemDeathCube lifeskull = new ItemLifeSkull();

	public static void init() {
		GameRegistry.registerItem(deathskull, "deathskull");
		GameRegistry.registerItem(lifeskull, "lifeskull");
	}

	public static void initItemRender() {
		Minecraft.getMinecraft().getRenderItem().getItemModelMesher().register(deathskull, 0, new ModelResourceLocation(Reference.MOD_ID + ":" + "deathskull", "inventory"));
		Minecraft.getMinecraft().getRenderItem().getItemModelMesher().register(lifeskull, 0, new ModelResourceLocation(Reference.MOD_ID + ":" + "lifeskull", "inventory"));
	}
}
