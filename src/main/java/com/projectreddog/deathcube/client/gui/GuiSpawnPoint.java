package com.projectreddog.deathcube.client.gui;

import java.io.IOException;

import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.GuiTextField;

import com.projectreddog.deathcube.init.ModNetwork;
import com.projectreddog.deathcube.network.MessageHandleTextUpdate;
import com.projectreddog.deathcube.network.MessageRequestTextUpdate_Client;
import com.projectreddog.deathcube.reference.Reference;
import com.projectreddog.deathcube.tileentity.TileEntitySpawnPoint;
import com.projectreddog.deathcube.utility.Log;

public class GuiSpawnPoint extends GuiDeathCube {

	private TileEntitySpawnPoint spawn_point;
	private GuiTextField text_PointTeam;
	private int gui_Width = 225;
	private int gui_Height = 137;
	private int x = 0;
	private int y = 0;
	private int ySpacing = 20;
	private int xSpacingLabel = 20;
	private int xSpacingField = 90;

	public GuiSpawnPoint(TileEntitySpawnPoint spawn_point) {
		super();
		this.spawn_point = spawn_point;
	}

	@Override
	public void initGui() {
		super.initGui();
		
		/**
		 * Need a better spot for this.  Or wait a short time?
		 */
		ModNetwork.simpleNetworkWrapper.sendToServer(new MessageRequestTextUpdate_Client(spawn_point.getPos()));

		// Set up text fields
		x = (this.width - this.gui_Width) / 2;
		y = (this.height - this.gui_Height) / 2;

		text_PointTeam = new GuiTextField(20, fontRendererObj, x + xSpacingField, y + ySpacing, 90, 12);
		text_PointTeam.setMaxStringLength(40);
		text_PointTeam.setText(spawn_point.getSpawnPointTeamColor());
	}
	
	@Override
	public void onTextfieldUpdate(int fieldID) {
		if (fieldID == Reference.MESSAGE_FIELD1_ID) {
			text_PointTeam.setText(spawn_point.getSpawnPointTeamColor());
		}
	}
	
	/**
	 * Called when the mouse is clicked.
	 */
	@Override
	protected void mouseClicked(int mouseX, int mouseY, int button) throws IOException {
		super.mouseClicked(mouseX, mouseY, button);
		text_PointTeam.mouseClicked(mouseX, mouseY, button);
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
		mc.fontRendererObj.drawString("Team Color", x + xSpacingLabel, y + ySpacing + 2, 4210752);
		text_PointTeam.drawTextBox();

		super.drawScreen(mouseX, mouseY, partTicks);
	}
	
	/**
	 * Fired when a key is typed. This is the equivalent of KeyListener.keyTyped(KeyEvent e).
	 */
	@Override
	protected void keyTyped(char chr, int keyCode) throws IOException {
		Log.info("Key typed: " + chr);
		if (text_PointTeam.textboxKeyTyped(chr, keyCode)) {
			ModNetwork.simpleNetworkWrapper.sendToServer(new MessageHandleTextUpdate(spawn_point.getPos(), Reference.MESSAGE_FIELD1_ID, text_PointTeam.getText()));
		} else {
			super.keyTyped(chr, keyCode);
		}
	}

	@Override
	public boolean doesGuiPauseGame() {
		return false;
	}

	@Override
	public void onGuiClosed() {
		super.onGuiClosed();
		
		boolean isValidColor = false;
		boolean foundValidColor = false;
		/**
		 * Verify that the Team Color is valid.
		 */
		if(text_PointTeam.getText().toLowerCase().equals(Reference.TEAM_RED)) {
			isValidColor = true;
			foundValidColor = true;
		} else if(foundValidColor || text_PointTeam.getText().toLowerCase().equals(Reference.TEAM_BLUE)) {
			isValidColor = true;
			foundValidColor = true;
		} else if(foundValidColor || text_PointTeam.getText().toLowerCase().equals(Reference.TEAM_GREEN)) {
			isValidColor = true;
			foundValidColor = true;
		} else if(foundValidColor || text_PointTeam.getText().toLowerCase().equals(Reference.TEAM_YELLOW)) {
			isValidColor = true;
			foundValidColor = true;
		}
		
		if(!isValidColor) {
			ModNetwork.simpleNetworkWrapper.sendToServer(new MessageHandleTextUpdate(spawn_point.getPos(), Reference.MESSAGE_FIELD1_ID, "Red"));
		}
	}
}
