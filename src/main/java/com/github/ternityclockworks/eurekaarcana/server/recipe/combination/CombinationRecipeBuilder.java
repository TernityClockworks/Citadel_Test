package com.github.ternityclockworks.eurekaarcana.server.recipe.combination;

import com.github.ternityclockworks.eurekaarcana.server.recipe.IRecipeTypeInfo;
import com.github.ternityclockworks.eurekaarcana.util.Pair;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import net.minecraft.core.NonNullList;
import net.minecraft.data.recipes.FinishedRecipe;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.level.ItemLike;
import net.minecraftforge.common.crafting.CraftingHelper;
import net.minecraftforge.common.crafting.conditions.ICondition;
import net.minecraftforge.common.crafting.conditions.ModLoadedCondition;
import net.minecraftforge.common.crafting.conditions.NotCondition;

public class CombinationRecipeBuilder<T extends CombinationRecipe<?>> {

	protected CombinationRecipeFactory<T> factory;
	protected CombinationRecipeParams params;
	protected List<ICondition> recipeConditions;

	public CombinationRecipeBuilder(CombinationRecipeFactory<T> factory, ResourceLocation recipeId) {
		params = new CombinationRecipeParams(recipeId);
		recipeConditions = new ArrayList<>();
		this.factory = factory;
	}

	public CombinationRecipeBuilder<T> withSingleItemOutput(ItemStack output) {
		return withItemOutputs(new CombinationOutput(output, 1));
	}

	public CombinationRecipeBuilder<T> withItemOutputs(CombinationOutput... outputs) {
		return withItemOutputs(NonNullList.of(CombinationOutput.EMPTY, outputs));
	}

	public CombinationRecipeBuilder<T> withItemOutputs(NonNullList<CombinationOutput> outputs) {
		params.recipeOutput = outputs;
		return this;
	}

	public CombinationRecipeBuilder<T> duration(int ticks) {
		params.combinationDuration = ticks;
		return this;
	}

	public CombinationRecipeBuilder<T> averageCombinationDuration() {
		return duration(100);
	}

	public T build() {
		return factory.create(params);
	}

	public void build(Consumer<FinishedRecipe> consumer) {
		consumer.accept(new DataGenResult<>(build(), recipeConditions));
	}

	// Datagen shortcuts

	public CombinationRecipeBuilder<T> mainhand(TagKey<Item> tag) {
		return mainhand(Ingredient.of(tag));
	}

	public CombinationRecipeBuilder<T> mainhand(ItemLike item) {
		return mainhand(Ingredient.of(item));
	}

	public CombinationRecipeBuilder<T> mainhand(Ingredient ingredient) {
		params.mainhandIngredient = ingredient;
		return this;
	}
	
	public CombinationRecipeBuilder<T> offhand(TagKey<Item> tag) {
		return offhand(Ingredient.of(tag));
	}

	public CombinationRecipeBuilder<T> offhand(ItemLike item) {
		return offhand(Ingredient.of(item));
	}

	public CombinationRecipeBuilder<T> offhand(Ingredient ingredient) {
		params.offhandIngredient = ingredient;
		return this;
	}

//	public CombinationRecipeBuilder<T> require(Mods mod, String id) {
//		params.ingredients.add(new SimpleDatagenIngredient(mod, id));
//		return this;
//	}
//
//	public CombinationRecipeBuilder<T> require(ResourceLocation ingredient) {
//		params.ingredients.add(DataIngredient.ingredient(null, ingredient));
//		return this;
//	}

	public CombinationRecipeBuilder<T> addOutput(ItemLike item) {
		return addOutput(item, 1);
	}

	public CombinationRecipeBuilder<T> addOutput(float chance, ItemLike item) {
		return addOutput(chance, item, 1);
	}

	public CombinationRecipeBuilder<T> addOutput(ItemLike item, int amount) {
		return addOutput(1, item, amount);
	}

	public CombinationRecipeBuilder<T> addOutput(float chance, ItemLike item, int amount) {
		return addOutput(chance, new ItemStack(item, amount));
	}

