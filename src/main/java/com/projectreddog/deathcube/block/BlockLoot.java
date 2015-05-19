package com.projectreddog.deathcube.block;

import java.util.List;
import java.util.Random;

import net.minecraft.block.BlockContainer;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyInteger;
import net.minecraft.block.state.BlockState;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.server.MinecraftServer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

import com.projectreddog.deathcube.DeathCube;
import com.projectreddog.deathcube.creativetab.CreativeTabDeathCube;
import com.projectreddog.deathcube.init.ModItems;
import com.projectreddog.deathcube.reference.Reference;
import com.projectreddog.deathcube.reference.Reference.GameStates;
import com.projectreddog.deathcube.tileentity.TileEntityCapturePoint;
import com.projectreddog.deathcube.tileentity.TileEntityLootBlock;
import com.projectreddog.deathcube.utility.Log;

public class BlockLoot extends BlockContainer {
	public static final PropertyInteger ACTIVE_STATE = PropertyInteger.create("active_state", 0, 1);
	private static boolean headBreak = false;

	public BlockLoot(Material material) {
		super(material);

		this.setCreativeTab(CreativeTabDeathCube.DEATHCUBE_TAB);
		this.setUnlocalizedName(Reference.MOD_ID.toLowerCase() + ":" + Reference.MODBLOCK_LOOT);
		this.setDefaultState(this.blockState.getBaseState().withProperty(ACTIVE_STATE, Integer.valueOf(0)));
		this.setBlockBounds(0.0F, 0.05F, 0.0F, 1.0F, 1.0F, 1.0F);
		this.setResistance(18000004.0f);
		this.setHardness(5f);// not sure on the hardness
		this.setStepSound(soundTypeMetal);
	}
	
	public BlockLoot() {
		this(Material.rock);
	}

	public static void toggleBlockState(World worldIn, BlockPos pos, IBlockState state) {
		int currentState = -1;
		
		if(state != null) {
			currentState = ((Integer) state.getValue(ACTIVE_STATE)).intValue();
		}

		if(currentState != -1) {
			Log.info("Loot Block - Player Activate state value: " + currentState);
			
			if (currentState == 0) {
				/**
				 * State 0 = Active Loot Block
				 * - Change to Inactive Block, State 1
				 */
				worldIn.setBlockState(pos, state.withProperty(ACTIVE_STATE, Integer.valueOf(1)), 2);
			} else if (currentState == 1) {
				/**
				 * State 1 = Inactive Block
				 * - Change to Active Loot Block, State 0
				 */
				worldIn.setBlockState(pos, state.withProperty(ACTIVE_STATE, Integer.valueOf(0)), 2);
			}
		}
	}
	
	public boolean onBlockActivated(World worldIn, BlockPos pos, IBlockState state, EntityPlayer playerIn, EnumFacing side, float hitX, float hitY, float hitZ)
    {   
        if (playerIn.capabilities.isCreativeMode) { // && DeathCube.gameState == GameStates.Lobby) {
			/**
			 * Can only open GUI if in Creative Mode and GameState is Lobby.
			 */
			TileEntity te = worldIn.getTileEntity(pos);
			if (te != null && !playerIn.isSneaking()) {
				playerIn.openGui(DeathCube.instance, Reference.GUI_LOOT_BLOCK, worldIn, pos.getX(), pos.getY(), pos.getZ());
				return true;
			} else {
				return false;
			}
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
		if(!player.capabilities.isCreativeMode && ((Integer) world.getBlockState(pos).getValue(ACTIVE_STATE)).intValue() == 0) {
			/**
	    	 * Set to Inactive Block
	    	 * - Do not destroy the block
	    	 * - 5 seconds * 20 ticks/second = 100 delay
	    	 */
			Log.info("Block Removed by Player - Setting Refresh Now");
			
			TileEntityLootBlock lookupTE = (TileEntityLootBlock) world.getTileEntity(pos);
			lookupTE.setRefreshTime(System.currentTimeMillis());
			
			Log.info("Loot Block TE: " + lookupTE.getPos().toString());
        	
			this.toggleBlockState(world, pos, world.getBlockState(pos));
			
			/**
			 * Drop item(s)
			 */
    		this.harvestBlock(world, player, pos, world.getBlockState(pos), null);
			
	    	return false;  //world.setBlockState(pos, world.getBlockState(pos).withProperty(ACTIVE_STATE, Integer.valueOf(1)), 2);
		} else {
			/**
			 * If player is in Creative Mode:
			 * - Allow block to be broken when not a head break
			 * - Head break toggles block state
			 */
			if(headBreak) {
				this.toggleBlockState(world, pos, world.getBlockState(pos));
				this.harvestBlock(world, player, pos, world.getBlockState(pos), null);
				headBreak = false;
				
				return false;
			} else {
				return world.setBlockToAir(pos);
			}
		}
    }

	/**
	 * Called When an Entity Collided with the Block
	 */
	public void onEntityCollidedWithBlock(World worldIn, BlockPos pos, IBlockState state, Entity entityIn) {
		if (entityIn instanceof EntityPlayer) {
			/**
			 * Toggle State and Schedule Update
			 */
			if(((Integer) state.getValue(ACTIVE_STATE)).intValue() == 0){
				headBreak = true;
				this.removedByPlayer(worldIn, pos, ((EntityPlayer) entityIn), true);
			}
			
			/**
			 * Drop item(s)
			 */
			//this.harvestBlock(worldIn, ((EntityPlayer) entityIn), pos, state, null);
		}
	}

	@Override
	public List<ItemStack> getDrops(IBlockAccess world, BlockPos pos, IBlockState state, int fortune) {
		List<ItemStack> ret = new java.util.ArrayList<ItemStack>();

		Random rand = world instanceof World ? ((World) world).rand : RANDOM;
		float randValue = rand.nextFloat();

		Log.info("Get Drops for Loot Block");
		
		int count = 1; //quantityDropped(state, fortune, rand);
		for (int i = 0; i < count; i++) {
			if (randValue < 0.33) {
				ret.add(new ItemStack(ModItems.deathskull));
			} else if (randValue > 0.66) {
				ret.add(new ItemStack(ModItems.lifeskull));
			} else {
				ret.add(new ItemStack(Items.arrow, 10));
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
	
	@Override
	public TileEntity createNewTileEntity(World worldIn, int meta) {
		return new TileEntityLootBlock();
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

	@Override
	public boolean isOpaqueCube() {
		return false;
	}
}
