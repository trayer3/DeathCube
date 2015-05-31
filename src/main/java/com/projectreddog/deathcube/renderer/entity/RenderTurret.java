package com.projectreddog.deathcube.renderer.entity;

import net.minecraft.client.Minecraft;
import net.minecraft.client.model.ModelBase;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.client.renderer.entity.RenderItem;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.entity.Entity;
import net.minecraft.util.ResourceLocation;

import org.lwjgl.opengl.GL11;

import com.projectreddog.deathcube.model.entity.ModelTurret;
import com.projectreddog.deathcube.reference.Reference;

public class RenderTurret extends Render {

	protected ModelBase modelTurret;

	private RenderItem itemRenderer;

	public RenderTurret(RenderManager renderManager) {

		super(renderManager);

		// LogHelper.info("in RenderLoader constructor");
		shadowSize = 1F;
		this.modelTurret = new ModelTurret();
		itemRenderer = Minecraft.getMinecraft().getRenderItem();

	}

	@Override
	public void doRender(Entity entity, double x, double y, double z, float yaw, float pitch) {

		GL11.glPushMatrix();
		GL11.glTranslatef((float) x, (float) y, (float) z);
		this.bindEntityTexture(entity);
		GL11.glScalef(-0.80F, -.80F, .80F);
		this.modelTurret.render(entity, 0.0F, 0.0F, -0.1F, 0.0F, 0.0F, 0.0625F);

		GL11.glPopMatrix();
	}

	@Override
	protected ResourceLocation getEntityTexture(Entity entity) {
		return new ResourceLocation("machinemod", Reference.MODEL_TURRET_TEXTURE_LOCATION);
	}

}
