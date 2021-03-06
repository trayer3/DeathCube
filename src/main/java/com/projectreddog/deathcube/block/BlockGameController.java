package com.projectreddog.deathcube.block;

import net.minecraft.block.BlockContainer;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.InventoryHelper;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.World;

import com.projectreddog.deathcube.DeathCube;
import com.projectreddog.deathcube.creativetab.CreativeTabDeathCube;
import com.projectreddog.deathcube.reference.Reference;
import com.projectreddog.deathcube.tileentity.TileEntityGameController;

public class BlockGameController extends BlockContainer {
	protected BlockGameController(Material material) {
		super(material);

		this.setCreativeTab(CreativeTabDeathCube.DEATHCUBE_TAB);
		this.setUnlocalizedName(Reference.MOD_ID.toLowerCase() + ":" + Reference.MODBLOCK_GAME_CONTROLLER);
		this.setStepSound(soundTypeMetal);
		this.setBlockUnbreakable();
		this.setResistance(18000004.0f);
	}

	public BlockGameController() {
		this(Material.rock);
	}

	@Override
	public boolean onBlockActivated(World worldIn, BlockPos pos, IBlockState state, net.minecraft.entity.player.EntityPlayer playerIn, EnumFacing side, float hitX, float hitY, float hitZ) {
		/**
		 * Can only open GUI if in Creative Mode and GameState is Lobby.
		 */
		TileEntity te = worldIn.getTileEntity(pos);
		if (te != null && !playerIn.isSneaking()) {
			playerIn.openGui(DeathCube.instance, Reference.GUI_GAME_CONTROLLER, worldIn, pos.getX(), pos.getY(), pos.getZ());
			return true;
		} else {
			return false;
		}
	};

	@Override
	public TileEntity createNewTileEntity(World worldIn, int meta) {
		return new TileEntityGameController();
	}

	@Override
	public int getRenderType() {
		/**
		 *  1 = Liquid
		 *  2 = TESR
		 *  3 = Normal
		 * -1 = Nothing (air)
		 */
		return 3;
	}

	@Override
	public boolean isOpaqueCube() {
		return false;
	}

	@Override
	public void breakBlock(World worldIn, BlockPos pos, IBlockState state) {

		/**
		 * Not needed if not using an inventory.
		 */
		TileEntity tileentity = worldIn.getTileEntity(pos);

		if (tileentity instanceof IInventory) {
			InventoryHelper.dropInventoryItems(worldIn, pos, (IInventory) tileentity);
		}

		super.breakBlock(worldIn, pos, state);
	}
}
