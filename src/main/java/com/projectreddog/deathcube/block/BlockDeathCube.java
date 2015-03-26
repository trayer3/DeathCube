package com.projectreddog.deathcube.block;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;

import com.projectreddog.deathcube.creativetab.CreativeTabDeathCube;
import com.projectreddog.deathcube.reference.Reference;

public class BlockDeathCube extends Block{

	protected BlockDeathCube(Material material) {
		super(material);

		// can override later ;)
		this.setCreativeTab(CreativeTabDeathCube.DEATHCUBE_TAB);
	}

	public BlockDeathCube() {
		// Generic constructor (set to rock by default)
		this(Material.rock);
	}

	@Override
	public String getUnlocalizedName() {
		return String.format("tile.%s%s", Reference.MOD_ID.toLowerCase() + ":", getUnwrappedUnlocalizedName(super.getUnlocalizedName()));
	}

	// 1.8 not needed?
	// @Override
	// @SideOnly(Side.CLIENT)
	// public void registerBlockIcons(IIconRegister iconRegister)
	// {
	// blockIcon =
	// iconRegister.registerIcon(this.getUnlocalizedName().substring(this.getUnlocalizedName().indexOf(".")+1));
	// }
	//
	protected String getUnwrappedUnlocalizedName(String unlocalizedName) {
		return unlocalizedName.substring(unlocalizedName.indexOf(".") + 1);
	}
}
