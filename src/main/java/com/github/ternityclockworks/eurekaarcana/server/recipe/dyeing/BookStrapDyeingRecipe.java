package com.github.ternityclockworks.eurekaarcana.server.recipe.dyeing;

import com.github.ternityclockworks.eurekaarcana.server.item.BookStrapItem;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.CraftingBookCategory;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.SimpleCraftingRecipeSerializer;

public class BookStrapDyeingRecipe extends DyeingRecipe{

	public BookStrapDyeingRecipe(ResourceLocation recipeID, CraftingBookCategory category) {
		super(recipeID, category);
	}
	
	@Override
	protected boolean isDyeableItem(ItemStack stack) {
		return stack.getItem() instanceof BookStrapItem;
	}
	
	@Override
	public RecipeSerializer<?> getSerializer() {
		return new SimpleCraftingRecipeSerializer<>(BookStrapDyeingRecipe::new);
	}
	
}
