package com.github.ternityclockworks.eurekaarcana.datagen.recipes;

import com.github.ternityclockworks.eurekaarcana.EurekaArcana;
import com.github.ternityclockworks.eurekaarcana.server.item.EurekaItemRegistry;
import com.github.ternityclockworks.eurekaarcana.server.recipe.EurekaRecipeRegistry;

import net.minecraft.advancements.critereon.InventoryChangeTrigger;
import net.minecraft.advancements.critereon.ItemPredicate;
import net.minecraft.data.PackOutput;
import net.minecraft.data.recipes.*;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.item.Items;
import net.minecraftforge.common.Tags;

import java.util.function.Consumer;

public class CraftingRecipeGen extends EurekaRecipeProvider {

    public CraftingRecipeGen(PackOutput packOutput) {
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
        
        SpecialRecipeBuilder.special(EurekaRecipeRegistry.BOOK_STRAP_DYEING.getSerializer()).save(consumer, EurekaArcana.MODID + ":" + "book_strap_dyeing");
        SpecialRecipeBuilder.special(EurekaRecipeRegistry.JOURNAL_DYEING.getSerializer()).save(consumer, EurekaArcana.MODID + ":" + "journal_dyeing");
    }
}