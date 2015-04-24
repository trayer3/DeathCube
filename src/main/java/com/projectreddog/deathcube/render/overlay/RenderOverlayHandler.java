package com.projectreddog.deathcube.render.overlay;


import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.Gui;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.client.event.RenderGameOverlayEvent.ElementType;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import org.lwjgl.opengl.GL11;

import com.projectreddog.deathcube.reference.Reference;
import com.projectreddog.deathcube.utility.Log;


public class RenderOverlayHandler extends Gui {
	private final FontRenderer fontRenderer;
	private Minecraft mc;

	public RenderOverlayHandler() {
		this.fontRenderer = Minecraft.getMinecraft().fontRendererObj;
		this.mc = Minecraft.getMinecraft();
	}

	@SubscribeEvent(priority = EventPriority.NORMAL)
	public void onRenderGameOverlay(RenderGameOverlayEvent.Post event) {

		 if (event.isCancelable() || event.type != ElementType.BOSSHEALTH) {
		 return;
		 // only render the overlay in the boss health  type so the xp hunger & health bars are not impacted.
		 }
		 //Log.info(event.type);
		
			int xPos = 2;
			int yPos = 2;

			GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
			GL11.glDisable(GL11.GL_LIGHTING);
			//Example drawing a texture :
			this.mc.renderEngine.bindTexture(getTextureLocationGage());
			this.drawTexturedModalRect(xPos, yPos, 0, 0, 16, 64);

			//Example drawing a texture :

			this.mc.renderEngine.bindTexture(getTextureLocationMarker());
			int yOffest = 0;
			this.drawTexturedModalRect(xPos + 10, yPos + yOffest, 0, 0, 6, 3);

			//Example drawing a a string:
			int depth = 1;
			this.fontRenderer.drawString("Depth:" + (depth), 0, 20, 14737632);
		
		
	}


	private ResourceLocation guageRL;

	protected ResourceLocation getTextureLocationGage() {
		// Original design was to cause it to only load this guageRL 1 time
		if (guageRL == null) {
			guageRL = Reference.GUI_STARTING_GEAR_CONFIG_BACKGROUND;
		}
		return guageRL;
	}

	private ResourceLocation markerRL;

	protected ResourceLocation getTextureLocationMarker() {
		//Original design was to cause it to only load this markerRL 1 time
		if (markerRL == null) {
			markerRL = Reference.GUI_STARTING_GEAR_CONFIG_BACKGROUND;
		}
		return markerRL;
	}
}
