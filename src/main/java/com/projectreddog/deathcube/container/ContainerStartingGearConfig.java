package com.projectreddog.deathcube.container;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.ICrafting;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import com.projectreddog.deathcube.tileentity.TileEntityStartingGearConfig;

public class ContainerStartingGearConfig extends Container {

	protected TileEntityStartingGearConfig startingGearTE;

	public ContainerStartingGearConfig(InventoryPlayer inventoryPlayer, TileEntityStartingGearConfig fermenter) {
		this.startingGearTE = fermenter;

		for (int i = 0; i < 3; i++) {
			for (int j = 0; j < 9; j++) {
				addSlotToContainer(new Slot(fermenter, j + i * 9, 8 + j * 18, 18 + i * 18));
			}
		}
		
		for (int i = 0; i < 9; i++) {
			addSlotToContainer(new Slot(inventoryPlayer, i, 8 + i * 58, 197));
		}

		/**
		 * Commonly used vanilla code that adds the player's inventory
		 */
		bindPlayerInventory(inventoryPlayer);
	}

	@Override
	public boolean canInteractWith(EntityPlayer player) {
		return startingGearTE.isUseableByPlayer(player);
	}

	protected void bindPlayerInventory(InventoryPlayer inventoryPlayer) {
		for (int i = 0; i < 3; i++) {
			for (int j = 0; j < 9; j++) {
				addSlotToContainer(new Slot(inventoryPlayer, j + i * 9 + 9, 8 + j * 18, 139 + i * 18));
			}
		}

		for (int i = 0; i < 9; i++) {
			addSlotToContainer(new Slot(inventoryPlayer, i, 8 + i * 18, 197));
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
			if (slot < 9) {
				if (!this.mergeItemStack(stackInSlot, 9, this.inventorySlots.size(), true)) {
					return null;
				}
			}
			
			/**
			 * Places the item into the tileEntity
			 * - Possible since its in the player inventory
			 */
			else if (!this.mergeItemStack(stackInSlot, 0, 9, false)) {
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
	 *
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

	}

	@SideOnly(Side.CLIENT)
	public void updateProgressBar(int id, int data) {
		this.startingGearTE.setField(id, data);
	}*/
}
