package com.github.ternityclockworks.eurekaarcana.datagen.recipes;

import com.github.ternityclockworks.eurekaarcana.EurekaArcana;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

import net.minecraft.data.PackOutput;
import net.minecraft.data.recipes.FinishedRecipe;
import net.minecraft.data.recipes.RecipeProvider;
import net.minecraft.tags.ItemTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraftforge.common.Tags;

/**
 * Uses the same recipe gen framework style as the Create mod.
 * See <a href="https://github.com/Creators-of-Create/Create/blob/mc1.20.1/dev/src/main/java/com/simibubi/create/foundation/data/recipe/CreateRecipeProvider.java"> CreateRecipeProvider</a>
 */
public abstract class EurekaRecipeProvider extends RecipeProvider {

	protected final List<GeneratedRecipe> recipes = new ArrayList<>();

	public EurekaRecipeProvider(PackOutput output) {
		super(output);
	}

	@Override
	protected void buildRecipes(Consumer<FinishedRecipe> consumer) {
		recipes.forEach(c -> c.register(consumer));
		EurekaArcana.LOGGER.info(getName() + " registered " + recipes.size() + " recipe" + (recipes.size() == 1 ? "" : "s"));
	}

	protected GeneratedRecipe register(GeneratedRecipe recipe) {
		recipes.add(recipe);
		return recipe;
	}

	@FunctionalInterface
	public interface GeneratedRecipe {
		void register(Consumer<FinishedRecipe> consumer);
	}
}