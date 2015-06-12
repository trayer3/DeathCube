package com.projectreddog.deathcube.client.handler;

import com.projectreddog.deathcube.utility.Log;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiPlayerTabOverlay;
import net.minecraft.client.settings.KeyBinding;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.InputEvent;

/**
 * 
 * Copying from net.minecraft.client.gui.GuiIngame
 * net.minecraft.client.gui.GuiPlayerTabOverlay
 *
 */
public class InputEventHandler {
	
	//protected final GuiPlayerTabOverlay overlayPlayerList;
	
	@SubscribeEvent
	public void handleKeyInputevent(InputEvent.KeyInputEvent event) {
		
		/**
		 * Check that Player does not currently have a GUI open?
		 */
		KeyBinding key;
		
		
		
		
		/**
		 * See if GameStats Key is Pressed
		 */
		if (Minecraft.getMinecraft().gameSettings.keyBindForward.isKeyDown()) {
			//this.overlayPlayerList.func_175246_a(true);
            //this.overlayPlayerList.func_175249_a(i, scoreboard, scoreobjective1);
        }
        else
        {
            //this.overlayPlayerList.func_175246_a(false);
        }
	}
}
