package com.projectreddog.deathcube.client.gui;

import java.io.IOException;

import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.GuiTextField;

import com.projectreddog.deathcube.DeathCube;
import com.projectreddog.deathcube.init.ModNetwork;
import com.projectreddog.deathcube.network.MessageHandleGuiButtonPress;
import com.projectreddog.deathcube.network.MessageHandleTextUpdate;
import com.projectreddog.deathcube.network.MessageRequestTextUpdate_Client;
import com.projectreddog.deathcube.reference.Reference;
import com.projectreddog.deathcube.tileentity.TileEntityGameController;
import com.projectreddog.deathcube.utility.Log;

public class GuiGameController extends GuiDeathCube {

	private TileEntityGameController game_controller;
	private GuiTextField text_MapName;
	private GuiTextField text_NumTeams;
	private GuiButton button_Start;
	private int gui_Width = 225;
	private int gui_Height = 137;
	private int x = 0;
	private int y = 0;
	private int ySpacing = 20;
	private int xSpacingLabel = 20;
	private int xSpacingField = 60;
	private int fieldWidth = 27;
	private int fieldHeight = 12;
	private int buttonWidth;
	private int pageNumber = 1;

	public GuiGameController(TileEntityGameController game_controller) {
		super();
		this.game_controller = game_controller;
	}

	@Override
	public void initGui() {
		super.initGui();

		pageNumber = 1;
		
		/**
		 * Find GUI upper-left corner.
		 */
		x = (this.width - this.gui_Width) / 2;
		y = (this.height - this.gui_Height) / 2;

		/**
		 * Prepare Button
		 * - Page 1 Buttons
		 */
		buttonWidth = 80;
		button_Start = new GuiButton(Reference.BUTTON_1, ((this.width - buttonWidth) / 2), ((this.height - 20) / 2) + 50, buttonWidth, 20, "Start Game!");
		buttonList.add(button_Start);

		/**
		 * Prepare Text Field
		 * - Page 1 Fields
		 */
		text_MapName = new GuiTextField(20, fontRendererObj, x + xSpacingField + 10, y + ySpacing, fieldWidth*2, fieldHeight);
		text_MapName.setMaxStringLength(50);
		text_MapName.setText(game_controller.getMapName());
		
		text_NumTeams = new GuiTextField(20, fontRendererObj, x + xSpacingField + 10, y + ySpacing*2, fieldWidth, fieldHeight);
		text_NumTeams.setMaxStringLength(1);
		text_NumTeams.setText(String.valueOf(game_controller.getNumTeams()));
	}
	
	@Override
	public void onTextfieldUpdate(int fieldID) {
		if (fieldID == Reference.MESSAGE_FIELD1_ID) {
			text_NumTeams.setText(String.valueOf(game_controller.getNumTeams()));
		} else if (fieldID == Reference.MESSAGE_FIELD7_ID) {
			text_MapName.setText(game_controller.getMapName());
		}
	}
	
	/**
	 * Called when the mouse is clicked.
	 */
	@Override
	protected void mouseClicked(int mouseX, int mouseY, int button) throws IOException {
		super.mouseClicked(mouseX, mouseY, button);
		text_MapName.mouseClicked(mouseX, mouseY, button);
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

		/**
		 * Text Fields
		 * - Page 1 Fields
		 */
		if(pageNumber == 1) {
			mc.fontRendererObj.drawString("DeathCube Game Controller", x + 8, y + 8, 4210752);
			
			mc.fontRendererObj.drawString("Map Name", x + xSpacingLabel, y + ySpacing + 2, 4210752);
			text_MapName.drawTextBox();
			
			mc.fontRendererObj.drawString("Teams", x + xSpacingLabel, y + ySpacing*2 + 2, 4210752);
			text_NumTeams.drawTextBox();
		}

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
		} else if (text_MapName.textboxKeyTyped(chr, keyCode)) {
			ModNetwork.simpleNetworkWrapper.sendToServer(new MessageHandleTextUpdate(game_controller.getPos(), Reference.MESSAGE_FIELD7_ID, text_MapName.getText()));
		} else {
			super.keyTyped(chr, keyCode);
		}
	}

	@Override
    protected void actionPerformed(GuiButton button){
        if(button.id == Reference.BUTTON_1) {
        	Log.info("Start Button Pressed - Start Game");
        	ModNetwork.simpleNetworkWrapper.sendToServer(new MessageHandleGuiButtonPress(game_controller.getPos(), Reference.BUTTON_1));
        } 
    }

	@Override
	public boolean doesGuiPauseGame() {
		return false;
	}
	
	@Override
	public void updateScreen() {
		super.updateScreen();
		
	}

	@Override
	public void onGuiClosed() {
		super.onGuiClosed();
		
		/**
		 * Verify that the Number of Teams is valid.
		 */
		int fieldNumTeams = Integer.parseInt(text_NumTeams.getText());
		if(fieldNumTeams < 2 || fieldNumTeams > Reference.TEAM_NUM_POSSIBLE) {
			ModNetwork.simpleNetworkWrapper.sendToServer(new MessageHandleTextUpdate(game_controller.getPos(), Reference.MESSAGE_FIELD1_ID, String.valueOf(Reference.TEAM_NUM_POSSIBLE)));
		} 
	}
}
