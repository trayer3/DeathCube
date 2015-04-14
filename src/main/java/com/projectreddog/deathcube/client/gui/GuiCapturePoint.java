package com.projectreddog.deathcube.client.gui;

import java.io.IOException;

import net.minecraft.client.gui.GuiTextField;

import com.projectreddog.deathcube.init.ModNetwork;
import com.projectreddog.deathcube.network.MessageHandleTextUpdate;
import com.projectreddog.deathcube.network.MessageRequestTextUpdate_Client;
import com.projectreddog.deathcube.reference.Reference;
import com.projectreddog.deathcube.tileentity.TileEntityCapturePoint;
import com.projectreddog.deathcube.utility.Log;

public class GuiCapturePoint extends GuiDeathCube {

	private TileEntityCapturePoint capture_point;
	private GuiTextField text_PointName;
	private GuiTextField text_PointTeam;
	private GuiTextField text_PointRadius;
	private GuiTextField text_PointCaptureOrderNumber;
	private GuiTextField text_PointCaptureTime;
	private int gui_Width = 225;
	private int gui_Height = 137;
	private int x = 0;
	private int y = 0;
	private int ySpacing = 20;
	private int xSpacingLabel = 20;
	private int xSpacingField = 90;

	public GuiCapturePoint(TileEntityCapturePoint capture_point) {
		super();
		this.capture_point = capture_point;
	}

	@Override
	public void initGui() {
		super.initGui();

		/**
		 * Need a better spot for this.  Or wait a short time?
		 */
		ModNetwork.simpleNetworkWrapper.sendToServer(new MessageRequestTextUpdate_Client(capture_point.getPos()));
		
		/**
		 * Find GUI upper-left corner.
		 */
		x = (this.width - this.gui_Width) / 2;
		y = (this.height - this.gui_Height) / 2;

		/**
		 * Prepare Text Field
		 */
		text_PointTeam = new GuiTextField(20, fontRendererObj, x + xSpacingField, y + ySpacing, 90, 12);
		text_PointTeam.setMaxStringLength(40);
		text_PointTeam.setText(capture_point.getCapturePointTeamColor());

		Log.info("Capture Point GUI Init - Team Color: " + capture_point.getCapturePointTeamColor());
		// True = Client
		Log.info("This is a client instance:" + String.valueOf(capture_point.getWorld().isRemote));

		text_PointName = new GuiTextField(20, fontRendererObj, x + xSpacingField, y + 2*ySpacing, 90, 12);
		text_PointName.setMaxStringLength(40);
		text_PointName.setText(capture_point.getCapturePointName());

		text_PointRadius = new GuiTextField(20, fontRendererObj, x + xSpacingField, y + 3*ySpacing, 90, 12);
		text_PointRadius.setMaxStringLength(40);
		text_PointRadius.setText(String.valueOf(capture_point.getCaptureRadius()));

		text_PointCaptureOrderNumber = new GuiTextField(20, fontRendererObj, x + xSpacingField, y + 4*ySpacing, 90, 12);
		text_PointCaptureOrderNumber.setMaxStringLength(40);
		text_PointCaptureOrderNumber.setText(String.valueOf(capture_point.getCaptureOrderNumber()));
		
		text_PointCaptureTime = new GuiTextField(20, fontRendererObj, x + xSpacingField, y + 5*ySpacing, 90, 12);
		text_PointCaptureTime.setMaxStringLength(40);
		text_PointCaptureTime.setText(String.valueOf(capture_point.getCaptureTime()));
	}

	@Override
	public void onTextfieldUpdate(int fieldID) {
		if (fieldID == Reference.MESSAGE_FIELD1_ID) {
			text_PointName.setText(capture_point.getCapturePointName());
		} else if (fieldID == Reference.MESSAGE_FIELD2_ID) {
			text_PointTeam.setText(capture_point.getCapturePointTeamColor());
		} else if (fieldID == Reference.MESSAGE_FIELD3_ID) {
			text_PointRadius.setText(String.valueOf(capture_point.getCaptureRadius()));
		} else if (fieldID == Reference.MESSAGE_FIELD4_ID) {
			text_PointCaptureOrderNumber.setText(String.valueOf(capture_point.getCaptureOrderNumber()));
		} else if (fieldID == Reference.MESSAGE_FIELD5_ID) {
			text_PointCaptureTime.setText(String.valueOf(capture_point.getCaptureTime()));
		}
	}

