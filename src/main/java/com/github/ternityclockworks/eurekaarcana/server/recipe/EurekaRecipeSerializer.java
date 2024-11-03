package com.github.ternityclockworks.eurekaarcana.server.recipe;

import javax.annotation.ParametersAreNonnullByDefault;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import com.github.ternityclockworks.eurekaarcana.server.recipe.EurekaRecipeBuilder.AbstractRecipeFactory;

import net.minecraft.MethodsReturnNonnullByDefault;
import net.minecraft.core.NonNullList;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.GsonHelper;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeSerializer;

/**
 * Complete credit to the ProcessingRecipeSerializer class from the Create mod.
 * See <a href="https://github.com/Creators-of-Create/Create/blob/mc1.20.1/dev/src/main/java/com/simibubi/create/content/processing/recipe/ProcessingRecipeSerializer.java"> Create ProcessingRecipeSerializer class</a>
 */
@MethodsReturnNonnullByDefault
@ParametersAreNonnullByDefault
public class EurekaRecipeSerializer<T extends EurekaRecipe<?>> implements RecipeSerializer<T> {

	private final AbstractRecipeFactory<T> factory;

	public EurekaRecipeSerializer(AbstractRecipeFactory<T> factory) {
		this.factory = factory;
	}

	protected void writeToJson(JsonObject json, T recipe) {
		JsonArray jsonIngredients = new JsonArray();
		JsonArray jsonOutputs = new JsonArray();

		recipe.ingredients.forEach(i -> jsonIngredients.add(i.toJson()));

		recipe.results.forEach(o -> jsonOutputs.add(o.serialize()));

		json.add("ingredients", jsonIngredients);
		json.add("results", jsonOutputs);

		int processingDuration = recipe.getProcessingDuration();
		if (processingDuration > 0)
			json.addProperty("processingTime", processingDuration);

		recipe.writeAdditional(json);
	}

	protected T readFromJson(ResourceLocation recipeId, JsonObject json) {
		EurekaRecipeBuilder<T> builder = new EurekaRecipeBuilder<>(factory, recipeId);
		NonNullList<Ingredient> ingredients = NonNullList.create();
		NonNullList<EurekaRecipeOutput> results = NonNullList.create();

		for (JsonElement je : GsonHelper.getAsJsonArray(json, "ingredients")) {
			ingredients.add(Ingredient.fromJson(je));
		}

		for (JsonElement je : GsonHelper.getAsJsonArray(json, "results")) {
			JsonObject jsonObject = je.getAsJsonObject();
			results.add(EurekaRecipeOutput.deserialize(je));
		}

		builder.withItemIngredients(ingredients)
			.withItemOutputs(results);

		if (GsonHelper.isValidNode(json, "processingTime"))
			builder.duration(GsonHelper.getAsInt(json, "processingTime"));
		
		T recipe = builder.build();
		recipe.readAdditional(json);
		return recipe;
	}

	protected void writeToBuffer(FriendlyByteBuf buffer, T recipe) {
		NonNullList<Ingredient> ingredients = recipe.ingredients;
		NonNullList<EurekaRecipeOutput> outputs = recipe.results;

		buffer.writeVarInt(ingredients.size());
		ingredients.forEach(i -> i.toNetwork(buffer));

		buffer.writeVarInt(outputs.size());
		outputs.forEach(o -> o.write(buffer));

		buffer.writeVarInt(recipe.getProcessingDuration());

		recipe.writeAdditional(buffer);
	}

	protected T readFromBuffer(ResourceLocation recipeId, FriendlyByteBuf buffer) {
		NonNullList<Ingredient> ingredients = NonNullList.create();
		NonNullList<EurekaRecipeOutput> results = NonNullList.create();

		int size = buffer.readVarInt();
		for (int i = 0; i < size; i++)
			ingredients.add(Ingredient.fromNetwork(buffer));

		size = buffer.readVarInt();
		for (int i = 0; i < size; i++)
			results.add(EurekaRecipeOutput.read(buffer));

		T recipe = new EurekaRecipeBuilder<>(factory, recipeId).withItemIngredients(ingredients)
			.withItemOutputs(results)
			.duration(buffer.readVarInt())
			.build();
		recipe.readAdditional(buffer);
		return recipe;
	}

	public final void write(JsonObject json, T recipe) {
		writeToJson(json, recipe);
	}

	@Override
	public final T fromJson(ResourceLocation id, JsonObject json) {
		return readFromJson(id, json);
	}

	@Override
	public final void toNetwork(FriendlyByteBuf buffer, T recipe) {
		writeToBuffer(buffer, recipe);
	}

	@Override
	public final T fromNetwork(ResourceLocation id, FriendlyByteBuf buffer) {
		return readFromBuffer(id, buffer);
	}

	public AbstractRecipeFactory<T> getFactory() {
		return factory;
	}

}