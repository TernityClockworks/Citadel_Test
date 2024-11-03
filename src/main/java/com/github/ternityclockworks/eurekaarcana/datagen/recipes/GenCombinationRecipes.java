package com.github.ternityclockworks.eurekaarcana.datagen.recipes;

import java.util.function.Consumer;

import com.github.ternityclockworks.eurekaarcana.server.item.EurekaItemRegistry;
import com.github.ternityclockworks.eurekaarcana.server.recipe.EurekaRecipe;
import com.github.ternityclockworks.eurekaarcana.server.recipe.EurekaRecipeTypes;

import net.minecraft.advancements.critereon.InventoryChangeTrigger;
import net.minecraft.advancements.critereon.ItemPredicate;
import net.minecraft.data.PackOutput;
import net.minecraft.data.recipes.FinishedRecipe;
import net.minecraft.data.recipes.RecipeCategory;
import net.minecraft.data.recipes.RecipeProvider;
import net.minecraft.data.recipes.ShapelessRecipeBuilder;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.item.Item;
import net.minecraftforge.registries.RegistryObject;

public class GenCombinationRecipes extends RecipeProvider {
	
	Item BROKEN_MECHANISM = EurekaItemRegistry.BROKEN_MECHANISM.get();

	public GenCombinationRecipes(PackOutput packOutput) {
		super(packOutput);
	}
	
    @Override
    protected void buildRecipes(Consumer<FinishedRecipe> consumer) {
        ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, EurekaItemRegistry.EUREKA_JOURNAL.get())
                .requires(ItemTags.LECTERN_BOOKS)
                .requires(ItemTags.FLOWERS)
                .unlockedBy("has_book", InventoryChangeTrigger.TriggerInstance.hasItems(
                        ItemPredicate.Builder.item().of(ItemTags.LECTERN_BOOKS).build()))
                .save(consumer);
    }

	protected EurekaRecipeTypes getRecipeType() {
		return EurekaRecipeTypes.MANUAL_COMBINATION;
	}

}