package com.projectreddog.deathcube.client.gui;

import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.entity.player.InventoryPlayer;

import org.lwjgl.opengl.GL11;

import com.projectreddog.deathcube.container.ContainerLootBlock;
import com.projectreddog.deathcube.reference.Reference;
import com.projectreddog.deathcube.tileentity.TileEntityLootBlock;

public class GuiLootBlock extends GuiContainer {

	private TileEntityLootBlock lootBlockTE;
	private int x = 0, y = 0;
	private int gui_Width = 176;
	private int gui_Height = 188;
	
	private GuiButton button_ToggleClass;
	private int buttonWidth;
	
	public GuiLootBlock(InventoryPlayer inventoryPlayer, TileEntityLootBlock lootBlockTE) {
		super(new ContainerLootBlock(inventoryPlayer, lootBlockTE));
		this.lootBlockTE = lootBlockTE;
	}

	@Override
	public void initGui() {
		/**
		 * Find GUI upper-left corner.
		 */
		x = (this.width - this.gui_Width) / 2;
		y = (this.height - this.gui_Height) / 2;

		/**
		 * Prepare Button
		 * - Page 1 Buttons
		 */
		buttonWidth = 40;
		button_ToggleClass = new GuiButton(Reference.BUTTON_1, x + gui_Width + 3, y + 5, buttonWidth, 20, "Button");
		buttonList.add(button_ToggleClass);
		
		this.xSize = gui_Width;
		this.ySize = gui_Height;
		super.initGui();
	}

	@Override
	protected void drawGuiContainerForegroundLayer(int param1, int param2) {
		fontRendererObj.drawString("Starting Inventory: ", 21 + 8, 8, 4210752);
		fontRendererObj.drawString("Inventory", 21 + 8, 99, 4210752);
	}

	@Override
	protected void drawGuiContainerBackgroundLayer(float par1, int par2, int par3) {
		GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
		
		x = (this.width - this.gui_Width) / 2;
		y = (this.height - this.gui_Height) / 2;
		
		this.mc.renderEngine.bindTexture(Reference.GUI_LOOT_BLOCK_BACKGROUND);
		this.drawTexturedModalRect(x, y, 0, 0, this.gui_Width, this.gui_Height);
	}
	
	@Override
	public boolean doesGuiPauseGame() {
		return false;
	}
	
	@Override
	public void onGuiClosed() {
		super.onGuiClosed();
	}
}
