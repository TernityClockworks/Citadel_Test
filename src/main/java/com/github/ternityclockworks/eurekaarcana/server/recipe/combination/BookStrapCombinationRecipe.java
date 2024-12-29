package com.github.ternityclockworks.eurekaarcana.server.recipe.combination;

import java.util.List;

import javax.annotation.ParametersAreNonnullByDefault;

import org.jetbrains.annotations.Nullable;

import com.github.ternityclockworks.eurekaarcana.server.recipe.EurekaRecipeRegistry;
import com.github.ternityclockworks.eurekaarcana.server.recipe.RollableOutput;
import com.github.ternityclockworks.eurekaarcana.server.recipe.combination.CombinationRecipe.CombinationInv;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import net.minecraft.core.NonNullList;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.GsonHelper;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;

@ParametersAreNonnullByDefault
public class BookStrapCombinationRecipe extends CombinationRecipe<CombinationInv> {
	
	public BookStrapCombinationRecipe(
			  ResourceLocation recipeID,
			  Ingredient mainhandIngredient,
			  Ingredient offhandIngredient,
			  NonNullList<RollableOutput> recipeOutput,
			  int combinationDuration) {
		super(	recipeID,
				mainhandIngredient,
				offhandIngredient,
				recipeOutput,
				combinationDuration);
	}
	
//	public static boolean canCombine(Level world, ItemStack stack) {
//		return !getMatchingRecipes(world, stack).isEmpty();
//	}
//
//	public static ItemStack combine(Level world, ItemStack stack) {
//		List<Recipe<CombinationInv>> matchingRecipes = getMatchingRecipes(world, stack);
//		if (!matchingRecipes.isEmpty())
//			return matchingRecipes.get(0) // Craft result
//				.assemble(new CombinationInv(stack), world.registryAccess())
//				.copy();
//		return stack; // Cannot combine
//	}
//
//	public static List<Recipe<CombinationInv>> getMatchingRecipes(Level world, ItemStack stack) {
//		return world.getRecipeManager()
//			.getRecipesFor(EurekaRecipeRegistry.BOOK_STRAP_COMBINATION.getType(), new CombinationInv(stack), world);
//	}
	
	@Override
	public ResourceLocation getId() {
		return this.getId();
	}
	
	public class Serializer<R extends CombinationRecipe<?>> implements RecipeSerializer<R> {

		@SuppressWarnings("unchecked")
		@Override
		public R fromJson(ResourceLocation recipeID, JsonObject jsonObj) {
			Ingredient mainhandIngredient = Ingredient.EMPTY;
			Ingredient offhandIngredient = Ingredient.EMPTY;
			NonNullList<RollableOutput> recipeOutput = NonNullList.create();
			int combinationDuration = 0;
			
			mainhandIngredient = Ingredient.fromJson(GsonHelper.getAsJsonObject(jsonObj, "mainhandIngredient"));

			offhandIngredient = Ingredient.fromJson(GsonHelper.getAsJsonObject(jsonObj, "offhandIngredient"));
			
			for (JsonElement output : GsonHelper.getAsJsonArray(jsonObj, "recipeOutput")) {
				recipeOutput.add(RollableOutput.deserialize(output.getAsJsonObject()));
			}
			
			if (GsonHelper.isValidNode(jsonObj, "combinationTime"))
				combinationDuration = GsonHelper.getAsInt(jsonObj, "combinationTime");
			
			//recipe = recipe.create(recipeID,mainhandIngredient,offhandIngredient,recipeOutput,combinationDuration);
			
			return (R) new BookStrapCombinationRecipe(recipeID,mainhandIngredient,offhandIngredient,recipeOutput,combinationDuration);
		}

		@Override
		public @Nullable R fromNetwork(ResourceLocation p_44105_, FriendlyByteBuf p_44106_) {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public void toNetwork(FriendlyByteBuf p_44101_, R p_44102_) {
			// TODO Auto-generated method stub
			
		}
		
	}
}