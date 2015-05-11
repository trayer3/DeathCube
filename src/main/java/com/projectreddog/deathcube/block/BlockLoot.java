package com.projectreddog.deathcube.block;

import java.util.List;
import java.util.Random;

import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyInteger;
import net.minecraft.block.state.BlockState;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.util.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

import com.projectreddog.deathcube.init.ModItems;
import com.projectreddog.deathcube.reference.Reference;

public class BlockLoot extends BlockDeathCube {
	public static final PropertyInteger ACTIVE_STATE = PropertyInteger.create("active_state", 0, 1);
	
	public BlockLoot() {
		super();

		this.setUnlocalizedName(Reference.MODBLOCK_LOOT);
		this.setHardness(5f);// not sure on the hardness
		this.setStepSound(soundTypeMetal);
	}

	@Override
	public void breakBlock(World worldIn, BlockPos pos, IBlockState state) {

		/**
		 * Capture drops and change to something from preset:
		 * - Config file?
		 * - Config TE?
		 */
		// MinecraftServer.getServer().worldServers[0].spawnParticle(EnumParticleTypes.SMOKE_LARGE, pos.getX(), pos.getY(), pos.getZ(), 3, 0, 0, 0, 1, 1);

		super.breakBlock(worldIn, pos, state);
	}
	
	public void onBlockDestroyedByPlayer(World worldIn, BlockPos pos, IBlockState state) {
	
	}
	
	public void onBlockHarvested(World worldIn, BlockPos pos, IBlockState state, EntityPlayer player) {
	
	}
	
	/**
     * Triggered whenever an entity collides with this block (enters into the block)
     */
    public void onEntityCollidedWithBlock(World worldIn, BlockPos pos, Entity entityIn) {
    
    }
    
    /**
     * Called When an Entity Collided with the Block
     */
    public void onEntityCollidedWithBlock(World worldIn, BlockPos pos, IBlockState state, Entity entityIn) {
    
    }
	
	public void updateTick(World worldIn, BlockPos pos, IBlockState state, Random rand) {
	
	}

	/**
	 * This returns a complete list of items dropped from this block.
	 *
	 * @param world
	 *            The current world
	 * @param pos
	 *            Block position in world
	 * @param state
	 *            Current state
	 * @param fortune
	 *            Breakers fortune level
	 * @return A ArrayList containing all items this block drops
	 */
	@Override
	public List<ItemStack> getDrops(IBlockAccess world, BlockPos pos, IBlockState state, int fortune) {
		List<ItemStack> ret = new java.util.ArrayList<ItemStack>();

		Random rand = world instanceof World ? ((World) world).rand : RANDOM;
		float randValue = rand.nextFloat();

		int count = quantityDropped(state, fortune, rand);
		for (int i = 0; i < count; i++) {
			if (randValue < 0.33) {
				ret.add(new ItemStack(Items.arrow, 10));
			} else if (randValue > 0.66) {
				ret.add(new ItemStack(ModItems.lifeskull));
			} else {
				ret.add(new ItemStack(ModItems.deathskull));
			}
		}
		return ret;
	}

	/**
	 * Convert the given metadata into a BlockState for this Block
	 */
	public IBlockState getStateFromMeta(int meta) {
		return this.getDefaultState().withProperty(ACTIVE_STATE, Integer.valueOf(meta));
	}

	/**
	 * Convert the BlockState into the correct metadata value
	 */
	public int getMetaFromState(IBlockState state) {
		return ((Integer) state.getValue(ACTIVE_STATE)).intValue();
	}

	protected BlockState createBlockState() {
		return new BlockState(this, new IProperty[] { ACTIVE_STATE });
	}
}
