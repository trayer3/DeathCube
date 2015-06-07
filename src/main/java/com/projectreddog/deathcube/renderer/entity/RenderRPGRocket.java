package com.projectreddog.deathcube.renderer.entity;

import net.minecraft.client.Minecraft;
import net.minecraft.client.model.ModelBase;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.client.renderer.entity.RenderItem;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.entity.Entity;
import net.minecraft.util.ResourceLocation;

import org.lwjgl.opengl.GL11;

import com.projectreddog.deathcube.model.entity.ModelRPGRocket;
import com.projectreddog.deathcube.reference.Reference;

public class RenderRPGRocket extends Render {

	protected ModelBase modelRPGRocket;

	private RenderItem itemRenderer;

	public RenderRPGRocket(RenderManager renderManager) {

		super(renderManager);

		// LogHelper.info("in RenderLoader constructor");
		shadowSize = 1F;
		this.modelRPGRocket = new ModelRPGRocket();
		itemRenderer = Minecraft.getMinecraft().getRenderItem();

	}

	@Override
	public void doRender(Entity entity, double x, double y, double z, float yaw, float pitch) {

		GL11.glPushMatrix();
		GL11.glTranslatef((float) x, (float) y, (float) z);
		GL11.glRotatef(yaw, 0, 1.0f, 0);
		GL11.glRotatef(entity.rotationPitch * -1, 1.0f, 0, 0);

		this.bindEntityTexture(entity);
		GL11.glScalef(-0.50F, -.50F, .50F);
		this.modelRPGRocket.render(entity, 0.0F, 0.0F, -0.1F, 0.0F, 0.0F, 0.0625F);

		GL11.glPopMatrix();
	}

	@Override
	protected ResourceLocation getEntityTexture(Entity entity) {
		return new ResourceLocation(Reference.MOD_ID, Reference.MODEL_RPGROCKET_TEXTURE_LOCATION);
	}

}
