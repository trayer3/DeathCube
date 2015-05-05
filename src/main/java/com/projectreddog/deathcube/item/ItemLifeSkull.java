package com.projectreddog.deathcube.item;

import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.World;

import com.projectreddog.deathcube.reference.Reference;

public class ItemLifeSkull extends ItemDeathCube {
	
	public ItemLifeSkull() {
		super();
		this.setUnlocalizedName("lifeskull");
		this.maxStackSize = 1;
		this.setMaxDamage(Reference.ITEM_LIFESKULL_DURABILITY);
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
		float entityHealth;
		
		if(entity instanceof EntityPlayer) {
			entityHealth = ((EntityPlayer) entity).getHealth();
			
			if(entityHealth >= ((EntityPlayer) entity).getMaxHealth()) {
				/**
				 * Add some other effect?
				 */
			} else {
				/**
				 * Heal the player for 1 heart (or half a heart?).
				 */
				float newPlayerHealth;
				
				if(((EntityPlayer) entity).getMaxHealth() - entityHealth <= (Reference.ITEM_LIFESKULL_HEAL_AMOUNT * 2)) {
					newPlayerHealth = ((EntityPlayer) entity).getMaxHealth();
				} else {
					newPlayerHealth = entityHealth + (Reference.ITEM_LIFESKULL_HEAL_AMOUNT * 2);
				}
				
				stack.damageItem(1, player);
				((EntityPlayer) entity).setHealth(newPlayerHealth);
			}
		}
		
        return false;
    }
	
	/**
     * Called whenever this item is equipped and the right mouse button is pressed. Args: itemStack, world, entityPlayer
     */
	@Override
    public ItemStack onItemRightClick(ItemStack itemStackIn, World worldIn, EntityPlayer playerIn)
    {
		float playerHealth = playerIn.getHealth();
		
		if(playerHealth >= playerIn.getMaxHealth()) {
			/**
			 * Add some other effect?
			 */
		} else {
			/**
			 * Heal the player for 1 heart.
			 */
			float newPlayerHealth;
			
			if(playerIn.getMaxHealth() - playerHealth <= Reference.ITEM_LIFESKULL_HEAL_AMOUNT) {
				newPlayerHealth = playerIn.getMaxHealth();
			} else {
				newPlayerHealth = playerHealth + Reference.ITEM_LIFESKULL_HEAL_AMOUNT;
			}
			
			itemStackIn.damageItem(1, playerIn);
			playerIn.setHealth(newPlayerHealth);
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
