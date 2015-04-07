package com.projectreddog.deathcube.client.gui;

import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.GuiTextField;

import com.projectreddog.deathcube.reference.Reference;
import com.projectreddog.deathcube.tileentity.TileEntitySpawnPoint;

public class GuiSpawnPoint extends GuiScreen {

	private TileEntitySpawnPoint spawn_point;
	private GuiTextField textField;
	private int gui_Width = 225;
	private int gui_Height = 137;
	private int x = 0;
	private int y = 0;

	public GuiSpawnPoint(TileEntitySpawnPoint spawn_point) {
		super();
		this.spawn_point = spawn_point;
	}

	@Override
	public void initGui() {
		super.initGui();
		// resetButton = new GuiButton(0, guiLeft + 10, guiTop + 37, 40, 20, "");
		// buttonList.add(resetButton);

		// Set up text fields
		x = (this.width - this.gui_Width) / 2;
		y = (this.height - this.gui_Height) / 2;

		textField = new GuiTextField(20, fontRendererObj, x + 60, y + 20, 70, 12);
		textField.setMaxStringLength(40);
		textField.setText("Red");
	}

	@Override
	public void drawScreen(int mouseX, int mouseY, float partTicks) {
		this.drawDefaultBackground();

		/**
		 * Get the centered x and y positions for the initial drawing location of the gui.
		 */
		x = (this.width - this.gui_Width) / 2;
		y = (this.height - this.gui_Height) / 2;

		/**
		 * Give gui image name, then draw the gui.
		 */
		this.mc.renderEngine.bindTexture(Reference.GUI_SPAWN_POINT_BACKGROUND);
		this.drawTexturedModalRect(x, y, 0, 0, this.gui_Width, this.gui_Height);

		// Gui Heading Text
		mc.fontRendererObj.drawString("Spawn Point Setup", x + 8, y + 8, 4210752);

		// Text Fields
		textField.drawTextBox();

		super.drawScreen(mouseX, mouseY, partTicks);
	}

	@Override
	public boolean doesGuiPauseGame() {
		return false;
	}

	@Override
	public void onGuiClosed() {
		super.onGuiClosed();

		/**
		 * Save data from gui to server.
		 */
	}
}
