package com.github.ternityclockworks.eurekaarcana.datagen;

import net.minecraft.core.HolderLookup;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.PackOutput;
import net.minecraft.data.loot.LootTableProvider;
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSets;
import net.minecraftforge.data.event.GatherDataEvent;

import java.util.Collections;
import java.util.List;
import java.util.concurrent.CompletableFuture;

import com.github.ternityclockworks.eurekaarcana.datagen.recipes.GenCraftingRecipes;

public class DataGeneration {

    public static void generate(GatherDataEvent event) {
        DataGenerator generator = event.getGenerator();
        PackOutput packOutput = generator.getPackOutput();
        CompletableFuture<HolderLookup.Provider> lookupProvider = event.getLookupProvider();

        //generator.addProvider(event.includeClient(), new EurekaBlockStates(packOutput, event.getExistingFileHelper()));
        generator.addProvider(event.includeClient(), new GenItemModels(packOutput, event.getExistingFileHelper()));
        generator.addProvider(event.includeClient(), new GenLanguageProvider(packOutput, "en_us"));
        
        GenBlockTags blockTags = new GenBlockTags(packOutput, lookupProvider, event.getExistingFileHelper());
        generator.addProvider(event.includeServer(), blockTags);
        generator.addProvider(event.includeServer(), new GenItemTags(packOutput, lookupProvider, blockTags, event.getExistingFileHelper()));
        generator.addProvider(event.includeServer(), new GenCraftingRecipes(packOutput));
        generator.addProvider(event.includeServer(), new LootTableProvider(packOutput, Collections.emptySet(),
                List.of(new LootTableProvider.SubProviderEntry(GenLootTables::new, LootContextParamSets.BLOCK))));
    }
}