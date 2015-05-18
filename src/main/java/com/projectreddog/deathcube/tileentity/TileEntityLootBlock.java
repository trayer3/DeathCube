package com.projectreddog.deathcube.tileentity;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.server.gui.IUpdatePlayerListBox;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.IChatComponent;

import com.projectreddog.deathcube.block.BlockLoot;
import com.projectreddog.deathcube.reference.Reference;

public class TileEntityLootBlock extends TileEntity implements IUpdatePlayerListBox, IInventory {
	
	protected ItemStack[] inventory;
	private long refreshTime = 0;
	private boolean needsRefreshed = false;

	public TileEntityLootBlock() {
		inventory = new ItemStack[Reference.LOOT_INVENTORY_SIZE];
	}

	@Override
	public void update() {
		if (!worldObj.isRemote) {
			// LogHelper.info("TE update entity called");
			
			if(needsRefreshed && System.currentTimeMillis() >= refreshTime) {
				BlockLoot.toggleBlockState(this.worldObj, this.pos, this.worldObj.getBlockState(pos));
				
				needsRefreshed = false;
			}
		}
	}
	
	public void setInventory(ItemStack[] inInventory) {
		/**
		 * Shouldn't need to use this.
		 */
	}
	
	public ItemStack[] getInventory() {
		return inventory.clone();
	}

	protected ItemStack addToinventory(ItemStack is) {
		int i = getSizeInventory();
		for (int j = 0; j < i && is != null && is.stackSize > 0; ++j) {
			if (is != null) {

				if (getStackInSlot(j) != null) {
					if (getStackInSlot(j).getItem() == is.getItem() && getStackInSlot(j).getItemDamage() == is.getItemDamage()) {
						/**
						 * This is the same item.
						 * - Remove some of the item from the item stack
						 * - Do not exceed the max stack amount
						 */
						if (getStackInSlot(j).stackSize < getStackInSlot(j).getMaxStackSize()) {
							/**
							 * There is room to add to this stack here.
							 */
							if (is.stackSize <= getStackInSlot(j).getMaxStackSize() - getStackInSlot(j).stackSize) {
								/**
								 * Entire stack can fit here.  So, put stack here.
								 */
								setInventorySlotContents(j, new ItemStack(getStackInSlot(j).getItem(), getStackInSlot(j).stackSize + is.stackSize, is.getItemDamage()));
								is = null;
							} else {
								/**
								 * Stack is too big for this slot.  Keep track of remaining amount.
								 */
								int countRemain = is.stackSize - (getStackInSlot(j).getMaxStackSize() - getStackInSlot(j).stackSize);
								setInventorySlotContents(j, new ItemStack(is.getItem(), getStackInSlot(j).getMaxStackSize(), is.getItemDamage()));
								is.stackSize = countRemain;
							}
						}
					}
				} else {
					/**
					 * Nothing in slot, so set contents
					 */
					setInventorySlotContents(j, new ItemStack(is.getItem(), is.stackSize, is.getItemDamage()));
					is = null;
				}
			}
		}

		return is;
	}

	@Override
	public void readFromNBT(NBTTagCompound compound) {

		super.readFromNBT(compound);

		NBTTagList tagList = compound.getTagList("Inventory", compound.getId());
		for (int i = 0; i < tagList.tagCount(); i++) {
			NBTTagCompound tag = (NBTTagCompound) tagList.getCompoundTagAt(i);
			byte slot = tag.getByte("Slot");
			if (slot >= 0 && slot < inventory.length) {
				inventory[slot] = ItemStack.loadItemStackFromNBT(tag);
			}
		}
	}

	@Override
	public void writeToNBT(NBTTagCompound compound) {
		super.writeToNBT(compound);

		NBTTagList itemList = new NBTTagList();
		for (int i = 0; i < inventory.length; i++) {
			ItemStack stack = inventory[i];
			if (stack != null) {
				NBTTagCompound tag = new NBTTagCompound();
				tag.setByte("Slot", (byte) i);
				stack.writeToNBT(tag);
				itemList.appendTag(tag);
			}
		}
		compound.setTag("Inventory", itemList);
	}

	@Override
	public String getName() {
		return null;
	}

	@Override
	public boolean hasCustomName() {
		return false;
	}

	@Override
	public IChatComponent getDisplayName() {
		return null;
	}

	@Override
	public int getSizeInventory() {
		return inventory.length;
	}

	@Override
	public ItemStack getStackInSlot(int slot) {
		return inventory[slot];
	}

	@Override
	public ItemStack decrStackSize(int slot, int amt) {
		ItemStack stack = getStackInSlot(slot);
		if (stack != null) {
			if (stack.stackSize <= amt) {
				setInventorySlotContents(slot, null);
			} else {
				stack = stack.splitStack(amt);
				if (stack.stackSize == 0) {
					setInventorySlotContents(slot, null);
				}
			}
		}
		return stack;
	}

	@Override
	public ItemStack getStackInSlotOnClosing(int slot) {
		ItemStack stack = getStackInSlot(slot);
		if (stack != null) {
			setInventorySlotContents(slot, null);
		}
		return stack;
	}

	@Override
	public void setInventorySlotContents(int slot, ItemStack stack) {
		inventory[slot] = stack;
		if (stack != null && stack.stackSize > getInventoryStackLimit()) {
			stack.stackSize = getInventoryStackLimit();
		}
	}

	@Override
	public int getInventoryStackLimit() {
		return 64;
	}

	@Override
	public boolean isUseableByPlayer(EntityPlayer playerIn) {
		return playerIn.getDistanceSq(this.getPos().getX(), this.getPos().getY(), this.getPos().getZ()) < 64;
	}

	@Override
	public void openInventory(EntityPlayer playerIn) {

	}

	@Override
	public void closeInventory(EntityPlayer playerIn) {

	}

	@Override
	public boolean isItemValidForSlot(int index, ItemStack stack) {
		return true;
	}

	@Override
	public int getField(int id) {
		/**
		 * Do nothing
		 * - No GUI updates to send to client or receive from server
		 */
		return 0;
	}

	@Override
	public void setField(int id, int value) {
		/**
		 * Do nothing
		 * - No GUI updates to send to client or receive from server
		 */
	}

	@Override
	public int getFieldCount() {
		/**
		 * Return 0 
		 * - No gui updates to send server ->client
		 */
		return 0;
	}

	@Override
	public void clear() {
		for (int i = 0; i < inventory.length; ++i) {
			inventory[i] = null;
		}
	}

	public long getRefreshTime() {
		return refreshTime;
	}

	public void setRefreshTime(long refreshTime) {
		needsRefreshed = true;
		this.refreshTime = refreshTime + ((long) Reference.LOOT_REFRESH_TIME * 1000);
	}
}
