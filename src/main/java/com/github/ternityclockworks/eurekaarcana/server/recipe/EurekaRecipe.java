package com.github.ternityclockworks.eurekaarcana.server.recipe;

import com.github.ternityclockworks.eurekaarcana.EurekaArcana;
import com.github.ternityclockworks.eurekaarcana.server.recipe.EurekaRecipeBuilder.EurekaRecipeParams;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;
import java.util.stream.Collectors;

import javax.annotation.ParametersAreNonnullByDefault;

import org.slf4j.Logger;

import com.google.gson.JsonObject;

import net.minecraft.MethodsReturnNonnullByDefault;
import net.minecraft.core.NonNullList;
import net.minecraft.core.RegistryAccess;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.Container;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraftforge.fluids.FluidStack;

/**
 * Heavily based on the ProcessingRecipe class from the Create mod.
 * Used as a basis for other general recipes in this mod.
 * See <a href="https://github.com/Creators-of-Create/Create/blob/mc1.20.1/dev/src/main/java/com/simibubi/create/content/processing/recipe/ProcessingRecipe.java">Create ProcessingRecipe class</a>
 * @param <T>
 */
@MethodsReturnNonnullByDefault
@ParametersAreNonnullByDefault
public abstract class EurekaRecipe<T extends Container> implements Recipe<T> {

	protected ResourceLocation id;
	protected NonNullList<Ingredient> ingredients;
	protected NonNullList<EurekaRecipeOutput> results;
	protected int processingDuration;

	private RecipeType<?> type;
	private RecipeSerializer<?> serializer;
	private IRecipeTypeInfo typeInfo;
	private Supplier<ItemStack> forcedResult;

	public EurekaRecipe(IRecipeTypeInfo typeInfo, EurekaRecipeParams params) {
		this.forcedResult = null;
		this.typeInfo = typeInfo;
		this.processingDuration = params.processingDuration;
		this.serializer = typeInfo.getSerializer();
		this.ingredients = params.ingredients;
		this.type = typeInfo.getType();
		this.results = params.results;
		this.id = params.id;

		validate(typeInfo.getId());
	}

	// Recipe type options:

	protected abstract int getMaxInputCount();

	protected abstract int getMaxOutputCount();

	protected boolean canSpecifyDuration() {
		return false;
	}

	private void validate(ResourceLocation recipeTypeId) {
		String messageHeader = "Your custom " + recipeTypeId + " recipe (" + id.toString() + ")";
		Logger logger = EurekaArcana.LOGGER;
		int ingredientCount = ingredients.size();
		int outputCount = results.size();

		if (ingredientCount > getMaxInputCount())
			logger.warn(messageHeader + " has more item inputs (" + ingredientCount + ") than supported ("
				+ getMaxInputCount() + ").");

		if (outputCount > getMaxOutputCount())
			logger.warn(messageHeader + " has more item outputs (" + outputCount + ") than supported ("
				+ getMaxOutputCount() + ").");

		if (processingDuration > 0 && !canSpecifyDuration())
			logger.warn(messageHeader + " specified a duration. Durations have no impact on this type of recipe.");
	}

	@Override
	public NonNullList<Ingredient> getIngredients() {
		return ingredients;
	}

	public List<EurekaRecipeOutput> getRollableResults() {
		return results;
	}

	public List<ItemStack> getRollableResultsAsItemStacks() {
		return getRollableResults().stream()
			.map(EurekaRecipeOutput::getStack)
			.collect(Collectors.toList());
	}

	public void enforceNextResult(Supplier<ItemStack> stack) {
		forcedResult = stack;
	}

	public List<ItemStack> rollResults() {
		return rollResults(this.getRollableResults());
	}

	public List<ItemStack> rollResults(List<EurekaRecipeOutput> rollableResults) {
		List<ItemStack> results = new ArrayList<>();
		for (int i = 0; i < rollableResults.size(); i++) {
			EurekaRecipeOutput output = rollableResults.get(i);
			ItemStack stack = i == 0 && forcedResult != null ? forcedResult.get() : output.rollOutput();
			if (!stack.isEmpty())
				results.add(stack);
		}
		return results;
	}

	public int getProcessingDuration() {
		return processingDuration;
	}

	// IRecipe<> paperwork

	@Override
	public ItemStack assemble(T inv, RegistryAccess registryAccess) {
		return getResultItem(registryAccess);
	}

	@Override
	public boolean canCraftInDimensions(int width, int height) {
		return true;
	}

	@Override
	public ItemStack getResultItem(RegistryAccess registryAccess) {
		return getRollableResults().isEmpty() ? ItemStack.EMPTY
			: getRollableResults().get(0)
				.getStack();
	}

	@Override
	public boolean isSpecial() {
		return true;
	}

	// Processing recipes do not show up in the recipe book
	@Override
	public String getGroup() {
		return "processing";
	}

	@Override
	public ResourceLocation getId() {
		return id;
	}

	@Override
	public RecipeSerializer<?> getSerializer() {
		return serializer;
	}

	@Override
	public RecipeType<?> getType() {
		return type;
	}

	public IRecipeTypeInfo getTypeInfo() {
		return typeInfo;
	}

	// Additional Data added by subtypes

	public void readAdditional(JsonObject json) {}

	public void readAdditional(FriendlyByteBuf buffer) {}

	public void writeAdditional(JsonObject json) {}

	public void writeAdditional(FriendlyByteBuf buffer) {}

}