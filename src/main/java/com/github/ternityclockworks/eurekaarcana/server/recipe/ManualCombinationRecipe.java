package com.github.ternityclockworks.eurekaarcana.server.recipe;

import javax.annotation.ParametersAreNonnullByDefault;

import com.google.gson.JsonObject;

import net.minecraft.world.Container;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.items.ItemStackHandler;
import net.minecraftforge.items.wrapper.RecipeWrapper;
import net.minecraft.core.NonNullList;
import net.minecraft.core.RegistryAccess;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;

/**
 * Basic template for all manual combination recipes.
 * Manual combination recipes refer to those which allow the player to craft by using an item in their main hand with an item in their off hand.
 * Based on Create's ProcessingRecipe abstract class.
 * @param <T>
 */
@ParametersAreNonnullByDefault
public abstract class ManualCombinationRecipe<T extends Container> implements Recipe<T>{

	protected ResourceLocation recipeTypeID;
	protected Ingredient offhandIngredient;
	protected Ingredient mainhandIngredient;
	protected LootTable recipeLootTable;
	protected NonNullList<ItemStack> recipeOutput;
	protected int combinationDuration;

	private RecipeType<?> type;
	private RecipeSerializer<?> serializer;
	private IRecipeTypeInfo typeInfo;
	
	public ManualCombinationRecipe( IRecipeTypeInfo typeInfo, ManualCombinationRecipeParams params) {
		this.typeInfo = typeInfo;
		this.serializer = typeInfo.getSerializer();
		this.type = typeInfo.getType();
		
		this.recipeTypeID = params.recipeTypeID;
		this.offhandIngredient = params.offhandIngredient;
		this.mainhandIngredient = params.mainhandIngredient;
		this.recipeLootTable = params.recipeLootTable;
		this.recipeOutput = params.recipeOutput;
		this.combinationDuration = params.combinationDuration;
	}
	
	@Override
	public NonNullList<Ingredient> getIngredients() {
		NonNullList<Ingredient> ingredients = NonNullList.create();
		ingredients.add(offhandIngredient);
		ingredients.add(mainhandIngredient);
		return ingredients;
	}
	
	public ResourceLocation getRecipeTypeID() {
		return recipeTypeID;
	}
	
	public Ingredient getOffhandIngredient() {
		return offhandIngredient;
	}
	
	public LootTable getLootTable() {
		return recipeLootTable;
	}
	
	public NonNullList<ItemStack> getRecipeOutput() {
		return recipeOutput;
	}
	
	public abstract NonNullList<ItemStack> rollRecipeOutput();

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
		return recipeOutput.get(0);
	}

	@Override
	public boolean isSpecial() {
		return true;
	}

	// Manual combination recipes do not show up in the recipe book
	@Override
	public String getGroup() {
		return "manual_combination";
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
	
	public static class ManualCombinationRecipeParams {
		protected ResourceLocation recipeTypeID;
		protected Ingredient offhandIngredient;
		protected Ingredient mainhandIngredient;
		protected LootTable recipeLootTable;
		protected NonNullList<ItemStack> recipeOutput;
		protected int combinationDuration;
		
		public ManualCombinationRecipeParams( ResourceLocation recipeTypeID,
											  Ingredient offhandIngredient,
											  Ingredient mainhandIngredient,
											  LootTable recipeLootTable,
											  NonNullList<ItemStack> recipeOutput,
											  int combinationDuration) {
			this.recipeTypeID = recipeTypeID;
			this.offhandIngredient = offhandIngredient;
			this.mainhandIngredient = mainhandIngredient;
			this.recipeLootTable = recipeLootTable;
			this.recipeOutput = recipeOutput;
			this.combinationDuration = combinationDuration;
		}
		
		public ManualCombinationRecipeParams(ResourceLocation recipeTypeID) {
			this.recipeTypeID = recipeTypeID;
			this.offhandIngredient = Ingredient.EMPTY;
			this.mainhandIngredient = Ingredient.EMPTY;
			this.recipeLootTable = LootTable.EMPTY;
			this.recipeOutput = NonNullList.create();
			this.combinationDuration = 0;
		}
		
	}
	
	

}