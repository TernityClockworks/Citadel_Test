package com.github.ternityclockworks.eurekaarcana.datagen.recipes;

import com.github.ternityclockworks.eurekaarcana.EurekaArcana;
import com.github.ternityclockworks.eurekaarcana.server.item.EurekaItemRegistry;
import com.github.ternityclockworks.eurekaarcana.server.recipe.EurekaRecipeCategory;
import com.github.ternityclockworks.eurekaarcana.server.recipe.combination.CombinationRecipe;

import net.minecraft.advancements.critereon.InventoryChangeTrigger;
import net.minecraft.advancements.critereon.ItemPredicate;
import net.minecraft.data.PackOutput;
import net.minecraft.data.recipes.*;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraftforge.common.Tags;

import java.util.function.Consumer;

public class CraftingRecipeProvider extends EurekaRecipeProvider {
	
	private static final String path = EurekaArcana.MODID + ":";

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
        
        SpecialRecipeBuilder.special(EurekaRecipeCategory.BOOK_STRAP_DYEING.getSerializer()).save(consumer, path + "book_strap_dyeing");
        SpecialRecipeBuilder.special(EurekaRecipeCategory.JOURNAL_DYEING.getSerializer()).save(consumer, path + "journal_dyeing");
        
        CombinationRecipe.Builder<CombinationRecipe> JournalRecipeBuilder = 
        		CombinationRecipe.build(EurekaRecipeCategory.COMBINATION, toLocation("eureka_journal"));
        JournalRecipeBuilder.withIngredients(EurekaItemRegistry.BOOK_STRAP.get(), Items.BOOK)
        	.withSingleOutput(new ItemStack(EurekaItemRegistry.EUREKA_JOURNAL.get()))
        	.withCombinationTime(12)
        	.save(consumer);
        
        CombinationRecipe.Builder<CombinationRecipe> MechanismRecipeBuilder = 
        		CombinationRecipe.build(EurekaRecipeCategory.COMBINATION, toLocation("broken_mechanism"));
        MechanismRecipeBuilder.withIngredients(EurekaItemRegistry.BROKEN_MECHANISM.get(), Items.BOWL)
        	.addOutput(new ItemStack(Items.IRON_INGOT),4,0.1f,0.8f,0.1f)
        	.addOutput(new ItemStack(Items.DIAMOND),0.7f,0.2f,0.1f)
        	.withCombinationTime(12)
        	.save(consumer);
    }
    
    private static ResourceLocation toLocation(String id) {
    	return new ResourceLocation(path + id);
    }
}