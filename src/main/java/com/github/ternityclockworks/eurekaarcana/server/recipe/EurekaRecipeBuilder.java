package com.github.ternityclockworks.eurekaarcana.server.recipe;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import com.github.ternityclockworks.eurekaarcana.server.recipe.IRecipeTypeInfo;
import com.github.ternityclockworks.eurekaarcana.util.Pair;
//import com.tterrag.registrate.util.DataIngredient;

import net.minecraft.core.NonNullList;
import net.minecraft.data.recipes.FinishedRecipe;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.material.Fluid;
import net.minecraftforge.common.crafting.CraftingHelper;
import net.minecraftforge.common.crafting.conditions.ICondition;
import net.minecraftforge.common.crafting.conditions.ModLoadedCondition;
import net.minecraftforge.common.crafting.conditions.NotCondition;
import net.minecraftforge.fluids.FluidStack;

public class EurekaRecipeBuilder<T extends EurekaRecipe<?>> {

	protected AbstractRecipeFactory<T> factory;
	protected EurekaRecipeParams params;
	protected List<ICondition> recipeConditions;

	public EurekaRecipeBuilder(AbstractRecipeFactory<T> factory, ResourceLocation recipeId) {
		params = new EurekaRecipeParams(recipeId);
		recipeConditions = new ArrayList<>();
		this.factory = factory;
	}

	public EurekaRecipeBuilder<T> withItemIngredients(Ingredient... ingredients) {
		return withItemIngredients(NonNullList.of(Ingredient.EMPTY, ingredients));
	}

	public EurekaRecipeBuilder<T> withItemIngredients(NonNullList<Ingredient> ingredients) {
		params.ingredients = ingredients;
		return this;
	}

	public EurekaRecipeBuilder<T> withSingleItemOutput(ItemStack output) {
		return withItemOutputs(new EurekaRecipeOutput(output, 1));
	}

	public EurekaRecipeBuilder<T> withItemOutputs(EurekaRecipeOutput... outputs) {
		return withItemOutputs(NonNullList.of(EurekaRecipeOutput.EMPTY, outputs));
	}

	public EurekaRecipeBuilder<T> withItemOutputs(NonNullList<EurekaRecipeOutput> outputs) {
		params.results = outputs;
		return this;
	}

	public EurekaRecipeBuilder<T> duration(int ticks) {
		params.processingDuration = ticks;
		return this;
	}

	public EurekaRecipeBuilder<T> averageProcessingDuration() {
		return duration(100);
	}
	
	public T build() {
		return factory.create(params);
	}

	public void build(Consumer<FinishedRecipe> consumer) {
		consumer.accept(new DataGenResult<>(build(), recipeConditions));
	}

	// Datagen shortcuts

	public EurekaRecipeBuilder<T> require(TagKey<Item> tag) {
		return require(Ingredient.of(tag));
	}

	public EurekaRecipeBuilder<T> require(ItemLike item) {
		return require(Ingredient.of(item));
	}

	public EurekaRecipeBuilder<T> require(Ingredient ingredient) {
		params.ingredients.add(ingredient);
		return this;
	}

	/*
	 * Possible TODO: External mod compat
	public EurekaRecipeBuilder<T> require(Mods mod, String id) {
		params.ingredients.add(new SimpleDatagenIngredient(mod, id));
		return this;
	}

	public EurekaRecipeBuilder<T> require(ResourceLocation ingredient) {
		params.ingredients.add(DataIngredient.ingredient(null, ingredient));
		return this;
	}
	*/

	public EurekaRecipeBuilder<T> output(ItemLike item) {
		return output(item, 1);
	}

	public EurekaRecipeBuilder<T> output(float chance, ItemLike item) {
		return output(chance, item, 1);
	}

	public EurekaRecipeBuilder<T> output(ItemLike item, int amount) {
		return output(1, item, amount);
	}

