package com.github.ternityclockworks.eurekaarcana.datagen.recipes;

import com.github.ternityclockworks.eurekaarcana.EurekaArcana;
import com.github.ternityclockworks.eurekaarcana.server.item.EurekaItemRegistry;
import com.github.ternityclockworks.eurekaarcana.server.recipe.EurekaRecipeCategory;
import com.github.ternityclockworks.eurekaarcana.server.recipe.combination.CombinationRecipe;
import com.github.ternityclockworks.eurekaarcana.server.recipe.combination.CombinationRecipe.Builder;
import com.github.ternityclockworks.eurekaarcana.server.recipe.combination.CombinationRecipe.CombinationInv;

import net.minecraft.advancements.critereon.InventoryChangeTrigger;
import net.minecraft.advancements.critereon.ItemPredicate;
import net.minecraft.data.PackOutput;
import net.minecraft.data.recipes.*;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraftforge.common.Tags;

import java.util.function.Consumer;

public class CraftingRecipeProvider extends EurekaRecipeProvider {

    public CraftingRecipeProvider(PackOutput packOutput) {
        super(packOutput);
    }

    @Override
    protected void buildRecipes(Consumer<FinishedRecipe> consumer) {
    	ShapedRecipeBuilder.shaped(RecipeCategory.MISC, EurekaItemRegistry.BOOK_STRAP.get())
    		.pattern("GL")
    		.define('L', Tags.Items.LEATHER)
    		.define('G', Tags.Items.INGOTS_GOLD)
    		.group("eureka")
    		.unlockedBy("has_gold", InventoryChangeTrigger.TriggerInstance.hasItems(
                    ItemPredicate.Builder.item().of(Tags.Items.INGOTS_GOLD).build()))
            .save(consumer);
        
        SpecialRecipeBuilder.special(EurekaRecipeCategory.BOOK_STRAP_DYEING.getSerializer()).save(consumer, EurekaArcana.MODID + ":" + "book_strap_dyeing");
        SpecialRecipeBuilder.special(EurekaRecipeCategory.JOURNAL_DYEING.getSerializer()).save(consumer, EurekaArcana.MODID + ":" + "journal_dyeing");
        
//        CombinationRecipe.Builder<CombinationRecipe<CombinationRecipe.CombinationInv>> CombinationRecipeBuilder = 
//        		new CombinationRecipe.Builder<CombinationRecipe<CombinationInv>>(EurekaRecipeCategory.COMBINATION, 
//        				new ResourceLocation(EurekaArcana.MODID + ":" + "combination_test"));
        CombinationRecipe.Builder<CombinationRecipe<CombinationRecipe.CombinationInv>> CombinationRecipeBuilder = 
        		CombinationRecipe.build(EurekaRecipeCategory.COMBINATION, new ResourceLocation(EurekaArcana.MODID + ":" + "combination_test"));
        CombinationRecipeBuilder.withIngredients(Ingredient.of(EurekaItemRegistry.BOOK_STRAP.get()), Ingredient.of(Items.BOOK))
        	.withSingleOutput(new ItemStack(EurekaItemRegistry.EUREKA_JOURNAL.get()), 1f)
        	.withCombinationTime(12)
        	.save(consumer);
    }
}