package com.projectreddog.deathcube.client.gui;

import net.minecraft.client.gui.GuiTextField;

import com.projectreddog.deathcube.init.ModNetwork;
import com.projectreddog.deathcube.network.DeathCubeMessageInputToServer;
import com.projectreddog.deathcube.network.MessageHandleTextUpdate;
import com.projectreddog.deathcube.network.NetworkHandler;
import com.projectreddog.deathcube.reference.Reference;
import com.projectreddog.deathcube.tileentity.TileEntityCapturePoint;
import com.projectreddog.deathcube.utility.Log;

public class GuiCapturePoint extends GuiDeathCube {

	private TileEntityCapturePoint capture_point;
	private GuiTextField text_PointName;
	private GuiTextField text_PointTeam;
	private GuiTextField text_PointRadius;
	private GuiTextField text_PointCaptureOrderNumber;
	private int gui_Width = 225;
	private int gui_Height = 137;
	private int x = 0;
	private int y = 0;

	public GuiCapturePoint(TileEntityCapturePoint capture_point) {
		super();
		this.capture_point = capture_point;
	}

	@Override
	public void initGui() {
		super.initGui();

		/**
		 * Find GUI upper-left corner.
		 */
		x = (this.width - this.gui_Width) / 2;
		y = (this.height - this.gui_Height) / 2;

		/**
		 * Prepare Text Field
		 */
		text_PointName = new GuiTextField(20, fontRendererObj, x + 70, y + 20, 70, 12);
		text_PointName.setMaxStringLength(40);
		text_PointName.setText("Point Name");

		text_PointTeam = new GuiTextField(20, fontRendererObj, x + 70, y + 45, 70, 12);
		text_PointTeam.setMaxStringLength(40);
		text_PointTeam.setText("Point Team");

		text_PointRadius = new GuiTextField(20, fontRendererObj, x + 70, y + 70, 70, 12);
		text_PointRadius.setMaxStringLength(40);
		text_PointRadius.setText("Point Radius");

		text_PointCaptureOrderNumber = new GuiTextField(20, fontRendererObj, x + 95, y + 20, 70, 12);
		text_PointCaptureOrderNumber.setMaxStringLength(40);
		text_PointCaptureOrderNumber.setText("Point Number");
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
		this.mc.renderEngine.bindTexture(Reference.GUI_GAME_CONTROLLER_BACKGROUND);
		this.drawTexturedModalRect(x, y, 0, 0, this.gui_Width, this.gui_Height);

		// Gui Heading Text
		mc.fontRendererObj.drawString("Capture Point", x + 8, y + 8, 4210752);

		// Text Fields
		mc.fontRendererObj.drawString("Teams", x + 20, y + 22, 4210752);
		text_PointName.drawTextBox();

		super.drawScreen(mouseX, mouseY, partTicks);
	}

	@Override
	public boolean doesGuiPauseGame() {
		return false;
	}

	@Override
	public void updateScreen() {
		super.updateScreen();
		// button.displayString = "Test Button";
	}

	/**
	 * Fired when a key is typed. This is the equivalent of KeyListener.keyTyped(KeyEvent e).
	 */
	@Override
	protected void keyTyped(char chr, int keyCode) {
		if (text_PointName.textboxKeyTyped(chr, keyCode)) {
			NetworkHandler.sendToServer(new MessageHandleTextUpdate(capture_point, Reference.MESSAGE_FIELD1_ID, text_PointName.getText()));
		} else if (text_PointTeam.textboxKeyTyped(chr, keyCode)) {
			NetworkHandler.sendToServer(new MessageHandleTextUpdate(capture_point, Reference.MESSAGE_FIELD1_ID, text_PointTeam.getText()));
		} else if (text_PointRadius.textboxKeyTyped(chr, keyCode)) {
			NetworkHandler.sendToServer(new MessageHandleTextUpdate(capture_point, Reference.MESSAGE_FIELD1_ID, text_PointRadius.getText()));
		} else if (text_PointCaptureOrderNumber.textboxKeyTyped(chr, keyCode)) {
			NetworkHandler.sendToServer(new MessageHandleTextUpdate(capture_point, Reference.MESSAGE_FIELD1_ID, text_PointCaptureOrderNumber.getText()));
		} else {
			super.keyTyped(chr, keyCode);
		}
	}

	@Override
	public void onGuiClosed() {
		super.onGuiClosed();

		/**
		 * Save data from Gui to server.
		 */
		Log.info("Capture Point Gui - Exited");
		ModNetwork.simpleNetworkWrapper.sendToServer(new DeathCubeMessageInputToServer(Reference.MESSAGE_SOURCE_GUI, Reference.GUI_GAME_CONTROLLER, text_PointName.getText(), text_PointTeam.getText(), text_PointRadius.getText(), text_PointCaptureOrderNumber.getText()));
	}
}
