package com.github.ternityclockworks.eurekaarcana.server.recipe.dyeing;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.DyeItem;
import net.minecraft.world.item.DyeableLeatherItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.CraftingBookCategory;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.SimpleCraftingRecipeSerializer;

import java.util.List;

import com.github.ternityclockworks.eurekaarcana.server.item.EurekaJournalItem;

public class JournalDyeingRecipe extends DyeingRecipe{

	public JournalDyeingRecipe(ResourceLocation recipeID, CraftingBookCategory category) {
		super(recipeID, category);
	}

	@Override
	protected boolean isDyeableItem(ItemStack stack) {
		return stack.getItem() instanceof EurekaJournalItem;
	}

	@Override
	protected ItemStack dyeItem(ItemStack dyeableStack, List<DyeItem> dyeItems) {
		return DyeableLeatherItem.dyeArmor(dyeableStack, dyeItems);
	}
	
	@Override
	public RecipeSerializer<?> getSerializer() {
		return new SimpleCraftingRecipeSerializer<>(JournalDyeingRecipe::new);
	}
	
}
