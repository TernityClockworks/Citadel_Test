package com.github.ternityclockworks.eurekaarcana.server.recipe.combination;

import java.util.List;

import javax.annotation.ParametersAreNonnullByDefault;

import com.github.ternityclockworks.eurekaarcana.server.recipe.EurekaRecipeRegistry;
import com.github.ternityclockworks.eurekaarcana.server.recipe.combination.ManualCombinationRecipe.CombinationInv;
import com.google.gson.JsonObject;
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
public class BookStrapCombinationRecipe extends ManualCombinationRecipe<CombinationInv> {
	
	public BookStrapCombinationRecipe(ManualCombinationRecipeParams params) {
		super(EurekaRecipeRegistry.BOOK_STRAP_COMBINATION, params);
	}
	
	public static boolean canCombine(Level world, ItemStack stack) {
		return !getMatchingRecipes(world, stack).isEmpty();
	}

	public static ItemStack combine(Level world, Vec3 position, ItemStack offhandItem, ItemStack mainhandItem) {
		List<Recipe<CombinationInv>> matchingRecipes = getMatchingRecipes(world, offhandItem);
		if (!matchingRecipes.isEmpty())
			return matchingRecipes.get(0) // Craft result
				.assemble(new CombinationInv(offhandItem), world.registryAccess())
				.copy();
		return offhandItem; // Cannot combine
	}

	public static List<Recipe<CombinationInv>> getMatchingRecipes(Level world, ItemStack stack) {
		return world.getRecipeManager()
			.getRecipesFor(EurekaRecipeRegistry.BOOK_STRAP_COMBINATION.getType(), new CombinationInv(stack), world);
	}
	
	@Override
	public ResourceLocation getId() {
		return this.getId();
	}

	public static class Serializer implements RecipeSerializer<BookStrapCombinationRecipe> {
        
		public BookStrapCombinationRecipe fromJson(ResourceLocation recipeID, JsonObject recipeJson) {
            Ingredient mainhandIngredient = Ingredient.fromJson(GsonHelper.getAsJsonObject(recipeJson, "mainhandIngredient"));
            Ingredient offhandIngredient = Ingredient.fromJson(GsonHelper.getAsJsonObject(recipeJson, "offhandIngredient"));
            int combinationDuration = GsonHelper.getAsInt(recipeJson, "combinationDuration");
            return new BookStrapCombinationRecipe( new ManualCombinationRecipeParams(recipeID,
            																		 mainhandIngredient,
            																		 offhandIngredient,
            																		 combinationDuration));
        }

        public BookStrapCombinationRecipe fromNetwork(ResourceLocation recipeID, FriendlyByteBuf buf) {
            Ingredient mainhandIngredient = Ingredient.fromNetwork(buf);
            Ingredient offhandIngredient = Ingredient.fromNetwork(buf);
            int combinationDuration = buf.readInt();
            return new BookStrapCombinationRecipe( new ManualCombinationRecipeParams(recipeID,
					  																 mainhandIngredient,
					  																 offhandIngredient,
					  																 combinationDuration));
        }

        public void toNetwork(FriendlyByteBuf buf, BookStrapCombinationRecipe recipe) {
            recipe.mainhandIngredient.toNetwork(buf);
            recipe.offhandIngredient.toNetwork(buf);
            buf.writeInt(recipe.combinationDuration);
        }
    }
}