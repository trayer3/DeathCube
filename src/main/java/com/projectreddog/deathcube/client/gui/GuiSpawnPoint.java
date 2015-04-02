package com.projectreddog.deathcube.client.gui;

import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.WorldRenderer;

import com.projectreddog.deathcube.reference.Reference;
import com.projectreddog.deathcube.tileentity.TileEntitySpawnPoint;

public class GuiSpawnPoint extends GuiScreen {

	private TileEntitySpawnPoint spawn_point;
	private int gui_Width = 176;
	private int gui_Height = 222;
	private int x = 0;
	private int y = 0;
	
	public GuiSpawnPoint(TileEntitySpawnPoint spawn_point) {
		super();
		this.spawn_point = spawn_point;
	}
	
	@Override
    public void drawScreen(int mouseX, int mouseY, float partTicks) {
        this.drawDefaultBackground();

        x = (this.width - this.gui_Width) / 2;
        y = (this.height - this.gui_Height) / 2;
        
        this.mc.renderEngine.bindTexture(Reference.GUI_SPAWN_POINT_BACKGROUND);
        this.drawTexturedModalRect(x, y, 0, 0, this.gui_Width, this.gui_Height);

        super.drawScreen(mouseX, mouseY, partTicks);
    }
	
	@Override
    public boolean doesGuiPauseGame() {
        return false;
    }
	
	@Override
    public void onGuiClosed() {
        super.onGuiClosed();

        /**
         *  Save data to server.
         */
    }
}
