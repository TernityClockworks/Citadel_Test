package com.github.ternityclockworks.eurekaarcana.server.recipe.dyeing;

import com.github.ternityclockworks.eurekaarcana.server.item.EurekaJournalItem;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.CraftingBookCategory;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.SimpleCraftingRecipeSerializer;

public class JournalDyeingRecipe extends DyeingRecipe{

	public JournalDyeingRecipe(ResourceLocation recipeID, CraftingBookCategory category) {
		super(recipeID, category);
	}
	
	@Override
	protected boolean isDyeableItem(ItemStack stack) {
		return stack.getItem() instanceof EurekaJournalItem;
	}
	
	@Override
	public RecipeSerializer<?> getSerializer() {
		return new SimpleCraftingRecipeSerializer<>(JournalDyeingRecipe::new);
	}
	
}
