package com.github.ternityclockworks.eurekaarcana.datagen.recipes;

import com.github.ternityclockworks.eurekaarcana.server.item.EurekaItemRegistry;

import net.minecraft.advancements.critereon.InventoryChangeTrigger;
import net.minecraft.advancements.critereon.ItemPredicate;
import net.minecraft.data.PackOutput;
import net.minecraft.data.recipes.*;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.item.Items;
import net.minecraftforge.common.Tags;

import java.util.function.Consumer;

public class GenCraftingRecipes extends RecipeProvider {

    public GenCraftingRecipes(PackOutput packOutput) {
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
}