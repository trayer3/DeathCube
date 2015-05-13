package com.projectreddog.deathcube.block;

import java.util.List;
import java.util.Random;

import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyInteger;
import net.minecraft.block.state.BlockState;
import net.minecraft.block.state.IBlockState;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.stats.StatList;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

import com.projectreddog.deathcube.init.ModItems;
import com.projectreddog.deathcube.reference.Reference;
import com.projectreddog.deathcube.utility.Log;

public class BlockLoot extends BlockDeathCube {
	public static final PropertyInteger ACTIVE_STATE = PropertyInteger.create("active_state", 0, 1);

	public BlockLoot() {
		super();

		this.setUnlocalizedName(Reference.MODBLOCK_LOOT);
		this.setDefaultState(this.blockState.getBaseState().withProperty(ACTIVE_STATE, Integer.valueOf(0)));
		this.setBlockBounds(0.0F, 0.05F, 0.0F, 1.0F, 1.0F, 1.0F);
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

	public boolean onBlockActivated(World worldIn, BlockPos pos, IBlockState state, EntityPlayer playerIn, EnumFacing side, float hitX, float hitY, float hitZ)
    {
        if(playerIn.capabilities.isCreativeMode) {
        	int i = ((Integer) state.getValue(ACTIVE_STATE)).intValue();
    		int j = ((Integer) worldIn.getBlockState(pos).getValue(ACTIVE_STATE)).intValue();

    		Log.info("Loot Block - Player Activate state value: " + i);
    		Log.info("Loot Block - Player Activate world-state value: " + j);

    		if (i == 1) {
    			/**
    			 * Change from Inactive Block to Loot Block
    			 */
    			worldIn.setBlockState(pos, state.withProperty(ACTIVE_STATE, Integer.valueOf(0)), 2); // What is the Comparable value = 2 for?
    		} else if (i == 0) {
    			/**
    			 * Change from Loot Block to Inactive Block
    			 */
    			worldIn.setBlockState(pos, state.withProperty(ACTIVE_STATE, Integer.valueOf(1)), 2); // What is the Comparable value = 2 for?
    			
    			/**
    			 * Drop item(s)
    			 */
    			this.harvestBlock(worldIn, playerIn, pos, state, null);
    		}
            
            return false;
        } else {
            return false;
        }
    }
	
	/**
     * Called when a player removes a block.  This is responsible for
     * actually destroying the block, and the block is intact at time of call.
     * This is called regardless of whether the player can harvest the block or
     * not.
     *
     * Return true if the block is actually destroyed.
     *
     * Note: When used in multiplayer, this is called on both client and
     * server sides!
     *
     * @param world The current world
     * @param player The player damaging the block, may be null
     * @param pos Block position in world
     * @param willHarvest True if Block.harvestBlock will be called after this, if the return in true.
     *        Can be useful to delay the destruction of tile entities till after harvestBlock
     * @return True if the block is actually destroyed.
     */
	@Override
    public boolean removedByPlayer(World world, BlockPos pos, EntityPlayer player, boolean willHarvest)
    {
    	/**
    	 * Set to Inactive Block
    	 * - How to get a proper state object?
    	 * - Still crashes - can't get state of Loot Block since already changed to Air block.
    	 */
    	//world.setBlockState(pos, this.getStateById(0).withProperty(ACTIVE_STATE, Integer.valueOf(1)), 2);
        
    	/**
    	 * Do not destroy the block
    	 */
    	return false;
    }
	
	// Try this maybe?  harvestBlock(World worldIn, EntityPlayer player, BlockPos pos, IBlockState state, TileEntity te)

	/**
	 * Called When an Entity Collided with the Block
	 */
	public void onEntityCollidedWithBlock(World worldIn, BlockPos pos, IBlockState state, Entity entityIn) {
		if (entityIn instanceof EntityPlayer) {
			worldIn.setBlockState(pos, state.withProperty(ACTIVE_STATE, Integer.valueOf(1)), 2);
			
			/**
			 * Drop item:
			 * - Drops all possible items currently.  Why?  Same call as in onBlockActivated.
			 * - Maybe it's the cast of Entity to EntityPlayer?  Would only affect harvesters and fortune level.
			 */
			this.harvestBlock(worldIn, ((EntityPlayer) entityIn), pos, state, null);
		}
	}

	public void updateTick(World worldIn, BlockPos pos, IBlockState state, Random rand) {
		/**
		 * Check time since block went inactive.
		 * - Set back to active if time > limit.
		 */
	}

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
