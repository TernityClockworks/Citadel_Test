package com.github.ternityclockworks.eurekaarcana.datagen.recipes.combination;

import java.util.function.Consumer;

import com.github.ternityclockworks.eurekaarcana.EurekaArcana;
import com.github.ternityclockworks.eurekaarcana.datagen.EurekaItemTags;
import com.github.ternityclockworks.eurekaarcana.datagen.recipes.CombinationRecipeProvider;
import com.github.ternityclockworks.eurekaarcana.datagen.recipes.EurekaRecipeProvider.GeneratedRecipe;
import com.github.ternityclockworks.eurekaarcana.server.item.EurekaItemRegistry;
import com.github.ternityclockworks.eurekaarcana.server.recipe.EurekaRecipeRegistry;

import net.minecraft.advancements.critereon.InventoryChangeTrigger;
import net.minecraft.advancements.critereon.ItemPredicate;
import net.minecraft.data.PackOutput;
import net.minecraft.data.recipes.FinishedRecipe;
import net.minecraft.data.recipes.RecipeCategory;
import net.minecraft.data.recipes.ShapedRecipeBuilder;
import net.minecraft.data.recipes.SpecialRecipeBuilder;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.item.Items;
import net.minecraftforge.common.Tags;
import net.minecraftforge.registries.ForgeRegistries;

public class BookStrapCombinationRecipeGen extends CombinationRecipeProvider {

	GeneratedRecipe

	BOOK_STRAP = create(Items.BOOK, EurekaItemRegistry.BOOK_STRAP.get(), b -> b.addOutput(EurekaItemRegistry.EUREKA_JOURNAL.get()))

	;

	public BookStrapCombinationRecipeGen(PackOutput output) {
		super(output);
	}

	@Override
	protected EurekaRecipeRegistry getRecipeType() {
		return EurekaRecipeRegistry.BOOK_STRAP_COMBINATION;
	}
}