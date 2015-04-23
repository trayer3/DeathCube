package com.projectreddog.deathcube.client.gui;

import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.entity.player.InventoryPlayer;

import org.lwjgl.opengl.GL11;

import com.projectreddog.deathcube.container.ContainerStartingGearConfig;
import com.projectreddog.deathcube.reference.Reference;
import com.projectreddog.deathcube.tileentity.TileEntityStartingGearConfig;

public class GuiStartingGearConfig extends GuiContainer {

	private TileEntityStartingGearConfig startingGearTE;
	private int x = 0, y = 0;
	private int gui_Width = 176;
	private int gui_Height = 222;
	
	public GuiStartingGearConfig(InventoryPlayer inventoryPlayer, TileEntityStartingGearConfig startingGearTE) {
		super(new ContainerStartingGearConfig(inventoryPlayer, startingGearTE));
		this.startingGearTE = startingGearTE;
	}

	@Override
	public void initGui() {
		super.initGui();
	}

	@Override
	protected void drawGuiContainerForegroundLayer(int param1, int param2) {
		fontRendererObj.drawString("Starting Inventory Setup:", 5, 5, 4210752);
	}

	@Override
	protected void drawGuiContainerBackgroundLayer(float par1, int par2, int par3) {
		GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
		
		x = (this.width - this.gui_Width) / 2;
		y = (this.height - this.gui_Height) / 2;
		
		this.mc.renderEngine.bindTexture(Reference.GUI_STARTING_GEAR_CONFIG_BACKGROUND);
		this.drawTexturedModalRect(x, y, 0, 0, xSize, ySize);
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
