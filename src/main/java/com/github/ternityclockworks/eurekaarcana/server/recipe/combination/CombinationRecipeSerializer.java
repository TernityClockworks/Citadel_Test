package com.github.ternityclockworks.eurekaarcana.server.recipe.combination;

import com.github.ternityclockworks.eurekaarcana.server.recipe.combination.CombinationRecipeBuilder.CombinationRecipeFactory;

import javax.annotation.ParametersAreNonnullByDefault;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import net.minecraft.MethodsReturnNonnullByDefault;
import net.minecraft.core.NonNullList;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.GsonHelper;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraftforge.fluids.FluidStack;

@MethodsReturnNonnullByDefault
@ParametersAreNonnullByDefault
public class CombinationRecipeSerializer<T extends CombinationRecipe<?>> implements RecipeSerializer<T> {

	private final CombinationRecipeFactory<T> factory;

	public CombinationRecipeSerializer(CombinationRecipeFactory<T> factory) {
		this.factory = factory;
	}

	protected void writeToJson(JsonObject json, T recipe) {
		JsonArray jsonOutputs = new JsonArray();

		recipe.recipeOutput.forEach(o -> jsonOutputs.add(o.serialize()));

		json.add("mainhandIngredient", recipe.mainhandIngredient.toJson());
		json.add("offhandIngredient", recipe.offhandIngredient.toJson());
		json.add("recipeOutput", jsonOutputs);

		int combinationDuration = recipe.getCombinationDuration();
		if (combinationDuration > 0)
			json.addProperty("combinationDuration", combinationDuration);
	}

	protected T readFromJson(ResourceLocation recipeId, JsonObject json) {
		CombinationRecipeBuilder<T> builder = new CombinationRecipeBuilder<>(factory, recipeId);
		Ingredient mainhandIngredient = Ingredient.EMPTY;
		Ingredient offhandIngredient = Ingredient.EMPTY;
		NonNullList<CombinationOutput> recipeOutput = NonNullList.create();
		
		mainhandIngredient = Ingredient.fromJson(GsonHelper.getAsJsonObject(json, "mainhandIngredient"));

		mainhandIngredient = Ingredient.fromJson(GsonHelper.getAsJsonObject(json, "offhandIngredient"));
		
		for (JsonElement je : GsonHelper.getAsJsonArray(json, "recipeOutput")) {
			recipeOutput.add(CombinationOutput.deserialize(je.getAsJsonObject()));
		}

		builder.mainhand(mainhandIngredient)
			.offhand(offhandIngredient)
			.withItemOutputs(recipeOutput);

		if (GsonHelper.isValidNode(json, "combinationTime"))
			builder.duration(GsonHelper.getAsInt(json, "combinationTime"));

		T recipe = builder.build();
		return recipe;
	}

	protected void writeToBuffer(FriendlyByteBuf buffer, T recipe) {
		Ingredient mainhandIngredient = recipe.mainhandIngredient;
		Ingredient offhandIngredient = recipe.offhandIngredient;
		NonNullList<CombinationOutput> outputs = recipe.recipeOutput;
		
		mainhandIngredient.toNetwork(buffer);
		offhandIngredient.toNetwork(buffer);
		
		buffer.writeVarInt(outputs.size());
		outputs.forEach(o -> o.write(buffer));

		buffer.writeVarInt(recipe.getCombinationDuration());
	}

	protected T readFromBuffer(ResourceLocation recipeId, FriendlyByteBuf buffer) {
		Ingredient mainhandIngredient = Ingredient.EMPTY;
		Ingredient offhandIngredient = Ingredient.EMPTY;
		NonNullList<CombinationOutput> recipeOutput = NonNullList.create();
		
		mainhandIngredient = Ingredient.fromNetwork(buffer);
		offhandIngredient = Ingredient.fromNetwork(buffer);
		
		int size = buffer.readVarInt();
		for (int i = 0; i < size; i++)
			recipeOutput.add(CombinationOutput.read(buffer));

		T recipe = new CombinationRecipeBuilder<>(factory, recipeId)
			.mainhand(mainhandIngredient)
			.offhand(offhandIngredient)
			.withItemOutputs(recipeOutput)
			.duration(buffer.readVarInt())
			.build();
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

	public CombinationRecipeFactory<T> getFactory() {
		return factory;
	}

}