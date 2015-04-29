package com.projectreddog.deathcube.renderer.overlay;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.Gui;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.client.event.RenderGameOverlayEvent.ElementType;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import org.lwjgl.opengl.GL11;

import com.projectreddog.deathcube.DeathCube;
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
			/**
			 * Only render the overlay in the boss health type so the xp hunger & health bars are not impacted.
			 */
		}

		if (DeathCube.displayScoreboard_client) {

			// Log.info(DeathCube.displayScoreboard_client);
			// Log.info(DeathCube.gameTeams_names.toString());

			int xPos = 0;
			int yPos = 0;

			GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
			GL11.glDisable(GL11.GL_LIGHTING);

			/**
			 * Example drawing a texture:
			 */
			this.mc.renderEngine.bindTexture(getTextureLocationGage());
			// this.drawTexturedModalRect(xPos, yPos, 0, 0, 16, 64);

			/**
			 * Example drawing a texture :
			 */

			/**
			 * Example drawing a a string:
			 */
			int depth = 1;
			int fontColor = 14737632;
			int x_margin = 4;
			int x_spacing = 40;

			if (DeathCube.gameTeams_names != null && DeathCube.gameTeams_names.length != 0) {
				/**
				 * Background. Supposed to be 50% transparent.
				 */
				this.mc.renderEngine.bindTexture(getTextureLocationMarker());
				int width = 85;
				int height = 60;
				this.drawTexturedModalRect(xPos, yPos, 0, 0, width, height);

				/**
				 * Scoreboard Text
				 */
				this.fontRenderer.drawString("Team:", x_margin, 2, fontColor);
				this.fontRenderer.drawString("Point:", x_margin + x_spacing, 2, fontColor);
				int y_spacing = 1;
				for (int i = 0; i < DeathCube.gameTeams_names.length; i++) {
					this.fontRenderer.drawString(DeathCube.gameTeams_names[i], x_margin, 2 + y_spacing * 10, fontColor);
					
					if(DeathCube.gameTeams_activePoints[i] > 0) {
						this.fontRenderer.drawString(String.valueOf(DeathCube.gameTeams_activePoints[i]), x_margin + x_spacing, 2 + y_spacing * 10, fontColor);
					}

					/**
					 * Display Time until Point Captured (Count-down)
					 */
					if (DeathCube.gameTeams_pointTimes[i] > 0) {
						String remainingTime = String.format("%.2f", DeathCube.gameTeams_pointTimes[i]);
						this.fontRenderer.drawString(remainingTime, x_margin + x_spacing + 20, 2 + y_spacing * 10, fontColor);
					}

					y_spacing++;
				}

				long currentTime = System.currentTimeMillis();
				float timeRemaining = ((float) (Reference.TIME_MAINGAME - (currentTime - DeathCube.gameTimeStart_client))) / 1000.0f;
				int remainingMinutes = (int) (timeRemaining / 60);
				int remainingSeconds = (int) (timeRemaining % 60);

				this.fontRenderer.drawString("GameTime:", x_margin, 2 + y_spacing * 10, fontColor);
				this.fontRenderer.drawString(String.valueOf(remainingMinutes) + ":" + remainingSeconds, x_margin + x_spacing + 10, 2 + y_spacing * 10, fontColor);
			}
		}
	}

	private ResourceLocation guageRL;

	protected ResourceLocation getTextureLocationGage() {
		// Original design was to cause it to only load this guageRL 1 time
		if (guageRL == null) {
			guageRL = Reference.HUD_SCORE_TEAM_POINTS;
		}
		return guageRL;
	}

	private ResourceLocation markerRL;

	protected ResourceLocation getTextureLocationMarker() {
		// Original design was to cause it to only load this markerRL 1 time
		if (markerRL == null) {
			markerRL = Reference.HUD_SCORE_TEAM_POINTS;
		}
		return markerRL;
	}
}
