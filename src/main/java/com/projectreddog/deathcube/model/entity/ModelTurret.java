package com.projectreddog.deathcube.model.entity;

import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.entity.Entity;
import net.minecraft.util.ResourceLocation;

import org.lwjgl.opengl.GL11;

import com.projectreddog.deathcube.entity.EntityTurret;
import com.projectreddog.deathcube.model.advanced.AdvancedModelLoader;
import com.projectreddog.deathcube.model.advanced.IModelCustom;
import com.projectreddog.deathcube.reference.Reference;

public class ModelTurret extends ModelBase {
	// fields
	private IModelCustom myModel;

	public ModelTurret() {

		// LogHelper.info("LOADING dump truck MODEL!");
		myModel = AdvancedModelLoader.loadModel(new ResourceLocation(Reference.MOD_ID.toLowerCase(), "models/Turret.obj"));
		// casinoTexture = new ResourceLocation("modid",
		// "textures/casinoTexture.png");

	}

	public void render(Entity entity, float f, float f1, float f2, float f3, float f4, float f5) {
		super.render(entity, f, f1, f2, f3, f4, f5);
		// myModel.renderAll();

		this.renderGroupObject("RotorBase_Cylinder");
		this.renderGroupObject("Cylinder_Cylinder.002");
		this.renderGroupObject("Legs_Cylinder.001");
		GL11.glTranslatef(0f, 0f, 0f);
		if (entity != null) {
			GL11.glRotatef((((EntityTurret) entity).topRotation), 0, 1, 0);

		}
		this.renderGroupObject("Housing_Cube.001");

		if (entity instanceof EntityTurret) {
			EntityTurret et = (EntityTurret) entity;
			if (et.state < Reference.TURRET_RECOIL_TICKS / 2) {
				GL11.glTranslatef(0f, 0f, et.state * Reference.TURRET_TRAVEL_MULTIPLIER);
			} else // => 30
			{
				GL11.glTranslatef(0f, 0f, ((Reference.TURRET_RECOIL_TICKS - et.state) * Reference.TURRET_TRAVEL_MULTIPLIER));
			}
		}
		this.renderGroupObject("Barrel_Cylinder.003");

	}

	public void renderGroupObject(String groupName) {
		myModel.renderPart(groupName);

	}

	private void setRotation(ModelRenderer model, float x, float y, float z) {
		model.rotateAngleX = x;
		model.rotateAngleY = y;
		model.rotateAngleZ = z;
	}

	public void setRotationAngles(float f, float f1, float f2, float f3, float f4, float f5, Entity e) {
		super.setRotationAngles(f, f1, f2, f3, f4, f5, e);
	}

	public ResourceLocation getTexture() {

		return new ResourceLocation("machinemod", Reference.MODEL_TURRET_TEXTURE_LOCATION);
	}
}
