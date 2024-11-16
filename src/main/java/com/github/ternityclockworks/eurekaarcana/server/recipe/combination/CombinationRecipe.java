package com.github.ternityclockworks.eurekaarcana.server.recipe.combination;

import javax.annotation.ParametersAreNonnullByDefault;

import com.github.ternityclockworks.eurekaarcana.server.recipe.IRecipeTypeInfo;
import com.github.ternityclockworks.eurekaarcana.server.recipe.combination.CombinationRecipeBuilder.CombinationRecipeParams;

import net.minecraft.world.Container;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraftforge.items.ItemStackHandler;
import net.minecraftforge.items.wrapper.RecipeWrapper;
import net.minecraft.core.NonNullList;
import net.minecraft.core.RegistryAccess;
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
public abstract class CombinationRecipe<T extends Container> implements Recipe<T>{

	protected ResourceLocation recipeID; // Resource for both the recipe and loot table
	protected Ingredient mainhandIngredient;
	protected Ingredient offhandIngredient;
	protected NonNullList<CombinationOutput> recipeOutput = NonNullList.create();;
	protected int combinationDuration;

	private RecipeType<?> type;
	private RecipeSerializer<?> serializer;
	private IRecipeTypeInfo typeInfo;
	
	public CombinationRecipe( IRecipeTypeInfo typeInfo, CombinationRecipeParams params) {
		this.typeInfo = typeInfo;
		this.serializer = typeInfo.getSerializer();
		this.type = typeInfo.getType();
		
		this.recipeID = params.recipeTypeID;
		this.mainhandIngredient = params.mainhandIngredient;
		this.offhandIngredient = params.offhandIngredient;
		this.combinationDuration = params.combinationDuration;
	}
	
	@Override
	public NonNullList<Ingredient> getIngredients() {
		NonNullList<Ingredient> ingredients = NonNullList.create();
		ingredients.add(mainhandIngredient);
		ingredients.add(offhandIngredient);
		return ingredients;
	}
	
	public ResourceLocation getRecipeTypeID() {
		return recipeID;
	}
	
	public Ingredient getMainhandIngredient() {
		return mainhandIngredient;
	}
	
	public Ingredient getOffhandIngredient() {
		return offhandIngredient;
	}
	
	public int getCombinationDuration() {
		return combinationDuration;
	}
	
	public LootTable getLootTable() {
		LootTable table = LootTable.EMPTY;
		table.setLootTableId(recipeID);
		return table;
	}
	
	public NonNullList<CombinationOutput> getRecipeOutput() {
		return recipeOutput;
	}
	
	@Override
	public ItemStack getResultItem(RegistryAccess reg) {
		// TODO roll output
		return ItemStack.EMPTY;
	}

	@Override
	public boolean matches(T inv, Level worldIn) {
		return mainhandIngredient.test(inv.getItem(0));
	}
	
	@Override
	public ItemStack assemble(T inv, RegistryAccess registryAccess) {
		return getResultItem(registryAccess);
	}

	@Override
	public boolean canCraftInDimensions(int width, int height) {
		return true;
	}

	// TODO roll results from loot table
//	@Override
//	public ItemStack getResultItem(RegistryAccess registryAccess) {
//		return recipeOutput.get(0);
//	}
	
	public void setRecipeOutput(NonNullList<CombinationOutput> recipeOutput) {
		this.recipeOutput = recipeOutput;
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
	
	public static class CombinationInv extends RecipeWrapper {
		// Player inventory main hand
		public CombinationInv(ItemStack stack) {
			super(new ItemStackHandler(1));
			inv.setStackInSlot(0, stack);
		}

	}
}