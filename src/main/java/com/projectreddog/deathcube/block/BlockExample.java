package com.projectreddog.deathcube.block;

import com.projectreddog.deathcube.reference.Reference;

public class BlockExample extends BlockDeathCube{
	
	public BlockExample() {
		super();
		// 1.8
		this.setUnlocalizedName(Reference.MODBLOCK_EXAMPLE);
		// this.setBlockTextureName(Reference.MODBLOCK_MACHINE_ASSEMBLY_TABLE);
		this.setHardness(15f);// not sure on the hardness
		this.setStepSound(soundTypeMetal);
	}
}
