package com.projectreddog.deathcube.client.gui;

import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.WorldRenderer;

import com.projectreddog.deathcube.reference.Reference;
import com.projectreddog.deathcube.tileentity.TileEntitySpawnPoint;

public class GuiSpawnPoint extends GuiScreen {

	private TileEntitySpawnPoint spawn_point;
	
	public GuiSpawnPoint(TileEntitySpawnPoint spawn_point) {
		super();
		this.spawn_point = spawn_point;
	}
	
	@Override
	public void drawBackground(int tint) {
		GlStateManager.disableLighting();
        GlStateManager.disableFog();
        Tessellator tessellator = Tessellator.getInstance();
        WorldRenderer worldrenderer = tessellator.getWorldRenderer();
        this.mc.getTextureManager().bindTexture(Reference.GUI_SPAWN_POINT_BACKGROUND);
        GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
        this.drawTexturedModalRect(0, 0, 0, 0, 176, 222);
        /**float f = 32.0F;
        worldrenderer.startDrawingQuads();
        worldrenderer.setColorOpaque_I(4210752);
        worldrenderer.addVertexWithUV(0.0D, (double)this.height, 0.0D, 0.0D, (double)((float)this.height / f + (float)tint));
        worldrenderer.addVertexWithUV((double)this.width, (double)this.height, 0.0D, (double)((float)this.width / f), (double)((float)this.height / f + (float)tint));
        worldrenderer.addVertexWithUV((double)this.width, 0.0D, 0.0D, (double)((float)this.width / f), (double)tint);
        worldrenderer.addVertexWithUV(0.0D, 0.0D, 0.0D, 0.0D, (double)tint);
        tessellator.draw();**/
	}
}
