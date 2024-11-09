package com.github.ternityclockworks.eurekaarcana.server.recipe.combination;

import com.github.ternityclockworks.eurekaarcana.server.recipe.EurekaRecipeRegistry;
import com.github.ternityclockworks.eurekaarcana.server.recipe.combination.ManualCombinationRecipe.CombinationInv;

import java.util.List;

import javax.annotation.ParametersAreNonnullByDefault;

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
public class MechanicalDisassemblyRecipe extends ManualCombinationRecipe<CombinationInv> {
	
	public MechanicalDisassemblyRecipe(ManualCombinationRecipeParams params) {
		super(EurekaRecipeRegistry.MECHANICAL_DISASSEMBLY, params);
	}
	
	public static boolean canCombine(Level world, ItemStack stack) {
		return !getMatchingRecipes(world, stack).isEmpty();
	}

	public static ItemStack combine(Level world, Vec3 position, ItemStack stack, ItemStack sandPaperStack) {
		List<Recipe<CombinationInv>> matchingRecipes = getMatchingRecipes(world, stack);
		if (!matchingRecipes.isEmpty())
			return matchingRecipes.get(0)
				.assemble(new CombinationInv(stack), world.registryAccess())
				.copy();
		return stack;
	}

	public static List<Recipe<CombinationInv>> getMatchingRecipes(Level world, ItemStack stack) {
		return world.getRecipeManager()
			.getRecipesFor(EurekaRecipeRegistry.MECHANICAL_DISASSEMBLY.getType(), new CombinationInv(stack), world);
	}
	
	public NonNullList<ItemStack> rollRecipeOutput() {
		// TODO Auto-generated method stub
		return null;
	}
	
	@Override
	public ResourceLocation getId() {
		return this.getId();
	}

	public static class Serializer implements RecipeSerializer<MechanicalDisassemblyRecipe> {
        
		public MechanicalDisassemblyRecipe fromJson(ResourceLocation recipeID, JsonObject recipeJson) {
            Ingredient mainhandIngredient = Ingredient.fromJson(GsonHelper.getAsJsonObject(recipeJson, "mainhandIngredient"));
            Ingredient offhandIngredient = Ingredient.fromJson(GsonHelper.getAsJsonObject(recipeJson, "offhandIngredient"));
            int combinationDuration = GsonHelper.getAsInt(recipeJson, "combinationDuration");
            return new MechanicalDisassemblyRecipe( new ManualCombinationRecipeParams(recipeID,
            																	  mainhandIngredient,
            																	  offhandIngredient,
            																	  combinationDuration));
        }

        public MechanicalDisassemblyRecipe fromNetwork(ResourceLocation recipeID, FriendlyByteBuf buf) {
            Ingredient mainhandIngredient = Ingredient.fromNetwork(buf);
            Ingredient offhandIngredient = Ingredient.fromNetwork(buf);
            int combinationDuration = buf.readInt();
            return new MechanicalDisassemblyRecipe( new ManualCombinationRecipeParams(recipeID,
					  mainhandIngredient,
					  offhandIngredient,
					  combinationDuration));
        }

        public void toNetwork(FriendlyByteBuf buf, MechanicalDisassemblyRecipe recipe) {
            recipe.mainhandIngredient.toNetwork(buf);
            recipe.offhandIngredient.toNetwork(buf);
            buf.writeInt(recipe.combinationDuration);
        }
    }

}