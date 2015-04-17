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
	private GuiTextField text_NumTeams;
	private GuiTextField text_ForceFieldx;
	private GuiTextField text_ForceFieldz;
	private GuiTextField text_ForceFieldyUp;
	private GuiTextField text_ForceFieldyDown;
	private GuiTextField text_ForceFieldStrength;
	private GuiButton button_Start;
	private GuiButton button_ForceField_Page;
	private GuiButton button_ForceField_Used;
	private GuiButton button_ForceField_Toggle;
	private GuiButton button_Back;
	private int gui_Width = 225;
	private int gui_Height = 137;
	private int x = 0;
	private int y = 0;
	private int ySpacing = 20;
	private int xSpacingLabel = 20;
	private int xSpacingField = 60;
	private int fieldWidth = 25;
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

		/**
		 * Need a better spot for this.  Or wait a short time?
		 * - Moved to DeathCube.serverStart()
		 */
		//ModNetwork.simpleNetworkWrapper.sendToServer(new MessageRequestTextUpdate_Client(game_controller.getPos()));
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
		
		buttonWidth = 120;
		button_ForceField_Page = new GuiButton(Reference.BUTTON_2, ((this.width - buttonWidth) / 2), ((this.height - 20) / 2) + 25, buttonWidth, 20, "Configure Force Field");
		buttonList.add(button_ForceField_Page);
		
		/**
		 * - Page 2 Buttons
		 */
		buttonWidth = 30;
		button_ForceField_Used = new GuiButton(Reference.BUTTON_3, ((this.width - buttonWidth) / 2) + 50, ((this.height - 20) / 2) - 50, buttonWidth, 20, "");
		if (DeathCube.useForceField) {
    		button_ForceField_Used.displayString = "Yes";
		} else {
			button_ForceField_Used.displayString = "No";
		}
		
		buttonWidth = 20;
		button_ForceField_Toggle = new GuiButton(Reference.BUTTON_4, (this.width - buttonWidth) / 2, ((this.height - 20) / 2), buttonWidth, 20, "#");
		
		
		// This button position:  Centered plus moved to the right of the GUI, then moved back by the width of the button, then back 10 more.  
		// --> Down to same height as Start Button. = Bottom Left Corner.
		buttonWidth = 40;
		button_Back = new GuiButton(Reference.BUTTON_5, ((this.width - buttonWidth) / 2) + ((this.gui_Width) / 2) - buttonWidth, ((this.height - 20) / 2) + 50, buttonWidth, 20, "Back");

		/**
		 * Prepare Text Field
		 * - Page 1 Fields
		 */
		text_NumTeams = new GuiTextField(20, fontRendererObj, x + xSpacingField, y + ySpacing, fieldWidth, fieldHeight);
		text_NumTeams.setMaxStringLength(40);
		text_NumTeams.setText(String.valueOf(game_controller.getNumTeams()));
		
		/**
		 * - Page 2 Fields
		 */
		text_ForceFieldx = new GuiTextField(20, fontRendererObj, ((this.width - 30) / 2) - fieldWidth, ((this.height - fieldHeight) / 2), fieldWidth, fieldHeight);
		text_ForceFieldx.setMaxStringLength(3);
		text_ForceFieldx.setText(String.valueOf(game_controller.getForceFieldx()));
		
		text_ForceFieldz = new GuiTextField(20, fontRendererObj, ((this.width - 30) / 2) + fieldWidth + 5, ((this.height - fieldHeight) / 2), fieldWidth, fieldHeight);
		text_ForceFieldz.setMaxStringLength(3);
		text_ForceFieldz.setText(String.valueOf(game_controller.getForceFieldz()));
		
		text_ForceFieldyUp = new GuiTextField(20, fontRendererObj, ((this.width - 30) / 2) + 5, ((this.height - fieldHeight) / 2) - 20, fieldWidth, fieldHeight);
		text_ForceFieldyUp.setMaxStringLength(3);
		text_ForceFieldyUp.setText(String.valueOf(game_controller.getForceFieldyUp()));
		
		text_ForceFieldyDown = new GuiTextField(20, fontRendererObj, ((this.width - 30) / 2) + 5, ((this.height - fieldHeight) / 2) + 20, fieldWidth, fieldHeight);
		text_ForceFieldyDown.setMaxStringLength(3);
		text_ForceFieldyDown.setText(String.valueOf(game_controller.getForceFieldyDown()));
		
		text_ForceFieldStrength = new GuiTextField(20, fontRendererObj, x + xSpacingField + 10, ((this.height - 16) / 2) + 50, fieldWidth, fieldHeight);
		text_ForceFieldStrength.setMaxStringLength(3);
		text_ForceFieldStrength.setText(String.valueOf(game_controller.getForceFieldStrength()));
	}
	
	@Override
	public void onTextfieldUpdate(int fieldID) {
		if (fieldID == Reference.MESSAGE_FIELD1_ID) {
			text_NumTeams.setText(String.valueOf(game_controller.getNumTeams()));
		} else if (fieldID == Reference.MESSAGE_FIELD2_ID) {
			text_ForceFieldx.setText(String.valueOf(game_controller.getForceFieldx()));
		} else if (fieldID == Reference.MESSAGE_FIELD3_ID) {
			text_ForceFieldz.setText(String.valueOf(game_controller.getForceFieldz()));
		} else if (fieldID == Reference.MESSAGE_FIELD4_ID) {
			text_ForceFieldyUp.setText(String.valueOf(game_controller.getForceFieldyUp()));
		} else if (fieldID == Reference.MESSAGE_FIELD5_ID) {
			text_ForceFieldyDown.setText(String.valueOf(game_controller.getForceFieldyDown()));
		} else if (fieldID == Reference.MESSAGE_FIELD6_ID) {
			text_ForceFieldStrength.setText(String.valueOf(game_controller.getForceFieldStrength()));
		}
	}
	
	/**
	 * Called when the mouse is clicked.
	 */
	@Override
	protected void mouseClicked(int mouseX, int mouseY, int button) throws IOException {
		super.mouseClicked(mouseX, mouseY, button);
		text_NumTeams.mouseClicked(mouseX, mouseY, button);
		text_ForceFieldx.mouseClicked(mouseX, mouseY, button);
		text_ForceFieldz.mouseClicked(mouseX, mouseY, button);
		text_ForceFieldyUp.mouseClicked(mouseX, mouseY, button);
		text_ForceFieldyDown.mouseClicked(mouseX, mouseY, button);
		text_ForceFieldStrength.mouseClicked(mouseX, mouseY, button);
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
			mc.fontRendererObj.drawString("Teams", x + xSpacingLabel, y + ySpacing + 2, 4210752);
			text_NumTeams.drawTextBox();
		}
		
		/**
		 * - Page 2 Fields
		 */
		if(pageNumber == 2 && DeathCube.useForceField) {
			mc.fontRendererObj.drawString("Use Force Field?", x + 8, y + 8, 4210752);
			mc.fontRendererObj.drawString("x:", ((this.width - 18) / 2) - 40, ((this.height - fieldHeight) / 2), 4210752);
			text_ForceFieldx.drawTextBox();
			
			mc.fontRendererObj.drawString("z:", ((this.width - 18) / 2) + 53, ((this.height - fieldHeight) / 2), 4210752);
			text_ForceFieldz.drawTextBox();
			
			mc.fontRendererObj.drawString("y-up:", ((this.width - 20) / 2), ((this.height - fieldHeight) / 2) - 30, 4210752);
			text_ForceFieldyUp.drawTextBox();
			
			mc.fontRendererObj.drawString("y-down:", ((this.width - 20) / 2) - 5, ((this.height - fieldHeight) / 2) + 35, 4210752);
			text_ForceFieldyDown.drawTextBox();
			
			mc.fontRendererObj.drawString("Strength:", x + xSpacingLabel, ((this.height - fieldHeight) / 2) + 50, 4210752);
			text_ForceFieldStrength.drawTextBox();
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
		} else if (text_ForceFieldx.textboxKeyTyped(chr, keyCode)) {
			ModNetwork.simpleNetworkWrapper.sendToServer(new MessageHandleTextUpdate(game_controller.getPos(), Reference.MESSAGE_FIELD2_ID, text_ForceFieldx.getText()));
		} else if (text_ForceFieldz.textboxKeyTyped(chr, keyCode)) {
			ModNetwork.simpleNetworkWrapper.sendToServer(new MessageHandleTextUpdate(game_controller.getPos(), Reference.MESSAGE_FIELD3_ID, text_ForceFieldz.getText()));
		} else if (text_ForceFieldyUp.textboxKeyTyped(chr, keyCode)) {
			ModNetwork.simpleNetworkWrapper.sendToServer(new MessageHandleTextUpdate(game_controller.getPos(), Reference.MESSAGE_FIELD4_ID, text_ForceFieldyUp.getText()));
		} else if (text_ForceFieldyDown.textboxKeyTyped(chr, keyCode)) {
			ModNetwork.simpleNetworkWrapper.sendToServer(new MessageHandleTextUpdate(game_controller.getPos(), Reference.MESSAGE_FIELD5_ID, text_ForceFieldyDown.getText()));
		} else if (text_ForceFieldStrength.textboxKeyTyped(chr, keyCode)) {
			ModNetwork.simpleNetworkWrapper.sendToServer(new MessageHandleTextUpdate(game_controller.getPos(), Reference.MESSAGE_FIELD6_ID, text_ForceFieldStrength.getText()));
		} else {
			super.keyTyped(chr, keyCode);
		}
	}

	@Override
    protected void actionPerformed(GuiButton button){
        if(button.id == Reference.BUTTON_1) {
        	Log.info("Start Button Pressed - Start Game");
        	ModNetwork.simpleNetworkWrapper.sendToServer(new MessageHandleGuiButtonPress(game_controller.getPos(), Reference.BUTTON_1));
        } else if(button.id == Reference.BUTTON_2) {
        	Log.info("Force Field Page - Button Pressed");
        	//ModNetwork.simpleNetworkWrapper.sendToServer(new MessageHandleGuiButtonPress(game_controller.getPos(), Reference.BUTTON_2));
        	pageNumber = 2;
        	buttonList.remove(button_Start);
        	buttonList.remove(button_ForceField_Page);
        	buttonList.add(button_ForceField_Used);
    		buttonList.add(button_ForceField_Toggle);
    		buttonList.add(button_Back);
        } else if(button.id == Reference.BUTTON_3) {
        	Log.info("Force Field Used - Button Pressed");
        	ModNetwork.simpleNetworkWrapper.sendToServer(new MessageHandleGuiButtonPress(game_controller.getPos(), Reference.BUTTON_3));
        	
        	if (DeathCube.useForceField) {
        		button_ForceField_Used.displayString = "Yes";
        		buttonList.add(button_ForceField_Toggle);
			} else {
				button_ForceField_Used.displayString = "No";
				buttonList.remove(button_ForceField_Toggle);
			}
        } else if(button.id == Reference.BUTTON_4) {
        	Log.info("Force Field Toggle - Pressed");
        	ModNetwork.simpleNetworkWrapper.sendToServer(new MessageHandleGuiButtonPress(game_controller.getPos(), Reference.BUTTON_4));
        } else if(button.id == Reference.BUTTON_5) {
        	Log.info("Back Button Pressed");
        	//ModNetwork.simpleNetworkWrapper.sendToServer(new MessageHandleGuiButtonPress(game_controller.getPos(), Reference.BUTTON_5));
        	pageNumber = 1;
        	buttonList.add(button_Start);
        	buttonList.add(button_ForceField_Page);
        	buttonList.remove(button_ForceField_Used);
    		buttonList.remove(button_ForceField_Toggle);
    		buttonList.remove(button_Back);
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
		
		/**
		 * Verify that the Force Field dimensions are valid.
		 */
		int fieldx = Integer.parseInt(text_ForceFieldx.getText());
		if(fieldx < 5 || fieldx > 500) {
			ModNetwork.simpleNetworkWrapper.sendToServer(new MessageHandleTextUpdate(game_controller.getPos(), Reference.MESSAGE_FIELD2_ID, String.valueOf(20)));
		}
		
		int fieldz = Integer.parseInt(text_ForceFieldz.getText());
		if(fieldz < 5 || fieldz > 500) {
			ModNetwork.simpleNetworkWrapper.sendToServer(new MessageHandleTextUpdate(game_controller.getPos(), Reference.MESSAGE_FIELD3_ID, String.valueOf(20)));
		}
		
		int fieldyUp = Integer.parseInt(text_ForceFieldyUp.getText());
		if(fieldyUp < 5 || fieldyUp > 256) {
			ModNetwork.simpleNetworkWrapper.sendToServer(new MessageHandleTextUpdate(game_controller.getPos(), Reference.MESSAGE_FIELD4_ID, String.valueOf(20)));
		}
		
		int fieldyDown = Integer.parseInt(text_ForceFieldyDown.getText());
		if(fieldyDown < 5 || fieldyDown > 256) {
			ModNetwork.simpleNetworkWrapper.sendToServer(new MessageHandleTextUpdate(game_controller.getPos(), Reference.MESSAGE_FIELD5_ID, String.valueOf(20)));
		}
		
		int field_strength = Integer.parseInt(text_ForceFieldStrength.getText());
		if(field_strength < 5 || field_strength > 999) {
			ModNetwork.simpleNetworkWrapper.sendToServer(new MessageHandleTextUpdate(game_controller.getPos(), Reference.MESSAGE_FIELD6_ID, String.valueOf(20)));
		}
		
	}
}