	public EurekaRecipeBuilder<T> output(float chance, ItemLike item, int amount) {
		return output(chance, new ItemStack(item, amount));
	}

	public EurekaRecipeBuilder<T> output(ItemStack output) {
		return output(1, output);
	}

	public EurekaRecipeBuilder<T> output(float chance, ItemStack output) {
		return output(new EurekaRecipeOutput(output, chance));
	}

	/*
	 * Possible TODO: External mod compat 
	public EurekaRecipeBuilder<T> output(float chance, Mods mod, String id, int amount) {
		return output(new EurekaRecipeOutput(Pair.of(mod.asResource(id), amount), chance));
	}

	public EurekaRecipeBuilder<T> output(Mods mod, String id) {
		return output(1, mod.asResource(id), 1);
	}
	*/

	public EurekaRecipeBuilder<T> output(float chance, ResourceLocation registryName, int amount) {
		return output(new EurekaRecipeOutput(Pair.of(registryName, amount), chance));
	}

	public EurekaRecipeBuilder<T> output(EurekaRecipeOutput output) {
		params.results.add(output);
		return this;
	}
	
	public EurekaRecipeBuilder<T> toolNotConsumed() {
		params.keepHeldItem = true;
		return this;
	}

	public EurekaRecipeBuilder<T> whenModLoaded(String modid) {
		return withCondition(new ModLoadedCondition(modid));
	}

	public EurekaRecipeBuilder<T> whenModMissing(String modid) {
		return withCondition(new NotCondition(new ModLoadedCondition(modid)));
	}

	public EurekaRecipeBuilder<T> withCondition(ICondition condition) {
		recipeConditions.add(condition);
		return this;
	}

	@FunctionalInterface
	public interface AbstractRecipeFactory<T extends EurekaRecipe<?>> {
		T create(EurekaRecipeParams params);
	}

	public static class EurekaRecipeParams {

		protected ResourceLocation id;
		protected NonNullList<Ingredient> ingredients;
		protected NonNullList<EurekaRecipeOutput> results;
		protected int processingDuration;

		public boolean keepHeldItem;

		protected EurekaRecipeParams(ResourceLocation id) {
			this.id = id;
			ingredients = NonNullList.create();
			results = NonNullList.create();
			processingDuration = 0;
			keepHeldItem = false;
		}

	}

	public static class DataGenResult<S extends EurekaRecipe<?>> implements FinishedRecipe {

		private List<ICondition> recipeConditions;
		private EurekaRecipeSerializer<S> serializer;
		private ResourceLocation id;
		private S recipe;

		@SuppressWarnings("unchecked")
		public DataGenResult(S recipe, List<ICondition> recipeConditions) {
			this.recipe = recipe;
			this.recipeConditions = recipeConditions;
			IRecipeTypeInfo recipeType = this.recipe.getTypeInfo();
			ResourceLocation typeId = recipeType.getId();

			if (!(recipeType.getSerializer() instanceof EurekaRecipeSerializer))
				throw new IllegalStateException("Cannot datagen EurekaRecipe of type: " + typeId);

			this.id = new ResourceLocation(recipe.getId().getNamespace(),
					typeId.getPath() + "/" + recipe.getId().getPath());
			this.serializer = (EurekaRecipeSerializer<S>) recipe.getSerializer();
		}

		@Override
		public void serializeRecipeData(JsonObject json) {
			serializer.write(json, recipe);
			if (recipeConditions.isEmpty())
				return;

			JsonArray conds = new JsonArray();
			recipeConditions.forEach(c -> conds.add(CraftingHelper.serialize(c)));
			json.add("conditions", conds);
		}

		@Override
		public ResourceLocation getId() {
			return id;
		}

		@Override
		public RecipeSerializer<?> getType() {
			return serializer;
		}

		@Override
		public JsonObject serializeAdvancement() {
			return null;
		}

		@Override
		public ResourceLocation getAdvancementId() {
			return null;
		}

	}

}