package com.projectreddog.deathcube.init;

import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.model.ModelResourceLocation;
import net.minecraftforge.fml.common.registry.GameRegistry;

import com.projectreddog.deathcube.item.ItemAirStrikeRadio;
import com.projectreddog.deathcube.item.ItemDeathCube;
import com.projectreddog.deathcube.item.ItemDeathSkull;
import com.projectreddog.deathcube.item.ItemLifeSkull;
import com.projectreddog.deathcube.item.ItemMinion;
import com.projectreddog.deathcube.item.ItemRPGLauncher;
import com.projectreddog.deathcube.item.ItemTurret;
import com.projectreddog.deathcube.reference.Reference;

@GameRegistry.ObjectHolder(Reference.MOD_ID)
public class ModItems {

	public static final ItemDeathCube deathskull = new ItemDeathSkull();
	public static final ItemDeathCube lifeskull = new ItemLifeSkull();
	public static final ItemDeathCube turret = new ItemTurret();
	public static final ItemDeathCube rpglauncher = new ItemRPGLauncher();
	public static final ItemDeathCube airstrikeradio = new ItemAirStrikeRadio();
	public static final ItemDeathCube minion = new ItemMinion();

	public static void init() {
		GameRegistry.registerItem(deathskull, "deathskull");
		GameRegistry.registerItem(lifeskull, "lifeskull");
		GameRegistry.registerItem(turret, "turret");
		GameRegistry.registerItem(rpglauncher, "rpglauncher");
		GameRegistry.registerItem(airstrikeradio, "airstrikeradio");
		GameRegistry.registerItem(minion, "minion");

	}

	public static void initItemRender() {
		Minecraft.getMinecraft().getRenderItem().getItemModelMesher().register(deathskull, 0, new ModelResourceLocation(Reference.MOD_ID + ":" + "deathskull", "inventory"));
		Minecraft.getMinecraft().getRenderItem().getItemModelMesher().register(lifeskull, 0, new ModelResourceLocation(Reference.MOD_ID + ":" + "lifeskull", "inventory"));
		Minecraft.getMinecraft().getRenderItem().getItemModelMesher().register(turret, 0, new ModelResourceLocation(Reference.MOD_ID + ":" + "turret", "inventory"));
		Minecraft.getMinecraft().getRenderItem().getItemModelMesher().register(rpglauncher, 0, new ModelResourceLocation(Reference.MOD_ID + ":" + "rpglauncher", "inventory"));
		Minecraft.getMinecraft().getRenderItem().getItemModelMesher().register(airstrikeradio, 0, new ModelResourceLocation(Reference.MOD_ID + ":" + "airstrikeradio", "inventory"));
		Minecraft.getMinecraft().getRenderItem().getItemModelMesher().register(minion, 0, new ModelResourceLocation(Reference.MOD_ID + ":" + "minion", "inventory"));

	}
}
