package com.projectreddog.deathcube.renderer.entity;

import net.minecraft.client.model.ModelBase;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.entity.Entity;
import net.minecraft.util.ResourceLocation;

import org.lwjgl.opengl.GL11;

import com.projectreddog.deathcube.DeathCube;
import com.projectreddog.deathcube.entity.EntityWaypoint;
import com.projectreddog.deathcube.model.ModelWaypoint;
import com.projectreddog.deathcube.reference.Reference;

public class RenderWaypoint extends Render {

	protected ModelBase Model;

	public RenderWaypoint(RenderManager renderManager) {
		super(renderManager);
		shadowSize = 1F;
		this.Model = new ModelWaypoint();
	}

	@Override
	protected ResourceLocation getEntityTexture(Entity entity) {
		return new ResourceLocation("deathcube", Reference.ENTITY_WAYPOINT_TEXTURE_LOCATION);
	}

	@Override
	public void doRender(Entity entity, double x, double y, double z, float yaw, float pitch) {

		GL11.glPushMatrix();
		GL11.glTranslated(x, y+ DeathCube.renderHelperYOffset, z);
		GL11.glRotated(DeathCube.renderHelperRotation, 0, 1, 0);
		/**
		 * Setup drawing to always be in front of other blocks
		 */
		int func1 = GL11.glGetInteger(GL11.GL_ALPHA_TEST_FUNC);
		float ref1 = GL11.glGetFloat(GL11.GL_ALPHA_TEST_REF);
		GL11.glAlphaFunc(GL11.GL_GREATER, 0.0F);
		GL11.glEnable(GL11.GL_BLEND);
		GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
		GL11.glDisable(GL11.GL_DEPTH_TEST);
		GL11.glDepthMask(false);
		GL11.glScalef(2f, 2f, 2f);
		/**
		 * End setup drawing to always be in front of other blocks
		 */
		this.bindEntityTexture(entity);
		if (entity instanceof EntityWaypoint){
			// should always be true but to be typesafe ;)
			EntityWaypoint entityWaypoint = (EntityWaypoint) entity;
			if (entityWaypoint.team ==1){
				//red
				GlStateManager.color(1.0F, 0F, 0F, 1.0F);
			}else if (entityWaypoint.team ==2){
				//green
				GlStateManager.color(0F, 1.0F, 0F, 1.0F);
			}else if (entityWaypoint.team ==3){
				//blue
				GlStateManager.color(0F, 0F, 1.0F, 1.0F);
			}else if (entityWaypoint.team ==4){
				// yellow
				GlStateManager.color(0F, 1.0F, 1.0F, 1.0F);
			}else{
				//normal coloring
				GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
			}
		}
		GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
		this.Model.render(entity, 0.0F, 0.0F, -0.1F, 0.0F, 0.0F, 0.0625F);

		/**
		 * Disable drawing to always be in front of other blocks
		 */
		GL11.glEnable(GL11.GL_DEPTH_TEST);
		GL11.glDepthMask(true);

		GL11.glAlphaFunc(func1, ref1);
		GL11.glEnable(GL11.GL_FOG);
		GL11.glEnable(GL11.GL_LIGHTING);
		/**
		 * End disable drawing to always be in front of other blocks
		 */
		GL11.glPopMatrix();

	}

}
