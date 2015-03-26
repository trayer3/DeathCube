package com.projectreddog.deathcube.block;

import com.projectreddog.deathcube.reference.Reference;

public class BlockCapturePoint extends BlockDeathCube{
	
	public BlockCapturePoint() {
		super();
		// 1.8
		this.setUnlocalizedName(Reference.MODBLOCK_CAPTURE_POINT);
		// this.setBlockTextureName(Reference.MODBLOCK_MACHINE_ASSEMBLY_TABLE);
		this.setHardness(15f);// not sure on the hardness
		this.setStepSound(soundTypeMetal);
	}
}
