package com.projectreddog.deathcube.block;

import net.minecraft.block.BlockContainer;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumWorldBlockLayer;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import com.projectreddog.deathcube.DeathCube;
import com.projectreddog.deathcube.creativetab.CreativeTabDeathCube;
import com.projectreddog.deathcube.reference.Reference;
import com.projectreddog.deathcube.reference.Reference.GameStates;
import com.projectreddog.deathcube.tileentity.TileEntityForceFieldGen;
import com.projectreddog.deathcube.utility.Log;

public class BlockForceFieldGen extends BlockContainer {
	public float damageAmount = 5;

	public BlockForceFieldGen(Material material) {
		super(material);

		this.setCreativeTab(CreativeTabDeathCube.DEATHCUBE_TAB);
		this.setUnlocalizedName(Reference.MODBLOCK_FORCE_FIELD);
		this.setStepSound(soundTypeMetal);
		this.setBlockUnbreakable();
		this.setResistance(18000004.0f);
	}
	
	public BlockForceFieldGen() {
		this(Material.rock);
	}

	@SideOnly(Side.CLIENT)
	public EnumWorldBlockLayer getBlockLayer() {
		return EnumWorldBlockLayer.TRANSLUCENT;
	}
	
	@Override
	public TileEntity createNewTileEntity(World worldIn, int meta) {

		return new TileEntityForceFieldGen();
	}

	@Override
	public int getRenderType() {
		/**
		 * 1 = Liquid
		 * 2 = TESR
		 * 3 = Normal
		 * -1 = Nothing (air)
		 */
		return 3;
	}

	public boolean isOpaqueCube() {
		return false;
	}

	@Override
	public boolean onBlockActivated(World worldIn, BlockPos pos, IBlockState state, net.minecraft.entity.player.EntityPlayer playerIn, EnumFacing side, float hitX, float hitY, float hitZ) {
		if (playerIn.capabilities.isCreativeMode && DeathCube.gameState == GameStates.Lobby) {
			/**
			 * Can only open GUI if in Creative Mode and GameState is Lobby.
			 */
			TileEntity te = worldIn.getTileEntity(pos);
			if (te != null && !playerIn.isSneaking()) {
				Log.info("Capture Point Block Clicked!");
				playerIn.openGui(DeathCube.instance, Reference.GUI_CAPTURE_POINT, worldIn, pos.getX(), pos.getY(), pos.getZ());
				return true;
			} else {
				return false;
			}
		} else {
			return false;
		}
	}
}
