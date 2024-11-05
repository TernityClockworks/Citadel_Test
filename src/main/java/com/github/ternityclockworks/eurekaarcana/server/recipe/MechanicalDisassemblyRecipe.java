package com.github.ternityclockworks.eurekaarcana.server.recipe;

import java.util.List;

import javax.annotation.ParametersAreNonnullByDefault;

import com.github.ternityclockworks.eurekaarcana.server.recipe.ManualCombinationRecipe.ManualCombinationRecipeParams;
import com.github.ternityclockworks.eurekaarcana.server.recipe.MechanicalDisassemblyRecipe.CombinationInv;
import com.github.ternityclockworks.eurekaarcana.server.recipe.EurekaRecipeTypes;

import net.minecraft.core.NonNullList;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.items.ItemStackHandler;
import net.minecraftforge.items.wrapper.RecipeWrapper;

@ParametersAreNonnullByDefault
public class MechanicalDisassemblyRecipe extends ManualCombinationRecipe<CombinationInv> {
	
	public MechanicalDisassemblyRecipe(ManualCombinationRecipeParams params) {
		super(EurekaRecipeTypes.MECHANICAL_DISASSEMBLY, params);
	}

	@Override
	public boolean matches(CombinationInv inv, Level worldIn) {
		return mainhandIngredient.test(inv.getItem(0));
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
			.getRecipesFor(EurekaRecipeTypes.MECHANICAL_DISASSEMBLY.getType(), new CombinationInv(stack), world);
	}
	
	@Override
	public NonNullList<ItemStack> rollRecipeOutput() {
		// TODO Auto-generated method stub
		return null;
	}

	public static class CombinationInv extends RecipeWrapper {
		// Player inventory main hand
		public CombinationInv(ItemStack stack) {
			super(new ItemStackHandler(1));
			inv.setStackInSlot(0, stack);
		}

	}
	
	@Override
	public ResourceLocation getId() {
		return this.getId();
	}

	

}