	/**
	 * Called when the mouse is clicked.
	 */
	@Override
	protected void mouseClicked(int mouseX, int mouseY, int button) throws IOException {
		super.mouseClicked(mouseX, mouseY, button);
		text_PointName.mouseClicked(mouseX, mouseY, button);
		text_PointTeam.mouseClicked(mouseX, mouseY, button);
		text_PointRadius.mouseClicked(mouseX, mouseY, button);
		text_PointCaptureOrderNumber.mouseClicked(mouseX, mouseY, button);
		text_PointCaptureTime.mouseClicked(mouseX, mouseY, button);
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
		mc.fontRendererObj.drawString("Team Color", x + xSpacingLabel, y + ySpacing + 2, 4210752);
		text_PointTeam.drawTextBox();

		mc.fontRendererObj.drawString("Point Name", x + xSpacingLabel, y + 2*ySpacing + 2, 4210752);
		text_PointName.drawTextBox();

		mc.fontRendererObj.drawString("Point Radius", x + xSpacingLabel, y + 3*ySpacing + 2, 4210752);
		text_PointRadius.drawTextBox();

		mc.fontRendererObj.drawString("Capture Order", x + xSpacingLabel, y + 4*ySpacing + 2, 4210752);
		text_PointCaptureOrderNumber.drawTextBox();
		
		mc.fontRendererObj.drawString("Capture Time", x + xSpacingLabel, y + 5*ySpacing + 2, 4210752);
		text_PointCaptureTime.drawTextBox();

		super.drawScreen(mouseX, mouseY, partTicks);
	}

	@Override
	public boolean doesGuiPauseGame() {
		return false;
	}

	@Override
	public void updateScreen() {
		super.updateScreen();
		//text_PointName.setText(capture_point.getCapturePointName());
		//text_PointTeam.setText(capture_point.getCapturePointTeamColor());
		//text_PointRadius.setText(String.valueOf(capture_point.getCaptureRadius()));
		//text_PointCaptureOrderNumber.setText(String.valueOf(capture_point.getCaptureOrderNumber()));
		//text_PointCaptureTime.setText(String.valueOf(capture_point.getCaptureTime()));
	}

	/**
	 * Fired when a key is typed. This is the equivalent of KeyListener.keyTyped(KeyEvent e).
	 * 
	 * @throws IOException
	 */
	@Override
	protected void keyTyped(char chr, int keyCode) throws IOException {
		Log.info("Key typed: " + chr);
		if (text_PointName.textboxKeyTyped(chr, keyCode)) {
			ModNetwork.simpleNetworkWrapper.sendToServer(new MessageHandleTextUpdate(capture_point.getPos(), Reference.MESSAGE_FIELD1_ID, text_PointName.getText()));
		} else if (text_PointTeam.textboxKeyTyped(chr, keyCode)) {
			ModNetwork.simpleNetworkWrapper.sendToServer(new MessageHandleTextUpdate(capture_point.getPos(), Reference.MESSAGE_FIELD2_ID, text_PointTeam.getText()));
		} else if (text_PointRadius.textboxKeyTyped(chr, keyCode)) {
			ModNetwork.simpleNetworkWrapper.sendToServer(new MessageHandleTextUpdate(capture_point.getPos(), Reference.MESSAGE_FIELD3_ID, text_PointRadius.getText()));
		} else if (text_PointCaptureOrderNumber.textboxKeyTyped(chr, keyCode)) {
			ModNetwork.simpleNetworkWrapper.sendToServer(new MessageHandleTextUpdate(capture_point.getPos(), Reference.MESSAGE_FIELD4_ID, text_PointCaptureOrderNumber.getText()));
		} else if (text_PointCaptureTime.textboxKeyTyped(chr, keyCode)) {
			ModNetwork.simpleNetworkWrapper.sendToServer(new MessageHandleTextUpdate(capture_point.getPos(), Reference.MESSAGE_FIELD5_ID, text_PointCaptureTime.getText()));
		} else {
			super.keyTyped(chr, keyCode);
		}
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
			ModNetwork.simpleNetworkWrapper.sendToServer(new MessageHandleTextUpdate(capture_point.getPos(), Reference.MESSAGE_FIELD2_ID, "Red"));
		}
	}
}
