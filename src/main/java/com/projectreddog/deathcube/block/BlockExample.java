package com.projectreddog.deathcube.block;

import com.projectreddog.deathcube.reference.Reference;

public class BlockExample extends BlockDeathCube{
	
	public BlockExample() {
		super();

		this.setUnlocalizedName(Reference.MODBLOCK_EXAMPLE);
		this.setHardness(15f);// not sure on the hardness
		this.setStepSound(soundTypeMetal);
	}
}
