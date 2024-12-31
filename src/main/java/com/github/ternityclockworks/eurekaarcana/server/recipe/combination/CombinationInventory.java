package com.github.ternityclockworks.eurekaarcana.server.recipe.combination;

import net.minecraft.world.item.ItemStack;
import net.minecraftforge.items.ItemStackHandler;
import net.minecraftforge.items.wrapper.RecipeWrapper;

public class CombinationInventory extends RecipeWrapper {
	public CombinationInventory(ItemStack stack) {
		super(new ItemStackHandler(1));
		inv.setStackInSlot(0, stack);
	}
}
