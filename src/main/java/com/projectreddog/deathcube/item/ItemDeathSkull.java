package com.projectreddog.deathcube.item;

import java.util.Random;

import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.World;

import com.projectreddog.deathcube.reference.Reference;

public class ItemDeathSkull extends ItemDeathCube {

	public ItemDeathSkull() {
		super();
		this.setUnlocalizedName("deathskull");
		this.maxStackSize = 1;
		this.setMaxDamage(Reference.ITEM_DEATHSKULL_DURABILITY);
	}
	
	/**
     * Called when the player Left Clicks (attacks) an entity.
     * Processed before damage is done, if return value is true further processing is canceled
     * and the entity is not attacked.
     *
     * @param stack The Item being used
     * @param player The player that is attacking
     * @param entity The entity being attacked
     * @return True to cancel the rest of the interaction.
     */
	@Override
	public boolean onLeftClickEntity(ItemStack stack, EntityPlayer player, Entity entity)
    {
		if(!player.worldObj.isRemote) {
			Random rand = new Random();
			float randAmountx = rand.nextFloat()/2;
			float randAmountz = rand.nextFloat()/2;
			
			if(entity instanceof EntityPlayer) {
				stack.damageItem(1, player);
				((EntityPlayer) entity).addVelocity(randAmountx, (Reference.ITEM_DEATHSKULL_VELOCITY_AMOUNT * 2), randAmountz);
			} else {
				stack.damageItem(1, player);
				entity.addVelocity(randAmountx, (Reference.ITEM_DEATHSKULL_VELOCITY_AMOUNT * 2), randAmountz);
			}
		}
		
        return true;  // Or maybe it's just because I'm returning true.  Lifeskull is false.
    }
	
	/**
     * Called whenever this item is equipped and the right mouse button is pressed. Args: itemStack, world, entityPlayer
     */
	@Override
    public ItemStack onItemRightClick(ItemStack itemStackIn, World worldIn, EntityPlayer playerIn)
    {
		Random rand = new Random();
		float randAmountx = rand.nextFloat()/5;
		float randAmountz = rand.nextFloat()/5;
		
		if(playerIn.onGround) {
			itemStackIn.damageItem(1, playerIn);
			playerIn.addVelocity(randAmountx, Reference.ITEM_DEATHSKULL_VELOCITY_AMOUNT, randAmountz);
		}
		
        return itemStackIn;
    }
    
    /**
     * This is called when the item is used, before the block is activated.
     * @param stack The Item Stack
     * @param player The Player that used the item
     * @param world The Current World
     * @param pos Target position
     * @param side The side of the target hit
     * @return Return true to prevent any further processing.
     */
	@Override
    public boolean onItemUseFirst(ItemStack stack, EntityPlayer player, World world, BlockPos pos, EnumFacing side, float hitX, float hitY, float hitZ)
    {
        return false;
    }

    /**
     * Called each tick while using an item.
     * @param stack The Item being used
     * @param player The Player using the item
     * @param count The amount of time in tick the item has been used for continuously
     */
	@Override
    public void onUsingTick(ItemStack stack, EntityPlayer player, int count)
    {
    }
    
    /**
     * Called when the player finishes using this Item (E.g. finishes eating.). Not called when the player stops using
     * the Item before the action is complete.
     */
	@Override
    public ItemStack onItemUseFinish(ItemStack stack, World worldIn, EntityPlayer playerIn)
    {
        return stack;
    }
	
	/**
     * Called when the player stops using an Item (stops holding the right mouse button).
     *  
     * @param timeLeft The amount of ticks left before the using would have been complete
     */
	@Override
    public void onPlayerStoppedUsing(ItemStack stack, World worldIn, EntityPlayer playerIn, int timeLeft) {
		
	}
}
