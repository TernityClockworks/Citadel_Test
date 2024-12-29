package com.github.ternityclockworks.eurekaarcana.server.recipe.combination;

import javax.annotation.ParametersAreNonnullByDefault;

import org.jetbrains.annotations.Nullable;

import com.github.ternityclockworks.eurekaarcana.server.recipe.RollableOutput;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

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
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.GsonHelper;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;

/**
 * Basic template for all manual combination recipes.
 * Manual combination recipes refer to those which allow the player to craft by using an item in their main hand with an item in their off hand.
 * Based on Create's ProcessingRecipe abstract class.
 */
@ParametersAreNonnullByDefault
public class CombinationRecipe<T extends Container> implements Recipe<T>{

	protected ResourceLocation recipeID; // Resource for both the recipe and loot table
	protected Ingredient mainhandIngredient = Ingredient.EMPTY;
	protected Ingredient offhandIngredient = Ingredient.EMPTY;
	protected NonNullList<RollableOutput> recipeOutput = NonNullList.create();;
	protected int combinationDuration = 0;
	protected boolean enforceItemOrder = false;

	private RecipeType<?> type;
	private RecipeSerializer<?> serializer;
	
	public CombinationRecipe( ResourceLocation recipeID,
							  Ingredient mainhandIngredient,
							  Ingredient offhandIngredient,
							  NonNullList<RollableOutput> recipeOutput,
							  int combinationDuration) {
		this.recipeID = recipeID;
		this.mainhandIngredient = mainhandIngredient;
		this.offhandIngredient = offhandIngredient;
		this.recipeOutput = recipeOutput;
		this.combinationDuration = combinationDuration;
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
	
	public NonNullList<RollableOutput> getRecipeOutput() {
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
	
	public void setRecipeOutput(NonNullList<RollableOutput> recipeOutput) {
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
	
	public void setMainhand(Ingredient mainhand) {
		this.mainhandIngredient = mainhand;
	}
	
	public void setOffhand(Ingredient offhand) {
		this.offhandIngredient = offhand;
	}
	
	public void setIngredients(Ingredient mainhand, Ingredient offhand) {
		setMainhand(mainhand);setOffhand(offhand);
	}
	
	public static class CombinationInv extends RecipeWrapper {
		public CombinationInv(ItemStack stack) {
			super(new ItemStackHandler(1));
			inv.setStackInSlot(0, stack);
		}

	}
	
	public abstract class Serializer<R extends CombinationRecipe<?>> implements RecipeSerializer<R> {

		@Override
		public abstract R fromJson(ResourceLocation recipeID, JsonObject jsonObj);
//			Ingredient mainhandIngredient = Ingredient.EMPTY;
//			Ingredient offhandIngredient = Ingredient.EMPTY;
//			NonNullList<RollableOutput> recipeOutput = NonNullList.create();
//			int combinationDuration = 0;
//			
//			mainhandIngredient = Ingredient.fromJson(GsonHelper.getAsJsonObject(jsonObj, "mainhandIngredient"));
//
//			offhandIngredient = Ingredient.fromJson(GsonHelper.getAsJsonObject(jsonObj, "offhandIngredient"));
//			
//			for (JsonElement output : GsonHelper.getAsJsonArray(jsonObj, "recipeOutput")) {
//				recipeOutput.add(RollableOutput.deserialize(output.getAsJsonObject()));
//			}
//			
//			if (GsonHelper.isValidNode(jsonObj, "combinationTime"))
//				combinationDuration = GsonHelper.getAsInt(jsonObj, "combinationTime");
//			
//			recipe = recipe.create(recipeID,mainhandIngredient,offhandIngredient,recipeOutput,combinationDuration);
//			
//			return recipe;
		
		public void toJson(JsonObject json, R recipe) {
			JsonArray jsonOutputs = new JsonArray();

			recipe.recipeOutput.forEach(o -> jsonOutputs.add(o.serialize()));

			json.add("mainhandIngredient", recipe.mainhandIngredient.toJson());
			json.add("offhandIngredient", recipe.offhandIngredient.toJson());
			json.add("recipeOutput", jsonOutputs);

			int combinationDuration = recipe.getCombinationDuration();
			if (combinationDuration > 0)
				json.addProperty("combinationDuration", combinationDuration);
		}

		@Override
		public @Nullable R fromNetwork(ResourceLocation recipeID, FriendlyByteBuf buf) {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public void toNetwork(FriendlyByteBuf buf, R recipe) {
			// TODO Auto-generated method stub
			
		}

        


    }

	@Override
	public ResourceLocation getId() {
		// TODO Auto-generated method stub
		return null;
	}
}