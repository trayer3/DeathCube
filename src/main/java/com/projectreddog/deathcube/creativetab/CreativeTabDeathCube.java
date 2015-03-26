package com.projectreddog.deathcube.creativetab;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.init.Items;
import net.minecraft.item.Item;

import com.projectreddog.deathcube.reference.Reference;

public class CreativeTabDeathCube {

	// all creative tabs should define icon & name

		public static final CreativeTabs DEATHCUBE_TAB = new CreativeTabs(Reference.MOD_ID) {

			@Override
			public Item getTabIconItem() {

				return Items.apple;
			}

			@Override
			public String getTranslatedTabLabel() {
				return "DeathCube";
			}
		};
}
