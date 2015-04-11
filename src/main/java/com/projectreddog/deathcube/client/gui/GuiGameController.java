package com.projectreddog.deathcube.client.gui;

import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.GuiTextField;

import com.projectreddog.deathcube.init.ModNetwork;
import com.projectreddog.deathcube.network.MessageHandleGuiButtonPress;
import com.projectreddog.deathcube.reference.Reference;
import com.projectreddog.deathcube.tileentity.TileEntityGameController;
import com.projectreddog.deathcube.utility.Log;

public class GuiGameController extends GuiScreen {

	private TileEntityGameController game_controller;
	private GuiTextField text_NumTeams;
	private GuiButton startButton;
	private int gui_Width = 225;
	private int gui_Height = 137;
	private int x = 0;
	private int y = 0;

	public GuiGameController(TileEntityGameController game_controller) {
		super();
		this.game_controller = game_controller;
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
		 * Prepare Button
		 */
		startButton = new GuiButton(Reference.BUTTON_START_GAME, (this.width - 80) / 2, ((this.height - 20) / 2) + 50, 80, 20, "");
		buttonList.add(startButton);

		/**
		 * Prepare Text Field
		 */
		text_NumTeams = new GuiTextField(20, fontRendererObj, x + 70, y + 20, 70, 12);
		text_NumTeams.setMaxStringLength(40);
		text_NumTeams.setText("2");
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
		mc.fontRendererObj.drawString("Teams", x + 20, y + 22, 4210752);
		text_NumTeams.drawTextBox();

		super.drawScreen(mouseX, mouseY, partTicks);
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
        	//ModNetwork.simpleNetworkWrapper.sendToServer(new DeathCubeMessageInputToServer(Reference.MESSAGE_SOURCE_GUI, Reference.GUI_GAME_CONTROLLER, String.valueOf(Reference.BUTTON_START_GAME), text_NumTeams.getText(), "", ""));
        	ModNetwork.simpleNetworkWrapper.sendToServer(new MessageHandleGuiButtonPress(game_controller, Reference.BUTTON_START_GAME));
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

		/**
		 * Save data from Gui to server.
		 */
	}
}
