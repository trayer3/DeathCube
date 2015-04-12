package com.projectreddog.deathcube.client.gui;

import java.io.IOException;

import net.minecraft.client.gui.GuiTextField;

import com.projectreddog.deathcube.init.ModNetwork;
import com.projectreddog.deathcube.network.MessageHandleTextUpdate;
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
		text_PointTeam = new GuiTextField(20, fontRendererObj, x + 90, y + 20, 90, 12);
		text_PointTeam.setMaxStringLength(40);
		text_PointTeam.setText(capture_point.getCapturePointTeamColor());
		
		Log.info("Opened Capture Point GUI - Team Color: " + capture_point.getCapturePointTeamColor());
		// True = Client
		Log.info("This is a client instance:" + String.valueOf(capture_point.getWorld().isRemote));
		
		text_PointName = new GuiTextField(20, fontRendererObj, x + 90, y + 40, 90, 12);
		text_PointName.setMaxStringLength(40);
		text_PointName.setText(capture_point.getCapturePointName());

		text_PointRadius = new GuiTextField(20, fontRendererObj, x + 90, y + 60, 90, 12);
		text_PointRadius.setMaxStringLength(40);
		text_PointRadius.setText(String.valueOf(capture_point.getCaptureRadius()));

		text_PointCaptureOrderNumber = new GuiTextField(20, fontRendererObj, x + 90, y + 80, 90, 12);
		text_PointCaptureOrderNumber.setMaxStringLength(40);
		text_PointCaptureOrderNumber.setText(String.valueOf(capture_point.getCaptureOrderNumber()));
	}

	@Override
    public void onTextfieldUpdate(int fieldID){
        if(fieldID == Reference.MESSAGE_FIELD1_ID) {
        	text_PointName.setText(capture_point.getCapturePointName());
        } else if(fieldID == Reference.MESSAGE_FIELD2_ID) {
        	text_PointTeam.setText(capture_point.getCapturePointTeamColor());
        } else if(fieldID == Reference.MESSAGE_FIELD3_ID) {
        	text_PointRadius.setText(String.valueOf(capture_point.getCaptureRadius()));
        } else if(fieldID == Reference.MESSAGE_FIELD4_ID) {
        	text_PointCaptureOrderNumber.setText(String.valueOf(capture_point.getCaptureOrderNumber()));
        }
    }
	
	/**
     * Called when the mouse is clicked.
	 * @throws IOException 
     */
    @Override
    protected void mouseClicked(int mouseX, int mouseY, int button) throws IOException{
        super.mouseClicked(mouseX, mouseY, button);
        text_PointName.mouseClicked(mouseX, mouseY, button);
        text_PointTeam.mouseClicked(mouseX, mouseY, button);
        text_PointRadius.mouseClicked(mouseX, mouseY, button);
        text_PointCaptureOrderNumber.mouseClicked(mouseX, mouseY, button);
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
		mc.fontRendererObj.drawString("Team Color", x + 20, y + 22, 4210752);
		text_PointTeam.drawTextBox();
		
		mc.fontRendererObj.drawString("Point Name", x + 20, y + 42, 4210752);
		text_PointName.drawTextBox();
		
		mc.fontRendererObj.drawString("Point Radius", x + 20, y + 62, 4210752);
		text_PointRadius.drawTextBox();
		
		mc.fontRendererObj.drawString("Capture Time", x + 20, y + 82, 4210752);
		text_PointCaptureOrderNumber.drawTextBox();

		super.drawScreen(mouseX, mouseY, partTicks);
	}

	@Override
	public boolean doesGuiPauseGame() {
		return false;
	}

	@Override
	public void updateScreen() {
		super.updateScreen();

	}

	/**
	 * Fired when a key is typed. This is the equivalent of KeyListener.keyTyped(KeyEvent e).
	 * @throws IOException 
	 */
	@Override
	protected void keyTyped(char chr, int keyCode) throws IOException {
		Log.info("Key typed: " + chr);
		if (text_PointName.textboxKeyTyped(chr, keyCode)) {
			ModNetwork.simpleNetworkWrapper.sendToServer(new MessageHandleTextUpdate(capture_point.getPos(), Reference.MESSAGE_FIELD1_ID, text_PointName.getText()));
		} else if (text_PointTeam.textboxKeyTyped(chr, keyCode)) {
			ModNetwork.simpleNetworkWrapper.sendToServer(new MessageHandleTextUpdate(capture_point.getPos(), Reference.MESSAGE_FIELD2_ID, text_PointTeam.getText()));
			Log.info("Key typed in Team Color box.");
		} else if (text_PointRadius.textboxKeyTyped(chr, keyCode)) {
			ModNetwork.simpleNetworkWrapper.sendToServer(new MessageHandleTextUpdate(capture_point.getPos(), Reference.MESSAGE_FIELD3_ID, text_PointRadius.getText()));
		} else if (text_PointCaptureOrderNumber.textboxKeyTyped(chr, keyCode)) {
			ModNetwork.simpleNetworkWrapper.sendToServer(new MessageHandleTextUpdate(capture_point.getPos(), Reference.MESSAGE_FIELD4_ID, text_PointCaptureOrderNumber.getText()));
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
		//ModNetwork.simpleNetworkWrapper.sendToServer(new DeathCubeMessageInputToServer(Reference.MESSAGE_SOURCE_GUI, Reference.GUI_GAME_CONTROLLER, text_PointName.getText(), text_PointTeam.getText(), text_PointRadius.getText(), text_PointCaptureOrderNumber.getText()));
	}
}
