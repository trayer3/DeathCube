package com.projectreddog.deathcube.container;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;

import com.projectreddog.deathcube.reference.Reference;
import com.projectreddog.deathcube.tileentity.TileEntityLootBlock;
import com.projectreddog.deathcube.tileentity.TileEntityStartingGearConfig;

public class ContainerLootBlock extends Container {

	protected TileEntityLootBlock lootBlockTE;

	public ContainerLootBlock(final InventoryPlayer inventoryPlayer, TileEntityLootBlock lootBlockTE) {
		this.lootBlockTE = lootBlockTE;
		int y_offset = 0;
		
		/**
		 * Visible hot-bar is indexes 0-8.
		 *
		for (int j = 0; j < 9; j++) {
			addSlotToContainer(new Slot(startingGearTE, j * 9, 21 + 8 + j * 18, 23 + 3 * 18));
		}
		
		for (int i = 0; i < 3; i++) {
			for (int j = 0; j < 9; j++) {
				addSlotToContainer(new Slot(startingGearTE, 9 + j + i * 9, 21 + 8 + j * 18, 19 + i * 18));
			}
		}*/
		
		for (int i = 0; i < 4; i++) {
			for (int j = 0; j < 9; j++) {
				if(i == 0) {
					y_offset = 77;
				} else {
					y_offset = 1;
				}
				
				addSlotToContainer(new Slot(lootBlockTE, j + i * 9, 21 + 8 + j * 18, y_offset + i * 18));
			}
		}

		/**
		 * Commonly used vanilla code that adds the player's inventory
		 */
		bindPlayerInventory(inventoryPlayer);
	}

	@Override
	public boolean canInteractWith(EntityPlayer player) {
		return lootBlockTE.isUseableByPlayer(player);
	}

	protected void bindPlayerInventory(InventoryPlayer inventoryPlayer) {
		for (int i = 0; i < 3; i++) {
			for (int j = 0; j < 9; j++) {
				addSlotToContainer(new Slot(inventoryPlayer, j + i * 9 + 9, 21 + 8 + j * 18, 110 + i * 18));
			}
		}

		for (int i = 0; i < 9; i++) {
			addSlotToContainer(new Slot(inventoryPlayer, i, 21 + 8 + i * 18, 168));
		}
	}

	@Override
	public ItemStack transferStackInSlot(EntityPlayer player, int slot) {
		ItemStack stack = null;
		Slot slotObject = (Slot) inventorySlots.get(slot);

		/**
		 * Null Check:
		 * - Can the item be stacked (maxStackSize > 1)
		 */
		if (slotObject != null && slotObject.getHasStack()) {
			ItemStack stackInSlot = slotObject.getStack();
			stack = stackInSlot.copy();

			/**
			 * Merges the item into player inventory since it's in the Entity
			 */
			if (slot < Reference.LOOT_INVENTORY_SIZE) {
				if (!this.mergeItemStack(stackInSlot, Reference.LOOT_INVENTORY_SIZE, this.inventorySlots.size(), true)) {
					return null;
				}
			}
			
			/**
			 * Places the item into the tileEntity
			 * - Possible since its in the player inventory
			 */
			else if (!this.mergeItemStack(stackInSlot, 0, Reference.LOOT_INVENTORY_SIZE, false)) {
				return null;
			}

			if (stackInSlot.stackSize == 0) {
				slotObject.putStack(null);
			} else {
				slotObject.onSlotChanged();
			}

			if (stackInSlot.stackSize == stack.stackSize) {
				return null;
			}
			slotObject.onPickupFromSlot(player, stackInSlot);
		}
		return stack;
	}

	/**
	 * Looks for changes made in the container, sends them to every listener.
	 */
	public void detectAndSendChanges() {
		super.detectAndSendChanges();

		/**
		for (int i = 0; i < this.crafters.size(); ++i) {
			ICrafting icrafting = (ICrafting) this.crafters.get(i);

			if (this.lastFuelStorage != this.startingGearTE.getField(0)) {
				icrafting.sendProgressBarUpdate(this, 0, this.startingGearTE.getField(0));
			}
		}

		this.lastFuelStorage = this.startingGearTE.getField(0); 
*/
	}
}
