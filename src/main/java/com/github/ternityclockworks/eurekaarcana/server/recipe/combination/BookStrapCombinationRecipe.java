package com.github.ternityclockworks.eurekaarcana.server.recipe.combination;

import java.util.List;

import javax.annotation.ParametersAreNonnullByDefault;

import com.github.ternityclockworks.eurekaarcana.server.recipe.EurekaRecipeRegistry;
import com.github.ternityclockworks.eurekaarcana.server.recipe.combination.CombinationRecipe.CombinationInv;
import com.github.ternityclockworks.eurekaarcana.server.recipe.combination.CombinationRecipeBuilder.CombinationRecipeParams;
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
public class BookStrapCombinationRecipe extends CombinationRecipe<CombinationInv> {
	
	public BookStrapCombinationRecipe(CombinationRecipeParams params) {
		super(EurekaRecipeRegistry.BOOK_STRAP_COMBINATION, params);
	}
	
	public static boolean canCombine(Level world, ItemStack stack) {
		return !getMatchingRecipes(world, stack).isEmpty();
	}

	public static ItemStack combine(Level world, ItemStack stack) {
		List<Recipe<CombinationInv>> matchingRecipes = getMatchingRecipes(world, stack);
		if (!matchingRecipes.isEmpty())
			return matchingRecipes.get(0) // Craft result
				.assemble(new CombinationInv(stack), world.registryAccess())
				.copy();
		return stack; // Cannot combine
	}

	public static List<Recipe<CombinationInv>> getMatchingRecipes(Level world, ItemStack stack) {
		return world.getRecipeManager()
			.getRecipesFor(EurekaRecipeRegistry.BOOK_STRAP_COMBINATION.getType(), new CombinationInv(stack), world);
	}
	
	@Override
	public ResourceLocation getId() {
		return this.getId();
	}
}