package com.projectreddog.deathcube.renderer.entity;

import net.minecraft.client.entity.AbstractClientPlayer;
import net.minecraft.client.model.ModelPlayer;
import net.minecraft.client.renderer.entity.RenderItem;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.entity.RendererLivingEntity;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EnumPlayerModelParts;
import net.minecraft.util.ResourceLocation;

import com.projectreddog.deathcube.entity.EntityMinion;

public class RenderMinion extends RendererLivingEntity {

	private RenderItem itemRenderer;

	public RenderMinion(RenderManager renderManager) {

		this(renderManager, false);

	}

	public RenderMinion(RenderManager renderManager, boolean useSmallArms) {

		super(renderManager, new ModelPlayer(0.0f, useSmallArms), 0.5f);

	}

	public void doRender(EntityLivingBase entity, double x, double y, double z, float p_76986_8_, float partialTicks) {

		this.func_180596_a(entity, x, y, z, p_76986_8_, partialTicks);
	}

	public void doRender(Entity entity, double x, double y, double z, float p_76986_8_, float partialTicks) {
		this.func_180596_a((EntityLivingBase) entity, x, y, z, p_76986_8_, partialTicks);
	}

	public void func_180596_a(EntityLivingBase p_180596_1_, double p_180596_2_, double p_180596_4_, double p_180596_6_, float p_180596_8_, float p_180596_9_) {
		double d3 = p_180596_4_;

		this.func_177137_d((EntityMinion) p_180596_1_);
		if (((EntityMinion) p_180596_1_).player != null) {
			super.doRender(p_180596_1_, p_180596_2_, d3, p_180596_6_, p_180596_8_, p_180596_9_);
		}

	}

	private void func_177137_d(EntityMinion p_177137_1_) {
		ModelPlayer modelplayer = this.getPlayerModel();
		// tmp

		AbstractClientPlayer abstractClientPlayer = (AbstractClientPlayer) p_177137_1_.player;
		// AbstractClientPlayer
		// TODO rendering item stack in hand?
		// ItemStack itemstack = p_177137_1_.inventory.getCurrentItem();
		modelplayer.setInvisible(true);
		if (abstractClientPlayer != null) {
			modelplayer.bipedHeadwear.showModel = abstractClientPlayer.func_175148_a(EnumPlayerModelParts.HAT);
			modelplayer.bipedBodyWear.showModel = abstractClientPlayer.func_175148_a(EnumPlayerModelParts.JACKET);
			modelplayer.bipedLeftLegwear.showModel = abstractClientPlayer.func_175148_a(EnumPlayerModelParts.LEFT_PANTS_LEG);
			modelplayer.bipedRightLegwear.showModel = abstractClientPlayer.func_175148_a(EnumPlayerModelParts.RIGHT_PANTS_LEG);
			modelplayer.bipedLeftArmwear.showModel = abstractClientPlayer.func_175148_a(EnumPlayerModelParts.LEFT_SLEEVE);
			modelplayer.bipedRightArmwear.showModel = abstractClientPlayer.func_175148_a(EnumPlayerModelParts.RIGHT_SLEEVE);
			modelplayer.heldItemLeft = 0;
			modelplayer.aimedBow = false;

		}
		// if (itemstack == null) {
		// modelplayer.heldItemRight = 0;
		// } else {
		// modelplayer.heldItemRight = 1;
		//
		// if (p_177137_1_.getItemInUseCount() > 0) {
		// EnumAction enumaction = itemstack.getItemUseAction();
		//
		// if (enumaction == EnumAction.BLOCK) {
		// modelplayer.heldItemRight = 3;
		// } else if (enumaction == EnumAction.BOW) {
		// modelplayer.aimedBow = true;
		// }
		// }
		//
		// }
	}

	/**
	 * Returns the location of an entity's texture. Doesn't seem to be called unless you call Render.bindEntityTexture.
	 */
	protected ResourceLocation getEntityTexture(Entity entity) {
		return this.func_180594_a((AbstractClientPlayer) ((EntityMinion) entity).player);
	}

	/**
	 * returns the more specialized type of the model as the player model.
	 */
	public ModelPlayer getPlayerModel() {
		return (ModelPlayer) super.getMainModel();
	}

	protected ResourceLocation func_180594_a(AbstractClientPlayer p_180594_1_) {
		return p_180594_1_.getLocationSkin();
	}

}
