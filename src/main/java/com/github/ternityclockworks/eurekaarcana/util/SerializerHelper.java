package com.github.ternityclockworks.eurekaarcana.util;

import com.github.ternityclockworks.eurekaarcana.server.recipe.EurekaRecipeCategory;
import com.github.ternityclockworks.eurekaarcana.server.recipe.RollableItem;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import net.minecraft.core.NonNullList;
import net.minecraft.util.GsonHelper;
import net.minecraft.world.item.crafting.Ingredient;

public class SerializerHelper {
	
	public static Ingredient ingredientFromJson(JsonObject jsonObj, String fieldName) {
		return Ingredient.fromJson(GsonHelper.getAsJsonObject(jsonObj, fieldName));
	}
	
	public static NonNullList<Ingredient> ingredientListFromJson(JsonObject jsonObj, String fieldName) {
		NonNullList<Ingredient> ingredientList = NonNullList.create();
		for (JsonElement ingredient : GsonHelper.getAsJsonArray(jsonObj, fieldName)) {
			ingredientList.add(Ingredient.fromJson(ingredient));
		}
		return ingredientList;
	}
	
	public static NonNullList<RollableItem> rollableOutputListFromJson(JsonObject jsonObj) {
		NonNullList<RollableItem> outputList = NonNullList.create();
		for (JsonElement output : GsonHelper.getAsJsonArray(jsonObj, "recipeOutput")) {
			outputList.add(RollableItem.fromJson(output.getAsJsonObject()));
		}
		return outputList;
	}
	
	public static NonNullList<RollableItem> rollableOutputListFromJson(JsonObject jsonObj, String fieldName) {
		NonNullList<RollableItem> outputList = NonNullList.create();
		for (JsonElement output : GsonHelper.getAsJsonArray(jsonObj, fieldName)) {
			outputList.add(RollableItem.fromJson(output.getAsJsonObject()));
		}
		return outputList;
	}
	
	public static EurekaRecipeCategory recipeCategoryFromJson(JsonObject jsonObj) {
		String category = GsonHelper.getAsString(jsonObj, "recipeCategory");
		return EurekaRecipeCategory.fromString(category);
	}
	
	public static EurekaRecipeCategory recipeCategoryFromJson(JsonObject jsonObj, String fieldName) {
		String category = GsonHelper.getAsString(jsonObj, fieldName);
		return EurekaRecipeCategory.fromString(category);
	}
}
