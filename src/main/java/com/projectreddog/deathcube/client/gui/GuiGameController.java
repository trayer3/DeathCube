package com.projectreddog.deathcube.client.gui;

import java.io.IOException;

import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.GuiTextField;

import com.projectreddog.deathcube.init.ModNetwork;
import com.projectreddog.deathcube.network.MessageHandleGuiButtonPress;
import com.projectreddog.deathcube.network.MessageHandleTextUpdate;
import com.projectreddog.deathcube.network.MessageRequestTextUpdate_Client;
import com.projectreddog.deathcube.reference.Reference;
import com.projectreddog.deathcube.tileentity.TileEntityGameController;
import com.projectreddog.deathcube.utility.Log;

public class GuiGameController extends GuiDeathCube {

	private TileEntityGameController game_controller;
	private GuiTextField text_NumTeams;
	private GuiButton startButton;
	private int gui_Width = 225;
	private int gui_Height = 137;
	private int x = 0;
	private int y = 0;
	private int ySpacing = 20;
	private int xSpacingLabel = 20;
	private int xSpacingField = 110;

	public GuiGameController(TileEntityGameController game_controller) {
		super();
		this.game_controller = game_controller;
	}

	@Override
	public void initGui() {
		super.initGui();

		/**
		 * Need a better spot for this.  Or wait a short time?
		 */
		ModNetwork.simpleNetworkWrapper.sendToServer(new MessageRequestTextUpdate_Client(game_controller.getPos()));
		
		/**
		 * Find GUI upper-left corner.
		 */
		x = (this.width - this.gui_Width) / 2;
		y = (this.height - this.gui_Height) / 2;

		/**
		 * Prepare Button
		 */
		startButton = new GuiButton(Reference.BUTTON_START_GAME, (this.width - 80) / 2, ((this.height - 20) / 2) + 50, 80, 20, "");
		buttonList.add(startButton);

		/**
		 * Prepare Text Field
		 */
		text_NumTeams = new GuiTextField(20, fontRendererObj, x + xSpacingField, y + ySpacing, 90, 12);
		text_NumTeams.setMaxStringLength(40);
		text_NumTeams.setText(String.valueOf(game_controller.getNumTeams()));
	}
	
	@Override
	public void onTextfieldUpdate(int fieldID) {
		if (fieldID == Reference.MESSAGE_FIELD1_ID) {
			text_NumTeams.setText(String.valueOf(game_controller.getNumTeams()));
		}
	}
	
	/**
	 * Called when the mouse is clicked.
	 */
	@Override
	protected void mouseClicked(int mouseX, int mouseY, int button) throws IOException {
		super.mouseClicked(mouseX, mouseY, button);
		text_NumTeams.mouseClicked(mouseX, mouseY, button);
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
		mc.fontRendererObj.drawString("DeathCube Game Controller", x + 8, y + 8, 4210752);

		// Text Fields
		mc.fontRendererObj.drawString("Number of Teams", x + xSpacingLabel, y + ySpacing + 2, 4210752);
		text_NumTeams.drawTextBox();

		super.drawScreen(mouseX, mouseY, partTicks);
	}
	
	/**
	 * Fired when a key is typed. This is the equivalent of KeyListener.keyTyped(KeyEvent e).
	 */
	@Override
	protected void keyTyped(char chr, int keyCode) throws IOException {
		Log.info("Key typed: " + chr);
		if (text_NumTeams.textboxKeyTyped(chr, keyCode)) {
			ModNetwork.simpleNetworkWrapper.sendToServer(new MessageHandleTextUpdate(game_controller.getPos(), Reference.MESSAGE_FIELD1_ID, text_NumTeams.getText()));
		} else {
			super.keyTyped(chr, keyCode);
		}
	}

	@Override
	public boolean doesGuiPauseGame() {
		return false;
	}

	@Override
    protected void actionPerformed(GuiButton button){
        if(button.id == Reference.BUTTON_START_GAME) {
        	/**
        	 * Start Game
        	 */
        	Log.info("Start Button Pressed");
        	ModNetwork.simpleNetworkWrapper.sendToServer(new MessageHandleGuiButtonPress(game_controller.getPos(), Reference.BUTTON_START_GAME));
        }
    }

	@Override
	public void updateScreen() {
		super.updateScreen();
		startButton.displayString = "Start Game!";
	}

	@Override
	public void onGuiClosed() {
		super.onGuiClosed();

		boolean isValid = false;
		int fieldNumTeams = 0;
		
		try {
			fieldNumTeams = Integer.parseInt(text_NumTeams.getText());
    	} catch (NumberFormatException e) {
    		Log.warn("Tried to parse non-Integer: " + text_NumTeams.getText());
    	}
		
		/**
		 * Verify that the Team Color is valid.
		 */
		if(fieldNumTeams > 1 && fieldNumTeams <= Reference.TEAM_NUM_POSSIBLE) {
			isValid = true;
		} 
		
		if(!isValid) {
			ModNetwork.simpleNetworkWrapper.sendToServer(new MessageHandleTextUpdate(game_controller.getPos(), Reference.MESSAGE_FIELD1_ID, String.valueOf(Reference.TEAM_NUM_POSSIBLE)));
		}
	}
}