	public CombinationRecipeBuilder<T> addOutput(ItemStack output) {
		return addOutput(1, output);
	}

	public CombinationRecipeBuilder<T> addOutput(float chance, ItemStack output) {
		return addOutput(new CombinationOutput(output, chance));
	}

//	public CombinationRecipeBuilder<T> output(float chance, Mods mod, String id, int amount) {
//		return output(new CombinationOutput(Pair.of(mod.asResource(id), amount), chance));
//	}

	// TODO external mod compat
//	public CombinationRecipeBuilder<T> output(Mods mod, String id) {
//		return output(1, mod.asResource(id), 1);
//	}

	public CombinationRecipeBuilder<T> addOutput(float chance, ResourceLocation registryName, int amount) {
		return addOutput(new CombinationOutput(Pair.of(registryName, amount), chance));
	}

	public CombinationRecipeBuilder<T> addOutput(CombinationOutput output) {
		params.recipeOutput.add(output);
		return this;
	}

	public CombinationRecipeBuilder<T> toolNotConsumed() {
		params.keepHeldItem = true;
		return this;
	}

	//

	public CombinationRecipeBuilder<T> whenModLoaded(String modid) {
		return withCondition(new ModLoadedCondition(modid));
	}

	public CombinationRecipeBuilder<T> whenModMissing(String modid) {
		return withCondition(new NotCondition(new ModLoadedCondition(modid)));
	}

	public CombinationRecipeBuilder<T> withCondition(ICondition condition) {
		recipeConditions.add(condition);
		return this;
	}

	@FunctionalInterface
	public interface CombinationRecipeFactory<T extends CombinationRecipe<?>> {
		T create(CombinationRecipeParams params);
	}

	public static class CombinationRecipeParams {
		
		protected ResourceLocation recipeTypeID;
		protected Ingredient mainhandIngredient;
		protected Ingredient offhandIngredient;
		protected NonNullList<CombinationOutput> recipeOutput;
		protected int combinationDuration;
		public boolean keepHeldItem;
		
		public CombinationRecipeParams( ResourceLocation recipeTypeID,
											  Ingredient mainhandIngredient,
											  Ingredient offhandIngredient,
											  NonNullList<CombinationOutput> recipeOutput,
											  int combinationDuration,
											  boolean keepHeldItem) {
			this.recipeTypeID = recipeTypeID;
			this.mainhandIngredient = mainhandIngredient;
			this.offhandIngredient = offhandIngredient;
			this.recipeOutput = recipeOutput;
			this.combinationDuration = combinationDuration;
			this.keepHeldItem = keepHeldItem;
		}
		
		public CombinationRecipeParams(ResourceLocation recipeTypeID) {
			this.recipeTypeID = recipeTypeID;
			this.mainhandIngredient = Ingredient.EMPTY;
			this.offhandIngredient = Ingredient.EMPTY;
			this.recipeOutput = NonNullList.create();
			this.combinationDuration = 0;
			this.keepHeldItem = false;
		}

	}

	public static class DataGenResult<S extends CombinationRecipe<?>> implements FinishedRecipe {

		private List<ICondition> recipeConditions;
		private CombinationRecipeSerializer<S> serializer;
		private ResourceLocation id;
		private S recipe;

		@SuppressWarnings("unchecked")
		public DataGenResult(S recipe, List<ICondition> recipeConditions) {
			this.recipe = recipe;
			this.recipeConditions = recipeConditions;
			IRecipeTypeInfo recipeType = this.recipe.getTypeInfo();
			ResourceLocation typeId = recipeType.getId();

			if (!(recipeType.getSerializer() instanceof CombinationRecipeSerializer))
				throw new IllegalStateException("Cannot datagen CombinationRecipe of type: " + typeId);

			this.id = new ResourceLocation(recipe.getId().getNamespace(),
					typeId.getPath() + "/" + recipe.getId().getPath());
			this.serializer = (CombinationRecipeSerializer<S>) recipe.getSerializer();
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